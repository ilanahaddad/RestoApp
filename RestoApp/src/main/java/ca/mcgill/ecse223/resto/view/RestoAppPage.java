package ca.mcgill.ecse223.resto.view;

import static java.lang.Integer.parseInt;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.*;

import ca.mcgill.ecse223.resto.controller.InvalidInputException;
import ca.mcgill.ecse223.resto.controller.RestoController;
import ca.mcgill.ecse223.resto.model.Table;


public class RestoAppPage extends JFrame
{
    private static final long serialVersionUID = 6588228649238198455L;

    private final int UNIT_LENGTH = 75;
    private final int GUI_WIDTH = 700;
    private final int GUI_HEIGHT = 550;
    private final String GUI_TITLE = "RestoApp";
    private final String RESSOURCES_PATH = "src/main/ressources/";

    private int maxX = GUI_HEIGHT;
    private int maxY = GUI_WIDTH;

    private final int SCROLLBAR_SPEED = 16;

    private JPanel tablePanel;

    public RestoAppPage()
    {
        setNiceTheme();
        initUI();
    }

    private void initUI()
    {
        configureUI();
        createMenuBar();
        createToolBar();
        createTablePanel();
    }

    private void createTablePanel()
    {
        tablePanel = new TablePanel();
        JScrollPane scrollbar = new JScrollPane(tablePanel);

        configureScrollbar(scrollbar);
        add(scrollbar);
    }

    private void createMenuBar()
    {
        JMenuBar menubar = new JMenuBar();
        JMenu actions = new JMenu("Actions");
        JMenuItem exitMenuItem = createMenuItem("Exit", RestoAppActions.EXIT_ACTION);
        JMenuItem addTableMenuItem = createMenuItem("Add Table", this::addTableAction);
        JMenuItem changeTableMenuItem = createMenuItem("Change Table", this::updateTableAction);
        JMenuItem moveTableMenuItem = createMenuItem("Move Table", this::moveTableAction);

        JMenuItem removeTableMenuItem = createMenuItem("Remove Table", this::removeTableAction);

        actions.add(addTableMenuItem);
        actions.add(changeTableMenuItem);
        actions.add(removeTableMenuItem);
        actions.add(exitMenuItem);
        menubar.add(actions);
        setJMenuBar(menubar);
    }

    private JMenuItem createMenuItem(String itemName, ActionListener action)
    {
        JMenuItem menuItem = new JMenuItem(itemName);
        menuItem.addActionListener(action);
        return menuItem;
    }

    private void createToolBar()
    {
        JToolBar toolbar = new JToolBar();
        JButton exitButton = createButton("power.jpg","Exit App [Alt + Q]", KeyEvent.VK_Q, RestoAppActions.EXIT_ACTION);
        JButton addTableButton = createButton("addTable.jpg", "Add Table [Alt + A]", KeyEvent.VK_A, this::addTableAction);
        JButton changeTableButton = createButton("changeTable.jpg", "Change Table [Alt + U]", KeyEvent.VK_U, this::updateTableAction); 
        JButton moveTableButton = createButton("moveTable.jpg", "Move Table [Alt + M]", KeyEvent.VK_M, this::moveTableAction); 
        JButton removeTableButton = createButton("removeTable.jpg", "Delete Table [Alt + D]", KeyEvent.VK_D, this::removeTableAction);

        toolbar.add(exitButton);
        toolbar.add(addTableButton);
        toolbar.add(changeTableButton);
        toolbar.add(moveTableButton);
        toolbar.add(removeTableButton);
        add(toolbar, BorderLayout.NORTH);
    }

    private JButton createButton(String iconName, String buttonTip, int shortcut, ActionListener action)
    {
        Path iconPath = Paths.get(RESSOURCES_PATH + iconName).toAbsolutePath();
        ImageIcon icon = new ImageIcon(iconPath.toString());
        JButton button = new JButton(icon);

        button.setMnemonic(shortcut);
        button.setToolTipText(buttonTip);
        button.addActionListener(action);
        return button;
    }

    private void updateScrollbarMax(int x, int y)
    {
        if (maxX < x*UNIT_LENGTH) { maxX = x*UNIT_LENGTH; }
        if (maxY < y*UNIT_LENGTH) { maxY = y*UNIT_LENGTH; }

        int xLimit = (maxX > GUI_WIDTH) ? maxX+UNIT_LENGTH : GUI_WIDTH;
        int yLimit = (maxY > GUI_HEIGHT) ? maxY+UNIT_LENGTH : GUI_HEIGHT;
        tablePanel.setPreferredSize(new Dimension(xLimit, yLimit));
    }

