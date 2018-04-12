package ca.mcgill.ecse223.resto.controller;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import ca.mcgill.ecse223.resto.application.RestoAppApplication;
import ca.mcgill.ecse223.resto.model.Menu;
import ca.mcgill.ecse223.resto.model.MenuItem;
import ca.mcgill.ecse223.resto.model.Order;
import ca.mcgill.ecse223.resto.model.OrderItem;
import ca.mcgill.ecse223.resto.model.PricedMenuItem;
import ca.mcgill.ecse223.resto.model.Reservation;
import ca.mcgill.ecse223.resto.model.RestoApp;
import ca.mcgill.ecse223.resto.model.Seat;
import ca.mcgill.ecse223.resto.model.Table;
import ca.mcgill.ecse223.resto.model.Table.Status;
import ca.mcgill.ecse223.resto.model.Bill;

import javax.swing.*;

public class RestoController {
	public static HashMap<String, Seat> hmap ;
	public static HashMap<String, MenuItem> hmap_orderItemsOfSeat;
	public static HashMap<String, Seat> generateHashMap(){
		hmap = new HashMap<String, Seat>();
		RestoApp restoApp = RestoAppApplication.getRestoApp();
		for(Table t: restoApp.getCurrentTables() ) {
			int i = 1;
			for(Seat s: t.getCurrentSeats()) {
				String seatIdentifier = "T"+ t.getNumber()+ "S"+ i; 
				i++;
				hmap.put(seatIdentifier, s);
			}
		}
		/*for(String s: hmap.keySet()) { //FOR DEBUGGING : prints all elements of hash map
			System.out.println(s);
		}*/
		return hmap;
	}

    public static List<MenuItem.ItemCategory> getItemCategories() {
        return Arrays.asList(MenuItem.ItemCategory.values());
    }

    public static List<MenuItem> getMenuItems(MenuItem.ItemCategory itemCategory) throws InvalidInputException {
        List<MenuItem> items = new ArrayList<>();
        RestoApp restoApp = RestoAppApplication.getRestoApp();
        Menu menu = restoApp.getMenu();

        List<MenuItem> menuItems = menu.getMenuItems();
        for (MenuItem mi : menuItems) {
            boolean current = mi.hasCurrentPricedMenuItem();
            MenuItem.ItemCategory category = mi.getItemCategory();

            if (current && category.equals(itemCategory)) {
                items.add(mi);
            }

        }

        return items;

    }

    public static void addMenuItem(String name, MenuItem.ItemCategory category, double price) throws InvalidInputException {
        if (name == null || name.equals("") || category == null || price < 0) {
            throw new InvalidInputException("Error");
        }
        RestoApp r = RestoAppApplication.getRestoApp();
        Menu menu = r.getMenu();

        MenuItem menuItem = new MenuItem(name, menu);

        menuItem.setItemCategory(category);
        PricedMenuItem pmi = menuItem.addPricedMenuItem(price, r);
        menuItem.setCurrentPricedMenuItem(pmi);
        RestoAppApplication.save();

    }

    //TAKE CARE OF SWITCH CASES
    public static void updateMenuItem(MenuItem menuItem, String name, MenuItem.ItemCategory itemCategory, double price) throws InvalidInputException {

        if ((menuItem == null) || (name == null) || (name.equals("")) || (itemCategory == null) || (price < 0)) {
            throw new InvalidInputException("Error");
        }
        boolean current = menuItem.hasCurrentPricedMenuItem();
        if (!current) {
            throw new InvalidInputException("item does not have priced menu item");
        }
        boolean duplicate = menuItem.setName(name);
        if (!duplicate) {
            throw new InvalidInputException("duplicate item");
        }

        menuItem.setItemCategory(itemCategory);

        if (price != menuItem.getCurrentPricedMenuItem().getPrice()) {
            RestoApp r = RestoAppApplication.getRestoApp();
            PricedMenuItem pmi = menuItem.addPricedMenuItem(price, r);
            menuItem.setCurrentPricedMenuItem(pmi);
        }

        RestoAppApplication.save();
    }

    public static void removeMenuItem(MenuItem menuItem) throws InvalidInputException {
        if (menuItem == null) {
            throw new InvalidInputException("Menu item does not exist");
        }
        boolean current = menuItem.hasCurrentPricedMenuItem();
        if (!current) {
            throw new InvalidInputException("priced menu item does not exist");
        }
        menuItem.setCurrentPricedMenuItem(null);
        RestoAppApplication.save();
    }

    public static void createTableAndSeats(int numSeats, int tableNum, int x, int y, int width, int length)
            throws InvalidInputException {
        String error = "";
        RestoApp restoApp = RestoAppApplication.getRestoApp();

        if (overlapsOtherTables(x, y, width, length, restoApp.getCurrentTables())) {
            error += "Input table overlaps with another table. \n";
        }
        if (x < 0) {
            error += "X must be positive. \n";
        }
        if (y < 0) {
            error += "Y must be positive. \n";
        }
        if (tableNum < 0) {
            error += "Table number must be positive. \n";
        }
        if (numSeats < 0) {
            error += "Number of seats must be positive. \n";
        }
        if (width <= 0) {
            error += "Width must be positive. \n";
        }
        if (length <= 0) {
            error += "Length must be positive. \n";
        }
        if (error.length() > 0) {
            throw new InvalidInputException(error.trim());
        }

        Table tableToAdd;
        // only bring an existent table to current IFF it has exactly the same
        // attributes
        // if not it is simply not the same table and must have a new table number
        if (exactTableNotInCurrent(tableNum, numSeats, width, length)
                && exactTableInApp(tableNum, numSeats, width, length)) {
            tableToAdd = getTableByNum(tableNum);
            tableToAdd.setX(x);
            tableToAdd.setY(y);
        }
        // table is new to the application
        else {
            try {
                tableToAdd = new Table(tableNum, x, y, width, length, restoApp); // throws runtime exception if number
                // is duplicate
            } catch (RuntimeException e) {
                error = e.getMessage();
                if (error.equals("Cannot create due to duplicate number")) {
                    error = "A table with this number already exists. Please use a different number.\n";
                }
                throw new InvalidInputException(error);
            }
            restoApp.addCurrentTable(tableToAdd);
            for (int i = 0; i < numSeats; i++) {
                Seat newSeat = tableToAdd.addSeat();
                tableToAdd.addCurrentSeat(newSeat);
                String seatIdentifier = "T"+ tableToAdd.getNumber()+ "S"+ (i+1); 
                hmap.put(seatIdentifier, newSeat);
            }
        }

        // restoApp.addCurrentTable(tableToAdd);
        RestoAppApplication.save();
    }

