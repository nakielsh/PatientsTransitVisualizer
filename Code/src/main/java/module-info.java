module Code {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.graphics;

    exports ptv to javafx.graphics;
    exports ptv.controllers to javafx.fxml;
    exports ptv.views to javafx.fxml;
    opens ptv.controllers to javafx.fxml;
}