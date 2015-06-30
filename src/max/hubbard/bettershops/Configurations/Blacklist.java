package max.hubbard.bettershops.Configurations;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
public class Blacklist implements Listener {

    private static File file = new File(Bukkit.getPluginManager().getPlugin("BetterShops").getDataFolder(), "blacklist.yml");
    private static YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

    public int getNextId() {
        return config.getInt("NextId");
    }

    public void addItem(ItemStack item) {
        ItemStack clone = item.clone();
        clone.setAmount(1);
        config.getConfigurationSection("Items").set("" + getNextId(), clone);
        config.set("NextId", getNextId() + 1);

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeItem(int id) {
        config.getConfigurationSection("Items").set("" + id, null);

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeItem(ItemStack item) {
        for (String s : config.getConfigurationSection("Items").getKeys(false)) {
            if (config.getConfigurationSection("Items").isItemStack(s)) {
                if (item.equals(config.getConfigurationSection("Items").getItemStack(s))) {
                    config.getConfigurationSection("Items").set(s, null);

                    try {
                        config.save(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    }

    public static boolean isBlacklisted(ItemStack ite) {

        ItemStack item = ite.clone();
        item.setAmount(1);

        if (getItems().contains(item)) {
            return true;
        } else {
            for (ItemStack it : getItems()) {
                if (it.getItemMeta() == null && it.getType() == item.getType()) {
                    return true;
                } else if (it.getItemMeta() != null) {
                    if (it.getItemMeta().getDisplayName() == null && it.getType() == item.getType() && it.getData().getData() == item.getData().getData() && it.getDurability() == item.getDurability()) {
                        return true;
                    }
                    if (it.getItemMeta().getLore() == null && it.getType() == item.getType() && it.getData().getData() == item.getData().getData() && it.getDurability() == item.getDurability()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static List<ItemStack> getItems() {
        List<ItemStack> items = new ArrayList<>();

        if (!config.isConfigurationSection("Items")){
            config.createSection("Items");
            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return items;
        }

        for (String s : config.getConfigurationSection("Items").getKeys(false)) {
            if (config.getConfigurationSection("Items").isItemStack(s)) {
                items.add(config.getConfigurationSection("Items").getItemStack(s));
            }
        }

        return items;
    }

    public static void openBlacklistInventory(Inventory inv, Player p, int page) {
        if (inv == null) {
            inv = Bukkit.createInventory(p, 54, "§7[BetterShops] §dBlacklist");
        } else {
            inv.clear();
        }

        //Glass
        ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7);
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.setDisplayName(" ");
        glass.setItemMeta(glassMeta);

        for (int i = 0; i < 9; i++) {
            inv.setItem(i, glass);
        }

        ItemStack arrow = new ItemStack(Material.ARROW);
        ItemMeta arrowMeta = arrow.getItemMeta();
        arrowMeta.setDisplayName(Language.getString("MainGUI", "NextPage"));
        arrow.setItemMeta(arrowMeta);

        ItemStack barrow = new ItemStack(Material.ARROW);
        ItemMeta barrowMeta = barrow.getItemMeta();
        barrowMeta.setDisplayName(Language.getString("MainGUI", "PreviousPage"));
        barrow.setItemMeta(barrowMeta);

        ItemStack it = new ItemStack(Material.CHEST, 1, (byte) 7);
        ItemMeta itMeta = it.getItemMeta();
        itMeta.setDisplayName("§eBlacklist");
        itMeta.setLore(Arrays.asList("§e§lClick §7an item to §cRemove §7it", "§e§lClick §7your item to §aAdd §7it", "§ePage: §7" + page));
        it.setItemMeta(itMeta);

        inv.setItem(4, it);

        if (page > 1) {
            inv.setItem(0, barrow);
        }

        if (!config.isConfigurationSection("Items")){
            config.createSection("Items");
            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        int maxPage = (int) Math.ceil((double) (config.getConfigurationSection("Items").getKeys(false).size()) / 45);

        if (page != maxPage && maxPage != 0) {
            inv.setItem(8, arrow);
        }

        int j = 0;

        if (page > 1) {
            j = 45 * (page - 1);
        }

        Object[] array = getItems().toArray();

        int k = array.length;

        if (k > 0) {
            if (page != maxPage) {
                k = k - (j + (array.length - 46));
            }


            for (int i = j; i < k; i++) {
                ItemStack ite = (ItemStack) array[i];
                inv.setItem(inv.firstEmpty(), ite);
            }
        }


        p.openInventory(inv);

    }

    @EventHandler
    public void onChange(InventoryClickEvent e) {
        //Add Item
        if (e.getInventory().getType() == InventoryType.CHEST) {
            if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
                ItemStack ite = e.getCurrentItem();

                final Player p = (Player) e.getWhoClicked();

                if (e.isLeftClick()) {
                    if (p.getInventory().contains(ite)) {
                        if (p.getOpenInventory() != null && p.getOpenInventory().getTopInventory().getName().equals("§7[BetterShops] §dBlacklist") && !isBlacklisted(ite)) {

                            e.setCancelled(true);
                            ItemStack item = ite.clone();

                            item.setAmount(1);

                            addItem(item);
                            p.playSound(p.getLocation(), Sound.NOTE_PLING, 400, 400);
                            openBlacklistInventory(e.getInventory(), p, 1);

                            return;
                        }
                    }
                }
            }
        }
        if (e.getInventory().getName().equals("§7[BetterShops] §dBlacklist")) {
            if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
                e.setCancelled(true);

                final Player p = (Player) e.getWhoClicked();

                if (e.getCurrentItem().getItemMeta().getDisplayName() != null) {
                    if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Language.getString("MainGUI", "NextPage"))) {
                        List<String> lore = e.getInventory().getItem(4).getItemMeta().getLore();
                        int page = Integer.parseInt(lore.get(2).substring(10));

                        openBlacklistInventory(e.getInventory(), p, page + 1);
                        return;
                    }
                }

                if (e.getCurrentItem().getItemMeta().getDisplayName() != null) {
                    if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Language.getString("MainGUI", "PreviousPage"))) {
                        List<String> lore = e.getInventory().getItem(4).getItemMeta().getLore();
                        int page = Integer.parseInt(lore.get(2).substring(10));

                        openBlacklistInventory(e.getInventory(), p, page - 1);
                        return;
                    }
                }

                //Remove Item
                if (getItems().contains(e.getCurrentItem())) {
                    removeItem(e.getCurrentItem());
                    p.playSound(p.getLocation(), Sound.NOTE_BASS, 400, 400);
                    openBlacklistInventory(e.getInventory(), p, 1);

                }
            }
        }


    }
}
