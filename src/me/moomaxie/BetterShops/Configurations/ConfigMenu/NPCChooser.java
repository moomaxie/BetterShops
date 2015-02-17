package me.moomaxie.BetterShops.Configurations.ConfigMenu;

import me.moomaxie.BetterShops.Configurations.Config;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
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
public class NPCChooser implements Listener {

    public static void openNPCConfigChooser(Player p) {
        Inventory inv = Bukkit.createInventory(p, 54, "§7[Shop] §dChoose NPCs");

        //Glass
        ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7);
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.setDisplayName(" ");
        glass.setItemMeta(glassMeta);

        for (int i = 0; i < 9; i++) {
            inv.setItem(i, glass);
        }

        for (EntityType type : Config.getAllNPCs()) {
            ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 5);
            ItemMeta meta = item.getItemMeta();
            if (Config.useNPC(type)) {
                meta.setDisplayName("§e" + type.name().replaceAll("_", " "));
                meta.setLore(Arrays.asList("§aON" , "§e§lClick §7to Toggle"));
            } else {
                item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 14);
                meta.setDisplayName("§e" + type.name().replaceAll("_", " "));
                meta.setLore(Arrays.asList("§cOFF" , "§e§lClick §7to Toggle"));
            }

            item.setItemMeta(meta);

            inv.setItem(inv.firstEmpty(), item);
        }

        p.openInventory(inv);
    }

    @EventHandler
    public void onToggleUseNPC(InventoryClickEvent e){
        if (e.getInventory().getName().equals("§7[Shop] §dChoose NPCs")){
            if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR){
                e.setCancelled(true);

                if (e.getCurrentItem().getItemMeta().getLore() != null && e.getCurrentItem().getItemMeta().getLore().contains("§e§lClick §7to Toggle")){
                    EntityType type = EntityType.valueOf(e.getCurrentItem().getItemMeta().getDisplayName().substring(2).replaceAll(" ","_"));

                    Config.setUseNPC(type,!Config.useNPC(type));

                    openNPCConfigChooser((Player) e.getWhoClicked());
                }
            }
        }
    }
}
