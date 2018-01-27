/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.27.0.3758.c600698 modeling language!*/


import java.util.*;

// line 17 "model.ump"
// line 115 "model.ump"
public class Menu
{

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static Menu theInstance = null;

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Menu Attributes
  private String pathToMenuFile;

  //Menu Associations
  private RestoAppManager restoAppManager;
  private List<Item> items;
  private List<Order> orders;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  private Menu()
  {
    pathToMenuFile = null;
    items = new ArrayList<Item>();
    orders = new ArrayList<Order>();
  }

  public static Menu getInstance()
  {
    if(theInstance == null)
    {
      theInstance = new Menu();
    }
    return theInstance;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setPathToMenuFile(String aPathToMenuFile)
  {
    boolean wasSet = false;
    pathToMenuFile = aPathToMenuFile;
    wasSet = true;
    return wasSet;
  }

  public String getPathToMenuFile()
  {
    return pathToMenuFile;
  }
  /* Code from template association_GetOne */
  public RestoAppManager getRestoAppManager()
  {
    return restoAppManager;
  }

  public boolean hasRestoAppManager()
  {
    boolean has = restoAppManager != null;
    return has;
  }
  /* Code from template association_GetMany */
  public Item getItem(int index)
  {
    Item aItem = items.get(index);
    return aItem;
  }

  public List<Item> getItems()
  {
    List<Item> newItems = Collections.unmodifiableList(items);
    return newItems;
  }

  public int numberOfItems()
  {
    int number = items.size();
    return number;
  }

  public boolean hasItems()
  {
    boolean has = items.size() > 0;
    return has;
  }

  public int indexOfItem(Item aItem)
  {
    int index = items.indexOf(aItem);
    return index;
  }
  /* Code from template association_GetMany */
  public Order getOrder(int index)
  {
    Order aOrder = orders.get(index);
    return aOrder;
  }

  public List<Order> getOrders()
  {
    List<Order> newOrders = Collections.unmodifiableList(orders);
    return newOrders;
  }

  public int numberOfOrders()
  {
    int number = orders.size();
    return number;
  }

  public boolean hasOrders()
  {
    boolean has = orders.size() > 0;
    return has;
  }

  public int indexOfOrder(Order aOrder)
  {
    int index = orders.indexOf(aOrder);
    return index;
  }
  /* Code from template association_SetOptionalOneToOptionalOne */
  public boolean setRestoAppManager(RestoAppManager aNewRestoAppManager)
  {
    boolean wasSet = false;
    if (aNewRestoAppManager == null)
    {
      RestoAppManager existingRestoAppManager = restoAppManager;
      restoAppManager = null;
      
      if (existingRestoAppManager != null && existingRestoAppManager.getMenu() != null)
      {
        existingRestoAppManager.setMenu(null);
      }
      wasSet = true;
      return wasSet;
    }

    RestoAppManager currentRestoAppManager = getRestoAppManager();
    if (currentRestoAppManager != null && !currentRestoAppManager.equals(aNewRestoAppManager))
    {
      currentRestoAppManager.setMenu(null);
    }

    restoAppManager = aNewRestoAppManager;
    Menu existingMenu = aNewRestoAppManager.getMenu();

    if (!equals(existingMenu))
    {
      aNewRestoAppManager.setMenu(this);
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_IsNumberOfValidMethod */
  public boolean isNumberOfItemsValid()
  {
    boolean isValid = numberOfItems() >= minimumNumberOfItems();
    return isValid;
  }

  public static int minimumNumberOfItems()
  {
    return 1;
  }
  /* Code from template association_AddMandatoryManyToOne */
  public Item addItem(String aName, String aDescription, float aPrice)
  {
    Item aNewItem = new Item(aName, aDescription, aPrice, this);
    return aNewItem;
  }

  public boolean addItem(Item aItem)
  {
    boolean wasAdded = false;
    if (items.contains(aItem)) { return false; }
    Menu existingMenu = aItem.getMenu();
    boolean isNewMenu = existingMenu != null && !this.equals(existingMenu);

    if (isNewMenu && existingMenu.numberOfItems() <= minimumNumberOfItems())
    {
      return wasAdded;
    }
    if (isNewMenu)
    {
      aItem.setMenu(this);
    }
    else
    {
      items.add(aItem);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeItem(Item aItem)
  {
    boolean wasRemoved = false;
    //Unable to remove aItem, as it must always have a menu
    if (this.equals(aItem.getMenu()))
    {
      return wasRemoved;
    }

    //menu already at minimum (1)
    if (numberOfItems() <= minimumNumberOfItems())
    {
      return wasRemoved;
    }

    items.remove(aItem);
    wasRemoved = true;
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addItemAt(Item aItem, int index)
  {  
    boolean wasAdded = false;
    if(addItem(aItem))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfItems()) { index = numberOfItems() - 1; }
      items.remove(aItem);
      items.add(index, aItem);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveItemAt(Item aItem, int index)
  {
    boolean wasAdded = false;
    if(items.contains(aItem))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfItems()) { index = numberOfItems() - 1; }
      items.remove(aItem);
      items.add(index, aItem);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addItemAt(aItem, index);
    }
    return wasAdded;
  }

  public static int minimumNumberOfOrders()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Order addOrder(RestoAppManager aRestoAppManager, Seat aSeat)
  {
    return new Order(aRestoAppManager, aSeat, this);
  }

  public boolean addOrder(Order aOrder)
  {
    boolean wasAdded = false;
    if (orders.contains(aOrder)) { return false; }
    Menu existingMenu = aOrder.getMenu();
    boolean isNewMenu = existingMenu != null && !this.equals(existingMenu);
    if (isNewMenu)
    {
      aOrder.setMenu(this);
    }
    else
    {
      orders.add(aOrder);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeOrder(Order aOrder)
  {
    boolean wasRemoved = false;
    //Unable to remove aOrder, as it must always have a menu
    if (!this.equals(aOrder.getMenu()))
    {
      orders.remove(aOrder);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addOrderAt(Order aOrder, int index)
  {  
    boolean wasAdded = false;
    if(addOrder(aOrder))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfOrders()) { index = numberOfOrders() - 1; }
      orders.remove(aOrder);
      orders.add(index, aOrder);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveOrderAt(Order aOrder, int index)
  {
    boolean wasAdded = false;
    if(orders.contains(aOrder))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfOrders()) { index = numberOfOrders() - 1; }
      orders.remove(aOrder);
      orders.add(index, aOrder);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addOrderAt(aOrder, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    if (restoAppManager != null)
    {
      restoAppManager.setMenu(null);
    }
    for(int i=items.size(); i > 0; i--)
    {
      Item aItem = items.get(i - 1);
      aItem.delete();
    }
    for(int i=orders.size(); i > 0; i--)
    {
      Order aOrder = orders.get(i - 1);
      aOrder.delete();
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "pathToMenuFile" + ":" + getPathToMenuFile()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "restoAppManager = "+(getRestoAppManager()!=null?Integer.toHexString(System.identityHashCode(getRestoAppManager())):"null");
  }
}