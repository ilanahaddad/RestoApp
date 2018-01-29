/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.27.0.3728.d139ed893 modeling language!*/

package ca.mcgill.ecse223.resto.model;

// line 3 "../../../../../RestoApp.ump"
public class Item
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum Category { Food, Beverage }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Item Attributes
  private String name;
  private String description;
  private float price;

  //Item Associations
  private Menu menu;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Item(String aName, float aPrice, Menu aMenu)
  {
    name = aName;
    description = null;
    price = aPrice;
    boolean didAddMenu = setMenu(aMenu);
    if (!didAddMenu)
    {
      throw new RuntimeException("Unable to create item due to menu");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setName(String aName)
  {
    boolean wasSet = false;
    name = aName;
    wasSet = true;
    return wasSet;
  }

  public boolean setDescription(String aDescription)
  {
    boolean wasSet = false;
    description = aDescription;
    wasSet = true;
    return wasSet;
  }

  public boolean setPrice(float aPrice)
  {
    boolean wasSet = false;
    price = aPrice;
    wasSet = true;
    return wasSet;
  }

  public String getName()
  {
    return name;
  }

  public String getDescription()
  {
    return description;
  }

  public float getPrice()
  {
    return price;
  }

  public Menu getMenu()
  {
    return menu;
  }

  public boolean setMenu(Menu aMenu)
  {
    boolean wasSet = false;
    //Must provide menu to item
    if (aMenu == null)
    {
      return wasSet;
    }

    if (menu != null && menu.numberOfItems() <= Menu.minimumNumberOfItems())
    {
      return wasSet;
    }

    Menu existingMenu = menu;
    menu = aMenu;
    if (existingMenu != null && !existingMenu.equals(aMenu))
    {
      boolean didRemove = existingMenu.removeItem(this);
      if (!didRemove)
      {
        menu = existingMenu;
        return wasSet;
      }
    }
    menu.addItem(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    Menu placeholderMenu = menu;
    this.menu = null;
    if(placeholderMenu != null)
    {
      placeholderMenu.removeItem(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "name" + ":" + getName()+ "," +
            "description" + ":" + getDescription()+ "," +
            "price" + ":" + getPrice()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "menu = "+(getMenu()!=null?Integer.toHexString(System.identityHashCode(getMenu())):"null");
  }
}