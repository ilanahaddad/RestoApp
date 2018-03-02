package ca.mcgill.ecse223.resto.view;

import ca.mcgill.ecse223.resto.controller.RestoController;
import ca.mcgill.ecse223.resto.model.Table;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class TablePanel extends JPanel
{
    private final int UNIT_LENGTH = 75;
    private final int tableNumXPadding = 35;
    private final int tableNumYPadding = 8 + UNIT_LENGTH/2;

    private final int seatDiameter = 20;
    private final int seatPadding = 5;

    private final float[] DASH = {4f, 0f, 2f};
    private final BasicStroke DASHED_STROKE = new BasicStroke(
            1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 1.0f, DASH, 2f);

    private void doDrawing(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;
        drawTablePanel(g2d);
        drawTableGrid(g2d);
    }

    private void drawTableGrid(Graphics2D g2d)
    {
        int numYSquares = getHeight() / UNIT_LENGTH + 1;
        int numXSquares = getWidth() / UNIT_LENGTH + 1;

        g2d.setColor(Color.black);
        g2d.setStroke(DASHED_STROKE);
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

    private void drawTablePanel(Graphics2D g2d)
    {
        for (Table table : RestoController.getTables())
        {
            drawTable(table, g2d);
            drawSeats(table, g2d);
        }
    }

    private void drawTable(Table table, Graphics2D g2d)
    {
        int x = table.getX();
        int y = table.getY();

        // draw rectangle
        g2d.setColor(generateRandomColor());
        g2d.fillRect(
                x*UNIT_LENGTH,
                y*UNIT_LENGTH,
                table.getWidth()*UNIT_LENGTH,
                table.getLength()*UNIT_LENGTH);

        // write number
        g2d.setColor(Color.black);
        g2d.setFont(new Font("Purisa", Font.BOLD, 13));
        g2d.drawString(
                String.valueOf(table.getNumber()),
                x*UNIT_LENGTH + tableNumXPadding,
                y*UNIT_LENGTH + tableNumYPadding);
    }

    private void drawSeats(Table table, Graphics2D g2d)
    {
        int x = table.getX();
        int y = table.getY();
        int width = table.getWidth();
        int maxSeatsPerLine = (width*UNIT_LENGTH -seatDiameter)/ seatDiameter;

        int seatYOffset = 0;
        int seatXOffset = 0;
        for (int i=0;i<table.getSeats().size(); i++)
        {
            if (i==maxSeatsPerLine)
            {
                seatYOffset=table.getLength()*UNIT_LENGTH-seatDiameter;
                seatXOffset=0;
            }

            g2d.setColor(Color.BLACK);
            g2d.drawOval(x*UNIT_LENGTH+seatXOffset, y*UNIT_LENGTH+seatYOffset, seatDiameter, seatDiameter);
            g2d.setColor(Color.WHITE);
            g2d.fillOval(x*UNIT_LENGTH+seatXOffset, y*UNIT_LENGTH+seatYOffset, seatDiameter, seatDiameter);

            seatXOffset+=seatDiameter + seatPadding;
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
