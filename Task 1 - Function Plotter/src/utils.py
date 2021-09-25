import re
from tkinter import Message
from traceback import print_stack
import matplotlib

from matplotlib.backends.backend_tkagg import FigureCanvasTkAgg
from matplotlib.ticker import NullFormatter  # useful for `logit` scale

import matplotlib.pyplot as plt
import PySimpleGUI as sg
import numpy as np
import sympy
import numpy

from PySimpleGUI.PySimpleGUI import R, WINDOW_CLOSED, popup

from typing import Tuple, Dict, List

from matplotlib.figure import Figure

matplotlib.use('TkAgg')

# Some "constants" to help with readability
ERRORS: Dict[str, str] = dict(
    {
        'no x': 'The function does not contain the variable x!',
        'consec': '2 or more consecutive x symbols or arithmetic symbols.',
        'invalid symbol': 'The function contains something other than x, +, -, *, /, and ^. Please make sure the function contains only these symbols',
        'parse error': 'x_min or x_max are invalid numbers, please make sure they\'re valid numbers',
        'malformed': 'Malformed function, please make sure there are no extra arithmetic operators at the end of the function, or that x is not preceeded or followed directly by a number',
    }
)

SUCCESS = 'success'

HelpAbout = '''Polynomial function plotter
Allowed operators: +, -, *, /, ^.
Only the variable x is allowed.
Does not support functions such as sin(), cos(), tan(), etc..
Supports fractions in the form `x/y`.
Built using PySimpleGUI, Sympy, Matplotlib, tkinter, and numpy.
'''
default_fn = '2*x^2'
default_x_min = '-50'
default_x_max = '50'


# Creates the window and returns the needed variables to plot
# Can be parametrized to easily change element sizes and placeholder text if needed
# Allows for easily creatin a context for testing if needed too


def create_window() -> Tuple[FigureCanvasTkAgg, Figure, plt.axes, sg.Window]:

    # Apply a theme to make things look a bit better
    theme = 'Reddit'
    sg.theme(theme)

    # define the window layout
    layout = [
        [sg.Canvas(key='-CANVAS-')],
        [
            sg.Frame(layout=[[sg.InputText(default_fn, key='fn', size=20)]
                             ],                     title='Function'),
            sg.Frame(layout=[
                [
                    sg.Text('x Min', justification='center'), sg.Input(default_x_min, key='x_min', size=5,justification='center'),
                    sg.Text('x Max', justification='center'), sg.Input(default_x_max, key='x_max', size=5, justification='center')
                ]
            ], title='Limits')
        ],
        [sg.Button('Plot!', bind_return_key=True), sg.Button('Help/About')]
    ]


    # create the form and show it without the plot
    window = sg.Window('MasterMacro(TM) Plotter 6000',
                       layout, finalize=True, element_justification='center', font='Helvetica 18')


    # Initialize figure and add the plot to the window
    fig = matplotlib.figure.Figure(figsize=(6, 4), dpi=100)
    fig_canvas_agg = draw_figure(window['-CANVAS-'].TKCanvas, fig)

    return fig_canvas_agg, fig,  window


# Matplotlib helper function
def draw_figure(canvas, figure):
    figure_canvas_agg = FigureCanvasTkAgg(figure, canvas)
    figure_canvas_agg.draw()
    figure_canvas_agg.get_tk_widget().pack(side='top', fill='both', expand=1)
    return figure_canvas_agg

# Validates input, returns an error with an explanation message.
# Parses x_min and x_max to ensure they're proper floats


def validate_input(input_fn: str, x_min: str, x_max: str) -> Tuple[List[str], float, float]:

    errors = []
    input_fn = input_fn.lower()
    # Check if function does not contain x
    if 'x' not in input_fn:
        errors.append(ERRORS['no x'])

    # If there are 2 or more consecutive x's
    if re.search(r"[x]{2,}", input_fn) is not None:
        errors.append(ERRORS['consec'])

    # Check if any of the legal symbols is duplicated two or more times consecutively
    if re.search(r"[+-/*\^]{2,}", input_fn) is not None:
        errors.append(ERRORS['consec'])

    # Check if function contains anything other than x, +, -, *, ^, / and whitespace
    if re.search(r"[^x+-/^*0-9 ()]", input_fn) is not None:
        errors.append(ERRORS['invalid symbol'])

    # Check if x is preceeded or followed directly by a number (2x or x2)
    if re.search(r"(?=[\dx]*\d)\d*x\d*", input_fn) is not None:
        errors.append(ERRORS['malformed'])

    # Check if the function ends with an operator (+, -, *, /, ^)
    if re.search(r"[*/+-]$", input_fn):
        errors.append(ERRORS['malformed'])

    # Parse x_min and x_max
    try:
        x_min = float(x_min)
        x_max = float(x_max)
    except ValueError:
        errors.append(ERRORS['parse error'])

    if(len(errors) == 0):
        errors.append(SUCCESS)
        return errors, x_min, x_max
    else:
        return errors, None, None

# Takes the user input and objects required to draw to the figure and returns a success/error code


def plot_user_fn(fn: str, x_min: str, x_max: str, fig_canvas_agg: FigureCanvasTkAgg,
                 fig: Figure) -> str:

    # Validate input
    error_codes, x_min, x_max = validate_input(fn, x_min, x_max)

    # Process input if valid
    if len(error_codes) == 1 and error_codes[0] == SUCCESS:

        # Define x as a variable
        x = sympy.symbols('x')

        # Replace carets with exponential symbols
        fn = fn.replace('^', '**')

        try:
            parsed_expr = sympy.parse_expr(fn)

            # Calculate axis values for plotting
            lamb = sympy.lambdify(x, parsed_expr, modules=['numpy'])
            x_axis = numpy.linspace(x_min, x_max)
            y_axis = lamb(x_axis)

            # Get current plots
            # If there are none, add one
            axes = fig.get_axes()
            if len(axes) == 0:
                fig.add_subplot(111)
                axes = fig.get_axes()

            # Update plot by clearing and plotting to it
            ax = axes[0]
            ax.cla()
            ax.plot(x_axis, y_axis)

            # Draw to the GUI
            fig_canvas_agg.draw()

        # Sympy can't parse the function
        except SyntaxError:
            error_codes.append(ERRORS['malformed'])

        # In case an exception occured
        # Should allow to return specific error codes/messages for different exceptions later on if needed
        finally:
            return error_codes

    # If the function is invalid
    return error_codes


def format_error_message(error_codes: List[str]) -> str:
    message = ''
    for i, error in enumerate(error_codes):
        message += f"Error #{i}: {error}\n\n"

    return message
