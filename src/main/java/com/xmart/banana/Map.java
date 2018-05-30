package com.xmart.banana;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

final class Map
{
  static final String LINE_SEPARATOR = String.format("%n");
  
  private final MapSlot[][] slots;
  
  Map(final MapCoordinatesRange mapSize)
  {
    final int columns = mapSize.getRightRange().iterator().next();
    
    slots = IntStream.range(0, mapSize.getLeftRange().iterator().next())
        .mapToObj(row ->
          IntStream.range(0, columns)
            .mapToObj(__ -> new MapSlot())
            .toArray(MapSlot[]::new))
        .toArray(MapSlot[][]::new);
  }
  
  String draw()
  {
    final String borders = String.join("", Collections.nCopies(slots[0].length, "-"));

    return String.format("%s%s%s%s%s", borders, LINE_SEPARATOR, Arrays.stream(slots)
        .map(row -> String.format("|%s|", Arrays.stream(row)
            .map(MapSlot::draw)
            .map(String::valueOf)
            .collect(Collectors.joining())))
        .collect(Collectors.joining(LINE_SEPARATOR)), LINE_SEPARATOR, borders);
  }
}
