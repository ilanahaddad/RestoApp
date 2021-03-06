package ca.mcgill.ecse223.resto.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
//import java.util.Random;

import javax.swing.JPanel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import ca.mcgill.ecse223.resto.controller.RestoController;
import ca.mcgill.ecse223.resto.model.Reservation;
import ca.mcgill.ecse223.resto.model.Seat;
import ca.mcgill.ecse223.resto.model.Table;

public class TablePanel extends JPanel
{
//	public HashMap<String, Seat> hmap = RestoController.generateHashMap();
	public HashMap<String, Seat> hmap = RestoController.hmap;
    private static final long serialVersionUID = 8978498317881881901L;

    private final int UNIT_LENGTH = 75;
    private final int tableNumXPadding = 35;
    private final int tableNumYPadding = 8 + UNIT_LENGTH/2;

    private final int seatDiameter = 20;
    private final int seatPadding = 5;

    //    private final Color TABLE_COLOR = generateRandomColor();
    private final Color TABLE_COLOR_Available = new Color(70,200,70);//70,200,70
    private final Color TABLE_COLOR_NothingOrdered = new Color(250,200,20);//250,200,20
    private final Color TABLE_COLOR_Ordered = new Color(255,30,30);//255,30,30
    

    private final float[] DASH = {4f, 0f, 2f};
    private final BasicStroke DASHED_STROKE = new BasicStroke(
            1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 1.0f, DASH, 2f);

