package concretecode;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.21.0.4666 modeling language!*/


import java.awt.Color;
import java.awt.Graphics;

// line 165 "gameplotconcrete.ump"
public class Shape
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Shape Attributes
  private int shapeId;
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
    shapeId = 0;
    left = aLeft;
    top = aTop;
    width = aWidth;
    height = aHeight;
    color = Color.white;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setShapeId(int aShapeId)
  {
    boolean wasSet = false;
    shapeId = aShapeId;
    wasSet = true;
    return wasSet;
  }

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

  public int getShapeId()
  {
    return shapeId;
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

  // line 177 "gameplotconcrete.ump"
  public void moveBy(int dx, int dy){
    left = left + dx;
	    top = top + dy;
  }

  // line 182 "gameplotconcrete.ump"
  public boolean containsPoint(int x, int y){
    if (x >= left && x < left+width && y >= top && y < top+height)
			return true;
		else
			return false;
  }

  // line 188 "gameplotconcrete.ump"
  public void draw(Graphics g){
    
  }


  public String toString()
  {
	  String outputString = "";
    return super.toString() + "["+
            "shapeId" + ":" + getShapeId()+ "," +
            "left" + ":" + getLeft()+ "," +
            "top" + ":" + getTop()+ "," +
            "width" + ":" + getWidth()+ "," +
            "height" + ":" + getHeight()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "color" + "=" + (getColor() != null ? !getColor().equals(this)  ? getColor().toString().replaceAll("  ","    ") : "this" : "null")
     + outputString;
  }
}