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

  public int[] getVector() {
    return directions[this.ordinal()];
  }
}