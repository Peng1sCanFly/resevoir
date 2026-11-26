
module projectcsi.reservoir {

    requires javafx.controls;
    requires javafx.web;
    requires java.net.http;

    // Allows the application to read Photon JSON responses.
    requires com.fasterxml.jackson.databind;

    // SQLite database connectivity.
    requires java.sql;
    requires org.xerial.sqlitejdbc;

    exports projectcsi.reservoir;

    opens projectcsi.reservoir to javafx.web;
}
