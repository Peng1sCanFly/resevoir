
package projectcsi.reservoir;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class ReservationDAO {

    // Save a reservation under an individual account.
    public static boolean saveReservation(
            int accountId,
            Reservation reservation,
            LocalDate eventDate,
            LocalTime startTime,
            LocalTime endTime) throws SQLException {

        if (accountId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid account.");
        }

        if (eventDate == null
                || startTime == null
                || endTime == null
                || !endTime.isAfter(startTime)) {

            throw new IllegalArgumentException(
                    "End time must be after start time.");
        }

        if (reservation.getNumberOfTickets() <= 0
                || !Double.isFinite(
                        reservation.getPricePerTicket())
                || reservation.getPricePerTicket() < 0) {

            throw new IllegalArgumentException(
                    "Invalid ticket quantity or price.");
        }

        String sql = "INSERT INTO reservations "
                + "(account_id, event_name, location, "
                + "event_date, event_time, end_time, "
                + "number_of_tickets, price_per_ticket, "
                + "total_cost, section) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn =
                     DatabaseConnection.getConnection()) {

            conn.setAutoCommit(false);

            try {

                // Lock the account during reservation creation.
                String lockSql =
                        "SELECT account_id FROM accounts "
                        + "WHERE account_id = ? FOR UPDATE";

                try (PreparedStatement lock =
                             conn.prepareStatement(lockSql)) {

                    lock.setInt(1, accountId);

                    try (ResultSet rs = lock.executeQuery()) {

                        if (!rs.next()) {
                            conn.rollback();
                            return false;
                        }
                    }
                }

                // Check for conflicting reservations.
                if (hasOverlappingReservation(
                        conn,
                        accountId,
                        eventDate,
                        startTime,
                        endTime)) {

                    conn.rollback();
                    return false;
                }

                // Save the confirmed reservation.
                try (PreparedStatement stmt =
                             conn.prepareStatement(sql)) {

                    stmt.setInt(1, accountId);
                    stmt.setString(2,
                            reservation.getEventName());
                    stmt.setString(3,
                            reservation.getLocation());
                    stmt.setDate(4,
                            Date.valueOf(eventDate));
                    stmt.setTime(5,
                            Time.valueOf(startTime));
                    stmt.setTime(6,
                            Time.valueOf(endTime));
                    stmt.setInt(7,
                            reservation.getNumberOfTickets());

                    BigDecimal price = BigDecimal.valueOf(
                            reservation.getPricePerTicket())
                            .setScale(2, RoundingMode.HALF_UP);

                    BigDecimal total = price.multiply(
                            BigDecimal.valueOf(
                                    reservation.getNumberOfTickets()));

                    stmt.setBigDecimal(8, price);
                    stmt.setBigDecimal(9, total);

                    stmt.setString(10,
                            reservation.getSection() == null
                                    ? ""
                                    : reservation.getSection());

                    boolean saved =
                            stmt.executeUpdate() == 1;

                    conn.commit();

                    return saved;
                }

            } catch (SQLException e) {

                conn.rollback();

                // Duplicate reservation.
                if (e.getErrorCode() == 1062) {
                    return false;
                }

                throw e;

            } catch (RuntimeException e) {

                conn.rollback();
                throw e;
            }
        }
    }

    // Check for overlapping reservation times.
    private static boolean hasOverlappingReservation(
            Connection conn,
            int accountId,
            LocalDate eventDate,
            LocalTime startTime,
            LocalTime endTime) throws SQLException {

        String sql = "SELECT reservation_id "
                + "FROM reservations "
                + "WHERE account_id = ? "
                + "AND event_date = ? "
                + "AND event_time < ? "
                + "AND (end_time IS NULL OR end_time > ?) "
                + "FOR UPDATE";

        try (PreparedStatement stmt =
                     conn.prepareStatement(sql)) {

            stmt.setInt(1, accountId);
            stmt.setDate(2, Date.valueOf(eventDate));
            stmt.setTime(3, Time.valueOf(endTime));
            stmt.setTime(4, Time.valueOf(startTime));

            try (ResultSet rs = stmt.executeQuery()) {

                return rs.next();
            }
        }
    }

    // Retrieve reservations for an individual account.
    public static ArrayList<Reservation> getReservations(
            int accountId) throws SQLException {

        ArrayList<Reservation> reservations =
                new ArrayList<>();

        String sql = "SELECT * FROM reservations "
                + "WHERE account_id = ? "
                + "ORDER BY event_date, event_time";

        try (Connection conn =
                     DatabaseConnection.getConnection();
             PreparedStatement stmt =
                     conn.prepareStatement(sql)) {

            stmt.setInt(1, accountId);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {

                    Reservation reservation =
                            new Reservation(
                                    rs.getInt("reservation_id"),
                                    rs.getString("event_name"),
                                    rs.getString("location"),
                                    rs.getString("event_date"),
                                    rs.getString("event_time"),
                                    rs.getString("end_time"),
                                    rs.getInt("number_of_tickets"),
                                    rs.getDouble("price_per_ticket"),
                                    rs.getString("section")
                            );

                    reservations.add(reservation);
                }
            }
        }

        return reservations;
    }

    // Cancel a reservation belonging to the current account.
    public static boolean cancelReservation(
            int accountId,
            int reservationId) throws SQLException {

        String sql = "DELETE FROM reservations "
                + "WHERE reservation_id = ? "
                + "AND account_id = ?";

        try (Connection conn =
                     DatabaseConnection.getConnection();
             PreparedStatement stmt =
                     conn.prepareStatement(sql)) {

            stmt.setInt(1, reservationId);
            stmt.setInt(2, accountId);

            return stmt.executeUpdate() == 1;
        }
    }
}
