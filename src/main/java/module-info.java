module com.heshanthenura.maze {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;


    opens com.heshanthenura.maze to javafx.fxml;
    exports com.heshanthenura.maze;
}