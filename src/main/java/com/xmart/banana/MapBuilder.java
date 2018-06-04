package com.xmart.banana;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.IntStream;

final class MapBuilder
{
  private final MapCoordinatesPositionHandler mapCoordinatesPositionHandler;
  
	private final MapSlot[][] mapSlots;
	
	private final Map map;
	
	private final Supplier<? extends MapCoordinatesPositionHandler> defaultPositionHandler = DefaultMapCoordinatesPositionHandler::new;
	
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
	    
	    mapCoordinatesPositionHandler = defaultPositionHandler.get();
	    
	    map = new Map(mapSlots, mapCoordinatesPositionHandler);
	}
	
	void setMapSlot(final MapCoordinatesRange mapSlotPosition, final Consumer<MapSlot> action)
	{
	  final int row = mapSlotPosition.getLeftRange().iterator().next() - 1;
    
    final int column = mapSlotPosition.getRightRange().iterator().next() - 1;
    
    action.accept(mapSlots[row][column]);
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
	
	MapBuilder deamon(final String deamonsInitialPosition)
	{
    Arrays.stream(MapCoordinatesRange.ofDeamons(deamonsInitialPosition))
        .forEach(deamonInitialPosition ->
        {
          setMapSlot(deamonInitialPosition, mapSlot -> mapSlot.deamon(new Deamon(map, deamonInitialPosition, defaultPositionHandler.get())));
        });
	  
	  return this;
	}
	
	private MapBuilder bonus(final String bonusPosition, final Supplier<? extends Bonus> bonusSupplier)
  {
    setMapSlot(MapCoordinatesRange.ofBonus(bonusPosition), mapSlot -> mapSlot.bonus(bonusSupplier.get()));
    
    return this;
  }
	
	MapBuilder freezer(final String freezerPosition)
	{
	  return bonus(freezerPosition, Freezer::new);
	}
	
	MapBuilder enhancer(final String enhancerPosition)
  {
    return bonus(enhancerPosition, Enhancer::new);
  }
	
	Map build()
	{
		return map;
	}
}
