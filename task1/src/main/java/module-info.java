module pl.tiad.task1.frontend {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.datatransfer;
    requires java.desktop;
    requires jfreechart;
    requires javaluator;
    requires commons.math;

    opens pl.tiad.task1.frontend to javafx.fxml;
    exports pl.tiad.task1.frontend;
    exports pl.tiad.task1.backend.utils;
}