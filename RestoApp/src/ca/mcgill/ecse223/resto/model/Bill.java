/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.27.0.3758.c600698 modeling language!*/


import java.util.*;

// line 25 "model.ump"
// line 125 "model.ump"
public class Bill
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Bill Attributes
  private float totalCosts;

  //Bill Associations
  private List<Order> orders;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Bill(float aTotalCosts, Order... allOrders)
  {
    totalCosts = aTotalCosts;
    orders = new ArrayList<Order>();
    boolean didAddOrders = setOrders(allOrders);
    if (!didAddOrders)
    {
      throw new RuntimeException("Unable to create Bill, must have at least 1 orders");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setTotalCosts(float aTotalCosts)
  {
    boolean wasSet = false;
    totalCosts = aTotalCosts;
    wasSet = true;
    return wasSet;
  }

  public float getTotalCosts()
  {
    return totalCosts;
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

  public static int minimumNumberOfOrders()
  {
    return 1;
  }
  /* Code from template association_AddMNToOptionalOne */
  public boolean addOrder(Order aOrder)
  {
    boolean wasAdded = false;
    if (orders.contains(aOrder)) { return false; }
    Bill existingBill = aOrder.getBill();
    if (existingBill != null && existingBill.numberOfOrders() <= minimumNumberOfOrders())
    {
      return wasAdded;
    }
    else if (existingBill != null)
    {
      existingBill.orders.remove(aOrder);
    }
    orders.add(aOrder);
    setBill(aOrder,this);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeOrder(Order aOrder)
  {
    boolean wasRemoved = false;
    if (orders.contains(aOrder) && numberOfOrders() > minimumNumberOfOrders())
    {
      orders.remove(aOrder);
      setBill(aOrder,null);
      wasRemoved = true;
    }
    return wasRemoved;
  }

  public boolean setOrders(Order... newOrders)
  {
    boolean wasSet = false;
    if (newOrders.length < minimumNumberOfOrders())
    {
      return wasSet;
    }

    ArrayList<Order> checkNewOrders = new ArrayList<Order>();
    HashMap<Bill,Integer> billToNewOrders = new HashMap<Bill,Integer>();
    for (Order aOrder : newOrders)
    {
      if (checkNewOrders.contains(aOrder))
      {
        return wasSet;
      }
      else if (aOrder.getBill() != null && !this.equals(aOrder.getBill()))
      {
        Bill existingBill = aOrder.getBill();
        if (!billToNewOrders.containsKey(existingBill))
        {
          billToNewOrders.put(existingBill, new Integer(existingBill.numberOfOrders()));
        }
        Integer currentCount = billToNewOrders.get(existingBill);
        int nextCount = currentCount - 1;
        if (nextCount < 1)
        {
          return wasSet;
        }
        billToNewOrders.put(existingBill, new Integer(nextCount));
      }
      checkNewOrders.add(aOrder);
    }

    orders.removeAll(checkNewOrders);

    for (Order orphan : orders)
    {
      setBill(orphan, null);
    }
    orders.clear();
    for (Order aOrder : newOrders)
    {
      if (aOrder.getBill() != null)
      {
        aOrder.getBill().orders.remove(aOrder);
      }
      setBill(aOrder, this);
      orders.add(aOrder);
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_GetPrivate */
  private void setBill(Order aOrder, Bill aBill)
  {
    try
    {
      java.lang.reflect.Field mentorField = aOrder.getClass().getDeclaredField("bill");
      mentorField.setAccessible(true);
      mentorField.set(aOrder, aBill);
    }
    catch (Exception e)
    {
      throw new RuntimeException("Issue internally setting aBill to aOrder", e);
    }
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
    for(Order aOrder : orders)
    {
      setBill(aOrder,null);
    }
    orders.clear();
  }


  public String toString()
  {
    return super.toString() + "["+
            "totalCosts" + ":" + getTotalCosts()+ "]";
  }
}