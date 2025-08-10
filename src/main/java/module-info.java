module lk.ijse.javafx.drivemax {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires static lombok;
    requires java.desktop;
    requires net.sf.jasperreports.core;
    requires java.mail;


    opens lk.ijse.javafx.drivemax.controller to javafx.fxml;
    opens lk.ijse.javafx.drivemax.dto.tm to javafx.base;


    exports lk.ijse.javafx.drivemax;
}