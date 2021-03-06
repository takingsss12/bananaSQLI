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
    
    final MapCoordinatesRange getIncrement()
    {
      return increment;
    }
    
    final boolean canReach(final MapCoordinatesRange source, final MapCoordinatesRange target, final int limit)
    {
      return Stream.iterate(source, previous -> MapCoordinatesRange.add(previous, getIncrement()))
          .limit(limit)
          .anyMatch(target::equals);
    }
    
    final boolean isHorizontal()
    {
      return Arrays.asList(RIGHT, LEFT).contains(this);
    }
  }
  
  void setCurrentDirection(final Direction currentDirection);
  
  Direction turnLeft();
  
  Direction turnRight();
  
  MapCoordinatesRange getNextMapCoordinatesRange(final MapCoordinatesRange currentMapCoordinatesRange, final int moveStep);
}