    /**
     * Removes a table from the current tables
     *
     * @param table table to be removed
     * @throws InvalidInputException If the specified table does not exist
     */
    public static void removeTable(Table table) throws InvalidInputException {
        String error = "";
        if (table == null) {
            throw new InvalidInputException("Input table does not exist");
        }

        boolean reserved = table.hasReservations();
        if (reserved) {
            error += "Table is reserved and cannot be removed.\n";
        }
        RestoApp r = RestoAppApplication.getRestoApp();
        List<Order> currentOrders = r.getCurrentOrders();

        List<Table> tables;
        boolean inUse;
        for (Order order : currentOrders) {
            tables = order.getTables();
            inUse = tables.contains(table);
            if (inUse) {
                error += "Cannot remove: Selected table is currently in use.\n";
            }
        }
        if (error.length() > 0) {
            throw new InvalidInputException(error.trim());
        }

        try {
            r.removeCurrentTable(table);
        } catch (RuntimeException e) {
            throw new InvalidInputException(e.getMessage());
        }
        Set<String> keys = hmap.keySet(); 
        List<String> seatsForTable = new ArrayList<String>(); 
        for(String s: keys) {
        		String tablePartOfString = s.substring(0, 2);
        		if(tablePartOfString.contains("T"+table.getNumber())) {
        			seatsForTable.add(s);
        		}
        }
        for(String s: seatsForTable) {
        		hmap.remove(s);
        }
        RestoAppApplication.save();
    }

    /**
     * Updates table number and number of seats at the table
     *
     * @param table         table to update
     * @param newNumber     new table number to update to
     * @param numberOfSeats number of seats for updated table to have
     * @throws InvalidInputException if table doesn't exist, if newNumber and numberOfSeats aren't
     *                               positive and if table is reserved
     */
    public static void updateTable(Table table, int newNumber, int numberOfSeats) throws InvalidInputException {
        RestoApp restoApp = RestoAppApplication.getRestoApp();
        if (table == null) {
            throw new InvalidInputException("Input Table does not exist.\n");
        }
        if (newNumber < 0) {
            throw new InvalidInputException("New table number must be positive.\n");
        }
        if (numberOfSeats < 0) {
            throw new InvalidInputException("Number of seats must be positive.\n");
        }
        if (table.hasReservations()) {
            throw new InvalidInputException("Can't update a table that is reserved.\n");
        }
        List<Order> currentOrders = restoApp.getCurrentOrders();
        boolean inUse = false;
        for (Order o : currentOrders) { // check if table to update has orders (i.e. inUse)
            List<Table> tablesWithOrders = o.getTables();
            inUse = tablesWithOrders.contains(table);
        }
        if (inUse) {
            throw new InvalidInputException("Can't update a table that is currently in use.\n");
        }
        int oldNum = table.getNumber();
        if (!table.setNumber(newNumber)) {
            throw new InvalidInputException(
                    "A table with this number already exists. Please use a different number.\n");
        }
        /*
         * if(error.length() > 0) { throw new InvalidInputException(error.trim()); }
         */
        Set<String> keys = hmap.keySet();
        List<String> seatsForTable = new ArrayList<String>();
        for(String s: keys) {
            String tablePartOfString = s.substring(0, 2);
            if(tablePartOfString.contains("T"+oldNum)) {
                seatsForTable.add(s);
            }
        }
        for(String s: seatsForTable) { //remove from hash map all items that have old table number
            hmap.remove(s);
        }
		int j = 1;
		for(Seat s: table.getCurrentSeats()) {
			String seatIdentifier = "T"+ table.getNumber()+ "S"+ j;
			hmap.put(seatIdentifier, s);
            j++;
		}
        
        int n = table.numberOfCurrentSeats();
        // Add seats if new numberOfSeats > numberOfCurrentSeats:
        for (int i = 1; i <= numberOfSeats - n; i++) {
            Seat seat = table.addSeat();
            table.addCurrentSeat(seat);
            String hmapIdentifier = "T"+newNumber+"S"+(n+i);
            hmap.put(hmapIdentifier, seat);
        }
        // Remove seats if new numberOfSeats < numberOfCurrentSeats:
        for (int i = 1; i <= n - numberOfSeats; i++) {
            Seat seat = table.getCurrentSeat(table.numberOfCurrentSeats() - 1);
            table.removeCurrentSeat(seat);
            hmap.remove("T"+newNumber+"S"+(n+1-i));
        }

		
		//
        RestoAppApplication.save();
    }

    /**
     * 
     *
     * @param table
     * @param newX
     * @param newY
     * @throws InvalidInputException
     */
    public static void moveTable(Table table, int newX, int newY) throws InvalidInputException {
        String error = "";
        if (table == null) {
            error += "Input table does not exist.\n";
        }
        if (newX < 0 || newY < 0) {
            error += "X and Y must be positive.\n";
        }
        int width = table.getWidth();
        int length = table.getLength();
        RestoApp r = RestoAppApplication.getRestoApp();
        List<Table> currentTables = r.getCurrentTables();
        List<Table> currentStationaryTables = new ArrayList<>();
        for (Table i : currentTables) {
            if (table.equals(i) == false) {
                currentStationaryTables.add(i);
            }
        }
        if (overlapsOtherTables(newX, newY, width, length, currentStationaryTables)) {
            error += "Input table overlaps with another table. \n";
        }
        if (error.length() > 0) {
            throw new InvalidInputException(error.trim());
        }
        table.setX(newX);
        table.setY(newY);
        RestoAppApplication.save();

    }

