package ca.mcgill.ecse223.resto.view;

import ca.mcgill.ecse223.resto.controller.InvalidInputException;
import ca.mcgill.ecse223.resto.controller.RestoController;
import ca.mcgill.ecse223.resto.model.Menu;
import ca.mcgill.ecse223.resto.model.MenuItem;
import ca.mcgill.ecse223.resto.model.PricedMenuItem;
import ca.mcgill.ecse223.resto.model.Table;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MenuPanel extends JPanel implements ActionListener {

	private JLabel jcomp3;
	private JList appetizer;
	private JList mainDish;
	private JList dessert;
	private JList alcohol;
	private JList nonAlcohol;
	private JComboBox itemCategories;

	List<MenuItem> appetizers;
	protected List<String> aplist = new ArrayList<>();

	List<MenuItem> mains;
	protected List<String> mainlist = new ArrayList<>();

	List<MenuItem> desserts;
	protected List<String> deslist = new ArrayList<>();

	List<MenuItem> abevs;
	protected List<String> abevlist = new ArrayList<>();

	List<MenuItem> nabevs;
	protected List<String> nabevlist = new ArrayList<>();

	public MenuPanel() {
		try {

			// List containing all menu items, need to go within JList
			appetizers = RestoController.getMenuItems(ca.mcgill.ecse223.resto.model.MenuItem.ItemCategory.Appetizer);
			mains = RestoController.getMenuItems(ca.mcgill.ecse223.resto.model.MenuItem.ItemCategory.Main);
			desserts = RestoController.getMenuItems(ca.mcgill.ecse223.resto.model.MenuItem.ItemCategory.Dessert);
			abevs = RestoController.getMenuItems(ca.mcgill.ecse223.resto.model.MenuItem.ItemCategory.AlcoholicBeverage);
			nabevs = RestoController.getMenuItems(MenuItem.ItemCategory.NonAlcoholicBeverage);

		} catch (InvalidInputException e) {
			e.printStackTrace();
		}

		for (MenuItem m : appetizers) {
			aplist.add(m.getName());
		}
		for (MenuItem m : mains) {
			mainlist.add(m.getName());
		}
		for (MenuItem m : desserts) {
			deslist.add(m.getName());
		}
		for (MenuItem m : abevs) {
			abevlist.add(m.getName());
		}
		for (MenuItem m : nabevs) {
			nabevlist.add(m.getName());
		}




		List<Double> appPrices = new ArrayList<>();
		for(MenuItem m : appetizers) {
			appPrices.add(m.getCurrentPricedMenuItem().getPrice());
		}

		List<Double> mainPices = new ArrayList<>();
        for(MenuItem m : mains) {
            mainPices.add(m.getCurrentPricedMenuItem().getPrice());
        }

        List<Double> desertPrices = new ArrayList<>();
        for(MenuItem m : desserts) {
            desertPrices.add(m.getCurrentPricedMenuItem().getPrice());
        }

        List<Double> abevPrices = new ArrayList<>();
        for(MenuItem m : abevs) {
            abevPrices.add(m.getCurrentPricedMenuItem().getPrice());
        }

        List<Double> nabevPrices = new ArrayList<>();
        for(MenuItem m : nabevs) {
            nabevPrices.add(m.getCurrentPricedMenuItem().getPrice());
        }

        String[] itemCategoriesItems = { "Appetizer", "Main", "Dessert", "AlcoholicBeverage", "NonAlcoholicBeverage" };

        String[] appetizerItems = aplist.stream().toArray(String[]::new);
        String[] mainDishItems = mainlist.stream().toArray(String[]::new);
        String[] dessertItems = deslist.stream().toArray(String[]::new);
        String[] alcoholItems = abevlist.stream().toArray(String[]::new);
        String[] nonAlcoholItems = nabevlist.stream().toArray(String[]::new);

        List<String> a = Arrays.asList(appetizerItems);
        for(int i = 0; i < a.size(); i++){
			a.set(i,a.get(i) + " $" +  Double.toString(appPrices.get(i)));
		}
		List<String> m = Arrays.asList(mainDishItems);
		for(int i = 0; i < m.size(); i++){
			m.set(i,m.get(i) + " $" +  Double.toString(mainPices.get(i)));
		}
		List<String> d = Arrays.asList(dessertItems);
		for(int i = 0; i < d.size(); i++){
			d.set(i,d.get(i) + " $" +  Double.toString(desertPrices.get(i)));
		}
		List<String> ai = Arrays.asList(alcoholItems);
		for(int i = 0; i < ai.size(); i++){
			ai.set(i,ai.get(i) + " $" +  Double.toString(abevPrices.get(i)));
		}
		List<String> nai = Arrays.asList(nonAlcoholItems);
		for(int i = 0; i < nai.size(); i++){
			nai.set(i,nai.get(i) + " $" +  Double.toString(nabevPrices.get(i)));
		}

		// Components constructors

		jcomp3 = new JLabel("Menu Categories:");

		itemCategories = new JComboBox(itemCategoriesItems);
		appetizer = new JList(a.toArray());
		mainDish = new JList(m.toArray());
		dessert = new JList(d.toArray());
		alcohol = new JList(ai.toArray());
		nonAlcohol = new JList(nai.toArray());

		// Adjust size
		setPreferredSize(new Dimension(515, 330));
		setLayout(null);

		// Add components
		add(jcomp3);
		add(itemCategories);

		add(appetizer);
		add(mainDish);
		add(dessert);
		add(alcohol);
		add(nonAlcohol);

		// Set Component Size and Location

		jcomp3.setBounds(30, 80, 150, 30);

		itemCategories.setBounds(30, 120, 145, 30);
		appetizer.setBounds(215, 35, 260, 250);
		mainDish.setBounds(215, 35, 260, 250);
		dessert.setBounds(215, 35, 260, 250);
		alcohol.setBounds(215, 35, 260, 250);
		nonAlcohol.setBounds(215, 35, 260, 250);

		mainDish.setVisible(false);
		dessert.setVisible(false);
		alcohol.setVisible(false);
		nonAlcohol.setVisible(false);

		// Add ActionListeners
		itemCategories.addActionListener(this);

	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == itemCategories) {
			String category = (String) itemCategories.getSelectedItem();

			switch (category) {
			case "Appetizer":
				appetizer.setVisible(true);
				mainDish.setVisible(false);
				dessert.setVisible(false);
				alcohol.setVisible(false);
				nonAlcohol.setVisible(false);
				break;
			case "Main":
				mainDish.setVisible(true);
				appetizer.setVisible(false);
				dessert.setVisible(false);
				alcohol.setVisible(false);
				nonAlcohol.setVisible(false);
				break;
			case "Dessert":
				dessert.setVisible(true);
				appetizer.setVisible(false);
				mainDish.setVisible(false);
				alcohol.setVisible(false);
				nonAlcohol.setVisible(false);
				break;
			case "AlcoholicBeverage":
				alcohol.setVisible(true);
				appetizer.setVisible(false);
				mainDish.setVisible(false);
				dessert.setVisible(false);
				nonAlcohol.setVisible(false);
				break;
			case "NonAlcoholicBeverage":
				nonAlcohol.setVisible(true);
				appetizer.setVisible(false);
				mainDish.setVisible(false);
				dessert.setVisible(false);
				alcohol.setVisible(false);
				break;
			}
		}

	}
}