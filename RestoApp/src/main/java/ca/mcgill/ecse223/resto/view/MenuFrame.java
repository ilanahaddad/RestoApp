package ca.mcgill.ecse223.resto.view;

import ca.mcgill.ecse223.resto.application.RestoAppApplication;
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
    public JComboBox categorySelector;

    //Buttons
    private JButton addMenuItemButton;


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

        //BUTTON LISTENERS
        addmenuItem();

        //ADD COMPONENTS TO PANEL
        c.gridx = 0;
        c.gridy = 0;
        menuPanel.add(addMenuItemButton, c);

        //Menu Display
        display();

        //JCOMBOBOX
        String[] categories = {"Appetizer", "Main", "Dessert", "AlcoholicBeverage", "NonAlcoholicBeverage"};
        categorySelector = new JComboBox(categories);

        menuPanel.add(categorySelector);
        categorySelector.addItemListener(handler);

        //JLISTS
        appetizerJList = new JList(appetizers);
        mainJList = new JList(mains);
        dessertJList = new JList(desserts);
        alcoholicBevJList = new JList(alcoholicBevs);
        nonAlcoholicBevJList = new JList(nonAlcoholicBevs);

        //JLIST LISTENERS
        appetizerListener();
        mainListener();
        dessertListener();
        alcListener();
        nonAlcListener();


        menuDisplay.add(appetizerJList);
        menuDisplay.add(mainJList);
        menuDisplay.add(dessertJList);
        menuDisplay.add(alcoholicBevJList);
        menuDisplay.add(nonAlcoholicBevJList);

        mainJList.setVisible(false);
        dessertJList.setVisible(false);
        alcoholicBevJList.setVisible(false);
        nonAlcoholicBevJList.setVisible(false);

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

    //JLIST LISTENER METHODS
    private void appetizerListener() {
        appetizerJList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                if (SwingUtilities.isRightMouseButton(event)) {
                    appetizerJList.setSelectedIndex(appetizerJList.locationToIndex(event.getPoint()));
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
                            try {
                                RestoController.updateMenuItem(appetizerMenuItems.get(appetizerJList.getSelectedIndex()), name.getText(), MenuItem.ItemCategory.Appetizer, Double.parseDouble(price.getText()));
                            } catch (InvalidInputException ex) {
                                ex.getMessage();
                            }
                            appetizers.set(appetizerJList.getSelectedIndex(), name.getText() + " $" + String.valueOf(Double.parseDouble(price.getText())));
                        } else {
                            System.out.println("error");
                        }
                    });

                    delete.addActionListener(e -> {
                        try {
                            RestoController.removeMenuItem(appetizerMenuItems.get(appetizerJList.getSelectedIndex()));
                        } catch (InvalidInputException ex) {
                            ex.getMessage();
                        }
                        appetizers.remove(appetizerJList.getSelectedIndex());
                    });
                    menu.add(edit);
                    menu.add(delete);
                    menu.show(appetizerJList, event.getPoint().x, event.getPoint().y);
                }

            }

        });
    }
    private void mainListener() {
        mainJList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                if (SwingUtilities.isRightMouseButton(event)) {
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
                            try {
                                RestoController.updateMenuItem(mainMenuItems.get(mainJList.getSelectedIndex()), name.getText(), MenuItem.ItemCategory.Main, Double.parseDouble(price.getText()));
                            } catch (InvalidInputException ex) {
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
                    menu.show(mainJList, event.getPoint().x, event.getPoint().y);
                }

                //RestoAppApplication.save();
            }

        });
    }
    private void dessertListener() {
        dessertJList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                if (SwingUtilities.isRightMouseButton(event)) {
                    dessertJList.setSelectedIndex(dessertJList.locationToIndex(event.getPoint()));
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
                            try {
                                RestoController.updateMenuItem(dessertMenuItems.get(dessertJList.getSelectedIndex()), name.getText(), MenuItem.ItemCategory.Dessert, Double.parseDouble(price.getText()));
                            } catch (InvalidInputException ex) {
                                ex.getMessage();
                            }
                            desserts.set(dessertJList.getSelectedIndex(), name.getText() + " $" + String.valueOf(Double.parseDouble(price.getText())));
                        } else {
                            System.out.println("error");
                        }
                    });

                    delete.addActionListener(e -> {
                        try {
                            RestoController.removeMenuItem(dessertMenuItems.get(dessertJList.getSelectedIndex()));
                        } catch (InvalidInputException ex) {
                            ex.getMessage();
                        }
                        desserts.remove(dessertJList.getSelectedIndex());
                    });
                    menu.add(edit);
                    menu.add(delete);
                    menu.show(dessertJList, event.getPoint().x, event.getPoint().y);
                }

                //RestoAppApplication.save();
            }

        });
    }
    private void alcListener() {
        alcoholicBevJList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                if (SwingUtilities.isRightMouseButton(event)) {
                    alcoholicBevJList.setSelectedIndex(alcoholicBevJList.locationToIndex(event.getPoint()));
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
                            try {
                                RestoController.updateMenuItem(alcoholicBevMenuItems.get(alcoholicBevJList.getSelectedIndex()), name.getText(), MenuItem.ItemCategory.AlcoholicBeverage, Double.parseDouble(price.getText()));
                            } catch (InvalidInputException ex) {
                                ex.getMessage();
                            }
                            alcoholicBevs.set(alcoholicBevJList.getSelectedIndex(), name.getText() + " $" + String.valueOf(Double.parseDouble(price.getText())));
                        } else {
                            System.out.println("error");
                        }
                    });

                    delete.addActionListener(e -> {
                        try {
                            RestoController.removeMenuItem(alcoholicBevMenuItems.get(alcoholicBevJList.getSelectedIndex()));
                        } catch (InvalidInputException ex) {
                            ex.getMessage();
                        }
                        alcoholicBevs.remove(alcoholicBevJList.getSelectedIndex());
                    });
                    menu.add(edit);
                    menu.add(delete);
                    menu.show(alcoholicBevJList, event.getPoint().x, event.getPoint().y);
                }

                //RestoAppApplication.save();
            }

        });
    }
    private void nonAlcListener() {
        nonAlcoholicBevJList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                if (SwingUtilities.isRightMouseButton(event)) {
                    nonAlcoholicBevJList.setSelectedIndex(nonAlcoholicBevJList.locationToIndex(event.getPoint()));
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
                            try {
                                RestoController.updateMenuItem(nonAlcoholicBevMenuItems.get(nonAlcoholicBevJList.getSelectedIndex()), name.getText(), MenuItem.ItemCategory.NonAlcoholicBeverage, Double.parseDouble(price.getText()));
                            } catch (InvalidInputException ex) {
                                ex.getMessage();
                            }
                            nonAlcoholicBevs.set(alcoholicBevJList.getSelectedIndex(), name.getText() + " $" + String.valueOf(Double.parseDouble(price.getText())));
                        } else {
                            System.out.println("error");
                        }
                    });

                    delete.addActionListener(e -> {
                        try {
                            RestoController.removeMenuItem(nonAlcoholicBevMenuItems.get(nonAlcoholicBevJList.getSelectedIndex()));
                        } catch (InvalidInputException ex) {
                            ex.getMessage();
                        }
                        nonAlcoholicBevs.remove(nonAlcoholicBevJList.getSelectedIndex());
                    });
                    menu.add(edit);
                    menu.add(delete);
                    menu.show(nonAlcoholicBevJList, event.getPoint().x, event.getPoint().y);
                }

                //RestoAppApplication.save();
            }

        });
    }

    //MAIN BUTTONS LISTNERES
    private void addmenuItem() {
        addMenuItemButton.addActionListener(e -> {
            JPanel panel = new JPanel();


            JLabel label1 = new JLabel("Menu item name: ");
            JTextField field1 = new JTextField(10);
            panel.add(label1);
            panel.add(field1);

            JLabel label2 = new JLabel("Menu item price: ");
            JTextField field2 = new JTextField(10);
            panel.add(label2);
            panel.add(field2);

            int value = JOptionPane.showConfirmDialog(frame, panel, "Add menu item to: " + String.valueOf(categorySelector.getSelectedItem()), JOptionPane.OK_CANCEL_OPTION);
            if (value == JOptionPane.OK_OPTION) {
                try {
                    // OK was pressed -- PUT IN CONTROLLER
                    String name = field1.getText();
                    Double price = Double.parseDouble(field2.getText());
                    //Persistence

                    if (categorySelector.getSelectedItem().equals("Appetizer")) {
                        RestoController.addMenuItem(name, MenuItem.ItemCategory.Appetizer, price);
                        appetizers.addElement(name + " $" + String.valueOf(price));
                    } else if (categorySelector.getSelectedItem().equals("Main")) {
                        RestoController.addMenuItem(name, MenuItem.ItemCategory.Main, price);
                        mains.addElement(name + " $" + String.valueOf(price));
                    } else if (categorySelector.getSelectedItem().equals("Dessert")) {
                        RestoController.addMenuItem(name, MenuItem.ItemCategory.Dessert, price);
                        mains.addElement(name + " $" + String.valueOf(price));
                    } else if (categorySelector.getSelectedItem().equals("AlcoholicBeverage")) {
                        RestoController.addMenuItem(name, MenuItem.ItemCategory.AlcoholicBeverage, price);
                        mains.addElement(name + " $" + String.valueOf(price));
                    } else if (categorySelector.getSelectedItem().equals("NonAlcoholicBeverage")) {
                        RestoController.addMenuItem(name, MenuItem.ItemCategory.NonAlcoholicBeverage, price);
                        mains.addElement(name + " $" + String.valueOf(price));
                    } else {
                        System.out.println("error");
                    }
                    // handle them
                } catch (Exception error) {
                    JOptionPane.showMessageDialog(
                            null,
                            error.getMessage(),
                            "Could not add menu item",
                            JOptionPane.ERROR_MESSAGE);
                }

                JOptionPane.showMessageDialog(null, "Menu item added successfully");
            }
        });

    }

}

