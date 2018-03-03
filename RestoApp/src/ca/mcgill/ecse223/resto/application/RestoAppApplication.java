package ca.mcgill.ecse223.resto.application;

import ca.mcgill.ecse223.resto.model.RestoApp;
import ca.mcgill.ecse223.resto.persistence.PersistenceObjectStream;
import ca.mcgill.ecse223.resto.view.RestoAppPage;

import java.io.Serializable;

public class RestoAppApplication
{

	private static RestoApp restoApp;
	private static String filename = "data.resto";
	
	/**
	* @param args
	*/
	public static void main(String[] args) 
	{
		System.out.println("sdf");
		// TODO start UI
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() { new RestoAppPage().setVisible(true); }
		});
	}
	
	public static RestoApp getRestoApp() 
	{
		if (restoApp == null) 
		{
			// load model
			restoApp = load();
		}
		return restoApp;
	}
	
	public static void save() { PersistenceObjectStream.serialize(restoApp); }
	
	public static RestoApp load() 
	{
		PersistenceObjectStream.setFilename(filename);

		restoApp = (RestoApp) PersistenceObjectStream.deserialize();
		if (restoApp == null) { restoApp = new RestoApp(); }
		// else { restoApp.reinitialize(); } //TODO create reinitialize from umple
		
		return restoApp;
	}
	
	public static void setFilename(String newFilename) { filename = newFilename; }
	
}
