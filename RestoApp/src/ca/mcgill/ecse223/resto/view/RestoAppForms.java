package ca.mcgill.ecse223.resto.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
            String successMessage = "Added table #" + tableNumField.getText() +
                    " sith " + numSeatsField.getText() + " seats.";
            JOptionPane.showMessageDialog(null, successMessage);
        } else {
            JOptionPane.showMessageDialog(null, "No Table Added.");
        }
    };
}
