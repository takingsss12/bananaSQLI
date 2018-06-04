package com.xmart.banana;

abstract class Bonus
{
  private boolean isTriggered;
  
  private int availabilityCounter;
  
  private int availabilityLimit;
  
  Bonus(final int availabilityLimit)
  {
    isTriggered = false;
    
    availabilityCounter = 0;
    
    this.availabilityLimit = availabilityLimit;
  }
  
  final void trigger()
  {
    isTriggered = true;
  }
  
  final boolean status()
  {
    return isTriggered && availabilityCounter++ < availabilityLimit;
  }
  
  abstract char draw();
}
