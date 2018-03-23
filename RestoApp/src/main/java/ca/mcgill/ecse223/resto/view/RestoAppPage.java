package ca.mcgill.ecse223.resto.view;

import static java.lang.Integer.parseInt;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;

import ca.mcgill.ecse223.resto.controller.InvalidInputException;
import ca.mcgill.ecse223.resto.controller.RestoController;
import ca.mcgill.ecse223.resto.model.Table;

import java.util.ArrayList;
import java.util.List;


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

    private void createMenuBar(){
        JMenuBar menubar = new JMenuBar();
        JMenu actions = new JMenu("Actions");
        JMenuItem exitMenuItem = createMenuItem("Exit", RestoAppActions.EXIT_ACTION);
        JMenuItem addTableMenuItem = createMenuItem("Add Table", this::addTableAction);
        JMenuItem changeTableMenuItem = createMenuItem("Change Table", this::updateTableAction);
        JMenuItem moveTableMenuItem = createMenuItem("Move Table", this::moveTableAction);
        JMenuItem removeTableMenuItem = createMenuItem("Remove Table", this::removeTableAction);
        JMenuItem menuMenuItem = createMenuItem("Display Menu", this::displayMenuAction);
        JMenuItem startOrderMenuItem = createMenuItem("Start Order", this::displayMenuAction);
        
        actions.add(addTableMenuItem);
        actions.add(changeTableMenuItem);
        actions.add(moveTableMenuItem);
        actions.add(removeTableMenuItem);
        actions.add(menuMenuItem);
        actions.add(exitMenuItem);
        menubar.add(actions);
        setJMenuBar(menubar);
        actions.add(startOrderMenuItem);
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
        JButton exitButton = createButton("power.png","Exit App [Alt + Q]", KeyEvent.VK_Q, RestoAppActions.EXIT_ACTION);
        JButton addTableButton = createButton("addTable.png", "Add Table [Alt + A]", KeyEvent.VK_A, this::addTableAction);
        JButton changeTableButton = createButton("updateTable.png", "Change Table [Alt + U]", KeyEvent.VK_U, this::updateTableAction); 
        JButton moveTableButton = createButton("moveTable.png", "Move Table [Alt + M]", KeyEvent.VK_M, this::moveTableAction); 
        JButton removeTableButton = createButton("removeTable.png", "Remove Table [Alt + X]", KeyEvent.VK_X, this::removeTableAction);
        JButton displayMenuButton = createButton("displayMenu.png", "Display Menu [Alt + D]", KeyEvent.VK_D, this::displayMenuAction);
        JButton startOrderButton = createButton("startOrder.png", "Start Order [Alt + O]", KeyEvent.VK_O, this::startOrderAction);
        
        toolbar.add(exitButton);
        toolbar.add(addTableButton);
        toolbar.add(changeTableButton);
        toolbar.add(moveTableButton);
        toolbar.add(removeTableButton);
        toolbar.add(displayMenuButton);
        toolbar.add(startOrderButton);
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
    private void startOrderAction(ActionEvent event){
        JPanel panel = new JPanel(new GridLayout(2, 2, 5,5));
        
        //create array of current table numbers
        int currentLength = RestoController.getCurrentTables().size();
        String currentTableNums[] = new String[currentLength];
        for (int i = 0; i < currentLength; i++){
        		currentTableNums[i] = "" + RestoController.getCurrentTable(i).getNumber();
        }

        panel.add(new JLabel("Select tables for order:"));
        DefaultListModel<String> listTableNums = new DefaultListModel<>();
        for(int i=0; i<currentTableNums.length;i++) { //fill list for UI with wanted list (element per element)
        		listTableNums.addElement(currentTableNums[i]);
        }
        JList<String> allTablesList  = new JList<>(listTableNums); //now list has table nums of current tables
        allTablesList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        
        //SCROLLBAR:
        allTablesList.setVisibleRowCount(3);
        JScrollPane scrollPane = new JScrollPane(allTablesList);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        //panel.add(allTablesList); with new scrollbar, this is replaced by add scrollpane below
        panel.add(scrollPane);
        int result = JOptionPane.showConfirmDialog(null, panel, "Start Order",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION){
        		try{
        			List<String> selectedNumbers = allTablesList.getSelectedValuesList();
        			List<Table> selectedTables = new ArrayList<Table>();
        			for(int i=0; i< selectedNumbers.size(); i++) {
        				int tableNum = Integer.parseInt((String) selectedNumbers.get(i));
        				Table selectedTable = RestoController.getTableByNum(tableNum);
        				selectedTables.add(selectedTable);
        			}
                RestoController.startOrder(selectedTables);
            	
                tablePanel.revalidate();
                tablePanel.repaint();

                JOptionPane.showMessageDialog(null, "Order started successfully.");
                }
                catch (Exception error)
                {
                    JOptionPane.showMessageDialog(
                            null,
                            error.getMessage(),
                            "Could not start order",
                            JOptionPane.ERROR_MESSAGE);
                    
                }
        } 
        else { 
        	JOptionPane.showMessageDialog(null, "No orders were started."); 
        	}
    	
    }
    private void displayMenuAction(ActionEvent event){
        JFrame f = new JFrame("menu");
        JPanel menuPanel = new MenuPanel();
        //JPanel p = new MenuPanel();

        f.add(menuPanel);
        f.setSize(400,150);
        f.setLayout(new FlowLayout());
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);

        menuPanel.validate();
        menuPanel.repaint();


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
	    	JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));
	    	
	    	//create array of current table numbers
	    	int currentLength = RestoController.getCurrentTables().size();
	    	String currentTableNums[] = new String[currentLength];
	    	for (int i = 0; i < currentLength; i++){
	    		currentTableNums[i] = "" + RestoController.getCurrentTable(i).getNumber();
	    	}
	
	    	panel.add(new JLabel("Select table to move:"));
	    	JComboBox<String> allTablesList = new JComboBox<String>(currentTableNums);
	    	panel.add(allTablesList);  
	    	
    		panel.add(new JLabel("New X:"));
        JTextField newXField = new JTextField();
        panel.add(newXField);
        
        panel.add(new JLabel("New Y:"));
        JTextField newYField = new JTextField();
        panel.add(newYField);
        
        int result = JOptionPane.showConfirmDialog(null, panel, "Move Table",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION){
        		try{
        			int tableNum = Integer.parseInt((String) allTablesList.getSelectedItem());
        			Table selectedTable = RestoController.getTableByNum(tableNum);
        			int newX = parseInt(newXField.getText());
        			int newY = parseInt(newYField.getText());
    
                RestoController.moveTable(selectedTable,newX, newY);
            	
                tablePanel.revalidate();
                tablePanel.repaint();

                JOptionPane.showMessageDialog(null, "Table moved successfully.");
                }
                catch (Exception error)
                {
                    JOptionPane.showMessageDialog(
                            null,
                            error.getMessage(),
                            "Could not move table",
                            JOptionPane.ERROR_MESSAGE);
                }
        } 
        else { 
        	JOptionPane.showMessageDialog(null, "No tables were moved."); 
        	}
        
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
