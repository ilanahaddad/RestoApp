package ca.mcgill.ecse223.resto.view;

import ca.mcgill.ecse223.resto.controller.RestoController;
import ca.mcgill.ecse223.resto.model.Table;

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
    private final int UNIT_LENGTH = 75;
    private final int GUI_WIDTH = 700;
    private final int GUI_HEIGHT = 550;
    private final String GUI_TITLE = "RestoApp";
    private final String RESSOURCES_PATH = "RestoApp/ressources/";

    private int maxX = GUI_HEIGHT;
    private int maxY = GUI_WIDTH;

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
        JScrollPane scrollPane = new JScrollPane(tablePanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        updateScrollbarMax(RestoController.getMaxX(), RestoController.getMaxY());

        add(scrollPane);
    }

    private void createMenuBar()
    {
        JMenuBar menubar = new JMenuBar();
        JMenu actions = new JMenu("Actions");
        JMenuItem removeTableMenuItem = createMenuItem(
        		"Remove Table", KeyEvent.VK_D, this::removeTableAction);
        JMenuItem addTableMenuItem = createMenuItem(
                "Add Table", KeyEvent.VK_A, this::addTableAction);
        JMenuItem exitMenuItem = createMenuItem(
                "Exit", KeyEvent.VK_E, RestoAppActions.EXIT_ACTION);

        actions.setMnemonic(KeyEvent.VK_F);
        actions.add(removeTableMenuItem);
        actions.add(addTableMenuItem);
        actions.add(exitMenuItem);
        menubar.add(actions);
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
        JButton removeTableButton = createButton(
        		"removeTable.png", "Remove Table", this::removeTableAction);

        toolbar.add(exitButton);
        toolbar.add(addTableButton);
        toolbar.add(removeTableButton);
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
            try {
                int numSeats = parseInt(numSeatsField.getText());
                int tableNum = parseInt(tableNumField.getText());
                int x = parseInt(xField.getText());
                int y = parseInt(yField.getText());
                int width = parseInt(widthField.getText());
                int length = parseInt(lengthField.getText());

                RestoController.createTableAndSeats(numSeats, tableNum, x, y, width, length);

                updateScrollbarMax(x,y);

                tablePanel.revalidate();
                tablePanel.repaint();


                JOptionPane.showMessageDialog(null, "Table added successfully.");
            }
            catch (NumberFormatException error)
            {
                JOptionPane.showMessageDialog(
                        null,
                        "All fields must be integers.",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
            }
            catch (Exception error)
            {
                JOptionPane.showMessageDialog(
                        null,
                        error.getMessage(),
                        "Couldn't add Table",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "No Table Added.");
        }
    }

    private void updateScrollbarMax(int x, int y)
    {
        if (maxX < x*UNIT_LENGTH) { maxX = x*UNIT_LENGTH; }
        if (maxY < y*UNIT_LENGTH) { maxY = y*UNIT_LENGTH; }

        int xLimit = (maxX > GUI_WIDTH) ? maxX : GUI_WIDTH;
        int yLimit = (maxY > GUI_HEIGHT) ? maxY : GUI_HEIGHT;
        tablePanel.setPreferredSize(new Dimension(xLimit+2*UNIT_LENGTH, yLimit+2*UNIT_LENGTH));
    }

    private void removeTableAction(ActionEvent event)
    {
        JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));
        
        panel.add(new JLabel("Select table to remove"));
        JComboBox<Table> currentTableList = new JComboBox(RestoController.getTables().toArray());
        panel.add(currentTableList);

        int result = JOptionPane.showConfirmDialog(null, panel, "Remove Table",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION)
        {
            try {
                //int tableNum = parseInt(tableNumField.getText()) - 1;
            	
                //RestoController.removeTable(RestoController.getCurrentTable(tableNum));
                RestoController.removeTable((Table)currentTableList.getSelectedItem());
            	tablePanel.revalidate();
                tablePanel.repaint();

                JOptionPane.showMessageDialog(null, "Table removed successfully.");
            }
            catch (NumberFormatException error)
            {
                JOptionPane.showMessageDialog(
                        null,
                        "All fields must be integers.",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
            }
            catch (Exception error)
            {
                JOptionPane.showMessageDialog(
                        null,
                        error.getMessage(),
                        "Could not remove Table",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Unsuccessful removal.");
        }
    }
}
