package me.moomaxie.BetterShops.Listeners.ManagerOptions;

import me.moomaxie.BetterShops.Configurations.AnvilGUI;
import me.moomaxie.BetterShops.Configurations.Config;
import me.moomaxie.BetterShops.Configurations.GUIMessages.History;
import me.moomaxie.BetterShops.Configurations.GUIMessages.ItemTexts;
import me.moomaxie.BetterShops.Configurations.GUIMessages.MainGUI;
import me.moomaxie.BetterShops.Configurations.GUIMessages.SearchEngine;
import me.moomaxie.BetterShops.Configurations.Messages;
import me.moomaxie.BetterShops.Configurations.Permissions.Permissions;
import me.moomaxie.BetterShops.Configurations.ShopLimits;
import me.moomaxie.BetterShops.Core;
import me.moomaxie.BetterShops.History.HistoryGUI;
import me.moomaxie.BetterShops.Listeners.BuyerOptions.OpenShop;
import me.moomaxie.BetterShops.Listeners.Misc.ChatMessages;
import me.moomaxie.BetterShops.Listeners.OpenShopOptions;
import me.moomaxie.BetterShops.Listeners.SearchEngine.OpenEngine;
import me.moomaxie.BetterShops.Listeners.SellerOptions.OpenSellShop;
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
import org.bukkit.inventory.meta.SkullMeta;

import java.math.BigDecimal;
import java.util.Arrays;
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
public class ItemManager implements Listener {

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
                        if (e.getCurrentItem().getItemMeta().getLore() != null && e.getCurrentItem().getItemMeta().getLore().contains(MainGUI.getString("ManageItem"))) {
                            openItemManager(e.getInventory(), p, shop, e.getCurrentItem());
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
        namMeta.setDisplayName(ItemTexts.getString("PriceDisplayName"));
        namMeta.setLore(Arrays.asList(ItemTexts.getString("PriceLore")));
        nam.setItemMeta(namMeta);

        ItemStack desc = new ItemStack(Material.STAINED_CLAY, 1, (byte) 15);
        ItemMeta descMeta = desc.getItemMeta();
        descMeta.setDisplayName(ItemTexts.getString("RemoveItemDisplayName"));
        descMeta.setLore(Arrays.asList(ItemTexts.getString("RemoveItemLore")));
        desc.setItemMeta(descMeta);

        ItemStack addStock = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 5);
        ItemMeta addStockMeta = addStock.getItemMeta();
        addStockMeta.setDisplayName(ItemTexts.getString("AddStockDisplayName"));
        addStockMeta.setLore(Arrays.asList(ItemTexts.getString("AddStockLore")));
        addStock.setItemMeta(addStockMeta);

