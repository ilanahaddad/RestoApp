package ca.mcgill.ecse223.resto.view;

import ca.mcgill.ecse223.resto.controller.InvalidInputException;
import ca.mcgill.ecse223.resto.controller.RestoController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.lang.Integer.parseInt;

public class RestoAppForms
{
    public static final ActionListener EXIT_ACTION = (ActionEvent event) -> System.exit(0);

    public static final ActionListener ADD_TABLE_ACTION = (ActionEvent event) -> {
        JTextField tableNumField = new JTextField();
        JTextField numSeatsField = new JTextField();
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Table Number:"));
        panel.add(tableNumField);
        panel.add(new JLabel("Number of Seats:"));
        panel.add(numSeatsField);
        int result = JOptionPane.showConfirmDialog(null, panel, "Add Table",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION)
        {
            //TODO validate input
            int numSeats = parseInt(numSeatsField.getText());
            int tableNum = parseInt(tableNumField.getText());
            try {
                RestoController.createTableAndSeats(numSeats, tableNum, 0,0,2,1);
            } catch (InvalidInputException e) {
                e.printStackTrace();
            }

            String successMessage = "Added table #" + tableNum + " with " + numSeats + " seats.";
            JOptionPane.showMessageDialog(null, successMessage);
        } else {
            JOptionPane.showMessageDialog(null, "No Table Added.");
        }
    };
}
