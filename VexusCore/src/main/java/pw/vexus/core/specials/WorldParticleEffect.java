package pw.vexus.core.specials;

import lombok.Getter;
import net.cogzmc.core.effect.particle.ParticleEffect;
import net.cogzmc.core.effect.particle.ParticleEffectType;
import net.cogzmc.core.util.Point;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitTask;
import pw.vexus.core.VexusCore;

public final class WorldParticleEffect {
    private final WPEType type;
    private final Point point;
    private final String world;

    private final int ticksPerCycle;
    private final int size;

    private final ParticleEffectType effectType;
    @Getter(lazy = true) private final World bukkitWorld = _getWorld();

    private Location getLocation() {
        return point.getLocation(getBukkitWorld());
    }

    private World _getWorld() {
        return Bukkit.getWorld(world);
    }

    public WorldParticleEffect(WPEType type, Point point, String world, int ticksPerCycle, int size, ParticleEffectType effectType) {
        this.type = type;
        this.point = point;
        this.world = world;
        this.ticksPerCycle = ticksPerCycle;
        this.size = size;
        this.effectType = effectType;
        new WPEPlayer().start();
    }


    static enum WPEType {
        REVOLVE(new WPETypeDelegate() {
            @Override
            public void playEffect(float percentage, WorldParticleEffect effect) {
                ParticleEffect ef = new ParticleEffect(effect.effectType);
                Location center = effect.getLocation();
                ef.emitGlobally(20L, new Location(effect.getBukkitWorld(), (Math.cos(Math.PI * 2 * percentage) * effect.size) + center.getX(), center.getY() + (Math.sin(32 * Math.PI * percentage) * 5), (Math.sin(Math.PI * 2 * percentage) * effect.size) + center.getZ()));
            }

            @Override
            public void playOutEffect(WorldParticleEffect effect) {

            }

            @Override
            public void playInEffect(WorldParticleEffect effect) {

            }
        }),
        GUSH(new WPETypeDelegate() {
            @Override
            public void playEffect(float percentage, WorldParticleEffect effect) {

            }

            @Override
            public void playOutEffect(WorldParticleEffect effect) {

            }

            @Override
            public void playInEffect(WorldParticleEffect effect) {

            }
        }),
        FOUNTIAN(new WPETypeDelegate() {
            @Override
            public void playEffect(float percentage, WorldParticleEffect effect) {

            }

            @Override
            public void playOutEffect(WorldParticleEffect effect) {

            }

            @Override
            public void playInEffect(WorldParticleEffect effect) {

            }
        }),
        EXPLODE(new WPETypeDelegate() {
            @Override
            public void playEffect(float percentage, WorldParticleEffect effect) {

            }

            @Override
            public void playOutEffect(WorldParticleEffect effect) {

            }

            @Override
            public void playInEffect(WorldParticleEffect effect) {

            }
        });

        private final WPETypeDelegate delegate;

        WPEType(WPETypeDelegate delegate) {
            this.delegate = delegate;
        }
    }

    private static interface WPETypeDelegate {
        void playEffect(float percentage, WorldParticleEffect effect);
        void playOutEffect(WorldParticleEffect effect);
        void playInEffect(WorldParticleEffect effect);
    }

    private class WPEPlayer implements Runnable {
        private int iterations = 0;

        @Override
        public void run() {
            WorldParticleEffect worldParticleEffect = WorldParticleEffect.this;
            float percentage = (float)iterations/(float)worldParticleEffect.ticksPerCycle;
            WPETypeDelegate delegate = worldParticleEffect.type.delegate;
            delegate.playEffect(percentage, worldParticleEffect);
            if (percentage >= 1) {
                delegate.playOutEffect(worldParticleEffect);
                iterations = 0;
            }
        }

        public void start() {
            Bukkit.getScheduler().runTaskTimer(VexusCore.getInstance(), this, 1, 1);
            WorldParticleEffect worldParticleEffect = WorldParticleEffect.this;
            worldParticleEffect.type.delegate.playInEffect(worldParticleEffect);
        }

    }
}
