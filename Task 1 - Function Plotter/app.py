import PySimpleGUI as sg

from PySimpleGUI.PySimpleGUI import WINDOW_CLOSED
from utils import create_window, SUCCESS, plot_user_fn


def main():
    fig_canvas_agg, fig, plot, window = create_window()

    # Main application loop
    while True:
        event, values = window.read()

        # If the plot button is pressed or the enter key is pressed
        if event == 'Plot!' :

            # Retrieving inputs
            fn = values['fn']
            x_min = values['x_min']
            x_max = values['x_max']

            message = plot_user_fn(fn, x_min, x_max, fig_canvas_agg, fig)

            if message != SUCCESS :
               sg.popup(message)

        if event == WINDOW_CLOSED:
            break

    window.close()


if __name__ == "__main__":
    main()