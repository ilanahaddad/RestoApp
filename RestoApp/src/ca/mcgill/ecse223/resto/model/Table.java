/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.27.0.3728.d139ed893 modeling language!*/

package ca.mcgill.ecse223.resto.model;
import java.util.*;

// line 37 "../../../../../RestoApp.ump"
public class Table
{

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static int nextId = 1;

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Table Attributes
  private int maxNumSeats;

  //Autounique Attributes
  private int id;

  //Table Associations
  private RestoAppManager restoAppManager;
  private List<Seat> seats;
  private Reservation reservation;
  private AvailabilityStatus availabilityStatus;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Table(int aMaxNumSeats, RestoAppManager aRestoAppManager, AvailabilityStatus aAvailabilityStatus)
  {
    maxNumSeats = aMaxNumSeats;
    id = nextId++;
    boolean didAddRestoAppManager = setRestoAppManager(aRestoAppManager);
    if (!didAddRestoAppManager)
    {
      throw new RuntimeException("Unable to create table due to restoAppManager");
    }
    seats = new ArrayList<Seat>();
    if (aAvailabilityStatus == null || aAvailabilityStatus.getTable() != null)
    {
      throw new RuntimeException("Unable to create Table due to aAvailabilityStatus");
    }
    availabilityStatus = aAvailabilityStatus;
  }

  public Table(int aMaxNumSeats, RestoAppManager aRestoAppManager)
  {
    maxNumSeats = aMaxNumSeats;
    id = nextId++;
    boolean didAddRestoAppManager = setRestoAppManager(aRestoAppManager);
    if (!didAddRestoAppManager)
    {
      throw new RuntimeException("Unable to create table due to restoAppManager");
    }
    seats = new ArrayList<Seat>();
    availabilityStatus = new AvailabilityStatus(this);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setMaxNumSeats(int aMaxNumSeats)
  {
    boolean wasSet = false;
    maxNumSeats = aMaxNumSeats;
    wasSet = true;
    return wasSet;
  }

  public int getMaxNumSeats()
  {
    return maxNumSeats;
  }

  public int getId()
  {
    return id;
  }

  public RestoAppManager getRestoAppManager()
  {
    return restoAppManager;
  }

  public Seat getSeat(int index)
  {
    Seat aSeat = seats.get(index);
    return aSeat;
  }

  public List<Seat> getSeats()
  {
    List<Seat> newSeats = Collections.unmodifiableList(seats);
    return newSeats;
  }

  public int numberOfSeats()
  {
    int number = seats.size();
    return number;
  }

  public boolean hasSeats()
  {
    boolean has = seats.size() > 0;
    return has;
  }

  public int indexOfSeat(Seat aSeat)
  {
    int index = seats.indexOf(aSeat);
    return index;
  }

  public Reservation getReservation()
  {
    return reservation;
  }

  public boolean hasReservation()
  {
    boolean has = reservation != null;
    return has;
  }

  public AvailabilityStatus getAvailabilityStatus()
  {
    return availabilityStatus;
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
      existingRestoAppManager.removeTable(this);
    }
    restoAppManager.addTable(this);
    wasSet = true;
    return wasSet;
  }

  public boolean isNumberOfSeatsValid()
  {
    boolean isValid = numberOfSeats() >= minimumNumberOfSeats();
    return isValid;
  }

  public static int minimumNumberOfSeats()
  {
    return 1;
  }

  public Seat addSeat(boolean aIsOccupied)
  {
    Seat aNewSeat = new Seat(aIsOccupied, this);
    return aNewSeat;
  }

  public boolean addSeat(Seat aSeat)
  {
    boolean wasAdded = false;
    if (seats.contains(aSeat)) { return false; }
    Table existingTable = aSeat.getTable();
    boolean isNewTable = existingTable != null && !this.equals(existingTable);

    if (isNewTable && existingTable.numberOfSeats() <= minimumNumberOfSeats())
    {
      return wasAdded;
    }
    if (isNewTable)
    {
      aSeat.setTable(this);
    }
    else
    {
      seats.add(aSeat);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeSeat(Seat aSeat)
  {
    boolean wasRemoved = false;
    //Unable to remove aSeat, as it must always have a table
    if (this.equals(aSeat.getTable()))
    {
      return wasRemoved;
    }

    //table already at minimum (1)
    if (numberOfSeats() <= minimumNumberOfSeats())
    {
      return wasRemoved;
    }

    seats.remove(aSeat);
    wasRemoved = true;
    return wasRemoved;
  }

  public boolean addSeatAt(Seat aSeat, int index)
  {  
    boolean wasAdded = false;
    if(addSeat(aSeat))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfSeats()) { index = numberOfSeats() - 1; }
      seats.remove(aSeat);
      seats.add(index, aSeat);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveSeatAt(Seat aSeat, int index)
  {
    boolean wasAdded = false;
    if(seats.contains(aSeat))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfSeats()) { index = numberOfSeats() - 1; }
      seats.remove(aSeat);
      seats.add(index, aSeat);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addSeatAt(aSeat, index);
    }
    return wasAdded;
  }

  public boolean setReservation(Reservation aReservation)
  {
    //
    // This source of this source generation is association_SetOptionalOneToMandatoryMany.jet
    // This set file assumes the generation of a maximumNumberOfXXX method does not exist because 
    // it's not required (No upper bound)
    //   
    boolean wasSet = false;
    Reservation existingReservation = reservation;

    if (existingReservation == null)
    {
      if (aReservation != null)
      {
        if (aReservation.addTable(this))
        {
          existingReservation = aReservation;
          wasSet = true;
        }
      }
    } 
    else if (existingReservation != null)
    {
      if (aReservation == null)
      {
        if (existingReservation.minimumNumberOfTables() < existingReservation.numberOfTables())
        {
          existingReservation.removeTable(this);
          existingReservation = aReservation;  // aReservation == null
          wasSet = true;
        }
      } 
      else
      {
        if (existingReservation.minimumNumberOfTables() < existingReservation.numberOfTables())
        {
          existingReservation.removeTable(this);
          aReservation.addTable(this);
          existingReservation = aReservation;
          wasSet = true;
        }
      }
    }
    if (wasSet)
    {
      reservation = existingReservation;
    }
    return wasSet;
  }
  
  public void delete()
  {
    RestoAppManager placeholderRestoAppManager = restoAppManager;
    this.restoAppManager = null;
    if(placeholderRestoAppManager != null)
    {
      placeholderRestoAppManager.removeTable(this);
    }
    for(int i=seats.size(); i > 0; i--)
    {
      Seat aSeat = seats.get(i - 1);
      aSeat.delete();
    }
    if (reservation != null)
    {
      if (reservation.numberOfTables() <= 1)
      {
        reservation.delete();
      }
      else
      {
        Reservation placeholderReservation = reservation;
        this.reservation = null;
        placeholderReservation.removeTable(this);
      }
    }
    AvailabilityStatus existingAvailabilityStatus = availabilityStatus;
    availabilityStatus = null;
    if (existingAvailabilityStatus != null)
    {
      existingAvailabilityStatus.delete();
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "id" + ":" + getId()+ "," +
            "maxNumSeats" + ":" + getMaxNumSeats()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "restoAppManager = "+(getRestoAppManager()!=null?Integer.toHexString(System.identityHashCode(getRestoAppManager())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "reservation = "+(getReservation()!=null?Integer.toHexString(System.identityHashCode(getReservation())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "availabilityStatus = "+(getAvailabilityStatus()!=null?Integer.toHexString(System.identityHashCode(getAvailabilityStatus())):"null");
  }
}