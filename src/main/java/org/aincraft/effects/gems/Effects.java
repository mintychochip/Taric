package org.aincraft.effects.gems;

import org.aincraft.effects.IGemEffect;

public final class Effects {

  public static IGemEffect AUTO_SMELT;

  public static IGemEffect BURROWING;

  public static IGemEffect VAMPIRISM;

  public static IGemEffect ECHOS_OF_INSIGHT;

  public static IGemEffect SCAVENGE;

  public static IGemEffect VORPAL;

  public static IGemEffect NETHER_SCOURGE;

  public static IGemEffect VEIN_MINER;

  public static IGemEffect BLINK;

  public static IGemEffect COLD_ASPECT;

  public static IGemEffect MULTISHOT;

  public static IGemEffect FLARE;

  public static IGemEffect PRISMATIC;

  static {
    AUTO_SMELT = new AutoSmelt();
    BURROWING = new Burrowing();
    VAMPIRISM = new Vampirism();
    ECHOS_OF_INSIGHT = new Insight();
    SCAVENGE = new Scavenge();
    VORPAL = new Vorpal();
    NETHER_SCOURGE = new NetherScourge();
    VEIN_MINER = new VeinMiner();
    BLINK = new Blink();
    COLD_ASPECT = new ColdAspect();
    MULTISHOT = new Multishot();
    FLARE = new Flare();
    PRISMATIC = new Prismatic();
  }
}
