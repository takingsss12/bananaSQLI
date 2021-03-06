package com.xmart.banana;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.IntFunction;
import java.util.function.ToIntBiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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
  
  static MapCoordinatesRange ofIncrements(final int rowIncrement, final int columnIncrement)
  {
    final IntFunction<List<Integer>> singletonList = n -> Collections.unmodifiableList(Arrays.asList(n));
    
    return new MapCoordinatesRange(singletonList.apply(rowIncrement), singletonList.apply(columnIncrement));
  }
  
  static MapCoordinatesRange[] ofDeamons(final String deamonsInitialPosition)
  {
    return Arrays.stream(deamonsInitialPosition.split("-"))
        .map(MapCoordinatesRange::ofMapSize)
        .toArray(MapCoordinatesRange[]::new);
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
  
  static MapCoordinatesRange ofRowAndColumn(final String rowAndColumn)
  {
    return ofMapSize(rowAndColumn);
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
  
  static MapCoordinatesRange ofBonus(final String bonusPosition)
  {
    return ofRowAndColumn(bonusPosition.replace('-', ','));
  }
  
  static MapCoordinatesRange add(final MapCoordinatesRange first, final MapCoordinatesRange second)
  {
    final int left = first.getLeftRange().iterator().next() + second.getLeftRange().iterator().next();
    
    final int right = first.getRightRange().iterator().next() + second.getRightRange().iterator().next();
    
    return ofIncrements(left, right);
  }
  
  static MapCoordinatesRange mult(final MapCoordinatesRange coordinates, final int factor)
  {
    final int left = coordinates.getLeftRange().iterator().next() * factor;
    
    final int right = coordinates.getRightRange().iterator().next() * factor;
    
    return ofIncrements(left, right);
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
  
  boolean isWithin(final int rows, final int columns)
  {
    ToIntBiFunction<Iterable<Integer>, BiFunction<Stream<Integer>, Comparator<Integer>, Optional<Integer>>> boundaryElement = (
        iterable, operation) -> operation
            .apply(StreamSupport.stream(iterable.spliterator(), false), Comparator.naturalOrder())
            .get();

    final boolean validRows = boundaryElement.applyAsInt(getLeftRange(), Stream::min) >= 1
        && boundaryElement.applyAsInt(getLeftRange(), Stream::max) <= rows;

    final boolean validColumns = boundaryElement.applyAsInt(getRightRange(), Stream::min) >= 1
        && boundaryElement.applyAsInt(getRightRange(), Stream::max) <= columns;

    return validRows && validColumns;
  }
  
  MapCoordinatesRange[] neighbors()
  {
    final int row = getLeftRange().iterator().next();
    
    final int column = getRightRange().iterator().next();
    
    return new MapCoordinatesRange[]
    {
        ofIncrements(row, column - 1),
        ofIncrements(row, column + 1),
        ofIncrements(row - 1, column),
        ofIncrements(row + 1, column)
    };
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((leftRange == null) ? 0 : leftRange.hashCode());
    result = prime * result + ((rightRange == null) ? 0 : rightRange.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    MapCoordinatesRange other = (MapCoordinatesRange) obj;
    if (leftRange == null)
    {
      if (other.leftRange != null)
        return false;
    } else if (!leftRange.equals(other.leftRange))
      return false;
    if (rightRange == null)
    {
      if (other.rightRange != null)
        return false;
    } else if (!rightRange.equals(other.rightRange))
      return false;
    return true;
  }
}
