package com.xmart.banana;

interface MapCoordinatesPositionHandler
{
  enum Direction
  {
    RIGHT(MapCoordinatesRange.ofIncrements(0, 1)),
    BOTTOM(MapCoordinatesRange.ofIncrements(1, 0)),
    LEFT(MapCoordinatesRange.ofIncrements(0, -1)),
    TOP(MapCoordinatesRange.ofIncrements(-1, 0));
    
    private final MapCoordinatesRange increment;
    
    private Direction(final MapCoordinatesRange increment)
    {
      this.increment = increment;
    }
    
    MapCoordinatesRange getIncrement()
    {
      return increment;
    }
  }
  
  void setCurrentDirection(final Direction currentDirection);
  
  Direction turnLeft();
  
  Direction turnRight();
  
  MapCoordinatesRange getNextMapCoordinatesRange(final MapCoordinatesRange currentMapCoordinatesRange, final int moveStep);
}
