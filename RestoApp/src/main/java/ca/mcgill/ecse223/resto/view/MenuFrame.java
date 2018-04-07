package ca.mcgill.ecse223.resto.view;

import ca.mcgill.ecse223.resto.application.RestoAppApplication;
import ca.mcgill.ecse223.resto.controller.InvalidInputException;
import ca.mcgill.ecse223.resto.controller.RestoController;
import ca.mcgill.ecse223.resto.model.MenuItem;
import ca.mcgill.ecse223.resto.model.Menu;
import ca.mcgill.ecse223.resto.model.PricedMenuItem;
import ca.mcgill.ecse223.resto.model.RestoApp;

import javax.management.JMException;
import javax.swing.*;
import java.awt.*;

import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class MenuFrame {
    //Frame and panel components
    private JFrame frame;
    private JPanel menuPanel;
    private JPanel menuDisplay;
    private JPanel addMenuItemPanel;
    private JComboBox categorySelector;

    //Buttons
    private JButton addMenuItemButton;
    private JButton removeMenuItemButton;
    private JButton editMenuItemButton;

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

    private DefaultListModel<String> appetizers = new DefaultListModel<>();
    private DefaultListModel<String> mains = new DefaultListModel<>();
    private DefaultListModel<String> desserts = new DefaultListModel<>();
    private DefaultListModel<String> alcoholicBevs = new DefaultListModel<>();
    private DefaultListModel<String> nonAlcoholicBevs = new DefaultListModel<>();

    private ItemHandler handler = new ItemHandler();

    private JTextField textfield;
    private JTextField textfield2;
    private JLabel label;
    private JLabel label2;

    private JPopupMenu rightClickMenu;

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
        menuDisplay.setBorder(BorderFactory.createLineBorder(Color.black));

        //GRIDBACK CONSTRAINTS
        GridBagConstraints c = new GridBagConstraints();

        //COMPONENTS
        addMenuItemButton = new JButton("add item to menu");
        removeMenuItemButton = new JButton("remove item from menu");
        editMenuItemButton = new JButton("edit a menu item");

        //BUTTON LISTENERS
        addMenuItemButton.addActionListener(e -> {
            JPanel panel = new JPanel();


            JLabel label1 = new JLabel("Menu item name: ");
            JTextField field1 = new JTextField( 10);
            panel.add(label1);
            panel.add(field1);

            JLabel label2 = new JLabel("Menu item price: ");
            JTextField field2 = new JTextField( 10);
            panel.add(label2);
            panel.add(field2);

            int value = JOptionPane.showConfirmDialog(frame, panel, "Add menu item to: " + String.valueOf(categorySelector.getSelectedItem()), JOptionPane.OK_CANCEL_OPTION);
            if (value == JOptionPane.OK_OPTION)
            {
               try{
                   // OK was pressed -- PUT IN CONTROLLER
                   String name = field1.getText();
                   Double price = Double.parseDouble(field2.getText());
                   //Persistence
                   RestoApp r = RestoAppApplication.getRestoApp();
                   Menu m = r.getMenu();
                   MenuItem mi = new MenuItem(name, m);
                   PricedMenuItem pmi = new PricedMenuItem(price, r, mi);

                   if(categorySelector.getSelectedItem().equals("Appetizer")){
                       appetizers.addElement(appetizers.size()+1 + "." +  name + " $" + String.valueOf(price));
                       mi.setItemCategory(MenuItem.ItemCategory.Appetizer);
                       mi.setCurrentPricedMenuItem(pmi);
                   } else if (categorySelector.getSelectedItem().equals("Main")){
                       mains.addElement(mains.size()+1 + "." +  name + " $" + String.valueOf(price));
                       mi.setItemCategory(MenuItem.ItemCategory.Main);
                       mi.setCurrentPricedMenuItem(pmi);
                   } else if (categorySelector.getSelectedItem().equals("Dessert")){
                       mains.addElement(desserts.size()+1 + "." +  name + " $" + String.valueOf(price));
                       mi.setItemCategory(MenuItem.ItemCategory.Dessert);
                       mi.setCurrentPricedMenuItem(pmi);
                   } else if (categorySelector.getSelectedItem().equals("AlcoholicBeverage")) {
                       mains.addElement(alcoholicBevs.size() + 1 + "." + name + " $" + String.valueOf(price));
                       mi.setItemCategory(MenuItem.ItemCategory.AlcoholicBeverage);
                       mi.setCurrentPricedMenuItem(pmi);
                   } else if (categorySelector.getSelectedItem().equals("NonAlcoholicBeverage")) {
                       mains.addElement(nonAlcoholicBevs.size() + 1 + "." + name + " $" + String.valueOf(price));
                       mi.setItemCategory(MenuItem.ItemCategory.NonAlcoholicBeverage);
                       mi.setCurrentPricedMenuItem(pmi);
                   } else {
                       System.out.println("error");
                   }

                   RestoAppApplication.save();

                   // handle them
               } catch (Exception error){
                   JOptionPane.showMessageDialog(
                           null,
                           error.getMessage(),
                           "Could not add menu item",
                           JOptionPane.ERROR_MESSAGE);
               }

                JOptionPane.showMessageDialog(null, "Menu item added successfully");
            }
        });

        removeMenuItemButton.addActionListener(e -> {
            JPanel panel = new JPanel();

            JLabel l = new JLabel("Select category:");
            panel.add(categorySelector);
            int value = JOptionPane.showConfirmDialog(frame, panel, "Remove menu item", JOptionPane.OK_CANCEL_OPTION);
            if (value == JOptionPane.OK_OPTION) {
            }
            try{

                // handle them
            } catch (Exception error){
                JOptionPane.showMessageDialog(
                        null,
                        error.getMessage(),
                        "Could not add menu item",
                        JOptionPane.ERROR_MESSAGE);
            }

        });


        //ADD COMPONENTS TO PANEL
        //menuPanel buttons
        c.gridx = 0;
        c.gridy = 0;
        menuPanel.add(addMenuItemButton, c);
//        c.gridx = 0;
//        c.gridy = 2;
//        menuPanel.add(removeMenuItemButton, c);
//        c.gridx = 0;
//        c.gridy = 3;
//        menuPanel.add(editMenuItemButton,c);

        //Menu DISPlay
        display();

        //JLISTS
        //appetizerJList = new JList(appetizers.stream().toArray(String[]::new));
        appetizerJList = new JList(appetizers);
        mainJList = new JList(mains);
        dessertJList = new JList(desserts);
        alcoholicBevJList = new JList(alcoholicBevs);
        nonAlcoholicBevJList = new JList(nonAlcoholicBevs);

        //JLIST LISTENERS
        appetizerJList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() == 2) {

                    // Double-click detected
                    int index = appetizerJList.locationToIndex(event.getPoint());

                    JPanel panel = new JPanel();
                    JButton remove = new JButton("remove");
                    JButton edit = new JButton("edit");
                    panel.add(remove);
                    panel.add(edit);

                    edit.addActionListener(e -> {
                        JOptionPane.showConfirmDialog(
                                frame,
                                panel,
                                "Edit or remove menu item: " + String.valueOf(index+1),
                                JOptionPane.OK_CANCEL_OPTION,
                                JOptionPane.YES_NO_CANCEL_OPTION
                        );

                        JPanel panel1 = new JPanel();
                        JLabel label1 = new JLabel("new item name: ");
                        JTextField field1 = new JTextField( 10);
                        panel1.add(label1);
                        panel1.add(field1);

                        JLabel label2 = new JLabel("new item price: ");
                        JTextField field2 = new JTextField( 10);
                        panel1.add(label2);
                        panel1.add(field2);

                        JOptionPane.showConfirmDialog(frame, panel1, "properties", JOptionPane.OK_CANCEL_OPTION);
                        JOptionPane.showMessageDialog(null, "Item edited succesfully");

                    });
                    remove.addActionListener(e -> {
                        try {
                            appetizers.remove(index);

                        } catch(Exception error){
                            JOptionPane.showMessageDialog(
                                    null,
                                    error.getMessage(),
                                    "Could not remove menu item",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                        JOptionPane.showMessageDialog(null, "Item removed succesfully");
                    });

                    JOptionPane.showConfirmDialog(
                            frame,
                            panel,
                            "Edit or remove menu item: " + String.valueOf(index+1),
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.YES_NO_CANCEL_OPTION
                    );

                }
            }

        });

        mainJList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                if(SwingUtilities.isRightMouseButton(event)) {
                    mainJList.setSelectedIndex(mainJList.locationToIndex(event.getPoint()));
                    JPopupMenu menu = new JPopupMenu();
                    JMenuItem edit = new JMenuItem("edit");
                    JMenuItem delete = new JMenuItem("delete");

                    edit.addActionListener(e -> {
                        JTextField name = new JTextField();
                        JTextField price = new JTextField();
                        Object[] message = {
                                "New item name:", name,
                                "New item price:", price
                        };

                        int option = JOptionPane.showConfirmDialog(null, message, "Edit item", JOptionPane.OK_CANCEL_OPTION);
                        if (option == JOptionPane.OK_OPTION) {
                            System.out.println("editign");
                            mains.set(mainJList.getSelectedIndex(), name.getText() + " $" + String.valueOf(Double.parseDouble(price.getText())));
                        } else {
                            System.out.println("error");
                        }
                    });

                    delete.addActionListener(e -> {
                        mains.remove(mainJList.getSelectedIndex());

                    });
                    menu.add(edit);
                    menu.add(delete);
                    menu.show(mainJList, event.getPoint().x,event.getPoint().y);
                }


            }
        });

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

    private void display() {
        //menuDisplay
        int i,k,p,n,t;
        i = 1; k = 1; p = 1; n = 1; t = 1;

        for (MenuItem m : appetizerMenuItems) {
            //appetizers.add(i + "." + m.getName() + " " + "$" + m.getCurrentPricedMenuItem().getPrice());
            appetizers.addElement(i + "." + m.getName() + " " + "$" + m.getCurrentPricedMenuItem().getPrice());
            i++;
        }
        for (MenuItem m : mainMenuItems) {
            mains.addElement(k + "." + m.getName() + " " + "$" + m.getCurrentPricedMenuItem().getPrice());
            k++;
        }
        for (MenuItem m : dessertMenuItems) {
            desserts.addElement(p + "." +m.getName() + " " + "$" + m.getCurrentPricedMenuItem().getPrice());
            p++;
        }
        for (MenuItem m : alcoholicBevMenuItems) {
            alcoholicBevs.addElement(n + "." +m.getName() + " " + "$" + m.getCurrentPricedMenuItem().getPrice());
            n++;
        }
        for (MenuItem m : nonAlcoholicBevMenuItems) {
            nonAlcoholicBevs.addElement(t + "." + m.getName() + " " + "$" + m.getCurrentPricedMenuItem().getPrice());
            t++;
        }
    }

}