    /**
     * Make a reservation of one or more tables under a single name
     *
     * @param date                date of the reservation
     * @param time                time of the reservation
     * @param numberInParty       number of people that are in the same reservation group
     * @param contactName         name of the reservation client
     * @param contactEmailAddress e-mail
     * @param contactPhoneNumber  phone number
     * @param tables              list of tables of type Table
     * @throws InvalidInputException
     */
    public static void reserveTable(Date date, Time time, int numberInParty, String contactName,
                                    String contactEmailAddress, String contactPhoneNumber, List<Table> tables) throws InvalidInputException {
        String error = "";
        if (tables == null) {
            error += "Please select one or more tables.\n";
        }
        if (date == null) {
            error += "Date cannot be null.\n";
        }
        if (time == null) {
            error += "Time cannot be null.\n";
        }
        if (contactName.equals("") || contactName == null) {
            error += "Please enter a contact name.\n";
        }
        if (contactEmailAddress == null || contactEmailAddress.equals("")) {
            error += "Please enter a contact e-mail address.\n";
        }
        if (contactPhoneNumber.equals("") || contactPhoneNumber == null) {
            error += "Please enter a contact phone number.\n";
        }
        Date currentDate = new Date(Calendar.getInstance().getTime().getTime());
        if (date.before(currentDate) || (date.equals(currentDate) && time.before(currentDate))) {
            error += "Cannot make reservation in the past.\n";
        }
        if (numberInParty < 1) {
            error += "Members in a party cannot be negative and must be at least 1.\n";
        }
        if (error.length() > 0) {
            throw new InvalidInputException(error.trim());
        }
        RestoApp r = RestoAppApplication.getRestoApp();
        List<Table> currentTables = r.getCurrentTables();
        int seatCapacity = 0;
        boolean current;
        List<Reservation> reservations;
        boolean overlaps;

        for (Table table : tables) {
            current = currentTables.contains(table);
            if (current == false)
                throw new InvalidInputException(
                        "The table you are trying to reserve is not currently in the restaurant.\n");
            seatCapacity += table.numberOfCurrentSeats();
            reservations = table.getReservations();

            for (Reservation reservation : reservations) {
                overlaps = reservationDoesOverlap(reservation, date, time);
                if (overlaps) {
                    throw new InvalidInputException("Reservation overlaps.\n");
                }
            }
        }
        if (seatCapacity < numberInParty)
            throw new InvalidInputException("Too many people in the party for the selected tables.\n");

        Table[] tableArray = new Table[tables.size()];
        for (int i = 0; i < tables.size(); i++) {
            tableArray[i] = tables.get(i);
        }
        Reservation res = new Reservation(date, time, numberInParty, contactName, contactEmailAddress,
                contactPhoneNumber, r, tableArray);
        r.addReservation(res); // not in seq diagram? should I remove? (PotassiumK)
        RestoAppApplication.save();
    }

    /**
     * Compare the date and time of two reservations to see if they overlap
     *
     * @param existingReservation the reservation that needs comparing to
     * @param date                date of new reservation to be compared
     * @param time                time of new reservation to be compared
     * @return
     */
    public static boolean reservationDoesOverlap(Reservation existingReservation, Date date, Time time) {
        boolean overlapStatus = false;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(existingReservation.getTime());
        int timeHour = calendar.get(Calendar.HOUR);
        int resHour = calendar2.get(Calendar.HOUR);
        if (existingReservation.getDate().equals(date)) {
            // if(Math.abs(existingReservation.getTime().getHours() - time.getHours()) > 2)
            // {
            if (Math.abs(timeHour - resHour) <= 2) {
                overlapStatus = true;
            }
        }

        return overlapStatus;
    }

    public static Reservation getEarliestRes(List<Reservation> reservations) {
        if (reservations.isEmpty())
            return null;

        Reservation earliest = reservations.get(0);
        for (Reservation r : reservations) {
            if (r.getDate().equals(earliest.getDate()) || r.getDate().before(earliest.getDate())) {
                if (r.getTime().before(earliest.getTime())) {
                    earliest = r;
                }
            }
        }

        return earliest;
    }

    public static Table getTableByNum(int tableNum) throws InvalidInputException {
        RestoApp restoApp = RestoAppApplication.getRestoApp();
        for (Table table : restoApp.getTables()) {
            if (table.getNumber() == tableNum) {
                return table;
            }
        }
        throw new InvalidInputException("Could not retrieve table number " + tableNum);
    }

    /**
     * @param tableNum
     * @return
     * @throws InvalidInputException
     */
    public static Table getCurrentTableByNum(int tableNum) throws InvalidInputException {
        RestoApp restoApp = RestoAppApplication.getRestoApp();
        for (Table table : restoApp.getCurrentTables()) {
            if (table.getNumber() == tableNum) {
                return table;
            }
        }
        throw new InvalidInputException("Could not retrieve table number " + tableNum);
    }

    // to here
    // checks if the table with given attributes is in the set tables in the app
    private static boolean exactTableInApp(int tableNum, int numSeats, int width, int length) {
        RestoApp restoApp = RestoAppApplication.getRestoApp();
        for (Table table : restoApp.getTables()) {
            if (areExactSameTable(table, tableNum, numSeats, width, length)) {
                return true;
            }
        }
        return false;
    }

    // checks if the table with given attributes is not in the set of current tables
    private static boolean exactTableNotInCurrent(int tableNum, int numSeats, int width, int length) {
        RestoApp restoApp = RestoAppApplication.getRestoApp();
        for (Table table : restoApp.getCurrentTables()) {
            if (areExactSameTable(table, tableNum, numSeats, width, length)) {
                return false;
            }
        }
        return true;
    }

    // validates if 2 tables are equal (x,y does not matter)
    private static boolean areExactSameTable(Table table, int tableNum, int numSeats, int width, int length) {
        return table.getNumber() == tableNum && table.getSeats().size() == numSeats && table.getWidth() == width
                && table.getLength() == length;
    }

    private static boolean overlapsOtherTables(int x, int y, int width, int length, List<Table> tables) {
        Shape newTableShape = new Rectangle2D.Float(x, y, width, length);
        for (Table table : tables) {
            Shape tableShape = new Rectangle2D.Float(table.getX(), table.getY(), table.getWidth(), table.getLength());
            if (tableShape.getBounds2D().intersects(newTableShape.getBounds2D())) {
                return true;
            }
        }
        return false;
    }

    public static List<Table> getCurrentTables() {
        RestoApp restoApp = RestoAppApplication.getRestoApp();
        return restoApp.getCurrentTables();
    }

    public static Table getCurrentTable(int tableNum) {
        RestoApp restoApp = RestoAppApplication.getRestoApp();
        return restoApp.getCurrentTable(tableNum);
    }

    public static List<Table> getTables() {
        RestoApp restoApp = RestoAppApplication.getRestoApp();
        return restoApp.getTables();
    }

