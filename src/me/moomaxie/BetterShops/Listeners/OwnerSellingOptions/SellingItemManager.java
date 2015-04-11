package me.moomaxie.BetterShops.Listeners.OwnerSellingOptions;

import me.moomaxie.BetterShops.Configurations.AnvilGUI;
import me.moomaxie.BetterShops.Configurations.Config;
import me.moomaxie.BetterShops.Configurations.GUIMessages.ItemTexts;
import me.moomaxie.BetterShops.Configurations.GUIMessages.MainGUI;
import me.moomaxie.BetterShops.Configurations.GUIMessages.SearchEngine;
import me.moomaxie.BetterShops.Configurations.Messages;
import me.moomaxie.BetterShops.Configurations.ShopLimits;
import me.moomaxie.BetterShops.Core;
import me.moomaxie.BetterShops.Listeners.BuyerOptions.OpenShop;
import me.moomaxie.BetterShops.Listeners.ManagerOptions.Stocks;
import me.moomaxie.BetterShops.Listeners.Misc.ChatMessages;
import me.moomaxie.BetterShops.Listeners.OpenShopOptions;
import me.moomaxie.BetterShops.ShopTypes.Holographic.DeleteHoloShop;
import me.moomaxie.BetterShops.ShopTypes.Holographic.ShopHologram;
import me.moomaxie.BetterShops.Shops.Shop;
import me.moomaxie.BetterShops.Shops.ShopItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * ***********************************************************************
 * Copyright me.moomaxie (c) 2014. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of me.moomaxie. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class SellingItemManager implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMan(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getInventory().getName().contains(MainGUI.getString("ShopHeader"))) {
            e.setCancelled(true);
            if (e.getInventory().getType() == InventoryType.CHEST) {
                if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {

                    String name = e.getInventory().getName();
                    name = name.substring(11);

                    Shop shop = ShopLimits.fromString(p, name);

                    if (shop != null) {

                        if (shop.getOwner().getUniqueId().equals(p.getUniqueId())) {
                            if (e.getCurrentItem().getItemMeta().getLore() != null && e.getCurrentItem().getItemMeta().getLore().contains(MainGUI.getString("SellOptions"))) {
                                openItemManager(e.getInventory(), p, shop, e.getCurrentItem());
                            }
                        }
                    }
                }
            }
        }
    }

    public static void openItemManager(Inventory inv, Player p, Shop shop, ItemStack it) {
        boolean same = true;
        if (inv == null) {
            same = false;
            inv = Bukkit.createInventory(p, 54, MainGUI.getString("ShopHeader") + shop.getName());
        } else {
            inv.clear();
        }

        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7);
        ItemMeta m = item.getItemMeta();
        m.setDisplayName(" ");
        item.setItemMeta(m);
        for (int i = 0; i < 18; i++) {
            inv.setItem(i, item);
        }

        ItemStack nam = new ItemStack(Material.STAINED_CLAY, 1, (byte) 4);
        ItemMeta namMeta = nam.getItemMeta();
        namMeta.setDisplayName(ItemTexts.getString("SellPriceDisplayName"));
        namMeta.setLore(Arrays.asList(ItemTexts.getString("PriceLore")));
        nam.setItemMeta(namMeta);

        ItemStack desc = new ItemStack(Material.STAINED_CLAY, 1, (byte) 15);
        ItemMeta descMeta = desc.getItemMeta();
        descMeta.setDisplayName(ItemTexts.getString("SellRemoveItemDisplayName"));
        descMeta.setLore(Arrays.asList(ItemTexts.getString("RemoveItemLore")));
        desc.setItemMeta(descMeta);

        ItemStack stock = new ItemStack(Material.STAINED_CLAY, 1, (byte) 4);
        ItemMeta stockMeta = stock.getItemMeta();
        stockMeta.setDisplayName(ItemTexts.getString("CollectStockDisplayName"));
        stockMeta.setLore(Arrays.asList(ItemTexts.getString("CollectStockLore")));
        stock.setItemMeta(stockMeta);

        ItemStack amount = new ItemStack(Material.STAINED_CLAY, 1, (byte) 2);
        ItemMeta amountMeta = amount.getItemMeta();
        amountMeta.setDisplayName(ItemTexts.getString("ChangeAskingAmount"));
        amountMeta.setLore(Arrays.asList(ItemTexts.getString("ChangeAskingAmountLore")));
        amount.setItemMeta(amountMeta);

        ItemStack limit = new ItemStack(Material.NAME_TAG);
        ItemMeta limitMeta = limit.getItemMeta();
        limitMeta.setDisplayName(ItemTexts.getString("SellLimit").replaceAll("<Amount>", "" + ShopItem.fromItemStack(shop, it, true).getLimit()));
        limitMeta.setLore(Arrays.asList(ItemTexts.getString("SellLimitLore")));
        limit.setItemMeta(limitMeta);

        ItemStack data = new ItemStack(Material.STAINED_CLAY, 1, (byte) 9);
        ItemMeta dataMeta = data.getItemMeta();
        dataMeta.setDisplayName(ItemTexts.getString("ItemDataDisplayName"));
        dataMeta.setLore(Arrays.asList(ItemTexts.getString("ItemDataWarning"),
                " ",
                ItemTexts.getString("ItemDataLore"),
                ItemTexts.getString("ItemDataExample")));
        data.setItemMeta(dataMeta);

        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName(MainGUI.getString("BackArrow"));
        back.setItemMeta(backMeta);

        inv.setItem(0, back);

        inv.setItem(4, it);
        inv.setItem(8, limit);

        inv.setItem(inv.firstEmpty(), nam);
        inv.setItem(inv.firstEmpty(), desc);
        inv.setItem(inv.firstEmpty(), stock);
        inv.setItem(inv.firstEmpty(), amount);
        inv.setItem(inv.firstEmpty(), data);

        if (!same)
            p.openInventory(inv);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onSettingsClick(final InventoryClickEvent e) {
        final Player p = (Player) e.getWhoClicked();
        if (e.getInventory().getName().contains(MainGUI.getString("ShopHeader"))) {
            e.setCancelled(true);

            if (e.getInventory().getType() == InventoryType.CHEST) {
                if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
                    String name = e.getInventory().getName();
                    name = name.substring(11);

                    final Shop shop = ShopLimits.fromString(p, name);

                    final ItemStack ite = e.getInventory().getItem(4);

                    final ShopItem shopItem = ShopItem.fromItemStack(shop, ite, true);

                    if (shopItem != null) {

                        if (e.getInventory().getItem(4) != null && e.getInventory().getItem(4).getType() != Material.AIR && e.getInventory().getItem(4).hasItemMeta() && e.getInventory().getItem(4).getItemMeta().getLore() != null && e.getInventory().getItem(4).getItemMeta().getLore().contains(MainGUI.getString("SellOptions")) && e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().equals(MainGUI.getString("BackArrow"))) {
                            if (shop.getOwner().getUniqueId().equals(p.getUniqueId())) {
                                OpenSellingOptions.openShopSellingOptions(e.getInventory(), p, shop, shopItem.getPage());
                            } else {
                                OpenShop.openShopItems(e.getInventory(), p, shop, shopItem.getPage());
                            }
                        }

                        if (e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().equals(ItemTexts.getString("SellPriceDisplayName"))) {
                            if (Config.useAnvil()) {
                                AnvilGUI gui = Core.getAnvilGUI();
                                gui.doGUIThing(p, new AnvilGUI.AnvilClickEventHandler() {
                                    @Override
                                    public void onAnvilClick(AnvilGUI.AnvilClickEvent ev) {
                                        if (ev.getSlot() == 2) {
                                            ev.setWillClose(true);
                                            ev.setWillDestroy(true);


                                            if (ev.getCurrentItem().getType() == Material.PAPER) {
                                                if (ev.getCurrentItem().hasItemMeta()) {
                                                    if (ev.getCurrentItem().getItemMeta().getDisplayName() != null) {
                                                        String name = ev.getCurrentItem().getItemMeta().getDisplayName();

                                                        boolean can;
                                                        double amt = 0.0;
                                                        try {
                                                            amt = Double.parseDouble(name);
                                                            can = true;
                                                        } catch (Exception ex) {
                                                            ((Player) e.getWhoClicked()).sendMessage(Messages.getString("Prefix") + Messages.getString("InvalidNumber"));
                                                            can = false;
                                                        }

                                                        BigDecimal bd = new BigDecimal(amt);
                                                        bd = bd.setScale(2, BigDecimal.ROUND_UP);
                                                        amt = bd.doubleValue();

                                                        if (can) {
                                                            if (amt > 0) {
                                                                if (amt <= Config.getMaxPrice()) {
                                                                    shopItem.setPrice(amt);
                                                                    if (shop.isHoloShop()) {
                                                                        ShopHologram h = shop.getHolographicShop();
                                                                        h.updateItemLines(h.getItemLine(), true);
                                                                    }
                                                                    p.sendMessage(Messages.getString("Prefix") + Messages.getString("ChangePrice"));
                                                                } else {
                                                                    if (String.valueOf(Config.getMaxPrice()).contains("E")) {
                                                                        p.sendMessage(Messages.getString("Prefix") + Messages.getString("HighPrice") + " §7(Max: " + Config.getMaxPriceAsString() + ")");
                                                                    } else {
                                                                        p.sendMessage(Messages.getString("Prefix") + Messages.getString("HighPrice") + " §7(Max: " + Config.getMaxPrice() + ")");
                                                                    }
                                                                }
                                                            } else {
                                                                p.sendMessage(Messages.getString("Prefix") + Messages.getString("Zero"));
                                                            }
                                                        }
                                                        OpenSellingOptions.openShopSellingOptions(e.getInventory(), (Player) e.getWhoClicked(), shop, shopItem.getPage());
                                                    }
                                                }
                                            }

                                        } else {
                                            ev.setWillClose(true);
                                            ev.setWillDestroy(true);
                                        }
                                    }
                                });

                                ItemStack it = new ItemStack(Material.PAPER);
                                ItemMeta meta = it.getItemMeta();
                                meta.setDisplayName(SearchEngine.getString("NewPrice"));
                                it.setItemMeta(meta);

                                gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, it);

                                gui.open();
                            } else {
                                p.closeInventory();
                                Map<Shop, ShopItem> map = new HashMap<>();
                                map.put(shop, shopItem);

                                ChatMessages.setSellPrice.put(p, map);
                                p.sendMessage(Messages.getString("Prefix") + Messages.getString("ChatMessage"));
                            }
                        } else if (e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().equals(ItemTexts.getString("ChangeAskingAmount"))) {
                            if (Config.useAnvil()) {
                                AnvilGUI gui = Core.getAnvilGUI();
                                gui.doGUIThing(p, new AnvilGUI.AnvilClickEventHandler() {
                                    @Override
                                    public void onAnvilClick(AnvilGUI.AnvilClickEvent ev) {
                                        if (ev.getSlot() == 2) {
                                            ev.setWillClose(true);
                                            ev.setWillDestroy(true);


                                            if (ev.getCurrentItem().getType() == Material.PAPER) {
                                                if (ev.getCurrentItem().hasItemMeta()) {
                                                    if (ev.getCurrentItem().getItemMeta().getDisplayName() != null) {
                                                        String name = ev.getCurrentItem().getItemMeta().getDisplayName();

                                                        boolean can;
                                                        int amt = 0;
                                                        try {
                                                            amt = Integer.parseInt(name);
                                                            can = true;
                                                        } catch (Exception ex) {
                                                            ((Player) e.getWhoClicked()).sendMessage(Messages.getString("Prefix") + Messages.getString("InvalidNumber"));
                                                            can = false;
                                                        }

                                                        if (can) {
                                                            if (amt > 0 && amt < 2304) {
                                                                shopItem.setAmount(amt);
                                                                if (shopItem.getLiveEco()) {
                                                                    shopItem.getSister().setAmount(amt);
                                                                }
                                                                if (shop.isHoloShop()) {
                                                                    ShopHologram h = shop.getHolographicShop();
                                                                    h.updateItemLines(h.getItemLine(), true);
                                                                }
                                                                ((Player) e.getWhoClicked()).sendMessage(Messages.getString("Prefix") + Messages.getString("ChangeAmount"));
                                                            } else {
                                                                p.sendMessage(Messages.getString("Prefix") + Messages.getString("HighAmount"));
                                                            }
                                                        }
                                                        OpenSellingOptions.openShopSellingOptions(e.getInventory(), (Player) e.getWhoClicked(), shop, shopItem.getPage());
                                                    }
                                                }
                                            }

                                        } else {
                                            ev.setWillClose(true);
                                            ev.setWillDestroy(true);
                                        }
                                    }
                                });

                                ItemStack it = new ItemStack(Material.PAPER);
                                ItemMeta meta = it.getItemMeta();
                                meta.setDisplayName(SearchEngine.getString("NewAmount"));
                                it.setItemMeta(meta);

                                gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, it);

                                gui.open();
                            } else {
                                p.closeInventory();
                                Map<Shop, ShopItem> map = new HashMap<>();
                                map.put(shop, shopItem);

                                ChatMessages.setSellAmount.put(p, map);
                                p.sendMessage(Messages.getString("Prefix") + Messages.getString("ChatMessage"));
                            }
                        } else if (e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().equals(ItemTexts.getString("CollectStockDisplayName"))) {
                            if (Config.useAnvil()) {
                                AnvilGUI gui = Core.getAnvilGUI();
                                gui.doGUIThing(p, new AnvilGUI.AnvilClickEventHandler() {
                                    @Override
                                    public void onAnvilClick(AnvilGUI.AnvilClickEvent ev) {
                                        if (ev.getSlot() == 2) {
                                            ev.setWillClose(true);
                                            ev.setWillDestroy(true);


                                            if (ev.getCurrentItem().getType() == Material.PAPER) {
                                                if (ev.getCurrentItem().hasItemMeta()) {
                                                    if (ev.getCurrentItem().getItemMeta().getDisplayName() != null) {
                                                        String name = ev.getCurrentItem().getItemMeta().getDisplayName();

                                                        int amt;
                                                        try {
                                                            amt = Integer.parseInt(name);
                                                        } catch (Exception ex) {
                                                            if (name.equalsIgnoreCase("all")) {
                                                                Stocks.collectAll(shopItem, shop, p);
                                                                if (shop.isHoloShop()) {
                                                                    ShopHologram h = shop.getHolographicShop();
                                                                    h.updateItemLines(h.getItemLine(), true);
                                                                }
                                                                return;
                                                            } else {
                                                                ((Player) e.getWhoClicked()).sendMessage(Messages.getString("Prefix") + Messages.getString("InvalidNumber"));
                                                                OpenSellingOptions.openShopSellingOptions(e.getInventory(), p, shop, shopItem.getPage());
                                                                return;
                                                            }
                                                        }

                                                        Stocks.collectStock(shopItem, amt, p, shop);

                                                    }
                                                    OpenSellingOptions.openShopSellingOptions(e.getInventory(), (Player) e.getWhoClicked(), shop, shopItem.getPage());
                                                }
                                            }

                                        } else

                                        {
                                            ev.setWillClose(true);
                                            ev.setWillDestroy(true);
                                        }
                                    }
                                });

                                ItemStack it = new ItemStack(Material.PAPER);
                                ItemMeta meta = it.getItemMeta();
                                meta.setDisplayName(SearchEngine.getString("HowMuch"));
                                it.setItemMeta(meta);

                                gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, it);

                                gui.open();

                            } else {
                                p.closeInventory();
                                Map<Shop, ShopItem> map = new HashMap<>();
                                map.put(shop, shopItem);

                                ChatMessages.collectStock.put(p, map);
                                p.sendMessage(Messages.getString("Prefix") + Messages.getString("ChatMessage"));
                            }
                        } else if (e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().equals(ItemTexts.getString("SellRemoveItemDisplayName"))) {

                            ShopItem item = ShopItem.fromItemStack(shop, ite, true);

                            if (item != null) {
                                int page = item.getPage();
                                Stocks.removeAllOfDeletedItem(item, shop, p, true);

                                boolean cal = false;

                                if (shop.isHoloShop()) {
                                    ShopHologram h = shop.getHolographicShop();
                                    if (h.getItemLine().getItemStack().equals(item.getItem())) {
                                        cal = true;
                                    }
                                }

                                shop.deleteShopItem(item);

                                if (cal) {
                                    ShopHologram h = shop.getHolographicShop();
                                    if (shop.getShopItems(true).size() > 0) {
                                        h.getItemLine().setItemStack(shop.getShopItems(true).get(0).getItem());
                                    } else {
                                        if (shop.getShopItems(false).size() > 0) {
                                            h.getItemLine().setItemStack(shop.getShopItems(false).get(0).getItem());
                                            h.getShopLine().setText(MainGUI.getString("Buying"));
                                        } else {
                                            DeleteHoloShop.deleteHologramShop(h);
                                            shop.setHoloShop(false);
                                        }
                                    }
                                }
                                OpenShopOptions.openShopOwnerOptionsInventory(null, p, shop, page);


                                p.sendMessage(Messages.getString("Prefix") + Messages.getString("RemoveItem"));

                            } else {
                                p.closeInventory();
                                p.sendMessage(Messages.getString("Prefix") + Messages.getString("NonExistingItem"));
                            }

                        } else if (e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().equals(ItemTexts.getString("ItemDataDisplayName"))) {
                            if (Config.useAnvil()) {
                                AnvilGUI gui = Core.getAnvilGUI();
                                gui.doGUIThing(p, new AnvilGUI.AnvilClickEventHandler() {
                                    @Override
                                    public void onAnvilClick(AnvilGUI.AnvilClickEvent ev) {
                                        if (ev.getSlot() == 2) {
                                            ev.setWillClose(true);
                                            ev.setWillDestroy(true);


                                            if (ev.getCurrentItem().getType() == Material.PAPER) {
                                                if (ev.getCurrentItem().hasItemMeta()) {
                                                    if (ev.getCurrentItem().getItemMeta().getDisplayName() != null) {
                                                        String name = ev.getCurrentItem().getItemMeta().getDisplayName();

                                                        boolean can;
                                                        int amt = 0;
                                                        try {
                                                            amt = Integer.parseInt(name);
                                                            can = true;
                                                        } catch (Exception ex) {
                                                            ((Player) e.getWhoClicked()).sendMessage(Messages.getString("Prefix") + Messages.getString("InvalidNumber"));
                                                            can = false;
                                                        }

                                                        if (can) {

                                                            boolean cal = false;

                                                            if (shop.isHoloShop()) {
                                                                ShopHologram h = shop.getHolographicShop();
                                                                if (h.getItemLine().getItemStack().equals(shopItem.getItem())) {
                                                                    cal = true;
                                                                }
                                                            }

                                                            shopItem.setData((byte) amt);

                                                            if (cal) {
                                                                ShopHologram h = shop.getHolographicShop();
                                                                h.getItemLine().setItemStack(shopItem.getItem());
                                                            }

                                                            ((Player) e.getWhoClicked()).sendMessage(Messages.getString("Prefix") + Messages.getString("ChangeData"));
                                                        }
                                                        OpenSellingOptions.openShopSellingOptions(e.getInventory(), p, shop, 1);
                                                    }
                                                }
                                            }
                                        } else {
                                            ev.setWillClose(true);
                                            ev.setWillDestroy(true);
                                        }
                                    }
                                });


                                ItemStack it = new ItemStack(Material.PAPER);
                                ItemMeta meta = it.getItemMeta();
                                meta.setDisplayName(SearchEngine.getString("NewData"));
                                it.setItemMeta(meta);

                                gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, it);

                                gui.open();
                            } else {
                                p.closeInventory();
                                Map<Shop, ShopItem> map = new HashMap<>();
                                map.put(shop, shopItem);

                                ChatMessages.changeData.put(p, map);
                                p.sendMessage(Messages.getString("Prefix") + Messages.getString("ChatMessage"));
                            }
                        } else if (e.getCurrentItem().getItemMeta().getLore() != null && e.getCurrentItem().getItemMeta().getLore().contains(ItemTexts.getString("SellLimitLore"))) {
                            if (Config.useAnvil()) {
                                AnvilGUI gui = Core.getAnvilGUI();
                                gui.doGUIThing(p, new AnvilGUI.AnvilClickEventHandler() {
                                    @Override
                                    public void onAnvilClick(AnvilGUI.AnvilClickEvent ev) {
                                        if (ev.getSlot() == 2) {
                                            ev.setWillClose(true);
                                            ev.setWillDestroy(true);


                                            if (ev.getCurrentItem().getType() == Material.PAPER) {
                                                if (ev.getCurrentItem().hasItemMeta()) {
                                                    if (ev.getCurrentItem().getItemMeta().getDisplayName() != null) {
                                                        String name = ev.getCurrentItem().getItemMeta().getDisplayName();

                                                        boolean can;
                                                        int amt = 0;
                                                        try {
                                                            amt = Integer.parseInt(name);
                                                            can = true;
                                                        } catch (Exception ex) {
                                                            p.sendMessage(Messages.getString("Prefix") + Messages.getString("InvalidNumber"));
                                                            can = false;
                                                        }

                                                        if (can) {

                                                            shopItem.setLimit(amt);

                                                            p.sendMessage(Messages.getString("Prefix") + Messages.getString("ChangeLimit"));
                                                        }
                                                        OpenSellingOptions.openShopSellingOptions(e.getInventory(), p, shop, 1);
                                                    }
                                                }
                                            }
                                        } else {
                                            ev.setWillClose(true);
                                            ev.setWillDestroy(true);
                                        }
                                    }
                                });


                                ItemStack it = new ItemStack(Material.PAPER);
                                ItemMeta meta = it.getItemMeta();
                                meta.setDisplayName(SearchEngine.getString("NewAmount"));
                                it.setItemMeta(meta);

                                gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, it);

                                gui.open();
                            } else {
                                p.closeInventory();
                                Map<Shop, ShopItem> map = new HashMap<>();
                                map.put(shop, shopItem);

                                ChatMessages.setLimit.put(p, map);
                                p.sendMessage(Messages.getString("Prefix") + Messages.getString("ChatMessage"));
                            }
                        }
                    }
                }
            }
        }
    }
}
