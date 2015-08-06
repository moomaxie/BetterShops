package max.hubbard.bettershops.Versions;

import max.hubbard.bettershops.Utils.WordsCapitalizer;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2015. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */

public class Spawner {

    public static LivingEntity spawnEntity(final EntityType type, final Object... loc) throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicReference<LivingEntity> ref = new AtomicReference<>();
        new BukkitRunnable() {

            @Override
            public void run() {

                String packageName = Bukkit.getServer().getClass().getPackage().getName();
                String version = packageName.substring(packageName.lastIndexOf('.') + 1);


                String npc = WordsCapitalizer.capitalizeEveryWord(type.name().replaceAll("_", " "));
                npc = npc.replaceAll(" ", "");

                if (type != EntityType.PLAYER) {
                    try {
                        Class.forName("max.hubbard.bettershops.Versions." + version + ".Register").getMethod("registerNPCs").invoke(null);
                        ref.set((LivingEntity) Class.forName("max.hubbard.bettershops.Versions." + version + ".Entities." + npc + "NPC").getMethod("spawn", loc[0].getClass()).invoke(null, loc[0]));
                        Class.forName("max.hubbard.bettershops.Versions." + version + ".Register").getMethod("unregisterNPCs").invoke(null);
                    } catch (IllegalAccessException | NoSuchMethodException | ClassNotFoundException | InvocationTargetException e) {
                        e.printStackTrace();
                        latch.countDown();
                    }
                } else {
                    try {
                        Class.forName("max.hubbard.bettershops.Versions." + version + ".Register").getMethod("registerNPCs").invoke(null);
                        ref.set((LivingEntity) Class.forName("max.hubbard.bettershops.Versions." + version + ".Entities." + npc + "NPC").getMethod("spawn", loc[0].getClass(), loc[1].getClass()).invoke(null, loc[0], loc[1]));
                        Class.forName("max.hubbard.bettershops.Versions." + version + ".Register").getMethod("unregisterNPCs").invoke(null);
                    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
                        e.printStackTrace();

                        latch.countDown();
                    }
                }
                latch.countDown();
            }
        }.runTask(Bukkit.getPluginManager().getPlugin("BetterShops"));

        try {
            latch.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        return ref.get();

    }
}
