package ca.mcgill.ecse223.resto.view;

import ca.mcgill.ecse223.resto.controller.InvalidInputException;
import ca.mcgill.ecse223.resto.controller.RestoController;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.*;

import static java.lang.Integer.parseInt;


public class RestoAppPage extends JFrame
{
    private final String RESSOURCES_PATH = "RestoApp/ressources/";
    private final String GUI_TITLE = "RestoApp";
    private final int GUI_WIDTH = 500;
    private final int GUI_HEIGHT = 450;

    private JPanel tablePanel;

    public RestoAppPage() { initUI(); }

    private void initUI()
    {
        createMenuBar();
        createToolBar();
        createTablePanel();

        setTitle(GUI_TITLE);
        setSize(GUI_WIDTH, GUI_HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void createTablePanel()
    {
        tablePanel = new TablePanel();
        add(tablePanel);
    }

    private void createMenuBar()
    {
        JMenuBar menubar = new JMenuBar();
        JMenu file = new JMenu("Actions");
        JMenuItem addTableMenuItem = createMenuItem(
                "Add Table", KeyEvent.VK_A, this::addTableAction);
        JMenuItem exitMenuItem = createMenuItem(
                "Exit", KeyEvent.VK_E, RestoAppActions.EXIT_ACTION);

        file.setMnemonic(KeyEvent.VK_F);
        file.add(addTableMenuItem);
        file.add(exitMenuItem);
        menubar.add(file);
        setJMenuBar(menubar);
    }

    private JMenuItem createMenuItem(String itemName, int keyEvent, ActionListener action)
    {
        JMenuItem menuItem = new JMenuItem(itemName);
        menuItem.setMnemonic(keyEvent);
        menuItem.addActionListener(action);
        return menuItem;
    }

    private void createToolBar()
    {
        JToolBar toolbar = new JToolBar();
        JButton exitButton = createButton(
                "power.png", "Exit RestoApp", RestoAppActions.EXIT_ACTION);
        JButton addTableButton = createButton(
                "addTable.png", "Add Table", this::addTableAction);

        toolbar.add(exitButton);
        toolbar.add(addTableButton);
        add(toolbar, BorderLayout.NORTH);
    }

    private JButton createButton(String iconName, String buttonTip, ActionListener action)
    {
        Path iconPath = Paths.get(RESSOURCES_PATH+iconName).toAbsolutePath();
        ImageIcon icon = new ImageIcon(iconPath.toString());
        JButton button = new JButton(icon);
        button.setToolTipText(buttonTip);
        button.addActionListener(action);
        return button;
    }

    private void addTableAction(ActionEvent event)
    {
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
            int numSeats = parseInt(numSeatsField.getText());
            int tableNum = parseInt(tableNumField.getText());
            int x = parseInt(xField.getText());
            int y = parseInt(yField.getText());
            int width = parseInt(widthField.getText());
            int length = parseInt(lengthField.getText());
            try {
                RestoController.createTableAndSeats(numSeats, tableNum, x, y, width, length);
            } catch (InvalidInputException e) {
                e.printStackTrace();
            }

            tablePanel.revalidate();
            tablePanel.repaint();

            String successMessage = "Added table #" + tableNum + " with " + numSeats + " seats.";
            JOptionPane.showMessageDialog(null, successMessage);

        } else {
            JOptionPane.showMessageDialog(null, "No Table Added.");
        }
    }
}
