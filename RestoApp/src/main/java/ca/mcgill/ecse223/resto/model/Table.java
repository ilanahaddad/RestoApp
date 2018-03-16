/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.27.0.3728.d139ed893 modeling language!*/

package ca.mcgill.ecse223.resto.model;
import java.io.Serializable;
import ca.mcgill.ecse223.resto.controller.RestoController;
import ca.mcgill.ecse223.resto.controller.InvalidInputException;
import java.util.*;

// line 13 "../../../../../RestoAppPersistence.ump"
// line 3 "../../../../../RestoAppStates.ump"
// line 26 "../../../../../RestoApp.ump"
public class Table implements Serializable
{

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static Map<Integer, Table> tablesByNumber = new HashMap<Integer, Table>();

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Table Attributes
  private int number;
  private int x;
  private int y;
  private int width;
  private int length;

  //Table State Machines
  public enum States { Available, InUse }
  public enum StatesInUse { Null, ReadyToOrder, IssuedBill }
  private States states;
  private StatesInUse statesInUse;

  //Table Associations
  private List<Seat> seats;
  private List<Seat> currentSeats;
  private RestoApp restoApp;
  private List<Reservation> reservations;
  private List<Order> orders;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Table(int aNumber, int aX, int aY, int aWidth, int aLength, RestoApp aRestoApp)
  {
    x = aX;
    y = aY;
    width = aWidth;
    length = aLength;
    if (!setNumber(aNumber))
    {
      throw new RuntimeException("Cannot create due to duplicate number");
    }
    seats = new ArrayList<Seat>();
    currentSeats = new ArrayList<Seat>();
    boolean didAddRestoApp = setRestoApp(aRestoApp);
    if (!didAddRestoApp)
    {
      throw new RuntimeException("Unable to create table due to restoApp");
    }
    reservations = new ArrayList<Reservation>();
    orders = new ArrayList<Order>();
    setStatesInUse(StatesInUse.Null);
    setStates(States.Available);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setNumber(int aNumber)
  {
    boolean wasSet = false;
    Integer anOldNumber = getNumber();
    if (hasWithNumber(aNumber)) {
      return wasSet;
    }
    number = aNumber;
    wasSet = true;
    if (anOldNumber != null) {
      tablesByNumber.remove(anOldNumber);
    }
    tablesByNumber.put(aNumber, this);
    return wasSet;
  }

  public boolean setX(int aX)
  {
    boolean wasSet = false;
    x = aX;
    wasSet = true;
    return wasSet;
  }

  public boolean setY(int aY)
  {
    boolean wasSet = false;
    y = aY;
    wasSet = true;
    return wasSet;
  }

  public boolean setWidth(int aWidth)
  {
    boolean wasSet = false;
    width = aWidth;
    wasSet = true;
    return wasSet;
  }

  public boolean setLength(int aLength)
  {
    boolean wasSet = false;
    length = aLength;
    wasSet = true;
    return wasSet;
  }

  public int getNumber()
  {
    return number;
  }

  public static Table getWithNumber(int aNumber)
  {
    return tablesByNumber.get(aNumber);
  }

  public static boolean hasWithNumber(int aNumber)
  {
    return getWithNumber(aNumber) != null;
  }

  public int getX()
  {
    return x;
  }

  public int getY()
  {
    return y;
  }

  public int getWidth()
  {
    return width;
  }

  public int getLength()
  {
    return length;
  }

  public String getStatesFullName()
  {
    String answer = states.toString();
    if (statesInUse != StatesInUse.Null) { answer += "." + statesInUse.toString(); }
    return answer;
  }

  public States getStates()
  {
    return states;
  }

  public StatesInUse getStatesInUse()
  {
    return statesInUse;
  }

  public boolean createReservation(Table table,int numSeatsRequested,Date resTime,String contactName,String contactEmail,String contactPhone)
  {
    boolean wasEventProcessed = false;
    
    States aStates = states;
    switch (aStates)
    {
      case Available:
        if (table.getCurrentSeats().size()>=numSeatsRequested)
        {
        // line 12 "../../../../../RestoAppStates.ump"
          List<Reservation> allRes = table.getReservations();
    			for(Reservation res: allRes) {
    				if(res.getDateTime()==resTime) {
    					return false;
    				}
    			}
          setStates(States.Available);
          wasEventProcessed = true;
          break;
        }
        break;
      case InUse:
        if (table.getCurrentSeats().size()>=numSeatsRequested)
        {
          exitStates();
        // line 51 "../../../../../RestoAppStates.ump"
          List<Reservation> allRes = table.getReservations();
    			for(Reservation res: allRes) {
    				if(res.getDateTime()==resTime) {
    					return false;
    				}
    			}
          setStates(States.InUse);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean cancelReservation(int resNum)
  {
    boolean wasEventProcessed = false;
    
    States aStates = states;
    switch (aStates)
    {
      case Available:
        setStates(States.Available);
        wasEventProcessed = true;
        break;
      case InUse:
        exitStates();
        setStates(States.InUse);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean createOrder(Table table,int numSeatsNeeded)
  {
    boolean wasEventProcessed = false;
    
    States aStates = states;
    switch (aStates)
    {
      case Available:
        if (table.getCurrentSeats().size()>=numSeatsNeeded)
        {
          setStates(States.InUse);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean addToOrder(Seat seat,OrderItem itemToOrder)
  {
    boolean wasEventProcessed = false;
    
    StatesInUse aStatesInUse = statesInUse;
    switch (aStatesInUse)
    {
      case ReadyToOrder:
        exitStatesInUse();
        // line 28 "../../../../../RestoAppStates.ump"
        if(seat.getOrderItems().size()==0){ 
	     		setSeatInUse(seat);
	     	}
        setStatesInUse(StatesInUse.ReadyToOrder);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean cancelItem(Seat seat,OrderItem itemToCancel)
  {
    boolean wasEventProcessed = false;
    
    StatesInUse aStatesInUse = statesInUse;
    switch (aStatesInUse)
    {
      case ReadyToOrder:
        exitStatesInUse();
        // line 33 "../../../../../RestoAppStates.ump"
        if(seat.getOrderItems().size()==1){
	     		setSeatAvailable();
	     	}
        setStatesInUse(StatesInUse.ReadyToOrder);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean freeTableAndSeats(Table table,Order order)
  {
    boolean wasEventProcessed = false;
    
    StatesInUse aStatesInUse = statesInUse;
    switch (aStatesInUse)
    {
      case ReadyToOrder:
        if (order.getOrderItems().size()==0)
        {
          exitStates();
          setStates(States.Available);
          wasEventProcessed = true;
          break;
        }
        break;
      case IssuedBill:
        if (allItemsOrderedWereBilled())
        {
          exitStates();
          setStates(States.Available);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean createBill(Seat seat,List<OrderItem> itemsToBill)
  {
    boolean wasEventProcessed = false;
    
    StatesInUse aStatesInUse = statesInUse;
    switch (aStatesInUse)
    {
      case ReadyToOrder:
        exitStatesInUse();
        setStatesInUse(StatesInUse.IssuedBill);
        wasEventProcessed = true;
        break;
      case IssuedBill:
        exitStatesInUse();
        setStatesInUse(StatesInUse.IssuedBill);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean cancelBill(Table table,Seat seat)
  {
    boolean wasEventProcessed = false;
    
    StatesInUse aStatesInUse = statesInUse;
    switch (aStatesInUse)
    {
      case IssuedBill:
        if (tableHasMoreThanOneBillLeft(table))
        {
          exitStatesInUse();
          setStatesInUse(StatesInUse.IssuedBill);
          wasEventProcessed = true;
          break;
        }
        if (tableHasOneBillLeft(table))
        {
          exitStatesInUse();
          setStatesInUse(StatesInUse.ReadyToOrder);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean addItemToOrderAndBillSeat(Seat seat,OrderItem itemToAdd)
  {
    boolean wasEventProcessed = false;
    
    StatesInUse aStatesInUse = statesInUse;
    switch (aStatesInUse)
    {
      case IssuedBill:
        if (seat.getBills().size()==0)
        {
          exitStatesInUse();
          setStatesInUse(StatesInUse.IssuedBill);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  private void exitStates()
  {
    switch(states)
    {
      case InUse:
        exitStatesInUse();
        break;
    }
  }

  private void setStates(States aStates)
  {
    states = aStates;

    // entry actions and do activities
    switch(states)
    {
      case InUse:
        if (statesInUse == StatesInUse.Null) { setStatesInUse(StatesInUse.ReadyToOrder); }
        break;
    }
  }

  private void exitStatesInUse()
  {
    switch(statesInUse)
    {
      case ReadyToOrder:
        setStatesInUse(StatesInUse.Null);
        break;
      case IssuedBill:
        setStatesInUse(StatesInUse.Null);
        break;
    }
  }

  private void setStatesInUse(StatesInUse aStatesInUse)
  {
    statesInUse = aStatesInUse;
    if (states != States.InUse && aStatesInUse != StatesInUse.Null) { setStates(States.InUse); }
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

  public Seat getCurrentSeat(int index)
  {
    Seat aCurrentSeat = currentSeats.get(index);
    return aCurrentSeat;
  }

  /**
   * subsets seats
   */
  public List<Seat> getCurrentSeats()
  {
    List<Seat> newCurrentSeats = Collections.unmodifiableList(currentSeats);
    return newCurrentSeats;
  }

  public int numberOfCurrentSeats()
  {
    int number = currentSeats.size();
    return number;
  }

  public boolean hasCurrentSeats()
  {
    boolean has = currentSeats.size() > 0;
    return has;
  }

  public int indexOfCurrentSeat(Seat aCurrentSeat)
  {
    int index = currentSeats.indexOf(aCurrentSeat);
    return index;
  }

  public RestoApp getRestoApp()
  {
    return restoApp;
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

  public boolean isNumberOfSeatsValid()
  {
    boolean isValid = numberOfSeats() >= minimumNumberOfSeats();
    return isValid;
  }

  public static int minimumNumberOfSeats()
  {
    return 1;
  }

  public Seat addSeat()
  {
    Seat aNewSeat = new Seat(this);
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

  public static int minimumNumberOfCurrentSeats()
  {
    return 0;
  }

  public boolean addCurrentSeat(Seat aCurrentSeat)
  {
    boolean wasAdded = false;
    if (currentSeats.contains(aCurrentSeat)) { return false; }
    currentSeats.add(aCurrentSeat);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeCurrentSeat(Seat aCurrentSeat)
  {
    boolean wasRemoved = false;
    if (currentSeats.contains(aCurrentSeat))
    {
      currentSeats.remove(aCurrentSeat);
      wasRemoved = true;
    }
    return wasRemoved;
  }

  public boolean addCurrentSeatAt(Seat aCurrentSeat, int index)
  {  
    boolean wasAdded = false;
    if(addCurrentSeat(aCurrentSeat))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfCurrentSeats()) { index = numberOfCurrentSeats() - 1; }
      currentSeats.remove(aCurrentSeat);
      currentSeats.add(index, aCurrentSeat);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveCurrentSeatAt(Seat aCurrentSeat, int index)
  {
    boolean wasAdded = false;
    if(currentSeats.contains(aCurrentSeat))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfCurrentSeats()) { index = numberOfCurrentSeats() - 1; }
      currentSeats.remove(aCurrentSeat);
      currentSeats.add(index, aCurrentSeat);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addCurrentSeatAt(aCurrentSeat, index);
    }
    return wasAdded;
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
      existingRestoApp.removeTable(this);
    }
    restoApp.addTable(this);
    wasSet = true;
    return wasSet;
  }

  public static int minimumNumberOfReservations()
  {
    return 0;
  }

  public boolean addReservation(Reservation aReservation)
  {
    boolean wasAdded = false;
    if (reservations.contains(aReservation)) { return false; }
    reservations.add(aReservation);
    if (aReservation.indexOfTable(this) != -1)
    {
      wasAdded = true;
    }
    else
    {
      wasAdded = aReservation.addTable(this);
      if (!wasAdded)
      {
        reservations.remove(aReservation);
      }
    }
    return wasAdded;
  }

  public boolean removeReservation(Reservation aReservation)
  {
    boolean wasRemoved = false;
    if (!reservations.contains(aReservation))
    {
      return wasRemoved;
    }

    int oldIndex = reservations.indexOf(aReservation);
    reservations.remove(oldIndex);
    if (aReservation.indexOfTable(this) == -1)
    {
      wasRemoved = true;
    }
    else
    {
      wasRemoved = aReservation.removeTable(this);
      if (!wasRemoved)
      {
        reservations.add(oldIndex,aReservation);
      }
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

  public static int minimumNumberOfOrders()
  {
    return 0;
  }

  public boolean addOrder(Order aOrder)
  {
    boolean wasAdded = false;
    if (orders.contains(aOrder)) { return false; }
    orders.add(aOrder);
    if (aOrder.indexOfTable(this) != -1)
    {
      wasAdded = true;
    }
    else
    {
      wasAdded = aOrder.addTable(this);
      if (!wasAdded)
      {
        orders.remove(aOrder);
      }
    }
    return wasAdded;
  }

  public boolean removeOrder(Order aOrder)
  {
    boolean wasRemoved = false;
    if (!orders.contains(aOrder))
    {
      return wasRemoved;
    }

    int oldIndex = orders.indexOf(aOrder);
    orders.remove(oldIndex);
    if (aOrder.indexOfTable(this) == -1)
    {
      wasRemoved = true;
    }
    else
    {
      wasRemoved = aOrder.removeTable(this);
      if (!wasRemoved)
      {
        orders.add(oldIndex,aOrder);
      }
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

  public void delete()
  {
    tablesByNumber.remove(getNumber());
    while (seats.size() > 0)
    {
      Seat aSeat = seats.get(seats.size() - 1);
      aSeat.delete();
      seats.remove(aSeat);
    }
    
    currentSeats.clear();
    RestoApp placeholderRestoApp = restoApp;
    this.restoApp = null;
    if(placeholderRestoApp != null)
    {
      placeholderRestoApp.removeTable(this);
    }
    ArrayList<Reservation> copyOfReservations = new ArrayList<Reservation>(reservations);
    reservations.clear();
    for(Reservation aReservation : copyOfReservations)
    {
      if (aReservation.numberOfTables() <= Reservation.minimumNumberOfTables())
      {
        aReservation.delete();
      }
      else
      {
        aReservation.removeTable(this);
      }
    }
    ArrayList<Order> copyOfOrders = new ArrayList<Order>(orders);
    orders.clear();
    for(Order aOrder : copyOfOrders)
    {
      if (aOrder.numberOfTables() <= Order.minimumNumberOfTables())
      {
        aOrder.delete();
      }
      else
      {
        aOrder.removeTable(this);
      }
    }
  }

  // line 19 "../../../../../RestoAppPersistence.ump"
   public static  void reinitializeUniqueNumber(List<Table> tables){
    tablesByNumber = new HashMap<Integer, Table>();
	    for (Table table : tables) {
	      tablesByNumber.put(table.getNumber(), table);
	    }
  }

  // line 66 "../../../../../RestoAppStates.ump"
   private boolean tableHasMoreThanOneBillLeft(Table table){
    List<Seat> currSeats = table.getCurrentSeats();
	int numBillsForTable = 0;
	for(Seat s: currSeats) { //for every current seat at that table
		if(s.getBills().size() >0) { 
			numBillsForTable ++; //add up bills per seat if any
		}
	}
	return (numBillsForTable > 1);
  }

  // line 76 "../../../../../RestoAppStates.ump"
   private boolean tableHasOneBillLeft(Table table){
    List<Seat> currSeats = table.getCurrentSeats();
	int numBillsForTable = 0;
	for(Seat s: currSeats) { //for every current seat at that table
		if(s.getBills().size() >0) { 
			numBillsForTable ++; //add up bills per seat if any
		}
	}
	return (numBillsForTable == 1);
  }

  // line 86 "../../../../../RestoAppStates.ump"
   private boolean allItemsOrderedWereBilled(){
    //TODO: implement this in iteration 5
    return false;
  }

  // line 90 "../../../../../RestoAppStates.ump"
   private boolean setSeatInUse(Seat seat){
    //TODO: implement this in iteration 5
  	return false;
  }

  // line 94 "../../../../../RestoAppStates.ump"
   private boolean setSeatAvailable(){
    //TODO: implement this in iteration 5
  	return false;
  }


  public String toString()
  {
    return super.toString() + "["+
            "number" + ":" + getNumber()+ "," +
            "x" + ":" + getX()+ "," +
            "y" + ":" + getY()+ "," +
            "width" + ":" + getWidth()+ "," +
            "length" + ":" + getLength()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "restoApp = "+(getRestoApp()!=null?Integer.toHexString(System.identityHashCode(getRestoApp())):"null");
  }  
  //------------------------
  // DEVELOPER CODE - PROVIDED AS-IS
  //------------------------
  
  // line 16 "../../../../../RestoAppPersistence.ump"
  private static final long serialVersionUID = 8896099581655989380L ;

  
}