/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.27.0.3728.d139ed893 modeling language!*/

package ca.mcgill.ecse223.resto.model;

// line 50 "../../../../../RestoApp.ump"
public class InUseTable extends AvailabilityStatus
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //InUseTable Attributes
  private int numberOfSeatsUsed;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public InUseTable(Table aTable, int aNumberOfSeatsUsed)
  {
    super(aTable);
    numberOfSeatsUsed = aNumberOfSeatsUsed;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setNumberOfSeatsUsed(int aNumberOfSeatsUsed)
  {
    boolean wasSet = false;
    numberOfSeatsUsed = aNumberOfSeatsUsed;
    wasSet = true;
    return wasSet;
  }

  public int getNumberOfSeatsUsed()
  {
    return numberOfSeatsUsed;
  }

  public void delete()
  {
    super.delete();
  }


  public String toString()
  {
    return super.toString() + "["+
            "numberOfSeatsUsed" + ":" + getNumberOfSeatsUsed()+ "]";
  }
}