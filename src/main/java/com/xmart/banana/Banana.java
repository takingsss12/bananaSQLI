package com.xmart.banana;

final class Banana
{
  private final Map map;
  
  private MapCoordinatesPositionHandler.Direction currentDirection;
  
  private MapCoordinatesRange currentPosition;
  
  Banana(final Map map, final String initialPosition)
  {
    this.map = map;
    
    currentDirection = MapCoordinatesPositionHandler.Direction.RIGHT;
    
    currentPosition = MapCoordinatesRange.ofRowAndColumn(initialPosition);
    
    this.map.getMapCoordinatesPositionHandler().setCurrentDirection(currentDirection);
    
    this.map.banana(this, currentPosition);
  }
  
  void move(final String step)
  {
    map.clear(currentPosition);
    
    map.banana(this, currentPosition = map.getMapCoordinatesPositionHandler().getNextMapCoordinatesRange(currentPosition, Integer.parseInt(step)));
  }
  
  void left()
  {
    this.map.getMapCoordinatesPositionHandler().setCurrentDirection(currentDirection = map.getMapCoordinatesPositionHandler().turnLeft());
  }
  
  void right()
  {
    this.map.getMapCoordinatesPositionHandler().setCurrentDirection(currentDirection = map.getMapCoordinatesPositionHandler().turnRight());
  }
  
  char draw()
  {
    return 'O';
  }
}
