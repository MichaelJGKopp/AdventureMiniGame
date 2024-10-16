package com.adventure;

import java.util.Scanner;

public class UI {

  private static final String menu = """
  Menu
  ----------
  0: Play
  1: Regenerate Map
  2: Save Map to File
  3: Load Map from File
  4: Print current Map
  5: Game Rules
  6: Exit
  
  Enter option:\s""";

  private static final String rules = """
  Game Rules
  ----------
  The hero (player) starts in the upper left of the map 
  and needs to move to the lower right corner of the map to end the game.
  There are many surprises awaiting the hero (you) along the way.
  """;

  private static final String youWon = """
            ********************************
            Congratulations you won!\n" +
            "You reached the end of the map!
            ********************************
            """;

  private Environment env = new Environment();
  private Scanner scanner = new Scanner(System.in);
  private boolean isRunning;

  public void menu() {

    env.loadPlaces(); // loads places with descriptions
    env.generateMap(5, 5);  // TODO: Generate start map, might change
    isRunning = true;
    while (isRunning) {
      System.out.print(menu);
      String input = scanner.nextLine().trim().substring(0, 1);
      System.out.println();
      switch (input) {
        case "0" -> {
          System.out.println(rules);
          play();
        }
        case "1" -> {
          env.generateMap(5, 5);
          env.printMap();
        }
        case "2" -> env.saveMap();
        case "3" -> env.loadMapFromFile();
        case "4" -> env.printMap();
        case "5" -> System.out.println(rules);
        case "6" -> {
          isRunning = false;
          System.out.println("Exiting game.");
        }
      }
    }
  }

  private void play() {

    int round = 0;
    boolean isMoving = true;
    boolean isPlaying = true;

    while (isPlaying) {
      if (isMoving) {
        if (env.gameEnd()) {
          isRunning = false;
          System.out.println(youWon);
          return;
        }
        printSetting();
        if (++round % 5 == 0) {
          env.earthQuake();
        }
      }

      System.out.print("Press a key to continue: ");
      String input = scanner.nextLine().trim().substring(0, 1).toLowerCase();

      if (input.equals("q")) {
        isRunning = false;
        return;
      }

      isMoving = move(input);
    }
  }

  private boolean move(String input) {
    for (var dir : Direction.values()) {
      if (dir.getButton().equals(input)) {
        return env.movePlayer(dir);
      }
    }
    return false;
  }

  private void printSetting() {
    env.printMap();
    env.printCurrentPlace();
    env.printSurroundingPlaces();
  }


}
