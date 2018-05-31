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
    
    try
    {
      this.map.banana(this, currentPosition);
    }
    catch (final Exception exception)
    {
      
    }
  }
  
  void move(final String step) throws FenceExeption, OutOfBoundsExeption
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
