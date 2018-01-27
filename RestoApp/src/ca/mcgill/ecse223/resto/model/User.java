/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.27.0.3758.c600698 modeling language!*/



// line 72 "model.ump"
// line 170 "model.ump"
public class User
{

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static int nextId = 1;

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Autounique Attributes
  private int id;

  //User Associations
  private RestoAppManager restoAppManager;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public User(RestoAppManager aRestoAppManager)
  {
    id = nextId++;
    boolean didAddRestoAppManager = setRestoAppManager(aRestoAppManager);
    if (!didAddRestoAppManager)
    {
      throw new RuntimeException("Unable to create user due to restoAppManager");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public int getId()
  {
    return id;
  }
  /* Code from template association_GetOne */
  public RestoAppManager getRestoAppManager()
  {
    return restoAppManager;
  }
  /* Code from template association_SetOneToMany */
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
      existingRestoAppManager.removeUser(this);
    }
    restoAppManager.addUser(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    RestoAppManager placeholderRestoAppManager = restoAppManager;
    this.restoAppManager = null;
    if(placeholderRestoAppManager != null)
    {
      placeholderRestoAppManager.removeUser(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "id" + ":" + getId()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "restoAppManager = "+(getRestoAppManager()!=null?Integer.toHexString(System.identityHashCode(getRestoAppManager())):"null");
  }
}