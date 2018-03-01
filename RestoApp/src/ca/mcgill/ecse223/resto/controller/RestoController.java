package ca.mcgill.ecse223.resto.controller;

import java.io.File;
import java.util.List;

import ca.mcgill.ecse223.resto.application.RestoAppApplication;
import ca.mcgill.ecse223.resto.model.Menu;
import ca.mcgill.ecse223.resto.model.MenuItem;
import ca.mcgill.ecse223.resto.model.Order;
import ca.mcgill.ecse223.resto.model.RestoApp;
import ca.mcgill.ecse223.resto.model.Seat;
import ca.mcgill.ecse223.resto.model.Table;

public class RestoController {
  
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
    Table newTable = new Table(tableNum, x, y, width, length, restoApp);
    
    for (int i=0; i<numSeats; i++)
    {
      Seat newSeat = newTable.addSeat();
      newTable.addCurrentSeat(newSeat);
    }
    
    RestoAppApplication.save();
  };
  
  /**
   * Removes a table from the current tables
   * @param Table table to be removed
   * @throws InvalidInputException If the specified table does not exist
   */
  public static void removeTable(Table table) throws InvalidInputException {
	  try {
		  String error = "";
		  boolean reserved = table.hasReservations();
		  if (reserved == true){
			  error = "Table is reserved and cannot be removed.";
		  }
		  if (error.length() > 0){
			  throw new InvalidInputException(error.trim());
		  }
		  RestoApp r = RestoAppApplication.getRestoApp();
		  List<Order> currentOrders = r.getCurrentOrders();
		  
		  
	  }
	  catch (RuntimeException e){
		  throw new InvalidInputException(e.getMessage());
	  }
  }
}
