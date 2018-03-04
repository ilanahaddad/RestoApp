package ca.mcgill.ecse223.resto.application;

import ca.mcgill.ecse223.resto.model.RestoApp;
import ca.mcgill.ecse223.resto.persistence.PersistenceObjectStream;
import ca.mcgill.ecse223.resto.view.RestoAppPage;


public class RestoAppApplication
{

	private static RestoApp restoApp;
	private static String filename = "data.resto";


	public static void main(String[] args)
	{
		java.awt.EventQueue.invokeLater(() -> new RestoAppPage().setVisible(true));
	}

	public static RestoApp getRestoApp()
	{
		if (restoApp == null) { restoApp = load(); }
		return restoApp;
	}

	public static void save() { PersistenceObjectStream.serialize(restoApp); }

	public static RestoApp load()
	{
		PersistenceObjectStream.setFilename(filename);

		restoApp = (RestoApp) PersistenceObjectStream.deserialize();
		if (restoApp == null) { restoApp = new RestoApp(); }

		return restoApp;
	}

	public static void setFilename(String newFilename) { filename = newFilename; }
}
