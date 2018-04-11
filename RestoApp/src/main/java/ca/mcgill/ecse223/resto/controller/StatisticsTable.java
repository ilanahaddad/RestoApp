package ca.mcgill.ecse223.resto.controller;
import ca.mcgill.ecse223.resto.model.Table;

	public class StatisticsTable
	{

	  //------------------------
	  // MEMBER VARIABLES
	  //------------------------

	  //StatisticsTable Attributes
	  private int numUsed;

	  //StatisticsTable Associations
	  private Table table;

	  //------------------------
	  // CONSTRUCTOR
	  //------------------------

	  public StatisticsTable(Table aTable, int aNumUsed)
	  {
		this.table = aTable;
	    this.numUsed = aNumUsed;
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

	  public boolean setTable(Table aTable) {
		  boolean wasSet = false;
		  table = aTable;
		  wasSet = true;
		  return wasSet;
	  }
	  
	  public Table getTable()
	  {
	    return table;
	  }

	  public boolean hasTable()
	  {
	    boolean has = table != null;
	    return has;
	  }

}
