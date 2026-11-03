package io.github.sefiraat.crystamaehistoria.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.junit.jupiter.api.Test;

/**
 * Paper changes particle data requirements between versions. Assert the real
 * World.spawnParticle overload receives exactly the runtime-required payload.
 */
class ParticleUtilsTest {

    @Test
    void flashReceivesColorOnlyWhenRuntimeRequiresIt() {
        var data = invokeAndCaptureData(Particle.FLASH);
        if (Particle.FLASH.getDataType() == Color.class) {
            assertEquals(Color.WHITE, data);
        } else {
            assertNull(data, "Earlier Paper versions do not require Color for FLASH");
        }
    }

    @Test
    void ordinaryParticlesRetainNoDataPayload() {
        assertNull(invokeAndCaptureData(Particle.FLAME));
    }

    private static Object invokeAndCaptureData(Particle particle) {
        var calls = new AtomicInteger();
        var observed = new AtomicReference<Object>();
        var world = (World) Proxy.newProxyInstance(
            World.class.getClassLoader(),
            new Class<?>[]{World.class},
            (proxy, method, args) -> {
                if ("spawnParticle".equals(method.getName())) {
                    assertEquals(4, args.length, "Use the data-capable spawnParticle overload");
                    assertEquals(particle, args[0]);
                    observed.set(args[3]);
                    calls.incrementAndGet();
                }
                if (method.getReturnType() == boolean.class) return false;
                if (method.getReturnType() == int.class) return 0;
                return null;
            }
        );

        ParticleUtils.displayParticleEffect(new Location(world, 0, 64, 0), particle, 0, 1);
        assertEquals(1, calls.get(), "Exactly one particle must be spawned");
        return observed.get();
    }
}
