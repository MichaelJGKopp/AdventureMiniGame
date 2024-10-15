package com.adventure;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class Environment {

  private char[][] playingField;
  private Map<String, EnumMap<Places, String>> data;
  private final Random random = new Random();

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

    if (xMax < 1 || yMax < 1) {
      System.out.println("Generated playing field can not be smaller than 1 tile.");
    }

    var playingFieldSymbols = data.keySet().stream()
      .map(s -> s.charAt(0))
      .toList();
    playingField = new char[xMax][yMax];

    for (int i = 0; i < xMax; i++) {
      for (int j = 0; j < yMax; j++) {
        playingField[i][j] = playingFieldSymbols.get(
          random.nextInt(0, playingFieldSymbols.size()));
      }
    }
  }

  public void saveMap() {

    Path path = Path.of("com/adventure/map.txt");
    var saveData = Arrays.stream(playingField)
      .map(String::valueOf)
      .map(s -> String.join("\n", s))
      .toString();

    try {
      Files.writeString(path, saveData);
    } catch (IOException e) {
      System.out.println("Could not write save data to file.");
    }
  }

  public void earthQuake() {
    System.out.println("***A mighty earthquake shifts the tiles of the map!\n" +
      "You should reorientate yourself.**");
    for (var m : playingField) {
      Collections.shuffle(Arrays.asList(m));
    }
    Collections.shuffle(playingField);
    printMap();
  }
}
