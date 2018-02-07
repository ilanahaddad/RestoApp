/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.27.0.3781.8b4a64e modeling language!*/

package ca.mcgill.ecse223.resto.model;
import java.util.*;

// line 51 "../../../../../../../../ump/tmp842728/model.ump"
// line 103 "../../../../../../../../ump/tmp842728/model.ump"
public class Menu
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Menu Associations
  private List<MenuItem> menuItems;
  private RestoApp restoApp;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Menu(RestoApp aRestoApp)
  {
    menuItems = new ArrayList<MenuItem>();
    if (aRestoApp == null || aRestoApp.getMenu() != null)
    {
      throw new RuntimeException("Unable to create Menu due to aRestoApp");
    }
    restoApp = aRestoApp;
  }

  public Menu()
  {
    menuItems = new ArrayList<MenuItem>();
    restoApp = new RestoApp(this);
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetMany */
  public MenuItem getMenuItem(int index)
  {
    MenuItem aMenuItem = menuItems.get(index);
    return aMenuItem;
  }

  public List<MenuItem> getMenuItems()
  {
    List<MenuItem> newMenuItems = Collections.unmodifiableList(menuItems);
    return newMenuItems;
  }

  public int numberOfMenuItems()
  {
    int number = menuItems.size();
    return number;
  }

  public boolean hasMenuItems()
  {
    boolean has = menuItems.size() > 0;
    return has;
  }

  public int indexOfMenuItem(MenuItem aMenuItem)
  {
    int index = menuItems.indexOf(aMenuItem);
    return index;
  }
  /* Code from template association_GetOne */
  public RestoApp getRestoApp()
  {
    return restoApp;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfMenuItems()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public MenuItem addMenuItem(String aName)
  {
    return new MenuItem(aName, this);
  }

  public boolean addMenuItem(MenuItem aMenuItem)
  {
    boolean wasAdded = false;
    if (menuItems.contains(aMenuItem)) { return false; }
    Menu existingMenu = aMenuItem.getMenu();
    boolean isNewMenu = existingMenu != null && !this.equals(existingMenu);
    if (isNewMenu)
    {
      aMenuItem.setMenu(this);
    }
    else
    {
      menuItems.add(aMenuItem);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeMenuItem(MenuItem aMenuItem)
  {
    boolean wasRemoved = false;
    //Unable to remove aMenuItem, as it must always have a menu
    if (!this.equals(aMenuItem.getMenu()))
    {
      menuItems.remove(aMenuItem);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addMenuItemAt(MenuItem aMenuItem, int index)
  {  
    boolean wasAdded = false;
    if(addMenuItem(aMenuItem))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfMenuItems()) { index = numberOfMenuItems() - 1; }
      menuItems.remove(aMenuItem);
      menuItems.add(index, aMenuItem);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveMenuItemAt(MenuItem aMenuItem, int index)
  {
    boolean wasAdded = false;
    if(menuItems.contains(aMenuItem))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfMenuItems()) { index = numberOfMenuItems() - 1; }
      menuItems.remove(aMenuItem);
      menuItems.add(index, aMenuItem);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addMenuItemAt(aMenuItem, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    while (menuItems.size() > 0)
    {
      MenuItem aMenuItem = menuItems.get(menuItems.size() - 1);
      aMenuItem.delete();
      menuItems.remove(aMenuItem);
    }
    
    RestoApp existingRestoApp = restoApp;
    restoApp = null;
    if (existingRestoApp != null)
    {
      existingRestoApp.delete();
    }
  }

}