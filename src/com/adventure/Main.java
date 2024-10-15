package com.adventure;

public class Main {
  public static void main(String[] args) {

    Environment env = new Environment();
    env.loadPlaces(); // loads places with descriptions

//    env.generateMap(5, 5);
//    env.printMap();
//    env.saveMap();
//
//    env.earthQuake();
    env.loadMapFromFile();
    env.printMap();


  }
}