package com.xmart.banana;

import java.util.Optional;

final class MapSlot
{
  private Optional<Fence> fence;
  
  MapSlot()
  {
    fence = Optional.empty();
  }
  
  void fence(final Fence fence)
  {
    this.fence = Optional.of(fence);
  }
  
  char draw()
  {
    return fence.map(Fence::draw).orElse(' ');
  }
}
