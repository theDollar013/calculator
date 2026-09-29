import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class calculator extends Application {

    // Initializes display, first number, operator, and a boolean to start a new number
    private TextField disp;
    private String op = "";
    private double firstNumber;
    private boolean startNewNumber = true;

    @Override
    public void start(Stage stage) {

        // Setting up display

        disp = new TextField("0");
        disp.setEditable(false);
        disp.setAlignment(Pos.CENTER_RIGHT);
        disp.setStyle("-fx-font-size: 28px;");

        // Setting up grid for buttons
        GridPane grid = new GridPane();
        grid.setHgap(8);
        grid.setVgap(8);
        grid.setAlignment(Pos.CENTER);
        String[][] keys = {

                {"⌫", "C", "", "/"},
                {"7", "8", "9", "*"},
                {"4", "5", "6", "-"},
                {"1", "2", "3", "+"},
                {"+/-", "0", ".", "="}
        };

        for (int row = 0; row < keys.length; row++) {

            for (int column = 0; column < keys[row].length; column++) {

                String key = keys[row][column];
                Button button = new Button(key);
                button.setStyle("-fx-font-size: 20px;");
                button.setOnAction(event -> handleInput(key));
                grid.add(button, column, row);
            }
        }

        VBox root = new VBox(12, disp, grid);
        root.setPadding(new Insets(15));
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 320, 350);

        // Handles Enter key presses as "=" rather than selecting a button
        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {

            if (event.getCode() == KeyCode.ENTER) {

                handleInput("=");
                event.consume();
            }
        });

        // Adds functionality for keyboard input
        scene.setOnKeyPressed(event -> {

                String key = event.getText();

                if (key.matches("[0-9]")) {

                    handleInput(key);
                }

                else if (key.equals(".")) {

                    handleInput(key);
                }

                else if (key.matches("[+\\-*/]")) {

                    handleInput(key);
                }

                else if (event.getCode() == KeyCode.BACK_SPACE) {

                    handleInput("⌫");
                }

                else if (event.getCode() == KeyCode.ESCAPE) {

                    handleInput("C");
                }
        });

        stage.setTitle("Calculator");
        stage.setScene(scene);
        stage.setResizable(false);
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

            // If there is already a queued operation, calculate it first
            if (!op.isEmpty() && !startNewNumber) {

                double secondNumber = Double.parseDouble(disp.getText());

                // Ensures there is no attempt to divide by zero
                if (op.equals("/") && secondNumber == 0) {

                    disp.setText("Unable to divide by 0");
                    op = "";
                    startNewNumber = true;
                    return;
                }

                firstNumber = calculate(firstNumber, secondNumber, op);
                disp.setText(formatResult(firstNumber));
            }

            else if (op.isEmpty()) {

                firstNumber = Double.parseDouble(disp.getText());
            }

            // Stores the selected operator
            op = selOp;
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
                disp.setText(formatResult(currNum));
            }
        }

        // If equals button is pressed
        else if (input.equals("=")) {

            if (!op.isEmpty() && !startNewNumber) {

                double secondNumber = Double.parseDouble(disp.getText());

                // Double protection against dividing by zero
                if (op.equals("/") && secondNumber == 0) {

                    disp.setText("Unable to divide by 0");
                    op = "";
                    startNewNumber = true;
                    return;
                }

                double result = calculate(firstNumber, secondNumber, op);
                disp.setText(formatResult(result));
            }

            else if (!op.isEmpty()) {

                firstNumber = Double.parseDouble(disp.getText());
            }

            // Stores the selected operator
            op = selOp;
            startNewNumber = true;
        }

        // If clear button is pressed
        else if (input.equals("C")) {

            disp.setText("0");
            firstNumber = 0;
            op = "";
            startNewNumber = true;
        }
    }

    private double calculate(double a, double b, String op) {

        switch (op) {

            case "+":
                return a + b;

            case "-":
                return a - b;

            case "*":
                return a * b;

            case "/":
                return a / b;

            default:
                return b;
        }
    }

    private String formatResult(double result) {

        if (result == (long) result) {

            return Long.toString((long) result);
        }

        return Double.toString(result);
    }

    public static void main(String[] args) {

        launch(args);
    }
}

