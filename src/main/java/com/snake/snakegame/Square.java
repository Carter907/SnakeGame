package com.snake.snakegame;


import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Button;

public class Square extends Button {
    private BooleanProperty aliveProperty;

    private BooleanProperty foodProperty;

    private int x;

    private int y;

    public Square(int x, int y, boolean alive, boolean Food) {
        this.aliveProperty = (BooleanProperty)new SimpleBooleanProperty(alive);
        this.foodProperty = (BooleanProperty)new SimpleBooleanProperty(Food);
        this.x = x;
        this.y = y;
        setText("  ");
        setStyle("-fx-background-color: black; -fx-border-color: grey; -fx-border-width: 1 1 1 1; -fx-background-insets: 0;");
        this.aliveProperty.addListener(new InvalidationListener() {
            public void invalidated(Observable ob) {
                if (Square.this.aliveProperty.getValue().booleanValue()) {
                    Square.this.setStyle("-fx-background-color: cyan; -fx-border-color: blue; -fx-border-width: 1 1 1 1; -fx-background-insets: 0;");
                } else {
                    Square.this.setStyle("-fx-background-color: black; -fx-border-color: grey; -fx-border-width: 1 1 1 1; -fx-background-insets: 0;");
                }
            }
        });
        this.foodProperty.addListener(new InvalidationListener() {
            public void invalidated(Observable ob) {
                if (Square.this.foodProperty.getValue().booleanValue()) {
                    Square.this.setStyle("-fx-background-color: red; -fx-border-color: red; -fx-border-width: 1 1 1 1; -fx-background-insets: 0;");
                } else {
                    Square.this.setStyle("-fx-background-color: cyan; -fx-border-color: blue; -fx-border-width: 1 1 1 1; -fx-background-insets: 0;");
                }
            }
        });
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void setAlive(boolean b) {
        getAliveProperty().setValue(Boolean.valueOf(b));
    }

    public boolean isAlive() {
        return getAliveProperty().getValue().booleanValue();
    }

    public void setFood(boolean b) {
        getFoodProperty().setValue(Boolean.valueOf(b));
    }

    public boolean isFood() {
        return getFoodProperty().getValue().booleanValue();
    }

    private BooleanProperty getFoodProperty() {
        return this.foodProperty;
    }

    private BooleanProperty getAliveProperty() {
        return this.aliveProperty;
    }

    public String toString() {
        return "x -> " + this.x + " y -> " + this.y;
    }
}