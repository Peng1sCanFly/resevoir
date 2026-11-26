
package projectcsi.reservoir;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class ReservationDAO {

    private static final DateTimeFormatter TIME_FORMAT =
            DateTimeFormatter.ofPattern("HH:mm:ss");

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

        String date = eventDate.toString();
        String start = startTime.format(TIME_FORMAT);
        String end = endTime.format(TIME_FORMAT);

        if (end.compareTo(start) <= 0) {
            throw new IllegalArgumentException(
                    "Invalid reservation time range.");
        }

        // Check account existence and overlapping reservations
        // while inserting in one atomic SQLite statement.
        String sql = "INSERT INTO reservations "
                + "(account_id, event_name, location, "
                + "event_date, event_time, end_time, "
                + "number_of_tickets, price_per_ticket, "
                + "total_cost, section) "
                + "SELECT ?, ?, ?, ?, ?, ?, ?, ?, ?, ? "
                + "WHERE EXISTS ("
                + "SELECT 1 FROM accounts "
                + "WHERE account_id = ?) "
                + "AND NOT EXISTS ("
                + "SELECT 1 FROM reservations "
                + "WHERE account_id = ? "
                + "AND event_date = ? "
                + "AND event_time < ? "
                + "AND (end_time IS NULL OR end_time > ?)) "
                + "ON CONFLICT(account_id, event_name, location, "
                + "event_date, event_time, section) DO NOTHING";

        try (Connection conn =
                     DatabaseConnection.getConnection();
             PreparedStatement stmt =
                     conn.prepareStatement(sql)) {

            stmt.setInt(1, accountId);

            stmt.setString(2,
                    reservation.getEventName());

            stmt.setString(3,
                    reservation.getLocation());

            stmt.setString(4, date);
            stmt.setString(5, start);
            stmt.setString(6, end);

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

            // Verify that the account exists.
            stmt.setInt(11, accountId);

            // Check for overlapping reservations.
            stmt.setInt(12, accountId);
            stmt.setString(13, date);
            stmt.setString(14, end);
            stmt.setString(15, start);

            // Returns false if a reservation conflicts
            // or the account does not exist.
            return stmt.executeUpdate() == 1;
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
