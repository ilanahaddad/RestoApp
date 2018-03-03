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
        int totalNumSeats = table.getSeats().size();

        if (table.getWidth() >= table.getLength())
        {
            int tablesLeftToDraw = fillTopBottom(table, totalNumSeats,g2d);
            fillLeftRight(table, totalNumSeats-tablesLeftToDraw, g2d);
            //TODO what if num chairs larger than what can be fit
        }
        else
        {
            int tablesLeftToDraw = fillLeftRight(table, totalNumSeats,g2d);
            fillTopBottom(table, (totalNumSeats-tablesLeftToDraw), g2d);
        }
    }

    // return number of seats drawn
    private int fillTopBottom(Table table, int seatsToDraw, Graphics2D g2d)
    {
        int maxSeatsPerWidth = (table.getWidth()*UNIT_LENGTH - 15)/ (seatDiameter);

        int drawnSeats = 0;
        int seatYOffset = 0;
        int seatXOffset = 3;
        for (int i=0;i<seatsToDraw; i++)
        {
            if (i>0 && i%maxSeatsPerWidth == 0)
            {
                if (seatYOffset == table.getLength()*UNIT_LENGTH-seatDiameter)
                {
                    // already filled both top and bottom
                    return drawnSeats;
                }
                seatYOffset=table.getLength()*UNIT_LENGTH-seatDiameter;
                seatXOffset=3;
            }

            g2d.setColor(Color.BLACK);
            g2d.drawOval(
                    table.getX()*UNIT_LENGTH+seatXOffset,
                    table.getY()*UNIT_LENGTH+seatYOffset,
                    seatDiameter, seatDiameter);
            g2d.setColor(Color.WHITE);
            g2d.fillOval(
                    table.getX()*UNIT_LENGTH+seatXOffset,
                    table.getY()*UNIT_LENGTH+seatYOffset,
                    seatDiameter, seatDiameter);

            drawnSeats++;

            seatXOffset+=seatDiameter + seatPadding;
        }

        return drawnSeats;
    }

    // return number of seats drawn
    private int fillLeftRight(Table table, int seatsToDraw, Graphics2D g2d)
    {
        int maxSeatsPerLength = (table.getLength()*UNIT_LENGTH - 15)/ (seatDiameter) -2;

        int drawnSeats = 0;
        int seatYOffset = seatDiameter+seatPadding;
        int seatXOffset = 3;
        for (int i=0;i<seatsToDraw; i++)
        {
            if (i>0 && i%maxSeatsPerLength == 0)
            {
                if (seatXOffset == table.getWidth()*UNIT_LENGTH-seatDiameter-3)
                {
                    // already filled both top and bottom
                    return drawnSeats;
                }
                seatXOffset=table.getWidth()*UNIT_LENGTH-seatDiameter-3;
                seatYOffset=seatDiameter+seatPadding;
            }

            g2d.setColor(Color.BLACK);
            g2d.drawOval(
                    table.getX()*UNIT_LENGTH+seatXOffset,
                    table.getY()*UNIT_LENGTH+seatYOffset,
                    seatDiameter, seatDiameter);
            g2d.setColor(Color.WHITE);
            g2d.fillOval(
                    table.getX()*UNIT_LENGTH+seatXOffset,
                    table.getY()*UNIT_LENGTH+seatYOffset,
                    seatDiameter, seatDiameter);

            drawnSeats++;

            seatYOffset+=seatDiameter + seatPadding;
        }

        return drawnSeats;
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
