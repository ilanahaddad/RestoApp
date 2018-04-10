package ca.mcgill.ecse223.resto.view;

import static java.lang.Integer.parseInt;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;

import com.github.lgooddatepicker.components.TimePicker;
import com.github.lgooddatepicker.components.TimePickerSettings;

import org.jdesktop.swingx.JXDatePicker;

import ca.mcgill.ecse223.resto.controller.RestoController;
import ca.mcgill.ecse223.resto.model.Order;
import ca.mcgill.ecse223.resto.model.Table;
import ca.mcgill.ecse223.resto.model.Seat;;


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
        //TODO before submitting split  into different tabs (system, table, menu)
        JMenuBar menubar = new JMenuBar();
        JMenu actions = new JMenu("Actions");
        JMenu manageBillsMenuItem= new JMenu("Manage Bills");
        JMenuItem exitMenuItem = createMenuItem("Exit", RestoAppActions.EXIT_ACTION);
        JMenuItem addTableMenuItem = createMenuItem("Add Table", this::addTableAction);
        JMenuItem changeTableMenuItem = createMenuItem("Change Table", this::updateTableAction);
        JMenuItem moveTableMenuItem = createMenuItem("Move Table", this::moveTableAction);
        JMenuItem removeTableMenuItem = createMenuItem("Remove Table", this::removeTableAction);
        JMenuItem menuMenuItem = createMenuItem("Display Menu", this::displayMenuAction);
        JMenuItem reservationMenuItem = createMenuItem("Make Reservation", this::reserveTableAction);
        JMenuItem startOrderMenuItem = createMenuItem("Start Order", this::startOrderAction);
        JMenuItem endOrderMenuItem = createMenuItem("End Order", this::endOrderAction);
        JMenuItem newBillMenuItem = createMenuItem("New Bill", this::newBillAction);
        JMenuItem reissueBillMenuItem = createMenuItem("Reissue Bill", this::reissueBillAction);
        JMenuItem resolveBillMenuItem = createMenuItem("Resolve Bill", this::resolveBillAction);
        JMenuItem cancelBillMenuItem = createMenuItem("Cancel Bill", this::cancelBillAction);
        
        actions.add(endOrderMenuItem);
        actions.add(startOrderMenuItem);
        actions.add(reservationMenuItem);
        actions.add(addTableMenuItem);
        actions.add(changeTableMenuItem);
        actions.add(moveTableMenuItem);
        actions.add(removeTableMenuItem);
        actions.add(menuMenuItem);
        manageBillsMenuItem.add(newBillMenuItem);
        manageBillsMenuItem.add(reissueBillMenuItem);
        manageBillsMenuItem.add(resolveBillMenuItem);
        manageBillsMenuItem.add(cancelBillMenuItem);
        actions.add(manageBillsMenuItem);
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
        JButton exitButton = createButton("power.png","Exit App [Alt + Q]", KeyEvent.VK_Q, RestoAppActions.EXIT_ACTION);
        JButton addTableButton = createButton("addTable.png", "Add Table [Alt + A]", KeyEvent.VK_A, this::addTableAction);
        JButton changeTableButton = createButton("updateTable.png", "Change Table [Alt + U]", KeyEvent.VK_U, this::updateTableAction);
        JButton moveTableButton = createButton("moveTable.png", "Move Table [Alt + M]", KeyEvent.VK_M, this::moveTableAction);
        JButton removeTableButton = createButton("removeTable.png", "Remove Table [Alt + X]", KeyEvent.VK_X, this::removeTableAction);
        JButton displayMenuButton = createButton("displayMenu.png", "Display Menu [Alt + D]", KeyEvent.VK_D, this::displayMenuAction);
        JButton reserveTableButton = createButton("reserveTable.png", "Make Reservation [Alt + R]", KeyEvent.VK_R, this::reserveTableAction);
        JButton startOrderButton = createButton("startOrder.png", "Start Order [Alt + O]", KeyEvent.VK_O, this::startOrderAction);
        JButton endOrderButton = createButton("endOrder.png", "Start Order [Alt + E]", KeyEvent.VK_E, this::endOrderAction);
        JButton billManagerButton = createButton("billManager.png", "Start Bill Manager [Alt + B]", KeyEvent.VK_B, this::billManagerAction);

        toolbar.add(exitButton);
        toolbar.add(addTableButton);
        toolbar.add(changeTableButton);
        toolbar.add(moveTableButton);
        toolbar.add(removeTableButton);
        toolbar.add(displayMenuButton);
        toolbar.add(reserveTableButton);
        toolbar.add(startOrderButton);
        toolbar.add(endOrderButton);
        toolbar.add(billManagerButton);
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

    private void reserveTableAction(ActionEvent event) {
        JPanel panel = new JPanel(new GridLayout(7, 4, 5, 5));

        //create array of current table numbers
        int currentLength = RestoController.getCurrentTables().size();
        String currentTableNums[] = new String[currentLength];
        for (int i = 0; i < currentLength; i++){
            currentTableNums[i] = "" + RestoController.getCurrentTable(i).getNumber();
        }

        panel.add(new JLabel("Select tables to reserve:"));
        DefaultListModel<String> listTableNums = new DefaultListModel<>();
        for (int i = 0; i < currentTableNums.length; i++) { //fill list for UI with wanted list (element per element)
            listTableNums.addElement(currentTableNums[i]);
        }
        JList<String> allTablesList  = new JList<>(listTableNums); //now list has table nums of current tables
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
        timeSettings.generatePotentialMenuTimes(TimePickerSettings.TimeIncrement.OneHour, LocalTime.of(9, 0), LocalTime.of(23,0));
        timeSettings.initialTime = LocalTime.now();
        TimePicker timePicker = new TimePicker(timeSettings);
        add(timePicker);
        panel.add(timePicker);

        panel.add(new JLabel("Number of people:"));
        JTextField numPeopleField = new JTextField();
        numPeopleField.setAutoscrolls(true);
        //numPeopleField.setVisibleRowCount(2);
        // numPeopleField.setPreferredSize(new Dimension(1,1));
        //numPeopleField.setBounds(0, 0, 5, 5);
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



        int result = JOptionPane.showConfirmDialog(null, panel, "Add Table",
        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION){
            try{

                Date date = new Date(picker.getDate().getTime());
                Time time = Time.valueOf(timePicker.getTime());
                int	numberInParty=0;
                try { //need a try/catch here because if no number is inputed, an
                    //un-informative error gets thrown here for trying to parse an empty string
                    numberInParty = parseInt((String) numPeopleField.getText());
                }
                catch(Exception error) {

                }
                String contactName = nameField.getText();
                String contactEmailAddress = emailField.getText();
                String contactPhoneNumber = phoneField.getText();
                List<String> selectedNumbers = allTablesList.getSelectedValuesList();
                List<Table> selectedTables = new ArrayList<Table>();
                for(int i=0; i< selectedNumbers.size(); i++) {
                    int tableNum = Integer.parseInt((String) selectedNumbers.get(i));
                    Table selectedTable = RestoController.getTableByNum(tableNum);
                    selectedTables.add(selectedTable);
                }

                RestoController.reserveTable(date, time, numberInParty, contactName, contactEmailAddress, contactPhoneNumber, selectedTables);


                tablePanel.revalidate();
                tablePanel.repaint();

                JOptionPane.showMessageDialog(null, "Reservation made successfully.");
            }
            catch (Exception error){
                JOptionPane.showMessageDialog(
                null,
                error.getMessage(),
                "Reservation was not made.",
                JOptionPane.ERROR_MESSAGE);
            }
        }
        else {
            JOptionPane.showMessageDialog(null, "No reservation made.");
        }

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
    
    private void billManagerAction(ActionEvent event){
    	JPanel panel = new JPanel(new GridLayout(2, 2, 5,5));
        JButton newBill = new JButton("New Bill");
        newBill.addActionListener(this::newBillAction);
        panel.add(newBill);
        JButton reissueBill = new JButton("Reissue Bill");
        reissueBill.addActionListener(this::reissueBillAction);
        panel.add(reissueBill);
        JButton resolveBill = new JButton("Resolve Bill");
        resolveBill.addActionListener(this::resolveBillAction);
        panel.add(resolveBill);
        JButton cancelBill = new JButton("Cancel Bill");
        cancelBill.addActionListener(this::cancelBillAction);
        panel.add(cancelBill);
    	
    	JOptionPane.showMessageDialog(null, panel, "Bill Manager",-1);
        }
    
    private void newBillAction(ActionEvent event){
    	JPanel panel = new JPanel(new GridLayout(2, 2, 5,5));

        int currentTableLength = RestoController.getCurrentTables().size();
        String currentTableNums[] = new String[currentTableLength];
        for (int i = 0; i < currentTableLength; i++){
            currentTableNums[i] = "" + RestoController.getCurrentTable(i).getNumber();
        }

        List<Table> currentTables = RestoController.getCurrentTables();
        List<Seat> seats = new ArrayList<Seat>();
        for(Table t: currentTables) {
        	seats.addAll(t.getSeats());
        }
        

        panel.add(new JLabel("Select tables to add to Bill:"));
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
        for (int i = 0; i < currentOrderLength; i++){
            currentOrderNums[i] = "" + RestoController.getCurrentOrder(i).getNumber();
        }

        panel.add(new JLabel("Select orders to add to Bill:"));
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
    }    

    private void reissueBillAction(ActionEvent event){
    	JPanel panel = new JPanel(new GridLayout(2, 2, 5,5));
    	List<Order> orders = RestoController.getCurrentOrders();
    	int numBills = 0;
    	for (Order o: orders) {
    		numBills = numBills + o.getBills().size();
    	}
        if (numBills > 0)
        {
            displayEndOrderAction(panel, numBills);
        }
        else
        {
            JOptionPane.showMessageDialog(
            null,
            "RestoApp currently has no bills issued",
            "Reissue Bills",
            JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void cancelBillAction(ActionEvent event){
    	JPanel panel = new JPanel(new GridLayout(2, 2, 5,5));
    	List<Order> orders = RestoController.getCurrentOrders();
    	int numBills = 0;
    	for (Order o: orders) {
    		numBills = numBills + o.getBills().size();
    	}
        if (numBills > 0)
        {
            displayEndOrderAction(panel, numBills);
        }
        else
        {
            JOptionPane.showMessageDialog(
            null,
            "RestoApp currently has no bills issued",
            "Cancel Bills",
            JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void resolveBillAction(ActionEvent event){
    	JPanel panel = new JPanel(new GridLayout(2, 2, 5,5));
    	List<Order> orders = RestoController.getCurrentOrders();
    	int numBills = 0;
    	for (Order o: orders) {
    		numBills = numBills + o.getBills().size();
    	}
        if (numBills > 0)
        {
            displayEndOrderAction(panel, numBills);
        }
        else
        {
            JOptionPane.showMessageDialog(
            null,
            "RestoApp currently has no bills issued",
            "Resolve Bills",
            JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void endOrderAction(ActionEvent event){
        JPanel panel = new JPanel(new GridLayout(2, 2, 5,5));

        //create array of current orders
        int numOrders = RestoController.getCurrentOrders().size();
        if (numOrders > 0)
        {
            displayEndOrderAction(panel, numOrders);
        }
        else
        {
            JOptionPane.showMessageDialog(
            null,
            "RestoApp currently has no table with orders",
            "No Orders currently available",
            JOptionPane.INFORMATION_MESSAGE);
        }
    }

	private void displayEndOrderAction(JPanel panel, int numOrders) {
		String currOrderNums[] = new String[numOrders];
        int i = 0;
        for (Order o : RestoController.getCurrentOrders()){
            currOrderNums[i++] = "" + o.getNumber();
        }

        panel.add(new JLabel("Select order to end:"));
        DefaultListModel<String> orderNumList = new DefaultListModel<>();
        for(i=0; i<currOrderNums.length;i++) {
            orderNumList.addElement(currOrderNums[i]);
        }
        JList<String> activeOrders  = new JList<>(orderNumList);
        activeOrders.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        panel.add(activeOrders);

        int result = JOptionPane.showConfirmDialog(null, panel, "End Order",
        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION){
            try{
                int selectedNum = Integer.parseInt(activeOrders.getSelectedValuesList().get(0));
                Order selectedOrder = RestoController.getCurrentOrder(selectedNum);

                RestoController.endOrder(selectedOrder);

                tablePanel.revalidate();
                tablePanel.repaint();

                JOptionPane.showMessageDialog(null, "Order ended successfully.");
            }
            catch (Exception error)
            {
                JOptionPane.showMessageDialog(
                null,
                error.getMessage(),
                "Could not end order",
                JOptionPane.ERROR_MESSAGE);
                error.printStackTrace();
            }
        }
        else {
            JOptionPane.showMessageDialog(null, "No order was ended.");
        }
	}

    private void displayMenuAction(ActionEvent event){
        new MenuFrame();
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
