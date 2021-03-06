

# Function Plotter
It can plot polynomial functions in x within a specific range.
Supported operators: +, -, *, /, ^.
Does not support functions in x such as trigonometric functions (`sin(x)` for example) nor other functions in x (`sqrt(x)` for example) 

# Dependencies
PySimpleGUI, Sympy, Matplotlib, Numpy, and Pytest.

# Running tests
Assuming you have `Pytest` installed, just set your current directory as the project's root (`Task 1 - Function Plotter/`) and enter `pytest` into your terminal. Pytest will then go through all files named `test_*.py` and run the functions inside them.

# Possible improvements
* Swapping out the GUI framework: While PySimpleGUI is a great framework to get your application up and running, I believe that you probably want to use another framework that allows for more customizability at the cost of development speed. I just happened to use this since it's the fastest way I know of creating a GUI application.
  
* More complex function support: Supporting functions (at least what Sympy allows) in x should be possible if you remove a couple of validation tests in `validate_input`, but that would also allow for invalid functions with undefined variables to be entered too.

# Snapshots

## Initial view
![Initial View](snapshots/InitialView.PNG)  

## Some correct inputs
![Correct 1](snapshots/1.PNG)
![Correct 2](snapshots/2.PNG)
![Correct 3](snapshots/3.PNG)

# Some incorrect inputs

![Incorrect 1](snapshots/w1.PNG)
![Incorrect 2](snapshots/w2.PNG)
![Incorrect 3](snapshots/w3.PNG)
![Incorrect 4](snapshots/w4.PNG)

# Functional Diagram
![Diagram](snapshots/Function%20Plotter%20Diagram.drawio.png)