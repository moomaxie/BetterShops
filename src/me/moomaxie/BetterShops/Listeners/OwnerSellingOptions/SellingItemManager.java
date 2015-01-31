package me.moomaxie.BetterShops.Listeners.OwnerSellingOptions;

import me.moomaxie.BetterShops.Configurations.AnvilGUI;
import me.moomaxie.BetterShops.Configurations.Config;
import me.moomaxie.BetterShops.Configurations.GUIMessages.ItemTexts;
import me.moomaxie.BetterShops.Configurations.GUIMessages.MainGUI;
import me.moomaxie.BetterShops.Configurations.Messages;
import me.moomaxie.BetterShops.Configurations.ShopLimits;
import me.moomaxie.BetterShops.Core;
import me.moomaxie.BetterShops.Listeners.BuyerOptions.OpenShop;
import me.moomaxie.BetterShops.Listeners.ManagerOptions.Stocks;
import me.moomaxie.BetterShops.Listeners.Misc.ChatMessages;
import me.moomaxie.BetterShops.Listeners.OpenShopOptions;
import me.moomaxie.BetterShops.Shops.Shop;
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
        if (e.getInventory().getName().contains("§7[Shop]")) {
            e.setCancelled(true);
            if (e.getInventory().getType() == InventoryType.CHEST) {
                if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {

                    String name = e.getInventory().getName();
                    name = name.substring(11);

                    Shop shop = ShopLimits.fromString(p, name);

                    if (shop.getOwner().getUniqueId().equals(p.getUniqueId())) {
                        if (e.getCurrentItem().getItemMeta().getLore() != null && e.getCurrentItem().getItemMeta().getLore().contains(MainGUI.getString("SellOptions"))) {
                            openItemManager(e.getInventory(),p, shop, e.getCurrentItem());
                        }
                    }
                }
            }
        }
    }

    public void openItemManager(Inventory inv, Player p, Shop shop, ItemStack it) {
        boolean same = true;
        if (inv == null) {
            same = false;
            inv = Bukkit.createInventory(p, 54, "§7[Shop] §a" + shop.getName());
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
        if (e.getInventory().getName().contains("§7[Shop]")) {
            e.setCancelled(true);

            if (e.getInventory().getType() == InventoryType.CHEST) {
                if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
                    String name = e.getInventory().getName();
                    name = name.substring(11);

                    final Shop shop = ShopLimits.fromString(p, name);

                    final ItemStack ite = e.getInventory().getItem(4);

                    if (e.getInventory().getSize() > 9) {

                        if (e.getInventory().getItem(4) != null && e.getInventory().getItem(4).getType() != Material.AIR && e.getInventory().getItem(4).hasItemMeta() && e.getInventory().getItem(4).getItemMeta().getLore() != null && e.getInventory().getItem(4).getItemMeta().getLore().contains(MainGUI.getString("SellOptions")) && e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().equals(MainGUI.getString("BackArrow"))) {
                            if (shop.getOwner().getUniqueId().equals(p.getUniqueId())) {
                                OpenSellingOptions.openShopSellingOptions(e.getInventory(), p, shop, 1);
                            } else {
                                OpenShop.openShopItems(e.getInventory(), p, shop, 1);
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
                                                            ((Player) e.getWhoClicked()).sendMessage(Messages.getPrefix() + Messages.getInvalidNumber());
                                                            can = false;
                                                        }

                                                        if (can) {
                                                            shop.setPrice(ite, amt, true);
                                                            ((Player) e.getWhoClicked()).sendMessage(Messages.getPrefix() + Messages.getChangePrice());
                                                        }
                                                        OpenShopOptions.openShopOwnerOptionsInventory(e.getInventory(), (Player) e.getWhoClicked(), shop, 1);
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
                                meta.setDisplayName("Type New Price");
                                it.setItemMeta(meta);

                                gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, it);

                                gui.open();
                            } else {
                                p.closeInventory();
                                Map<Shop, ItemStack> map = new HashMap<>();
                                map.put(shop, ite);

                                ChatMessages.setSellPrice.put(p, map);
                                p.sendMessage(Messages.getPrefix() + Messages.getChatMessage());
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
                                                            ((Player) e.getWhoClicked()).sendMessage(Messages.getPrefix() + Messages.getInvalidNumber());
                                                            can = false;
                                                        }

                                                        if (can) {

                                                            shop.setAmount(ite, amt, true);
                                                            ((Player) e.getWhoClicked()).sendMessage(Messages.getPrefix() + Messages.getChangeAmount());

                                                        }
                                                        OpenShopOptions.openShopOwnerOptionsInventory(e.getInventory(), (Player) e.getWhoClicked(), shop, 1);
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
                                meta.setDisplayName("Type New Amount");
                                it.setItemMeta(meta);

                                gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, it);

                                gui.open();
                            } else {
                                p.closeInventory();
                                Map<Shop, ItemStack> map = new HashMap<>();
                                map.put(shop, ite);

                                ChatMessages.setSellAmount.put(p, map);
                                p.sendMessage(Messages.getPrefix() + Messages.getChatMessage());
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
                                                                Stocks.collectAll(ite, shop, p);
                                                                return;
                                                            } else {
                                                                ((Player) e.getWhoClicked()).sendMessage(Messages.getPrefix() + Messages.getInvalidNumber());
                                                                OpenSellingOptions.openShopSellingOptions(e.getInventory(), p, shop, 1);
                                                                return;
                                                            }
                                                        }

                                                        Stocks.collectStock(ite, amt, p, shop);

                                                    }
                                                    OpenShopOptions.openShopOwnerOptionsInventory(e.getInventory(), (Player) e.getWhoClicked(), shop, 1);
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
                                meta.setDisplayName("How Much?");
                                it.setItemMeta(meta);

                                gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, it);

                                gui.open();

                            } else {
                                p.closeInventory();
                                Map<Shop, ItemStack> map = new HashMap<>();
                                map.put(shop, ite);

                                ChatMessages.collectStock.put(p, map);
                                p.sendMessage(Messages.getPrefix() + Messages.getChatMessage());
                            }
                        } else if (e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().equals(ItemTexts.getString("SellRemoveItemDisplayName"))) {

                            ItemStack item = null;


                            for (ItemStack it : shop.getShopContents(true).keySet()) {
                                if (shop.compareItems(ite, it, true)) {
                                    item = it;
                                }
                            }


                            if (item != null) {
                                if (shop.getStock(item, true) > 0) {
                                    Stocks.removeAllOfDeletedItem(item,shop,p,true);
                                    shop.deleteItem(ite, true);
                                    OpenSellingOptions.openShopSellingOptions(null, p, shop, 1);
                                    ((Player) e.getWhoClicked()).sendMessage(Messages.getPrefix() + Messages.getRemoveItem());
                                } else {
                                    shop.deleteItem(ite, true);
                                    OpenSellingOptions.openShopSellingOptions(null, p, shop, 1);
                                    ((Player) e.getWhoClicked()).sendMessage(Messages.getPrefix() + Messages.getRemoveItem());
                                }
                            } else {
                                p.sendMessage(Messages.getPrefix() + "§cNon-Existing Item");
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
                                                            ((Player) e.getWhoClicked()).sendMessage(Messages.getPrefix() + Messages.getInvalidNumber());
                                                            can = false;
                                                        }

                                                        if (can) {

                                                            int am = shop.getAmount(ite, true);
                                                            double price = shop.getPrice(ite, true);

                                                            int slot = shop.getSlotForItem(ite, true);

                                                            Material mat = ite.getType();

                                                            shop.deleteItem(ite, true);

                                                            ItemStack it = new ItemStack(mat, 1, (byte) amt);

                                                            shop.addItem(it, slot, true);

                                                            shop.setAmount(it, am, true);
                                                            shop.setPrice(it, price, true);

                                                            ((Player) e.getWhoClicked()).sendMessage(Messages.getPrefix() + Messages.getChangeData());

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
                                meta.setDisplayName("New Data");
                                it.setItemMeta(meta);

                                gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, it);

                                gui.open();
                            } else {
                                p.closeInventory();
                                Map<Shop, ItemStack> map = new HashMap<>();
                                map.put(shop, ite);

                                ChatMessages.changeData.put(p, map);
                                p.sendMessage(Messages.getPrefix() + Messages.getChatMessage());
                            }
                        }
                    }
                }
            }
        }
    }
}
