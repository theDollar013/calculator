import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
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

        // Setting up grid pattern for buttons
        GridPane grid = new GridPane();
        grid.setHgap(8);
        grid.setVgap(8);
        grid.setAlignment(Pos.CENTER);
        String[][] keys = {
                {"7", "8", "9", "/"},
                {"4", "5", "6", "*"},
                {"1", "2", "3", "-"},
                {"C", "0", "=", "+"}
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

        stage.setTitle("Calculator");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    private void handleInput(String input) {

        // Converts X to * for Java to recognize as multiplication
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

