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
  5: Exit
  
  Enter option:\s""";

  private Environment env = new Environment();
  private Scanner scanner = new Scanner(System.in);
  private boolean isRunning;

  public void menu() {

    env.loadPlaces(); // loads places with descriptions

    isRunning = true;
    while (isRunning) {
      System.out.print(menu);
      String input = scanner.nextLine().trim().substring(0, 1);
      switch (input) {
        case "0" -> play();
        case "1" -> env.generateMap(5, 5);
        case "2" -> env.saveMap();
        case "3" -> env.loadMapFromFile();
        case "4" -> env.printMap();
        case "5" -> {
          isRunning = false;
          System.out.println("Exiting game.");
        }
      }
    }
  }

  private void play() {

    env.printCurrentPlace();

    env.printSurroundingPlaces();

//    env.earthQuake();

    env.loadPlaces(); // loads places with descriptions

    boolean isPlaying = true;
    while (isPlaying) {

      System.out.print(menu);

      String input = scanner.nextLine().trim().substring(0, 1);

      isPlaying = false;
    }

  }


}
