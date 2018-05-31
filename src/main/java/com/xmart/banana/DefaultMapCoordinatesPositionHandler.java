package com.xmart.banana;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;

final class DefaultMapCoordinatesPositionHandler implements MapCoordinatesPositionHandler
{

  private Direction currentDirection;
  
  private final List<MapCoordinatesPositionHandler.Direction> directionsAsList;
  
  DefaultMapCoordinatesPositionHandler()
  {
    directionsAsList = Arrays.asList(MapCoordinatesPositionHandler.Direction.values());
  }

  @Override
  public void setCurrentDirection(Direction currentDirection)
  {
    this.currentDirection = currentDirection;
  }
  
  private MapCoordinatesPositionHandler.Direction turn(final IntUnaryOperator nextIndex, final IntPredicate isValidNextIndex, final int validIndexIfNot)
  {
    return currentDirection = directionsAsList.get(
        Optional.of(nextIndex.applyAsInt(directionsAsList.indexOf(currentDirection)))
            .filter(isValidNextIndex::test)
            .orElse(validIndexIfNot)
    );
  }

  @Override
  public MapCoordinatesPositionHandler.Direction turnLeft()
  {
    return turn(index -> index - 1, index -> index >= 0, directionsAsList.size() - 1);
  }

  @Override
  public MapCoordinatesPositionHandler.Direction turnRight()
  {
    return turn(index -> index + 1, index -> index < directionsAsList.size(), 0);
  }

  @Override
  public MapCoordinatesRange getNextMapCoordinatesRange(MapCoordinatesRange currentMapCoordinatesRange, int moveStep)
  {
    return MapCoordinatesRange.add(currentMapCoordinatesRange, MapCoordinatesRange.mult(currentDirection.getIncrement(), moveStep));
  }

}