    public static Table getTable(int tableNum) {
        RestoApp restoApp = RestoAppApplication.getRestoApp();
        return restoApp.getTable(tableNum);
    }

    public static List<Order> getCurrentOrders() {
        RestoApp restoApp = RestoAppApplication.getRestoApp();
        return restoApp.getCurrentOrders();
    }

    public static Order getCurrentOrder(int orderNum) {
        RestoApp restoApp = RestoAppApplication.getRestoApp();
        List<Order> currOrders = restoApp.getCurrentOrders();
        for (Order o : currOrders) {
            if (o.getNumber() == orderNum) {
                return o;
            }
        }
        return null;
    }

    // get largest Y coordinate of all the app's current tables
    public static int getMaxX() {
        RestoApp restoApp = RestoAppApplication.getRestoApp();
        int maxX = 0;
        for (Table table : restoApp.getCurrentTables()) {
            if (maxX < table.getX() + table.getWidth()) {
                maxX = table.getX() + table.getWidth();
            }
        }
        return maxX;
    }

    // get largest Y coordinate of all the app's current tables
    public static int getMaxY() {
        RestoApp restoApp = RestoAppApplication.getRestoApp();
        int maxY = 0;
        for (Table table : restoApp.getCurrentTables()) {
            if (maxY < table.getY() + table.getLength()) {
                maxY = table.getY() + table.getLength();
            }
        }
        return maxY;
    }

    public static void startOrder(List<Table> tables) throws InvalidInputException {
        if (tables == null) {
            throw new InvalidInputException("Error: Tables can't be null.\n");
        }
        RestoApp r = RestoAppApplication.getRestoApp();
        List<Table> currentTables = r.getCurrentTables();
        boolean current = false;
        for (Table t : tables) { // loop through tables given
            current = currentTables.contains(t); // check if each table given is contained in currentTables
            if (!current) {
                throw new InvalidInputException("Error: One of the tables is not current");
            }
        }
        boolean orderCreated = false;
        Order newOrder = null;
        for (Table t : tables) {
            if (orderCreated) {
                t.addToOrder(newOrder);
            } else { // new Order:
                Order lastOrder = null;
                if (t.numberOfOrders() > 0) { // if table has order(s), get last one
                    lastOrder = t.getOrder(t.numberOfOrders() - 1);
                }
                t.startOrder(); // now lastOrder is secondToLast (or null if new order was first one)
                if (t.numberOfOrders() > 0 && !t.getOrder(t.numberOfOrders() - 1).equals(lastOrder)) {
                    // checks that order just created is not equal to previous last order from
                    // tables orders
                    // (checks that t.startOrder successfully added order to table)
                    orderCreated = true;
                    newOrder = t.getOrder(t.numberOfOrders() - 1); // pass last order of table to newOrder object
                }
            }
        }
        if (!orderCreated) {
            // went through loop for all tables and an order was never created
            throw new InvalidInputException("Order not created: failed for all tables.\n");
        }
        r.addCurrentOrder(newOrder); // added newly created order to list of current orders
        RestoAppApplication.save();
    }

    public static List<OrderItem> getOrderItems(Table table) throws InvalidInputException {
        if (table == null) {
            throw new InvalidInputException("Error: Table can't be null.\n");
        }

        RestoApp r = RestoAppApplication.getRestoApp();

        boolean isTableCurrent = false;
        for (Table t : r.getCurrentTables()) {
            if (t.getNumber() == table.getNumber()) {
                isTableCurrent = true;
                break;
            }
        }
        if (!isTableCurrent) {
            throw new InvalidInputException("Error: Table is not part of current tables.\n");
        }

        if (table.getStatus() == Status.Available) {
            throw new InvalidInputException("Error: Table is not currently being used.\n");
        }

        Order lastOrder = null;
        if (table.numberOfOrders() > 0) {
            lastOrder = table.getOrder(table.numberOfOrders() - 1);
        } else {
            throw new InvalidInputException("Error: Table has no current orders.\n");
        }

        List<OrderItem> finalOrderItems = new ArrayList<OrderItem>();

        for (Seat seat : table.getCurrentSeats()) {
            for (OrderItem oi : seat.getOrderItems()) {
                if (lastOrder.equals(oi.getOrder()) && !finalOrderItems.contains(oi)) {
                    finalOrderItems.add(oi);
                }
            }
        }

        return finalOrderItems;
    }

    public static void endOrder(Order orderToEnd) throws InvalidInputException {
        RestoApp r = RestoAppApplication.getRestoApp();

        List<Order> currOrders = r.getCurrentOrders();
        if (!currOrders.contains(orderToEnd)) {
            throw new InvalidInputException("Order to end is not part of current orders");
        }
        
        if (!orderToEnd.hasBills() && orderToEnd.getOrderItems().size() >0) {
            throw new InvalidInputException("The bill hasnt been paid for some tables"); 
        }

        // create new list of tables
        List<Table> tablesInOrder = new ArrayList<Table>(orderToEnd.getTables());

        List<Table> tablesMarked = new ArrayList<Table>();
        for (Table table : tablesInOrder) {
            if (orderBelongsToTable(orderToEnd, table)) {
                tablesMarked.add(table);
            }
        }

        for (Table table : tablesMarked) {
            table.endOrder(orderToEnd);
        }

        if (allTablesAvailableOrDifferentCurrentOrder(orderToEnd, tablesInOrder)) {
            r.removeCurrentOrder(orderToEnd);
        }

        RestoAppApplication.save();
    }

    private static boolean allTablesAvailableOrDifferentCurrentOrder(Order orderToEnd, List<Table> tablesInOrder) {
        for (Table table : tablesInOrder) {
            if (table.getStatus() == Table.Status.Available
                    || !table.getOrder(table.numberOfOrders() - 1).equals(orderToEnd)) {
                return false;
            }
        }
        return true;
    }

    private static boolean orderBelongsToTable(Order orderToEnd, Table table) {
        return table.numberOfOrders() > 0 && table.getOrder(table.numberOfOrders() - 1).equals(orderToEnd);
    }

