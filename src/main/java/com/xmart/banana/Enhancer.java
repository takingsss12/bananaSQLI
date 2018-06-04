package com.xmart.banana;

final class Enhancer extends Bonus
{
  Enhancer()
  {
    super(10);
  }

  @Override
  char draw()
  {
    return '*';
  }
}
