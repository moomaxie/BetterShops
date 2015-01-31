package me.moomaxie.BetterShops.Listeners.ManagerOptions;

import me.moomaxie.BetterShops.Configurations.Messages;
import me.moomaxie.BetterShops.Configurations.ShopLimits;
import me.moomaxie.BetterShops.Listeners.BuyerOptions.OpenShop;
import me.moomaxie.BetterShops.Listeners.OpenShopOptions;
import me.moomaxie.BetterShops.Shops.Shop;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2014. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class AddItemManager implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAddItem(final InventoryClickEvent e) {

        if (e.getInventory().getType() == InventoryType.CHEST) {
            if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
                ItemStack ite = e.getCurrentItem();

                final Player p = (Player) e.getWhoClicked();

                if (e.isLeftClick()) {
                    if (e.getCurrentItem().getItemMeta() != null && e.getCurrentItem().getItemMeta().getLore() != null && e.getCurrentItem().getItemMeta().getLore().contains("§eStock:") && e.getCurrentItem().getItemMeta().getLore().contains("§eAmount:") && e.getCurrentItem().getItemMeta().getLore().contains("§ePrice:") && e.getCurrentItem().getItemMeta().getLore().contains("§e§lLeft Click §7to")) {
                        return;
                    }
                    if (p.getInventory().contains(ite)) {
                        if (p.getOpenInventory() != null && p.getOpenInventory().getTopInventory().getName().contains("§7[Shop]")) {

                            String name = p.getOpenInventory().getTopInventory().getName();
                            name = name.substring(11);

                            final Shop shop = ShopLimits.fromString(p, name);

                            int slot = e.getSlot();

                            if (shop.getOwner().getUniqueId().equals(p.getUniqueId())) {

                                if (e.getInventory().getItem(3).getItemMeta().getDisplayName() != null && e.getInventory().getItem(3).getItemMeta().getDisplayName().equals("§e§lBuying")) {

                                    ItemStack item = ite.clone();

                                    if (!shop.alreadyBeingSold(ite, false)) {
                                        if (shop.getHighestSlot(false) < 161) {

                                            if (shop.getHighestSlot(false) == 53) {
                                                shop.addItem(item, 72, false);
                                            } else if (shop.getHighestSlot(false) == 107) {
                                                shop.addItem(item, 126, false);
                                            } else if (e.getInventory().getItem(12).getData().getData() == (byte) 10) {
                                                if (e.getInventory().firstEmpty() >= 18) {
                                                    shop.addItem(item, p.getOpenInventory().getTopInventory(), false);
                                                } else {
                                                    shop.addItem(item, shop.getHighestSlot(false) + 1, false);
                                                }
                                            } else {
                                                shop.addItem(item, shop.getHighestSlot(false) + 1, false);
                                            }


                                            p.sendMessage(Messages.getPrefix() + Messages.getAddItem());
                                            p.playSound(p.getLocation(), Sound.NOTE_PLING, 400, 400);


                                            int am = ite.getAmount() - 1;

                                            if (shop.isServerShop()) {
                                                OpenShop.openShopItems(e.getInventory(), p, shop, 1);
                                            } else {
                                                OpenShopOptions.openShopOwnerOptionsInventory(e.getInventory(), p, shop, 1);
                                            }


                                            if (am > 0) {
                                                ite.setAmount(am);
                                            } else {
                                                p.getInventory().setItem(slot, new ItemStack(Material.AIR));
                                            }


                                        } else {
                                            p.sendMessage(Messages.getPrefix() + Messages.getShopFull());
                                        }
                                    } else {
                                        Stocks.addAll(item, shop, p);
                                        if (shop.isServerShop()) {
                                            OpenShop.openShopItems(e.getInventory(), p, shop, 1);
                                        } else {
                                            OpenShopOptions.openShopOwnerOptionsInventory(e.getInventory(), p, shop, 1);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