    // called everytime UI is refreshed
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        doDrawing(g);
    }

    // draws the UI
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
        for (Table table : RestoController.getCurrentTables())
        {
            drawTable(table, g2d);
            drawSeats(table, g2d);
        }
    }

    private void drawTable(Table table, Graphics2D g2d)
    {
        int x = table.getX();
        int y = table.getY();
        Table.Status s = table.getStatus();

        // draw rectangle
        switch(s) {
            case Available:
                g2d.setColor(TABLE_COLOR_Available);
                break;
            case NothingOrdered:
                g2d.setColor(TABLE_COLOR_NothingOrdered);
                break;
            case Ordered:
                g2d.setColor(TABLE_COLOR_Ordered);
                break;
        }// draw rectangle
        g2d.fillRect(x*UNIT_LENGTH, y*UNIT_LENGTH, table.getWidth()*UNIT_LENGTH, table.getLength()*UNIT_LENGTH);

        // write table number, state and latest reservation
        g2d.setColor(Color.black);
        g2d.setFont(new Font("Purisa", Font.BOLD, 13));
        g2d.drawString(table.getNumber()+"", x*UNIT_LENGTH + tableNumXPadding, y*UNIT_LENGTH + tableNumYPadding - 7);
        g2d.drawString(table.getStatus()+"", x*UNIT_LENGTH + 2, y*UNIT_LENGTH + tableNumYPadding + 7);

        //table.getReservations().sort();

        if (!table.getReservations().isEmpty()) {
            Reservation earliest = RestoController.getEarliestRes(table.getReservations());
            g2d.drawString(earliest.getDate()+" at "+earliest.getTime(), x*UNIT_LENGTH + 2, y*UNIT_LENGTH + tableNumYPadding + 20);
        }
    }

    private void drawSeats(Table table, Graphics2D g2d)
    {
        if (table.getWidth() >= table.getLength())
        {
            int endSeatIdx = fillTopBottom(table, 0, g2d);
            fillLeftRight(table, endSeatIdx, g2d);
            //TODO what if num chairs larger than what can be fit
        }
        else
        {
            int endSeatIdx = fillLeftRight(table, 0, g2d);
            fillTopBottom(table, endSeatIdx, g2d);
        }
    }

    private List<String> getStringsForSeatsOfTable(Table table){

	    	if(hmap != null) {
	    		Set<String> keys = hmap.keySet();
	
	    		List<String> seatsForTable = new ArrayList<String>(); 
	    		for(String s: keys) {
	    			String tablePartOfString = s.substring(0, 2);
	    			if(tablePartOfString.contains("T"+table.getNumber())) {
	    				seatsForTable.add(s);
	    			}
	    		}
	    		return seatsForTable;
	    	}
	    	return null;
    }
    // returns number of seats drawn
    private int fillTopBottom(Table table, int startIdx, Graphics2D g2d)
    {
        int maxSeatsPerWidth = getMaxSeatsPerWidth(table);

        int drawnSeats = 0;
        int seatYOffset = 0;
        int seatXOffset = 3;
        List<Seat> currSeats = table.getCurrentSeats();
        List<String> seatStrings = getStringsForSeatsOfTable(table);
        for (int i=startIdx;i<currSeats.size(); i++)
        {
            if (filledTableSide(maxSeatsPerWidth, i))
            {
                if (finishedFillingTopBottom(table, seatYOffset)) { return drawnSeats; }

                seatYOffset=getTableBottomCoordinates(table);
                seatXOffset=3;
            }
            String stringForSeat = seatStrings.get(i);
            int numInString = Integer.parseInt(stringForSeat.substring(3));
            drawSeat(numInString, table, g2d, seatYOffset, seatXOffset);
            drawnSeats++;

            seatXOffset+=seatDiameter + seatPadding;
        }

        return drawnSeats;
    }

    // returns number of seats drawn
    private int fillLeftRight(Table table, int startIdx, Graphics2D g2d)
    {
        int maxSeatsPerLength = getMaxSeatsPerLength(table);

        int drawnSeats = 0;
        int seatYOffset = seatDiameter+seatPadding;
        int seatXOffset = 3;
        List<Seat> currSeats = table.getCurrentSeats();
        List<String> seatStrings = getStringsForSeatsOfTable(table);
        for (int i=startIdx;i<currSeats.size(); i++)
        {
            if (filledTableSide(maxSeatsPerLength, i))
            {
                if (finishedFillingLeftRight(table, seatXOffset)) { return drawnSeats; }

                seatXOffset=getTableRightCoordinates(table);
                seatYOffset=seatDiameter+seatPadding;
            }
            String stringForSeat = seatStrings.get(i);
            int numInString = Integer.parseInt(stringForSeat.substring(3));
            drawSeat(numInString, table, g2d, seatYOffset, seatXOffset);
            drawnSeats++;

            seatYOffset+=seatDiameter + seatPadding;
        }

        return drawnSeats;
    }

    private void drawSeat(int seatNum, Table table, Graphics2D g2d, int seatYOffset, int seatXOffset)
    {
        g2d.setColor(Color.BLACK);
        g2d.drawOval(table.getX()*UNIT_LENGTH+seatXOffset, table.getY()*UNIT_LENGTH+seatYOffset, seatDiameter, seatDiameter);
        g2d.setColor(Color.WHITE);
        g2d.fillOval(table.getX()*UNIT_LENGTH+seatXOffset, table.getY()*UNIT_LENGTH+seatYOffset, seatDiameter, seatDiameter);

        int textPadding = (seatNum > 9) ? 2 : 7;
        g2d.setColor(Color.black);
        g2d.setFont(new Font("Purisa", Font.PLAIN, 13));
        g2d.drawString(seatNum+"", table.getX()*UNIT_LENGTH+seatXOffset+textPadding, table.getY()*UNIT_LENGTH+seatYOffset+15);
    }

    // checks if already drawn the maximum possible number of seats in the table's side
    private boolean filledTableSide(int maxSeatsPerWidth, int i) { return i>0 && i%maxSeatsPerWidth == 0; }

    private int getTableBottomCoordinates(Table table) { return table.getLength()*UNIT_LENGTH-seatDiameter; }

    private int getTableRightCoordinates(Table table) { return table.getWidth()*UNIT_LENGTH-seatDiameter-3; }

    private int getMaxSeatsPerWidth(Table table) { return (table.getWidth()*UNIT_LENGTH-15)/(seatDiameter); }

    private int getMaxSeatsPerLength(Table table) { return (table.getLength()*UNIT_LENGTH-15)/(seatDiameter)-2; }

    private boolean finishedFillingTopBottom(Table table, int seatYOffset)
    {
        return seatYOffset == getTableBottomCoordinates(table);
    }

    private boolean finishedFillingLeftRight(Table table, int seatXOffset)
    {
        return seatXOffset == getTableRightCoordinates(table);
    }

    /* private Color generateRandomColor()
    {
        Random r = new Random();
        float red = (float) (r.nextFloat() / 2f + 0.5);
        float green = (float) (r.nextFloat() / 2f + 0.5);
        float blue = (float) (r.nextFloat() / 2f + 0.5);
        return new Color(red, green, blue);
    }*/
}