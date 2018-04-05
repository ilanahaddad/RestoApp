package ca.mcgill.ecse223.resto.view;

import ca.mcgill.ecse223.resto.controller.InvalidInputException;
import ca.mcgill.ecse223.resto.controller.RestoController;
import ca.mcgill.ecse223.resto.model.MenuItem;

import javax.swing.*;
import java.awt.*;

import java.awt.event.*;
import java.util.*;
import java.util.List;

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
    private List<String> mains = new ArrayList<>();
    private List<String> desserts = new ArrayList<>();
    private List<String> alcoholicBevs = new ArrayList<>();
    private List<String> nonAlcoholicBevs = new ArrayList<>();

    private ItemHandler handler = new ItemHandler();

    private JTextField textfield;
    private JTextField textfield2;
    private JLabel label;
    private JLabel label2;




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
                   // OK was pressed
                   String name = field1.getText();
                   Double price = Double.parseDouble(field2.getText());

                   appetizers.addElement(appetizers.size()+1 + "." +  name + " $" + String.valueOf(price));

                   System.out.println(name);
                   System.out.println(price);

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


        //menuDisplay
        int i,k,p,n,t;
        i = 1; k = 1; p = 1; n = 1; t = 1;

        for (MenuItem m : appetizerMenuItems) {
            //appetizers.add(i + "." + m.getName() + " " + "$" + m.getCurrentPricedMenuItem().getPrice());
            appetizers.addElement(i + "." + m.getName() + " " + "$" + m.getCurrentPricedMenuItem().getPrice());
            i++;
        }
        for (MenuItem m : mainMenuItems) {
            mains.add(k + "." + m.getName() + " " + "$" + m.getCurrentPricedMenuItem().getPrice());
            k++;
        }
        for (MenuItem m : dessertMenuItems) {
            desserts.add(p + "." +m.getName() + " " + "$" + m.getCurrentPricedMenuItem().getPrice());
            p++;
        }
        for (MenuItem m : alcoholicBevMenuItems) {
            alcoholicBevs.add(n + "." +m.getName() + " " + "$" + m.getCurrentPricedMenuItem().getPrice());
            n++;
        }
        for (MenuItem m : nonAlcoholicBevMenuItems) {
            nonAlcoholicBevs.add(t + "." + m.getName() + " " + "$" + m.getCurrentPricedMenuItem().getPrice());
            t++;
        }

        //JLISTS
        //appetizerJList = new JList(appetizers.stream().toArray(String[]::new));
        appetizerJList = new JList(appetizers);
        mainJList = new JList(mains.stream().toArray(String[]::new));
        dessertJList = new JList(desserts.stream().toArray(String[]::new));
        alcoholicBevJList = new JList(alcoholicBevs.stream().toArray(String[]::new));
        nonAlcoholicBevJList = new JList(nonAlcoholicBevs.stream().toArray(String[]::new));

        //JLIST LISTENERS

        appetizerJList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    // Double-click detected
                    int index = appetizerJList.locationToIndex(e.getPoint());

                    JPanel panel = new JPanel();
                    JButton remove = new JButton("remove");
                    JButton edit = new JButton("edit");

                    edit.addActionListener(e1 -> {
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
                    remove.addActionListener(e2 -> {
                        JOptionPane.showMessageDialog(null, "Item removed succesfully");
                    });

                    panel.add(remove);
                    panel.add(edit);

                    JOptionPane.showConfirmDialog(frame, panel, "Edit or remove menu item: " + String.valueOf(index+1), JOptionPane.OK_CANCEL_OPTION);

                }
            }

        });

//        appetizerJList.setModel(new DefaultListModel());
//        DefaultListModel lm1 = (DefaultListModel)appetizerJList.getModel();
//        lm1.add(3, "gsdkjnsdijgnsa");

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

}
