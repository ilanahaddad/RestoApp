/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.27.0.3728.d139ed893 modeling language!*/

package ca.mcgill.ecse223.resto.model;
import java.util.*;

// line 82 "../../../../../RestoApp-v3.ump"
public class Statistics
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Statistics Associations
  private List<StatisticsTable> statisticsTables;
  private List<StatisticsItem> statisticsItems;
  private RestoApp restoApp;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Statistics(RestoApp aRestoApp)
  {
    statisticsTables = new ArrayList<StatisticsTable>();
    statisticsItems = new ArrayList<StatisticsItem>();
    if (aRestoApp == null || aRestoApp.getStatistics() != null)
    {
      throw new RuntimeException("Unable to create Statistics due to aRestoApp");
    }
    restoApp = aRestoApp;
  }

  public Statistics(Menu aMenuForRestoApp)
  {
    statisticsTables = new ArrayList<StatisticsTable>();
    statisticsItems = new ArrayList<StatisticsItem>();
    restoApp = new RestoApp(aMenuForRestoApp, this);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public StatisticsTable getStatisticsTable(int index)
  {
    StatisticsTable aStatisticsTable = statisticsTables.get(index);
    return aStatisticsTable;
  }

  public List<StatisticsTable> getStatisticsTables()
  {
    List<StatisticsTable> newStatisticsTables = Collections.unmodifiableList(statisticsTables);
    return newStatisticsTables;
  }

  public int numberOfStatisticsTables()
  {
    int number = statisticsTables.size();
    return number;
  }

  public boolean hasStatisticsTables()
  {
    boolean has = statisticsTables.size() > 0;
    return has;
  }

  public int indexOfStatisticsTable(StatisticsTable aStatisticsTable)
  {
    int index = statisticsTables.indexOf(aStatisticsTable);
    return index;
  }

  public StatisticsItem getStatisticsItem(int index)
  {
    StatisticsItem aStatisticsItem = statisticsItems.get(index);
    return aStatisticsItem;
  }

  public List<StatisticsItem> getStatisticsItems()
  {
    List<StatisticsItem> newStatisticsItems = Collections.unmodifiableList(statisticsItems);
    return newStatisticsItems;
  }

  public int numberOfStatisticsItems()
  {
    int number = statisticsItems.size();
    return number;
  }

  public boolean hasStatisticsItems()
  {
    boolean has = statisticsItems.size() > 0;
    return has;
  }

  public int indexOfStatisticsItem(StatisticsItem aStatisticsItem)
  {
    int index = statisticsItems.indexOf(aStatisticsItem);
    return index;
  }

  public RestoApp getRestoApp()
  {
    return restoApp;
  }

  public static int minimumNumberOfStatisticsTables()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public StatisticsTable addStatisticsTable(int aNumUsed, RestoApp aRestoApp)
  {
    return new StatisticsTable(aNumUsed, aRestoApp, this);
  }

  public boolean addStatisticsTable(StatisticsTable aStatisticsTable)
  {
    boolean wasAdded = false;
    if (statisticsTables.contains(aStatisticsTable)) { return false; }
    Statistics existingStatistics = aStatisticsTable.getStatistics();
    boolean isNewStatistics = existingStatistics != null && !this.equals(existingStatistics);
    if (isNewStatistics)
    {
      aStatisticsTable.setStatistics(this);
    }
    else
    {
      statisticsTables.add(aStatisticsTable);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeStatisticsTable(StatisticsTable aStatisticsTable)
  {
    boolean wasRemoved = false;
    //Unable to remove aStatisticsTable, as it must always have a statistics
    if (!this.equals(aStatisticsTable.getStatistics()))
    {
      statisticsTables.remove(aStatisticsTable);
      wasRemoved = true;
    }
    return wasRemoved;
  }

  public boolean addStatisticsTableAt(StatisticsTable aStatisticsTable, int index)
  {  
    boolean wasAdded = false;
    if(addStatisticsTable(aStatisticsTable))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfStatisticsTables()) { index = numberOfStatisticsTables() - 1; }
      statisticsTables.remove(aStatisticsTable);
      statisticsTables.add(index, aStatisticsTable);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveStatisticsTableAt(StatisticsTable aStatisticsTable, int index)
  {
    boolean wasAdded = false;
    if(statisticsTables.contains(aStatisticsTable))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfStatisticsTables()) { index = numberOfStatisticsTables() - 1; }
      statisticsTables.remove(aStatisticsTable);
      statisticsTables.add(index, aStatisticsTable);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addStatisticsTableAt(aStatisticsTable, index);
    }
    return wasAdded;
  }

  public static int minimumNumberOfStatisticsItems()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public StatisticsItem addStatisticsItem(int aNumOrdered, RestoApp aRestoApp)
  {
    return new StatisticsItem(aNumOrdered, aRestoApp, this);
  }

  public boolean addStatisticsItem(StatisticsItem aStatisticsItem)
  {
    boolean wasAdded = false;
    if (statisticsItems.contains(aStatisticsItem)) { return false; }
    Statistics existingStatistics = aStatisticsItem.getStatistics();
    boolean isNewStatistics = existingStatistics != null && !this.equals(existingStatistics);
    if (isNewStatistics)
    {
      aStatisticsItem.setStatistics(this);
    }
    else
    {
      statisticsItems.add(aStatisticsItem);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeStatisticsItem(StatisticsItem aStatisticsItem)
  {
    boolean wasRemoved = false;
    //Unable to remove aStatisticsItem, as it must always have a statistics
    if (!this.equals(aStatisticsItem.getStatistics()))
    {
      statisticsItems.remove(aStatisticsItem);
      wasRemoved = true;
    }
    return wasRemoved;
  }

  public boolean addStatisticsItemAt(StatisticsItem aStatisticsItem, int index)
  {  
    boolean wasAdded = false;
    if(addStatisticsItem(aStatisticsItem))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfStatisticsItems()) { index = numberOfStatisticsItems() - 1; }
      statisticsItems.remove(aStatisticsItem);
      statisticsItems.add(index, aStatisticsItem);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveStatisticsItemAt(StatisticsItem aStatisticsItem, int index)
  {
    boolean wasAdded = false;
    if(statisticsItems.contains(aStatisticsItem))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfStatisticsItems()) { index = numberOfStatisticsItems() - 1; }
      statisticsItems.remove(aStatisticsItem);
      statisticsItems.add(index, aStatisticsItem);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addStatisticsItemAt(aStatisticsItem, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    while (statisticsTables.size() > 0)
    {
      StatisticsTable aStatisticsTable = statisticsTables.get(statisticsTables.size() - 1);
      aStatisticsTable.delete();
      statisticsTables.remove(aStatisticsTable);
    }
    
    while (statisticsItems.size() > 0)
    {
      StatisticsItem aStatisticsItem = statisticsItems.get(statisticsItems.size() - 1);
      aStatisticsItem.delete();
      statisticsItems.remove(aStatisticsItem);
    }
    
    RestoApp existingRestoApp = restoApp;
    restoApp = null;
    if (existingRestoApp != null)
    {
      existingRestoApp.delete();
    }
  }

}