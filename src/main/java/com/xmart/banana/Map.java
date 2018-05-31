package com.xmart.banana;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

final class Map
{
  static final String LINE_SEPARATOR = String.format("%n");
  
  private final MapSlot[][] slots;

  private final MapCoordinatesPositionHandler mapCoordinatesPositionHandler;
  
  Map(final MapSlot[][] slots, final MapCoordinatesPositionHandler mapCoordinatesPositionHandler)
  {
    this.slots = slots;
    
    this.mapCoordinatesPositionHandler = mapCoordinatesPositionHandler;
  }
  
  private int rows()
  {
    return slots.length;
  }
  
  private int columns()
  {
    return slots[0].length;
  }
  
  void banana(final Banana banana, final MapCoordinatesRange position) throws FenceExeption, OutOfBoundsExeption
  {
    if (!position.isWithin(rows(), columns()))
    {
      throw new OutOfBoundsExeption();
    }
    
    slots[position.getLeftRange().iterator().next() - 1][position.getRightRange().iterator().next() - 1].banana(banana);
  }
  
  void clear(final MapCoordinatesRange position)
  {
    slots[position.getLeftRange().iterator().next() - 1][position.getRightRange().iterator().next() - 1].clear();
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
  
  MapCoordinatesPositionHandler getMapCoordinatesPositionHandler()
  {
    return mapCoordinatesPositionHandler;
  }
}
