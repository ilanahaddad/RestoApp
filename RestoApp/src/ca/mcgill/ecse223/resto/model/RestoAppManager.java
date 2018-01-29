/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.27.0.3728.d139ed893 modeling language!*/

package ca.mcgill.ecse223.resto.model;
import java.util.*;
import java.sql.Date;
import java.sql.Time;

// line 71 "../../../../../RestoApp.ump"
public class RestoAppManager
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //RestoAppManager Associations
  private List<Order> orders;
  private List<User> users;
  private Menu menu;
  private List<Reservation> reservations;
  private List<Table> tables;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public RestoAppManager()
  {
    orders = new ArrayList<Order>();
    users = new ArrayList<User>();
    reservations = new ArrayList<Reservation>();
    tables = new ArrayList<Table>();
  }

  //------------------------
  // INTERFACE
  //------------------------

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

  public User getUser(int index)
  {
    User aUser = users.get(index);
    return aUser;
  }

  public List<User> getUsers()
  {
    List<User> newUsers = Collections.unmodifiableList(users);
    return newUsers;
  }

  public int numberOfUsers()
  {
    int number = users.size();
    return number;
  }

  public boolean hasUsers()
  {
    boolean has = users.size() > 0;
    return has;
  }

  public int indexOfUser(User aUser)
  {
    int index = users.indexOf(aUser);
    return index;
  }

  public Menu getMenu()
  {
    return menu;
  }

  public boolean hasMenu()
  {
    boolean has = menu != null;
    return has;
  }

  public Reservation getReservation(int index)
  {
    Reservation aReservation = reservations.get(index);
    return aReservation;
  }

  public List<Reservation> getReservations()
  {
    List<Reservation> newReservations = Collections.unmodifiableList(reservations);
    return newReservations;
  }

  public int numberOfReservations()
  {
    int number = reservations.size();
    return number;
  }

  public boolean hasReservations()
  {
    boolean has = reservations.size() > 0;
    return has;
  }

  public int indexOfReservation(Reservation aReservation)
  {
    int index = reservations.indexOf(aReservation);
    return index;
  }

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

  public static int minimumNumberOfOrders()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Order addOrder(Seat aSeat, Menu aMenu)
  {
    return new Order(this, aSeat, aMenu);
  }

  public boolean addOrder(Order aOrder)
  {
    boolean wasAdded = false;
    if (orders.contains(aOrder)) { return false; }
    RestoAppManager existingRestoAppManager = aOrder.getRestoAppManager();
    boolean isNewRestoAppManager = existingRestoAppManager != null && !this.equals(existingRestoAppManager);
    if (isNewRestoAppManager)
    {
      aOrder.setRestoAppManager(this);
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
    //Unable to remove aOrder, as it must always have a restoAppManager
    if (!this.equals(aOrder.getRestoAppManager()))
    {
      orders.remove(aOrder);
      wasRemoved = true;
    }
    return wasRemoved;
  }

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

  public static int minimumNumberOfUsers()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public User addUser()
  {
    return new User(this);
  }

  public boolean addUser(User aUser)
  {
    boolean wasAdded = false;
    if (users.contains(aUser)) { return false; }
    RestoAppManager existingRestoAppManager = aUser.getRestoAppManager();
    boolean isNewRestoAppManager = existingRestoAppManager != null && !this.equals(existingRestoAppManager);
    if (isNewRestoAppManager)
    {
      aUser.setRestoAppManager(this);
    }
    else
    {
      users.add(aUser);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeUser(User aUser)
  {
    boolean wasRemoved = false;
    //Unable to remove aUser, as it must always have a restoAppManager
    if (!this.equals(aUser.getRestoAppManager()))
    {
      users.remove(aUser);
      wasRemoved = true;
    }
    return wasRemoved;
  }

  public boolean addUserAt(User aUser, int index)
  {  
    boolean wasAdded = false;
    if(addUser(aUser))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfUsers()) { index = numberOfUsers() - 1; }
      users.remove(aUser);
      users.add(index, aUser);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveUserAt(User aUser, int index)
  {
    boolean wasAdded = false;
    if(users.contains(aUser))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfUsers()) { index = numberOfUsers() - 1; }
      users.remove(aUser);
      users.add(index, aUser);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addUserAt(aUser, index);
    }
    return wasAdded;
  }

  public boolean setMenu(Menu aNewMenu)
  {
    boolean wasSet = false;
    if (aNewMenu == null)
    {
      Menu existingMenu = menu;
      menu = null;
      
      if (existingMenu != null && existingMenu.getRestoAppManager() != null)
      {
        existingMenu.setRestoAppManager(null);
      }
      wasSet = true;
      return wasSet;
    }

    Menu currentMenu = getMenu();
    if (currentMenu != null && !currentMenu.equals(aNewMenu))
    {
      currentMenu.setRestoAppManager(null);
    }

    menu = aNewMenu;
    RestoAppManager existingRestoAppManager = aNewMenu.getRestoAppManager();

    if (!equals(existingRestoAppManager))
    {
      aNewMenu.setRestoAppManager(this);
    }
    wasSet = true;
    return wasSet;
  }

  public static int minimumNumberOfReservations()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Reservation addReservation(Date aDate, Time aTime, int aNumOfPeople, String aCustomerName, String aCustomerContact, Table... allTables)
  {
    return new Reservation(aDate, aTime, aNumOfPeople, aCustomerName, aCustomerContact, this, allTables);
  }

  public boolean addReservation(Reservation aReservation)
  {
    boolean wasAdded = false;
    if (reservations.contains(aReservation)) { return false; }
    RestoAppManager existingRestoAppManager = aReservation.getRestoAppManager();
    boolean isNewRestoAppManager = existingRestoAppManager != null && !this.equals(existingRestoAppManager);
    if (isNewRestoAppManager)
    {
      aReservation.setRestoAppManager(this);
    }
    else
    {
      reservations.add(aReservation);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeReservation(Reservation aReservation)
  {
    boolean wasRemoved = false;
    //Unable to remove aReservation, as it must always have a restoAppManager
    if (!this.equals(aReservation.getRestoAppManager()))
    {
      reservations.remove(aReservation);
      wasRemoved = true;
    }
    return wasRemoved;
  }

  public boolean addReservationAt(Reservation aReservation, int index)
  {  
    boolean wasAdded = false;
    if(addReservation(aReservation))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfReservations()) { index = numberOfReservations() - 1; }
      reservations.remove(aReservation);
      reservations.add(index, aReservation);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveReservationAt(Reservation aReservation, int index)
  {
    boolean wasAdded = false;
    if(reservations.contains(aReservation))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfReservations()) { index = numberOfReservations() - 1; }
      reservations.remove(aReservation);
      reservations.add(index, aReservation);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addReservationAt(aReservation, index);
    }
    return wasAdded;
  }

  public static int minimumNumberOfTables()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Table addTable(int aMaxNumSeats, AvailabilityStatus aAvailabilityStatus)
  {
    return new Table(aMaxNumSeats, this, aAvailabilityStatus);
  }

  public boolean addTable(Table aTable)
  {
    boolean wasAdded = false;
    if (tables.contains(aTable)) { return false; }
    RestoAppManager existingRestoAppManager = aTable.getRestoAppManager();
    boolean isNewRestoAppManager = existingRestoAppManager != null && !this.equals(existingRestoAppManager);
    if (isNewRestoAppManager)
    {
      aTable.setRestoAppManager(this);
    }
    else
    {
      tables.add(aTable);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeTable(Table aTable)
  {
    boolean wasRemoved = false;
    //Unable to remove aTable, as it must always have a restoAppManager
    if (!this.equals(aTable.getRestoAppManager()))
    {
      tables.remove(aTable);
      wasRemoved = true;
    }
    return wasRemoved;
  }

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
    for(int i=orders.size(); i > 0; i--)
    {
      Order aOrder = orders.get(i - 1);
      aOrder.delete();
    }
    for(int i=users.size(); i > 0; i--)
    {
      User aUser = users.get(i - 1);
      aUser.delete();
    }
    if (menu != null)
    {
      menu.setRestoAppManager(null);
    }
    for(int i=reservations.size(); i > 0; i--)
    {
      Reservation aReservation = reservations.get(i - 1);
      aReservation.delete();
    }
    for(int i=tables.size(); i > 0; i--)
    {
      Table aTable = tables.get(i - 1);
      aTable.delete();
    }
  }

}