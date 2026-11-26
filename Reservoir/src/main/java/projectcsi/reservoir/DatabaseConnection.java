
package projectcsi.reservoir;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;

public class DatabaseConnection {

    private static final Path DATABASE_DIRECTORY =
            Paths.get(System.getProperty("user.home"), ".reservoir");

    private static final Path DATABASE_FILE =
            DATABASE_DIRECTORY.resolve("reservoir.db");

    private static final String URL =
            "jdbc:sqlite:" + DATABASE_FILE.toAbsolutePath();

    public static Connection getConnection() throws SQLException {

        try {
            Files.createDirectories(DATABASE_DIRECTORY);
        } catch (IOException e) {
            throw new SQLException(
                    "Could not create database directory.", e);
        }

        Connection connection = DriverManager.getConnection(URL);

        try {
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON");
                stmt.execute("PRAGMA busy_timeout = 5000");
            }

            initializeDatabase(connection);

            return connection;

        } catch (SQLException e) {
            connection.close();
            throw e;
        }
    }

    private static void initializeDatabase(Connection connection)
            throws SQLException {

        try (Statement stmt = connection.createStatement()) {

            // Create accounts table.
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS accounts ("
                    + "account_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "first_name TEXT NOT NULL, "
                    + "last_name TEXT NOT NULL, "
                    + "email TEXT NOT NULL UNIQUE, "
                    + "password_hash TEXT NOT NULL, "
                    + "phone_number TEXT, "
                    + "billing_address TEXT, "
                    + "created_at TEXT DEFAULT CURRENT_TIMESTAMP"
                    + ")"
            );

            // Create reservations table.
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS reservations ("
                    + "reservation_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "account_id INTEGER NOT NULL, "
                    + "event_name TEXT NOT NULL, "
                    + "location TEXT NOT NULL, "
                    + "event_date TEXT NOT NULL, "
                    + "event_time TEXT NOT NULL, "
                    + "end_time TEXT, "
                    + "number_of_tickets INTEGER NOT NULL, "
                    + "price_per_ticket NUMERIC NOT NULL, "
                    + "total_cost NUMERIC NOT NULL, "
                    + "section TEXT NOT NULL DEFAULT '', "
                    + "created_at TEXT DEFAULT CURRENT_TIMESTAMP, "
                    + "FOREIGN KEY (account_id) "
                    + "REFERENCES accounts(account_id) ON DELETE CASCADE, "
                    + "UNIQUE (account_id, event_name, location, "
                    + "event_date, event_time, section)"
                    + ")"
            );

            // Create payment methods table.
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS payment_methods ("
                    + "payment_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "account_id INTEGER NOT NULL, "
                    + "cardholder_name TEXT NOT NULL, "
                    + "card_type TEXT NOT NULL, "
                    + "last_four TEXT NOT NULL, "
                    + "expiration_month INTEGER NOT NULL, "
                    + "expiration_year INTEGER NOT NULL, "
                    + "payment_token TEXT NOT NULL UNIQUE, "
                    + "billing_address TEXT, "
                    + "FOREIGN KEY (account_id) "
                    + "REFERENCES accounts(account_id) ON DELETE CASCADE"
                    + ")"
            );
        }
    }

    public static void main(String[] args) {

        try (Connection connection = getConnection()) {

            System.out.println(
                    "Successfully connected to Reservoir SQLite database!"
            );

            System.out.println(
                    "Database location: " + DATABASE_FILE.toAbsolutePath()
            );

            System.out.println(
                    "All database tables initialized successfully!"
            );

        } catch (SQLException e) {

            System.out.println(
                    "Database connection failed: " + e.getMessage()
            );

            e.printStackTrace();
        }
    }
}
