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

/**
  *
  * Calculator.java handles the UI throughput into the JavaFX built GUI.
  * It builds the user interface, handles IO, and allows interaction between the user and
  * the program's functions.
  *
  **/

public class Calculator extends Application {

    // Initializes display, button map, and controller object
    private TextField disp;
    private TextField histDisp;
    private final Map<String, Button> buttons = new HashMap<>();
    private final Controller control = new Controller();

    @Override
    public void start(Stage stage) {

        setupDisplays();

        GridPane grid = createButtonGrid();

        VBox displayBox = new VBox(histDisp, disp);
        displayBox.setId("display-box");
        VBox root = new VBox(25, displayBox, grid);
        root.setPadding(new Insets(10));
        root.setAlignment(Pos.TOP_CENTER);
        VBox.setVgrow(grid, Priority.ALWAYS);

        Scene scene = new Scene(root, 500, 650);

        // Links class to .css file for gui customization
        scene.getStylesheets().add(getClass().getResource("/calculator.css").toExternalForm());

        setupKBControls(scene);

        stage.setTitle("Calculator");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setMinHeight(500);
        stage.setMinWidth(350);
        stage.show();
    }

    private void setupDisplays() {

        // Sets up display
        disp = new TextField("0");
        disp.setEditable(false);
        disp.setFocusTraversable(false);
        disp.setAlignment(Pos.CENTER_RIGHT);
        disp.setId("display");
        disp.setPrefHeight(80);
        disp.setMaxWidth(Double.MAX_VALUE);
        updateDispFont();

        // Sets up display for calculation history
        histDisp = new TextField();
        histDisp.setEditable(false);
        histDisp.setFocusTraversable(false);
        histDisp.setAlignment(Pos.CENTER_RIGHT);
        histDisp.setId("history-display");
    }

    private GridPane createButtonGrid() {

        // Sets up grid for buttons
        GridPane grid = new GridPane();
        grid.setHgap(7);
        grid.setVgap(7);
        grid.setAlignment(Pos.CENTER);

        // Allows button expansion along columns with window expansion
        for (int i = 0; i < 4; i++) {

            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(25);
            column.setHgrow(Priority.ALWAYS);
            grid.getColumnConstraints().add(column);
        }

        // Allows button expansion among rows with window expansion
        for (int i = 0; i < 6; i++) {

            RowConstraints row = new RowConstraints();
            row.setPercentHeight(18);
            row.setVgrow(Priority.ALWAYS);
            grid.getRowConstraints().add(row);
        }

        // Button grid layout
        String[][] keys = {

                {"C", "x⁻¹", "√", "⌫"},
                {"x²", "xʸ", "%", "/"},
                {"7", "8", "9", "*"},
                {"4", "5", "6", "-"},
                {"1", "2", "3", "+"},
                {"+/-", "0", ".", "="}
        };

        // Creates buttons using the grid layout
        for (int row = 0; row < keys.length; row++) {

            for (int column = 0; column < keys[row].length; column++) {

                // Creates buttons, button size, styles, etc.
                String key = keys[row][column];
                Button button = new Button(key);
                buttons.put(key, button);
                button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

                // Assigns equals sign its own class in .css file
                if (key.matches("=")) {

                    button.getStyleClass().add("equals-button");
                }

                // Assigns function and operator buttons their own class in .css file
                if (key.equals("C") || key.equals("⌫")
                        || key.equals("%") || key.equals("√") || key.equals("xʸ")
                        || key.equals("x²") || key.equals("x⁻¹") || key.matches("[+\\-/*]")) {

                    button.getStyleClass().add("function-button");
                }

                button.setOnAction(event -> handleInput(key));
                grid.add(button, column, row);
            }
        }

        return grid;
    }

