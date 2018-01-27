/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.27.0.3758.c600698 modeling language!*/


import java.sql.Date;
import java.sql.Time;
import java.util.*;

// line 60 "model.ump"
// line 160 "model.ump"
public class Reservation
{

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static int nextId = 1;

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Reservation Attributes
  private Date date;
  private Time time;
  private int numOfPeople;
  private String customerName;
  private String customerContact;

  //Autounique Attributes
  private int id;

  //Reservation Associations
  private RestoAppManager restoAppManager;
  private List<Table> tables;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Reservation(Date aDate, Time aTime, int aNumOfPeople, String aCustomerName, String aCustomerContact, RestoAppManager aRestoAppManager, Table... allTables)
  {
    date = aDate;
    time = aTime;
    numOfPeople = aNumOfPeople;
    customerName = aCustomerName;
    customerContact = aCustomerContact;
    id = nextId++;
    boolean didAddRestoAppManager = setRestoAppManager(aRestoAppManager);
    if (!didAddRestoAppManager)
    {
      throw new RuntimeException("Unable to create reservation due to restoAppManager");
    }
    tables = new ArrayList<Table>();
    boolean didAddTables = setTables(allTables);
    if (!didAddTables)
    {
      throw new RuntimeException("Unable to create Reservation, must have at least 1 tables");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setDate(Date aDate)
  {
    boolean wasSet = false;
    date = aDate;
    wasSet = true;
    return wasSet;
  }

  public boolean setTime(Time aTime)
  {
    boolean wasSet = false;
    time = aTime;
    wasSet = true;
    return wasSet;
  }

  public boolean setNumOfPeople(int aNumOfPeople)
  {
    boolean wasSet = false;
    numOfPeople = aNumOfPeople;
    wasSet = true;
    return wasSet;
  }

  public boolean setCustomerName(String aCustomerName)
  {
    boolean wasSet = false;
    customerName = aCustomerName;
    wasSet = true;
    return wasSet;
  }

  public boolean setCustomerContact(String aCustomerContact)
  {
    boolean wasSet = false;
    customerContact = aCustomerContact;
    wasSet = true;
    return wasSet;
  }

  public Date getDate()
  {
    return date;
  }

  public Time getTime()
  {
    return time;
  }

  public int getNumOfPeople()
  {
    return numOfPeople;
  }

  public String getCustomerName()
  {
    return customerName;
  }

  public String getCustomerContact()
  {
    return customerContact;
  }

  public int getId()
  {
    return id;
  }
  /* Code from template association_GetOne */
  public RestoAppManager getRestoAppManager()
  {
    return restoAppManager;
  }
  /* Code from template association_GetMany */
  public Table getTable(int index)
  {
    Table aTable = tables.get(index);
    return aTable;
  }

  public List<Table> getTables()
  {
    List<Table> newTables = Collections.unmodifiableList(tables);
    return newTables;
  }

  public int numberOfTables()
  {
    int number = tables.size();
    return number;
  }

  public boolean hasTables()
  {
    boolean has = tables.size() > 0;
    return has;
  }

  public int indexOfTable(Table aTable)
  {
    int index = tables.indexOf(aTable);
    return index;
  }
  /* Code from template association_SetOneToMany */
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
      existingRestoAppManager.removeReservation(this);
    }
    restoAppManager.addReservation(this);
    wasSet = true;
    return wasSet;
  }

  public static int minimumNumberOfTables()
  {
    return 1;
  }
  /* Code from template association_AddMNToOptionalOne */
  public boolean addTable(Table aTable)
  {
    boolean wasAdded = false;
    if (tables.contains(aTable)) { return false; }
    Reservation existingReservation = aTable.getReservation();
    if (existingReservation != null && existingReservation.numberOfTables() <= minimumNumberOfTables())
    {
      return wasAdded;
    }
    else if (existingReservation != null)
    {
      existingReservation.tables.remove(aTable);
    }
    tables.add(aTable);
    setReservation(aTable,this);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeTable(Table aTable)
  {
    boolean wasRemoved = false;
    if (tables.contains(aTable) && numberOfTables() > minimumNumberOfTables())
    {
      tables.remove(aTable);
      setReservation(aTable,null);
      wasRemoved = true;
    }
    return wasRemoved;
  }

  public boolean setTables(Table... newTables)
  {
    boolean wasSet = false;
    if (newTables.length < minimumNumberOfTables())
    {
      return wasSet;
    }

    ArrayList<Table> checkNewTables = new ArrayList<Table>();
    HashMap<Reservation,Integer> reservationToNewTables = new HashMap<Reservation,Integer>();
    for (Table aTable : newTables)
    {
      if (checkNewTables.contains(aTable))
      {
        return wasSet;
      }
      else if (aTable.getReservation() != null && !this.equals(aTable.getReservation()))
      {
        Reservation existingReservation = aTable.getReservation();
        if (!reservationToNewTables.containsKey(existingReservation))
        {
          reservationToNewTables.put(existingReservation, new Integer(existingReservation.numberOfTables()));
        }
        Integer currentCount = reservationToNewTables.get(existingReservation);
        int nextCount = currentCount - 1;
        if (nextCount < 1)
        {
          return wasSet;
        }
        reservationToNewTables.put(existingReservation, new Integer(nextCount));
      }
      checkNewTables.add(aTable);
    }

    tables.removeAll(checkNewTables);

    for (Table orphan : tables)
    {
      setReservation(orphan, null);
    }
    tables.clear();
    for (Table aTable : newTables)
    {
      if (aTable.getReservation() != null)
      {
        aTable.getReservation().tables.remove(aTable);
      }
      setReservation(aTable, this);
      tables.add(aTable);
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_GetPrivate */
  private void setReservation(Table aTable, Reservation aReservation)
  {
    try
    {
      java.lang.reflect.Field mentorField = aTable.getClass().getDeclaredField("reservation");
      mentorField.setAccessible(true);
      mentorField.set(aTable, aReservation);
    }
    catch (Exception e)
    {
      throw new RuntimeException("Issue internally setting aReservation to aTable", e);
    }
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addTableAt(Table aTable, int index)
  {  
    boolean wasAdded = false;
    if(addTable(aTable))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfTables()) { index = numberOfTables() - 1; }
      tables.remove(aTable);
      tables.add(index, aTable);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveTableAt(Table aTable, int index)
  {
    boolean wasAdded = false;
    if(tables.contains(aTable))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfTables()) { index = numberOfTables() - 1; }
      tables.remove(aTable);
      tables.add(index, aTable);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addTableAt(aTable, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    RestoAppManager placeholderRestoAppManager = restoAppManager;
    this.restoAppManager = null;
    if(placeholderRestoAppManager != null)
    {
      placeholderRestoAppManager.removeReservation(this);
    }
    for(Table aTable : tables)
    {
      setReservation(aTable,null);
    }
    tables.clear();
  }


  public String toString()
  {
    return super.toString() + "["+
            "id" + ":" + getId()+ "," +
            "numOfPeople" + ":" + getNumOfPeople()+ "," +
            "customerName" + ":" + getCustomerName()+ "," +
            "customerContact" + ":" + getCustomerContact()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "date" + "=" + (getDate() != null ? !getDate().equals(this)  ? getDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "time" + "=" + (getTime() != null ? !getTime().equals(this)  ? getTime().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "restoAppManager = "+(getRestoAppManager()!=null?Integer.toHexString(System.identityHashCode(getRestoAppManager())):"null");
  }
}