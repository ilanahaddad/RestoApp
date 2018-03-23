package ca.mcgill.ecse223.resto.controller;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import ca.mcgill.ecse223.resto.application.RestoAppApplication;
import ca.mcgill.ecse223.resto.model.Bill;
import ca.mcgill.ecse223.resto.model.Menu;
import ca.mcgill.ecse223.resto.model.MenuItem;
import ca.mcgill.ecse223.resto.model.Order;
import ca.mcgill.ecse223.resto.model.Reservation;
import ca.mcgill.ecse223.resto.model.RestoApp;
import ca.mcgill.ecse223.resto.model.Seat;
import ca.mcgill.ecse223.resto.model.Table;

public class RestoController
{
	public static List<MenuItem.ItemCategory> getItemCategories() {
	    return Arrays.asList(MenuItem.ItemCategory.values());
	  }

	  public static List<MenuItem> getMenuItems (MenuItem.ItemCategory itemCategory) throws InvalidInputException {
	      List<MenuItem> items = new ArrayList<>();
	    RestoApp restoApp = RestoAppApplication.getRestoApp();
	    Menu menu = restoApp.getMenu();

	    List<MenuItem> menuItems = menu.getMenuItems();

	    for(MenuItem mi : menuItems) {
	      boolean current = mi.hasCurrentPricedMenuItem();
	      MenuItem.ItemCategory category = mi.getItemCategory();

	      if(current && category.equals(itemCategory)){
	        items.add(mi);
	      }

	    }

	    return items;

	  }
    /**
     * Updates desired item from the menu
     * @param  menuFile  menu read from file to be displayed
     */
    public static Menu readMenu (File menuFile) throws InvalidInputException { return null; }

    /**
     * Removes desired item from the menu
     * @param  name  name of the created menu item
     * @param  category  enum category for the menu item
     */
    public static MenuItem createMenuItem  (String name, MenuItem.ItemCategory category) throws InvalidInputException { return null; }

    /**
     * @param item  item to be added to the menu
     */
    public static void addMenuItem (MenuItem item) throws InvalidInputException {}

    /**
     * Removes desired item from the menu
     * @param  item  the item to be removed
     */
    public static void removeMenuItem(MenuItem item) throws InvalidInputException {}

    /**
     * Updates desired item from the menu
     * @param  item  item to be updated
     */
    public static void updateMenuItem(MenuItem item) throws InvalidInputException {}

    /**
     * Creates table and its seats and adds them to the application
     * @throws InvalidInputException If the table number already exists
     */
    public static void createTableAndSeats(
            int numSeats, int tableNum, int x, int y, int width, int length
    ) throws InvalidInputException
    {
    		String error="";
        RestoApp restoApp = RestoAppApplication.getRestoApp();

        if (overlapsOtherTables(x, y, width, length, restoApp.getCurrentTables()))
        {
            error += "Input table overlaps with another table. \n";
        }
        if (x<0 || y<0 || tableNum<0 || numSeats<0 || width<=0 || length<=0)
        {
            error += "Input must be positive.";
        }
        if (error.length() > 0){
            throw new InvalidInputException(error.trim());
        }
        
        Table tableToAdd;
        // only bring an existent table to current IFF it has exactly the same attributes
        // if not it is simply not the same table and must have a new table number
        if (exactTableNotInCurrent(tableNum, numSeats, width, length) &&
                exactTableInApp(tableNum, numSeats, width, length))
        {
            tableToAdd = getTableByNum(tableNum);
            tableToAdd.setX(x);
            tableToAdd.setY(y);
        }
        // table is new to the application
        else{
            try{
            	tableToAdd = new Table(tableNum, x, y, width, length, restoApp); //throws runtime exception if number is duplicate
            }
            catch(RuntimeException e) {
            		error = e.getMessage();
            		if(error.equals("Cannot create due to duplicate number")) {
            			error = "A table with this number already exists. Please use a different number.\n"; 
            		}
                throw new InvalidInputException(error);
            }
            restoApp.addTable(tableToAdd);

            for (int i=0; i<numSeats; i++)
            {
                Seat newSeat = tableToAdd.addSeat();
                tableToAdd.addCurrentSeat(newSeat);
            }
        }

        restoApp.addCurrentTable(tableToAdd);
        RestoAppApplication.save();
    }

