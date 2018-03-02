package ca.mcgill.ecse223.resto.view;

import ca.mcgill.ecse223.resto.controller.RestoController;
import ca.mcgill.ecse223.resto.model.Table;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class TablePanel extends JPanel
{
    private final int UNIT_LENGTH = 50;
    private final float[] DASH = {4f, 0f, 2f};
    private final BasicStroke DASHED_STROKE = new BasicStroke(
            1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 1.0f, DASH, 2f);

    private int numXSquares;
    private int numYSquares;

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
        g2d.setStroke(DASHED_STROKE);
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
        for (Table table : RestoController.getTables()) {
            g2d.setColor(generateRandomColor());
            g2d.fillRect(
                    table.getX()*UNIT_LENGTH,
                    table.getY()*UNIT_LENGTH,
                    table.getWidth()*UNIT_LENGTH,
                    table.getLength()*UNIT_LENGTH);
            g2d.setColor(Color.black);
            g2d.setFont(new Font("Purisa", Font.BOLD, 13));

            g2d.drawString(table.getNumber()+"", table.getX()*UNIT_LENGTH, table.getY()*UNIT_LENGTH + UNIT_LENGTH/2);


        }
    }

    private Color generateRandomColor()
    {
        Random r = new Random();
        float red = (float) (r.nextFloat() / 2f + 0.5);
        float green = (float) (r.nextFloat() / 2f + 0.5);
        float blue = (float) (r.nextFloat() / 2f + 0.5);
        return new Color(red, green, blue);
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        doDrawing(g);
    }
}
