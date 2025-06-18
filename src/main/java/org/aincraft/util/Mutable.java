package org.aincraft.util;


public class Mutable<T> {

  private T data;

  public Mutable(T data) {
    this.data = data;
  }

  public T get() {
    return data;
  }

  public void set(T data) {
    this.data = data;
  }
}
