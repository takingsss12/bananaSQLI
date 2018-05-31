package com.xmart.banana;

import java.util.stream.IntStream;

final class MapBuilder
{
  private final MapCoordinatesPositionHandler mapCoordinatesPositionHandler;
  
	private final MapSlot[][] mapSlots;
	
	MapBuilder(final String mapSize)
	{
		final MapCoordinatesRange parsedMapSize = MapCoordinatesRange.ofMapSize(mapSize);
		
		final int columns = parsedMapSize.getRightRange().iterator().next();
	    
	    mapSlots = IntStream.range(0, parsedMapSize.getLeftRange().iterator().next())
	        .mapToObj(row ->
	          IntStream.range(0, columns)
	            .mapToObj(__ -> new MapSlot())
	            .toArray(MapSlot[]::new))
	        .toArray(MapSlot[][]::new);
	    
	    mapCoordinatesPositionHandler = new DefaultMapCoordinatesPositionHandler();
	}
	
	MapBuilder horizontalFence(final String horizontalFence)
	{
		final MapCoordinatesRange parsedHorizontalFence = MapCoordinatesRange.ofHorizontalFence(horizontalFence);
		
		final int row = parsedHorizontalFence.getLeftRange().iterator().next();
		
		for (final int column : parsedHorizontalFence.getRightRange())
		{
			mapSlots[row - 1][column - 1].fence(Fence.HORIZONTAL_FENCE);
		}
		
		return this;
	}
	
	MapBuilder verticalFence(final String verticalFence)
	{
		final MapCoordinatesRange parsedHorizontalFence = MapCoordinatesRange.ofVerticalFence(verticalFence);
		
		final int column = parsedHorizontalFence.getRightRange().iterator().next();
		
		for (final int row : parsedHorizontalFence.getLeftRange())
		{
			mapSlots[row - 1][column - 1].fence(Fence.VERITCAL_FENCE);
		}
		
		return this;
	}
	
	Map build()
	{
		return new Map(mapSlots, mapCoordinatesPositionHandler);
	}
}
