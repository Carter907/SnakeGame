module com.snake.snakegame {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.snake.snakegame to javafx.fxml;
    exports com.snake.snakegame;
}