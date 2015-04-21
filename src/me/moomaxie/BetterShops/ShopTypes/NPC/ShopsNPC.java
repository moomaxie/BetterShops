package me.moomaxie.BetterShops.ShopTypes.NPC;

import BetterShops.Dev.API.Events.NPCShopCreateEvent;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import me.moomaxie.BetterShops.Configurations.Config;
import me.moomaxie.BetterShops.Configurations.GUIMessages.MainGUI;
import me.moomaxie.BetterShops.Configurations.Messages;
import me.moomaxie.BetterShops.Configurations.ShopManager;
import me.moomaxie.BetterShops.Core;
import me.moomaxie.BetterShops.Shops.Shop;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2014. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class ShopsNPC {

    public LivingEntity entity;
    Shop shop;
    Location l;

    public ShopsNPC(LivingEntity e, Shop s) {
        entity = e;
        shop = s;
        l = shop.getLocation();
        spawn();
    }

    public ShopsNPC(EntityType e, Shop s) {
        shop = s;
        l = shop.getLocation();
        boolean c = false;
        try {

            if (Core.useWorldGuard()) {
                if (Core.getWorldGuard().getGlobalConfiguration().activityHaltToggle) {
                    c = true;
                    Core.getWorldGuard().getGlobalConfiguration().activityHaltToggle = false;
                }
            }

            if (Core.useWorldGuard() && Config.useNPCOverride()) {
                allowMobs(shop.getLocation());
            } else if (Core.useWorldGuard()) {
                com.sk89q.worldguard.protection.ApplicableRegionSet set = Core.getRegionSet(shop.getLocation());

                if (!set.allows(DefaultFlag.MOB_SPAWNING)) {

                    if (shop.getOwner().isOnline())
                        shop.getOwner().getPlayer().sendMessage(Messages.getString("Prefix") + Messages.getString("WorldGuardDenyNPC"));
                    shop.setNPCShop(false);
                    return;
                }
            }

            entity = (LivingEntity) s.getLocation().getWorld().spawnEntity(s.getLocation().clone().add(.5, 0, .5), e);

            entity.setCustomName("§a§l" + shop.getName());
            entity.setCustomNameVisible(true);
            entity.setRemoveWhenFarAway(false);
            entity.setCanPickupItems(false);

            entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 9999999, 9999));

            entity.getEquipment().setItemInHand(null);

            if (entity instanceof Monster) {
                ((Monster) entity).setTarget(null);
            }

            removeChest();


            NPCShopCreateEvent en = new NPCShopCreateEvent(shop);

            Bukkit.getPluginManager().callEvent(en);

        } catch (Exception ex) {
            addChest(l);
        } finally {
            if (Core.useWorldGuard() && Config.useNPCOverride()) {
                denyMobs(shop.getLocation());
            }
            if (c) {
                Core.getWorldGuard().getGlobalConfiguration().activityHaltToggle = true;
            }
        }


    }

    public ShopsNPC(EntityType e, Shop s, Skeleton.SkeletonType skType) {

        shop = s;
        l = shop.getLocation();

        boolean c = false;
        try {

            if (Core.useWorldGuard()) {
                if (Core.getWorldGuard().getGlobalConfiguration().activityHaltToggle) {
                    c = true;
                    Core.getWorldGuard().getGlobalConfiguration().activityHaltToggle = false;
                }
            }

            if (Core.useWorldGuard() && Config.useNPCOverride()) {
                allowMobs(shop.getLocation());
            } else if (Core.useWorldGuard()) {
                com.sk89q.worldguard.protection.ApplicableRegionSet set = Core.getRegionSet(shop.getLocation());

                if (!set.allows(DefaultFlag.MOB_SPAWNING)) {

                    shop.getOwner().getPlayer().sendMessage(Messages.getString("Prefix") + Messages.getString("WorldGuardDenyNPC"));
                    shop.setNPCShop(false);
                    return;
                }
            }
            entity = (LivingEntity) s.getLocation().getWorld().spawnEntity(s.getLocation().clone().add(.5, 0, .5), e);
            entity.setCustomName("§a§l" + s.getName());
            entity.setCustomNameVisible(true);
            entity.setRemoveWhenFarAway(false);
            entity.setCanPickupItems(false);

            entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 9999999, 9999));

            entity.getEquipment().setItemInHand(null);

            if (entity instanceof Skeleton) {
                ((Skeleton) entity).setSkeletonType(skType);

            }

            if (entity instanceof Monster) {
                ((Monster) entity).setTarget(null);

            }

            removeChest();



            NPCShopCreateEvent en = new NPCShopCreateEvent(shop);

            Bukkit.getPluginManager().callEvent(en);

        } catch (Exception ex) {
            addChest(l);
        } finally {
            if (Core.useWorldGuard() && Config.useNPCOverride()) {
                denyMobs(shop.getLocation());
            }
            if (c) {
                Core.getWorldGuard().getGlobalConfiguration().activityHaltToggle = true;
            }
        }
    }


    public ShopsNPC(EntityType e, Shop s, boolean baby, Ocelot.Type type) {
        shop = s;
        l = shop.getLocation();
        boolean c = false;
        try {

            if (Core.useWorldGuard()) {
                if (Core.getWorldGuard().getGlobalConfiguration().activityHaltToggle) {
                    c = true;
                    Core.getWorldGuard().getGlobalConfiguration().activityHaltToggle = false;
                }
            }

            if (Core.useWorldGuard() && Config.useNPCOverride()) {
                allowMobs(shop.getLocation());
            } else if (Core.useWorldGuard()) {
                com.sk89q.worldguard.protection.ApplicableRegionSet set = Core.getRegionSet(shop.getLocation());

                if (!set.allows(DefaultFlag.MOB_SPAWNING)) {

                    shop.getOwner().getPlayer().sendMessage(Messages.getString("Prefix") + Messages.getString("WorldGuardDenyNPC"));
                    shop.setNPCShop(false);
                    return;
                }
            }
            entity = (LivingEntity) s.getLocation().getWorld().spawnEntity(s.getLocation().clone().add(.5, 0, .5), e);
            entity.setCustomName("§a§l" + s.getName());
            entity.setCustomNameVisible(true);
            entity.setRemoveWhenFarAway(false);
            entity.setCanPickupItems(false);

            entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 9999999, 9999));

            entity.getEquipment().setItemInHand(null);

            if (entity instanceof Ocelot) {
                ((Ocelot) entity).setCatType(type);
                if (baby) {
                    ((Ocelot) entity).setBaby();
                }
            }

            if (entity instanceof Monster) {
                ((Monster) entity).setTarget(null);

            }

            removeChest();



            NPCShopCreateEvent en = new NPCShopCreateEvent(shop);

            Bukkit.getPluginManager().callEvent(en);

        } catch (Exception ex) {
            addChest(l);
        } finally {
            if (Core.useWorldGuard() && Config.useNPCOverride()) {
                denyMobs(shop.getLocation());
            }
            if (c) {
                Core.getWorldGuard().getGlobalConfiguration().activityHaltToggle = true;
            }
        }
    }

    public ShopsNPC(EntityType e, Shop s, int size) {
        shop = s;
        l = shop.getLocation();
        boolean c = false;
        try {

            if (Core.useWorldGuard()) {
                if (Core.getWorldGuard().getGlobalConfiguration().activityHaltToggle) {
                    c = true;
                    Core.getWorldGuard().getGlobalConfiguration().activityHaltToggle = false;
                }
            }

            if (Core.useWorldGuard() && Config.useNPCOverride()) {
                allowMobs(shop.getLocation());
            } else if (Core.useWorldGuard()) {
                com.sk89q.worldguard.protection.ApplicableRegionSet set = Core.getRegionSet(shop.getLocation());

                if (!set.allows(DefaultFlag.MOB_SPAWNING)) {

                    shop.getOwner().getPlayer().sendMessage(Messages.getString("Prefix") + Messages.getString("WorldGuardDenyNPC"));
                    shop.setNPCShop(false);
                    return;
                }
            }
            entity = (LivingEntity) s.getLocation().getWorld().spawnEntity(s.getLocation().clone().add(.5, 0, .5), e);
            entity.setCustomName("§a§l" + s.getName());
            entity.setCustomNameVisible(true);
            entity.setRemoveWhenFarAway(false);
            entity.setCanPickupItems(false);

            entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 9999999, 9999));

            entity.getEquipment().setItemInHand(null);

            if (entity instanceof Slime) {
                ((Slime) entity).setSize(size);
            }

            if (entity instanceof MagmaCube) {
                ((MagmaCube) entity).setSize(size);

            }

            if (entity instanceof Monster) {
                ((Monster) entity).setTarget(null);

            }

            removeChest();

            NPCShopCreateEvent en = new NPCShopCreateEvent(shop);

            Bukkit.getPluginManager().callEvent(en);

        } catch (Exception ex) {
            addChest(l);
        } finally {
            if (Core.useWorldGuard() && Config.useNPCOverride()) {
                denyMobs(shop.getLocation());
            }
            if (c) {
                Core.getWorldGuard().getGlobalConfiguration().activityHaltToggle = true;
            }
        }
    }


    public ShopsNPC(EntityType e, Shop s, boolean baby, boolean villagerZombie) {
        shop = s;
        l = shop.getLocation();
        boolean c = false;
        try {

            if (Core.useWorldGuard()) {
                if (Core.getWorldGuard().getGlobalConfiguration().activityHaltToggle) {
                    c = true;
                    Core.getWorldGuard().getGlobalConfiguration().activityHaltToggle = false;
                }
            }

            if (Core.useWorldGuard() && Config.useNPCOverride()) {
                allowMobs(shop.getLocation());
            } else if (Core.useWorldGuard()) {
                com.sk89q.worldguard.protection.ApplicableRegionSet set = Core.getRegionSet(shop.getLocation());

                if (!set.allows(DefaultFlag.MOB_SPAWNING)) {

                    shop.getOwner().getPlayer().sendMessage(Messages.getString("Prefix") + Messages.getString("WorldGuardDenyNPC"));
                    shop.setNPCShop(false);
                    return;
                }
            }
            entity = (LivingEntity) s.getLocation().getWorld().spawnEntity(s.getLocation().clone().add(.5, 0, .5), e);
            entity.setCustomName("§a§l" + s.getName());
            entity.setCustomNameVisible(true);
            entity.setRemoveWhenFarAway(false);
            entity.setCanPickupItems(false);

            entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 9999999, 9999));

            entity.getEquipment().setItemInHand(null);

            if (entity instanceof Zombie) {
                ((Zombie) entity).setVillager(villagerZombie);
                ((Zombie) entity).setBaby(baby);
            }

            if (entity instanceof Chicken && baby) {
                ((Chicken) entity).setBaby();
            }

            if (entity instanceof Cow && baby) {
                ((Cow) entity).setBaby();
            }

            if (entity instanceof Horse && baby) {
                ((Horse) entity).setBaby();

            }

            if (entity instanceof Monster) {
                ((Monster) entity).setTarget(null);

            }

            removeChest();



            NPCShopCreateEvent en = new NPCShopCreateEvent(shop);

            Bukkit.getPluginManager().callEvent(en);

        } catch (Exception ex) {
            addChest(l);
        } finally {
            if (Core.useWorldGuard() && Config.useNPCOverride()) {
                denyMobs(shop.getLocation());
            }
            if (c) {
                Core.getWorldGuard().getGlobalConfiguration().activityHaltToggle = true;
            }
        }
    }

    public ShopsNPC(EntityType e, Shop s, boolean baby) {
        shop = s;
        l = shop.getLocation();
        boolean c = false;
        try {

            if (Core.useWorldGuard()) {
                if (Core.getWorldGuard().getGlobalConfiguration().activityHaltToggle) {
                    c = true;
                    Core.getWorldGuard().getGlobalConfiguration().activityHaltToggle = false;
                }
            }

            if (Core.useWorldGuard() && Config.useNPCOverride()) {
                allowMobs(shop.getLocation());
            } else if (Core.useWorldGuard()) {
                com.sk89q.worldguard.protection.ApplicableRegionSet set = Core.getRegionSet(shop.getLocation());

                if (!set.allows(DefaultFlag.MOB_SPAWNING)) {

                    shop.getOwner().getPlayer().sendMessage(Messages.getString("Prefix") + Messages.getString("WorldGuardDenyNPC"));
                    shop.setNPCShop(false);
                    return;
                }
            }
            entity = (LivingEntity) s.getLocation().getWorld().spawnEntity(s.getLocation().clone().add(.5, 0, .5), e);
            entity.setCustomName("§a§l" + s.getName());
            entity.setCustomNameVisible(true);
            entity.setRemoveWhenFarAway(false);
            entity.setCanPickupItems(false);

            entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 9999999, 9999));

            entity.getEquipment().setItemInHand(null);

            if (entity instanceof Zombie) {
                ((Zombie) entity).setBaby(baby);
            }


            if (entity instanceof Chicken && baby) {
                ((Chicken) entity).setBaby();
                ((Chicken) entity).setAgeLock(true);
            }

            if (entity instanceof Pig && baby) {
                ((Pig) entity).setBaby();
                ((Pig) entity).setAgeLock(true);
            }

            if (entity instanceof PigZombie) {
                ((PigZombie) entity).setBaby(baby);
                ((PigZombie) entity).setAngry(false);
            }

            if (entity instanceof Cow && baby) {
                ((Cow) entity).setBaby();
                ((Cow) entity).setAgeLock(true);
            }

            if (entity instanceof Horse && baby) {
                ((Horse) entity).setBaby();
                ((Horse) entity).setAgeLock(true);

            }

            if (entity instanceof Monster) {
                ((Monster) entity).setTarget(null);

            }

            removeChest();



            NPCShopCreateEvent en = new NPCShopCreateEvent(shop);

            Bukkit.getPluginManager().callEvent(en);


        } catch (Exception ex) {
            addChest(l);
        } finally {
            if (Core.useWorldGuard() && Config.useNPCOverride()) {
                denyMobs(shop.getLocation());
            }
            if (c) {
                Core.getWorldGuard().getGlobalConfiguration().activityHaltToggle = true;
            }
        }
    }

    public ShopsNPC(EntityType e, Shop s, boolean baby, Horse.Variant variant, Horse.Color color, Horse.Style style, boolean chest) {
        shop = s;
        l = shop.getLocation();
        boolean c = false;
        try {

            if (Core.useWorldGuard()) {
                if (Core.getWorldGuard().getGlobalConfiguration().activityHaltToggle) {
                    c = true;
                    Core.getWorldGuard().getGlobalConfiguration().activityHaltToggle = false;
                }
            }

            if (Core.useWorldGuard() && Config.useNPCOverride()) {
                allowMobs(shop.getLocation());
            } else if (Core.useWorldGuard()) {
                com.sk89q.worldguard.protection.ApplicableRegionSet set = Core.getRegionSet(shop.getLocation());

                if (!set.allows(DefaultFlag.MOB_SPAWNING)) {

                    shop.getOwner().getPlayer().sendMessage(Messages.getString("Prefix") + Messages.getString("WorldGuardDenyNPC"));
                    shop.setNPCShop(false);
                    return;
                }
            }
            entity = (LivingEntity) s.getLocation().getWorld().spawnEntity(s.getLocation().clone().add(.5, 0, .5), e);
            entity.setCustomName("§a§l" + s.getName());
            entity.setCustomNameVisible(true);
            entity.setRemoveWhenFarAway(false);
            entity.setCanPickupItems(false);

            entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 9999999, 9999));

            entity.getEquipment().setItemInHand(null);


            if (entity instanceof Horse) {
                if (baby)
                    ((Horse) entity).setBaby();
                if (color != null)
                    ((Horse) entity).setColor(color);
                if (style != null)
                    ((Horse) entity).setStyle(style);
                if (variant != null)
                    ((Horse) entity).setVariant(variant);

                ((Horse) entity).setCarryingChest(chest);
                ((Horse) entity).setAgeLock(true);

            }

            if (entity instanceof Monster) {
                ((Monster) entity).setTarget(null);

            }

            removeChest();



            NPCShopCreateEvent en = new NPCShopCreateEvent(shop);

            Bukkit.getPluginManager().callEvent(en);

        } catch (Exception ex) {
            addChest(l);
        } finally {
            if (Core.useWorldGuard() && Config.useNPCOverride()) {
                denyMobs(shop.getLocation());
            }
            if (c) {
                Core.getWorldGuard().getGlobalConfiguration().activityHaltToggle = true;
            }
        }
    }

    public ShopsNPC(EntityType e, Shop s, boolean sheared, boolean baby, DyeColor color) {
        shop = s;
        l = shop.getLocation();
        boolean c = false;
        try {

            if (Core.useWorldGuard()) {
                if (Core.getWorldGuard().getGlobalConfiguration().activityHaltToggle) {
                    c = true;
                    Core.getWorldGuard().getGlobalConfiguration().activityHaltToggle = false;
                }
            }

            if (Core.useWorldGuard() && Config.useNPCOverride()) {
                allowMobs(shop.getLocation());
            } else if (Core.useWorldGuard()) {
                com.sk89q.worldguard.protection.ApplicableRegionSet set = Core.getRegionSet(shop.getLocation());

                if (!set.allows(DefaultFlag.MOB_SPAWNING)) {

                    shop.getOwner().getPlayer().sendMessage(Messages.getString("Prefix") + Messages.getString("WorldGuardDenyNPC"));
                    shop.setNPCShop(false);
                    return;
                }
            }
            entity = (LivingEntity) s.getLocation().getWorld().spawnEntity(s.getLocation().clone().add(.5, 0, .5), e);
            entity.setCustomName("§a§l" + s.getName());
            entity.setCustomNameVisible(true);
            entity.setRemoveWhenFarAway(false);
            entity.setCanPickupItems(false);

            entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 9999999, 9999));

            entity.getEquipment().setItemInHand(null);

            if (entity instanceof Sheep) {
                ((Sheep) entity).setColor(color);
                ((Sheep) entity).setSheared(sheared);

                if (baby) {
                    ((Sheep) entity).setBaby();
                    ((Sheep) entity).setAgeLock(true);
                }
            }

            if (entity instanceof Monster) {
                ((Monster) entity).setTarget(null);

            }

            removeChest();



            NPCShopCreateEvent en = new NPCShopCreateEvent(shop);

            Bukkit.getPluginManager().callEvent(en);

        } catch (Exception ex) {
            addChest(l);
        } finally {
            if (Core.useWorldGuard() && Config.useNPCOverride()) {
                denyMobs(shop.getLocation());
            }
            if (c) {
                Core.getWorldGuard().getGlobalConfiguration().activityHaltToggle = true;
            }
        }
    }

    public ShopsNPC(EntityType e, Shop s, boolean baby, DyeColor color) {
        shop = s;
        l = shop.getLocation();
        boolean c = false;
        try {

            if (Core.useWorldGuard()) {
                if (Core.getWorldGuard().getGlobalConfiguration().activityHaltToggle) {
                    c = true;
                    Core.getWorldGuard().getGlobalConfiguration().activityHaltToggle = false;
                }
            }

            if (Core.useWorldGuard() && Config.useNPCOverride()) {
                allowMobs(shop.getLocation());
            } else if (Core.useWorldGuard()) {
                com.sk89q.worldguard.protection.ApplicableRegionSet set = Core.getRegionSet(shop.getLocation());

                if (!set.allows(DefaultFlag.MOB_SPAWNING)) {

                    shop.getOwner().getPlayer().sendMessage(Messages.getString("Prefix") + Messages.getString("WorldGuardDenyNPC"));
                    shop.setNPCShop(false);
                    return;
                }
            }
            entity = (LivingEntity) s.getLocation().getWorld().spawnEntity(s.getLocation().clone().add(.5, 0, .5), e);
            entity.setCustomName("§a§l" + s.getName());
            entity.setCustomNameVisible(true);
            entity.setRemoveWhenFarAway(false);
            entity.setCanPickupItems(false);

            entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 9999999, 9999));

            entity.getEquipment().setItemInHand(null);

            if (entity instanceof Sheep) {
                ((Sheep) entity).setColor(color);
                if (baby) {
                    ((Sheep) entity).setBaby();
                    ((Sheep) entity).setAgeLock(true);
                }
            }

            if (entity instanceof Wolf) {
                ((Wolf) entity).setTamed(true);
                ((Wolf) entity).setCollarColor(color);
                if (baby) {
                    ((Wolf) entity).setBaby();
                    ((Wolf) entity).setAgeLock(true);
                }
            }

            if (entity instanceof Monster) {
                ((Monster) entity).setTarget(null);

            }

            removeChest();



            NPCShopCreateEvent en = new NPCShopCreateEvent(shop);

            Bukkit.getPluginManager().callEvent(en);

        } catch (Exception ex) {
            addChest(l);
        } finally {
            if (Core.useWorldGuard() && Config.useNPCOverride()) {
                denyMobs(shop.getLocation());
            }
            if (c) {
                Core.getWorldGuard().getGlobalConfiguration().activityHaltToggle = true;
            }
        }
    }

    public ShopsNPC(EntityType e, Shop s, boolean baby, Rabbit.Type rabbitType) {
        shop = s;
        l = shop.getLocation();

        boolean c = false;
        try {

            if (Core.useWorldGuard()) {
                if (Core.getWorldGuard().getGlobalConfiguration().activityHaltToggle) {
                    c = true;
                    Core.getWorldGuard().getGlobalConfiguration().activityHaltToggle = false;
                }
            }

            if (Core.useWorldGuard() && Config.useNPCOverride()) {
                allowMobs(shop.getLocation());
            } else if (Core.useWorldGuard()) {
                com.sk89q.worldguard.protection.ApplicableRegionSet set = Core.getRegionSet(shop.getLocation());

                if (!set.allows(DefaultFlag.MOB_SPAWNING)) {

                    shop.getOwner().getPlayer().sendMessage(Messages.getString("Prefix") + Messages.getString("WorldGuardDenyNPC"));
                    shop.setNPCShop(false);
                    return;
                }
            }


            entity = (LivingEntity) s.getLocation().getWorld().spawnEntity(s.getLocation().clone().add(.5, 0, .5), e);
            entity.setCustomName("§a§l" + s.getName());
            entity.setCustomNameVisible(true);
            entity.setRemoveWhenFarAway(false);
            entity.setCanPickupItems(false);

            entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 9999999, 9999));

            entity.getEquipment().setItemInHand(null);

            if (entity instanceof Rabbit) {
                ((Rabbit) entity).setRabbitType(rabbitType);

                if (baby) {
                    ((Rabbit) entity).setBaby();
                    ((Rabbit) entity).setAgeLock(true);
                }
            }

            if (entity instanceof Monster) {
                ((Monster) entity).setTarget(null);

            }

            removeChest();



            NPCShopCreateEvent en = new NPCShopCreateEvent(shop);

            Bukkit.getPluginManager().callEvent(en);

        } catch (Exception ex) {
            addChest(l);
        } finally {
            if (Core.useWorldGuard() && Config.useNPCOverride()) {
                denyMobs(shop.getLocation());
            }
            if (c) {
                Core.getWorldGuard().getGlobalConfiguration().activityHaltToggle = true;
            }
        }
    }

    public ShopsNPC(EntityType e, Shop s, boolean baby, Villager.Profession pro) {
        shop = s;
        l = shop.getLocation();

        boolean c = false;
        try {

            if (Core.useWorldGuard()) {
                if (Core.getWorldGuard().getGlobalConfiguration().activityHaltToggle) {
                    c = true;
                    Core.getWorldGuard().getGlobalConfiguration().activityHaltToggle = false;
                }
            }

            if (Core.useWorldGuard() && Config.useNPCOverride()) {
                allowMobs(shop.getLocation());
            } else if (Core.useWorldGuard()) {
                com.sk89q.worldguard.protection.ApplicableRegionSet set = Core.getRegionSet(shop.getLocation());

                if (!set.allows(DefaultFlag.MOB_SPAWNING)) {

                    shop.getOwner().getPlayer().sendMessage(Messages.getString("Prefix") + Messages.getString("WorldGuardDenyNPC"));
                    shop.setNPCShop(false);
                    return;
                }
            }


            entity = (LivingEntity) s.getLocation().getWorld().spawnEntity(s.getLocation().clone().add(.5, 0, .5), e);
            entity.setCustomName("§a§l" + s.getName());
            entity.setCustomNameVisible(true);
            entity.setRemoveWhenFarAway(false);
            entity.setCanPickupItems(false);

            entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 9999999, 9999));

            entity.getEquipment().setItemInHand(null);

            if (entity instanceof Villager) {
                ((Villager) entity).setProfession(pro);

                if (baby) {
                    ((Villager) entity).setBaby();
                    ((Villager) entity).setAgeLock(true);
                }
            }

            if (entity instanceof Monster) {
                ((Monster) entity).setTarget(null);

            }

            removeChest();



            NPCShopCreateEvent en = new NPCShopCreateEvent(shop);

            Bukkit.getPluginManager().callEvent(en);

        } catch (Exception ex) {
            addChest(l);
        } finally {
            if (Core.useWorldGuard() && Config.useNPCOverride()) {
                denyMobs(shop.getLocation());
            }
            if (c) {
                Core.getWorldGuard().getGlobalConfiguration().activityHaltToggle = true;
            }
        }
    }

    public void spawn() {
        if (!entity.isValid() && entity.isDead()) {

            NPCs.removeNPC(this);

            EntityType type = entity.getType();

            boolean baby = false;
            boolean shear = false;
            boolean villager = false;
            int size = 1;
            DyeColor dyeColor = null;
            Horse.Color horseColor = null;
            Horse.Variant horseVar = null;
            Horse.Style horseStyle = null;
            Rabbit.Type rabbitType = null;
            Ocelot.Type catType = null;
            Skeleton.SkeletonType skeletonType = Skeleton.SkeletonType.NORMAL;
            Villager.Profession prof = null;

            if (entity instanceof Ageable) {
                baby = !((Ageable) entity).isAdult();
            }
            if (entity instanceof Sheep) {
                shear = ((Sheep) entity).isSheared();
                dyeColor = ((Sheep) entity).getColor();
            }
            if (entity instanceof Zombie) {
                villager = ((Zombie) entity).isVillager();
            }
            if (entity instanceof Slime) {
                size = ((Slime) entity).getSize();
            }
            if (entity instanceof MagmaCube) {
                size = ((MagmaCube) entity).getSize();
            }
            if (entity instanceof Wolf) {
                dyeColor = ((Wolf) entity).getCollarColor();
            }
            if (entity instanceof Horse) {
                horseColor = ((Horse) entity).getColor();
                horseStyle = ((Horse) entity).getStyle();
                horseVar = ((Horse) entity).getVariant();
            }
            if (entity instanceof Rabbit) {
                rabbitType = ((Rabbit) entity).getRabbitType();
            }
            if (entity instanceof Ocelot) {
                catType = ((Ocelot) entity).getCatType();
            }
            if (entity instanceof Skeleton) {
                skeletonType = ((Skeleton) entity).getSkeletonType();
            }
            if (entity instanceof Villager) {
                prof = ((Villager) entity).getProfession();
            }

            if (type == EntityType.VILLAGER) {
                me.moomaxie.BetterShops.ShopTypes.NPC.NPCs.addNPC(new ShopsNPC(type, shop, baby, prof));
            } else if (type == EntityType.OCELOT) {
                me.moomaxie.BetterShops.ShopTypes.NPC.NPCs.addNPC(new ShopsNPC(type, shop, baby, catType));
            } else if (type == EntityType.WOLF) {
                me.moomaxie.BetterShops.ShopTypes.NPC.NPCs.addNPC(new ShopsNPC(type, shop, baby, dyeColor));
            } else if (type == EntityType.ZOMBIE) {
                me.moomaxie.BetterShops.ShopTypes.NPC.NPCs.addNPC(new ShopsNPC(type, shop, baby, villager));
            } else if (type == EntityType.SLIME || type == EntityType.MAGMA_CUBE) {
                me.moomaxie.BetterShops.ShopTypes.NPC.NPCs.addNPC(new ShopsNPC(type, shop, size));
            } else if (type == EntityType.HORSE) {
                me.moomaxie.BetterShops.ShopTypes.NPC.NPCs.addNPC(new ShopsNPC(type, shop, baby, horseVar, horseColor, horseStyle, false));
            } else if (type == EntityType.SHEEP) {
                me.moomaxie.BetterShops.ShopTypes.NPC.NPCs.addNPC(new ShopsNPC(type, shop, shear, baby, dyeColor));
            } else if (type == EntityType.SKELETON) {
                me.moomaxie.BetterShops.ShopTypes.NPC.NPCs.addNPC(new ShopsNPC(type, shop, skeletonType));
            } else if (type == EntityType.RABBIT) {
                me.moomaxie.BetterShops.ShopTypes.NPC.NPCs.addNPC(new ShopsNPC(type, shop, baby, rabbitType));
            } else if (type == EntityType.COW || type == EntityType.CHICKEN || type == EntityType.MUSHROOM_COW || type == EntityType.PIG_ZOMBIE || type == EntityType.PIG) {
                me.moomaxie.BetterShops.ShopTypes.NPC.NPCs.addNPC(new ShopsNPC(type, shop, baby));
            } else {
                me.moomaxie.BetterShops.ShopTypes.NPC.NPCs.addNPC(new ShopsNPC(type, shop));
            }

        }
    }

    public void returnNPC() {

        if (!entity.isValid() && entity.isDead()) {
            spawn();
        } else {
            if (entity instanceof Monster) {
                ((Monster) entity).setTarget(null);
            }
            entity.setFireTicks(0);
            entity.setFallDistance(0);
            entity.getEquipment().setItemInHand(null);
            if (entity.getLocation().distance(l) > .75) {
                entity.teleport(l.clone().add(.5, 0, .5));
            }
        }
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public Shop getShop() {
        return ShopManager.fromString(shop.getName());
    }

    public static void addChest(Location l) {
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

                s.setLine(0, MainGUI.getString("SignLine1"));
                s.setLine(1, MainGUI.getString("SignLine2"));
                s.setLine(2, MainGUI.getString("SignLine3Open"));

                s.setLine(3, MainGUI.getString("SignLine4"));

                s.update();
            }
        }
    }

    public void allowMobs(Location l) {
        com.sk89q.worldguard.protection.ApplicableRegionSet set = Core.getRegionSet(l);

        for (com.sk89q.worldguard.protection.regions.ProtectedRegion r : set) {
            r.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.MOB_SPAWNING, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
        }
    }

    public void denyMobs(Location l) {
        com.sk89q.worldguard.protection.ApplicableRegionSet set = Core.getRegionSet(l);

        for (com.sk89q.worldguard.protection.regions.ProtectedRegion r : set) {
            r.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.MOB_SPAWNING, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
        }
    }

    public void removeChest() {
        if (shop.getLocation().getBlock().getRelative(1, 0, 0).getType() == Material.WALL_SIGN) {
            Sign s = (Sign) shop.getLocation().getBlock().getRelative(1, 0, 0).getState();

            if (s.getLine(0).contains(MainGUI.getString("SignLine1"))) {
                if (s.getLine(3).contains(MainGUI.getString("SignLine4"))) {
                    if (s.getLine(1).contains(MainGUI.getString("SignLine2"))) {
                        s.getBlock().setType(Material.AIR);
                    }
                }
            }
        }

        if (shop.getLocation().getBlock().getRelative(-1, 0, 0).getType() == Material.WALL_SIGN) {
            Sign s = (Sign) shop.getLocation().getBlock().getRelative(-1, 0, 0).getState();

            if (s.getLine(0).contains(MainGUI.getString("SignLine1"))) {
                if (s.getLine(3).contains(MainGUI.getString("SignLine4"))) {
                    if (s.getLine(1).contains(MainGUI.getString("SignLine2"))) {
                        s.getBlock().setType(Material.AIR);
                    }
                }
            }
        }

        if (shop.getLocation().getBlock().getRelative(0, 0, 1).getType() == Material.WALL_SIGN) {
            Sign s = (Sign) shop.getLocation().getBlock().getRelative(0, 0, 1).getState();

            if (s.getLine(0).contains(MainGUI.getString("SignLine1"))) {
                if (s.getLine(3).contains(MainGUI.getString("SignLine4"))) {
                    if (s.getLine(1).contains(MainGUI.getString("SignLine2"))) {
                        s.getBlock().setType(Material.AIR);
                    }
                }
            }
        }

        if (shop.getLocation().getBlock().getRelative(0, 0, -1).getType() == Material.WALL_SIGN) {
            Sign s = (Sign) shop.getLocation().getBlock().getRelative(0, 0, -1).getState();

            if (s.getLine(0).contains(MainGUI.getString("SignLine1"))) {
                if (s.getLine(3).contains(MainGUI.getString("SignLine4"))) {
                    if (s.getLine(1).contains(MainGUI.getString("SignLine2"))) {
                        s.getBlock().setType(Material.AIR);
                    }
                }
            }
        }

        shop.getLocation().getBlock().setType(Material.AIR);
    }
}
