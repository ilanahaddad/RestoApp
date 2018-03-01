package ca.mcgill.ecse223.resto.view;

import javax.swing.*;
import java.awt.*;

public class TablePanel extends JPanel
{
    private final int UNIT_LENGTH = 50;

    private void doDrawing(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;
        drawTables(g2d);
        drawGrid(g2d);

    }

    private void drawGrid(Graphics2D g2d) {
        int numXSquares = getHeight() % UNIT_LENGTH;
        int numYSquares = getWidth() % UNIT_LENGTH;

        float[] dash = {4f, 0f, 2f};
        BasicStroke dashedStroke = new BasicStroke(
                1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 1.0f, dash, 2f);

        g2d.setStroke(dashedStroke);

        for (int x=1; x<numXSquares; x++)
        {
            g2d.drawLine(x*UNIT_LENGTH, 0, x*UNIT_LENGTH, getHeight());
        }
        for (int y=1; y<numYSquares; y++)
        {
            g2d.drawLine(0, y*UNIT_LENGTH, getWidth(), y*UNIT_LENGTH);
        }


        g2d.dispose();
    }

    private void drawTables(Graphics2D g2d) {
        setTransparentBorder(g2d);
        g2d.drawRect(10, 15, 90, 60);
        g2d.drawRect(130, 15, 90, 60);
        g2d.drawRect(250, 15, 90, 60);
        g2d.drawRect(10, 105, 90, 60);
        g2d.drawRect(130, 105, 90, 60);
        g2d.drawRect(250, 105, 90, 60);
        g2d.drawRect(10, 195, 90, 60);
        g2d.drawRect(130, 195, 90, 60);
        g2d.drawRect(250, 195, 90, 60);

        g2d.setColor(new Color(125, 167, 116));
        g2d.fillRect(10, 15, 90, 60);

        g2d.setColor(new Color(42, 179, 231));
        g2d.fillRect(130, 15, 90, 60);

        g2d.setColor(new Color(70, 67, 123));
        g2d.fillRect(250, 15, 90, 60);

        g2d.setColor(new Color(130, 100, 84));
        g2d.fillRect(10, 105, 90, 60);

        g2d.setColor(new Color(252, 211, 61));
        g2d.fillRect(130, 105, 90, 60);

        g2d.setColor(new Color(241, 98, 69));
        g2d.fillRect(250, 105, 90, 60);

        g2d.setColor(new Color(217, 146, 54));
        g2d.fillRect(10, 195, 90, 60);

        g2d.setColor(new Color(63, 121, 186));
        g2d.fillRect(130, 195, 90, 60);

        g2d.setColor(new Color(31, 21, 1));
        g2d.fillRect(250, 195, 90, 60);
    }

    private void setTransparentBorder(Graphics2D g2d)
    {
        g2d.setColor(new Color(212, 212, 212));
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        doDrawing(g);
    }
}
