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
  
  void banana(final Banana banana, final MapCoordinatesRange position)
  {
    slots[position.getLeftRange().iterator().next() - 1][position.getRightRange().iterator().next() - 1].banana(banana);
  }
  
  void clear(final MapCoordinatesRange position)
  {
    slots[position.getLeftRange().iterator().next() - 1][position.getRightRange().iterator().next() - 1].clear();
  }
  
  String draw()
  {
    final String borders = String.join("", Collections.nCopies(slots[0].length + 2, "-"));

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
