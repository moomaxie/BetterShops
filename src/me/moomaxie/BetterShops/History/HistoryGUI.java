package me.moomaxie.BetterShops.History;

import com.google.common.collect.Lists;
import me.moomaxie.BetterShops.Configurations.GUIMessages.History;
import me.moomaxie.BetterShops.Configurations.GUIMessages.MainGUI;
import me.moomaxie.BetterShops.Configurations.Messages;
import me.moomaxie.BetterShops.Configurations.ShopLimits;
import me.moomaxie.BetterShops.Configurations.WordsCapitalizer;
import me.moomaxie.BetterShops.Listeners.BuyerOptions.OpenShop;
import me.moomaxie.BetterShops.Listeners.OpenShopOptions;
import me.moomaxie.BetterShops.Shops.Shop;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.LinkedList;
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
public class HistoryGUI implements Listener {

    public static void openHistoryGUI(Player p, Shop shop, int page) {
        Inventory inv = Bukkit.createInventory(p, 54, "§7[Shop] §a" + shop.getName());

        if (!shop.transLoaded){
            shop.loadTransactions();
        }

        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7);
        ItemMeta m = item.getItemMeta();
        m.setDisplayName(" ");
        item.setItemMeta(m);
        for (int i = 0; i < 18; i++) {
            inv.setItem(i, item);
        }

        ItemStack info = new ItemStack(Material.ENDER_CHEST);
        ItemMeta infoMeta = info.getItemMeta();
        infoMeta.setDisplayName(me.moomaxie.BetterShops.Configurations.GUIMessages.History.getString("History"));
        infoMeta.setLore(Arrays.asList(MainGUI.getString("Page") + " §7" + page));
        info.setItemMeta(infoMeta);

        ItemStack clear = new ItemStack(Material.STAINED_GLASS_PANE,1,(byte) 14);
        ItemMeta clearMeta = clear.getItemMeta();
        clearMeta.setDisplayName(me.moomaxie.BetterShops.Configurations.GUIMessages.History.getString("ClearHistory"));
        clearMeta.setLore(Arrays.asList(History.getString("ClearHistoryLore")));
        clear.setItemMeta(clearMeta);

        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName(MainGUI.getString("BackArrow"));
        back.setItemMeta(backMeta);

        ItemStack arrow = new ItemStack(Material.ARROW);
        ItemMeta arrowMeta = arrow.getItemMeta();
        arrowMeta.setDisplayName(MainGUI.getString("NextPage"));
        arrow.setItemMeta(arrowMeta);

        ItemStack barrow = new ItemStack(Material.ARROW);
        ItemMeta barrowMeta = barrow.getItemMeta();
        barrowMeta.setDisplayName(MainGUI.getString("PreviousPage"));
        barrow.setItemMeta(barrowMeta);

        if (page > 1) {
            inv.setItem(1, barrow);
        }

        inv.setItem(0, back);

        inv.setItem(4, info);
        inv.setItem(7,clear);

        int maxPage = (int) Math.ceil((double) (shop.getHistory().getAllTransactions().size()) / 36);

        if (maxPage == 0){
            maxPage = 1;
        }


        if (page != maxPage) {
            inv.setItem(8, arrow);
        }

        int j = 0;

        if (page > 1) {
            j = 36 * (page - 1);
        }

        int k = shop.getHistory().getAllTransactions().size();

        if (page != maxPage) {
            k = k - (j + (shop.getHistory().getAllTransactions().size() - 36));
        }

        LinkedList<Transaction> list = shop.getHistory().getAllTransactions();

        List<Transaction> l = Lists.reverse(list);

        if (list.size() > 0) {
            for (int i = j; i < k; i++) {
                Transaction trans = l.get(i);
                ItemStack it = new ItemStack(Material.EMERALD);
                ItemMeta sk = it.getItemMeta();
                String s = "Buying";
                if (trans.isSell()) {
                    it = new ItemStack(Material.EMERALD_BLOCK);
                    s = "Selling";
                }

                sk.setDisplayName("§a" + trans.getPlayer().getName());

                if (trans.getItem().getItemMeta().getDisplayName() != null) {
                    sk.setLore(Arrays.asList(History.getString("Date") + " §8" + trans.getDate().toLocaleString(),
                            History.getString("Item") + " §8" + trans.getItem().getItemMeta().getDisplayName(),
                            History.getString("Price") + " §8" + trans.getPrice(),
                            History.getString("Amount") + " §8" + trans.getAmount(),
                            History.getString("Shop") + " §8" + s));
                } else {
                    sk.setLore(Arrays.asList(History.getString("Date") + " §8" + trans.getDate().toLocaleString(),
                            History.getString("Item") + " §8" + WordsCapitalizer.capitalizeEveryWord(trans.getItem().getType().name().replaceAll("_", " ")),
                            History.getString("Price") + " §8" + trans.getPrice(),
                            History.getString("Amount") + " §8" + trans.getAmount(),
                            History.getString("Shop") + " §8" + s));
                }
                it.setItemMeta(sk);

                if (inv.firstEmpty() > 0)
                    inv.setItem(inv.firstEmpty(), it);
            }
        }

        p.openInventory(inv);
    }

    @EventHandler
    public void onHistoryChangePage(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getInventory().getName().contains("§7[Shop]")) {
            e.setCancelled(true);
            if (e.getInventory().getType() == InventoryType.CHEST) {
                if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {

                    String name = e.getInventory().getName();
                    name = name.substring(11);

                    Shop shop = ShopLimits.fromString(p, name);

                    if (e.getInventory().getItem(4).getItemMeta().getDisplayName() != null &&
                            e.getInventory().getItem(4).getItemMeta().getDisplayName().equals(History.getString("History"))) {

                        if (e.getCurrentItem().getItemMeta().getDisplayName() != null
                                && e.getCurrentItem().getItemMeta().getDisplayName().equals(MainGUI.getString("BackArrow"))) {

                            if (shop.isServerShop()) {
                                OpenShop.openShopItems(e.getInventory(), p, shop, 1);
                            } else {
                                OpenShopOptions.openShopOwnerOptionsInventory(e.getInventory(), p, shop, 1);
                            }
                        }

                        if (e.getCurrentItem().getItemMeta().getDisplayName() != null
                                && e.getCurrentItem().getItemMeta().getDisplayName().equals(History.getString("ClearHistory"))) {

                            if (shop.getHistory().getAllTransactions().size() > 2) {
                                shop.getHistory().clearHistory();
                                openHistoryGUI(p, shop, 1);
                            } else {
                                p.sendMessage(Messages.getPrefix() + "§cPlease wait until more transactions occur in order to Clear History.");
                            }
                        }

                        if (e.getCurrentItem().getItemMeta().getDisplayName() != null
                                && e.getCurrentItem().getItemMeta().getDisplayName().equals(MainGUI.getString("NextPage"))) {

                            List<String> lore = e.getInventory().getItem(4).getItemMeta().getLore();
                            int page = Integer.parseInt(lore.get(0).substring(MainGUI.getString("Page").length() + 3));

                            openHistoryGUI(p, shop, page + 1);
                        }

                        if (e.getCurrentItem().getItemMeta().getDisplayName() != null
                                && e.getCurrentItem().getItemMeta().getDisplayName().equals(MainGUI.getString("PreviousPage"))) {

                            List<String> lore = e.getInventory().getItem(4).getItemMeta().getLore();
                            int page = Integer.parseInt(lore.get(0).substring(MainGUI.getString("Page").length() + 3));

                            openHistoryGUI(p, shop, page - 1);
                        }
                    }
                }
            }
        }
    }
}
