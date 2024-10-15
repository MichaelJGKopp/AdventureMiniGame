package com.adventure;

import com.sun.jdi.CharType;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class Player {

  private String name;
  private CharClass charClass;
  private int level;
  private int exp;
  private int health;
  private int mana;
  private int strength;
  private int defense;
  private int agility;
  private int magicPower;
  private int recovery;
  private int luck;
  private int stamina;

  private List<Item> inventory;
  private int gold;
  private EnumMap<EquipSlot, Item> equipSlots;
  private List<Ability> abilities;

}
