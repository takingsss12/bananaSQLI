package com.xmart.banana;

import java.util.Optional;

final class MapSlot
{
  private Optional<Fence> fence;
  
  private Optional<Banana> banana;
  
  MapSlot()
  {
    fence = Optional.empty();
    
    banana = Optional.empty();
  }
  
  void fence(final Fence fence)
  {
    this.fence = Optional.of(fence);
  }
  
  void banana(final Banana banana)
  {
    this.banana = Optional.of(banana);
  }
  
  void clear()
  {
    banana = Optional.empty();
  }
  
  char draw()
  {
    return fence.map(Fence::draw).orElse(banana.map(Banana::draw).orElse(' '));
  }
}
