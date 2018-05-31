package com.xmart.banana;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

final class MapCoordinatesRange
{
  private static MapCoordinatesRange of(final String range)
  {
    final String[] rangeTokens = range.split(",");
    
    final String[] leftRange = rangeTokens[0].split("-");
    
    final int leftRangeStart = Integer.parseInt(leftRange[0]);
    
    final int leftRangeEnd = Integer.parseInt(leftRange[1]);
    
    final String[] rightRange = rangeTokens[1].split("-");
    
    final int rightRangeStart = Integer.parseInt(rightRange[0]);
    
    final int rightRangeEnd = Integer.parseInt(rightRange[1]);
    
    BiFunction<Integer, Integer, List<Integer>> toUnmodifiableList = (n, m) -> Collections.unmodifiableList(
          IntStream.rangeClosed(n, m)
            .boxed()
            .collect(Collectors.toList())
    );
    
    return new MapCoordinatesRange(
        toUnmodifiableList.apply(leftRangeStart, leftRangeEnd),
        toUnmodifiableList.apply(rightRangeStart, rightRangeEnd)
    );
  }
  
  private static List<String> matcherGroupsToList(final Matcher matcher)
  {
    return Collections.unmodifiableList(Optional.ofNullable(matcher.find())
        .filter(Boolean::valueOf)
        .map(__ -> IntStream.rangeClosed(1, matcher.groupCount())
            .mapToObj(matcher::group)
            .collect(Collectors.toList()))
        .orElse(Collections.emptyList()));
  }
  
  static MapCoordinatesRange ofMapSize(final String mapSize)
  {
    final List<String> groups = matcherGroupsToList(Pattern.compile("(\\d+),(\\d+)").matcher(mapSize));
    
    return of(String.format("%s-%s,%s-%s", groups.get(0), groups.get(0), groups.get(1), groups.get(1)));
  }
  
  static MapCoordinatesRange ofHorizontalFence(final String horizontalFence)
  {
    final List<String> groups = matcherGroupsToList(Pattern.compile("(\\d+),(\\d+)-(\\d+)").matcher(horizontalFence));
    
    return of(String.format("%s-%s,%s-%s", groups.get(0), groups.get(0), groups.get(1), groups.get(2)));
  }
  
  static MapCoordinatesRange ofVerticalFence(final String verticalFence)
  {
    final List<String> groups = matcherGroupsToList(Pattern.compile("(\\d+)-(\\d+),(\\d+)").matcher(verticalFence));
    
    return of(String.format("%s-%s,%s-%s", groups.get(0), groups.get(1), groups.get(2), groups.get(2)));
  }
  
  private final Iterable<Integer> leftRange;
  private final Iterable<Integer> rightRange;
  
  private MapCoordinatesRange(Iterable<Integer> leftRange, Iterable<Integer> rightRange)
  {
    this.leftRange = leftRange;
    this.rightRange = rightRange;
  }

  Iterable<Integer> getLeftRange()
  {
    return leftRange;
  }

  Iterable<Integer> getRightRange()
  {
    return rightRange;
  }
}
