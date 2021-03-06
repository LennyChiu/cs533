package concretecode;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.21.0.4666 modeling language!*/


import java.util.*;
import java.util.*;

// line 77 "gameplotconcrete.ump"
public class Trigger extends StoryLineNode
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Trigger Associations
  private Mission mission;
  private BlackTriangle blackTriangle;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Trigger(Position aPosition, Mission aMission, BlackTriangle aBlackTriangle)
  {
    super(aPosition);
    if (!setMission(aMission))
    {
      throw new RuntimeException("Unable to create Trigger due to aMission");
    }
    if (!setBlackTriangle(aBlackTriangle))
    {
      throw new RuntimeException("Unable to create Trigger due to aBlackTriangle");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public Mission getMission()
  {
    return mission;
  }

  public BlackTriangle getBlackTriangle()
  {
    return blackTriangle;
  }

  public boolean setMission(Mission aNewMission)
  {
    boolean wasSet = false;
    if (aNewMission != null)
    {
      mission = aNewMission;
      wasSet = true;
    }
    return wasSet;
  }

  public boolean setBlackTriangle(BlackTriangle aNewBlackTriangle)
  {
    boolean wasSet = false;
    if (aNewBlackTriangle != null)
    {
      blackTriangle = aNewBlackTriangle;
      wasSet = true;
    }
    return wasSet;
  }

  public void delete()
  {
    mission = null;
    blackTriangle = null;
    super.delete();
  }

}