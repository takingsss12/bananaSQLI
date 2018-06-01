package com.xmart.banana;

import java.util.Arrays;

final class Deamon
{
  private final Map map;
  
  private MapCoordinatesRange currentPosition;
  
  private MapCoordinatesPositionHandler.Direction currentDirection;
  
  private final MapCoordinatesPositionHandler positionHandler;
  
  Deamon(Map map, MapCoordinatesRange currentPosition, MapCoordinatesPositionHandler positionHandler)
  {
    this.map = map;
    
    this.currentPosition = currentPosition;
    
    currentDirection = MapCoordinatesPositionHandler.Direction.RIGHT;
    
    this.positionHandler = positionHandler;
    
    this.positionHandler.setCurrentDirection(currentDirection);
  }

  boolean canSeeBanana(final MapCoordinatesRange bananaPosition)
  {
    final int row = currentPosition.getLeftRange().iterator().next();
    
    final int bananaRow = bananaPosition.getLeftRange().iterator().next();
    
    final int column = currentPosition.getRightRange().iterator().next();
    
    final int bananaColumn = bananaPosition.getRightRange().iterator().next();
    
    if (row == bananaRow)
    {
      return !map.horizontalFenceInBetween(row - 1, Math.min(column - 1, bananaColumn - 1), Math.max(column - 1, bananaColumn - 1));
    }
    else if (column == bananaColumn)
    {
      return !map.verticalFenceInBetween(column - 1, Math.min(row - 1, bananaRow - 1), Math.max(row - 1, bananaRow - 1));
    }
    
    return false;
  }
  
  MapCoordinatesPositionHandler.Direction faceBanana(final MapCoordinatesRange bananaPosition)
  {
    return null;
  }
  
  void move(final Banana banana)
  {
    final Runnable moveAction = () ->
    {
      map.clear(currentPosition);
      
      currentPosition = positionHandler.getNextMapCoordinatesRange(currentPosition, 1);
      
      map.deamon(this, currentPosition);
    };
    
    if (!canSeeBanana(banana.position()))
    {
      if (map.isFenced(MapCoordinatesRange.add(currentPosition, currentDirection.getIncrement())))
      {
        positionHandler.setCurrentDirection(currentDirection = positionHandler.turnRight());
      }
      else
      {
        moveAction.run();
      }
    }
    else
    {
      if (currentDirection.canReach(banana.position(), currentDirection.isHorizontal() ? map.columns() : map.rows()))
      {
        moveAction.run();
      }
      else
      {
        positionHandler.setCurrentDirection(currentDirection = faceBanana(banana.position()));
      }
    }
    
    Arrays.stream(currentPosition.neighbors())
        .filter(neighbor -> neighbor.isWithin(map.rows(), map.columns()))
        .forEach(map::killBanana);
  }
  
  char draw()
  {
    return 'X';
  }
}
