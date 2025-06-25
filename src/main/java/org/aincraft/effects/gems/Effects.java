package org.aincraft.effects.gems;

import org.aincraft.Taric;
import org.aincraft.effects.IGemEffect;

public final class Effects {

  public static IGemEffect AUTO_SMELT;

  public static IGemEffect BURROWING;

  public static IGemEffect VAMPIRISM;

  public static IGemEffect INSIGHT;

  public static IGemEffect SCAVENGE;

  public static IGemEffect VORPAL;

  public static IGemEffect NETHER_SCOURGE;

  public static IGemEffect VEIN_MINER;

  public static IGemEffect BLINK;

  public static IGemEffect FROSTBITE;

  public static IGemEffect MULTISHOT;

  public static IGemEffect FLARE;

  public static IGemEffect PRISMATIC;

  public static IGemEffect HARVEST;

  public static IGemEffect OVERFLOWING;

  public static IGemEffect GLIMMER;

  public static IGemEffect HARDENED;

  public static IGemEffect TILLER;

  public static IGemEffect MANA_BORE;

  static {
    AUTO_SMELT = AutoSmelt.create(Taric.getConfiguration("gems"));
    BURROWING = new Burrowing();
    VAMPIRISM = new Vampirism();
    INSIGHT = new Insight();
    SCAVENGE = new Scavenge();
    VORPAL = new Vorpal();
    NETHER_SCOURGE = new NetherScourge();
    VEIN_MINER = new VeinMiner();
    BLINK = new Blink();
    FROSTBITE = new Frostbite();
    MULTISHOT = new Multishot();
    FLARE = new Flare();
    PRISMATIC = new Prismatic();
    HARVEST = new Harvest();
    OVERFLOWING = new Overflowing();
    GLIMMER = new Glimmer();
    HARDENED = new Hardened();
    TILLER = new Tiller();
    MANA_BORE = new ManaBore();
  }
}
