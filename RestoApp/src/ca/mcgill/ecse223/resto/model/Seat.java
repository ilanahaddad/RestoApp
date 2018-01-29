/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.27.0.3728.d139ed893 modeling language!*/

package ca.mcgill.ecse223.resto.model;

// line 31 "../../../../../RestoApp.ump"
public class Seat
{

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static int nextId = 1;

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Seat Attributes
  private boolean isOccupied;

  //Autounique Attributes
  private int id;

  //Seat Associations
  private Order order;
  private Table table;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Seat(boolean aIsOccupied, Table aTable)
  {
    isOccupied = aIsOccupied;
    id = nextId++;
    boolean didAddTable = setTable(aTable);
    if (!didAddTable)
    {
      throw new RuntimeException("Unable to create seat due to table");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setIsOccupied(boolean aIsOccupied)
  {
    boolean wasSet = false;
    isOccupied = aIsOccupied;
    wasSet = true;
    return wasSet;
  }

  public boolean getIsOccupied()
  {
    return isOccupied;
  }

  public int getId()
  {
    return id;
  }

  public boolean isIsOccupied()
  {
    return isOccupied;
  }

  public Order getOrder()
  {
    return order;
  }

  public boolean hasOrder()
  {
    boolean has = order != null;
    return has;
  }

  public Table getTable()
  {
    return table;
  }

  public boolean setOrder(Order aNewOrder)
  {
    boolean wasSet = false;
    if (order != null && !order.equals(aNewOrder) && equals(order.getSeat()))
    {
      //Unable to setOrder, as existing order would become an orphan
      return wasSet;
    }

    order = aNewOrder;
    Seat anOldSeat = aNewOrder != null ? aNewOrder.getSeat() : null;

    if (!this.equals(anOldSeat))
    {
      if (anOldSeat != null)
      {
        anOldSeat.order = null;
      }
      if (order != null)
      {
        order.setSeat(this);
      }
    }
    wasSet = true;
    return wasSet;
  }

  public boolean setTable(Table aTable)
  {
    boolean wasSet = false;
    //Must provide table to seat
    if (aTable == null)
    {
      return wasSet;
    }

    if (table != null && table.numberOfSeats() <= Table.minimumNumberOfSeats())
    {
      return wasSet;
    }

    Table existingTable = table;
    table = aTable;
    if (existingTable != null && !existingTable.equals(aTable))
    {
      boolean didRemove = existingTable.removeSeat(this);
      if (!didRemove)
      {
        table = existingTable;
        return wasSet;
      }
    }
    table.addSeat(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    Order existingOrder = order;
    order = null;
    if (existingOrder != null)
    {
      existingOrder.delete();
    }
    Table placeholderTable = table;
    this.table = null;
    if(placeholderTable != null)
    {
      placeholderTable.removeSeat(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "id" + ":" + getId()+ "," +
            "isOccupied" + ":" + getIsOccupied()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "order = "+(getOrder()!=null?Integer.toHexString(System.identityHashCode(getOrder())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "table = "+(getTable()!=null?Integer.toHexString(System.identityHashCode(getTable())):"null");
  }
}