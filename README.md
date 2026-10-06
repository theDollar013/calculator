# Calculator
A Java-built calculator utilizing a JavaFX GUI
Developed by theDollar013

This project is a Windows desktop calculator built as a Java learning and portfolio project. It uses JavaFX for the GUI
	and separates the UI, calculator state, and mathematical logic into their own dedicated classes.

 It currently uses immediate execution logic, similar to that of a traditional, basic calculator, rather than
 	recognizing order of operations. A separate scientific mode may be implemented at a later time that calculates with
 	order of operations in mind. The calculator also handles invalid operations such as division by zero and square
	roots of negative numbers.
	
# Features

Current functionality includes:
- Addition / Subtraction
- Multiplication / Division
- Decimal Input
- + / - toggle
- Percentages
- Square Root
- Squaring / Exponents / Reciprocals
- Keyboard Input
- Single-Line Calculation History
- Dynamic Display Sizing
- Responsive Layout

# Keyboard Controls

The following keyboard inputs are currently supported:
- 0-9 -- Number Entry
- . -- Decimal
- + -- Addition
- - -- Subtraction
- * -- Multiplication
- / -- Division
- Shift + 6 (^) -- Exponents
- Enter -- Equals
- Backspace
- Escape -- Clear

# Project Structure

The project is organized into 3 main Java classes:
- Calculator.java
- Controller.java
- Logic.java

# Calculator.java

Handles the JavaFX interface, including:
- Window Creation
- Display Fields
- Button Layout
- Keyboard Input
- Visual Updates
- Calculation History

# Controller.java

Coordinates user input and manages the calculator state. It determines how input should affect the current calculation
	and acts as the connection between the JavaFX interface and the mathematical logic.

# Logic.java

Handles mathematical calculations and internal operation logic.

# Technologies

- Java 27
- JavaFX 27
- Apache Maven
- JUnit
- jpackage
- WiX Toolset
- Windows PowerShell

# Requirements

To build and run the project from source requires JDK 27 and Apache Maven. To run the calculator, from the project
	directory, run the command 'mvn javafx:run'. To test, run 'mvn test'. To run a clean build and execute all tests,
	run 'mvn clean verify'. The generated JAR is located in target. The JAR alone is not intended to be the primary
	distribution method since the application also requires its JavaFX runtime dependencies.

# Building the Windows Installer

Creating the Windows Installer additionally requires
- JDK 27
- WiX Toolset

A PowerShell build script is included in the project: build-installer.ps1. A successful build creates the Windows
	installer in target/installer. The packaged application includes its own JRE, so users don't need to install Java
	or Maven separately in order to run the installed program. The current packaging setup uses WiX to generate the
	Windows .exe installer.

# Planned Features

Later builds may include support for:
- Multi-Line Calculation History
- Scientific Mode
- Further UI and Animation Improvements
	
# Build Output

Generated build files are placed inside target/
Because Maven recreates this directory during builds, generated application and installer files should not be treated
	as source files.