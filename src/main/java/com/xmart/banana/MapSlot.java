package com.xmart.banana;

import java.util.Optional;

final class MapSlot
{
  private Optional<Fence> fence;
  
  private Optional<Banana> banana;
  
  private Optional<Deamon> deamon;
  
  private Optional<Bonus> bonus;
  
  MapSlot()
  {
    fence = Optional.empty();
    
    banana = Optional.empty();
    
    deamon = Optional.empty();
    
    bonus = Optional.empty();
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
    
    bonus.ifPresent(Bonus::trigger);
    
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
  
  Deamon getDeamon()
  {
    return deamon.get();
  }
  
  void bonus(final Bonus bonus)
  {
    this.bonus = Optional.of(bonus);
  }
  
  private boolean triggerBonusCount(final Class<? extends Bonus> bonusClass)
  {
    return bonus.filter(bonusClass::isInstance).map(Bonus::status).orElse(false);
  }
  
  boolean triggerFreezer()
  {
    return triggerBonusCount(Freezer.class);
  }
  
  boolean triggerEnhancer()
  {
    return triggerBonusCount(Enhancer.class);
  }
  
  void clear()
  {
    banana = Optional.empty();
    deamon = Optional.empty();
    bonus = Optional.empty();
  }
  
  char draw()
  {
    return fence.map(Fence::draw)
        .orElse(banana.map(Banana::draw)
            .orElse(deamon.map(Deamon::draw)
                .orElse(bonus.map(Bonus::draw)
                    .orElse(' '))));
  }
}
