package ca.mcgill.ecse223.resto.view;

import ca.mcgill.ecse223.resto.controller.InvalidInputException;
import ca.mcgill.ecse223.resto.controller.RestoController;
import ca.mcgill.ecse223.resto.model.MenuItem;

import javax.swing.*;
import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class MenuFrame {
    //Frame and panel components
    private JFrame frame;
    private JPanel menuPanel;
    private JPanel menuDisplay;
    private JComboBox categorySelector;

    //Buttons
    private JButton addMenuItem;

    //Jlist items (displayed after choosing category)
    private JList appetizerJList;
    private JList mainJList;
    private JList dessertJList;
    private JList alcoholicBevJList;
    private JList nonAlcoholicBevJList;

    private List<MenuItem> appetizerMenuItems = new ArrayList<>();
    private List<MenuItem> mainMenuItems = new ArrayList<>();
    private List<MenuItem> dessertMenuItems = new ArrayList<>();
    private List<MenuItem> alcoholicBevMenuItems = new ArrayList<>();
    private List<MenuItem> nonAlcoholicBevMenuItems = new ArrayList<>();

//    private List<Double> appetizerPrices = new ArrayList<>();
//    private List<Double> mainPrices = new ArrayList<>();
//    private List<Double> dessertPrices = new ArrayList<>();
//    private List<Double> alcoholicBevPrices = new ArrayList<>();
//    private List<Double> nonAlcoholicBevPrices = new ArrayList<>();

    private List<String> appetizers = new ArrayList<>();
    private List<String> mains = new ArrayList<>();
    private List<String> desserts = new ArrayList<>();
    private List<String> alcoholicBevs = new ArrayList<>();
    private List<String> nonAlcoholicBevs = new ArrayList<>();

    private ItemHandler handler = new ItemHandler();


    public MenuFrame() {
        populate();
        gui();
    }

    public void populate() {
        try {

            appetizerMenuItems = RestoController.getMenuItems(MenuItem.ItemCategory.Appetizer);
//            appetizerPrices = RestoController.getMenuItems(MenuItem.ItemCategory.Appetizer)
//                    .stream()
//                    .map(x -> x.getCurrentPricedMenuItem().getPrice())
//                    .collect(Collectors.toList());

            mainMenuItems = RestoController.getMenuItems(MenuItem.ItemCategory.Main);
//            mainPrices = RestoController.getMenuItems(MenuItem.ItemCategory.Main)
//                    .stream()
//                    .map(x -> x.getCurrentPricedMenuItem().getPrice())
//                    .collect(Collectors.toList());

            dessertMenuItems = RestoController.getMenuItems(MenuItem.ItemCategory.Dessert);
//            dessertPrices = RestoController.getMenuItems(MenuItem.ItemCategory.Dessert)
//                    .stream()
//                    .map(x -> x.getCurrentPricedMenuItem().getPrice())
//                    .collect(Collectors.toList());

            alcoholicBevMenuItems = RestoController.getMenuItems(MenuItem.ItemCategory.AlcoholicBeverage);
//            alcoholicBevPrices = RestoController.getMenuItems(MenuItem.ItemCategory.AlcoholicBeverage)
//                    .stream()
//                    .map(x -> x.getCurrentPricedMenuItem().getPrice())
//                    .collect(Collectors.toList());

            nonAlcoholicBevMenuItems = RestoController.getMenuItems(MenuItem.ItemCategory.NonAlcoholicBeverage);
//            nonAlcoholicBevPrices = RestoController.getMenuItems(MenuItem.ItemCategory.NonAlcoholicBeverage)
//                    .stream()
//                    .map(x-> x.getCurrentPricedMenuItem().getPrice())
//                    .collect(Collectors.toList());

        } catch (InvalidInputException e) {
            e.printStackTrace();
        }
    }


    public void gui() {
        //FRAME
        frame = new JFrame("Menu");
        frame.setVisible(true);
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //PANEL
        menuPanel = new JPanel(new GridBagLayout());
        menuPanel.setBackground(Color.WHITE);

        menuDisplay = new JPanel(new GridBagLayout());
        menuDisplay.setBackground(Color.WHITE);

        //GRIDBACK CONSTRAINTS
        GridBagConstraints c = new GridBagConstraints();

        //COMPONENTS
        addMenuItem = new JButton("add item to menu");
        addMenuItem.addActionListener(e -> JOptionPane.showMessageDialog(null, "aaaa"));

        //ADD COMPONENTS TO PANEL
        //menuPanel
        c.gridx = 0;
        c.gridy = 1;
        menuPanel.add(addMenuItem, c);

        //menuDisplay
        for (MenuItem m : appetizerMenuItems) {
            appetizers.add(m.getName() + " " + "$" + m.getCurrentPricedMenuItem().getPrice());
        }
        for (MenuItem m : mainMenuItems) {
            mains.add(m.getName() + " " + "$" + m.getCurrentPricedMenuItem().getPrice());
        }
        for (MenuItem m : dessertMenuItems) {
            desserts.add(m.getName() + " " + "$" + m.getCurrentPricedMenuItem().getPrice());
        }
        for (MenuItem m : alcoholicBevMenuItems) {
            alcoholicBevs.add(m.getName() + " " + "$" + m.getCurrentPricedMenuItem().getPrice());
        }
        for (MenuItem m : nonAlcoholicBevMenuItems) {
            nonAlcoholicBevs.add(m.getName() + " " + "$" + m.getCurrentPricedMenuItem().getPrice());
        }

        appetizerJList = new JList(appetizers.stream().toArray(String[]::new));
        mainJList = new JList(mains.stream().toArray(String[]::new));
        dessertJList = new JList(desserts.stream().toArray(String[]::new));
        alcoholicBevJList = new JList(alcoholicBevs.stream().toArray(String[]::new));
        nonAlcoholicBevJList = new JList(nonAlcoholicBevs.stream().toArray(String[]::new));

        menuDisplay.add(appetizerJList);
        menuDisplay.add(mainJList);
        menuDisplay.add(dessertJList);
        menuDisplay.add(alcoholicBevJList);
        menuDisplay.add(nonAlcoholicBevJList);

        mainJList.setVisible(false);
        dessertJList.setVisible(false);
        alcoholicBevJList.setVisible(false);
        nonAlcoholicBevJList.setVisible(false);

        //JCOMBOBOX
        String[] categories = {"Appetizer", "Main", "Dessert", "AlcoholicBeverage", "NonAlcoholicBeverage"};
        categorySelector = new JComboBox(categories);
        menuPanel.add(categorySelector);

        categorySelector.addItemListener(handler);


        //ADD COMPONENTS TO FRAME
        frame.add(menuPanel, BorderLayout.WEST);
        frame.add(menuDisplay);
        frame.pack();
        frame.setVisible(true);

    }

    private class ItemHandler implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getSource() == categorySelector) {
                if (categorySelector.getSelectedItem().equals("Appetizer")) {
                    appetizerJList.setVisible(true);
                    mainJList.setVisible(false);
                    dessertJList.setVisible(false);
                    alcoholicBevJList.setVisible(false);
                    nonAlcoholicBevJList.setVisible(false);
                } else if (categorySelector.getSelectedItem().equals("Main")) {
                    appetizerJList.setVisible(false);
                    mainJList.setVisible(true);
                    dessertJList.setVisible(false);
                    alcoholicBevJList.setVisible(false);
                    nonAlcoholicBevJList.setVisible(false);
                } else if (categorySelector.getSelectedItem().equals("Dessert")) {
                    appetizerJList.setVisible(false);
                    mainJList.setVisible(false);
                    dessertJList.setVisible(true);
                    alcoholicBevJList.setVisible(false);
                    nonAlcoholicBevJList.setVisible(false);
                } else if (categorySelector.getSelectedItem().equals("AlcoholicBeverage")) {
                    appetizerJList.setVisible(false);
                    mainJList.setVisible(false);
                    dessertJList.setVisible(false);
                    alcoholicBevJList.setVisible(true);
                    nonAlcoholicBevJList.setVisible(false);
                } else if (categorySelector.getSelectedItem().equals("NonAlcoholicBeverage")) {
                    appetizerJList.setVisible(false);
                    mainJList.setVisible(false);
                    dessertJList.setVisible(false);
                    alcoholicBevJList.setVisible(false);
                    nonAlcoholicBevJList.setVisible(true);
                }
            }

        }
    }
}
