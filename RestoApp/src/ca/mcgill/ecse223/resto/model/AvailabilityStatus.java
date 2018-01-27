/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.27.0.3758.c600698 modeling language!*/



// line 42 "model.ump"
// line 140 "model.ump"
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
  /* Code from template association_GetOne */
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