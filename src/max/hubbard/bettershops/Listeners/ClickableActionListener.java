package max.hubbard.bettershops.Listeners;

import max.hubbard.bettershops.Configurations.Language;
import max.hubbard.bettershops.Shops.Items.Actions.ClickableItem;
import max.hubbard.bettershops.Shops.Items.Actions.LeftClickAction;
import max.hubbard.bettershops.Shops.Items.Actions.RightClickAction;
import max.hubbard.bettershops.Shops.Items.Actions.ShiftClickAction;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.HashMap;
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
public class ClickableActionListener implements Listener {

    @EventHandler
    public void onClick(final InventoryClickEvent e) {
        if (e.getInventory().getName().contains(Language.getString("MainGUI", "ShopHeader"))) {
            if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
                e.setCancelled(true);
                final Player p = (Player) e.getWhoClicked();
                HashMap<OfflinePlayer, List<ClickableItem>> items = ClickableItem.getItems();

                if (items.containsKey(p)) {
                    for (final ClickableItem item : items.get(p)) {
                        if (item != null) {
                            if (item.getItem().compare(e.getCurrentItem()) && item.getInventory().equals(e.getInventory())) {

                                if (e.isLeftClick() && !e.isShiftClick()) {
                                    Bukkit.getScheduler().runTaskAsynchronously(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                                        @Override
                                        public void run() {

                                            if (item.getLeftClickActions().size() > 0)
                                                ClickableItem.clearPlayer(p);
                                            for (final LeftClickAction a : item.getLeftClickActions()) {
                                                a.onAction(e);
                                            }
                                        }
                                    });
                                }

                                if (e.isRightClick() && !e.isShiftClick()) {

                                    Bukkit.getScheduler().runTaskAsynchronously(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                                        @Override
                                        public void run() {
                                            if (item.getRightClickActions().size() > 0)
                                                ClickableItem.clearPlayer(p);
                                            for (RightClickAction a : item.getRightClickActions()) {
                                                a.onAction(e);
                                            }
                                        }
                                    });
                                }

                                if (e.isShiftClick()) {
                                    Bukkit.getScheduler().runTaskAsynchronously(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                                        @Override
                                        public void run() {
                                            if (item.getShiftClickActions().size() > 0)
                                                ClickableItem.clearPlayer(p);
                                            for (ShiftClickAction a : item.getShiftClickActions()) {
                                                a.onAction(e);
                                            }
                                        }
                                    });
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
}
