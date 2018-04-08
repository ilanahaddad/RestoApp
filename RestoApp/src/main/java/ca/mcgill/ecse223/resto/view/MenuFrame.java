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
            public JComboBox categorySelector;

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
            public DefaultListModel<String> appetizers = new DefaultListModel<>();

    public DefaultListModel<String> mains = new DefaultListModel<>();
    public DefaultListModel<String> desserts = new DefaultListModel<>();
    public DefaultListModel<String> alcoholicBevs = new DefaultListModel<>();
    public DefaultListModel<String> nonAlcoholicBevs = new DefaultListModel<>();

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

                            if(categorySelector.getSelectedItem().equals("Appetizer")){
                                //RestoController.addMenuItem(name, MenuItem.ItemCategory.Appetizer, price);
                                appetizers.addElement(name + " $" + String.valueOf(price));
                            } else if (categorySelector.getSelectedItem().equals("Main")){
                                //RestoController.addMenuItem(name, MenuItem.ItemCategory.Main, price);
                                mains.addElement(name + " $" + String.valueOf(price));
                            } else if (categorySelector.getSelectedItem().equals("Dessert")){
                                //RestoController.addMenuItem(name, MenuItem.ItemCategory.Dessert, price);
                                mains.addElement(name + " $" + String.valueOf(price));
                            } else if (categorySelector.getSelectedItem().equals("AlcoholicBeverage")) {
                                //RestoController.addMenuItem(name, MenuItem.ItemCategory.AlcoholicBeverage, price);
                                mains.addElement(name + " $" + String.valueOf(price));
                            } else if (categorySelector.getSelectedItem().equals("NonAlcoholicBeverage")) {
                                //RestoController.addMenuItem(name, MenuItem.ItemCategory.NonAlcoholicBeverage, price);
                                mains.addElement(name + " $" + String.valueOf(price));
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

                //ADD COMPONENTS TO PANEL
                c.gridx = 0;
                c.gridy = 0;
                menuPanel.add(addMenuItemButton, c);

                //Menu DISPlay
                display();

                //JLISTS
                appetizerJList = new JList(appetizers);
                mainJList = new JList(mains);
                dessertJList = new JList(desserts);
                alcoholicBevJList = new JList(alcoholicBevs);
                nonAlcoholicBevJList = new JList(nonAlcoholicBevs);

                //JLIST LISTENERS
//                appetizerJList.addMouseListener(new MouseAdapter() {
//                    public void mouseClicked(MouseEvent event) {
//                        if (event.getClickCount() == 2) {
//
//                            // Double-click detected
//                            int index = appetizerJList.locationToIndex(event.getPoint());
//
//                            JPanel panel = new JPanel();
//                            JButton remove = new JButton("remove");
//                            JButton edit = new JButton("edit");
//                            panel.add(remove);
//                            panel.add(edit);
//
//                            edit.addActionListener(e -> {
//                                JOptionPane.showConfirmDialog(
//                                        frame,
//                                        panel,
//                                        "Edit or remove menu item: " + String.valueOf(index+1),
//                                        JOptionPane.OK_CANCEL_OPTION,
//                                        JOptionPane.YES_NO_CANCEL_OPTION
//                                );
//
//                                JPanel panel1 = new JPanel();
//                                JLabel label1 = new JLabel("new item name: ");
//                                JTextField field1 = new JTextField( 10);
//                                panel1.add(label1);
//                                panel1.add(field1);
//
//                                JLabel label2 = new JLabel("new item price: ");
//                                JTextField field2 = new JTextField( 10);
//                                panel1.add(label2);
//                                panel1.add(field2);
//
//                                JOptionPane.showConfirmDialog(frame, panel1, "properties", JOptionPane.OK_CANCEL_OPTION);
//                                JOptionPane.showMessageDialog(null, "Item edited succesfully");
//
//                            });
//                            remove.addActionListener(e -> {
//                                try {
//                                    appetizers.remove(index);
//
//                                } catch(Exception error){
//                                    JOptionPane.showMessageDialog(
//                                            null,
//                                            error.getMessage(),
//                                            "Could not remove menu item",
//                                            JOptionPane.ERROR_MESSAGE);
//                                }
//                                JOptionPane.showMessageDialog(null, "Item removed succesfully");
//                            });
//
//                            JOptionPane.showConfirmDialog(
//                                    frame,
//                                    panel,
//                                    "Edit or remove menu item: " + String.valueOf(index+1),
//                                    JOptionPane.OK_CANCEL_OPTION,
//                                    JOptionPane.YES_NO_CANCEL_OPTION
//                            );
//
//                        }
//                    }
//
//                });

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
                                    try{
                                        RestoController.updateMenuItem(mainMenuItems.get(mainJList.getSelectedIndex()), name.getText(), MenuItem.ItemCategory.Main, Double.parseDouble(price.getText()));
                                    } catch (InvalidInputException ex){
                                        ex.getMessage();
                                    }
                                    mains.set(mainJList.getSelectedIndex(), name.getText() + " $" + String.valueOf(Double.parseDouble(price.getText())));
                                } else {
                                    System.out.println("error");
                                }
                            });

                            delete.addActionListener(e -> {
                                try {
                                    RestoController.removeMenuItem(mainMenuItems.get(mainJList.getSelectedIndex()));
                                } catch (InvalidInputException ex) {
                                    ex.getMessage();
                                }
                                mains.remove(mainJList.getSelectedIndex());
                            });
                            menu.add(edit);
                            menu.add(delete);
                            menu.show(mainJList, event.getPoint().x,event.getPoint().y);
                        }

                        RestoAppApplication.save();
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
                for (MenuItem m : appetizerMenuItems) {
                    //appetizers.add(i + "." + m.getName() + " " + "$" + m.getCurrentPricedMenuItem().getPrice());
                    appetizers.addElement(m.getName() + " " + "$" + m.getCurrentPricedMenuItem().getPrice());

                }
                for (MenuItem m : mainMenuItems) {
                    mains.addElement(m.getName() + " " + "$" + m.getCurrentPricedMenuItem().getPrice());
                }
                for (MenuItem m : dessertMenuItems) {
                    desserts.addElement(m.getName() + " " + "$" + m.getCurrentPricedMenuItem().getPrice());

                }
                for (MenuItem m : alcoholicBevMenuItems) {
                    alcoholicBevs.addElement(m.getName() + " " + "$" + m.getCurrentPricedMenuItem().getPrice());

                }
                for (MenuItem m : nonAlcoholicBevMenuItems) {
                    nonAlcoholicBevs.addElement(m.getName() + " " + "$" + m.getCurrentPricedMenuItem().getPrice());
                }
            }

        }

