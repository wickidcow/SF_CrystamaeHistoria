package io.github.sefiraat.crystamaehistoria.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicReference;

import org.bukkit.entity.ArmorStand;
import org.junit.jupiter.api.Test;

class ArmourStandDistanceDespawnTest {

    @Test
    void preservesNonDespawningDisplayStandPolicy() throws Exception {
        AtomicReference<Boolean> lastFlag = new AtomicReference<>();
        ArmorStand displayStand = (ArmorStand) Proxy.newProxyInstance(
                ArmorStand.class.getClassLoader(),
                new Class<?>[] { ArmorStand.class },
                (proxy, method, args) -> {
                    if ("setRemoveWhenFarAway".equals(method.getName())) {
                        lastFlag.set((Boolean) args[0]);
                        return null;
                    }
                    throw new AssertionError("Unexpected ArmorStand API call: " + method.getName());
                }
        );

        distanceDespawnMethod().invoke(null, displayStand, false);
        assertEquals(Boolean.FALSE, lastFlag.get(), "Display armor stands must not despawn at distance");
    }

    @Test
    void doesNotSilentlyReplaceDistanceDespawnWithWorldPersistence() throws Exception {
        ArmorStand displayStand = (ArmorStand) Proxy.newProxyInstance(
                ArmorStand.class.getClassLoader(),
                new Class<?>[] { ArmorStand.class },
                (proxy, method, args) -> {
                    throw new IllegalArgumentException("Simulated setter failure");
                }
        );

        InvocationTargetException failure = assertThrows(
                InvocationTargetException.class,
                () -> distanceDespawnMethod().invoke(null, displayStand, false)
        );
        assertInstanceOf(IllegalStateException.class, failure.getCause());
    }

    private static Method distanceDespawnMethod() throws NoSuchMethodException {
        Method method = ArmourStandUtils.class.getDeclaredMethod("setDistanceDespawn", ArmorStand.class, boolean.class);
        method.setAccessible(true);
        return method;
    }
}
