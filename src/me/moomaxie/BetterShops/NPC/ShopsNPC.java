package me.moomaxie.BetterShops.NPC;

import me.moomaxie.BetterShops.Configurations.GUIMessages.MainGUI;
import me.moomaxie.BetterShops.Configurations.ShopLimits;
import me.moomaxie.BetterShops.Core;
import me.moomaxie.BetterShops.Shops.Shop;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
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

    LivingEntity entity;
    Shop shop;
    Location l;

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

            entity = (LivingEntity) s.getLocation().getWorld().spawnEntity(s.getLocation(),e);

            entity.setCustomName("§a§l" + shop.getName());
            entity.setCustomNameVisible(true);
            entity.setRemoveWhenFarAway(false);

            entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 9999999, 9999));
            entity.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 9999999, 9999));
            entity.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 9999999, 9999));

            entity.getEquipment().setItemInHand(null);

            if (entity instanceof Monster){
                ((Monster) entity).setTarget(null);


            }

        } catch (Exception ex){
            addChest(l);
        } finally {
            if (c) {
                Core.getWorldGuard().getGlobalConfiguration().activityHaltToggle = true;
            }
        }


//        entity = (LivingEntity) s.getLocation().getWorld().spawnEntity(s.getLocation(),e);
//        entity.setCustomName("§a§l" + s.getName());
//        entity.setCustomNameVisible(true);
//        entity.setRemoveWhenFarAway(false);
//        entity.setCanPickupItems(false);
//
//        entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 9999999, 9999));
//        entity.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 9999999, 9999));
//        entity.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 9999999, 9999));
//
//        entity.getEquipment().setItemInHand(null);
//
//        if (entity instanceof Monster){
//            ((Monster) entity).setTarget(null);
//
//        }


    }

    public ShopsNPC(EntityType e, Shop s, Skeleton.SkeletonType skType) {
        shop = s;
        l = shop.getLocation();
        entity = (LivingEntity) s.getLocation().getWorld().spawnEntity(s.getLocation(),e);
        entity.setCustomName("§a§l" + s.getName());
        entity.setCustomNameVisible(true);
        entity.setRemoveWhenFarAway(false);
        entity.setCanPickupItems(false);

        entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 9999999, 9999));
        entity.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 9999999, 9999));
        entity.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 9999999, 9999));

        entity.getEquipment().setItemInHand(null);

        if (entity instanceof Skeleton){
            ((Skeleton) entity).setSkeletonType(skType);
        }

        if (entity instanceof Monster){
            ((Monster) entity).setTarget(null);

        }


    }

    public ShopsNPC(EntityType e, Shop s,  Ocelot.Type type) {
        shop = s;
        l = shop.getLocation();
        entity = (LivingEntity) s.getLocation().getWorld().spawnEntity(s.getLocation(),e);
        entity.setCustomName("§a§l" + s.getName());
        entity.setCustomNameVisible(true);
        entity.setRemoveWhenFarAway(false);
        entity.setCanPickupItems(false);

        entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 9999999, 9999));
        entity.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 9999999, 9999));
        entity.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 9999999, 9999));

        entity.getEquipment().setItemInHand(null);

        if (entity instanceof Ocelot){
            ((Ocelot) entity).setCatType(type);
        }

        if (entity instanceof Monster){
            ((Monster) entity).setTarget(null);

        }


    }

    public ShopsNPC(EntityType e, Shop s, int size) {
        shop = s;
        l = shop.getLocation();
        entity = (LivingEntity) s.getLocation().getWorld().spawnEntity(s.getLocation(),e);
        entity.setCustomName("§a§l" + s.getName());
        entity.setCustomNameVisible(true);
        entity.setRemoveWhenFarAway(false);
        entity.setCanPickupItems(false);

        entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 9999999, 9999));
        entity.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 9999999, 9999));
        entity.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 9999999, 9999));

        entity.getEquipment().setItemInHand(null);

        if (entity instanceof Slime){
            ((Slime) entity).setSize(size);
        }

        if (entity instanceof MagmaCube){
            ((MagmaCube) entity).setSize(size);
        }

        if (entity instanceof Monster){
            ((Monster) entity).setTarget(null);

        }


    }

    public ShopsNPC(EntityType e, Shop s, boolean sheared, DyeColor color) {
        shop = s;
        l = shop.getLocation();
        entity = (LivingEntity) s.getLocation().getWorld().spawnEntity(s.getLocation(),e);
        entity.setCustomName("§a§l" + s.getName());
        entity.setCustomNameVisible(true);
        entity.setRemoveWhenFarAway(false);
        entity.setCanPickupItems(false);

        entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 9999999, 9999));
        entity.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 9999999, 9999));
        entity.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 9999999, 9999));

        entity.getEquipment().setItemInHand(null);

        if (entity instanceof Sheep){
            ((Sheep) entity).setColor(color);
            ((Sheep) entity).setSheared(sheared);
        }

        if (entity instanceof Monster){
            ((Monster) entity).setTarget(null);

        }


    }

    public ShopsNPC(EntityType e, Shop s, DyeColor color) {
        shop = s;
        l = shop.getLocation();
        entity = (LivingEntity) s.getLocation().getWorld().spawnEntity(s.getLocation(),e);
        entity.setCustomName("§a§l" + s.getName());
        entity.setCustomNameVisible(true);
        entity.setRemoveWhenFarAway(false);
        entity.setCanPickupItems(false);

        entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 9999999, 9999));
        entity.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 9999999, 9999));
        entity.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 9999999, 9999));

        entity.getEquipment().setItemInHand(null);

        if (entity instanceof Sheep){
            ((Sheep) entity).setColor(color);
        }

        if (entity instanceof Wolf){
            ((Wolf) entity).setCollarColor(color);
        }



        if (entity instanceof Monster){
            ((Monster) entity).setTarget(null);

        }


    }

    public ShopsNPC(EntityType e, Shop s, Villager.Profession pro) {
        shop = s;
        l = shop.getLocation();
        entity = (LivingEntity) s.getLocation().getWorld().spawnEntity(s.getLocation(),e);
        entity.setCustomName("§a§l" + s.getName());
        entity.setCustomNameVisible(true);
        entity.setRemoveWhenFarAway(false);
        entity.setCanPickupItems(false);

        entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 9999999, 9999));
        entity.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 9999999, 9999));
        entity.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 9999999, 9999));

        entity.getEquipment().setItemInHand(null);

        if (entity instanceof Villager){
            ((Villager) entity).setProfession(pro);
        }


        if (entity instanceof Monster){
            ((Monster) entity).setTarget(null);

        }


    }

    public void spawn(){
        if (!entity.isValid()){
            boolean c = false;
            try {


                if (Core.useWorldGuard()) {
                    if (Core.getWorldGuard().getGlobalConfiguration().activityHaltToggle) {
                        c = true;
                        Core.getWorldGuard().getGlobalConfiguration().activityHaltToggle = false;
                    }
                }

                entity = (LivingEntity) shop.getLocation().getWorld().spawnEntity(shop.getLocation(), entity.getType());

                entity.setCustomName("§a§l" + shop.getName());
                entity.setCustomNameVisible(true);
                entity.setRemoveWhenFarAway(false);

                entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 9999999, 9999));
                entity.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 9999999, 9999));
                entity.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 9999999, 9999));

                entity.getEquipment().setItemInHand(null);

                if (entity instanceof Monster){
                    ((Monster) entity).setTarget(null);


                }

            } catch (Exception e){
                addChest(l);
            } finally {
                if (c) {
                    Core.getWorldGuard().getGlobalConfiguration().activityHaltToggle = true;
                }
            }
        }
    }

    public void returnNPC(){

        if (!entity.isValid()){
            spawn();
        } else {
            if (entity instanceof Monster){
                ((Monster) entity).setTarget(null);
            }
            entity.setLastDamage(0);
            entity.setFireTicks(0);
            entity.setFallDistance(0);
            entity.getEquipment().setItemInHand(null);
            if (entity.getLocation().distance(l) > .5) {
                entity.teleport(l);
            }
        }
    }

    public LivingEntity getEntity(){
        return entity;
    }

    public Shop getShop(){
        return ShopLimits.fromString(shop.getName());
    }

    public static void addChest(Location l) {
        l.getBlock().setType(Material.CHEST);

        Chest chest = (Chest) l.getBlock().getState();

        org.bukkit.material.Chest c = (org.bukkit.material.Chest) chest.getData();

        BlockFace face = c.getFacing();

        c.setFacingDirection(face.getOppositeFace());

        chest.setData(c);

        chest.update();

        Block b = chest.getBlock().getRelative(face.getOppositeFace());

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

                s.setLine(3, MainGUI.getString("SignLine4Open"));

                s.update();
            }
        }
    }
}
