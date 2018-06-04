package com.xmart.banana;

enum Fence
{
  HORIZONTAL_FENCE('-'),
  VERITCAL_FENCE('|');
  
  private final char repr;
  
  private Fence(final char repr)
  {
    this.repr = repr;
  }
  
  char draw()
  {
    return repr;
  }
}