    /**
     * Removes a table from the current tables
     * @param table table to be removed
     * @throws InvalidInputException If the specified table does not exist
     */
    public static void removeTable(Table table) throws InvalidInputException {
        String error = "";
        if (table == null){
            throw new InvalidInputException("Input table does not exist");
        }

        boolean reserved = table.hasReservations();
        if (reserved){
            error += "Table is reserved and cannot be removed.\n";
        }
        RestoApp r = RestoAppApplication.getRestoApp();
        List<Order> currentOrders = r.getCurrentOrders();

        List<Table> tables;
        boolean inUse;
        for (Order order : currentOrders){
            tables = order.getTables();
            inUse = tables.contains(table);
            if (inUse){
                error += "Cannot remove: Selected table is currently in use.\n";
            }
        }
        if (error.length() > 0){
            throw new InvalidInputException(error.trim());
        }

        try{
            r.removeCurrentTable(table);
            RestoAppApplication.save();
        }
        catch (RuntimeException e){
            throw new InvalidInputException(e.getMessage());
        }
    }

    /**
     * Updates table number and number of seats at the table
     * @param table table to update
     * @param newNumber new table number to update to
     * @param numberOfSeats number of seats for updated table to have
     * @throws InvalidInputException if table doesn't exist, if newNumber and numberOfSeats aren't positive
     * 		and if table is reserved
     */
    public static void updateTable(Table table, int newNumber, int numberOfSeats) throws InvalidInputException{
        String error = "";
        RestoApp restoApp = RestoAppApplication.getRestoApp();
        if(table == null) {
            error += "Input Table does not exist.\n";
        }
        if(newNumber < 0) {
            error += "New table number must be positive.\n";
        }
        if(numberOfSeats < 0) {
            error += "Number of seats must be positive.\n";
        }
        if(table.hasReservations()) {
            error += "Can't update a table that is reserved.\n";
        }
        List<Order> currentOrders = restoApp.getCurrentOrders();
        boolean inUse = false;
        for(Order o: currentOrders) { //check if table to update has orders (i.e. inUse)
            List<Table> tablesWithOrders = o.getTables();
            inUse = tablesWithOrders.contains(table);
        }
        if(inUse) {
            error += "Can't update a table that is currently in use.\n";
        }
        
        if(!table.setNumber(newNumber)) {
    			error+= "A table with this number already exists. Please use a different number.\n";
        }
        if(error.length() > 0) {
            throw new InvalidInputException(error.trim());
        }
       
        int n = table.numberOfCurrentSeats();
        //Add seats if new numberOfSeats > numberOfCurrentSeats:
        for(int i = 1; i <= numberOfSeats - n; i++) {
            Seat seat = table.addSeat();
            table.addCurrentSeat(seat);
        }
        //Remove seats if new numberOfSeats < numberOfCurrentSeats:
        for(int i = 1; i <= n - numberOfSeats; i++) {
            Seat seat = table.getCurrentSeat(0);
            table.removeCurrentSeat(seat);
        }
        RestoAppApplication.save();
    }
    
    /**
     * TODO: Fill moveTable api comment
     * @param table
     * @param newX
     * @param newY
     * @throws InvalidInputException
     */
	public static void moveTable(Table table, int newX, int newY) throws InvalidInputException {
		String error = "";
		if(table==null) {
			error+= "Input table does not exist.\n";
		}
		if(newX < 0 || newY < 0) {
			error+= "X and Y must be positive.\n";
		}
		int width = table.getWidth();
		int length = table.getLength();
		RestoApp r = RestoAppApplication.getRestoApp();
		List<Table> currentTables = r.getCurrentTables();
		List<Table> currentStationaryTables = new ArrayList<>();
		for(Table i : currentTables) {
			if (table.equals(i) == false) {
				currentStationaryTables.add(i);
			}
		}
        if (overlapsOtherTables(newX, newY, width, length, currentStationaryTables)){
        	error += "Input table overlaps with another table. \n";
        }
        if(error.length() > 0) {
            throw new InvalidInputException(error.trim());
        }
        table.setX(newX);
        table.setY(newY);
        RestoAppApplication.save();
        
	}
	
