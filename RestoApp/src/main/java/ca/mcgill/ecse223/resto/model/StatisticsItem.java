/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.27.0.3728.d139ed893 modeling language!*/

package ca.mcgill.ecse223.resto.model;

// line 91 "../../../../../RestoApp-v3.ump"
public class StatisticsItem
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //StatisticsItem Attributes
  private int numOrdered;

  //StatisticsItem Associations
  private RestoApp restoApp;
  private OrderItem orderItem;
  private Statistics statistics;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public StatisticsItem(int aNumOrdered, RestoApp aRestoApp, Statistics aStatistics)
  {
    numOrdered = aNumOrdered;
    boolean didAddRestoApp = setRestoApp(aRestoApp);
    if (!didAddRestoApp)
    {
      throw new RuntimeException("Unable to create statisticsItem due to restoApp");
    }
    boolean didAddStatistics = setStatistics(aStatistics);
    if (!didAddStatistics)
    {
      throw new RuntimeException("Unable to create statisticsItem due to statistics");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setNumOrdered(int aNumOrdered)
  {
    boolean wasSet = false;
    numOrdered = aNumOrdered;
    wasSet = true;
    return wasSet;
  }

  public int getNumOrdered()
  {
    return numOrdered;
  }

  public RestoApp getRestoApp()
  {
    return restoApp;
  }

  public OrderItem getOrderItem()
  {
    return orderItem;
  }

  public boolean hasOrderItem()
  {
    boolean has = orderItem != null;
    return has;
  }

  public Statistics getStatistics()
  {
    return statistics;
  }

  public boolean setRestoApp(RestoApp aRestoApp)
  {
    boolean wasSet = false;
    if (aRestoApp == null)
    {
      return wasSet;
    }

    RestoApp existingRestoApp = restoApp;
    restoApp = aRestoApp;
    if (existingRestoApp != null && !existingRestoApp.equals(aRestoApp))
    {
      existingRestoApp.removeStatisticsItem(this);
    }
    restoApp.addStatisticsItem(this);
    wasSet = true;
    return wasSet;
  }

  public boolean setOrderItem(OrderItem aNewOrderItem)
  {
    boolean wasSet = false;
    if (aNewOrderItem == null)
    {
      OrderItem existingOrderItem = orderItem;
      orderItem = null;
      
      if (existingOrderItem != null && existingOrderItem.getStatisticItem() != null)
      {
        existingOrderItem.setStatisticItem(null);
      }
      wasSet = true;
      return wasSet;
    }

    OrderItem currentOrderItem = getOrderItem();
    if (currentOrderItem != null && !currentOrderItem.equals(aNewOrderItem))
    {
      currentOrderItem.setStatisticItem(null);
    }

    orderItem = aNewOrderItem;
    StatisticsItem existingStatisticItem = aNewOrderItem.getStatisticItem();

    if (!equals(existingStatisticItem))
    {
      aNewOrderItem.setStatisticItem(this);
    }
    wasSet = true;
    return wasSet;
  }

  public boolean setStatistics(Statistics aStatistics)
  {
    boolean wasSet = false;
    if (aStatistics == null)
    {
      return wasSet;
    }

    Statistics existingStatistics = statistics;
    statistics = aStatistics;
    if (existingStatistics != null && !existingStatistics.equals(aStatistics))
    {
      existingStatistics.removeStatisticsItem(this);
    }
    statistics.addStatisticsItem(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    RestoApp placeholderRestoApp = restoApp;
    this.restoApp = null;
    if(placeholderRestoApp != null)
    {
      placeholderRestoApp.removeStatisticsItem(this);
    }
    if (orderItem != null)
    {
      orderItem.setStatisticItem(null);
    }
    Statistics placeholderStatistics = statistics;
    this.statistics = null;
    if(placeholderStatistics != null)
    {
      placeholderStatistics.removeStatisticsItem(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "numOrdered" + ":" + getNumOrdered()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "restoApp = "+(getRestoApp()!=null?Integer.toHexString(System.identityHashCode(getRestoApp())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "orderItem = "+(getOrderItem()!=null?Integer.toHexString(System.identityHashCode(getOrderItem())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "statistics = "+(getStatistics()!=null?Integer.toHexString(System.identityHashCode(getStatistics())):"null");
  }
}