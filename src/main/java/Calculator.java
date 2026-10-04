import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.HashMap;
import java.util.Map;

public class Calculator extends Application {

    // Initializes display, first number, operator, and a boolean to start a new number
    private TextField disp;
    private TextField histDisp;
    private boolean startNewNumber = true;
    private String expStart;
    private final Map<String, Button> buttons = new HashMap<>();
    private final Logic log = new Logic();

    @Override
    public void start(Stage stage) {

        // Sets up display
        disp = new TextField("0");
        disp.setEditable(false);
        disp.setFocusTraversable(false);
        disp.setAlignment(Pos.CENTER_RIGHT);
        disp.setId("display");
        disp.setPrefHeight(80);
        disp.setMaxWidth(Double.MAX_VALUE);

        // Sets up display for calculation history
        histDisp = new TextField();
        histDisp.setEditable(false);
        histDisp.setFocusTraversable(false);
        histDisp.setAlignment(Pos.CENTER_RIGHT);
        histDisp.setId("history-display");

        // Sets up grid for buttons
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

        for (int i = 0; i < 6; i++) {

            RowConstraints row = new RowConstraints();
            row.setPercentHeight(18);
            row.setVgrow(Priority.ALWAYS);
            grid.getRowConstraints().add(row);
        }

        // Button grid layout
        String[][] keys = {

                {"", "C", "√", "⌫"},
                {"", "", "%", "/"},
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
                button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

                // Assigns operator buttons their own class in .css file
                if (key.matches("[+\\-/*=]")) {

                    button.getStyleClass().add("operator-button");
                }

                // Assigns function buttons their own class in .css file
                if (key.equals("C") || key.equals("⌫")
                        || key.equals("%") || key.equals("√")) {

                    button.getStyleClass().add("function-button");
                }

                button.setOnAction(event -> handleInput(key));
                grid.add(button, column, row);
            }
        }

        VBox root = new VBox(15, histDisp, disp, grid);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        VBox.setVgrow(grid, Priority.ALWAYS);

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

        // If any number is pressed
        if (input.matches("[0-9]")) {

            handleNumber(input);
        }

        // If . is pressed for decimal usage
        else if (input.equals(".")) {

            handleDecimal();
        }

        // If operator is pressed
        else if (input.matches("[+\\-*/]")) {

            handleOperator(input);
        }

        // If backspace button is pushed
        else if (input.equals("⌫")) {

            handleBackspace();
        }

        // If +/- button is pushed (neg-to-pos or pos-to-neg)
        else if (input.equals("+/-")) {

            handleSignChange();
        }

        // If square root button is pressed
        else if (input.equals("√")) {

            handleSqRt();
        }

        // If equals button is pressed
        else if (input.equals("=")) {

            handleEquals();
        }

        // If clear button is pressed
        else if (input.equals("C")) {

            handleClear();
        }

        // If % button is pressed
        else if (input.equals("%")) {

            handlePercent();
        }
    }

    private void handleNumber(String input) {

        if (startNewNumber) {

            disp.setText(input);
            startNewNumber = false;
        }

        else {

            disp.setText(disp.getText() + input);
        }

        updateExpHist();
    }

    private void handleDecimal() {

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

        updateExpHist();
    }

    private void handleOperator(String input) {

        // If there is already a queued operation, calculate it first
        if (log.hasOperation() && !startNewNumber) {

            String secNumStr = disp.getText();
            double secondNumber = Double.parseDouble(secNumStr);
            expStart = expStart + " " + secNumStr + " " + input;
            histDisp.setText(expStart);
            double result = log.calcWith(secondNumber);
            String formResult = log.formatResult(result);
            disp.setText(formResult);
            log.setOperation(result, input);
        }

        else {

            String firstNum = disp.getText();
            expStart = firstNum + " " + input;
            histDisp.setText(expStart);
            double currNum = Double.parseDouble(firstNum);
            log.setOperation(currNum, input);
        }

        startNewNumber = true;
    }

    private void handleBackspace() {

        if (!startNewNumber) {

            String currTxt = disp.getText();

            if (currTxt.length() > 1) {

                disp.setText(currTxt.substring(0, currTxt.length() - 1));

                updateExpHist();
            }

            else {

                disp.setText("0");
            }
        }
    }

    private void handleSignChange() {

        // Protects against sign change while waiting on number input
        if (!startNewNumber) {

            double currNum = Double.parseDouble(disp.getText());

            // Prevents displaying -0
            if (currNum != 0) {
                currNum = currNum * -1;
                String formNum = log.formatResult(currNum);
                disp.setText(formNum);

                // If sign change is part of an equation, displays sign change in calc. history
                if (log.hasOperation()) {

                    histDisp.setText(expStart + " " + formNum);
                }

            }
        }
    }

    private void handleSqRt() {

        double num = Double.parseDouble(disp.getText());
        String origNum = disp.getText();

        try {
            num = log.sqrt(num);
            String formResult = log.formatResult(num);

            // Square roots part of an equation
            if (log.hasOperation()) {

                histDisp.setText(expStart + " √" + origNum);
                disp.setText(formResult);
                startNewNumber = false;
            }

            // Standalone square roots
            else {

                histDisp.setText("√" + origNum + " = " + formResult);
                disp.setText(formResult);
                startNewNumber = true;
            }
        }

        catch (ArithmeticException e) {

            disp.setText(e.getMessage());
        }
    }

    private void handleEquals() {

        if (log.hasOperation() && !startNewNumber) {

            double secondNumber = Double.parseDouble(disp.getText());

            try {

                double result = log.calcWith(secondNumber);
                disp.setText(log.formatResult(result));
                String formResult = log.formatResult(result);
                disp.setText(formResult);
                histDisp.setText(histDisp.getText() + " = " + formResult);
            }

            // User attempted to divide by 0
            catch (ArithmeticException e) {
                disp.setText(e.getMessage());
            }

            log.clearOperation();
            startNewNumber = true;
        }
    }

    private void handleClear() {

        disp.setText("0");
        log.clearOperation();
        histDisp.clear();
        expStart = "";
        startNewNumber = true;
    }

    private void handlePercent() {

        double num = Double.parseDouble(disp.getText());
        double result;
        String formResult;

        // Percentage part of an equation (like calculating totals + tip)
        if (log.hasOperation()) {

            result = log.calculatePctage(num);
            formResult = log.formatResult(result);
            histDisp.setText(expStart + " " + num + "%");
            disp.setText(formResult);
            startNewNumber = false;
        }

        // Standalone percentage
        else {

            result  = log.pctage(num);
            formResult = log.formatResult(result);
            histDisp.setText(num + "% = " + formResult);
            disp.setText(formResult);
            startNewNumber = true;
        }
    }

    private void updateExpHist() {
        // Handles numbers in the calculation history box

        if (log.hasOperation()) {

            histDisp.setText(expStart + " " + disp.getText());
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