        ItemStack removeStock = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 14);
        ItemMeta removeStockMeta = removeStock.getItemMeta();
        removeStockMeta.setDisplayName(ItemTexts.getString("RemoveStockDisplayName"));
        removeStockMeta.setLore(Arrays.asList(ItemTexts.getString("RemoveStockLore")));
        removeStock.setItemMeta(removeStockMeta);

        ItemStack amount = new ItemStack(Material.STAINED_CLAY, 1, (byte) 2);
        ItemMeta amountMeta = amount.getItemMeta();
        amountMeta.setDisplayName(ItemTexts.getString("AmountDisplayName"));
        amountMeta.setLore(Arrays.asList(ItemTexts.getString("AmountLore")));
        amount.setItemMeta(amountMeta);

        ItemStack infinite = new ItemStack(Material.STAINED_CLAY, 1, (byte) 6);
        ItemMeta infiniteMeta = infinite.getItemMeta();
        if (shop.isInfinite(it, false)) {
            infiniteMeta.setDisplayName(ItemTexts.getString("InfiniteDisplayNameOn"));
        } else {
            infiniteMeta.setDisplayName(ItemTexts.getString("InfiniteDisplayNameOff"));
        }
        infiniteMeta.setLore(Arrays.asList(ItemTexts.getString("InfiniteLore")));
        infinite.setItemMeta(infiniteMeta);

        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName(MainGUI.getString("BackArrow"));
        back.setItemMeta(backMeta);

        inv.setItem(0, back);

        inv.setItem(4, it);

        inv.setItem(inv.firstEmpty(), nam);
        inv.setItem(inv.firstEmpty(), desc);

        inv.setItem(inv.firstEmpty(), amount);
        if (p.isOp() || Config.usePerms() && Permissions.hasInfinitePerm(p)) {
            inv.setItem(inv.firstEmpty() + 1, infinite);
        }


        inv.setItem(32, removeStock);
        inv.setItem(33, removeStock);
        inv.setItem(34, removeStock);
        inv.setItem(35, removeStock);


        inv.setItem(27, addStock);
        inv.setItem(28, addStock);
        inv.setItem(29, addStock);
        inv.setItem(30, addStock);


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

                    boolean sell = false;
                    if (e.getInventory().getItem(4).getItemMeta().getDisplayName() != null && e.getInventory().getItem(4).getItemMeta().getDisplayName().equals(SearchEngine.getString("SearchSellItems"))) {
                        sell = true;
                    }

                    if (e.getInventory().getSize() > 9) {





                        if (e.getInventory().getItem(4) != null && e.getInventory().getItem(4).getType() != Material.AIR && e.getInventory().getItem(4).hasItemMeta() && e.getInventory().getItem(4).getItemMeta().getLore() != null && e.getInventory().getItem(4).getItemMeta().getLore().contains(MainGUI.getString("ManageItem")) || e.getInventory().getItem(4).getItemMeta().getLore() != null && e.getInventory().getItem(4).getItemMeta().getLore().contains(MainGUI.getString("ShopKeeperManage"))
                                && e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().equals(MainGUI.getString("BackArrow"))) {
                            if (shop.getOwner().getUniqueId().equals(p.getUniqueId())) {
                                if (!shop.isServerShop()) {
                                    OpenShopOptions.openShopOwnerOptionsInventory(e.getInventory(), p, shop, 1);
                                } else {
                                    OpenShop.openShopItems(e.getInventory(), p, shop, 1);
                                }
                            } else {
                                OpenShop.openShopItems(e.getInventory(), p, shop, 1);
                            }
                        }

                        if (shop.getOwner().getUniqueId().equals(p.getUniqueId()) && e.isLeftClick()) {
                            boolean b = shop.isOpen();
                            if (e.getCurrentItem().getItemMeta().getLore() != null && e.getCurrentItem().getItemMeta().getLore().contains(MainGUI.getString("TurnOffServerShop"))) {
                                shop.setServerShop(false);
                                shop.setOpen(b);
                                OpenShopOptions.openShopOwnerOptionsInventory(e.getInventory(), p, shop, 1);

                            }
                        }

                        if (shop.getOwner().getUniqueId().equals(p.getUniqueId())) {
                            if (e.getCurrentItem().getItemMeta().getLore() != null && e.getCurrentItem().getItemMeta().getLore().contains(History.getString("OpenHistory"))) {
                                HistoryGUI.openHistoryGUI(p, shop, 1);
                            }
                        }

                        if (e.getCurrentItem().getItemMeta().getLore() != null && e.getCurrentItem().getItemMeta().getLore().contains(SearchEngine.getString("ReturnLore"))) {
                            if (!sell){
                                OpenShop.openShopItems(e.getInventory(),p,shop,1);
                            } else{
                                OpenSellShop.openSellerShop(e.getInventory(),p,shop,1);
                            }

                        }

                        if (e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().equals(ItemTexts.getString("PriceDisplayName"))) {
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

                                                        BigDecimal bd = new BigDecimal(amt);
                                                        bd = bd.setScale(2,BigDecimal.ROUND_HALF_UP);
                                                        amt = bd.doubleValue();

                                                        if (can) {
                                                            if (amt > 0) {
                                                                if (amt <= Config.getMaxPrice()) {
                                                                    shop.setPrice(ite, amt, false);
                                                                    p.sendMessage(Messages.getPrefix() + Messages.getChangePrice());
                                                                } else {
                                                                    if (String.valueOf(Config.getMaxPrice()).contains("E")) {
                                                                        p.sendMessage(Messages.getPrefix() + "§cThat price is too high §7(Max: " + Config.getMaxPriceAsString() + ")");
                                                                    } else {
                                                                        p.sendMessage(Messages.getPrefix() + "§cThat price is too high §7(Max: " + Config.getMaxPrice() + ")");
                                                                    }
                                                                }
                                                            } else {
                                                                p.sendMessage(Messages.getPrefix() + "§cMust be greater than 0");
                                                            }
                                                        }
                                                        OpenShopOptions.openShopOwnerOptionsInventory(e.getInventory(), (Player) e.getWhoClicked(), shop, 1);
                                                    }
                                                }
                                            }

                                        } else {
                                            ev.setWillClose(false);
                                            ev.setWillDestroy(false);
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

                                ChatMessages.setBuyPrice.put(p, map);
                                p.sendMessage(Messages.getPrefix() + Messages.getChatMessage());
                            }
                        } else if (e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().equals(ItemTexts.getString("AmountDisplayName"))) {
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
                                                            OpenShopOptions.openShopOwnerOptionsInventory(e.getInventory(), p, shop, 1);
                                                            can = false;
                                                        }

                                                        if (can) {

                                                            if (amt > 0 && amt <= 2304) {
                                                                shop.setAmount(ite, amt, false);

                                                                ((Player) e.getWhoClicked()).sendMessage(Messages.getPrefix() + Messages.getChangeAmount());
                                                            } else {
                                                                p.sendMessage(Messages.getPrefix() + "§cMust be between 0 and 2034");
                                                            }
                                                        }
                                                        OpenShopOptions.openShopOwnerOptionsInventory(e.getInventory(), (Player) e.getWhoClicked(), shop, 1);
                                                    }
                                                }
                                            }

                                        } else {
                                            ev.setWillClose(false);
                                            ev.setWillDestroy(false);
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

                                ChatMessages.setBuyAmount.put(p, map);
                                p.sendMessage(Messages.getPrefix() + Messages.getChatMessage());
                            }
                        } else if (e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().equals(ItemTexts.getString("AddStockDisplayName"))) {

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
                                                                        Stocks.addAll(ite, shop, p);
                                                                        return;
                                                                    } else {
                                                                        ((Player) e.getWhoClicked()).sendMessage(Messages.getPrefix() + Messages.getInvalidNumber());
                                                                        OpenShopOptions.openShopOwnerOptionsInventory(e.getInventory(), p, shop, 1);
                                                                        return;
                                                                    }
                                                                }


                                                                Stocks.addStock(ite, amt, p, shop);
                                                            }
                                                        }
                                                    }

                                                } else {
                                                    ev.setWillClose(false);
                                                    ev.setWillDestroy(false);
                                                }
                                            }
                                        }

                                );

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

                                ChatMessages.addStock.put(p, map);
                                p.sendMessage(Messages.getPrefix() + Messages.getChatMessage());
                            }
                        } else if (e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().equals(ItemTexts.getString("RemoveStockDisplayName"))) {

                            if (Config.useAnvil() && Core.getAnvilGUI() != null) {
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
                                                                        Stocks.removeAll(ite, shop, p);
                                                                        return;
                                                                    } else {
                                                                        ((Player) e.getWhoClicked()).sendMessage(Messages.getPrefix() + Messages.getInvalidNumber());
                                                                        OpenShopOptions.openShopOwnerOptionsInventory(e.getInventory(), p, shop, 1);
                                                                        return;
                                                                    }
                                                                }


                                                                Stocks.removeStock(ite, amt, p, shop);
                                                            }
                                                        }
                                                    }

                                                } else {
                                                    ev.setWillClose(false);
                                                    ev.setWillDestroy(false);
                                                }
                                            }
                                        }

                                );

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

                                ChatMessages.removeStock.put(p, map);
                                p.sendMessage(Messages.getPrefix() + Messages.getChatMessage());
                            }
                        } else if (e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().equals(ItemTexts.getString("RemoveItemDisplayName"))) {

                            ItemStack item = null;

                            if (ite.getType() == Material.SKULL_ITEM) {
                                SkullMeta meta = (SkullMeta) ite.getItemMeta();

                                meta.setLore(shop.getLore(ite));
                                ite.setItemMeta(meta);
                            } else {
                                ItemMeta meta = ite.getItemMeta();

                                meta.setLore(shop.getLore(ite));
                                ite.setItemMeta(meta);
                            }


                            for (ItemStack it : shop.getShopContents(false).keySet()) {
                                if (it.getType() == Material.SKULL_ITEM) {
                                    SkullMeta meta = (SkullMeta) it.getItemMeta();

                                    meta.setLore(shop.getLore(it));
                                    it.setItemMeta(meta);
                                } else {
                                    ItemMeta meta = it.getItemMeta();

                                    meta.setLore(shop.getLore(it));
                                    it.setItemMeta(meta);
                                }

                                it.setAmount(1);

                                if (ite.equals(it) || ite.toString().equals(it.toString()) && ite.getData().getData() == it.getData().getData() && ite.getDurability() == it.getDurability()) {
                                    item = it;
                                }
                            }

                            if (item != null) {
                                if (shop.getStock(item, false) > 0) {
                                    Stocks.removeAllOfDeletedItem(item, shop, p, false);
                                }
                                shop.deleteItem(ite, false);

                                OpenShopOptions.openShopOwnerOptionsInventory(null, (Player) e.getWhoClicked(), shop, 1);
                                ((Player) e.getWhoClicked()).sendMessage(Messages.getPrefix() + Messages.getRemoveItem());

                            } else {
                                p.sendMessage(Messages.getPrefix() + "§cNon-Existing Item");
                            }

                        } else if (e.getCurrentItem().getItemMeta().getLore() != null && e.getCurrentItem().getItemMeta().getLore().contains(ItemTexts.getString("InfiniteLore"))) {


                            if (shop.isInfinite(ite, false) || e.getCurrentItem().getItemMeta().getDisplayName().equals(ItemTexts.getString("InfiniteDisplayNameOn"))) {
                                shop.setInfinite(ite, false, false);
                                ((Player) e.getWhoClicked()).sendMessage(Messages.getPrefix() + Messages.getInfiniteStock().replaceAll("<Value>", "§cOff"));
                            } else {
                                shop.setInfinite(ite, true, false);
                                ((Player) e.getWhoClicked()).sendMessage(Messages.getPrefix() + Messages.getInfiniteStock().replaceAll("<Value>", "§aOn"));
                            }

                            openItemManager(e.getInventory(), (Player) e.getWhoClicked(), shop, ite);
                        }
                    }



                    if (e.getCurrentItem().getItemMeta() != null && e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().equals(SearchEngine.getString("SearchMaterials"))) {
                        if (Config.useAnvil()) {
                            OpenEngine.useMaterialSearch(p, shop, sell);
                        } else {
                            p.closeInventory();
                            Map<Shop, Boolean> map = new HashMap<>();
                            map.put(shop, sell);

                            ChatMessages.searchMaterial.put(p, map);
                            p.sendMessage(Messages.getPrefix() + Messages.getChatMessage());
                        }
                    }
                    if (e.getCurrentItem().getItemMeta() != null && e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().equals(SearchEngine.getString("SearchName"))) {
                        if (Config.useAnvil()) {
                            OpenEngine.useNameSearch(p, shop, sell);
                        } else {
                            p.closeInventory();
                            Map<Shop, Boolean> map = new HashMap<>();
                            map.put(shop, sell);

                            ChatMessages.searchName.put(p, map);
                            p.sendMessage(Messages.getPrefix() + Messages.getChatMessage());
                        }
                    }
                    if (e.getCurrentItem().getItemMeta() != null && e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().equals(SearchEngine.getString("SearchId"))) {
                        if (Config.useAnvil()) {
                            OpenEngine.useIdSearch(p, shop, sell);
                        } else {
                            p.closeInventory();
                            Map<Shop, Boolean> map = new HashMap<>();
                            map.put(shop, sell);

                            ChatMessages.searchID.put(p, map);
                            p.sendMessage(Messages.getPrefix() + Messages.getChatMessage());
                        }
                    }
                    if (e.getCurrentItem().getItemMeta() != null && e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().equals(SearchEngine.getString("SearchPrice"))) {
                        if (Config.useAnvil()) {
                            OpenEngine.usePriceSearch(p, shop, sell);
                        } else {
                            p.closeInventory();
                            Map<Shop, Boolean> map = new HashMap<>();
                            map.put(shop, sell);

                            ChatMessages.searchPrice.put(p, map);
                            p.sendMessage(Messages.getPrefix() + Messages.getChatMessage());
                        }
                    }

                    if (e.getCurrentItem().getItemMeta() != null && e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().equals(MainGUI.getString("BackArrow")) && e.getInventory().getItem(4).getItemMeta().getDisplayName() != null && e.getInventory().getItem(4).getItemMeta().getDisplayName().equals(SearchEngine.getString("SearchBuyItems"))) {
                        OpenShop.openShopItems(e.getInventory(), p, shop, 1);
                    }

                    if (e.getCurrentItem().getItemMeta() != null && e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().equals(MainGUI.getString("BackArrow")) && e.getInventory().getItem(4).getItemMeta().getDisplayName() != null && e.getInventory().getItem(4).getItemMeta().getDisplayName().equals(SearchEngine.getString("SearchSellItems"))) {
                        OpenSellShop.openSellerShop(e.getInventory(), p, shop, 1);
                    }


                }
            }
        }
    }


}