    private void setNiceTheme()
    {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception error)
        {
            JOptionPane.showMessageDialog(
                    null, error.getMessage(), "Could not set theme", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void configureUI()
    {
        setTitle(GUI_TITLE);
        setSize(GUI_WIDTH, GUI_HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void configureScrollbar(JScrollPane scrollbar)
    {
        scrollbar.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollbar.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollbar.getVerticalScrollBar().setUnitIncrement(SCROLLBAR_SPEED);
        scrollbar.getHorizontalScrollBar().setUnitIncrement(SCROLLBAR_SPEED);

        updateScrollbarMax(RestoController.getMaxX(), RestoController.getMaxY());
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
        if (result == JOptionPane.OK_OPTION){
            try{
                int numSeats = parseInt(numSeatsField.getText());
                int tableNum = parseInt(tableNumField.getText());
                int x = parseInt(xField.getText());
                int y = parseInt(yField.getText());
                int width = parseInt(widthField.getText());
                int length = parseInt(lengthField.getText());

                RestoController.createTableAndSeats(numSeats, tableNum, x, y, width, length);

                updateScrollbarMax(x+width, y+length);
                tablePanel.revalidate();
                tablePanel.repaint();

                JOptionPane.showMessageDialog(null, "Table added successfully.");
            }
      /*      catch (NumberFormatException error){
                String errorMessage = "All fields must be integers.";
                JOptionPane.showMessageDialog(null, errorMessage, "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }*/
            catch (Exception error){
            	JOptionPane.showMessageDialog(
                        null,
                        error.getMessage(),
                        "Could not add table",
                        JOptionPane.ERROR_MESSAGE);
            }
        } 
        else { 
        		JOptionPane.showMessageDialog(null, "No Table Added."); 
        	}
    }
    private void updateTableAction(ActionEvent event){
        JPanel panel = new JPanel(new GridLayout(6, 2, 5,5));

 /*       //create array of all table numbers in resto app 
        int allTablesAmount = RestoController.getTables().size();
        String allTablesNums[] = new String[allTablesAmount];
        for (int i = 0; i < allTablesAmount; i++){
        		allTablesNums[i] = "" + RestoController.getTable(i).getNumber();
        }*/
        
        //create array of current table numbers
        int currentLength = RestoController.getCurrentTables().size();
        String currentTableNums[] = new String[currentLength];
        for (int i = 0; i < currentLength; i++){
        	currentTableNums[i] = "" + RestoController.getCurrentTable(i).getNumber();
        }

        panel.add(new JLabel("Select table to change:"));
        JComboBox<String> allTablesList = new JComboBox<String>(currentTableNums);
        panel.add(allTablesList);  	
        	
        panel.add(new JLabel("New Table Number:"));
        JTextField newTableNumField = new JTextField();
        panel.add(newTableNumField);

        panel.add(new JLabel("Number of Seats:"));
        JTextField numSeatsField = new JTextField();
        panel.add(numSeatsField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Update Table",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION){
        		try{
        			int tableNum = Integer.parseInt((String) allTablesList.getSelectedItem());
        			Table selectedTable = RestoController.getTableByNum(tableNum);
        			int newTableNum = parseInt(newTableNumField.getText());
        			int amountOfSeats = parseInt(numSeatsField.getText());
    
                RestoController.updateTable(selectedTable, newTableNum, amountOfSeats);
            	
            	tablePanel.revalidate();
                tablePanel.repaint();

                JOptionPane.showMessageDialog(null, "Table updated successfully.");
                }
                catch (Exception error)
                {
                    JOptionPane.showMessageDialog(
                            null,
                            error.getMessage(),
                            "Could not update table",
                            JOptionPane.ERROR_MESSAGE);
                }
        } 
        else { 
        	JOptionPane.showMessageDialog(null, "No tables were changed."); 
        	}
    }
    private void moveTableAction(ActionEvent event){
    	
    }
    private void removeTableAction(ActionEvent event)
    {
        JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));
        //create array of current table numbers
        int currentLength = RestoController.getCurrentTables().size();
        String currentTableNums[] = new String[currentLength];
        for (int i = 0; i < currentLength; i++){
        	currentTableNums[i] = "" + RestoController.getCurrentTable(i).getNumber();
        }
        
        panel.add(new JLabel("Select table to remove"));
        JComboBox<String> currentTableList = new JComboBox<String>(currentTableNums);
        panel.add(currentTableList);

        int result = JOptionPane.showConfirmDialog(null, panel, "Remove Table",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION)
        {
            try {
                //RestoController.removeTable((Table)currentTableList.getSelectedItem());
            	int tableNum = Integer.parseInt((String) currentTableList.getSelectedItem());
            	Table selectedTable = RestoController.getCurrentTableByNum(tableNum);
            	RestoController.removeTable(selectedTable);
            	
            	tablePanel.revalidate();
                tablePanel.repaint();

                JOptionPane.showMessageDialog(null, "Table removed successfully.");
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
