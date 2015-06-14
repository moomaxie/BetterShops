package max.hubbard.bettershops.Configurations.ConfigMenu;

import max.hubbard.bettershops.Configurations.Config;
import max.hubbard.bettershops.Configurations.Language;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

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
public class ConfigMenu {


    public static void openConfigMenu(Inventory inv, Player p, int page) {

        boolean open = false;

        if (inv == null) {
            inv = Bukkit.createInventory(p, 54, "§7[BetterShops] §dConfig");
            open = true;
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

        ItemStack info = new ItemStack(Material.ENDER_CHEST);
        ItemMeta infoMeta = info.getItemMeta();
        infoMeta.setDisplayName("§e§lBetterShops Configuration");
        infoMeta.setLore(Arrays.asList(Language.getString("MainGUI", "Page") + " §7" + page));
        info.setItemMeta(infoMeta);

        ItemStack next = new ItemStack(Material.ARROW);
        ItemMeta nextMeta = next.getItemMeta();
        nextMeta.setDisplayName(Language.getString("MainGUI", "NextPage"));
        next.setItemMeta(nextMeta);

        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName(Language.getString("MainGUI", "PreviousPage"));
        back.setItemMeta(backMeta);

        ItemStack NPCChoose = new ItemStack(Material.MONSTER_EGG);
        ItemMeta NPCChooseMeta = NPCChoose.getItemMeta();
        NPCChooseMeta.setDisplayName("§eChoose NPCs");
        NPCChoose.setItemMeta(NPCChooseMeta);

        inv.setItem(1, NPCChoose);

        if (page > 1) {
            inv.setItem(0, back);
        }

        int maxPage = (int) Math.ceil((double) (Config.config.getKeys(false).size()) / 36);

        if (maxPage == 0) {
            maxPage = 1;
        }


        if (page != maxPage) {
            inv.setItem(8, next);
        }

        int j = 0;

        if (page > 1) {
            j = 36 * (page - 1);
        }

        int k = Config.config.getKeys(false).size();

        if (page != maxPage) {
            k = k - (j + (Config.config.getKeys(false).size() - 36));
        }

        for (int i = j; i < k; i++) {
            String s = (String) Config.config.getKeys(false).toArray()[i];

            if (!s.contains("SQL") && !s.contains("database") && !s.contains("Version") && !s.contains("user") && !s.contains("host") && !s.contains("pass") && !s.contains("root") && !s.contains("port")) {

                if (!Config.config.isConfigurationSection(s)) {
                    ItemStack it = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 5);
                    ItemMeta itMeta = it.getItemMeta();

                    if (Config.getObject(s) instanceof Boolean && !(boolean) Config.getObject(s)) {
                        MaterialData data = it.getData();
                        data.setData((byte) 14);
                        it.setData(data);
                        it = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 14);
                    }
                    if (Config.getObject(s) instanceof Boolean) {
                        itMeta.setLore(Arrays.asList("§e§lClick §7to change"));
                    } else {
                        it = new ItemStack(Material.NAME_TAG);
                        itMeta.setLore(Arrays.asList("§7" + Config.getObject(s), "§e§lClick §7to change"));
                    }
                    itMeta.setDisplayName("§e§l" + s);

                    it.setItemMeta(itMeta);
                    if (inv.firstEmpty() > 0)
                        inv.setItem(inv.firstEmpty(), it);
                }
            }
        }

        inv.setItem(4, info);

        if (open)

            p.openInventory(inv);
    }

}