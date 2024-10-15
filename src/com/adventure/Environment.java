package com.adventure;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Environment {

  private char[][] map;
  private Map<String, EnumMap<Places, String>> data;

  enum Places {
    NAME, DESCRIPTION
  }

  public void loadData() {

    data = new HashMap<>();
    Path path = Path.of("com/adventure/places.txt");

    try {
      for (var line : Files.readAllLines(path)) {
        var lineAr = line.split("\\$");
        var lineMap = new EnumMap<Places, String>(Places.class);

        for (int i = 0; i < lineAr.length && i < Places.values().length; i++) {
          lineMap.put(Places.values()[i], lineAr[i]);
        }
        data.put(lineAr[0], lineMap);
      }
    } catch (IOException e) {
      System.out.println("Could ot read: " + path);
    }
  }

  public void generateMap(int xMax, int yMax) {


    for (int i = 0; i < xMax; i++) {
      for (int j = 0; j < yMax; j++) {

      }
    }
    map = new
  }

  public void earthQuake() {
    System.out.println("***A mighty earthquake shifts the tiles of the map!\n" +
      "You should reorientate yourself.**");
    for (var m : map) {
      Collections.shuffle(Arrays.asList(m));
    }
    Collections.shuffle(map);
    printMap();
  }
}
