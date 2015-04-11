package me.moomaxie.BetterShops.ShopTypes.NPC;

import me.moomaxie.BetterShops.Configurations.Config;
import me.moomaxie.BetterShops.Configurations.GUIMessages.MainGUI;
import me.moomaxie.BetterShops.Configurations.Messages;
import me.moomaxie.BetterShops.Configurations.Permissions.Permissions;
import me.moomaxie.BetterShops.Configurations.ShopLimits;
import me.moomaxie.BetterShops.Shops.Shop;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2014. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class NPCMenu implements Listener {

    public static void openNPCMenu(Shop shop, Player p) {

        Inventory inv = Bukkit.createInventory(p, 54, MainGUI.getString("ShopHeader") + shop.getName());

        //Glass
        org.bukkit.inventory.ItemStack glass = new org.bukkit.inventory.ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7);
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.setDisplayName(" ");
        glass.setItemMeta(glassMeta);

        for (int i = 0; i < 9; i++) {
            inv.setItem(i, glass);
        }

        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName(MainGUI.getString("BackArrow"));
        back.setItemMeta(backMeta);

        inv.setItem(0, back);

        for (EntityType type : Config.getNPCs()) {
            boolean can = true;

            if (Config.usePerms()) {
                can = Permissions.hasNPCTypePerm(type, p);
            }

            if (can) {
                ItemStack item = new ItemStack(Material.MONSTER_EGG, 1, type.getTypeId());
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName("§e" + type.name().replaceAll("_", " "));
                meta.setLore(Arrays.asList(MainGUI.getString("ChooseNPC")));
                item.setItemMeta(meta);

                inv.setItem(inv.firstEmpty(), item);
            }
        }

        p.openInventory(inv);
    }

    @EventHandler
    public void onChooseNPC(InventoryClickEvent e) {
        final Player p = (Player) e.getWhoClicked();
        if (e.getInventory().getName().contains(MainGUI.getString("ShopHeader"))) {
            e.setCancelled(true);

            if (e.getInventory().getType() == InventoryType.CHEST) {
                if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
                    String name = e.getInventory().getName();
                    name = name.substring(11);

                    final Shop shop = ShopLimits.fromString(p, name);

                    if (e.getCurrentItem().getItemMeta().getLore() != null && e.getCurrentItem().getItemMeta().getLore().contains(MainGUI.getString("ChooseNPC"))) {
                        EntityType type = EntityType.valueOf(e.getCurrentItem().getItemMeta().getDisplayName().substring(2).replaceAll(" ", "_").toUpperCase());

                        if (type == EntityType.VILLAGER) {
                            TypeSpecifier.openVillagerSpecifier(p, shop, type, false, null);
                        } else if (type == EntityType.OCELOT) {
                            TypeSpecifier.openCatSpecifier(p, shop, type, false, null);
                        } else if (type == EntityType.WOLF) {
                            TypeSpecifier.openDogSpecifier(p, shop, type, false, null);
                        } else if (type == EntityType.ZOMBIE) {
                            TypeSpecifier.openZombieSpecifier(p, shop, type, false, false);
                        } else if (type == EntityType.SLIME || type == EntityType.MAGMA_CUBE) {
                            TypeSpecifier.openSlimeSpecifier(p, shop, type, 1);
                        } else if (type == EntityType.HORSE) {
                            TypeSpecifier.openHorseSpecifier(p, shop, type, false, null, null, null);
                        } else if (type == EntityType.SHEEP) {
                            TypeSpecifier.openSheepSpecifier(p, shop, type, false, false, null);
                        } else if (type == EntityType.SKELETON) {
                            TypeSpecifier.openSkeletonSpecifier(p, shop, type, null);
                        } else if (type == EntityType.RABBIT) {
                            TypeSpecifier.openRabbitSpecifier(p, shop, type, false, null);
                        } else if (type == EntityType.COW || type == EntityType.CHICKEN || type == EntityType.MUSHROOM_COW || type == EntityType.PIG_ZOMBIE || type == EntityType.PIG) {
                            TypeSpecifier.openBabySpecifier(p, shop, type, false);
                        } else {
                            NPCs.addNPC(new ShopsNPC(type, shop));

                            p.closeInventory();
                            shop.setNPCShop(true);
                            shop.setOpen(true);
                            p.sendMessage(Messages.getString("Prefix") + Messages.getString("NPCShop").replaceAll("<Value>", "§aOn"));

                        }
                    }
                }
            }
        }
    }
}
