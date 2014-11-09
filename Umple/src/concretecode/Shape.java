package concretecode;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.20.2.4305 modeling language!*/


import java.awt.Color;
import java.awt.Graphics;

// line 156 "gameplotconcrete.ump"
public class Shape
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Shape Attributes
  private int left;
  private int top;
  private int width;
  private int height;
  private Color color;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Shape(int aLeft, int aTop, int aWidth, int aHeight)
  {
    left = aLeft;
    top = aTop;
    width = aWidth;
    height = aHeight;
    color = Color.white;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setLeft(int aLeft)
  {
    boolean wasSet = false;
    left = aLeft;
    wasSet = true;
    return wasSet;
  }

  public boolean setTop(int aTop)
  {
    boolean wasSet = false;
    top = aTop;
    wasSet = true;
    return wasSet;
  }

  public boolean setWidth(int aWidth)
  {
    boolean wasSet = false;
    width = aWidth;
    wasSet = true;
    return wasSet;
  }

  public boolean setHeight(int aHeight)
  {
    boolean wasSet = false;
    height = aHeight;
    wasSet = true;
    return wasSet;
  }

  public boolean setColor(Color aColor)
  {
    boolean wasSet = false;
    color = aColor;
    wasSet = true;
    return wasSet;
  }

  public int getLeft()
  {
    return left;
  }

  public int getTop()
  {
    return top;
  }

  public int getWidth()
  {
    return width;
  }

  public int getHeight()
  {
    return height;
  }

  public Color getColor()
  {
    return color;
  }

  public void delete()
  {}

  // line 167 "gameplotconcrete.ump"
  public void reshape(int left, int top, int width, int height){
    // Set the position and size of this shape.
		this.left = left;
		this.top = top;
		this.width = width;
		this.height = height;
  }

  // line 175 "gameplotconcrete.ump"
  public void moveBy(int dx, int dy){
    // Move the shape by dx pixels horizontally and dy pixels vertically
		// (by changing the position of the top-left corner of the shape).
	    left += dx;
	    top += dy;
  }

  // line 187 "gameplotconcrete.ump"
  public boolean containsPoint(int x, int y){
    // Check whether the shape contains the point (x,y).
		// By default, this just checks whether (x,y) is inside the
		// rectangle that bounds the shape.  This method should be
		// overridden by a subclass if the default behavior is not
		// appropriate for the subclass.
		if (x >= left && x < left+width && y >= top && y < top+height)
			return true;
		else
			return false;
  }

  // line 198 "gameplotconcrete.ump"
  public void draw(Graphics g){
    
  }


  public String toString()
  {
	  String outputString = "";
    return super.toString() + "["+
            "left" + ":" + getLeft()+ "," +
            "top" + ":" + getTop()+ "," +
            "width" + ":" + getWidth()+ "," +
            "height" + ":" + getHeight()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "color" + "=" + (getColor() != null ? !getColor().equals(this)  ? getColor().toString().replaceAll("  ","    ") : "this" : "null")
     + outputString;
  }
}