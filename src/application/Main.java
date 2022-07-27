package application;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Random;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {
	public static final double screenWidth = 514.0D;

	public static final double screenHeight = 501.0D;

	public static Square[][] squareMatrix = new Square[25][25];

	public static boolean w_Pressed = false;

	public static boolean s_Pressed = false;

	public static boolean a_Pressed = false;

	public static boolean d_Pressed = false;

	public static boolean eat_Food = false;

	public static IntegerProperty score = (IntegerProperty) new SimpleIntegerProperty(0);

	public static int highScore = 0;

	public static final int startingX = 5;

	public static final int startingY = 5;

	public static int startValue1 = 15;

	public static int startValue2 = 2;

	public static Random ran = new Random();

	public static int foodX = ran.nextInt(1, 20);

	public static int foodY = ran.nextInt(1, 17);

	public static boolean death = false;

	public static int deathCount = 0;

	public static boolean reset = false;

	public void start(final Stage primaryStage) {
		Pane root = new Pane();
		root.maxHeight(500.0D);
		root.maxWidth(500.0D);
		root.setLayoutX(-27.0D);
		root.setLayoutY(-29.0D);
		GridPane plane = new GridPane();
		plane.setHgap(2.0D);
		plane.setVgap(2.0D);
		plane.setAlignment(Pos.CENTER);
		plane.setStyle("-fx-background-color: black");
		
		final Square[][] squareMatrix = new Square[20][21];
		for (int i = 0; i < squareMatrix.length; i++) {
			for (int j = 0; j < (squareMatrix[i]).length; j++) {
				squareMatrix[i][j] = new Square(j, i, false, false);
				plane.add((Node) squareMatrix[i][j], j, i);
			}
		}
		final Deque<Square> snake = new LinkedList<>();
		Thread tr = new Thread((Runnable) new Task<Integer>() {
			protected Integer call() throws Exception {
				while (true) {
					Thread.sleep(100L);
					Platform.runLater(() -> {
						if (reset) {
							plane.getChildren().removeAll();
							for (int i = 0; i < squareMatrix.length; i++) {
								for (int j = 0; j < (squareMatrix[i]).length; j++) {
									squareMatrix[i][j] = new Square(j, i, false, false);
									plane.add(squareMatrix[i][j], j, i);
								}
							}
							snake.clear();
							Main.startValue1 = 5;
							Main.startValue2 = 5;
							snake.offerFirst(squareMatrix[startValue1][startValue2]);
							Main.foodX = Main.ran.nextInt(1, 20);
							Main.foodY = Main.ran.nextInt(1, 17);
							squareMatrix[foodY][foodX].setFood(true);
							Main.resetKeys();
							Main.highScore = (Main.score.getValue().intValue() > Main.highScore)
									? Main.score.getValue().intValue()
									: Main.highScore;
							Main.score.setValue(Integer.valueOf(0));
							Main.death = false;
							Main.deathCount = 0;
							Main.reset = false;
						}
						if (!death) {
							if (snake.size() > score.getValue() + 1)
								snake.pollLast().setAlive(false);
							snake.element().setAlive(true);
							Main.eat_Food = squareMatrix[Main.startValue1][Main.startValue2].isFood();
							if (Main.eat_Food) {
								squareMatrix[Main.startValue1][Main.startValue2].setFood(false);
								do {
									Main.foodY = Main.ran.nextInt(1, 17);
									Main.foodX = Main.ran.nextInt(1, 20);
								} while (squareMatrix[Main.foodY][Main.foodX].isAlive());
								squareMatrix[Main.foodY][Main.foodX].setFood(true);
								Main.score.setValue(Integer.valueOf(Main.score.getValue().intValue() + 1));
							}
							if (Main.deathCount > 0)
								Main.death = true;
							if (Main.startValue1 > 0 && Main.startValue1 <= 17 && Main.w_Pressed) {
								snake.offerFirst(
										squareMatrix[Integer.valueOf(--Main.startValue1).intValue()][Integer
												.valueOf(Main.startValue2).intValue()]);
								if (squareMatrix[Integer.valueOf(Main.startValue1).intValue()][Integer
										.valueOf(Main.startValue2).intValue()].isAlive() || Main.startValue1 == 0)
									Main.deathCount++;
							} else if (Main.startValue1 >= 0 && Main.startValue1 <= 17 && Main.s_Pressed) {
								snake.offerFirst(
										squareMatrix[Integer.valueOf(++Main.startValue1).intValue()][Integer
												.valueOf(Main.startValue2).intValue()]);
								if (squareMatrix[Integer.valueOf(Main.startValue1).intValue()][Integer
										.valueOf(Main.startValue2).intValue()].isAlive() || Main.startValue1 == 18)
									Main.deathCount++;
							} else if (Main.startValue2 > 0 && Main.startValue2 <= 19 && Main.a_Pressed) {
								snake.offerFirst(
										squareMatrix[Integer.valueOf(Main.startValue1).intValue()][Integer
												.valueOf(--Main.startValue2).intValue()]);
								if (squareMatrix[Integer.valueOf(Main.startValue1).intValue()][Integer
										.valueOf(Main.startValue2).intValue()].isAlive() || Main.startValue2 == 0)
									Main.deathCount++;
							} else if (Main.startValue2 >= 0 && Main.startValue2 <= 19 && Main.d_Pressed) {
								snake.offerFirst(
										squareMatrix[Integer.valueOf(Main.startValue1).intValue()][Integer
												.valueOf(++Main.startValue2).intValue()]);
								if (squareMatrix[Integer.valueOf(Main.startValue1).intValue()][Integer
										.valueOf(Main.startValue2).intValue()].isAlive() || Main.startValue2 == 20)
									Main.deathCount++;
							}
						} else if (snake.size() > 0) {
							((Square) snake.pollLast()).setAlive(false);
						}
					});
				}
			}
		});
		tr.setDaemon(true);
		tr.start();
		plane.setOnKeyPressed(g -> {
			switch (g.getCode()) {
			case W:
				w_Pressed = true;
				s_Pressed = false;
				a_Pressed = false;
				d_Pressed = false;
				break;
			case S:
				w_Pressed = false;
				s_Pressed = true;
				a_Pressed = false;
				d_Pressed = false;
				break;
			case A:
				w_Pressed = false;
				s_Pressed = false;
				a_Pressed = true;
				d_Pressed = false;
				break;
			case D:
				w_Pressed = false;
				s_Pressed = false;
				a_Pressed = false;
				d_Pressed = true;
				break;
			case R:
				reset = true;
				break;
			}
		});
		score.addListener(new InvalidationListener() {
			public void invalidated(Observable arg0) {
				primaryStage
						.setTitle("Snake     score: " + Main.score.getValue() + "       highscore: " + Main.highScore);
			}
		});
		squareMatrix[startValue1][startValue2].setAlive(true);
		snake.offerFirst(squareMatrix[startValue1][startValue2]);
		squareMatrix[foodY][foodX].setFood(true);
		
		
		root.getChildren().add(plane);
		Scene scene = new Scene(root, 514.0D, 501.0D);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setTitle("Snake     score: " + score.getValue() + "       highscore: " + highScore);
		primaryStage.setResizable(true);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void resetKeys() {
		w_Pressed = false;
		s_Pressed = false;
		a_Pressed = false;
		d_Pressed = false;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
