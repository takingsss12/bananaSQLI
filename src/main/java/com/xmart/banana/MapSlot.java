package com.xmart.banana;

import java.util.Optional;

final class MapSlot
{
  private Optional<Fence> fence;
  
  private Optional<Banana> banana;
  
  private Optional<Deamon> deamon;
  
  MapSlot()
  {
    fence = Optional.empty();
    
    banana = Optional.empty();
    
    deamon = Optional.empty();
  }
  
  void fence(final Fence fence)
  {
    this.fence = Optional.of(fence);
  }
  
  boolean isFenced()
  {
    return fence.isPresent();
  }
  
  void banana(final Banana banana)
  {
    if (isFenced())
    {
      throw new FenceExeption();
    }
    
    this.banana = Optional.of(banana);
  }
  
  boolean killBanana()
  {
    if (banana.isPresent())
    {
      banana.get().die();
      return true;
    }
    
    return false;
  }
  
  void deamon(final Deamon deamon)
  {
    this.deamon = Optional.of(deamon);
  }
  
  boolean isDeamonIn()
  {
    return deamon.isPresent();
  }
  
  public Deamon getDeamon()
  {
    return deamon.get();
  }
  
  void clear()
  {
    banana = Optional.empty();
    deamon = Optional.empty();
  }
  
  char draw()
  {
    return fence.map(Fence::draw)
        .orElse(banana.map(Banana::draw)
            .orElse(deamon.map(Deamon::draw)
                .orElse(' ')));
  }
}
