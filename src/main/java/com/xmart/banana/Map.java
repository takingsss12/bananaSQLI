package com.xmart.banana;

import java.util.Arrays;
import java.util.Collections;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

final class Map
{
  static final String LINE_SEPARATOR = String.format("%n");
  
  private final MapSlot[][] slots;

  private final MapCoordinatesPositionHandler bananaCoordinatesPositionHandler;
  
  Map(final MapSlot[][] slots, final MapCoordinatesPositionHandler mapCoordinatesPositionHandler)
  {
    this.slots = slots;
    
    this.bananaCoordinatesPositionHandler = mapCoordinatesPositionHandler;
  }
  
  int rows()
  {
    return slots.length;
  }
  
  int columns()
  {
    return slots[0].length;
  }
  
  private Deamon[] deamons()
  {
    return Arrays.stream(slots)
        .flatMap(Arrays::stream)
        .filter(MapSlot::isDeamonIn)
        .map(MapSlot::getDeamon)
        .toArray(Deamon[]::new);
  }
  
  private MapSlot getSlot(final MapCoordinatesRange position)
  {
    return slots[position.getLeftRange().iterator().next() - 1][position.getRightRange().iterator().next() - 1];
  }
  
  void banana(final Banana banana, final MapCoordinatesRange position)
  {
    if (!position.isWithin(rows(), columns()))
    {
      throw new OutOfBoundsExeption();
    }
    
    getSlot(position).banana(banana);
  }
  
  void deamon(final Deamon deamon, final MapCoordinatesRange position)
  {
    getSlot(position).deamon(deamon);
  }
  
  void clear(final MapCoordinatesRange position)
  {
    getSlot(position).clear();
  }
  
  String draw()
  {
    final String borders = String.join("", Collections.nCopies(columns() + 2, "-"));

    return String.format("%s%s%s%s%s", borders, LINE_SEPARATOR, Arrays.stream(slots)
        .map(row -> String.format("|%s|", Arrays.stream(row)
            .map(MapSlot::draw)
            .map(String::valueOf)
            .collect(Collectors.joining())))
        .collect(Collectors.joining(LINE_SEPARATOR)), LINE_SEPARATOR, borders);
  }
  
  MapCoordinatesPositionHandler getBananaCoordinatesPositionHandler()
  {
    return bananaCoordinatesPositionHandler;
  }
  
  boolean horizontalFenceInBetween(final int row, final int leftEdgeColumn, final int rightEdgeColumn)
  {
    return Arrays.stream(Arrays.copyOfRange(slots[row], leftEdgeColumn + 1, rightEdgeColumn))
        .anyMatch(MapSlot::isFenced);
  }
  
  boolean verticalFenceInBetween(final int column, final int topEdgeRow, final int bottomEdgeRow)
  {
    return Arrays.stream(slots)
        .map(row -> row[column])
        .skip(topEdgeRow + 1)
        .limit(bottomEdgeRow - topEdgeRow - 1)
        .anyMatch(MapSlot::isFenced);
  }
  
  boolean isFenced(final MapCoordinatesRange position)
  {
    return getSlot(position).isFenced();
  }
  
  void moveDeamons(final Banana banana, final Consumer<Banana> bananaMoveAction, final int bananaMoveStep)
  {
    if (banana.isDead())
    {
      throw new DeadBananaException();
    }
    
    final Deamon[] deamons = deamons();
    
    final BiConsumer<Supplier<Boolean>, Supplier<Boolean>> repeatMoveAction = (moveAction1, moveAction2) ->
    {
      IntStream.range(0, bananaMoveStep)
          .forEach(__ ->
          {
            if (moveAction1.get())
            {
              moveAction2.get();
            }
          });
    };
    
    final Supplier<Boolean> wrappedBananaMoveAction = () ->
    {
      bananaMoveAction.accept(banana);
      
      return true;
    };
    
    final Supplier<Boolean> deamonsMoveAction = () ->
    {
      return Arrays.stream(deamons)
        .map(deamon -> deamon.move(banana))
        .reduce(true, (accumulation, value) -> value && accumulation);
    };
    
    if (Arrays.stream(deamons).anyMatch(deamon -> deamon.canSeeBanana(banana.position())))
    {
      repeatMoveAction.accept(deamonsMoveAction, wrappedBananaMoveAction);
    }
    else
    {
      repeatMoveAction.accept(wrappedBananaMoveAction, deamonsMoveAction);
    }
  }
  
  boolean killBanana(final MapCoordinatesRange position)
  {
    return getSlot(position).killBanana();
  }
}
