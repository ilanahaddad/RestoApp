package ca.mcgill.ecse223.resto.view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;

public class RestoAppPage extends JFrame
{
    final ActionListener EXIT_ACTION = (ActionEvent event) -> System.exit(0);
    public RestoAppPage() { initUI(); }

    private void initUI()
    {
        createMenuBar();
        createToolBar();

        setTitle("Dummy GUI");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void createMenuBar()
    {
        JMenuBar menubar = new JMenuBar();
        JMenu file = new JMenu("Actions");
        JMenuItem addTableMenuItem = createMenuItem("Add Table", KeyEvent.VK_A, EXIT_ACTION);
        JMenuItem exitMenuItem = createMenuItem("Exit", KeyEvent.VK_E, EXIT_ACTION);

        file.setMnemonic(KeyEvent.VK_F);
        file.add(addTableMenuItem);
        file.add(exitMenuItem);
        menubar.add(file);
        setJMenuBar(menubar);
    }

    private JMenuItem createMenuItem(String itemName, int keyEvent, ActionListener action)
    {
        JMenuItem menuItem = new JMenuItem(itemName);
        menuItem.setMnemonic(keyEvent);
        menuItem.addActionListener(action);
        return menuItem;
    }

    private void createToolBar()
    {
        JToolBar toolbar = new JToolBar();
        JButton exitButton = createButton("power.png", "Exit RestoApp", EXIT_ACTION);
        JButton addTableButton = createButton("addTable.png", "Add Table", EXIT_ACTION);

        toolbar.add(exitButton);
        toolbar.add(addTableButton);
        add(toolbar, BorderLayout.NORTH);
    }

    private JButton createButton(String iconName, String buttonTip, ActionListener action)
    {
        Path iconPath = Paths.get("RestoApp/ressources/"+iconName).toAbsolutePath();
        ImageIcon icon = new ImageIcon(iconPath.toString());
        JButton button = new JButton(icon);
        button.setToolTipText(buttonTip);
        button.addActionListener(action);
        return button;
    }
}
