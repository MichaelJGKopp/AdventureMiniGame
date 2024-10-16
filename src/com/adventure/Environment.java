package com.adventure;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;

public class Environment {

  private char[][] playingField;
  private char[][] playingFieldWithPlayer;
  private Map<String, EnumMap<Places, String>> placesData;
  private final Random random = new Random();
  private final Path mapPath = Path.of("map.txt");
  private int[] playerPosition = {0, 0};

  enum Places {
    NAME, DESCRIPTION, SYMBOL
  }

  public void printSurroundingPlaces() {

    if (playingField == null || playingField[0] == null) {
      System.out.println("Map not loaded yet. Can not print surrounding places.");
      return;
    }

    System.out.println("Looking around you, you see in the far distance ...");

    int x = playerPosition[0];
    int y = playerPosition[1];

    for (Direction dir : Direction.values()) {
      int[] vector = dir.getVector();
      int newX = playerPosition[0] + vector[0];
      int newY = playerPosition[1] + vector[1];
      if (newX < 0 || newX > playingField.length - 1
        || newY < 0 || newY > playingField[0].length - 1) {
        continue;
      }
      System.out.printf("%s(%s, \"%s\"): %s%n",
        dir, dir.getArrow(), dir.getButton(), getPlaceKey(newX, newY));
    }
    System.out.println();
  }



  public void printCurrentPlace() {
    printPlace(playerPosition[0], playerPosition[1]);
  }

  public void printPlace(int x, int y) {

    var locationName = getPlaceKey(x, y);
    System.out.println("You arrive at a " + locationName + ".");
    System.out.println(getPlaceDescription(x, y));
    System.out.println();
  }

  public String getPlaceDescription(int x, int y) {
    return getPlaceData(x, y, Places.DESCRIPTION).replace("\\n", "\n");
  }

  public String getPlaceData(int x, int y, Places places) {
    var placeData = placesData.get(getPlaceKey(x, y));
    return placeData.getOrDefault(places, "Place data not found.");
  }

  public String addSymbolsToMap(String mapString) {

    for (var entry : placesData.entrySet()) {
      String placeName = entry.getKey().trim();
      String symbol = entry.getValue().get(Places.SYMBOL);

      if (symbol != null) {
        mapString = mapString.replace(placeName.substring(0, 1).toUpperCase() + " ", symbol);
        mapString = mapString.replace(placeName.substring(0, 1).toUpperCase(), symbol);
      }
    }
    return mapString;
  }

  public String getPlaceKey(int x, int y) {

    if (playingField == null || x >= playingField.length || x < 0
      || playingField[0] == null || y >= playingField[0].length || y < 0) {
      System.out.println("Can not reach place outside of map.");
      return null;
    }

    char placeChar = playingField[x][y];
    var locationName = placesData.keySet().stream()
      .filter(s -> s.charAt(0) == placeChar)
      .collect(Collectors.joining());
    return locationName;
  }

  public void loadPlaces() {

    placesData = new HashMap<>();
    Path path = Path.of("places.txt");

    try {
      for (var line : Files.readAllLines(path)) {
        var lineAr = line.split("\\$");
        var lineMap = new EnumMap<Places, String>(Places.class);

        for (int i = 0; i < lineAr.length && i < Places.values().length; i++) {
          lineMap.put(Places.values()[i], lineAr[i]);
        }
        placesData.put(lineAr[0], lineMap);
      }
    } catch (IOException e) {
      System.out.println("Could not read: " + path);
    }
  }

  public void generateMap(int xMax, int yMax) {

    if (xMax < 1 || yMax < 1) {
      System.out.println("Generated playing field can not be smaller than 1 tile.");
    }

    var playingFieldSymbols = placesData.keySet().stream()
      .map(s -> s.charAt(0))
      .toList();
    playingField = new char[xMax][yMax];

    for (int i = 0; i < xMax; i++) {
      for (int j = 0; j < yMax; j++) {
        playingField[i][j] = playingFieldSymbols.get(
          random.nextInt(0, playingFieldSymbols.size()));
      }
    }

    updatePlayerPosition();
  }

  public void saveMap() {

    try {
      Files.writeString(mapPath, getMapString(),
        StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    } catch (IOException e) {
      System.out.println("Could not write save data to file.");
    }
  }

  public void loadMapFromFile() {

    try {
      List<String> lines = Files.readAllLines(mapPath);
      char[][] playingFieldNew = new char[lines.size()][];
      int i = 0, j = 0;
      for (String line : lines) {
        playingFieldNew[i++] = line.replace(" ", "").toCharArray();
      }
      playingField = playingFieldNew;
      updatePlayerPosition();
    } catch (IOException e) {
      System.out.println("Could not load map from file.");
    }
  }

  public void earthQuake() {

    System.out.println("***A mighty earthquake shifts the tiles of the map!\n" +
      "You should reorientate yourself.**");
    for (var m : playingField) {
      Collections.shuffle(Arrays.asList(m));
    }
    Collections.shuffle(Arrays.asList(playingField));
    printMap();
  }

  public void printMap() {

    if (playingField == null) {
      System.out.println("Could not print. First initialize a map.");
      return;
    }

    System.out.println("Map");
    System.out.println("-------------------");
    System.out.println(getMapString(true));
    System.out.println("-------------------");
    System.out.println();
  }

  public boolean gameEnd() {

    if(playerPosition[0] == playingField.length - 1 && playingField[1] != null &&
       playerPosition[1] == playingField[playerPosition[0]].length - 1) {
      return true;
    }
    return false;
  }

  public boolean movePlayer(Direction dir) {

    int x = playerPosition[0] + dir.getVector()[0];
    int y = playerPosition[1] + dir.getVector()[1];

    if (playingField == null || x >= playingField.length || x < 0
      || playingField[0] == null || y >= playingField[x].length || y < 0) {
      System.out.println("Can not reach place outside of map.");
      return false;
    }
    playerPosition[0] = x;
    playerPosition[1] = y;
    return true;
  }

  public void updatePlayerPosition() {

    playingFieldWithPlayer = deepCopy2DArray(playingField);
    playingFieldWithPlayer[playerPosition[0]][playerPosition[1]] = '☺';

    int xMax = playingField.length - 1;
    int yMax = playingField[xMax].length - 1;
    playingFieldWithPlayer[xMax][yMax] = '!';
  }

  private char[][] deepCopy2DArray(char[][] original) {

    if (original == null) {
      return null;
    }

    char[][] copy = new char[original.length][];
    for (int i = 0; i < original.length; i++) {
      copy[i] = Arrays.copyOf(original[i], original[i].length);
    }
    return copy;
  }

  private String getMapString() {
    return getMapString(false);
  }

  private String getMapString(boolean withPlayer) {

    if (playingField == null) {
      return "";
    }

    if (withPlayer) {
      updatePlayerPosition();
    }
    String result = Arrays.stream(withPlayer ? playingFieldWithPlayer : playingField)
      .map(row -> new String(row).replace("", "  ").trim())
      .collect(Collectors.joining("\n"));

    if (withPlayer) {
      return addSymbolsToMap(result)
        .replace("☺ ", "🧙")
        .replace("☺", "🧙")
        .replace("!", "💎");
    } else {
      return result;
    }
//      .replace("F ", "🌲")
//      .replace("L ", "🔥")
//      .replace("T ", "🏠")
//      .replace("V ", "🚪")
//      .replace("W ", "🌊")
//
//      .replace("S ", "🧟")
//      .replace("R ", "🐉")
  }
}