	/**
	 * Make a reservation of one or more tables under a single name
	 * @param date date of the reservation
	 * @param time time of the reservation
	 * @param numberInParty number of people that are in the same reservation group
	 * @param contactName name of the reservation client
	 * @param contactEmailAddress e-mail
	 * @param contactPhoneNumber phone number
	 * @param tables list of tables of type Table
	 * @throws InvalidInputException
	 */
	public static void reserveTable(Date date, Time time, int numberInParty, String contactName, String contactEmailAddress, String contactPhoneNumber, List<Table> tables) throws InvalidInputException {
		
		String error = "";
		if (date == null || time == null ) {
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
		if (contactPhoneNumber.equals("")||contactPhoneNumber == null) {
			error += "Please enter a contact phone number.\n";
		}
		Date currentDate = new Date(Calendar.getInstance().getTime().getTime());
		if (date.before(currentDate) || (date.equals(currentDate) && time.before(currentDate))) {
			error += "Cannot make reservation in the past.\n";
		}
		if (numberInParty < 1) {
			error += "Members in a party cannot be negative and must be at least 1.\n";
		}
		if(error.length() > 0) {
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
			seatCapacity += table.numberOfCurrentSeats();
			reservations = table.getReservations();
			
			for (Reservation reservation : reservations) {
				overlaps = reservationDoesOverlap(reservation, date, time);
				if (overlaps) {
					throw new InvalidInputException("Reservation overlaps.\n");
				}
			}
		}
		if (seatCapacity < numberInParty) throw new InvalidInputException("Too many people in the party for the selected tables.\n");
		
		Table[] tableArray = new Table[tables.size()];
		for (int i = 0; i < tables.size(); i++) {
			tableArray[i] = tables.get(i);
		}
		Reservation res = new Reservation(date, time, numberInParty, contactName, contactEmailAddress, contactPhoneNumber, r, tableArray);
		r.addReservation(res); //not in seq diagram? should I remove? (PotassiumK)
		RestoAppApplication.save();
	}
	
	/**
	 * Compare the date and time of two reservations to see if they overlap
	 * @param existingReservation the reservation that needs comparing to
	 * @param date date of new reservation to be compared
	 * @param time time of new reservation to be compared
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
		if(existingReservation.getDate().equals(date)) {
			//if(Math.abs(existingReservation.getTime().getHours() - time.getHours()) > 2) {
			if(Math.abs(timeHour - resHour) <= 2) {
				overlapStatus = true;
			}
		}
		
		return overlapStatus;
	}
	
    public static Table getTableByNum(int tableNum) throws InvalidInputException {
        RestoApp restoApp = RestoAppApplication.getRestoApp();
        for (Table table : restoApp.getTables())
        {
            if (table.getNumber() == tableNum) { return table; }
        }
        throw new InvalidInputException("Could not retrieve table number " + tableNum);
    }
    /**
     * 
     * @param tableNum
     * @return
     * @throws InvalidInputException
     */
    public static Table getCurrentTableByNum(int tableNum) throws InvalidInputException {
        RestoApp restoApp = RestoAppApplication.getRestoApp();
        for (Table table : restoApp.getCurrentTables())
        {
            if (table.getNumber() == tableNum) { return table; }
        }
        throw new InvalidInputException("Could not retrieve table number " + tableNum);
    }
    
//to here
    // checks if the table with given attributes is in the set tables in the app
    private static boolean exactTableInApp(int tableNum, int numSeats, int width, int length)
    {
        RestoApp restoApp = RestoAppApplication.getRestoApp();
        for (Table table : restoApp.getTables())
        {
            if (areExactSameTable(table, tableNum, numSeats, width, length))
            { return true; }
        }
        return false;
    }

    // checks if the table with given attributes is not in the set of current tables
    private static boolean exactTableNotInCurrent(int tableNum, int numSeats, int width, int length)
    {
        RestoApp restoApp = RestoAppApplication.getRestoApp();
        for (Table table : restoApp.getCurrentTables())
        {
            if (areExactSameTable(table, tableNum, numSeats, width, length))
            { return false; }
        }
        return true;
    }

    // validates if 2 tables are equal (x,y does not matter)
    private static boolean areExactSameTable(Table table, int tableNum, int numSeats, int width, int length)
    {
        return table.getNumber() == tableNum && table.getSeats().size() == numSeats &&
                table.getWidth() == width && table.getLength() == length;
    }

    private static boolean overlapsOtherTables(int x, int y, int width, int length, List<Table> tables) {
        Shape newTableShape = new Rectangle2D.Float(x, y, width, length);
        for (Table table : tables)
        {
            Shape tableShape = new Rectangle2D.Float(table.getX(), table.getY(), table.getWidth(), table.getLength());
            if (tableShape.getBounds2D().intersects(newTableShape.getBounds2D())) { return true; }
        }
        return false;
    }

    public static List<Table> getCurrentTables() {
        RestoApp restoApp = RestoAppApplication.getRestoApp();
        return restoApp.getCurrentTables();
    }

    public static Table getCurrentTable(int tableNum){
        RestoApp restoApp = RestoAppApplication.getRestoApp();
        return restoApp.getCurrentTable(tableNum);
    }
    public static List<Table> getTables(){
      RestoApp restoApp = RestoAppApplication.getRestoApp();
      return restoApp.getTables();
    }
    public static Table getTable(int tableNum){
        RestoApp restoApp = RestoAppApplication.getRestoApp();
        return restoApp.getTable(tableNum);
    }
    // get largest Y coordinate of all the app's current tables
    public static int getMaxX()
    {
        RestoApp restoApp = RestoAppApplication.getRestoApp();
        int maxX = 0;
        for (Table table : restoApp.getCurrentTables())
        {
            if (maxX < table.getX() + table.getWidth()) { maxX = table.getX() + table.getWidth(); }
        }
        return maxX;
    }

    // get largest Y coordinate of all the app's current tables
    public static int getMaxY()
    {
        RestoApp restoApp = RestoAppApplication.getRestoApp();
        int maxY = 0;
        for (Table table : restoApp.getCurrentTables())
        {
            if (maxY < table.getY() + table.getLength()) { maxY = table.getY() + table.getLength(); }
        }
        return maxY;
    }
    public static void startOrder(List<Table> tables) throws InvalidInputException{
    		if(tables == null) {
    			throw new InvalidInputException("Error: Tables can't be null.\n");
    		}
    		RestoApp r = RestoAppApplication.getRestoApp();
    		List<Table> currentTables = r.getCurrentTables();
    		boolean current =false;
    		for(Table t: tables) { //loop through tables given 
    			current = currentTables.contains(t); //check if each table given is contained in currentTables
    			if(!current) {
    				throw new InvalidInputException("Error: One of the tables is not current");
    			}
    		}
    		boolean orderCreated = false;
    		Order newOrder = null;
    		for(Table t: tables) { 
    			if(orderCreated) { 
    				t.addToOrder(newOrder);
    			}
    			else { //new Order: 
    				Order lastOrder = null;
    				if(t.numberOfOrders() >0) { //if table has order(s), get last one 
    					lastOrder = t.getOrder(t.numberOfOrders()-1);
    				}
    				t.startOrder(); //now lastOrder is secondToLast (or null if new order was first one)
    				if(t.numberOfOrders() > 0 && !t.getOrder(t.numberOfOrders()-1).equals(lastOrder)) {
    				//checks that order just created is not equal to previous last order from tables orders 
    					//(checks that t.startOrder successfully added order to table)
    					orderCreated = true; 
    					newOrder = t.getOrder(t.numberOfOrders()-1); //pass last order of table to newOrder object 
    				}
    			}
    		}
    		if(!orderCreated) {
    			//went through loop for all tables and an order was never created 
    			throw new InvalidInputException("Order not created: failed for all tables.\n");
    		}
    		r.addCurrentOrder(newOrder); //added newly created order to list of current orders
    		RestoAppApplication.save();
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    


}
