import PySimpleGUI as sg

from PySimpleGUI import WINDOW_CLOSED
from utils import create_window, SUCCESS, format_error_message, plot_user_fn, format_error_message, default_fn,default_x_min,default_x_max,  HelpAbout


def main():
    fig_canvas_agg, fig,  window = create_window()
    plot_user_fn(default_fn,default_x_min,default_x_max, fig_canvas_agg, fig)

    # Main application loop
    while True:
        event, values = window.read()

        # If the plot button is pressed or the enter key is pressed
        if event == 'Plot!' :

            # Retrieving inputs
            fn = values['fn']
            x_min = values['x_min']
            x_max = values['x_max']

            # Pass the given inputs to be validated and plotted
            error_codes = plot_user_fn(fn, x_min, x_max, fig_canvas_agg, fig)

            # If any errors occured, format a list of errors and display
            # them in a popup
            if error_codes[0] != SUCCESS :
                sg.popup(format_error_message(error_codes))

        if event == WINDOW_CLOSED:
            break

        if event == 'Help/About':
            sg.popup(HelpAbout)

    window.close()


if __name__ == "__main__":
    main()