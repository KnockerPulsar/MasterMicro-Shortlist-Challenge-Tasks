#!/usr/bin/env python
import matplotlib.pyplot as plt
import PySimpleGUI as sg
import numpy as np
import matplotlib
import sympy
import numpy
import typing
import re
from matplotlib.backends.backend_tkagg import FigureCanvasTkAgg
from PySimpleGUI.PySimpleGUI import WINDOW_CLOSED
from matplotlib.ticker import NullFormatter  # useful for `logit` scale
matplotlib.use('TkAgg')

def validate_input(input_fn : str):
    # Check if function does not contain x
    if 'x' not in input_fn:
        return False
    
    # If there are 2 or more consecutive x's
    elif re.search(r"[x]{2,}", input_fn) is not None:
        return False

    # Check if any of the legal symbols is duplicated two or more times consecutively 
    elif re.search(r"[+-/*\^]{2,}", input_fn) is not None:
        return False

    # Check if function contains anything other than x, +, -, *, ^, / and whitespace
    elif re.search(r"[^x+-/^*0-9 ()]", input_fn) is not None:
        return False

    return True


# ------------------------------- Beginning of Matplotlib helper code -----------------------

def draw_figure(canvas, figure):
    figure_canvas_agg = FigureCanvasTkAgg(figure, canvas)
    figure_canvas_agg.draw()
    figure_canvas_agg.get_tk_widget().pack(side='top', fill='both', expand=1)
    return figure_canvas_agg

# ------------------------------- Beginning of GUI CODE -------------------------------

# define the window layout
layout = [
    [sg.Canvas(key='-CANVAS-') ],
    [sg.InputText('2*x^2', key='fn')],
    [sg.Text('xMin'), sg.Input('0', key='xMin')],
    [sg.Text('xMax'), sg.Input('100', key='xMax')],
    [sg.Button('Plot!', bind_return_key=True)],
    ]

# create the form and show it without the plot
window = sg.Window('MasterMacro(TM) Plotter 6000',
     layout, finalize=True, element_justification='center', font='Helvetica 18')

# add the plot to the window
fig = matplotlib.figure.Figure(figsize=(4, 4), dpi=100)
plot = None
fig_canvas_agg = draw_figure(window['-CANVAS-'].TKCanvas, fig)
while True:
    event, values = window.read()

    if event == 'Plot!':
        fn = values['fn']
        fn = fn.lower()

        valid = validate_input(fn) # TODO validation checks
        if valid == True:

            x = sympy.symbols('x')
            
            # TODO move this outside to the validation function
            try:
                xMin = float(values['xMin'])
                xMax = float(values['xMax'])
            except:
                print("Min or max axis values cannot be converted")
                break


            fn = fn.replace('^', '**')

            try:
                parsed_expr = sympy.parse_expr(fn)
            except SyntaxError:
                print("Syntax Error!\nAn operator might be written incorrectly")
                # TODO Error handling
            
            # Calculate axis values for plotting
            lamb = sympy.lambdify(x, parsed_expr,modules=['numpy'])
            x_axis = numpy.linspace(xMin,xMax)
            y_axis = lamb(x_axis)
            

            # If first time plotting, add a subplot
            if plot is None:
                plot = fig.add_subplot(111)

            # Clear and update the plot
            fig_canvas_agg.get_tk_widget().pack_forget()
            plot.clear()
            plot.plot(x_axis,y_axis)

            # Draw to the GUI
            fig_canvas_agg = draw_figure(window['-CANVAS-'].TKCanvas, fig)
        else:
            print('ERROR')
            #TODO Error handling
        
    if event == WINDOW_CLOSED:
        break

window.close()