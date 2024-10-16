package com.adventure;

public enum Direction {
  NORTH, NORTHEAST, EAST, SOUTHEAST, SOUTH, SOUTHWEST, WEST, NORTHWEST;

  private static final int[][] directions = {
    {-1, 0},  // NORTH
    {-1, 1},  // NORTHEAST
    {0, 1},   // EAST
    {1, 1},   // SOUTHEAST
    {1, 0},   // SOUTH
    {1, -1},  // SOUTHWEST
    {0, -1},  // WEST
    {-1, -1}  // NORTHWEST
  };

  private static final String[] arrows = {
    "\u2191",  // NORTH: ↑
    "\u2197",  // NORTHEAST: ↗
    "\u2192",  // EAST: →
    "\u2198",  // SOUTHEAST: ↘
    "\u2193",  // SOUTH: ↓
    "\u2199",  // SOUTHWEST: ↙
    "\u2190",  // WEST: ←
    "\u2196"   // NORTHWEST: ↖
  };

  private static final String[] keys = {
    "w",  // NORTH: ↑
    "e",  // NORTHEAST: ↗
    "d",  // EAST: →
    "c",  // SOUTHEAST: ↘
    "s",  // SOUTH: ↓
    "y",  // SOUTHWEST: ↙
    "a",  // WEST: ←
    "q"   // NORTHWEST: ↖
  };

  public int[] getVector() {
    return directions[this.ordinal()];
  }

  public String getButton() {
    return keys[this.ordinal()];
  }

  public String getArrow() {
    return arrows[this.ordinal()];
  }
}