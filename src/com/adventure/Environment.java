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
    NAME, DESCRIPTION
  }

  public void printCurrentPlace() {
    printPlace(playerPosition[0], playerPosition[1]);
  }

  public void printPlace(int x, int y) {

    var locationName = getPlaceKey(x, y);
    System.out.println("You arrive at a " + locationName + ".");
    System.out.println(getPlaceDescription(x, y));
  }

  public String getPlaceDescription(int x, int y) {
    return getPlaceData(x, y, Places.DESCRIPTION);
  }

  public String getPlaceData(int x, int y, Places places) {

    var placeData = placesData.get(getPlaceKey(x, y));
    return placeData.getOrDefault(places, "Place data not found.");
  }

  public String getPlaceKey(int x, int y) {

    if (playingField == null || x >= playingField.length
      || playingField[0] == null || y >= playingField[0].length) {
      System.out.println("Can not reach place outside of map.");
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

    System.out.println(path.toAbsolutePath());
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

    System.out.println("Map");
    System.out.println("-------------------");
    System.out.println(getMapString(true));
    System.out.println("-------------------");
  }

  public void updatePlayerPosition() {
    playingFieldWithPlayer = deepCopy2DArray(playingField);
    playingFieldWithPlayer[playerPosition[0]][playerPosition[1]] = '☺';
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

    if (withPlayer) {
      updatePlayerPosition();
    }
    return Arrays.stream(withPlayer ? playingFieldWithPlayer : playingField)
      .map(row -> new String(row).replace("", " ").trim())
      .collect(Collectors.joining("\n"));
  }
}
