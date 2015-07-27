package max.hubbard.bettershops.Listeners;

import max.hubbard.bettershops.Configurations.Blacklist;
import max.hubbard.bettershops.Configurations.Config;
import max.hubbard.bettershops.Configurations.Language;
import max.hubbard.bettershops.Configurations.Permissions;
import max.hubbard.bettershops.Menus.MenuType;
import max.hubbard.bettershops.ShopManager;
import max.hubbard.bettershops.Shops.FileShop;
import max.hubbard.bettershops.Shops.Items.Actions.ClickableItem;
import max.hubbard.bettershops.Shops.Items.FileShopItem;
import max.hubbard.bettershops.Shops.Items.SQLShopItem;
import max.hubbard.bettershops.Shops.Items.ShopItem;
import max.hubbard.bettershops.Shops.Shop;
import max.hubbard.bettershops.Shops.Types.Holo.ShopHologram;
import max.hubbard.bettershops.TradeManager;
import max.hubbard.bettershops.Utils.ItemUtils;
import max.hubbard.bettershops.Utils.Stocks;
import max.hubbard.bettershops.Utils.Trade;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

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
public class AddItemListener implements Listener {

    @EventHandler
    public void onAddItem(final InventoryClickEvent e) {

        if (e.getInventory().getType() == InventoryType.CHEST) {
            if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
                final ItemStack ite = e.getCurrentItem();

                final Player p = (Player) e.getWhoClicked();

                if (e.isLeftClick()) {
                    if (e.getCurrentItem().getItemMeta() != null && e.getCurrentItem().getItemMeta().getLore() != null && e.getCurrentItem().getItemMeta().getLore().contains(Language.getString("MainGUI", "Stock")) && e.getCurrentItem().getItemMeta().getLore().contains(Language.getString("MainGUI", "Amount")) && e.getCurrentItem().getItemMeta().getLore().contains(Language.getString("MainGUI", "Price"))) {
                        return;
                    }

                    if (p.getInventory().contains(ite) && e.getRawSlot() >= 54) {

                        if (p.getOpenInventory() != null && p.getOpenInventory().getTopInventory().getName().contains(Language.getString("MainGUI", "ShopHeader"))) {

                            Bukkit.getScheduler().runTaskAsynchronously(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                                @Override
                                public void run() {

                                    String name = p.getOpenInventory().getTopInventory().getName();
                                    name = name.substring(Language.getString("MainGUI", "ShopHeader").length());

                                    final Shop shop = ShopManager.fromString(p, name);

                                    int slot = e.getSlot();

                                    if (shop.getOwner().getUniqueId().equals(p.getUniqueId()) || Permissions.hasEditPerm(p, shop)) {

                                        if (e.getInventory().getItem(3).getItemMeta().getLore() != null &&
                                                e.getInventory().getItem(3).getItemMeta().getLore().contains(Language.getString("MainGUI", "SearchOptions"))) {
                                            return;
                                        }


                                        if (e.getInventory().getItem(3).getItemMeta().getDisplayName() != null && e.getInventory().getItem(3).getItemMeta().getDisplayName().equals(Language.getString("MainGUI", "Buying"))) {

                                            ItemStack item = ite.clone();

                                            item.setAmount(1);

                                            if (!Blacklist.isBlacklisted(item) || Blacklist.isBlacklisted(item) && Permissions.hasBlacklistPerm(p)) {

                                                ShopItem shopItem;
                                                if (shop instanceof FileShop) {
                                                    shopItem = FileShopItem.fromItemStack(shop, item, false);
                                                } else {
                                                    shopItem = SQLShopItem.fromItemStack(shop, item, false);
                                                }

                                                if (shopItem == null) {
                                                    int page = shop.getNextAvailablePage(false);
                                                    int s = shop.getNextSlotForPage(page, false);

                                                    ShopItem sItem = shop.createShopItem(item, s, page, false);

                                                    p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "AddItem"));
                                                    p.playSound(p.getLocation(), Sound.NOTE_PLING, 400, 400);


                                                    int am = ite.getAmount() - 1;

                                                    if (shop.isServerShop()) {
                                                        shop.getMenu(MenuType.MAIN_BUYING).draw(p, page);
                                                    } else {
                                                        shop.getMenu(MenuType.OWNER_BUYING).draw(p, page);
                                                    }


                                                    if (am > 0) {
                                                        ite.setAmount(am);
                                                    } else {
                                                        p.getInventory().setItem(slot, new ItemStack(Material.AIR));
                                                    }

                                                    shop.setObject("Removal", "");
                                                } else {

                                                    int limit = (int) Config.getObject("StockLimit");

                                                    if (limit != 0 && shopItem.getStock() >= limit) {
                                                        p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "HighStock"));
                                                        return;
                                                    }

                                                    if (limit != 0 && Stocks.getNumberInInventory(shopItem, p, shop) + shopItem.getStock() > limit) {
                                                        Stocks.addStock(shopItem, limit - shopItem.getStock(), p, shop);
                                                        p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "StopStock"));
                                                    } else {
                                                        Stocks.addAll(shopItem, shop, p);
                                                    }
                                                    if (shop.isHoloShop()) {
                                                        ShopHologram h = shop.getHolographicShop();
                                                        h.updateItemLines(h.getItemLine(), false);
                                                    }

                                                    ClickableItem.clearPlayer(p);
                                                    if (shop.isServerShop()) {
                                                        shop.getMenu(MenuType.MAIN_BUYING).draw(p, shopItem.getPage());
                                                    } else {
                                                        shop.getMenu(MenuType.OWNER_BUYING).draw(p, shopItem.getPage());
                                                    }
                                                    p.playSound(p.getLocation(), Sound.NOTE_PLING, 400, 400);
                                                }
                                            } else {
                                                p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "Blacklist"));
                                            }
                                        } else if (e.getInventory().getItem(3).getItemMeta().getDisplayName() != null && e.getInventory().getItem(3).getItemMeta().getDisplayName().equals(Language.getString("MainGUI", "Selling")) && (boolean) Config.getObject("SellingShops")) {

                                            ItemStack item = ite.clone();

                                            item.setAmount(1);

                                            if (!Blacklist.isBlacklisted(item) || Blacklist.isBlacklisted(item) && Permissions.hasBlacklistPerm(p)) {

                                                ShopItem shopItem;
                                                if (shop instanceof FileShop) {
                                                    shopItem = FileShopItem.fromItemStack(shop, item, true);
                                                } else {
                                                    shopItem = SQLShopItem.fromItemStack(shop, item, true);
                                                }

                                                if (shopItem == null) {
                                                    int page = shop.getNextAvailablePage(true);
                                                    int s = shop.getNextSlotForPage(page, true);

                                                    ShopItem sItem = shop.createShopItem(item, s, page, true);

                                                    p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "AddItem"));
                                                    p.playSound(p.getLocation(), Sound.NOTE_PLING, 400, 400);

                                                    if (shop.isServerShop()) {
                                                        shop.getMenu(MenuType.MAIN_SELLING).draw(p, page);
                                                    } else {
                                                        shop.getMenu(MenuType.OWNER_SELLING).draw(p, page);
                                                    }

                                                    shop.setObject("Removal", "");

                                                } else {
                                                    p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "AlreadyHave"));
                                                }
                                            } else {
                                                p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "Blacklist"));
                                            }
                                        } else if (e.getInventory().getItem(4).getItemMeta().getDisplayName() != null && e.getInventory().getItem(4).getItemMeta().getDisplayName().equals(Language.getString("Trades", "Trades"))) {

                                            Trade trade = TradeManager.getTrade(p);

                                            if (trade != null) {

                                                String lore = e.getInventory().getItem(4).getItemMeta().getLore().get(0);
                                                lore = lore.substring(Language.getString("Trades", "TradeId").length());

                                                if (trade.getId().equals(lore)) {

                                                    ItemStack item = ite.clone();
                                                    item.setAmount(1);

                                                    List<ItemStack> items = trade.getTradeItems();

                                                    ItemStack t = null;

                                                    for (ItemStack it : items) {
                                                        if (ItemUtils.compare(item, it)) {
                                                            t = it.clone();
                                                            t.setAmount(t.getAmount() + 1);
                                                            items.remove(it);
                                                            break;
                                                        }
                                                    }

                                                    if (t == null) {
                                                        items.add(item);
                                                    } else {
                                                        items.add(t);
                                                    }

                                                    shop.getMenu(MenuType.TRADE_MANAGER).draw(p, 1, new Trade(trade.getId(),
                                                            trade.getShop(), items, trade.getTradeGold(),
                                                            trade.getRecievingItems(), trade.getReceivingGold(), false), new Trade(trade.getId(),
                                                            trade.getShop(), items, trade.getTradeGold(),
                                                            trade.getRecievingItems(), trade.getReceivingGold(), false));
                                                }
                                            }
                                        }
                                    }
                                }
                            });
                        }
                    }
                }
            } else if (e.getCurrentItem() != null) {

                final Player p = (Player) e.getWhoClicked();

                if (e.isLeftClick()) {
                    if (p.getOpenInventory() != null && p.getOpenInventory().getTopInventory().getName().contains(Language.getString("MainGUI", "ShopHeader"))) {
                        e.setCancelled(true);
                        String name = p.getOpenInventory().getTopInventory().getName();
                        name = name.substring(Language.getString("MainGUI", "ShopHeader").length());

                        final Shop shop = ShopManager.fromString(p, name);


                        if (e.getInventory().getItem(5).getItemMeta().getDisplayName() != null && e.getInventory().getItem(5).getItemMeta().getDisplayName().equals(Language.getString("MainGUI", "Arrangement"))) {
                            int slot = e.getSlot();
                            int page = Integer.parseInt(e.getInventory().getItem(13).getItemMeta().getDisplayName().substring(Language.getString("MainGUI", "Page").length() + 3));
                            if (shop.getArrange().containsKey(p.getUniqueId())) {
                                boolean sell = shop.getArrange().get(p.getUniqueId()).isSelling();
                                shop.getArrange().get(p.getUniqueId()).setObject("Page", page);
                                shop.getArrange().get(p.getUniqueId()).setObject("Slot", slot);
                                shop.getArrange().remove(p.getUniqueId());
                                shop.getMenu(MenuType.REARRANGE).draw(p, page, sell);
                            }
                        }
                    }
                }
            }
        }
    }
}
