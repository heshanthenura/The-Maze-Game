module com.heshanthenura.mazevisualize {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.heshanthenura.mazevisualize to javafx.fxml;
    exports com.heshanthenura.mazevisualize;
}