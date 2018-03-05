package ca.mcgill.ecse223.resto.view;

import ca.mcgill.ecse223.resto.controller.InvalidInputException;
import ca.mcgill.ecse223.resto.controller.RestoController;
import ca.mcgill.ecse223.resto.model.MenuItem;
import ca.mcgill.ecse223.resto.model.Table;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MenuPanel extends JPanel implements ActionListener {

    private JButton load;
    private JButton save;
    private JLabel jcomp3;
    private JList appetizer;
    private JList mainDish;
    private JList dessert;
    private JList alcohol;
    private JList nonAlcohol;
    private JComboBox itemCategories;

    List<MenuItem> appetizers;  protected List<String> aplist = new ArrayList<>();
    List<MenuItem> mains;       protected List<String> mainlist = new ArrayList<>();
    List<MenuItem> desserts;    protected List<String> deslist = new ArrayList<>();
    List<MenuItem> abevs;       protected List<String> abevlist = new ArrayList<>();
    List<MenuItem> nabevs;      protected List<String> nabevlist = new ArrayList<>();

    public MenuPanel() {
        try {
            //List containing all menu items, need to go within JList
            appetizers = RestoController.getMenuItems(ca.mcgill.ecse223.resto.model.MenuItem.ItemCategory.Appetizer);
            mains = RestoController.getMenuItems(ca.mcgill.ecse223.resto.model.MenuItem.ItemCategory.Main);
            desserts = RestoController.getMenuItems(ca.mcgill.ecse223.resto.model.MenuItem.ItemCategory.Dessert);
            abevs = RestoController.getMenuItems(ca.mcgill.ecse223.resto.model.MenuItem.ItemCategory.AlcoholicBeverage);
            nabevs = RestoController.getMenuItems(MenuItem.ItemCategory.NonAlcoholicBeverage);
        } catch (InvalidInputException e) {
            e.printStackTrace();
        }

        for(MenuItem m : appetizers){
            aplist.add(m.getName());
        }
        for(MenuItem m : mains){
            mainlist.add(m.getName());
        }
        for(MenuItem m : desserts){
            deslist.add(m.getName());
        }
        for(MenuItem m : abevs) {
            abevlist.add(m.getName());
        }
        for(MenuItem m : nabevs) {
            nabevlist.add(m.getName());
        }

        String[] itemCategoriesItems = { "Appetizer", "Main", "Dessert", "AlcoholicBeverage", "NonAlcoholicBeverage" };

        String[] appetizerItems = aplist.stream().toArray(String[]::new);// get from controller
        String[] mainDishItems = mainlist.stream().toArray(String[]::new);// get from controller
        String[] dessertItems = deslist.stream().toArray(String[]::new);;// get from controller
        String[] alcoholItems = abevlist.stream().toArray(String[]::new);;// get from controller
        String[] nonAlcoholItems = nabevlist.stream().toArray(String[]::new);;// get from controller

        // Components constructors
        load = new JButton("Load");
        save = new JButton("Save");
        jcomp3 = new JLabel("Menu Actions:");

        itemCategories = new JComboBox(itemCategoriesItems);
        appetizer = new JList(appetizerItems);
        mainDish = new JList(mainDishItems);
        dessert = new JList(dessertItems);
        alcohol = new JList(alcoholItems);
        nonAlcohol = new JList(nonAlcoholItems);

        // Adjust size
        setPreferredSize(new Dimension(513, 327));
        setLayout(null);

        // Add components
        add(load);
        add(save);
        add(jcomp3);
        add(itemCategories);

        add(appetizer);
        add(mainDish);
        add(dessert);
        add(alcohol);
        add(nonAlcohol);

        // Set Component Size and Location
        load.setBounds(30, 120, 145, 30);
        save.setBounds(30, 70, 145, 30);
        jcomp3.setBounds(30, 30, 150, 30);

        itemCategories.setBounds(30, 175, 145, 30);
        appetizer.setBounds(215, 35, 260, 250);
        mainDish.setBounds(215, 35, 260, 250);
        dessert.setBounds(215, 35, 260, 250);
        alcohol.setBounds(215, 35, 260, 250);
        nonAlcohol.setBounds(215, 35, 260, 250);
        appetizer.setVisible(false);
        mainDish.setVisible(false);
        dessert.setVisible(false);
        alcohol.setVisible(false);
        nonAlcohol.setVisible(false);

        // Add ActionListeners
        load.addActionListener(this);
        save.addActionListener(this);
        itemCategories.addActionListener(this);

    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == load) {
            // Load Menu from file
        }
        if (e.getSource() == save) {
            // Save menu to file
        }

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