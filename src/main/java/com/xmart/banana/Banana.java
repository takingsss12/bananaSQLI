package com.xmart.banana;

import java.util.Arrays;

final class Banana
{
  private final Map map;
  
  private MapCoordinatesPositionHandler.Direction currentDirection;
  
  private MapCoordinatesRange currentPosition;
  
  private boolean isDead = false;
  
  Banana(final Map map, final String initialPosition)
  {
    this.map = map;
    
    currentDirection = MapCoordinatesPositionHandler.Direction.RIGHT;
    
    currentPosition = MapCoordinatesRange.ofRowAndColumn(initialPosition);
    
    this.map.getBananaCoordinatesPositionHandler().setCurrentDirection(currentDirection);
    
    this.map.banana(this, currentPosition);
  }
  
  void killAdjacentDeamons()
  {
    map.killDeamons(Arrays.stream(currentPosition.neighbors())
        .filter(position -> position.isWithin(map.rows(), map.columns()))
        .toArray(MapCoordinatesRange[]::new));
  }
  
  private void move()
  {
    map.clear(currentPosition);
    
    map.banana(this, currentPosition = map.getBananaCoordinatesPositionHandler().getNextMapCoordinatesRange(currentPosition, 1));
  }
  
  private void leftImpl()
  {
    this.map.getBananaCoordinatesPositionHandler().setCurrentDirection(currentDirection = map.getBananaCoordinatesPositionHandler().turnLeft());
  }
  
  private void rightImpl()
  {
    this.map.getBananaCoordinatesPositionHandler().setCurrentDirection(currentDirection = map.getBananaCoordinatesPositionHandler().turnRight());
  }
  
  void move(final String step)
  {
    map.moveDeamons(this, Banana::move, Integer.parseInt(step));
  }
  
  void left()
  {
    map.moveDeamons(this, Banana::leftImpl, 1);
  }
  
  void right()
  {
    map.moveDeamons(this, Banana::rightImpl, 1);
  }
  
  MapCoordinatesRange position()
  {
    return currentPosition;
  }
  
  void die()
  {
    isDead = true;
  }
  
  boolean isDead()
  {
    return isDead;
  }
  
  char draw()
  {
    return isDead ? ' ' : 'O';
  }
}
