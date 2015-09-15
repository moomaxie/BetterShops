package max.hubbard.bettershops.Shops.Types.NPC;

import max.hubbard.bettershops.Configurations.Config;
import max.hubbard.bettershops.Configurations.Language;
import max.hubbard.bettershops.Core;
import max.hubbard.bettershops.Events.NPCShopCreateEvent;
import max.hubbard.bettershops.ShopManager;
import max.hubbard.bettershops.Shops.Shop;
import max.hubbard.bettershops.Utils.ProfileLoader;
import max.hubbard.bettershops.Utils.WorldGuardStuff;
import max.hubbard.bettershops.Versions.Spawner;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

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
public class NPCShop implements ShopsNPC {

    public LivingEntity entity;
    Shop shop;
    Location l;
    private EntityInfo info;

    public NPCShop(final EntityType e, final List<String> lore, final Shop s, final Boolean... b) {
        shop = s;
        l = shop.getLocation();
        info = new EntityInfo(e, lore, b);
        shop.setObject("NPCInfo",info.toString());
        spawn();
    }

    public NPCShop(EntityInfo i, Shop s) {
        shop = s;
        l = s.getLocation();
        info = i;
        shop.setObject("NPCInfo",info.toString());
        spawn();
    }


    public void spawn() {

        if (shop.getNPCShop() == null || entity == null || !entity.isValid()) {

            boolean ca = false;
            EntityType e = info.getType();
            List<String> lore = info.getLore();
            try {

                removeChest();

                if (Core.useWorldGuard()) {
                    if (Core.getWorldGuard().getGlobalConfiguration().activityHaltToggle) {
                        ca = true;
                        Core.getWorldGuard().getGlobalConfiguration().activityHaltToggle = false;
                    }
                }

                if (Core.useWorldGuard()) {
                    if (!WorldGuardStuff.checkNPCOverride(shop)) {
                        if (shop.getOwner().isOnline())
                            shop.getOwner().getPlayer().sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "WorldGuardDenyNPC"));
                    }
                }

                if (e != EntityType.PLAYER) {
                    entity = Spawner.spawnEntity(e, shop.getLocation().clone().add(.5, 0, .5));
                } else {
                    if (lore.size() > 0 && lore.get(0).contains(Language.getString("NPCs", "Player"))) {
                        OfflinePlayer pl = Bukkit.getOfflinePlayer(lore.get(0).substring(Language.getString("NPCs", "Player").length() + 3));
                        if (pl != null) {
                            entity = Spawner.spawnEntity(e, new ProfileLoader(pl.getUniqueId().toString(), "§a§l" + shop.getName()).loadProfile(), shop.getLocation().clone().add(.5, 0, .5));
                        } else {
                            entity = Spawner.spawnEntity(e, new ProfileLoader(shop.getOwner().getUniqueId().toString(), "§a§l" + shop.getName()).loadProfile(), shop.getLocation().clone().add(.5, 0, .5));
                        }
                    } else {
                        entity = Spawner.spawnEntity(e, new ProfileLoader(shop.getOwner().getUniqueId().toString(), "§a§l" + shop.getName()).loadProfile(), shop.getLocation().clone().add(.5, 0, .5));
                    }
                }

                if (entity == null) {
                    shop.setObject("NPC", false);
                    addChest(l);
                    shop.getOwner().getPlayer().sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "NPCTimeOut"));
                    return;
                }


                entity.setCustomName("§a§l" + shop.getName());
                entity.setCustomNameVisible(true);
                entity.setRemoveWhenFarAway(true);
                entity.setCanPickupItems(false);

                for (Class cl : entity.getType().getEntityClass().getDeclaredClasses()) {
                    for (String l : lore) {
                        boolean caa = false;
                        if (cl.getSimpleName().equals("Type") && e == EntityType.OCELOT) {
                            if (l.contains(Language.getString("NPCs", "OcelotType"))) {
                                caa = true;
                            }
                        } else if (cl.getSimpleName().equals("Type") && e == EntityType.RABBIT) {
                            if (l.contains(Language.getString("NPCs", "RabbitType"))) {
                                caa = true;
                            }
                        } else {
                            if (e != EntityType.PLAYER)
                                if (l.contains(Language.getString("NPCs", cl.getSimpleName()))) {
                                    caa = true;
                                }
                        }
                        if (caa) {
                            String obj = "";
                            if (cl.getSimpleName().equals("Type") && e == EntityType.OCELOT) {
                                if (l.contains(Language.getString("NPCs", "OcelotType"))) {
                                    obj = l.substring(Language.getString("NPCs", "OcelotType").length() + 3);
                                }
                            } else if (cl.getSimpleName().equals("Type") && e == EntityType.RABBIT) {
                                if (l.contains(Language.getString("NPCs", "RabbitType"))) {
                                    obj = l.substring(Language.getString("NPCs", "RabbitType").length() + 3);
                                }
                            } else {
                                if (e != EntityType.PLAYER)
                                    if (l.contains(Language.getString("NPCs", cl.getSimpleName()))) {
                                        obj = l.substring(Language.getString("NPCs", cl.getSimpleName()).length() + 3);
                                    }
                            }
                            if (cl.isEnum()) {
                                Enum en = (Enum) cl.getMethod("valueOf", String.class).invoke(cl, obj);

                                if (cl.getSimpleName().equals("SkeletonType")) {
                                    entity.getClass().getMethod("setSkeletonType", en.getClass()).invoke(entity, en);
                                }
                                if (cl.getSimpleName().equals("Type") && e == EntityType.OCELOT) {
                                    entity.getClass().getMethod("setCatType", en.getClass()).invoke(entity, en);
                                }
                                if (cl.getSimpleName().equals("Type") && e == EntityType.RABBIT) {
                                    entity.getClass().getMethod("setRabbitType", en.getClass()).invoke(entity, en);
                                }
                                if (cl.getSimpleName().equals("Style")) {
                                    entity.getClass().getMethod("setStyle", en.getClass()).invoke(entity, en);
                                }
                                if (cl.getSimpleName().equals("Variant")) {
                                    entity.getClass().getMethod("setVariant", en.getClass()).invoke(entity, en);
                                }
                                if (cl.getSimpleName().equals("Color")) {
                                    entity.getClass().getMethod("setColor", en.getClass()).invoke(entity, en);
                                }
                                if (cl.getSimpleName().equals("Profession")) {
                                    entity.getClass().getMethod("setProfession", en.getClass()).invoke(entity, en);
                                }
                            }
                        }
                    }
                }

                for (String l : lore) {
                    if (l.contains(Language.getString("NPCs", "DyeColor"))) {
                        String obj = l.substring(Language.getString("NPCs", "DyeColor").length() + 3);
                        Enum en = (Enum) DyeColor.valueOf(obj);
                        if (e == EntityType.SHEEP) {
                            entity.getClass().getMethod("setColor", DyeColor.class).invoke(entity, en);
                        }
                        if (e == EntityType.WOLF) {
                            entity.getClass().getSuperclass().getMethod("setTamed", boolean.class).invoke(entity, true);
                            entity.getClass().getMethod("setCollarColor", DyeColor.class).invoke(entity, en);
                        }
                    }
                    if (l.contains(Language.getString("NPCs", "Size"))) {
                        String obj = l.substring(Language.getString("NPCs", "Size").length() + 3);
                        int i = Integer.parseInt(obj);
                        entity.getClass().getMethod("setSize", int.class).invoke(entity, i);
                    }
                }

                if (Ageable.class.isAssignableFrom(e.getEntityClass())) {
                    if (info.isBaby()) {
                        entity.getClass().getSuperclass().getMethod("setBaby").invoke(entity);
                    } else {
                        entity.getClass().getSuperclass().getMethod("setAdult").invoke(entity);
                    }
                }
                if (e == EntityType.SHEEP) {
                    entity.getClass().getMethod("setSheared", boolean.class).invoke(entity, info.isSheared());
                }
                if (e == EntityType.ZOMBIE) {
                    entity.getClass().getMethod("setVillager", boolean.class).invoke(entity, info.isVillagerZombie());
                    entity.getClass().getMethod("setBaby", boolean.class).invoke(entity, info.isBaby());
                }


                NPCShopCreateEvent en = new NPCShopCreateEvent(shop);

                Bukkit.getPluginManager().callEvent(en);


            } catch (Exception ex) {
                addChest(shop.getLocation());
                ex.printStackTrace();
            } finally {
                if (Core.useWorldGuard() && (boolean) Config.getObject("NPCOverride")) {
                    WorldGuardStuff.denyMobs(shop.getLocation());
                }
                if (ca) {
                    Core.getWorldGuard().getGlobalConfiguration().activityHaltToggle = true;
                }
            }
        }
    }

    public void returnNPC() {

        if (entity == null || !entity.isValid()) {
            spawn();
        } else {
            if (entity instanceof Monster) {
                ((Monster) entity).setTarget(null);
            }
            entity.setFireTicks(0);
            entity.setFallDistance(0);
            entity.getEquipment().setItemInHand(null);

            if (entity.getLocation().distance(shop.getLocation().clone()) > .75) {
                entity.remove();
                spawn();

                for (LivingEntity ent : shop.getLocation().getWorld().getLivingEntities()) {
                    if (ent.getCustomName() != null && ent.getCustomName().equals("§a§l" + shop.getName())) {
                        if (!ent.equals(getEntity())) {
                            ent.remove();
                        }
                    }
                }
            }
        }
    }

    public LivingEntity getEntity() {
        return entity;
    }

    @Override
    public void setLocation(Location l) {
        l.setX((int) l.getX() + .5);
        l.setZ((int) l.getZ() + .5);
        entity.teleport(l, PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

    public EntityInfo getInfo() {
        return info;
    }

    public Shop getShop() {
        return ShopManager.fromString(shop.getName());
    }

    public static void addChest(final Location l) {

        new BukkitRunnable() {

            @Override
            public void run() {
                l.getBlock().setType(Material.CHEST);

                Chest chest = (Chest) l.getBlock().getState();

                org.bukkit.material.Chest c = (org.bukkit.material.Chest) chest.getData();

                BlockFace face = c.getFacing();

                c.setFacingDirection(face.getOppositeFace());

                chest.setData(c);

                chest.update();

                Block b = chest.getBlock().getRelative(face);

                if (b != null) {

                    b.setType(Material.WALL_SIGN);


                    if (b.getState() instanceof Sign) {
                        Sign s = (Sign) b.getState();

                        org.bukkit.material.Sign sign = (org.bukkit.material.Sign) s.getData();

                        sign.setFacingDirection(face.getOppositeFace());

                        s.setData(sign);

                        s.setLine(0, Language.getString("MainGUI", "SignLine1"));
                        s.setLine(1, Language.getString("MainGUI", "SignLine2"));
                        s.setLine(2, Language.getString("MainGUI", "SignLine3Open"));

                        s.setLine(3, Language.getString("MainGUI", "SignLine4"));

                        s.update();
                    }
                }
            }

        }.runTask(Bukkit.getPluginManager().getPlugin("BetterShops"));
    }

    public void removeChest() {

        new BukkitRunnable() {

            @Override
            public void run() {

                if (shop.getLocation().getBlock().getRelative(1, 0, 0).getType() == Material.WALL_SIGN) {
                    Sign s = (Sign) shop.getLocation().getBlock().getRelative(1, 0, 0).getState();

                    if (s.getLine(0).contains(Language.getString("MainGUI", "SignLine1"))) {
                        if (s.getLine(3).contains(Language.getString("MainGUI", "SignLine4"))) {
                            if (s.getLine(1).contains(Language.getString("MainGUI", "SignLine2"))) {
                                s.getBlock().setType(Material.AIR);
                            }
                        }
                    }
                }

                if (shop.getLocation().getBlock().getRelative(-1, 0, 0).getType() == Material.WALL_SIGN) {
                    Sign s = (Sign) shop.getLocation().getBlock().getRelative(-1, 0, 0).getState();

                    if (s.getLine(0).contains(Language.getString("MainGUI", "SignLine1"))) {
                        if (s.getLine(3).contains(Language.getString("MainGUI", "SignLine4"))) {
                            if (s.getLine(1).contains(Language.getString("MainGUI", "SignLine2"))) {
                                s.getBlock().setType(Material.AIR);
                            }
                        }
                    }
                }

                if (shop.getLocation().getBlock().getRelative(0, 0, 1).getType() == Material.WALL_SIGN) {
                    Sign s = (Sign) shop.getLocation().getBlock().getRelative(0, 0, 1).getState();

                    if (s.getLine(0).contains(Language.getString("MainGUI", "SignLine1"))) {
                        if (s.getLine(3).contains(Language.getString("MainGUI", "SignLine4"))) {
                            if (s.getLine(1).contains(Language.getString("MainGUI", "SignLine2"))) {
                                s.getBlock().setType(Material.AIR);
                            }
                        }
                    }
                }

                if (shop.getLocation().getBlock().getRelative(0, 0, -1).getType() == Material.WALL_SIGN) {
                    Sign s = (Sign) shop.getLocation().getBlock().getRelative(0, 0, -1).getState();

                    if (s.getLine(0).contains(Language.getString("MainGUI", "SignLine1"))) {
                        if (s.getLine(3).contains(Language.getString("MainGUI", "SignLine4"))) {
                            if (s.getLine(1).contains(Language.getString("MainGUI", "SignLine2"))) {
                                s.getBlock().setType(Material.AIR);
                            }
                        }
                    }
                }

                shop.getLocation().getBlock().setType(Material.AIR);
            }

        }.runTask(Bukkit.getPluginManager().getPlugin("BetterShops"));
    }
}