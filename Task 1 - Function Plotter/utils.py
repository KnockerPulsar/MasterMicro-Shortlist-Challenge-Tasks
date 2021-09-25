import re
from traceback import print_stack
import matplotlib

from matplotlib.backends.backend_tkagg import FigureCanvasTkAgg
from matplotlib.ticker import NullFormatter  # useful for `logit` scale

import matplotlib.pyplot as plt
import PySimpleGUI as sg
import numpy as np
import sympy
import numpy

from PySimpleGUI.PySimpleGUI import WINDOW_CLOSED, popup

from typing import Tuple, Dict

from matplotlib.figure import Figure

matplotlib.use('TkAgg')

# Some "constants" to help with readability
ERRORS: Dict[str, str] = dict(
    {
        'no x': 'The function does not contain the variable x!',
        'consec': '2 or more consecutive x symbols or arithmetic symbols.',
        'invalid symbol': 'The function contains something other than x, +, -, *, /, ^, and **. Please make sure the function contains only these symbols',
        'parse error': 'xMin or xMax are invalid numbers, please make sure they\'re valid numbers',
        'malformed': 'Malformed function, please make sure there are no extra arithmetic operators at the end of the function, or that x is not preceeded or followed directly by a number',
    }
)

SUCCESS = 'success'

# Creates the window and returns the needed variables to plot
# Can be parametrized to easily change element sizes and placeholder text if needed
# Allows for easily creatin a context for testing if needed too


def create_window() -> Tuple[FigureCanvasTkAgg, Figure, plt.axes, sg.Window]:
    # define the window layout
    layout = [
        [sg.Canvas(key='-CANVAS-')],
        [sg.InputText('2*x^2', key='fn', size=24)],
        [[sg.Text('x Min'), sg.Input('-50', key='x_min', size=4),
          sg.Text('x Max'), sg.Input('50', key='x_max', size=4)]],
        [sg.Button('Plot!', bind_return_key=True)],
    ]

    # create the form and show it without the plot
    window = sg.Window('MasterMacro(TM) Plotter 6000',
                       layout, finalize=True, element_justification='center', font='Helvetica 18')

    # Initialize figure and add the plot to the window
    fig = matplotlib.figure.Figure(figsize=(3, 3), dpi=100)
    plot = None
    fig_canvas_agg = draw_figure(window['-CANVAS-'].TKCanvas, fig)

    return fig_canvas_agg, fig, plot, window


# Matplotlib helper function
def draw_figure(canvas, figure):
    figure_canvas_agg = FigureCanvasTkAgg(figure, canvas)
    figure_canvas_agg.draw()
    figure_canvas_agg.get_tk_widget().pack(side='top', fill='both', expand=1)
    return figure_canvas_agg

# Validates input, returns an error with an explanation message.
# Parses x_min and x_max to ensure they're proper floats


def validate_input(input_fn: str, xMin: str, xMax: str) -> Tuple[str, float, float]:
    # Check if function does not contain x
    if 'x' not in input_fn:
        return ERRORS['no x'], None, None

    # If there are 2 or more consecutive x's
    elif re.search(r"[x]{2,}", input_fn) is not None:
        return ERRORS['consec'], None, None

    # Check if any of the legal symbols is duplicated two or more times consecutively
    elif re.search(r"[+-/*\^]{2,}", input_fn) is not None:
        return ERRORS['consec'], None, None

    # Check if function contains anything other than x, +, -, *, ^, / and whitespace
    elif re.search(r"[^x+-/^*0-9 ()]", input_fn) is not None:
        return ERRORS['invalid symbol'], None, None

    # Check if x is preceeded or followed directly by a number (2x or x2)
    elif re.search(r"(?=[\dx]*\d)\d*x\d*", input_fn) is not None:
        return ERRORS['malformed'], None, None

    # Parse xMin and xMax
    try:
        xMin = float(xMin)
        xMax = float(xMax)
    except ValueError:
        return ERRORS['parse error'], None, None

    return SUCCESS, xMin, xMax

# Takes the user input and objects required to draw to the figure and returns a success/error code


def plot_user_fn(fn: str, x_min: str, x_max: str, fig_canvas_agg: FigureCanvasTkAgg,
                 fig: Figure) -> str:

    # Convert the function string to lowercase to avoid checking for uppercase
    fn = fn.lower()

    # Validate input
    valid, xMin, xMax = validate_input(fn, x_min, x_max)

    # Process input if valid
    if valid == SUCCESS:

        # Define x as a variable
        x = sympy.symbols('x')

        # Replace carets with exponential symbols
        fn = fn.replace('^', '**')

        try:
            parsed_expr = sympy.parse_expr(fn)

            # Calculate axis values for plotting
            lamb = sympy.lambdify(x, parsed_expr, modules=['numpy'])
            x_axis = numpy.linspace(xMin, xMax)
            y_axis = lamb(x_axis)

            axes = fig.get_axes()
            if len(axes) == 0:
                fig.add_subplot(111)
                axes = fig.get_axes()


            ax = axes[0]
            ax.cla()
            ax.plot(x_axis, y_axis)

            # Draw to the GUI
            fig_canvas_agg.draw()

        except SyntaxError:
            valid = ERRORS['malformed']

        # In case an exception occured
        # Should allow to return specific error codes/messages for different exceptions later on if needed
        finally:
            return valid

    # If the function is invalid
    return valid
