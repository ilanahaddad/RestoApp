
public class testtable {

	private String name;
	private int x;
	private int y;
	private int h;
	private int w;
	
	public testtable(int startx, int starty, int startw, int starth, String startname)	{
		name = startname;
		x = startx;
		y = starty;
		h = starth;
		w = startw;
	}
	
	public void MoveTable(int NewX, int NewY) {
		x = NewX;
		y = NewY;
	}
	
	public void ChangeSize(int NewW, int NewH) {
		w = NewW;
		h = NewH;
	}	
	
	public String getName() {
		return name;
	}
	
	public int getBottomLeftX() {
		return x;
	}
	
	public int getBottomLeftY() {
		return y;
	}
	
	public int getWidth() {
		return w;
	}
	
	public int getHeight() {
		return h;
	}
	
	public int getTopRightX() {
		return (x+w);
	}
	
	public int getTopRightY() {
		return (y+h);
	}
}
