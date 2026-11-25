module projectcsi.reservoir {

    requires javafx.controls;
    requires javafx.web;
    requires java.net.http;

    // Allows the application to read Photon JSON responses.
    requires com.fasterxml.jackson.databind;

    // MySQL database connectivity.
    requires java.sql;

    exports projectcsi.reservoir;

    opens projectcsi.reservoir to javafx.web;
}