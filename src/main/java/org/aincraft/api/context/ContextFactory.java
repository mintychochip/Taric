package org.aincraft.api.context;

import io.papermc.paper.event.entity.EntityDamageItemEvent;
import io.papermc.paper.event.entity.EntityMoveEvent;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.aincraft.api.context.IItemDamageContext.EntityItemDamageContext;
import org.aincraft.api.context.IShearEntityContext.IPlayerShearEntityContext;
import org.aincraft.api.trigger.IOnInteract.PlayerInteractContext;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class ContextFactory {

  public static BlockBreakContext create(BlockBreakEvent event) {
    ContextBinder<BlockBreakEvent> binder = new ContextBinder<>(event)
        .bind("getTool", e -> {
          Player player = e.getPlayer();
          PlayerInventory inventory = player.getInventory();
          return inventory.getItemInMainHand();
        }).bind("getExperience", "getExpToDrop")
        .bind("setExperience", "setExpToDrop");
    return binder.build(BlockBreakContext.class);
  }

  public static EntityDamageEntityContext create(EntityDamageByEntityEvent event) {
    return new ContextBinder<>(event).build(EntityDamageEntityContext.class);
  }

  public static EntityItemDamageContext create(EntityDamageItemEvent event) {
    return new ContextBinder<>(event).build(EntityItemDamageContext.class);
  }

  public static EntityKillContext create(EntityDeathEvent event) {
    return new ContextBinder<>(event)
        .bind("setDrops", (e, args) -> {
          List<ItemStack> drops = (List<ItemStack>) args[0];
          e.getDrops().clear();
          e.getDrops().addAll(drops);
          return null;
        })
        .bind("getSlain", "getEntity")
        .bind("setExperience", "setDroppedExp")
        .bind("getExperience", "getDroppedExp")
        .build(EntityKillContext.class);
  }

  public static PlayerInteractContext create(PlayerInteractEvent event) {
    return new ContextBinder<>(event).build(PlayerInteractContext.class);
  }

  public static IPlayerShearEntityContext create(PlayerShearEntityEvent event) {
    return new ContextBinder<>(event).build(IPlayerShearEntityContext.class);
  }

  public static EntityMoveContext create(EntityMoveEvent event) {
    return new ContextBinder<>(event).build(EntityMoveContext.class);
  }

  public static PlayerMoveContext create(PlayerMoveEvent event) {
    return new ContextBinder<>(event).build(PlayerMoveContext.class);
  }

  private static final class ContextBinder<E> {

    private final E delegate;
    private final Map<String, BiFunction<E, Object[], Object>> overrides = new HashMap<>();
    private final Map<String, String> renames = new HashMap<>();

    private ContextBinder(E delegate) {
      this.delegate = delegate;
    }

    public ContextBinder<E> bind(String methodName, Function<E, ?> getter) {
      overrides.put(methodName, (delegate, args) -> getter.apply(delegate));
      return this;
    }

    public ContextBinder<E> bind(String methodName, BiFunction<E, Object[], Object> function) {
      overrides.put(methodName, function);
      return this;
    }

    public ContextBinder<E> bind(String from, String to) {
      renames.put(from, to);
      return this;
    }

    @SuppressWarnings("unchecked")
    public <I> I build(Class<I> contextClazz) {
      ContextHandler<E> handler = new ContextHandler<>(delegate, contextClazz, overrides,
          renames);
      return (I) Proxy.newProxyInstance(contextClazz.getClassLoader(), new Class<?>[]{contextClazz},
          handler);
    }
  }

  private static final class ContextHandler<E> implements InvocationHandler {

    private final E delegate;
    private final Class<?> contextClazz;
    private final Map<String, BiFunction<E, Object[], Object>> overrides;
    private final Map<String, String> renames;
    private final Map<Method, Method> methodCache = new ConcurrentHashMap<>();

    private ContextHandler(E delegate, Class<?> contextClazz,
        Map<String, BiFunction<E, Object[], Object>> overrides, Map<String, String> renames) {
      this.delegate = delegate;
      this.contextClazz = contextClazz;
      this.overrides = overrides;
      this.renames = renames;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      if (method.getDeclaringClass() == Object.class) {
        return switch (method.getName()) {
          case "toString" -> proxy.getClass().getInterfaces()[0].getSimpleName()
              + "@" + Integer.toHexString(System.identityHashCode(proxy))
              + "(" + contextClazz.getSimpleName() + ")";
          case "hashCode" -> System.identityHashCode(proxy);
          case "equals" -> proxy == args[0];
          default -> method.invoke(this, args);
        };
      }

      BiFunction<E, Object[], Object> override = overrides.get(method.getName());
      if (override != null) {
        return override.apply(delegate, args);
      }

      Method targetMethod = methodCache.computeIfAbsent(method, this::resolveMethod);
      try {
        return targetMethod.invoke(delegate, args);
      } catch (IllegalAccessException | InvocationTargetException e) {
        throw new RuntimeException(e);
      }
    }

    private Method resolveMethod(Method proxyMethod) {
      String targetName = renames.getOrDefault(proxyMethod.getName(), proxyMethod.getName());
      try {
        Method m = delegate.getClass().getMethod(targetName, proxyMethod.getParameterTypes());
        m.setAccessible(true);
        return m;
      } catch (NoSuchMethodException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
