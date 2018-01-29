/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.27.0.3728.d139ed893 modeling language!*/

package ca.mcgill.ecse223.resto.model;

// line 12 "../../../../../RestoApp.ump"
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

  public SplitItem(String aName, float aPrice, Menu aMenu, int aNumPeopleSharing)
  {
    super(aName, aPrice, aMenu);
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