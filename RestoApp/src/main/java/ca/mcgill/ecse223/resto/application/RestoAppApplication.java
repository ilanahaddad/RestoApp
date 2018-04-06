package ca.mcgill.ecse223.resto.application;

import ca.mcgill.ecse223.resto.model.RestoApp;
import ca.mcgill.ecse223.resto.persistence.PersistenceObjectStream;
import ca.mcgill.ecse223.resto.view.RestoAppPage;


public class RestoAppApplication
{

	private static RestoApp restoApp;
	private static String filename = "menu.resto";


	public static void main(String[] args)
	{
		/* try {
			List<MenuItem> mi = RestoController.getMenuItems(MenuItem.ItemCategory.Appetizer);
			System.out.println(mi.size());
		} catch (InvalidInputException e) {
			e.printStackTrace();
		} */
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
		if (restoApp == null) { restoApp = new RestoApp();}
		else {
			restoApp.reinitialize();
		}

		return restoApp;
	}

	public static void setFilename(String newFilename) { filename = newFilename; }
}
