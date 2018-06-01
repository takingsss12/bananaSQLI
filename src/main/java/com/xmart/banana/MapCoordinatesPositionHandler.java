package com.xmart.banana;

import java.util.Arrays;
import java.util.stream.Stream;

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
    
    boolean canReach(final MapCoordinatesRange target, final int limit)
    {
      return Stream.iterate(getIncrement(), previous -> MapCoordinatesRange.add(previous, getIncrement()))
          .limit(limit)
          .anyMatch(target::equals);
    }
    
    boolean isHorizontal()
    {
      return Arrays.asList(RIGHT, LEFT).contains(this);
    }
  }
  
  void setCurrentDirection(final Direction currentDirection);
  
  Direction turnLeft();
  
  Direction turnRight();
  
  MapCoordinatesRange getNextMapCoordinatesRange(final MapCoordinatesRange currentMapCoordinatesRange, final int moveStep);
}
