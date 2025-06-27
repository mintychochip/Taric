package org.aincraft.api.container;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public final class EffectInstanceMeta {

  @Expose
  @SerializedName("rank")
  private int rank;

  @Expose
  @SerializedName("extra")
  private JsonObject extra;

  public EffectInstanceMeta(int rank) {
    this.rank = rank;
    this.extra = new JsonObject();
  }

  public EffectInstanceMeta(int rank, JsonObject extra) {
    this.rank = rank;
    this.extra = extra;
  }

  @Override
  public String toString() {
    return super.toString();
  }

  public int getRank() {
    return rank;
  }

  public void setRank(int rank) {
    this.rank = rank;
  }

  public JsonObject getExtra() {
    return extra;
  }

  public void setExtra(JsonObject extra) {
    this.extra = extra;
  }

  public EffectInstanceMeta copy() {
    return new EffectInstanceMeta(rank, extra.deepCopy());
  }
}
