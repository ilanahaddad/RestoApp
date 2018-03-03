package ca.mcgill.ecse223.resto.controller;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.List;

import ca.mcgill.ecse223.resto.application.RestoAppApplication;
import ca.mcgill.ecse223.resto.model.Menu;
import ca.mcgill.ecse223.resto.model.MenuItem;
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

    if (overlapsOtherTables(x, y, width, length, restoApp.getTables()))
    {
      throw new InvalidInputException("Input table overlaps with another table");
    }

    Table newTable = new Table(tableNum, x, y, width, length, restoApp);
    
    for (int i=0; i<numSeats; i++)
    {
      Seat newSeat = newTable.addSeat();
      newTable.addCurrentSeat(newSeat);
    }
    
    RestoAppApplication.save();
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

  public static List<Table> getTables()
    {
      RestoApp restoApp = RestoAppApplication.getRestoApp();
      return restoApp.getTables();
    }
}