	/**
	 * Cancel an ordered item for a customer 
	 * @param orderItem orderItem to delete
	 * @throws InvalidInputException if OrderItem is null, and if table doesn't have an order
	 */
    public static void cancelOrderItem(OrderItem orderItem) throws InvalidInputException{
    		if(orderItem==null) {
    			throw new InvalidInputException("Please select an order item.\n");
    		}
    		List<Seat> seats = orderItem.getSeats();
    		Order order = orderItem.getOrder();
    		List<Table> tables = new ArrayList<Table>();
    		for(Seat seat: seats) {
    			Table table = seat.getTable();
    			Order lastOrder = null;
    			if(table.numberOfOrders()>0) { //if the seat's table has orders
    				lastOrder = table.getOrder(table.numberOfOrders()-1);
    			}
    			else { // if table has no order (this would never be thrown because if one of its seats has 
    				//an orderItem then table must have an order)
    				throw new InvalidInputException("The table doesn't have an order");
    			}
    			if(lastOrder.equals(order) && !tables.contains(table)) {
    				//if this is the table's last order, add table to list of tables that we will cancel items for
    				tables.add(table);
    			}
    		}
    		for(Table table: tables) {
    			table.cancelOrderItem(orderItem);
    		}
    		RestoAppApplication.save();
    }
    /**
     * Delete all order items of the table 
     * @param table table to cancel order for
     * @throws InvalidInputException if table is null or if table isn't current
     */
    public static void cancelOrder(Table table) throws InvalidInputException{
    		if(table == null) {
    			throw new InvalidInputException("Please select a table.\n");
    		}
    		RestoApp r = RestoAppApplication.getRestoApp();
    		List<Table> currentTables = r.getCurrentTables();
    		boolean isTableCurrent = currentTables.contains(table);
    		if(!isTableCurrent) {
    			throw new InvalidInputException("Table is not current.\n");
    		}
    		table.cancelOrder();
    		RestoAppApplication.save();
    }
    
    public static List<OrderItem> getAllCurrentOrderItems(){
        	RestoApp r = RestoAppApplication.getRestoApp();
        	List<OrderItem> allOrderItems = new ArrayList<OrderItem>();
        	List<Table> curTables = r.getCurrentTables();
        	for(Table t: curTables) {
        		List<Seat> curSeatsAtTable = t.getCurrentSeats();
        		for(Seat s: curSeatsAtTable) {
        			List<OrderItem> orderItemsForSeat = s.getOrderItems(); 
        			if(s.hasOrderItems()) {
            			for(OrderItem i: orderItemsForSeat) {
            				allOrderItems.add(i);
            			}
        			}

        		}
        	}
        	return allOrderItems;
    }
    
    //USING THIS NOW: ILANA2
  /*  public static List<String> getAllCurrentOrderItemsNamesWithSeatKey(){
	    	RestoApp r = RestoAppApplication.getRestoApp();
	    	List<String> allOrderItemNames = new ArrayList<String>();
	    	List<Table> curTables = r.getCurrentTables();
	    	List<String> allOrderItemNamesWithSeatNum = new ArrayList<String>();
	    	for(Table t: curTables) {
	    		List<Seat> curSeatsAtTable = t.getCurrentSeats();
	    		for(Seat s: curSeatsAtTable) {
	    			List<OrderItem> orderItemsForSeat = s.getOrderItems(); 
	    			if(s.hasOrderItems()) {
	        			for(OrderItem i: orderItemsForSeat) {
	        				allOrderItemNames.add(i.getPricedMenuItem().getMenuItem().getName());
	        				String orderItemName = i.getPricedMenuItem().getMenuItem().getName();
	        				String num = getKeyForSeat(s);
	        				allOrderItemNamesWithSeatNum.add(orderItemName + " " + num);
	        			}
	    			}
	
	    		}
	    	}
	    	return allOrderItemNamesWithSeatNum;
	    	
    }*/
    public static List<String> getAllCurrentOrderItemsNamesWithSeatKey(){
    	RestoApp r = RestoAppApplication.getRestoApp();
    	List<Table> curTables = r.getCurrentTables();
    	List<String> allOrderItemNamesWithSeatNum = new ArrayList<String>();
    	for(Table t: curTables) {
    		List<Seat> curSeatsAtTable = t.getCurrentSeats();
    		for(Seat s: curSeatsAtTable) {
    			List<OrderItem> orderItemsForSeat = s.getOrderItems(); 
    			int count = 1;
    			if(s.hasOrderItems()) {
    				
        			for(OrderItem i: orderItemsForSeat) {
        				String orderItemName = i.getPricedMenuItem().getMenuItem().getName();
        				String num = getKeyForSeat(s);
        				allOrderItemNamesWithSeatNum.add(count+". "+orderItemName + " " + num);
        				count++;
        			}
        			
    			}
    			

    		}
    	}
    	return allOrderItemNamesWithSeatNum;
    	
    }
    /*  public static List<MenuItem> getMenuItemsFromAllCurrentOrderItems(){
    		List<MenuItem> menuItems= new ArrayList<MenuItem>();
    		List<OrderItem> orderItems = getAllCurrentOrderItems();
    		if(orderItems == null) {
    			return null;
    		}
    		for(OrderItem i: orderItems) {
    			MenuItem m = i.getPricedMenuItem().getMenuItem();
				if(menuItems!=null) {//to ensure no null pointer exception
					if(!menuItems.contains(m)) {
						menuItems.add(m);
					}
				}
    		}
    		return menuItems;
    }
    public static HashMap<String, String> getMapOfSeatsWithOrderItemNames(){
    		HashMap<String, String> map = new HashMap<String, String>();
    		RestoApp r = RestoAppApplication.getRestoApp();
        	List<OrderItem> allOrderItems = new ArrayList<OrderItem>();
        	List<Table> curTables = r.getCurrentTables();
        	for(Table t: curTables) {
        		List<Seat> curSeatsAtTable = t.getCurrentSeats();
        		for(Seat s: curSeatsAtTable) {
        			List<OrderItem> orderItemsForSeat = s.getOrderItems(); 
        			if(s.hasOrderItems()) {
            			for(OrderItem i: orderItemsForSeat) { //for every order item of the seat
            				allOrderItems.add(i);
            				
            			}
        			}

        		}
        	}
        	for(String keyString: hmap.keySet()) {
        		if(keyString.)
        	}
        	for(Seat s: hmap.values()) { //for all seats in map
            	for(String keyString: hmap.keySet()) {
            		if(hmap.get(keyString).equals(s)) {
            			
            		}
            	}
        	}
    		
    }*/
    public static String getKeyForSeat(Seat seat) {
	    	for(String keyString: hmap.keySet()) { //for all keys
	    		if(hmap.get(keyString).equals(seat)) {
	    			return keyString;
	    		}
	    	}
	    	return null;
    }
    public static OrderItem getOrderItemFromMenuItemName(String name) {
    		List<OrderItem> allOrderItems = getAllCurrentOrderItems();
    		OrderItem orderItem = null;
    		for(OrderItem i: allOrderItems) {
    			if(i.getPricedMenuItem().getMenuItem().getName().equals(name)) {
    				orderItem= i;
    			}
    		}
    		return orderItem;
    }
    public static OrderItem getOrderItemFromNameWithSeatKey(String orderItemNameWithSeatKey) {
    		int length = orderItemNameWithSeatKey.length();
    		String orderItemName = orderItemNameWithSeatKey.substring(3, length-5);
    		String seatKey = orderItemNameWithSeatKey.substring(length-4, length);
    		Seat seat = hmap.get(seatKey);
    		
		List<OrderItem> currOrderItems = getAllCurrentOrderItems();
		//OrderItem orderItemWanted = null;
		for(OrderItem oi: currOrderItems) {
			String oiName = oi.getPricedMenuItem().getMenuItem().getName();
			List<Seat> oi_seats = oi.getSeats();
			for(Seat s: oi_seats) {
				if(oiName.equals(orderItemName) && s.equals(seat)) {
					return oi;
				}
			}
		}
		return null;
    }
  /*  public static void tempCancelOrder(Table table) {
	 	Order curOrder = table.getOrder(table.numberOfOrders()-1);
		List<OrderItem> orderItemsOfTable = curOrder.getOrderItems();
		for(int i=0;i<orderItemsOfTable.size();i++) {
			orderItemsOfTable.get(i).delete();
		}*/
	/*	for(OrderItem o: orderItemsOfTable) {
			o.delete(); //delete all order items of the table 
		}*/
  //  }

