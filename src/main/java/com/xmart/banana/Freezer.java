package com.xmart.banana;

final class Freezer extends Bonus
{
  Freezer()
  {
    super(5);
  }

  @Override
  char draw()
  {
    return 'F';
  }
}
