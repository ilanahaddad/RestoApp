package ca.mcgill.ecse223.resto.view;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class TablePanel extends JPanel
{
    private final int UNIT_LENGTH = 50;
    private int numXSquares;
    private int numYSquares;

    private final float[] dash = {4f, 0f, 2f};
    private final BasicStroke dashedStroke = new BasicStroke(
            1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 1.0f, dash, 2f);

    private void doDrawing(Graphics g)
    {
        numYSquares = getHeight() / UNIT_LENGTH + 1;
        numXSquares = getWidth() / UNIT_LENGTH + 1;

        Graphics2D g2d = (Graphics2D) g;
        drawTables(g2d);
        drawGrid(g2d);
    }

    private void drawGrid(Graphics2D g2d)
    {
        g2d.setStroke(dashedStroke);
        g2d.setColor(Color.black);
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

    private void drawTables(Graphics2D g2d)
    {
        setTransparentBorder(g2d);

        for (int x=1; x<numXSquares; x+=2)
        {
            for (int y=1; y<numYSquares; y+=2)
            {
                g2d.setColor(generateRandomColor());
                g2d.drawRect(x*UNIT_LENGTH, y*UNIT_LENGTH, UNIT_LENGTH, UNIT_LENGTH);
                g2d.fillRect(x*UNIT_LENGTH, y*UNIT_LENGTH, UNIT_LENGTH, UNIT_LENGTH);
            }
        }
    }

    private Color generateRandomColor()
    {
        Random r = new Random();
        float red = (float) (r.nextFloat() / 2.5f + 0.5);
        float green = (float) (r.nextFloat() / 2.5f + 0.5);
        float blue = (float) (r.nextFloat() / 2f + 0.5);
        return new Color(red, green, blue);
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
