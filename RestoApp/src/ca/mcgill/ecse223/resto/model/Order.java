/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.27.0.3728.d139ed893 modeling language!*/

package ca.mcgill.ecse223.resto.model;

// line 24 "../../../../../RestoApp.ump"
public class Order
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Order Associations
  private RestoAppManager restoAppManager;
  private Bill bill;
  private Seat seat;
  private Menu menu;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Order(RestoAppManager aRestoAppManager, Seat aSeat, Menu aMenu)
  {
    boolean didAddRestoAppManager = setRestoAppManager(aRestoAppManager);
    if (!didAddRestoAppManager)
    {
      throw new RuntimeException("Unable to create order due to restoAppManager");
    }
    boolean didAddSeat = setSeat(aSeat);
    if (!didAddSeat)
    {
      throw new RuntimeException("Unable to create order due to seat");
    }
    boolean didAddMenu = setMenu(aMenu);
    if (!didAddMenu)
    {
      throw new RuntimeException("Unable to create order due to menu");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public RestoAppManager getRestoAppManager()
  {
    return restoAppManager;
  }

  public Bill getBill()
  {
    return bill;
  }

  public boolean hasBill()
  {
    boolean has = bill != null;
    return has;
  }

  public Seat getSeat()
  {
    return seat;
  }

  public Menu getMenu()
  {
    return menu;
  }

  public boolean setRestoAppManager(RestoAppManager aRestoAppManager)
  {
    boolean wasSet = false;
    if (aRestoAppManager == null)
    {
      return wasSet;
    }

    RestoAppManager existingRestoAppManager = restoAppManager;
    restoAppManager = aRestoAppManager;
    if (existingRestoAppManager != null && !existingRestoAppManager.equals(aRestoAppManager))
    {
      existingRestoAppManager.removeOrder(this);
    }
    restoAppManager.addOrder(this);
    wasSet = true;
    return wasSet;
  }

  public boolean setBill(Bill aBill)
  {
    //
    // This source of this source generation is association_SetOptionalOneToMandatoryMany.jet
    // This set file assumes the generation of a maximumNumberOfXXX method does not exist because 
    // it's not required (No upper bound)
    //   
    boolean wasSet = false;
    Bill existingBill = bill;

    if (existingBill == null)
    {
      if (aBill != null)
      {
        if (aBill.addOrder(this))
        {
          existingBill = aBill;
          wasSet = true;
        }
      }
    } 
    else if (existingBill != null)
    {
      if (aBill == null)
      {
        if (existingBill.minimumNumberOfOrders() < existingBill.numberOfOrders())
        {
          existingBill.removeOrder(this);
          existingBill = aBill;  // aBill == null
          wasSet = true;
        }
      } 
      else
      {
        if (existingBill.minimumNumberOfOrders() < existingBill.numberOfOrders())
        {
          existingBill.removeOrder(this);
          aBill.addOrder(this);
          existingBill = aBill;
          wasSet = true;
        }
      }
    }
    if (wasSet)
    {
      bill = existingBill;
    }
    return wasSet;
  }
  
  public boolean setSeat(Seat aNewSeat)
  {
    boolean wasSet = false;
    if (aNewSeat == null)
    {
      //Unable to setSeat to null, as order must always be associated to a seat
      return wasSet;
    }
    
    Order existingOrder = aNewSeat.getOrder();
    if (existingOrder != null && !equals(existingOrder))
    {
      //Unable to setSeat, the current seat already has a order, which would be orphaned if it were re-assigned
      return wasSet;
    }
    
    Seat anOldSeat = seat;
    seat = aNewSeat;
    seat.setOrder(this);

    if (anOldSeat != null)
    {
      anOldSeat.setOrder(null);
    }
    wasSet = true;
    return wasSet;
  }

  public boolean setMenu(Menu aMenu)
  {
    boolean wasSet = false;
    if (aMenu == null)
    {
      return wasSet;
    }

    Menu existingMenu = menu;
    menu = aMenu;
    if (existingMenu != null && !existingMenu.equals(aMenu))
    {
      existingMenu.removeOrder(this);
    }
    menu.addOrder(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    RestoAppManager placeholderRestoAppManager = restoAppManager;
    this.restoAppManager = null;
    if(placeholderRestoAppManager != null)
    {
      placeholderRestoAppManager.removeOrder(this);
    }
    if (bill != null)
    {
      if (bill.numberOfOrders() <= 1)
      {
        bill.delete();
      }
      else
      {
        Bill placeholderBill = bill;
        this.bill = null;
        placeholderBill.removeOrder(this);
      }
    }
    Seat existingSeat = seat;
    seat = null;
    if (existingSeat != null)
    {
      existingSeat.setOrder(null);
    }
    Menu placeholderMenu = menu;
    this.menu = null;
    if(placeholderMenu != null)
    {
      placeholderMenu.removeOrder(this);
    }
  }

}