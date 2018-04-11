/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.27.0.3728.d139ed893 modeling language!*/

package ca.mcgill.ecse223.resto.model;

// line 88 "../../../../../RestoApp-v3.ump"
public class StatisticsTable
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //StatisticsTable Attributes
  private int numUsed;

  //StatisticsTable Associations
  private RestoApp restoApp;
  private Table table;
  private Statistics statistics;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public StatisticsTable(int aNumUsed, RestoApp aRestoApp, Statistics aStatistics)
  {
    numUsed = aNumUsed;
    boolean didAddRestoApp = setRestoApp(aRestoApp);
    if (!didAddRestoApp)
    {
      throw new RuntimeException("Unable to create statisticsTable due to restoApp");
    }
    boolean didAddStatistics = setStatistics(aStatistics);
    if (!didAddStatistics)
    {
      throw new RuntimeException("Unable to create statisticsTable due to statistics");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setNumUsed(int aNumUsed)
  {
    boolean wasSet = false;
    numUsed = aNumUsed;
    wasSet = true;
    return wasSet;
  }

  public int getNumUsed()
  {
    return numUsed;
  }

  public RestoApp getRestoApp()
  {
    return restoApp;
  }

  public Table getTable()
  {
    return table;
  }

  public boolean hasTable()
  {
    boolean has = table != null;
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
      existingRestoApp.removeStatisticsTable(this);
    }
    restoApp.addStatisticsTable(this);
    wasSet = true;
    return wasSet;
  }

  public boolean setTable(Table aNewTable)
  {
    boolean wasSet = false;
    if (aNewTable == null)
    {
      Table existingTable = table;
      table = null;
      
      if (existingTable != null && existingTable.getStatisticTable() != null)
      {
        existingTable.setStatisticTable(null);
      }
      wasSet = true;
      return wasSet;
    }

    Table currentTable = getTable();
    if (currentTable != null && !currentTable.equals(aNewTable))
    {
      currentTable.setStatisticTable(null);
    }

    table = aNewTable;
    StatisticsTable existingStatisticTable = aNewTable.getStatisticTable();

    if (!equals(existingStatisticTable))
    {
      aNewTable.setStatisticTable(this);
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
      existingStatistics.removeStatisticsTable(this);
    }
    statistics.addStatisticsTable(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    RestoApp placeholderRestoApp = restoApp;
    this.restoApp = null;
    if(placeholderRestoApp != null)
    {
      placeholderRestoApp.removeStatisticsTable(this);
    }
    if (table != null)
    {
      table.setStatisticTable(null);
    }
    Statistics placeholderStatistics = statistics;
    this.statistics = null;
    if(placeholderStatistics != null)
    {
      placeholderStatistics.removeStatisticsTable(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "numUsed" + ":" + getNumUsed()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "restoApp = "+(getRestoApp()!=null?Integer.toHexString(System.identityHashCode(getRestoApp())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "table = "+(getTable()!=null?Integer.toHexString(System.identityHashCode(getTable())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "statistics = "+(getStatistics()!=null?Integer.toHexString(System.identityHashCode(getStatistics())):"null");
  }
}