    /**
     * Order a menu item to a seat
     *
     * @param menuItem MenuItem to be added to order
     * @param quantity quantity of menuItem to be ordered
     * @param seats    seats associated with the order
     * @throws InvalidInputException if menuItem or seats is null, seats list is empty, or quantity is
     *                               not positive
     */

    public static void orderMenuItem(MenuItem menuItem, int quantity, List<Seat> seats) throws InvalidInputException {
        if (!(quantity > 0)) {
            throw new InvalidInputException("Please enter a positive quantity.\n");
        }
        if (menuItem == null) {
            throw new InvalidInputException("Please enter a valid MenuItem.\n");
        }
        if (seats == null) {
            throw new InvalidInputException("Please enter valid seats.\n");
        }
        if (seats.isEmpty()) {
            throw new InvalidInputException("No seats in list.\n");
        }

        RestoApp r = RestoAppApplication.getRestoApp();

        boolean current = menuItem.hasCurrentPricedMenuItem();
        if (!current) {
            throw new InvalidInputException("The ordered MenuItem does not exist.\n");
        }

        List<Table> currentTables = r.getCurrentTables();
        Order lastOrder = null;

        for (Seat seat : seats) {
            Table table = seat.getTable();

            current = currentTables.contains(table);
            if (!current) {
                throw new InvalidInputException("The table associated with the seat is not currently active.\n");
            }

            List<Seat> currentSeats = table.getCurrentSeats();

            current = currentSeats.contains(seat);
            if (!current) {
                throw new InvalidInputException("The seat is not currently active.\n");
            }

            if (lastOrder == null) {
                if (table.numberOfOrders() > 0) {
                    lastOrder = table.getOrder(table.numberOfOrders() - 1);
                } else {
                    throw new InvalidInputException("No order associated with table.\n");
                }
            } else {
                Order comparedOrder = null;

                if (table.numberOfOrders() > 0) {
                    comparedOrder = table.getOrder(table.numberOfOrders() - 1);
                } else {
                    throw new InvalidInputException("No order associated with table.\n");
                }

                if (!comparedOrder.equals(lastOrder)) {
                    throw new InvalidInputException("Last order does not match compared last order.\n");
                }
            }
        }

        if (lastOrder == null) {
            throw new InvalidInputException("No Order found.\n");
        }

        PricedMenuItem pmi = menuItem.getCurrentPricedMenuItem();
        boolean itemCreated = false;
        OrderItem newItem = null;

        for (Seat seat : seats) {
            Table table = seat.getTable();

            if (itemCreated) {
                table.addToOrderItem(newItem, seat);
            } else {
                OrderItem lastItem = null;

                if (lastOrder.numberOfOrderItems() > 0) {
                    lastItem = lastOrder.getOrderItem(lastOrder.numberOfOrderItems() - 1);
                }

                table.orderItem(quantity, lastOrder, seat, pmi);

                if ((lastOrder.numberOfOrderItems() > 0)
                        && (!lastOrder.getOrderItem(lastOrder.numberOfOrderItems() - 1).equals(lastItem))) {
                    itemCreated = true;
                    newItem = lastOrder.getOrderItem(lastOrder.numberOfOrderItems() - 1);
                }
            }
        }

        if (!itemCreated) {
            throw new InvalidInputException("OrderItem not sucessfully created.\n");
        }

        RestoAppApplication.save();

    }
    public static List<Bill> getCurrentBills() {
        RestoApp restoApp = RestoAppApplication.getRestoApp();
        return restoApp.getBills();
    }

