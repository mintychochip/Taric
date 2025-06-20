package org.aincraft.container.launchable;

import org.aincraft.api.container.IEntityProxy;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

class EntityProxy<E extends Entity> implements IEntityProxy {

  protected Location location;
  protected Vector velocity;
  protected Class<? extends E> entityClazz;

  EntityProxy(Class<? extends E> entityClazz, Vector velocity, Location location) {
    this.entityClazz = entityClazz;
    this.velocity = velocity;
    this.location = location;
  }

  @Override
  public void add() {
    World world = location.getWorld();
    Entity entity = world.createEntity(location, entityClazz);
    world.addEntity(entity);
    entity.setVelocity(velocity);
  }

  @Override
  public void setVelocity(Vector velocity) {
    this.velocity = velocity;
  }

  @Override
  public Vector getVelocity() {
    return velocity;
  }

  @Override
  public Location getLocation() {
    return location;
  }

  @Override
  public void setLocation(Location location) {
    this.location = location;
  }

  @Override
  protected Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
}
