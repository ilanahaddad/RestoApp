package ca.mcgill.ecse223.resto.controller;

import ca.mcgill.ecse223.resto.model.Menu;
import ca.mcgill.ecse223.resto.model.MenuItem;

import java.io.File;

public class RestoController {
    /**
     * Updates desired item from the menu
     * @param  menuFile  menu read from file to be displayed
     */
    public static Menu readMenu (File menuFile) throws InvalidInputException {}

    /**
     * Removes desired item from the menu
     * @param  name  name of the created menu item
     * @param  category  enum category for the menu item
     */
    public static MenuItem createMenuItem  (String name, MenuItem.ItemCategory category) throws InvalidInputException {}

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

}
