
package projectcsi.reservoir;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

public class AccountDAO {

    // Register a new account.
    public static boolean createAccount(
            String firstName,
            String lastName,
            String email,
            String password) throws SQLException {

        String sql = "INSERT INTO accounts "
                + "(first_name, last_name, email, password_hash) "
                + "VALUES (?, ?, ?, ?) "
                + "ON CONFLICT(email) DO NOTHING";

        String passwordHash =
                PasswordUtil.hashPassword(password);

        try (Connection conn =
                     DatabaseConnection.getConnection();
             PreparedStatement stmt =
                     conn.prepareStatement(sql)) {

            stmt.setString(1, firstName.trim());
            stmt.setString(2, lastName.trim());
            stmt.setString(3, normalizeEmail(email));
            stmt.setString(4, passwordHash);

            // Returns false if the email already exists.
            return stmt.executeUpdate() == 1;
        }
    }

    // Verify login credentials.
    public static Account login(
            String email,
            String password) throws SQLException {

        String sql = "SELECT * FROM accounts WHERE email = ?";

        try (Connection conn =
                     DatabaseConnection.getConnection();
             PreparedStatement stmt =
                     conn.prepareStatement(sql)) {

            stmt.setString(1, normalizeEmail(email));

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {

                    String storedHash =
                            rs.getString("password_hash");

                    if (PasswordUtil.verifyPassword(
                            password, storedHash)) {

                        return buildAccount(rs);
                    }
                }
            }
        }

        return null;
    }

    // Retrieve account information.
    public static Account getAccountById(
            int accountId) throws SQLException {

        String sql = "SELECT * FROM accounts "
                + "WHERE account_id = ?";

        try (Connection conn =
                     DatabaseConnection.getConnection();
             PreparedStatement stmt =
                     conn.prepareStatement(sql)) {

            stmt.setInt(1, accountId);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return buildAccount(rs);
                }
            }
        }

        return null;
    }

    // Update profile information for checkout autofill.
    public static boolean updateProfile(
            int accountId,
            String phoneNumber,
            String billingAddress) throws SQLException {

        String sql = "UPDATE accounts SET "
                + "phone_number = ?, billing_address = ? "
                + "WHERE account_id = ?";

        try (Connection conn =
                     DatabaseConnection.getConnection();
             PreparedStatement stmt =
                     conn.prepareStatement(sql)) {

            stmt.setString(1, phoneNumber);
            stmt.setString(2, billingAddress);
            stmt.setInt(3, accountId);

            return stmt.executeUpdate() == 1;
        }
    }

    // Convert database information into an Account object.
    private static Account buildAccount(
            ResultSet rs) throws SQLException {

        return new Account(
                rs.getInt("account_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email"),
                rs.getString("phone_number"),
                rs.getString("billing_address")
        );
    }

    // Keep email formatting consistent.
    private static String normalizeEmail(String email) {

        return email.trim().toLowerCase(Locale.ROOT);
    }
}
