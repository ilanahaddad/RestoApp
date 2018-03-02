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
        JPanel panel = new JPanel(new GridLayout(6, 2, 5,5));

        panel.add(new JLabel("Table Number:"));
        JTextField tableNumField = new JTextField();
        panel.add(tableNumField);

        panel.add(new JLabel("Number of Seats:"));
        JTextField numSeatsField = new JTextField();
        panel.add(numSeatsField);

        panel.add(new JLabel("X:"));
        JTextField xField = new JTextField();
        panel.add(xField);

        panel.add(new JLabel("Y:"));
        JTextField yField = new JTextField();
        panel.add(yField);

        panel.add(new JLabel("Width:"));
        JTextField widthField = new JTextField();
        panel.add(widthField);

        panel.add(new JLabel("Length:"));
        JTextField lengthField = new JTextField();
        panel.add(lengthField);


        int result = JOptionPane.showConfirmDialog(null, panel, "Add Table",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION)
        {
            //TODO validate input
//            int numSeats = parseInt(numSeatsField.getText());
//            int tableNum = parseInt(tableNumField.getText());
//            int x = parseInt(xField.getText());
//            int y = parseInt(yField.getText());
//            int width = parseInt(widthField.getText());
//            int length = parseInt(lengthField.getText());
            try {
//                RestoController.createTableAndSeats(
//                        numSeats, tableNum, x,y,width,length);
                RestoController.createTableAndSeats(
                        2, 99, 1,1,1,1);
            } catch (InvalidInputException e) {
                e.printStackTrace();
            }

//            String successMessage = "Added table #" + tableNum + " with " + numSeats + " seats.";
//            JOptionPane.showMessageDialog(null, successMessage);
        } else {
            JOptionPane.showMessageDialog(null, "No Table Added.");
        }
    };
}
