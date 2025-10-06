package org.aincraft.container;

import com.google.common.base.Preconditions;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.aincraft.api.container.IEquipment;
import org.aincraft.api.container.IEquipment.IEquipmentFactory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

final class EquipmentFactory implements IEquipmentFactory {

  private static final class EquipmentHandler implements InvocationHandler {

    private final Object target;
    private final Class<?> targetClazz;
    private final Map<Method, Method> methodCache = new ConcurrentHashMap<>();

    private EquipmentHandler(Object target, Class<?> targetClazz) {
      this.target = target;
      this.targetClazz = targetClazz;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      if (method.getDeclaringClass() == Object.class) {
        return switch (method.getName()) {
          case "toString" -> "IEquipment->" + targetClazz.getSimpleName();
          case "hashCode" -> System.identityHashCode(proxy);
          case "equals" -> proxy == args[0];
          default -> method.invoke(this, args);
        };
      }
      Method targetMethod = methodCache.computeIfAbsent(method, m -> {
        try {
          Method resolvedMethod = targetClazz.getMethod(m.getName(), m.getParameterTypes());
          resolvedMethod.setAccessible(true);
          return resolvedMethod;
        } catch (NoSuchMethodException e) {
          throw new RuntimeException(e);
        }
      });
      try {
        return targetMethod.invoke(target, args);
      } catch (IllegalAccessException | InvocationTargetException e) {
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public IEquipment create(Entity entity) throws IllegalArgumentException {
    Preconditions.checkArgument(entity instanceof LivingEntity);
    if (entity instanceof Player player) {
      return new PlayerEquipment(player.getInventory());
    }
    return new EntityEquipment(((LivingEntity) entity).getEquipment());
  }
}
