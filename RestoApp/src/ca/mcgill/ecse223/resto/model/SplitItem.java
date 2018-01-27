/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.27.0.3758.c600698 modeling language!*/



// line 11 "model.ump"
// line 110 "model.ump"
public class SplitItem extends Item
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //SplitItem Attributes
  private int numPeopleSharing;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public SplitItem(String aName, String aDescription, float aPrice, Menu aMenu, int aNumPeopleSharing)
  {
    super(aName, aDescription, aPrice, aMenu);
    numPeopleSharing = aNumPeopleSharing;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setNumPeopleSharing(int aNumPeopleSharing)
  {
    boolean wasSet = false;
    numPeopleSharing = aNumPeopleSharing;
    wasSet = true;
    return wasSet;
  }

  public int getNumPeopleSharing()
  {
    return numPeopleSharing;
  }

  public void delete()
  {
    super.delete();
  }


  public String toString()
  {
    return super.toString() + "["+
            "numPeopleSharing" + ":" + getNumPeopleSharing()+ "]";
  }
}