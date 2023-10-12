module cs349.a3battleship {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;


    opens cs349.a3battleship to javafx.fxml;
    exports cs349.a3battleship;
}