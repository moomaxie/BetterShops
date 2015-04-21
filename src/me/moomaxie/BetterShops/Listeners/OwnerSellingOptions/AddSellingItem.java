package me.moomaxie.BetterShops.Listeners.OwnerSellingOptions;

import me.moomaxie.BetterShops.Configurations.AnvilGUI;
import me.moomaxie.BetterShops.Configurations.Config;
import me.moomaxie.BetterShops.Configurations.GUIMessages.MainGUI;
import me.moomaxie.BetterShops.Configurations.GUIMessages.SearchEngine;
import me.moomaxie.BetterShops.Configurations.Messages;
import me.moomaxie.BetterShops.Configurations.ShopManager;
import me.moomaxie.BetterShops.Core;
import me.moomaxie.BetterShops.Listeners.BuyerOptions.OpenShop;
import me.moomaxie.BetterShops.Listeners.Misc.ChatMessages;
import me.moomaxie.BetterShops.Listeners.OpenShopOptions;
import me.moomaxie.BetterShops.Shops.Shop;
import me.moomaxie.BetterShops.Shops.ShopItem;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2014. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class AddSellingItem implements Listener {

//    @EventHandler(priority = EventPriority.LOWEST)
//    public void onAddItem(final InventoryClickEvent e) {
//
//        if (e.getInventory().getType() == InventoryType.CHEST) {
//            if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
//                ItemStack ite = e.getCurrentItem();
//
//
//               final Player p = (Player) e.getWhoClicked();
//
//                if (e.isLeftClick()) {
//
//                    if (p.getInventory().contains(ite)) {
//                        if (p.getOpenInventory() != null && p.getOpenInventory().getTopInventory().getName().contains(MainGUI.getString("ShopHeader"))) {
//
//                            String name = p.getOpenInventory().getTopInventory().getName();
//                            name = name.substring(MainGUI.getString("ShopHeader").length());
//
//                            final Shop shop = ShopManager.fromString(p, name);
//
//                            int slot = e.getSlot();
//
//                            if (shop.getOwner().getUniqueId().equals(p.getUniqueId())) {
//
//
//                                if (e.getInventory().getItem(3).getItemMeta().getDisplayName() != null && e.getInventory().getItem(3).getItemMeta().getDisplayName().equals(MainGUI.getString("Selling")) && Config.useSellingShop()) {
//
//                                    ItemStack item = ite.clone();
//
//                                    item.setAmount(1);
//
//                                    if (!Blacklist.isBlacklisted(item) || Blacklist.isBlacklisted(item) && Permissions.hasBlacklistPerm(p)) {
//
//                                        if (ShopItem.fromItemStack(shop, item, true) == null) {
//                                            int page = shop.getNextAvailablePage(true);
//                                            int s = shop.getNextSlotForPage(page, true);
//
//                                            ShopItem sItem = shop.createShopItem(item, s, page, true);
//
//                                            p.sendMessage(Messages.getString("Prefix") + Messages.getString("AddItem"));
//                                            p.playSound(p.getLocation(), Sound.NOTE_PLING, 400, 400);
//
//
//                                            if (shop.isServerShop()) {
//                                                OpenSellShop.openSellerShop(e.getInventory(), p, shop, page);
//                                            } else {
//                                                OpenSellingOptions.openShopSellingOptions(e.getInventory(), p, shop, page);
//                                            }
//
//                                        } else {
//                                            p.sendMessage(Messages.getString("Prefix") + Messages.getString("AlreadyHave"));
//                                        }
//                                    } else {
//                                        p.sendMessage(Messages.getString("Prefix") + Messages.getString("Blacklist"));
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }

    @EventHandler
    public void onAdd(final InventoryClickEvent e) {
        final Player p = (Player) e.getWhoClicked();
        if (e.getInventory().getName().contains(MainGUI.getString("ShopHeader"))) {
            e.setCancelled(true);

            if (e.getInventory().getType() == InventoryType.CHEST) {
                if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
                    String name = e.getInventory().getName();
                    name = name.substring(MainGUI.getString("ShopHeader").length());

                    final Shop shop = ShopManager.fromString(p, name);


                    if (e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().equals(MainGUI.getString("AddItem"))) {

                        if (Config.useAnvil()) {

                            AnvilGUI gui = Core.getAnvilGUI();
                            gui.doGUIThing(p, new AnvilGUI.AnvilClickEventHandler() {
                                @Override
                                public void onAnvilClick(AnvilGUI.AnvilClickEvent ev) {
                                    if (ev.getSlot() == 2) {
                                        ev.setWillClose(true);
                                        ev.setWillDestroy(true);

                                        boolean can = false;


                                        if (ev.getCurrentItem().getType() == Material.PAPER) {
                                            if (ev.getCurrentItem().hasItemMeta()) {
                                                if (ev.getCurrentItem().getItemMeta().getDisplayName() != null) {
                                                    String name = ev.getCurrentItem().getItemMeta().getDisplayName();

                                                    int amt;
                                                    try {
                                                        amt = Integer.parseInt(name);

                                                        ItemStack item = new ItemStack(amt);

                                                        if (shop != null) {
                                                            if (ShopItem.fromItemStack(shop, item, true) == null) {
                                                                int page = shop.getNextAvailablePage(true);
                                                                int s = shop.getNextSlotForPage(page, true);

                                                                ShopItem sItem = shop.createShopItem(item, s, page, true);

                                                                p.sendMessage(Messages.getString("Prefix") + Messages.getString("AddItem"));
                                                                p.playSound(p.getLocation(), Sound.NOTE_PLING, 400, 400);


                                                                if (shop.isServerShop()) {
                                                                    OpenShop.openShopItems(e.getInventory(), p, shop, page);
                                                                } else {
                                                                    OpenShopOptions.openShopOwnerOptionsInventory(e.getInventory(), p, shop, page);
                                                                }

                                                            } else {
                                                                can = true;
                                                            }
                                                        }


                                                    } catch (Exception ex) {

                                                        if (Material.getMaterial(name.toUpperCase()) != null) {
                                                            ItemStack item = new ItemStack(Material.valueOf(name.toUpperCase()));

                                                            if (shop != null) {
                                                                if (ShopItem.fromItemStack(shop, item, true) == null) {
                                                                    int page = shop.getNextAvailablePage(true);
                                                                    int s = shop.getNextSlotForPage(page, true);

                                                                    ShopItem sItem = shop.createShopItem(item, s, page, true);

                                                                    p.sendMessage(Messages.getString("Prefix") + Messages.getString("AddItem"));
                                                                    p.playSound(p.getLocation(), Sound.NOTE_PLING, 400, 400);


                                                                    if (shop.isServerShop()) {
                                                                        OpenShop.openShopItems(e.getInventory(), p, shop, page);
                                                                    } else {
                                                                        OpenShopOptions.openShopOwnerOptionsInventory(e.getInventory(), p, shop, page);
                                                                    }

                                                                } else {
                                                                    can = true;
                                                                }
                                                            }
                                                        } else {
                                                            for (Material m : Material.values()) {
                                                                if (m.name().contains(name.toUpperCase().replaceAll(" ", "_"))) {
                                                                    ItemStack item = new ItemStack(m);

                                                                    if (shop != null) {
                                                                        if (ShopItem.fromItemStack(shop, item, true) == null) {
                                                                            int page = shop.getNextAvailablePage(true);
                                                                            int s = shop.getNextSlotForPage(page, true);

                                                                            ShopItem sItem = shop.createShopItem(item, s, page, true);

                                                                            p.sendMessage(Messages.getString("Prefix") + Messages.getString("AddItem"));
                                                                            p.playSound(p.getLocation(), Sound.NOTE_PLING, 400, 400);


                                                                            if (shop.isServerShop()) {
                                                                                OpenShop.openShopItems(e.getInventory(), p, shop, page);
                                                                            } else {
                                                                                OpenShopOptions.openShopOwnerOptionsInventory(e.getInventory(), p, shop, page);
                                                                            }

                                                                        } else {
                                                                            can = true;
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                        if (!can) {
                                            p.sendMessage(Messages.getString("Prefix") + Messages.getString("InvalidItem"));
                                        }

                                    } else {
                                        ev.setWillClose(true);
                                        ev.setWillDestroy(true);
                                    }
                                }
                            });

                            ItemStack it = new ItemStack(Material.PAPER);
                            ItemMeta meta = it.getItemMeta();
                            meta.setDisplayName(SearchEngine.getString("ItemName"));
                            it.setItemMeta(meta);

                            gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, it);

                            gui.open();
                        } else {
                            p.closeInventory();

                            Map<Shop, Inventory> map = new HashMap<>();
                            map.put(shop, e.getInventory());

                            ChatMessages.addSellItem.put(p, map);
                            p.sendMessage(Messages.getString("Prefix") + Messages.getString("ChatMessage"));
                        }
                    }
                }
            }
        }
    }
}
