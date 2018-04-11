package ca.mcgill.ecse223.resto.controller;
import ca.mcgill.ecse223.resto.model.MenuItem;

public class StatisticsItem
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //StatisticsTable Attributes
  private int numUsed;

  //StatisticsTable Associations
  private MenuItem item;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public StatisticsItem(MenuItem anItem, int aNumUsed)
  {
	item = anItem;
    numUsed = aNumUsed;
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

  public boolean setItem(MenuItem anItem) {
	  boolean wasSet = false;
	  item = anItem;
	  wasSet = true;
	  return wasSet;
  }
  
  public MenuItem getItem()
  {
    return item;
  }

  public boolean hasMenuItem()
  {
    boolean has = item != null;
    return has;
  }

}
