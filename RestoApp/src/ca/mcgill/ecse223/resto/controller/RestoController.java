package ca.mcgill.ecse223.resto.controller;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.List;

import ca.mcgill.ecse223.resto.application.RestoAppApplication;
import ca.mcgill.ecse223.resto.model.Menu;
import ca.mcgill.ecse223.resto.model.MenuItem;
import ca.mcgill.ecse223.resto.model.Order;
import ca.mcgill.ecse223.resto.model.RestoApp;
import ca.mcgill.ecse223.resto.model.Seat;
import ca.mcgill.ecse223.resto.model.Table;

public class RestoController
{
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
        RestoApp restoApp = RestoAppApplication.getRestoApp();

        if (overlapsOtherTables(x, y, width, length, restoApp.getCurrentTables()))
        {
            throw new InvalidInputException("Input table overlaps with another table");
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
        else
        {
            tableToAdd = new Table(tableNum, x, y, width, length, restoApp);
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
            error = error + "Table is reserved and cannot be removed.";
        }
        RestoApp r = RestoAppApplication.getRestoApp();
        List<Order> currentOrders = r.getCurrentOrders();

        List<Table> tables;
        boolean inUse;
        for (Order order : currentOrders){
            tables = order.getTables();
            inUse = tables.contains(table);
            if (inUse){
                error = error + "Cannot remove: Selected table is currently in use.";
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
     * @throws InvalidInputException if table with given number doesn't exist, if newNumber and numberOfSeats aren't positive
     * 		and if table is reserved
     */
    public static void updateTable(Table table, int newNumber, int numberOfSeats) throws InvalidInputException{
        String error = "";
        RestoApp restoApp = RestoAppApplication.getRestoApp();
        if(table == null) {
            error = "A table with this number does not exist. ";
        }
        if(newNumber < 0) {
            error = "New table number must be positive. ";
        }
        if(numberOfSeats < 0) {
            error = "Number of seats must be positive. ";
        }
        if(table.hasReservations()) {
            error = "Can't update a table that is reserved. ";
        }
        List<Order> currentOrders = restoApp.getCurrentOrders();
        boolean inUse = false;
        for(Order o: currentOrders) { //check if table to update has orders (i.e. inUse)
            List<Table> tablesWithOrders = o.getTables();
            inUse = tablesWithOrders.contains(table);
        }
        if(inUse) {
            error = "Can't update a table that is currently in use.";
        }
        if(error.length() > 0) {
            throw new InvalidInputException(error.trim());
        }
        try{
            table.setNumber(newNumber); //throws runtime exception if number is duplicate
        }
        catch(RuntimeException e) {
            throw new InvalidInputException(e.getMessage());
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

    private static Table getTableByNum(int tableNum) throws InvalidInputException {
        RestoApp restoApp = RestoAppApplication.getRestoApp();
        for (Table table : restoApp.getTables())
        {
            if (table.getNumber() == tableNum) { return table; }
        }
        throw new InvalidInputException("Could not retrieve table number " + tableNum);
    }

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
        System.out.println("T: " +table.getNumber()+"\tNEW: "+tableNum);
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
}
