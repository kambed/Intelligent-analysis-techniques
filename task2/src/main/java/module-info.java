module pl.tiad.task2.frontend {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.datatransfer;
    requires java.desktop;
    requires jfreechart;
    requires javaluator;
    requires commons.math;

    opens pl.tiad.task2.frontend to javafx.fxml;
    exports pl.tiad.task2.frontend;
    exports pl.tiad.task2.backend.utils;
}