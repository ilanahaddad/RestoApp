package ca.mcgill.ecse223.resto.view;

import static java.lang.Integer.parseInt;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.Time;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JDialog;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;

import com.github.lgooddatepicker.components.TimePicker;
import com.github.lgooddatepicker.components.TimePickerSettings;

import org.jdesktop.swingx.JXDatePicker;

import ca.mcgill.ecse223.resto.application.RestoAppApplication;
import ca.mcgill.ecse223.resto.controller.RestoController;
import ca.mcgill.ecse223.resto.controller.StatisticsItem;
import ca.mcgill.ecse223.resto.controller.StatisticsTable;
import ca.mcgill.ecse223.resto.model.MenuItem;
import ca.mcgill.ecse223.resto.model.Order;
import ca.mcgill.ecse223.resto.model.OrderItem;
import ca.mcgill.ecse223.resto.model.Seat;
import ca.mcgill.ecse223.resto.model.Table;
import ca.mcgill.ecse223.resto.model.Bill;

public class RestoAppPage extends JFrame {
	public HashMap<String, Seat> hmap = RestoController.generateHashMap();
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
	
	public RestoAppPage() {
		setNiceTheme();
		initUI();
	}
	
	private void initUI() {
		configureUI();
		createMenuBar();
		createToolBar();
		createTablePanel();
	}
	
	private void createTablePanel() {
		tablePanel = new TablePanel();
		JScrollPane scrollbar = new JScrollPane(tablePanel);
		
		configureScrollbar(scrollbar);
		add(scrollbar);
	}
	
	private void createMenuBar() {
		// TODO before submitting split into different tabs (system, table, menu)
		JMenuBar menubar = new JMenuBar();
		JMenu actions = new JMenu("Actions");
		JMenu manageBillsMenuItem = new JMenu("Bills");
		JMenu manageTablesMenuItem = new JMenu("Tables");
		JMenu manageOrderMenuItem = new JMenu("Orders");
		JMenuItem exitMenuItem = createMenuItem("Exit", RestoAppActions.EXIT_ACTION);
		JMenuItem addTableMenuItem = createMenuItem("Add Table", this::addTableAction);
		JMenuItem changeTableMenuItem = createMenuItem("Change Table", this::updateTableAction);
		JMenuItem moveTableMenuItem = createMenuItem("Move Table", this::moveTableAction);
		JMenuItem removeTableMenuItem = createMenuItem("Remove Table", this::removeTableAction);
		JMenuItem menuMenuItem = createMenuItem("Display Menu", this::displayMenuAction);
		JMenuItem reservationMenuItem = createMenuItem("Make Reservation", this::reserveTableAction);
		JMenuItem startOrderMenuItem = createMenuItem("Start Order", this::startOrderAction);
		JMenuItem viewOrderMenuItem = createMenuItem("View Order", this::viewOrderAction);
		JMenuItem endOrderMenuItem = createMenuItem("End Order", this::endOrderAction);
		JMenuItem orderMenuItem = createMenuItem("Order Menu Item", this::orderMenuItemAction);
		JMenuItem newBillMenuItem = createMenuItem("New Bill", this::newBillAction);
        JMenuItem viewBillMenuItem = createMenuItem("View Bill", this::viewBillAction);
        JMenuItem cancelBillMenuItem = createMenuItem("Cancel Bill", this::cancelBillAction);
		JMenuItem businessStatistics = createMenuItem("Business Statistics", this::businessStatisticsAction);

		JMenuItem cancelOrderItem = createMenuItem("Cancel Order Item", this::cancelOrderItemAction);
		JMenuItem cancelOrder = createMenuItem("Cancel Order", this::cancelOrderAction);

		actions.add(businessStatistics);
		actions.add(orderMenuItem);
		actions.add(endOrderMenuItem);
		actions.add(viewOrderMenuItem);
		actions.add(startOrderMenuItem);
		actions.add(reservationMenuItem);
		actions.add(addTableMenuItem);
		actions.add(changeTableMenuItem);
		actions.add(moveTableMenuItem);
		actions.add(removeTableMenuItem);
		actions.add(menuMenuItem);
		actions.add(reservationMenuItem);
		actions.add(manageTablesMenuItem);
		actions.add(manageOrderMenuItem);
        actions.add(manageBillsMenuItem);
		actions.add(businessStatistics);
		actions.add(exitMenuItem);
		menubar.add(actions);
		
		manageTablesMenuItem.add(addTableMenuItem);
		manageTablesMenuItem.add(changeTableMenuItem);
		manageTablesMenuItem.add(moveTableMenuItem);
		manageTablesMenuItem.add(removeTableMenuItem);
		
		manageOrderMenuItem.add(startOrderMenuItem);
		manageOrderMenuItem.add(orderMenuItem);
		manageOrderMenuItem.add(viewOrderMenuItem);
		manageOrderMenuItem.add(cancelOrderItem);
		manageOrderMenuItem.add(cancelOrder);
		manageOrderMenuItem.add(endOrderMenuItem);
		
		manageBillsMenuItem.add(newBillMenuItem);
        manageBillsMenuItem.add(viewBillMenuItem);
        manageBillsMenuItem.add(cancelBillMenuItem);
		setJMenuBar(menubar);
	}
	
	private JMenuItem createMenuItem(String itemName, ActionListener action) {
		JMenuItem menuItem = new JMenuItem(itemName);
		menuItem.addActionListener(action);
		return menuItem;
	}
	
	private void createToolBar() {
		JToolBar toolbar = new JToolBar();
		JButton exitButton = createButton("power.png", "Exit App [Alt + Q]", KeyEvent.VK_Q,
		RestoAppActions.EXIT_ACTION);
		JButton addTableButton = createButton("addTable.png", "Add Table [Alt + A]", KeyEvent.VK_A,
		this::addTableAction);
		JButton changeTableButton = createButton("updateTable.png", "Change Table [Alt + U]", KeyEvent.VK_U,
		this::updateTableAction);
		JButton moveTableButton = createButton("moveTable.png", "Move Table [Alt + M]", KeyEvent.VK_M,
		this::moveTableAction);
		JButton removeTableButton = createButton("removeTable.png", "Remove Table [Alt + X]", KeyEvent.VK_X,
		this::removeTableAction);
		JButton displayMenuButton = createButton("displayMenu.png", "Display Menu [Alt + D]", KeyEvent.VK_D,
		this::displayMenuAction);
		JButton reserveTableButton = createButton("reserveTable.png", "Make Reservation [Alt + R]", KeyEvent.VK_R,
		this::reserveTableAction);
		JButton businessStatisticsButton = createButton("businessStatistics.png", "View Business Statistics [Alt + 1]", KeyEvent.VK_1,
		this::businessStatisticsAction);
		JButton billManagerButton = createButton("issueBill.png", "Start Bill Manager [Alt + B]", KeyEvent.VK_B, this::billManagerAction);
		JButton orderManagerButton = createButton("orderManager.png", "Start Order Manager [Alt + O]", KeyEvent.VK_U, this::orderManagerAction);
		
		toolbar.add(exitButton);
		toolbar.add(addTableButton);
		toolbar.add(changeTableButton);
		toolbar.add(moveTableButton);
		toolbar.add(removeTableButton);
		toolbar.add(displayMenuButton);
		toolbar.add(reserveTableButton);
		toolbar.add(businessStatisticsButton);
		toolbar.add(orderManagerButton);
		toolbar.add(billManagerButton);
		add(toolbar, BorderLayout.NORTH);
	}
	
