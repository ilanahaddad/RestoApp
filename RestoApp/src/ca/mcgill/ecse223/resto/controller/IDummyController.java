package ca.mcgill.ecse223.resto.controller;

import ca.mcgill.ecse223.resto.model.Menu;
import ca.mcgill.ecse223.resto.model.MenuItem;

public interface IDummyController
{
    /**
     * Determines if a given menu item is present in the menu
     * @param menu      Instance of Menu in which to search
     * @param item      Instance of Item to search for
     * @throws InputException corresponding to an invalid item or menu
     */
    public boolean menuHasItem(Menu menu, MenuItem item);
}