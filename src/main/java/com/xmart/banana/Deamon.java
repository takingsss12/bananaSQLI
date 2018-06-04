package com.xmart.banana;

import java.util.Arrays;
import java.util.function.Predicate;

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
  
  boolean move(final Banana banana)
  {
    final Runnable moveAction = () ->
    {
      map.clear(currentPosition);
      
      currentPosition = positionHandler.getNextMapCoordinatesRange(currentPosition, 1);
      
      map.deamon(this, currentPosition);
    };
    
    if (Arrays.stream(currentPosition.neighbors())
        .filter(neighbor -> neighbor.isWithin(map.rows(), map.columns()))
        .anyMatch(map::killBanana))
    {
      return false;
    }
    
    if (!canSeeBanana(banana.position()))
    {
      final MapCoordinatesRange nextPosition = MapCoordinatesRange.add(currentPosition, currentDirection.getIncrement());
      
      if (!nextPosition.isWithin(map.rows(), map.columns()) || map.isFenced(nextPosition))
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
      Predicate<MapCoordinatesPositionHandler.Direction> canReach = direction -> direction.canReach(
          currentPosition,
          banana.position(),
          direction.isHorizontal() ? map.columns() : map.rows()
      );
      
      if (canReach.test(currentDirection))
      {
        moveAction.run();
      }
      else if (canReach.test(currentDirection = positionHandler.turnRight()))
      {
        
      }
      else if(canReach.test(currentDirection = positionHandler.turnLeft()))
      {
        
      }
      else
      {
        currentDirection = positionHandler.turnRight();
      }
      
      positionHandler.setCurrentDirection(currentDirection);
    }
    
    return true;
  }
  
  char draw()
  {
    return 'X';
  }
}
