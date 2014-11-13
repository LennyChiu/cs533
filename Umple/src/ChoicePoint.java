/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.21.0.4666 modeling language!*/


import java.util.*;

// line 85 "gameplotconcrete.ump"
public class ChoicePoint extends StoryLineNode
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //ChoicePoint Associations
  private BlueCircle blueCircle;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public ChoicePoint(int aStoryLineNodeid, Position aPosition, BlueCircle aBlueCircle)
  {
    super(aStoryLineNodeid, aPosition);
    if (!setBlueCircle(aBlueCircle))
    {
      throw new RuntimeException("Unable to create ChoicePoint due to aBlueCircle");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public BlueCircle getBlueCircle()
  {
    return blueCircle;
  }

  public boolean setBlueCircle(BlueCircle aNewBlueCircle)
  {
    boolean wasSet = false;
    if (aNewBlueCircle != null)
    {
      blueCircle = aNewBlueCircle;
      wasSet = true;
    }
    return wasSet;
  }

  public void delete()
  {
    blueCircle = null;
    super.delete();
  }

}