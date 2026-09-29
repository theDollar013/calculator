import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.HashMap;
import java.util.Map;

public class calculator extends Application {

    // Initializes display, first number, operator, and a boolean to start a new number
    private TextField disp;
    private boolean startNewNumber = true;
    private final Map<String, Button> buttons = new HashMap<>();
    private final CalculatorLogic log = new CalculatorLogic();

    @Override
    public void start(Stage stage) {

        // Setting up display
        disp = new TextField("0");
        disp.setEditable(false);
        disp.setFocusTraversable(false);
        disp.setAlignment(Pos.CENTER_RIGHT);
        disp.setId("display");
        disp.setPrefHeight(80);
        disp.setMaxWidth(Double.MAX_VALUE);

        // Setting up grid for buttons
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        // Allows button expansion with window expansion
        for (int i = 0; i < 4; i++) {

            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(25);
            column.setHgrow(Priority.ALWAYS);
            grid.getColumnConstraints().add(column);
        }

        // Button grid layout
        String[][] keys = {

                {"⌫", "C", "√", "/"},
                {"7", "8", "9", "*"},
                {"4", "5", "6", "-"},
                {"1", "2", "3", "+"},
                {"+/-", "0", ".", "="}
        };

        for (int row = 0; row < keys.length; row++) {

            for (int column = 0; column < keys[row].length; column++) {

                // Creates buttons, button size, styles, etc.
                String key = keys[row][column];
                Button button = new Button(key);
                buttons.put(key, button);
                button.setPrefSize(75, 65);
                button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

                // Assigns operator buttons their own class in .css file
                if (key.matches("[+\\-/*=]")) {

                    button.getStyleClass().add("operator-button");
                }

                // Assigns function buttons their own class in .css file
                if (key.equals("C") || key.equals("⌫") || key.equals("+/-") || key.equals("√")) {

                    button.getStyleClass().add("function-button");
                }

                button.setOnAction(event -> handleInput(key));
                grid.add(button, column, row);
            }
        }

        VBox root = new VBox(15, disp, grid);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 400, 500);

        // Links class to .css file for gui customization
        scene.getStylesheets().add(getClass().getResource("/calculator.css").toExternalForm());

        // Handles Enter key presses as "=" rather than selecting a button
        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {

            if (event.getCode() == KeyCode.ENTER) {

                handleInput("=");
                pressButton("=");
                event.consume();
            }
        });

        scene.addEventFilter(KeyEvent.KEY_RELEASED, event -> {

            if (event.getCode() == KeyCode.ENTER) {

                Button button = buttons.get("=");

                if (button != null) {

                    button.getStyleClass().remove("keyboard-pressed");
                }

                event.consume();
            }
        });

        // Adds functionality for keyboard input while giving the button a "pressed" look when pressed
        scene.setOnKeyPressed(event -> {

                String key = event.getText();
                String visKey = key;

                if (key.matches("[0-9]")) {

                    handleInput(key);
                    pressButton(key);
                }

                else if (key.equals(".")) {

                    handleInput(key);
                    pressButton(key);
                }

                else if (key.matches("[+\\-*/]")) {

                    handleInput(key);
                    pressButton(key);
                }

                else if (event.getCode() == KeyCode.BACK_SPACE) {

                    handleInput("⌫");
                    pressButton("⌫");
                }

                else if (event.getCode() == KeyCode.ESCAPE) {

                    handleInput("C");
                    pressButton("C");
                }
        });

        // Removes the "pressed" look when released
        scene.setOnKeyReleased(event -> {

            String key = event.getText();
            Button button = buttons.get(key);
            String visKey = key;

            if (button != null) {

                button.getStyleClass().remove("keyboard-pressed");
            }

            else if (event.getCode() == KeyCode.BACK_SPACE) {

                visKey = "⌫";
            }

            else if (event.getCode() == KeyCode.ESCAPE) {

                visKey = "C";
            }

            button = buttons.get(visKey);

            if (button != null) {

                button.getStyleClass().remove("keyboard-pressed");
            }
        });

        stage.setTitle("Calculator");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setMinHeight(400);
        stage.setMinWidth(350);
        stage.show();
    }

    private void handleInput(String input) {

        // This was originally meant to convert X to * for Java to recognize as multiplication
        // X didn't look good in GUI, so reverted back to *
        // However, this line ensures selOp is initialized for compiling
        String selOp = input.equals("X") ? "*" : input;

        // If any number is pressed
        if (input.matches("[0-9]")) {

            if (startNewNumber || disp.getText().equals("0")) {

                disp.setText(input);
            }

            else {

                disp.setText(disp.getText() + input);
            }

            startNewNumber = false;
        }

        // If . is pressed for decimal usage
        else if (input.equals(".")) {

            // If an operator has already been selected, ensures the decimal is used on the second number
            if (startNewNumber) {

                disp.setText("0.");
                startNewNumber = false;
            }

            // Verifies that there isn't already a decimal present
            // If there is already a decimal present, nothing happens
            else if (!disp.getText().contains(".")) {

                disp.setText(disp.getText() + ".");

            }
        }

        // If operator is pressed
        else if (input.matches("[+\\-*/]")) {

            String op = input;

            // If there is already a queued operation, calculate it first
            if (log.hasOperation() && !startNewNumber) {

                double secondNumber = Double.parseDouble(disp.getText());
                double result = log.calcWith(secondNumber);
                disp.setText(log.formatResult(result));
                log.setOperation(result, op);
            }

            else {

                double currNum = Double.parseDouble(disp.getText());
                log.setOperation(currNum, op);
            }

            startNewNumber = true;
        }

        // If backspace button is pushed
        else if (input.equals("⌫")) {

            if (!startNewNumber) {

                String currTxt = disp.getText();

                if (currTxt.length() > 1) {

                    disp.setText(currTxt.substring(0, currTxt.length() - 1));
                }

                else {

                    disp.setText("0");
                }
            }
        }

        // If +/- button is pushed (neg-to-pos or pos-to-neg)
        else if (input.equals("+/-")) {

            double currNum = Double.parseDouble(disp.getText());

            // Prevents displaying -0
            if (currNum != 0) {
                currNum = currNum * -1;
                disp.setText(log.formatResult(currNum));
            }
        }

        // If square root button is pressed
        else if (input.equals("√")) {

            double num = Double.parseDouble(disp.getText());

            // SqRts of neg numbers undefined
            if (num < 0) {

                disp.setText("Undefined");
            }

            else {

                num = log.sqrt(num);
                disp.setText(log.formatResult(num));
            }
        }

        // If equals button is pressed
        else if (input.equals("=")) {

            if (log.hasOperation() && !startNewNumber) {

                double secondNumber = Double.parseDouble(disp.getText());

                try {

                    double result = log.calcWith(secondNumber);
                    disp.setText(log.formatResult(result));
                }

                // User attempted to divide by 0
                catch (ArithmeticException e) {
                    disp.setText(e.getMessage());
                }

                log.clearOperation();
                startNewNumber = true;
            }
        }

        // If clear button is pressed
        else if (input.equals("C")) {

            disp.setText("0");
            log.clearOperation();
            startNewNumber = true;
        }
    }

    private void pressButton(String key) {

        Button button = buttons.get(key);

        if (button != null) {

            button.getStyleClass().add("keyboard-pressed");
        }
    }

    public static void main(String[] args) {

        launch(args);
    }
}