	private JButton createButton(String iconName, String buttonTip, int shortcut, ActionListener action) {
		Path iconPath = Paths.get(RESSOURCES_PATH + iconName).toAbsolutePath();
		ImageIcon icon = new ImageIcon(iconPath.toString());
		JButton button = new JButton(icon);
		
		button.setMnemonic(shortcut);
		button.setToolTipText(buttonTip);
		button.addActionListener(action);
		return button;
	}
	
	private void updateScrollbarMax(int x, int y) {
		if (maxX < x * UNIT_LENGTH) {
			maxX = x * UNIT_LENGTH;
		}
		if (maxY < y * UNIT_LENGTH) {
			maxY = y * UNIT_LENGTH;
		}
		
		int xLimit = (maxX > GUI_WIDTH) ? maxX + UNIT_LENGTH : GUI_WIDTH;
		int yLimit = (maxY > GUI_HEIGHT) ? maxY + UNIT_LENGTH : GUI_HEIGHT;
		tablePanel.setPreferredSize(new Dimension(xLimit, yLimit));
	}
	
	private void setNiceTheme() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception error) {
			JOptionPane.showMessageDialog(null, error.getMessage(), "Could not set theme", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void configureUI() {
		setTitle(GUI_TITLE);
		setSize(GUI_WIDTH, GUI_HEIGHT);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
    private void orderManagerAction(ActionEvent event){
    	JFrame f = new JFrame("Order Manager");
    	JPanel panel = new JPanel(new GridLayout(2, 2, 5,5));
		JButton startOrderButton = createButton("startOrder.png", "Start Order [Alt + O]", KeyEvent.VK_O,
		this::startOrderAction);
		JButton orderMenuItemButton = createButton("orderMenuItem.png", "Order Menu Item [Alt + I]", KeyEvent.VK_I,
		this::orderMenuItemAction);
		JButton viewOrderButton = createButton("viewOrder.png", "View Order [Alt + V]", KeyEvent.VK_V, 
		this::viewOrderAction);
		JButton cancelOrderItemButton = createButton("cancelOrderItem.png", "Cancel Order Item [Alt + C]", KeyEvent.VK_E, this::cancelOrderItemAction);
		JButton cancelOrderButton = createButton("cancelOrder.png", "Cancel Order [Alt + F]", KeyEvent.VK_E, this::cancelOrderAction);
		JButton endOrderButton = createButton("endOrder.png", "Start Order [Alt + E]", KeyEvent.VK_E,
		this::endOrderAction);
        panel.add(startOrderButton);
        panel.add(viewOrderButton);
        panel.add(orderMenuItemButton);
        panel.add(cancelOrderItemButton);
        panel.add(cancelOrderButton);
        panel.add(endOrderButton);
        
		f.add(panel);
		f.setSize(400,150);
		// f.setLayout(new FlowLayout());
		f.setLocationRelativeTo(null);
		f.setVisible(true);
		f.setResizable(false);
		panel.validate();
		panel.repaint();
        }
	
	
	private void configureScrollbar(JScrollPane scrollbar) {
		scrollbar.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollbar.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollbar.getVerticalScrollBar().setUnitIncrement(SCROLLBAR_SPEED);
		scrollbar.getHorizontalScrollBar().setUnitIncrement(SCROLLBAR_SPEED);
		
		updateScrollbarMax(RestoController.getMaxX(), RestoController.getMaxY());
	}
	
/*	private void tempCancelOrderItemAction(ActionEvent event) {
		JPanel panel = new JPanel(new GridLayout(2, 2, 5,5));
		List<OrderItem> orderItems = new ArrayList<OrderItem>();
		for (Table t: RestoController.getCurrentTables()) {
			for(Seat s: t.getCurrentSeats()) {
				for(OrderItem oi: s.getOrderItems()) {
					if (!orderItems.contains(oi)) {
						orderItems.add(oi);
					}
				}
			}
		}
		if(orderItems != null) {
			
			String orderItemsNames[] = new String[orderItems.size()];
			int i = 0;
			for (OrderItem oi: orderItems) {
				String name = oi.getPricedMenuItem().getMenuItem().getName();
				orderItemsNames[i] = "" + i + " " + name;
				for (Seat s: oi.getSeats()) {
					orderItemsNames[i] = orderItemsNames[i] + " " + RestoController.getKeyForSeat(s);
				}
				i++;
			}
		}	
		else {
			JOptionPane.showMessageDialog(
					null,
					"RestoApp currently has no seats with order items",
					"Cancel Order Items Currently Unavailable",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	*/
	
	private void cancelOrderItemAction(ActionEvent event) { //author: ilanahaddad
		JPanel panel = new JPanel(new GridLayout(2, 2, 5,5));

		List<String> orderItems = RestoController.getAllCurrentOrderItemsNamesWithSeatKey();
		//List<String> orderItems = RestoController.getAllCurrentOrderItemsNamesWithSeatKey2();
		if(orderItems != null) {
			displayCancelOrderItemAction(panel,orderItems);
		}
		else {
			JOptionPane.showMessageDialog(
					null,
					"RestoApp currently has no seats with order items",
					"Cancel Order Items Currently Unavailable",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}
	private void displayCancelOrderItemAction(JPanel panel, List<String> orderItems) { //author: ilanahaddad
		int numOrderItems = orderItems.size();
		//create array to fill combo box with menu items ordered:
		String orderItemsArray[] = new String[numOrderItems];
		for(int i=0; i< numOrderItems; i++) {
			orderItemsArray[i] = orderItems.get(i);
		}

		panel.add(new JLabel("Select order item to cancel:"));
		JComboBox<String> orderItemsBox = new JComboBox<String>(orderItemsArray);
		panel.add(orderItemsBox);

		int result = JOptionPane.showConfirmDialog(null, panel, "Cancel Order Item",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION){
			try{
				int indexSelected = orderItemsBox.getSelectedIndex();
				String s = orderItemsArray[indexSelected];
				OrderItem orderItemSelected = RestoController.getOrderItemFromNameWithSeatKey(s);
				RestoController.cancelOrderItem(orderItemSelected);
				/*
				String orderItemWithSeatSelected = (String) orderItemsBox.getSelectedItem();
				OrderItem orderItemSelected = RestoController.getOrderItemFromNameWithSeatKey(orderItemWithSeatSelected);
				RestoController.cancelOrderItem(orderItemSelected);*/
				
				tablePanel.revalidate();
				tablePanel.repaint();

				JOptionPane.showMessageDialog(null, "Order items cancelled successfully.");
			}
			catch (Exception error){
				error.printStackTrace();
				JOptionPane.showMessageDialog(
						null,
						error.getMessage(),
						"Could not cancel order item",
						JOptionPane.ERROR_MESSAGE);
			}
		}
		else {
			JOptionPane.showMessageDialog(null, "No order items were cancelled.");
		}

	}
	/*
	private void displayCancelOrderItemAction(JPanel panel, List<MenuItem> menuItemsOrdered) { //author: ilanahaddad
		int numMenuItems = menuItemsOrdered.size();
		//create array to fill combo box with menu items ordered:
		String menuItemNamesArray[] = new String[numMenuItems];
		for(int i=0; i< numMenuItems; i++) {
			menuItemNamesArray[i] = menuItemsOrdered.get(i).getName();
		}

		panel.add(new JLabel("Select order item to cancel:"));
		JComboBox<String> menuItemsOrderedList = new JComboBox<String>(menuItemNamesArray);
		panel.add(menuItemsOrderedList);

		int result = JOptionPane.showConfirmDialog(null, panel, "Cancel Order Item",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION){
			try{
				String menuItemNameSelected = (String) menuItemsOrderedList.getSelectedItem();
				OrderItem orderItemSelected = RestoController.getOrderItemFromMenuItemName(menuItemNameSelected);
				RestoController.cancelOrderItem(orderItemSelected);

				tablePanel.revalidate();
				tablePanel.repaint();

				JOptionPane.showMessageDialog(null, "Order items cancelled successfully.");
			}
			catch (Exception error){
				error.printStackTrace();
				JOptionPane.showMessageDialog(
						null,
						error.getMessage(),
						"Could not cancel order item",
						JOptionPane.ERROR_MESSAGE);
			}
		}
		else {
			JOptionPane.showMessageDialog(null, "No order items were cancelled.");
		}

	}*/
	private void cancelOrderAction(ActionEvent event) { //author: ilanahaddad
		JPanel panel = new JPanel(new GridLayout(2, 2, 5,5));

		//create array of current table numbers
		int currentLength = RestoController.getCurrentTables().size();
		String currentTableNums[] = new String[currentLength];
		for (int i = 0; i < currentLength; i++){
			currentTableNums[i] = "" + RestoController.getCurrentTable(i).getNumber();
		}

		panel.add(new JLabel("Select table to cancel all order items for:"));
		JComboBox<String> allTablesList = new JComboBox<String>(currentTableNums);
		panel.add(allTablesList);

		int result = JOptionPane.showConfirmDialog(null, panel, "Cancel Order",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION){
			try{
				int tableNum = Integer.parseInt((String) allTablesList.getSelectedItem());
				Table selectedTable = RestoController.getTableByNum(tableNum);
				if(!selectedTable.hasOrders()) {
					JOptionPane.showMessageDialog(null, "No orders were cancelled because table has no orders.");
					return;
				}
				RestoController.cancelOrder(selectedTable);

				tablePanel.revalidate();
				tablePanel.repaint();

				JOptionPane.showMessageDialog(null, "Order cancelled successfully.");
			}
			catch (Exception error)
			{
				error.printStackTrace();
				JOptionPane.showMessageDialog(
						null,
						error.getMessage(),
						"Could cancel order",
						JOptionPane.ERROR_MESSAGE);
			}

		}
		else {
			JOptionPane.showMessageDialog(null, "No orders were cancelled.");
		}
	}
	
	private void reserveTableAction(ActionEvent event) {
		JPanel panel = new JPanel(new GridLayout(7, 4, 5, 5));
		
		// create array of current table numbers
		int currentLength = RestoController.getCurrentTables().size();
		String currentTableNums[] = new String[currentLength];
		for (int i = 0; i < currentLength; i++) {
			currentTableNums[i] = "" + RestoController.getCurrentTable(i).getNumber();
		}
		
		panel.add(new JLabel("Select tables to reserve:"));
		DefaultListModel<String> listTableNums = new DefaultListModel<>();
		for (int i = 0; i < currentTableNums.length; i++) { // fill list for UI with wanted list (element per element)
			listTableNums.addElement(currentTableNums[i]);
		}
		JList<String> allTablesList = new JList<>(listTableNums); // now list has table nums of current tables
		allTablesList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		allTablesList.setVisibleRowCount(3);
		JScrollPane scrollPane = new JScrollPane(allTablesList);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		panel.add(scrollPane);
		// panel.add(allTablesList);
		
		panel.add(new JLabel("Date:"));
		
		JXDatePicker picker = new JXDatePicker();
		picker.setDate(Calendar.getInstance().getTime());
		picker.setFormats(new SimpleDateFormat("dd.MM.yyyy"));
		panel.add(picker);
		
		panel.add(new JLabel("Time:"));
		TimePickerSettings timeSettings = new TimePickerSettings();
		timeSettings.setColor(TimePickerSettings.TimeArea.TimePickerTextValidTime, Color.black);
		timeSettings.generatePotentialMenuTimes(TimePickerSettings.TimeIncrement.OneHour, LocalTime.of(9, 0),
		LocalTime.of(23, 0));
		timeSettings.initialTime = LocalTime.now();
		TimePicker timePicker = new TimePicker(timeSettings);
		add(timePicker);
		panel.add(timePicker);
		
		panel.add(new JLabel("Number of people:"));
		JTextField numPeopleField = new JTextField();
		numPeopleField.setAutoscrolls(true);
		// numPeopleField.setVisibleRowCount(2);
		// numPeopleField.setPreferredSize(new Dimension(1,1));
		// numPeopleField.setBounds(0, 0, 5, 5);
		panel.add(numPeopleField);
		
		panel.add(new JLabel("Contact name:"));
		JTextField nameField = new JTextField();
		panel.add(nameField);
		
		panel.add(new JLabel("Contact e-mail:"));
		JTextField emailField = new JTextField();
		panel.add(emailField);
		
		panel.add(new JLabel("Contact phone number:"));
		JTextField phoneField = new JTextField();
		panel.add(phoneField);
		
		int result = JOptionPane.showConfirmDialog(null, panel, "Add Table", JOptionPane.OK_CANCEL_OPTION,
		JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			try {
				
				Date date = new Date(picker.getDate().getTime());
				Time time = Time.valueOf(timePicker.getTime());
				int numberInParty = 0;
				try { // need a try/catch here because if no number is inputed, an
					// un-informative error gets thrown here for trying to parse an empty string
					numberInParty = parseInt((String) numPeopleField.getText());
				} catch (Exception error) {
					
				}
				String contactName = nameField.getText();
				String contactEmailAddress = emailField.getText();
				String contactPhoneNumber = phoneField.getText();
				List<String> selectedNumbers = allTablesList.getSelectedValuesList();
				List<Table> selectedTables = new ArrayList<Table>();
				for (int i = 0; i < selectedNumbers.size(); i++) {
					int tableNum = Integer.parseInt((String) selectedNumbers.get(i));
					Table selectedTable = RestoController.getTableByNum(tableNum);
					selectedTables.add(selectedTable);
				}
				
				RestoController.reserveTable(date, time, numberInParty, contactName, contactEmailAddress,
				contactPhoneNumber, selectedTables);
				
				tablePanel.revalidate();
				tablePanel.repaint();
				
				JOptionPane.showMessageDialog(null, "Reservation made successfully.");
			} catch (Exception error) {
				JOptionPane.showMessageDialog(null, error.getMessage(), "Reservation was not made.",
				JOptionPane.ERROR_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(null, "No reservation made.");
		}
		
	}
	
	private void startOrderAction(ActionEvent event) {
		JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
		
		// create array of current table numbers
		int currentLength = RestoController.getCurrentTables().size();
		String currentTableNums[] = new String[currentLength];
		for (int i = 0; i < currentLength; i++) {
			currentTableNums[i] = "" + RestoController.getCurrentTable(i).getNumber();
		}
		
		panel.add(new JLabel("Select tables for order:"));
		DefaultListModel<String> listTableNums = new DefaultListModel<>();
		for (int i = 0; i < currentTableNums.length; i++) { // fill list for UI with wanted list (element per element)
			listTableNums.addElement(currentTableNums[i]);
		}
		JList<String> allTablesList = new JList<>(listTableNums); // now list has table nums of current tables
		allTablesList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
		// SCROLLBAR:
		allTablesList.setVisibleRowCount(3);
		JScrollPane scrollPane = new JScrollPane(allTablesList);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		// panel.add(allTablesList); with new scrollbar, this is replaced by add
		// scrollpane below
		panel.add(scrollPane);
		int result = JOptionPane.showConfirmDialog(null, panel, "Start Order", JOptionPane.OK_CANCEL_OPTION,
		JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			try {
				List<String> selectedNumbers = allTablesList.getSelectedValuesList();
				List<Table> selectedTables = new ArrayList<Table>();
				for (int i = 0; i < selectedNumbers.size(); i++) {
					int tableNum = Integer.parseInt((String) selectedNumbers.get(i));
					Table selectedTable = RestoController.getTableByNum(tableNum);
					selectedTables.add(selectedTable);
				}
				RestoController.startOrder(selectedTables);
				
				tablePanel.revalidate();
				tablePanel.repaint();
				
				JOptionPane.showMessageDialog(null, "Order started successfully.");
			} catch (Exception error) {
				JOptionPane.showMessageDialog(null, error.getMessage(), "Could not start order",
				JOptionPane.ERROR_MESSAGE);
				
			}
		} else {
			JOptionPane.showMessageDialog(null, "No orders were started.");
		}
		
	}
	
	private void viewOrderAction(ActionEvent event){
		JFrame f = new JFrame("View Order");
		JPanel panel = new JPanel();
		
		GridBagLayout grid = new GridBagLayout();  
		GridBagConstraints gbc = new GridBagConstraints();  
		panel.setLayout(grid);  
		
		//create array of current table numbers
		int currentLength = RestoController.getCurrentTables().size() + 1;
		String currentTableNums[] = new String[currentLength];
		currentTableNums[0] = " --- ";
		for (int i = 1; i < currentLength; i++){
			currentTableNums[i] = "" + RestoController.getCurrentTable(i-1).getNumber();
		}
		
		JTextArea display  = new JTextArea(20, 60);
		display.setEditable (false);
		JScrollPane scroll = new JScrollPane(display);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		JComboBox<String> allTablesList = new JComboBox<String>(currentTableNums);
		allTablesList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					JComboBox comboBox = (JComboBox) event.getSource();
					int tableNum = Integer.parseInt((String) allTablesList.getSelectedItem());
					Table selectedTable = RestoController.getTableByNum(tableNum);
					String formattedOrderItems = getPrettyOrderItems(RestoController.getOrderItems(selectedTable));
					display.setText(formattedOrderItems);
				}catch (NumberFormatException e) {
				}catch (Exception error)
				{
					JOptionPane.showMessageDialog(
					null,
					error.getMessage(),
					"Could not retrieve table's order",
					JOptionPane.ERROR_MESSAGE);
				}
			}
		});       
		
		gbc.ipady = 0;  
		gbc.gridx = 0;  
		gbc.gridy = 0;  
		gbc.gridwidth = 2;
		gbc.insets = new Insets(10,0,0,0);
		panel.add(new JLabel("Select table:"), gbc);  
		
		
		gbc.gridx = 0;  
		gbc.gridy = 1;  
		gbc.gridwidth = 2;
		panel.add(allTablesList, gbc);  
		
		gbc.gridx = 0;  
		gbc.gridy = 3;  
		gbc.gridwidth = 2;  
		panel.add(scroll, gbc);  
		
		f.add(panel);
		f.setSize(500,500);
		// f.setLayout(new FlowLayout());
		f.pack();
		f.setLocationRelativeTo(null);
		f.setVisible(true);
		
		panel.validate();
		panel.repaint();
	}
	
	private String getPrettyOrderItems(List<OrderItem> orderItems) {
		String output = "";
		String appetizers = "APPETIZERS\n";
		String main = "MAIN\n";
		String dessert = "DESSERT\n";
		String alcoholicBev = "ALCOHOLIC BEVERAGES\n";
		String nonAlcoholicBev = "NON-ALCOHOLIC BEVERAGES\n";

		DecimalFormat df = new DecimalFormat("#.##");

		for (OrderItem oi : orderItems)
		{
			int qty = oi.getQuantity();
			double unitPrice = oi.getPricedMenuItem().getPrice();
			String totalPrice = df.format(qty * unitPrice);
			MenuItem menuItem = oi.getPricedMenuItem().getMenuItem();
			switch (menuItem.getItemCategory()) {
				case Appetizer: 
				appetizers += qty + "x " + menuItem.getName() + " [" + df.format(unitPrice) + "$]" + "\t" + totalPrice + "$\n";
				break;
				case Main: 
				main += qty + "x " + menuItem.getName() + " [" + df.format(unitPrice) + "$]" + "\t" + totalPrice + "$\n";
				break;
				case Dessert:
				dessert += qty + "x " + menuItem.getName() + " [" + df.format(unitPrice) + "$]" + "\t" + totalPrice + "$\n";
				break;
				case AlcoholicBeverage: 
				alcoholicBev += qty + "x " + menuItem.getName() + " [" + df.format(unitPrice) + "$]" + "\t" + totalPrice + "$\n";
				break;
				case NonAlcoholicBeverage: 
				nonAlcoholicBev += qty + "x " + menuItem.getName() + " [" + df.format(unitPrice) + "$]" + "\t" + totalPrice + "$\n";
				break;
			}
		}
		
		output = appetizers + "\n" + main + "\n" + dessert + "\n" + alcoholicBev + "\n" + nonAlcoholicBev;
		
		return output;
	}
	
	private void endOrderAction(ActionEvent event) {
		JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
		
		// create array of current orders
		int numOrders = RestoController.getCurrentOrders().size();
		if (numOrders > 0) {
			displayEndOrderAction(panel, numOrders);
		} else {
			JOptionPane.showMessageDialog(null, "RestoApp currently has no table with orders",
			"No Orders currently available", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	private void displayEndOrderAction(JPanel panel, int numOrders) {
		String currOrderNums[] = new String[numOrders];
		int i = 0;
		for (Order o : RestoController.getCurrentOrders()) {
			currOrderNums[i++] = "" + o.getNumber();
		}
		
		panel.add(new JLabel("Select order to end:"));
		DefaultListModel<String> orderNumList = new DefaultListModel<>();
		for (i = 0; i < currOrderNums.length; i++) {
			orderNumList.addElement(currOrderNums[i]);
		}
		JList<String> activeOrders = new JList<>(orderNumList);
		activeOrders.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		panel.add(activeOrders);
		
		int result = JOptionPane.showConfirmDialog(null, panel, "End Order", JOptionPane.OK_CANCEL_OPTION,
		JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			try {
				int selectedNum = Integer.parseInt(activeOrders.getSelectedValuesList().get(0));
				Order selectedOrder = RestoController.getCurrentOrder(selectedNum);
				
				RestoController.endOrder(selectedOrder);
				
				tablePanel.revalidate();
				tablePanel.repaint();
				
				JOptionPane.showMessageDialog(null, "Order ended successfully.");
			} catch (Exception error) {
				JOptionPane.showMessageDialog(null, error.getMessage(), "Could not end order",
				JOptionPane.ERROR_MESSAGE);
				error.printStackTrace();
			}
		} else {
			JOptionPane.showMessageDialog(null, "No order was ended.");
		}
	}
	
    private void billManagerAction(ActionEvent event){
    	JFrame f = new JFrame("Bill Manager");
    	JPanel panel = new JPanel(new GridLayout(2, 2, 5,5));
        JButton newBill = new JButton("New Bill");
        newBill.addActionListener(this::newBillAction);
        panel.add(newBill);
        JButton viewBill = new JButton("View Bill");
        viewBill.addActionListener(this::viewBillAction);
        panel.add(viewBill);
        JButton cancelBill = new JButton("Cancel Bill");
        cancelBill.addActionListener(this::cancelBillAction);
        panel.add(cancelBill);
    	f.add(panel);
		f.setSize(400,150);
		// f.setLayout(new FlowLayout());
		f.setLocationRelativeTo(null);
		f.setVisible(true);
		f.setResizable(false);
		panel.validate();
		panel.repaint();
        }
    
    private void newBillAction(ActionEvent event){
    	JPanel panel = new JPanel(new GridLayout(2, 2, 5,5));

        int currentTableLength = RestoController.getCurrentTables().size();
        String currentTableNums[] = new String[currentTableLength];
        for (int i = 0; i < currentTableLength; i++){
            currentTableNums[i] = "" + RestoController.getCurrentTable(i).getNumber();
        }


        int currentSeatLength = RestoController.hmap.keySet().size();
        String currentSeats[] = new String[currentSeatLength];
        int count = 0;
        for (String s: RestoController.hmap.keySet()) {
        	currentSeats[count] = s;
        	count++;
        }
        panel.add(new JLabel("Select seats to add to Bill:"));
        panel.add(new JLabel("Select tables to add to Bill:"));
        panel.add(new JLabel("Select orders to add to Bill:"));
        DefaultListModel<String> listSeatNums = new DefaultListModel<>();
        for(int i=0; i<currentSeats.length;i++) { //fill list for UI with wanted list (element per element)
            listSeatNums.addElement(currentSeats[i]);
        }
        JList<String> allSeatsList  = new JList<>(currentSeats); //now list has table nums of current tables
        allSeatsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        //SCROLLBAR:
        allSeatsList.setVisibleRowCount(3);
        JScrollPane scrollPaneSeats = new JScrollPane(allSeatsList);
        scrollPaneSeats.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        //panel.add(allTablesList); with new scrollbar, this is replaced by add scrollpane below
        panel.add(scrollPaneSeats);

        

        DefaultListModel<String> listTableNums = new DefaultListModel<>();
        for(int i=0; i<currentTableNums.length;i++) { //fill list for UI with wanted list (element per element)
            listTableNums.addElement(currentTableNums[i]);
        }
        JList<String> allTablesList  = new JList<>(listTableNums); //now list has table nums of current tables
        allTablesList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        //SCROLLBAR:
        allTablesList.setVisibleRowCount(3);
        JScrollPane scrollPaneTable = new JScrollPane(allTablesList);
        scrollPaneTable.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        //panel.add(allTablesList); with new scrollbar, this is replaced by add scrollpane below
        panel.add(scrollPaneTable);
    	
      //create array of current order numbers
        int currentOrderLength = RestoController.getCurrentOrders().size();
        String currentOrderNums[] = new String[currentOrderLength];
        List<Order> currentOrders = RestoController.getCurrentOrders();
        for (int i = 0; i < currentOrderLength; i++){
            currentOrderNums[i] = "O" + currentOrders.get(i).getNumber()+ " ";
            for (Table t: currentOrders.get(i).getTables()) {
            	currentOrderNums[i] = currentOrderNums[i] + " T" + t.getNumber();
            }
        }

        DefaultListModel<String> listOrderNums = new DefaultListModel<>();
        for(int i=0; i<currentOrderNums.length;i++) { //fill list for UI with wanted list (element per element)
            listOrderNums.addElement(currentOrderNums[i]);
        }
        JList<String> allOrdersList  = new JList<>(listOrderNums); //now list has order nums of current tables
        allOrdersList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        //SCROLLBAR:
        allOrdersList.setVisibleRowCount(3);
        JScrollPane scrollPaneOrder = new JScrollPane(allOrdersList);
        scrollPaneOrder.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        //panel.add(allTablesList); with new scrollbar, this is replaced by add scrollpane below
        panel.add(scrollPaneOrder);
    	

        
        int result = JOptionPane.showConfirmDialog(null, panel, "New Bill",
        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION){
            try{
            	List<String> selectedTablesNums = allTablesList.getSelectedValuesList();
            	List<String> selectedOrderNums = allOrdersList.getSelectedValuesList();
                List<String> selectedSeatNums = allSeatsList.getSelectedValuesList();
                List<Seat> passedSeats = new ArrayList<Seat>();
                for (int i = 0; i <selectedOrderNums.size(); i++) {
                	int orderNum = Integer.parseInt((String) selectedOrderNums.get(i));
                	Order O = RestoController.getCurrentOrder(orderNum);
                	for (Table t: O.getTables()) {
                		for(Seat s: t.getSeats()) {
                    		if(!passedSeats.contains(s)) {
                    			passedSeats.add(s);
                    		}
                    	} 
                	}
                }
                for (int i = 0; i < selectedTablesNums.size(); i++) {
                	int tableNum = Integer.parseInt((String) selectedTablesNums.get(i));
                	Table t = RestoController.getTableByNum(tableNum);
                	List<Seat> seatsAtTable = t.getSeats();
                	for(Seat s: seatsAtTable) {
                		if(!passedSeats.contains(s)) {
                			passedSeats.add(s);
                		}
                	}
                }           
                                
                for(int i = 0; i < selectedSeatNums.size(); i++) {
                	if(!passedSeats.contains(RestoController.hmap.get(selectedSeatNums.get(i)))) {
                		passedSeats.add(RestoController.hmap.get(selectedSeatNums.get(i)));
                	}
                }
                RestoController.issueBill(passedSeats);

                tablePanel.revalidate();
                tablePanel.repaint();

                JOptionPane.showMessageDialog(null, "Bill started successfully.");
            }
            catch (Exception error)
            {
            	JOptionPane.showMessageDialog(
                null,
                error.getMessage(),
                "Could not start bill",
                JOptionPane.ERROR_MESSAGE);

            }
        }
        else {
            JOptionPane.showMessageDialog(null, "No bills were issued.");
        }
    }    

    private void cancelBillAction(ActionEvent event){
    	JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));
		// create array of bills
    	List<Bill> bills = RestoController.getCurrentBills();
		int currentLength = bills.size();
		String currentBillNums[] = new String[currentLength];
		for (int i = 0; i < currentLength; i++) {
			String billName = "b" + i;
			Bill bill = bills.get(i);
			List<Seat> seatsToShow = bill.getIssuedForSeats();
			for(Seat s: seatsToShow) {
				billName = billName + " " + RestoController.getKeyForSeat(s);
			}
			currentBillNums[i] = billName;
		}
		
		panel.add(new JLabel("Select bill to cancel"));
		JComboBox<String> currentBillList = new JComboBox<String>(currentBillNums);
		panel.add(currentBillList);
		
		int result = JOptionPane.showConfirmDialog(null, panel, "Cancel Bill", JOptionPane.OK_CANCEL_OPTION,
		JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			try {
				// RestoController.removeTable((Table)currentTableList.getSelectedItem());
				int billNum = currentBillList.getSelectedIndex();
				bills.get(billNum).delete();
				
				tablePanel.revalidate();
				tablePanel.repaint();
				
				JOptionPane.showMessageDialog(null, "Bill canceled successfully.");
			} catch (Exception error) {
				JOptionPane.showMessageDialog(null, error.getMessage(), "Could not cancel Bill",
				JOptionPane.ERROR_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(null, "Unsuccessful cancellation.");
		}
    }
    
    private void viewBillAction(ActionEvent event){
    	JFrame f = new JFrame("View Bills");
    	JDialog d = new JDialog(f, "View Bills");
		JPanel panel = new JPanel();
		
		GridBagLayout grid = new GridBagLayout();  
		GridBagConstraints gbc = new GridBagConstraints();  
		panel.setLayout(grid);  
		
		//create array of current table numbers
		List<Bill> bills = RestoController.getCurrentBills();
		int currentLength = bills.size();
		String currentBillNums[] = new String[currentLength];
		for (int i = 0; i < currentLength; i++) {
			String billName = "b" + i;
			Bill bill = bills.get(i);
			List<Seat> seatsToShow = bill.getIssuedForSeats();
			int count = 0;
			for(Seat s: seatsToShow) {
				count++;
				billName = billName + " t" + s.getTable().getNumber() + "s" + count;
			}
			currentBillNums[i] = billName;
		}
		
		JTextArea display  = new JTextArea(20, 60);
		display.setEditable (false);
		JScrollPane scroll = new JScrollPane(display);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		JComboBox<String> allBillList = new JComboBox<String>(currentBillNums);
		allBillList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					DecimalFormat df = new DecimalFormat("#.##");
					int billNum = allBillList.getSelectedIndex();
					Bill selectedBill = RestoController.getCurrentBills().get(billNum);
					String displayItems = "Bill " + billNum + "\n";
					List<Seat> seats = selectedBill.getIssuedForSeats();
					double totalBillPrice = 0;
					for (Seat s: seats) {
						displayItems = displayItems + "\nSeat " + RestoController.getKeyForSeat(s);
						List<OrderItem> items = s.getOrderItems();
						for(OrderItem item: items) {
							int qty = item.getQuantity();
							int splitBetween = item.getSeats().size();
							String name = item.getPricedMenuItem().getMenuItem().getName();
							double unitPrice = item.getPricedMenuItem().getPrice();
							String totalPrice = df.format(qty * unitPrice / splitBetween);
							totalBillPrice = totalBillPrice + (qty * unitPrice / splitBetween);
							displayItems = displayItems + "\n" + qty + "/" + splitBetween + " " + name + "---" + totalPrice; 
						}
					}
					displayItems = displayItems + "\n\nBILL TOTAL = $" + df.format(totalBillPrice);
					display.setText(displayItems);
				}catch (NumberFormatException e) {
				}catch (Exception error)
				{
					JOptionPane.showMessageDialog(
					null,
					error.getMessage(),
					"Could not retrieve table's order",
					JOptionPane.ERROR_MESSAGE);
				}
			}
		});       
		
		gbc.ipady = 0;  
		gbc.gridx = 0;  
		gbc.gridy = 0;  
		gbc.gridwidth = 2;
		gbc.insets = new Insets(10,0,0,0);
		panel.add(new JLabel("Select bill:"), gbc);  
		
		
		gbc.gridx = 0;  
		gbc.gridy = 1;  
		gbc.gridwidth = 2;
		panel.add(allBillList, gbc);  
		
		gbc.gridx = 0;  
		gbc.gridy = 3;  
		gbc.gridwidth = 2;  
		panel.add(scroll, gbc);  
		
		d.add(panel);
		d.setSize(500,500);
		// f.setLayout(new FlowLayout());
		d.pack();
		d.setLocationRelativeTo(null);
		d.setVisible(true);
		
		panel.validate();
		panel.repaint();
	}
	
	private void displayMenuAction(ActionEvent event) {
		new MenuFrame();
	}
	
	private void addTableAction(ActionEvent event) {
		JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));
		
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
		
		int result = JOptionPane.showConfirmDialog(null, panel, "Add Table", JOptionPane.OK_CANCEL_OPTION,
		JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			try {
				int numSeats = parseInt(numSeatsField.getText());
				int tableNum = parseInt(tableNumField.getText());
				int x = parseInt(xField.getText());
				int y = parseInt(yField.getText());
				int width = parseInt(widthField.getText());
				int length = parseInt(lengthField.getText());
				
				RestoController.createTableAndSeats(numSeats, tableNum, x, y, width, length);
				
				updateScrollbarMax(x + width, y + length);
				tablePanel.revalidate();
				tablePanel.repaint();
				
				JOptionPane.showMessageDialog(null, "Table added successfully.");
			} catch (Exception error) {
				JOptionPane.showMessageDialog(null, error.getMessage(), "Could not add table",
				JOptionPane.ERROR_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(null, "No Table Added.");
		}
	}
	
	private void updateTableAction(ActionEvent event) {
		JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));
		
		// create array of current table numbers
		int currentLength = RestoController.getCurrentTables().size();
		String currentTableNums[] = new String[currentLength];
		for (int i = 0; i < currentLength; i++) {
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
		
		int result = JOptionPane.showConfirmDialog(null, panel, "Update Table", JOptionPane.OK_CANCEL_OPTION,
		JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			try {
				int tableNum = Integer.parseInt((String) allTablesList.getSelectedItem());
				Table selectedTable = RestoController.getTableByNum(tableNum);
				int newTableNum = parseInt(newTableNumField.getText());
				int amountOfSeats = parseInt(numSeatsField.getText());
				
				RestoController.updateTable(selectedTable, newTableNum, amountOfSeats);
				
				tablePanel.revalidate();
				tablePanel.repaint();
				
				JOptionPane.showMessageDialog(null, "Table updated successfully.");
			} catch (Exception error) {
				error.printStackTrace();
				JOptionPane.showMessageDialog(null, error.getMessage(), "Could not update table",
				JOptionPane.ERROR_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(null, "No tables were changed.");
		}
	}
	
	private void moveTableAction(ActionEvent event) {
		JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));
		
		// create array of current table numbers
		int currentLength = RestoController.getCurrentTables().size();
		String currentTableNums[] = new String[currentLength];
		for (int i = 0; i < currentLength; i++) {
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
		
		int result = JOptionPane.showConfirmDialog(null, panel, "Move Table", JOptionPane.OK_CANCEL_OPTION,
		JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			try {
				int tableNum = Integer.parseInt((String) allTablesList.getSelectedItem());
				Table selectedTable = RestoController.getTableByNum(tableNum);
				int newX = parseInt(newXField.getText());
				int newY = parseInt(newYField.getText());
				
				RestoController.moveTable(selectedTable, newX, newY);
				
				tablePanel.revalidate();
				tablePanel.repaint();
				
				JOptionPane.showMessageDialog(null, "Table moved successfully.");
			} catch (Exception error) {
				JOptionPane.showMessageDialog(null, error.getMessage(), "Could not move table",
				JOptionPane.ERROR_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(null, "No tables were moved.");
		}
		
	}
	
	private void removeTableAction(ActionEvent event) {
		JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));
		// create array of current table numbers
		int currentLength = RestoController.getCurrentTables().size();
		String currentTableNums[] = new String[currentLength];
		for (int i = 0; i < currentLength; i++) {
			currentTableNums[i] = "" + RestoController.getCurrentTable(i).getNumber();
		}
		
		panel.add(new JLabel("Select table to remove"));
		JComboBox<String> currentTableList = new JComboBox<String>(currentTableNums);
		panel.add(currentTableList);
		
		int result = JOptionPane.showConfirmDialog(null, panel, "Remove Table", JOptionPane.OK_CANCEL_OPTION,
		JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			try {
				// RestoController.removeTable((Table)currentTableList.getSelectedItem());
				int tableNum = Integer.parseInt((String) currentTableList.getSelectedItem());
				Table selectedTable = RestoController.getCurrentTableByNum(tableNum);
				RestoController.removeTable(selectedTable);
				
				tablePanel.revalidate();
				tablePanel.repaint();
				
				JOptionPane.showMessageDialog(null, "Table removed successfully.");
			} catch (Exception error) {
				JOptionPane.showMessageDialog(null, error.getMessage(), "Could not remove Table",
				JOptionPane.ERROR_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(null, "Unsuccessful removal.");
		}
	}
	
	private void orderMenuItemAction(ActionEvent event) {
		JFrame frame = new JFrame("Order Menu Item");
		JPanel panel = new JPanel();
		
		int numoftable = RestoController.getCurrentTables().size();
		String currentTableNums[] = new String[numoftable];
		for (int i = 0; i < numoftable; i++) {
			currentTableNums[i] = "" + RestoController.getCurrentTable(i).getNumber();
		}
		
		String currentItemCategories[] = { "Choose a category...", "Appetizer", "Main", "Dessert", "AlcoholicBeverage",
		"NonAlcoholicBeverage" };
		
		List<MenuItem> appetizerMenuItems = new ArrayList<>();
		List<MenuItem> mainMenuItems = new ArrayList<>();
		List<MenuItem> dessertMenuItems = new ArrayList<>();
		List<MenuItem> alcoholicBevMenuItems = new ArrayList<>();
		List<MenuItem> nonAlcoholicBevMenuItems = new ArrayList<>();
		
		List<String> appetizers = new ArrayList<>();
		List<String> mains = new ArrayList<>();
		List<String> desserts = new ArrayList<>();
		List<String> alcoholicBevs = new ArrayList<>();
		List<String> nonAlcoholicBevs = new ArrayList<>();
		
		try {
			appetizerMenuItems = RestoController.getMenuItems(MenuItem.ItemCategory.Appetizer);
			mainMenuItems = RestoController.getMenuItems(MenuItem.ItemCategory.Main);
			dessertMenuItems = RestoController.getMenuItems(MenuItem.ItemCategory.Dessert);
			alcoholicBevMenuItems = RestoController.getMenuItems(MenuItem.ItemCategory.AlcoholicBeverage);
			nonAlcoholicBevMenuItems = RestoController.getMenuItems(MenuItem.ItemCategory.NonAlcoholicBeverage);
		} catch (Exception error) {
			JOptionPane.showMessageDialog(null, error.getMessage(), "Cannot get list of MenuItems",
			JOptionPane.ERROR_MESSAGE);
			error.printStackTrace();
		}
		
		for (MenuItem m : appetizerMenuItems) {
			appetizers.add(m.getName());
		}
		
		for (MenuItem m : mainMenuItems) {
			mains.add(m.getName());
			
		}
		for (MenuItem m : dessertMenuItems) {
			desserts.add(m.getName());
			
		}
		for (MenuItem m : alcoholicBevMenuItems) {
			alcoholicBevs.add(m.getName());
			
		}
		for (MenuItem m : nonAlcoholicBevMenuItems) {
			nonAlcoholicBevs.add(m.getName());
		}
		
		JLabel menuItemCatlabel = new JLabel("Item Category");
		JComboBox<String> menuItemCategory = new JComboBox<String>(currentItemCategories);
		JLabel menuItemlabel = new JLabel("Menu Item");
		JComboBox<String> menuItem = new JComboBox<String>();
		JLabel quantitylabel = new JLabel("Quantity");
		JTextField quantity = new JTextField();
		JLabel tablelabel = new JLabel("Table");

		//ILANA
		Set<String> keys = hmap.keySet();
		String[] keysArray = new String[hmap.keySet().size()];
		int count = 0;
		for(String s: keys) {
			keysArray[count] = s;
			count++;
		}
		DefaultListModel<String> listTablesAndSeats = new DefaultListModel<>();
		for (int i = 0; i < keysArray.length; i++) { // fill list for UI with wanted list (element per element)
			listTablesAndSeats.addElement(keysArray[i]);
		}
		JList<String> allTablesAndSeatsBox = new JList<>(listTablesAndSeats); // now list has table nums of current tables
		allTablesAndSeatsBox.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
		// SCROLLBAR:
		allTablesAndSeatsBox.setVisibleRowCount(4);
		JScrollPane scrollPane = new JScrollPane(allTablesAndSeatsBox);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		JButton add = new JButton("Add to Order");
		
		panel.setPreferredSize(new Dimension(260, 400));
		//panel.setPreferredSize(new Dimension(260, 460));
		panel.setLayout(null);
		
		
		panel.add(menuItemCatlabel);
		panel.add(menuItemCategory);
		panel.add(menuItem);
		panel.add(menuItemlabel);
		panel.add(quantity);
		panel.add(quantitylabel);
		//panel.add(tables);
		panel.add(scrollPane);
		panel.add(tablelabel);
		panel.add(add);
		
		menuItemCatlabel.setBounds(30, 20, 150, 25);
		menuItemCategory.setBounds(30, 50, 200, 30);
		menuItemlabel.setBounds(30, 90, 150, 25);
		menuItem.setBounds(30, 120, 200, 30);
		quantitylabel.setBounds(30, 160, 100, 25);
		quantity.setBounds(30, 190, 200, 30);
		tablelabel.setBounds(30, 230, 100, 25);
		//tables.setBounds(30, 260, 200, 30);
		scrollPane.setBounds(30, 260, 200, 60);
		add.setBounds(30, 340, 200, 30);
		
		frame.getContentPane().add(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		menuItemCategory.addActionListener(e -> {
			
			JComboBox<String> source = (JComboBox<String>) e.getSource();
			String selectedValue = source.getSelectedItem().toString();
			
			switch (selectedValue) {
				case "Choose a category...":
				menuItem.removeAllItems();
				break;
				case "Appetizer":
				menuItem.removeAllItems();
				for (String name : appetizers) {
					menuItem.addItem(name);
				}
				break;
				case "Main":
				menuItem.removeAllItems();
				for (String name : mains) {
					menuItem.addItem(name);
				}
				break;
				case "Dessert":
				menuItem.removeAllItems();
				for (String name : desserts) {
					menuItem.addItem(name);
				}
				break;
				case "AlcoholicBeverage":
				menuItem.removeAllItems();
				for (String name : alcoholicBevs) {
					menuItem.addItem(name);
				}
				break;
				case "NonAlcoholicBeverage":
				menuItem.removeAllItems();
				for (String name : nonAlcoholicBevs) {
					menuItem.addItem(name);
				}
				break;
			}
			
		});
		
		
		add.addActionListener(e -> {
			try {
				MenuItem menuitem = RestoController.getMenuItem((String) menuItem.getSelectedItem());
				int qty = Integer.parseInt(quantity.getText());
				/*List<Seat> seats = RestoController.getSeats(
				RestoController.getCurrentTableByNum(Integer.parseInt((String) tables.getSelectedItem())));*/
				
				List<String> selectedStrings = allTablesAndSeatsBox.getSelectedValuesList();
				List<Seat> selectedSeats = new ArrayList<Seat>();
				for (int i = 0; i < selectedStrings.size(); i++) {
					Seat seat = hmap.get(selectedStrings.get(i)); //get seat associated to each hashmap string selected
					selectedSeats.add(seat);
				}
				RestoController.orderMenuItem(menuitem, qty, selectedSeats);
				tablePanel.revalidate();
				tablePanel.repaint();
				JOptionPane.showMessageDialog(null, "Item ordered sucessfully!", "Item Add Sucess",
				JOptionPane.INFORMATION_MESSAGE);
			} catch (Exception error) {
				JOptionPane.showMessageDialog(null, error.getMessage(), "Could not add item to order",
				JOptionPane.ERROR_MESSAGE);
				error.printStackTrace();
			}
			
		});
		
	}
	
	private void businessStatisticsAction(ActionEvent event) {
		JPanel panel = new JPanel(new GridLayout(7, 4, 5, 5));

		panel.add(new JLabel("Start Date:"));
		
		JXDatePicker startDatePicker = new JXDatePicker();
		startDatePicker.setDate(Calendar.getInstance().getTime());
		startDatePicker.setFormats(new SimpleDateFormat("dd.MM.yyyy"));
		panel.add(startDatePicker);
		
		panel.add(new JLabel("Start Time:"));
		TimePickerSettings timeSettings = new TimePickerSettings();
		timeSettings.setColor(TimePickerSettings.TimeArea.TimePickerTextValidTime, Color.black);
		timeSettings.generatePotentialMenuTimes(TimePickerSettings.TimeIncrement.OneHour, LocalTime.of(9, 0),
		LocalTime.of(23, 0));
		timeSettings.initialTime = LocalTime.now();
		TimePicker startTimePicker = new TimePicker(timeSettings);
		add(startTimePicker);
		panel.add(startTimePicker);
		
		panel.add(new JLabel("End Date:"));
		
		JXDatePicker endDatePicker = new JXDatePicker();
		endDatePicker.setDate(Calendar.getInstance().getTime());
		endDatePicker.setFormats(new SimpleDateFormat("dd.MM.yyyy"));
		panel.add(endDatePicker);
		
		panel.add(new JLabel("End Time:"));
		//TimePickerSettings timeSettings2 = new TimePickerSettings();
		TimePicker endTimePicker = new TimePicker(timeSettings);
		add(endTimePicker);
		panel.add(endTimePicker);
		
		String[] options = new String[3];
		options[0] = new String("Top Tables");
		options[1] = new String("Top Items");
		options[2] = new String("Exit");
		int result = JOptionPane.showOptionDialog(null, panel, "Business Statistics", 
				0, JOptionPane.DEFAULT_OPTION, null, options, null);
		if (result == 0) {
			try {
				
				Date startDate = new Date(startDatePicker.getDate().getTime());
				Time startTime = Time.valueOf(startTimePicker.getTime());
				Date endDate = new Date(endDatePicker.getDate().getTime());
				Time endTime = Time.valueOf(endTimePicker.getTime());
				
				
				JPanel topTables = new JPanel(new GridLayout(15, 4, 5, 5));
				topTables.add(new JLabel("Top 10 Tables: "));
				List<StatisticsTable> top10Tables = RestoController.getTableStatistics(startDate, startTime, endDate, endTime);
				for (StatisticsTable t : top10Tables) {
					topTables.add(new JLabel("Table " + t.getTable().getNumber() + " used " + t.getTable().getNumUsed() + " times.\n"));
				}
				
				//JFrame f = new JFrame();
				//f.setLayout(new BorderLayout());
				//f.add(topTables);
				//f.pack();
				//f.setVisible(true);
				JOptionPane.showMessageDialog(null, topTables);
				//JOptionPane.showConfirmDialog(null, topTables, "Top Tables", JOptionPane.OK_OPTION,
				//		JOptionPane.PLAIN_MESSAGE);
			} catch (Exception error) {
				JOptionPane.showMessageDialog(null, error.getMessage(), "Top 10 Tables failed to generate.",
				JOptionPane.ERROR_MESSAGE);
			}
		} 
		else if (result == 1) {
			try {
				
				Date startDate = new Date(startDatePicker.getDate().getTime());
				Time startTime = Time.valueOf(startTimePicker.getTime());
				Date endDate = new Date(endDatePicker.getDate().getTime());
				Time endTime = Time.valueOf(endTimePicker.getTime());
				
				
				JPanel topItems = new JPanel(new GridLayout(15, 4, 5, 5));
				topItems.add(new JLabel("Top 10 Menu Items: "));
				List<StatisticsItem> top10Items = RestoController.getItemStatistics(startDate, startTime, endDate, endTime);
				for (StatisticsItem sI : top10Items) {
					topItems.add(new JLabel("" + sI.getItem().getName() + " ordered " + sI.getNumUsed() + " times.\n"));
				}
				
				JOptionPane.showMessageDialog(null, topItems);

			} catch (Exception error) {
				JOptionPane.showMessageDialog(null, error.getMessage(), "Top 10 Menu Items Failed to Generate.",
				JOptionPane.ERROR_MESSAGE);
			}
			//JOptionPane.showMessageDialog(null, "Top 10 Items.");
		}
		else {
			JOptionPane.showMessageDialog(null, "Statistics exited.");
		}
	}
}