/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.27.0.3728.d139ed893 modeling language!*/

package ca.mcgill.ecse223.resto.model;

// line 43 "../../../../../RestoApp.ump"
public class AvailabilityStatus
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //AvailabilityStatus Associations
  private Table table;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public AvailabilityStatus(Table aTable)
  {
    if (aTable == null || aTable.getAvailabilityStatus() != null)
    {
      throw new RuntimeException("Unable to create AvailabilityStatus due to aTable");
    }
    table = aTable;
  }

  public AvailabilityStatus(int aMaxNumSeatsForTable, RestoAppManager aRestoAppManagerForTable)
  {
    table = new Table(aMaxNumSeatsForTable, aRestoAppManagerForTable, this);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public Table getTable()
  {
    return table;
  }

  public void delete()
  {
    Table existingTable = table;
    table = null;
    if (existingTable != null)
    {
      existingTable.delete();
    }
  }

}