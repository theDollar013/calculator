# Calculator
A Java-built calculator utilizing a JavaFX GUI
Developed by theDollar013

The calculator handles basic mathematic functions (addition, subtraction, division, and multiplication)
	as well as supporting percentages and square roots.
	
The calculator includes a single line of history to show the previous operation that was calculated. It also includes
	keyboard-input support for all number, basic operator, enter, backspace, and escape inputs.

KNOWN BUGS:
- calculations with decimals can sometimes be incorrect (23.35 + 3.55 shows a result of 28.9000000000002)

Later builds will include support for:
- multi-line calculation history
- scientific notation
	
More features may be made available over time as they are thought of and implemented.

## Building and Running

Requirements:
- JDK 25
- Apache Maven

To Run the Calculator:
```bash
mvn javafx:run
```

To Run the Unit Tests:
```bash
mvn test
```

To Build the Project and Run Verification:
```bash
mvn clean verify
```

The generated JAR is located at:
`target/javafx-calculator-1.0.jar`

NOTE: The JAR is a build artifact. In order to launch the calculator with its JavaFX
dependencies, use `mvn javafx:run`.