    public static void issueBill(List<Seat> seats) throws InvalidInputException{
        	RestoApp restoApp = RestoAppApplication.getRestoApp();
        	if(seats == null) {
                throw new InvalidInputException("Error: Seats can't be null.\n");
            }
            RestoApp r = RestoAppApplication.getRestoApp();
            List<Table> currentTables = r.getCurrentTables();
            boolean current =false;
            Table t;
            List<Seat> currentSeats;
            Order lastOrder = null;
            for(Seat s: seats) { //loop through tables given
            	t = s.getTable();
                current = currentTables.contains(t); //check if each table given is contained in currentTables
                if(!current) {
                    throw new InvalidInputException("Error: One of the seats is at a table that is not current");
                }
                currentSeats = t.getCurrentSeats();
                current = currentSeats.contains(s);	
                if(!current) {
                	throw new InvalidInputException("Error: One of the seats is not current");
                }
                if(lastOrder == null) {
                	if (t.numberOfOrders() > 0) {
                		lastOrder = t.getOrder(t.numberOfOrders()-1);
                	}
                	else {
                		throw new InvalidInputException("Error: There are no orders");
                	}
                }
                else {
                	Order comparedOrder = null;
                	if (t.numberOfOrders() > 0) {
                		comparedOrder = t.getOrder(t.numberOfOrders()-1);
                	}
                	else {
                		throw new InvalidInputException("Error: There are no orders");
                	}
                	if(!comparedOrder.equals(lastOrder)) {
                		throw new InvalidInputException("Error: Not the same order?");
                	}
                }  
            }
            if(lastOrder == null) {
            	throw new InvalidInputException("Error: lastOrder is null");
            }
            boolean billCreated = false;
            Bill newBill = null;
            for(Seat s: seats) {
            	t = s.getTable();
            	if(billCreated) {
            		tempaddToBill(newBill, s);
            	}
            	else {
            		Bill lastBill = null;
            		if(lastOrder.numberOfBills() > 0) {
            			lastBill = lastOrder.getBill(lastOrder.numberOfBills()-1);
            		}
            		tempbillForSeat(lastOrder, s);
            		if((lastOrder.numberOfBills() > 0) && (!lastOrder.getBill(lastOrder.numberOfBills() - 1).equals(lastBill))) {
            			billCreated = true;
            			newBill = lastOrder.getBill(lastOrder.numberOfBills() - 1);
            		}
            	}
            }
            if (!billCreated) {
            	throw new InvalidInputException("Error: Bill not created");
            }
            RestoAppApplication.save();
        }



    private static void tempaddToBill(Bill newBill, Seat s) {
		List<Bill> otherBills = s.getBills();	
		if (otherBills.size() > 0) {
   			for(int i = 0; i < otherBills.size(); i++) {
   				if(otherBills.get(i).numberOfIssuedForSeats() == 1) {
   					otherBills.get(i).delete();
					}
				else {
					s.removeBill(otherBills.get(i));
				}
			}
		}
    	s.addBill(newBill);
    }

	private static void tempbillForSeat(Order o, Seat s) {
		List<Bill> otherBills = s.getBills();	
    	if (otherBills.size() > 0) {
   			for(int i = 0; i < otherBills.size(); i++) {
   				if(otherBills.get(i).numberOfIssuedForSeats() == 1) {
   					otherBills.get(i).delete();
				}
				else {
					s.removeBill(otherBills.get(i));
				}
			}
		}
    	s.addBill(new Bill(o, RestoAppApplication.getRestoApp(), s));
    	o.addBill(s.getBill(0));
	}

    
    
    public static List<Seat> getSeats(Table table) throws InvalidInputException {
        if (table == null) {
            throw new InvalidInputException("No table entered.\n");
        }
        RestoApp r = RestoAppApplication.getRestoApp();
        List<Table> currentTables = r.getCurrentTables();
        boolean current = currentTables.contains(table);
        if (!current) {
            throw new InvalidInputException("The table is not active.\n");
        }
        List<Seat> seats = table.getCurrentSeats();
        return seats;
    }

    public static MenuItem getMenuItem(String menuItem) throws InvalidInputException {
        if (menuItem == null) {
            throw new InvalidInputException("No Menu Item entered.\n");
        }

        MenuItem item = null;

        RestoApp restoApp = RestoAppApplication.getRestoApp();
        Menu menu = restoApp.getMenu();
        List<MenuItem> menuItems = menu.getMenuItems();

        for (MenuItem mi : menuItems) {
            if (mi.getName().equals(menuItem)) {
                item = mi;
            }
        }

        if (item == null) {
            throw new InvalidInputException("MenuItem not found.\n");
        }
        return item;

    }
    
	/**
	 * Helper method that determines if an order is within a specified time range
	 * @param order
	 * @param startDate
	 * @param startTime
	 * @param endDate
	 * @param endTime
	 * @return
	 */
	public static boolean orderInTimeRange(Order order, Date startDate, Time startTime, Date endDate, Time endTime) throws InvalidInputException{
		boolean inRange = false;
		Date orderDate = order.getDate();
		Time orderTime = order.getTime();
		
		//construct date and time objects
		Calendar dateStart = Calendar.getInstance();
		dateStart.setTime(startDate);
		Calendar timeStart = Calendar.getInstance();
		timeStart.setTime(startTime);
		dateStart.set(Calendar.HOUR_OF_DAY, timeStart.get(Calendar.HOUR_OF_DAY));
		dateStart.set(Calendar.MINUTE, timeStart.get(Calendar.MINUTE));
		dateStart.set(Calendar.SECOND, timeStart.get(Calendar.SECOND));
		
		java.util.Date start = dateStart.getTime();
		//
		Calendar dateEnd = Calendar.getInstance();
		dateEnd.setTime(endDate);
		Calendar timeEnd = Calendar.getInstance();
		timeEnd.setTime(endTime);
		dateEnd.set(Calendar.HOUR_OF_DAY, timeEnd.get(Calendar.HOUR_OF_DAY));
		dateEnd.set(Calendar.MINUTE, timeEnd.get(Calendar.MINUTE));
		dateEnd.set(Calendar.SECOND, timeEnd.get(Calendar.SECOND));
		
		java.util.Date end = dateEnd.getTime();
		//
		Calendar dateOrder = Calendar.getInstance();
		dateOrder.setTime(orderDate);
		Calendar timeOrder = Calendar.getInstance();
		timeOrder.setTime(orderTime);
		dateOrder.set(Calendar.HOUR_OF_DAY, timeOrder.get(Calendar.HOUR_OF_DAY));
		dateOrder.set(Calendar.MINUTE, timeOrder.get(Calendar.MINUTE));
		dateOrder.set(Calendar.SECOND, timeOrder.get(Calendar.SECOND));
		
		java.util.Date timeOfOrder = dateOrder.getTime();
		
		
		//Check if date and time are within range

		if (start.after(end)) {
			throw new InvalidInputException("Start time cannot be after end time.");
		}
		if (timeOfOrder.after(start) && timeOfOrder.before(end)) {
			inRange = true;
		}
		if (timeOfOrder.equals(start) || timeOfOrder.equals(end)) {
			inRange = true;
		}
		
		
		return inRange;
	}
	