    private void setupKBControls(Scene scene) {

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

        // Adds functionality for giving the button a "pressed" look when pressed with keyboard input
        scene.setOnKeyPressed(event -> {

            String key = event.getText();

            if (event.getCode() == KeyCode.DIGIT6 && event.isShiftDown()) {

                handleInput("^");
                pressButton("xʸ");
            }

            else if (key.matches("[0-9]")) {

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

        // Removes the "pressed" look when keyboard input is released
        scene.setOnKeyReleased(event -> {

            String key = event.getText();
            Button button;
            String visKey = key;

            if (event.getCode() == KeyCode.DIGIT6 && event.isShiftDown()) {

                visKey = "xʸ";
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
    }

    private void handleInput(String input) {
        // Handles any input, utilizing appropriate methods as necessary

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

        else if (input.equals("x²")) {

            handleSquare();
        }

        else if (input.equals("x⁻¹")) {

            handleReciprocal();
        }

        else if (input.equals("xʸ") || input.equals("^")) {

            handleOperator("^");
        }
    }

    private void handleNumber(String input) {
        // Handles usage of any number button

        if (control.isStartNewNumber()) {

            disp.setText(input);
            control.setStartNewNumber(false);
        }

        else {

            disp.setText(disp.getText() + input);
        }

        updateDispFont();
        updateExpHist();
    }

    private void handleDecimal() {
        // Handles usage of decimal button

        // If an operator has already been selected, ensures the decimal is used on the second number
        if (control.isStartNewNumber()) {

            disp.setText("0.");
            control.setStartNewNumber(false);
        }

        // Verifies that there isn't already a decimal present
        // If there is already a decimal present, nothing happens
        else if (!disp.getText().contains(".")) {

            disp.setText(disp.getText() + ".");
        }

        updateDispFont();
        updateExpHist();
    }

    private void handleOperator(String input) {
        // Handles usage of any operator button

        String result = control.handleOperator(disp.getText(), input);
        disp.setText(result);
        updateDispFont();
        histDisp.setText(control.getExpStart());
        control.setStartNewNumber(true);
    }

    private void handleBackspace() {
        // Handles usage of backspace button

        if (!control.isStartNewNumber()) {

            String currTxt = disp.getText();

            if (currTxt.length() > 1) {

                disp.setText(currTxt.substring(0, currTxt.length() - 1));
                updateExpHist();
            }

            else {

                disp.setText("0");
            }

            updateDispFont();
        }
    }

    private void handleSignChange() {
        // Handles usage of sign change button (+/-)

        String signChange = control.handleSignChange(disp.getText());
        disp.setText(signChange);
        updateDispFont();

        // If sign change is part of an equation, displays sign change in calc. history
        if (control.hasOperation()) {

            histDisp.setText(control.getExpStart() + " " + signChange);
        }
    }

    private void handleSqRt() {
        // Handles usage of square root button

        String origNum = disp.getText();

        try {
            String result = control.handleSqRt(origNum);

            // Square roots that are part of an equation
            if (control.hasOperation()) {

                histDisp.setText(control.getExpStart() + " √" + origNum);
                control.setStartNewNumber(false);
            }

            // Standalone square roots
            else {

                histDisp.setText("√" + origNum + " = " + result);
                control.setStartNewNumber(true);
            }

            disp.setText(result);
            updateDispFont();
        }

        catch (ArithmeticException e) {

            disp.setText(e.getMessage());
            updateDispFont();
        }
    }

    private void handleEquals() {
        // Handles usage of equals button

        if (control.hasOperation() && !control.isStartNewNumber()) {

            String secondNumber = disp.getText();

            try {

                String result = control.handleEquals(secondNumber);
                disp.setText(result);
                histDisp.setText(histDisp.getText() + " = " + result);
            }

            // User attempted to divide by 0
            catch (ArithmeticException e) {
                disp.setText(e.getMessage());
            }

            updateDispFont();
            control.clear();
        }
    }

    private void handleClear() {
        // Handles usage of clear button

        control.clear();
        disp.setText("0");
        updateDispFont();
        histDisp.clear();
    }

    private void handlePercent() {
        // Handles percentages

        String origNum = disp.getText();
        String result = control.handlePercent(origNum);

        // Percentage part of an equation (like calculating totals + tip)
        if (control.hasOperation()) {

            histDisp.setText(control.getExpStart() + " " + origNum + "%");
            control.setStartNewNumber(false);
        }

        // Standalone percentage
        else {

            histDisp.setText(origNum + "% = " + result);
            control.setStartNewNumber(true);
        }

        disp.setText(result);
        updateDispFont();
    }

    private void handleSquare() {
        // Handles squaring numbers

        String origNum = disp.getText();
        String result = control.handleSquare(origNum);

        // Square part of an equation
        if (control.hasOperation()) {

            histDisp.setText(control.getExpStart() + " " + origNum + "²");
            control.setStartNewNumber(false);
        }

        // Standalone square
        else {

            histDisp.setText(origNum + "² = " + result);
            control.setStartNewNumber(true);
        }

        disp.setText(result);
        updateDispFont();
    }

    private void handleReciprocal() {
        // Handles reciprocals

        String origNum = disp.getText();

        try {

            String result = control.handleReciprocal(origNum);

            // Reciprocal part of an equation
            if (control.hasOperation()) {

                histDisp.setText(control.getExpStart() + " " + origNum + "⁻¹");
                control.setStartNewNumber(false);
            }

            else {

                histDisp.setText(origNum + "⁻¹ = " + result);
                control.setStartNewNumber(true);
            }

            disp.setText(result);
            updateDispFont();
        }

        catch (ArithmeticException e) {

            disp.setText(e.getMessage());
            updateDispFont();
        }
    }

    private void updateExpHist() {
        // Handles numbers in the calculation history box

        if (control.hasOperation()) {

            histDisp.setText(control.getExpStart() + " " + disp.getText());
        }
    }

    private void updateDispFont() {
        // Updates font size on the display depending on number length

        String currTxt = disp.getText();

        if (currTxt.length() <= 19) {

            disp.setStyle("-fx-font-size: 40px");
        }

        else if (currTxt.length() <= 25) {

            disp.setStyle("-fx-font-size: 32px");
        }

        else if (currTxt.length() <= 31) {

            disp.setStyle("-fx-font-size: 24px");
        }

        else if (currTxt.length() > 31) {

            disp.setStyle("-fx-font-size: 18px");
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