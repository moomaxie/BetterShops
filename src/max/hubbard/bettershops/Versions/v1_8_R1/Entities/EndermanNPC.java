package max.hubbard.bettershops.Versions.v1_8_R1.Entities;

import net.minecraft.server.v1_8_R1.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftEnderman;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftLivingEntity;
import org.bukkit.entity.Enderman;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.lang.reflect.Field;
import java.util.List;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2015. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class EndermanNPC extends EntityEnderman {
    public EndermanNPC(World world) {
        super(world);

        List<?> goalB = (List<?>) getPrivateField("b", PathfinderGoalSelector.class, goalSelector);
        goalB.clear();
        List<?> goalC = (List<?>) getPrivateField("c", PathfinderGoalSelector.class, goalSelector);
        goalC.clear();
        List<?> targetB = (List<?>) getPrivateField("b", PathfinderGoalSelector.class, targetSelector);
        targetB.clear();
        List<?> targetC = (List<?>) getPrivateField("c", PathfinderGoalSelector.class, targetSelector);
        targetC.clear();

        this.goalSelector = new PathfinderGoalSelector(new MethodProfiler());

        this.goalSelector.a(0, new PathfinderGoalFloat(this));
        this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
        this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));

        this.ai = false;

    }

    public static Object getPrivateField(String fieldName, Class<PathfinderGoalSelector> clazz, Object object) {

        Field field;
        Object o = null;

        try {
            field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            o = field.get(object);

        } catch (NoSuchFieldException e) {

            e.printStackTrace();

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return o;

    }


    @Override
    public void enderTeleportTo(double d0, double d1, double d2) {

    }

    @Override
    public double e(double d0, double d1, double d2) {
        return 0;
    }

    @Override
    public void move(double d0, double d1, double d2) {

    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        return false;
    }

    @Override
    public boolean k(double x, double y, double z) {
        return false;
    }


    @Override
    public void g(double x, double y, double z) {

    }


    public static Enderman spawn(Location location) {

        MinecraftServer server = MinecraftServer.getServer();
        WorldServer world = server.getWorldServer(0);
        for (WorldServer ws : server.worlds) {
            if (ws.getWorld().getName().equals(location.getWorld().getName())) {
                world = ws;
                break;
            }
        }
        World mcWorld = (World) ((CraftWorld) location.getWorld()).getHandle();
        final EndermanNPC customEntity = new EndermanNPC(
                world);
        customEntity.setLocation(location.getX(), location.getY(),
                location.getZ(), location.getYaw(), location.getPitch());
        ((CraftLivingEntity) customEntity.getBukkitEntity())
                .setRemoveWhenFarAway(false);
        mcWorld.addEntity(customEntity, CreatureSpawnEvent.SpawnReason.CUSTOM);
        return (CraftEnderman) customEntity.getBukkitEntity();
    }
}