	/**
	 * Helper method for table statistics that return the top 10 tables
	 * @param tables
	 * @return
	 */
	public static List<StatisticsTable> sortAndTrimTables(List<StatisticsTable> tables){
		List<StatisticsTable> result = new ArrayList<>();
		int resultSize = 0;
		int insertIndex = 0;
		for (StatisticsTable t : tables) {
			if (result.isEmpty()) {
				result.add(t);
			}
			else {
				resultSize = result.size();
				if (t.getNumUsed() > result.get(resultSize - 1).getNumUsed()) { //if numUsed greater than last element in result
					insertIndex = resultSize - 1;
					for (int i = resultSize - 2; i >= 0; i--) {
						if (t.getNumUsed() > result.get(i).getNumUsed()) {
							insertIndex--;
						}
						else {
							break;
						}
					}
					result.add(insertIndex, t);
				}
				else if (resultSize < 10) {
					result.add(t);
				}
			}
		}
		// pop objects until only top 10 remain
		while (result.size() > 10){
			result.remove(result.get(result.size() - 1)); // removes last element
		}
		
		return result;
	}
	
	/**
	 * Helper method for item statistics that return the top 10 items
	 * @param
	 * @return
	 */
	public static List<StatisticsItem> sortAndTrimItems(List<StatisticsItem> items){
		List<StatisticsItem> result = new ArrayList<>();
		int resultSize = 0;
		int insertIndex = 0;
		for (StatisticsItem sI : items) {
			if (result.isEmpty()) {
				result.add(sI);
			}
			else {
				resultSize = result.size();
				if (sI.getNumUsed() > result.get(resultSize - 1).getNumUsed()) { //if numUsed greater than last element in result
					insertIndex = resultSize - 1;
					for (int i = resultSize - 2; i >= 0; i--) {
						if (sI.getNumUsed() > result.get(i).getNumUsed()) {
							insertIndex--;
						}
						else {
							break;
						}
					}
					result.add(insertIndex, sI);
				}
				else if (resultSize < 10) {
					result.add(sI);
				}
			}
		}
		// pop objects until only top 10 remain
		while (result.size() > 10){
			result.remove(result.get(result.size() - 1)); // removes last element
		}
		
		return result;
	}
	
	/**
	 * Gets the top 10 tables in the restaurant between a time frame
	 * @param startDate
	 * @param startTime
	 * @param endDate
	 * @param endTime
	 * @return a list of 10 best-selling tables (of type StatisticsTable), in order of most popular to least popular
	 */
	public static List<StatisticsTable> getTableStatistics(Date startDate, Time startTime, Date endDate, Time endTime) throws InvalidInputException {
		//input validation
		if (startDate == null) {
			throw new InvalidInputException ("Start date must be specified.\n");
		}
		if (startTime == null) {
			throw new InvalidInputException ("Start time must be specified.\n");
		}
		if (endDate == null) {
			throw new InvalidInputException ("End date must be specified.\n");
		}
		if (endTime == null) {
			throw new InvalidInputException ("End time must be specified.\n");
		}
		
		RestoApp restoApp = RestoAppApplication.getRestoApp();
		List<Table> allTables = restoApp.getTables();
		if (allTables.isEmpty()) {
			throw new InvalidInputException("No tables have yet been created in this app.");
		}
		int currentNumUsed = 0;
		List<StatisticsTable> tablesInTimeRange = new ArrayList<>();
		for (Table t : allTables) {
			t.setNumUsed(0);

			for (Order o: t.getOrders()) { //looping thru all orders in history of app for one table
				if (orderInTimeRange(o, startDate, startTime, endDate, endTime)) {//tables in time range

					currentNumUsed = t.getNumUsed();
					t.setNumUsed(currentNumUsed + 1);
				}	
			}
			if (t.getNumUsed() != 0) {
				StatisticsTable statTable = new StatisticsTable(t, t.getNumUsed());
				tablesInTimeRange.add(statTable);
			}
		}
		if (tablesInTimeRange.isEmpty()) {
			throw new InvalidInputException("No tables have been used in the specified time range.");
		}
		List<StatisticsTable> topTenTables = sortAndTrimTables(tablesInTimeRange); //sort stat tables per highest numUsed
		return topTenTables; 
	}
	
	/**
	 * Gets the top 10 Menu Items within a specified time frame
	 * @param startDate
	 * @param startTime
	 * @param endDate
	 * @param endTime
	 * @return
	 * @throws InvalidInputException
	 */
	public static List<StatisticsItem> getItemStatistics(Date startDate, Time startTime, Date endDate, Time endTime) throws InvalidInputException {
		//input validation
				if (startDate == null) {
					throw new InvalidInputException ("Start date must be specified.\n");
				}
				if (startTime == null) {
					throw new InvalidInputException ("Start time must be specified.\n");
				}
				if (endDate == null) {
					throw new InvalidInputException ("End date must be specified.\n");
				}
				if (endTime == null) {
					throw new InvalidInputException ("End time must be specified.\n");
				}
				
				RestoApp restoApp = RestoAppApplication.getRestoApp();
				List<Order> allOrders = restoApp.getOrders();
				if (allOrders.isEmpty()) {
					throw new InvalidInputException ("No orders have been placed yet.");
				}
				//clear all menu item numUsed
				List<MenuItem> allMenuItems = restoApp.getMenu().getMenuItems();
				if (allMenuItems.isEmpty()) {
					throw new InvalidInputException ("No history of menu items detected.");
				}
				for (MenuItem m : allMenuItems) {
					m.setNumUsed(0);
				}
				for (Order order : allOrders) {
					if(orderInTimeRange(order, startDate, startTime, endDate, endTime)){
						List<OrderItem> orderItems = order.getOrderItems();
						for (OrderItem item : orderItems) {
							item.getPricedMenuItem().getMenuItem().setNumUsed(item.getPricedMenuItem().getMenuItem().getNumUsed() + item.getQuantity());
						}
					}
				}
				
				List<StatisticsItem> itemsInTimeRange = new ArrayList<>();
				for (MenuItem menuItem : allMenuItems) {
					if (menuItem.getNumUsed() != 0) {
						StatisticsItem statItem = new StatisticsItem(menuItem, menuItem.getNumUsed());
						itemsInTimeRange.add(statItem);
					}
				}
				if(itemsInTimeRange.isEmpty()) {
					throw new InvalidInputException("No items were ordered in the specified time range.");
				}
		List<StatisticsItem> topTenItems = sortAndTrimItems(itemsInTimeRange);
		if(topTenItems.isEmpty()) {
			throw new InvalidInputException("No items were ordered in the specified time range.");
		}
		return topTenItems;
	
	}

}
