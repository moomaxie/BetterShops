package max.hubbard.bettershops.Shops.Types.NPC;

import max.hubbard.bettershops.Configurations.Language;
import max.hubbard.bettershops.Events.NPCShopCreateEvent;
import max.hubbard.bettershops.Shops.Shop;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.DespawnReason;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.LookClose;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
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
public class CitizensShop implements ShopsNPC {

    private Shop shop;
    private EntityInfo info;
    private NPC npc;
    private LivingEntity entity;

    public CitizensShop(EntityInfo info, Shop shop) {
        this.shop = shop;
        this.info = info;
        if (info.getType() == EntityType.PLAYER) {
            npc = CitizensAPI.getNPCRegistry().createNPC(info.getType(), "§a§l" + shop.getName());
        } else {
            npc = CitizensAPI.getNPCRegistry().createNPC(info.getType(), "§a§l" + shop.getName());
        }
        shop.setObject("NPCInfo", info.toString());
    }

    public CitizensShop(NPC n, LivingEntity ent, Shop s) {
        shop = s;
        npc = n;
        entity = ent;
        try {
            info = EntityInfo.getInfo(ent);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public void setInfo(EntityInfo info) {

        this.info = info;
        npc.setName("§a§l" + shop.getName());
        entity.setCustomNameVisible(true);
        entity.setRemoveWhenFarAway(false);
        entity.setCanPickupItems(false);
        LookClose lookclose = new LookClose();
        lookclose.setRealisticLooking(true);
        lookclose.lookClose(true);
        npc.addTrait(lookclose);


        List<String> lore = info.getLore();
        EntityType e = info.getType();

        try {

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

            if (Ageable.class.isAssignableFrom(e.getEntityClass()) && e != EntityType.ZOMBIE) {
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
        } catch (Exception e1) {

        }
    }

    public void spawn() {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
            @Override
            public void run() {


                npc.spawn(shop.getLocation().clone().add(.5, 0, .5));
                npc.setProtected(true);
                npc.setName("§a§l" + shop.getName());
                LookClose lookclose = new LookClose();
                lookclose.setRealisticLooking(true);
                lookclose.lookClose(true);
                npc.addTrait(lookclose);
                entity = (LivingEntity) npc.getEntity();

                setInfo(info);

                NPCShopCreateEvent en = new NPCShopCreateEvent(shop);

                Bukkit.getPluginManager().callEvent(en);
            }
        });
    }

    @Override
    public Shop getShop() {
        return shop;
    }

    @Override
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

    public NPC getNPC() {
        return npc;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public EntityInfo getInfo() {
        return info;
    }

    public void returnNPC() {
        if (npc != null)
            if (!npc.isSpawned()) {
                spawn();
            } else {
                npc.getEntity().setFireTicks(0);
                npc.getEntity().setFallDistance(0);
                if (npc.getEntity().getLocation().distance(shop.getLocation().clone()) > .75) {
                    npc.despawn(DespawnReason.PLUGIN);
                    spawn();
                }
            }
    }
}
