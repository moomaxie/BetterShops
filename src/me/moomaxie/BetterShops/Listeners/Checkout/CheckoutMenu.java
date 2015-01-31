package me.moomaxie.BetterShops.Listeners.Checkout;

import BetterShops.Dev.API.Events.ShopBuyItemEvent;
import me.moomaxie.BetterShops.Configurations.Config;
import me.moomaxie.BetterShops.Configurations.GUIMessages.Checkout;
import me.moomaxie.BetterShops.Configurations.GUIMessages.MainGUI;
import me.moomaxie.BetterShops.Configurations.Messages;
import me.moomaxie.BetterShops.Configurations.ShopLimits;
import me.moomaxie.BetterShops.Core;
import me.moomaxie.BetterShops.Listeners.BuyerOptions.OpenShop;
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
import org.bukkit.material.MaterialData;

import java.util.*;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2014. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */

public class CheckoutMenu implements Listener {

    private static HashMap<HashMap<UUID, Shop>, List<HashMap<HashMap<ItemStack, Byte>, Integer>>> cart = new HashMap<>();

    public static void addToCart(Player p, Shop shop, ItemStack item, int amt) {
        HashMap<UUID, Shop> k = null;
        boolean can = false;

        for (HashMap<UUID, Shop> key : cart.keySet()) {
            if (key.keySet().contains(p.getUniqueId())) {
                if (key.get(p.getUniqueId()).getName().equals(shop.getName())) {
                    can = true;
                    k = key;
                    break;
                }
            }
        }

        if (can) {

            HashMap<HashMap<ItemStack, Byte>, Integer> value = new HashMap<>();
            HashMap<ItemStack, Byte> by = new HashMap<>();
            by.put(item, item.getData().getData());
            value.put(by, amt);

            List<HashMap<HashMap<ItemStack, Byte>, Integer>> v = cart.get(k);

            v.add(value);

            cart.put(k, v);


        } else {
            HashMap<UUID, Shop> key = new HashMap<>();
            key.put(p.getUniqueId(), shop);

            HashMap<HashMap<ItemStack, Byte>, Integer> value = new HashMap<>();
            HashMap<ItemStack, Byte> by = new HashMap<>();
            by.put(item, item.getData().getData());
            value.put(by, amt);

            List<HashMap<HashMap<ItemStack, Byte>, Integer>> v = new ArrayList<>();
            v.add(value);

            cart.put(key, v);
        }
    }

    public static void openCheckoutMenu(Player p, Shop shop) {

        Inventory inv = Bukkit.createInventory(p, 54, "§7[Shop] §a" + shop.getName());

        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7);
        ItemMeta m = item.getItemMeta();
        m.setDisplayName(" ");
        item.setItemMeta(m);
        for (int i = 0; i < 9; i++) {
            inv.setItem(i, item);
        }

        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName(MainGUI.getString("BackArrow"));
        back.setItemMeta(backMeta);

        ItemStack price = new ItemStack(Material.EMERALD);
        ItemMeta priceMeta = price.getItemMeta();
        priceMeta.setDisplayName(Checkout.getString("Prices"));
        priceMeta.setLore(Arrays.asList(Checkout.getString("NothingInCart")));
        price.setItemMeta(priceMeta);

        ItemStack buy = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 5);
        ItemMeta buyMeta = buy.getItemMeta();
        buyMeta.setDisplayName(Checkout.getString("BuyItems"));
        buy.setItemMeta(buyMeta);

        ItemStack no = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 0);
        ItemMeta noMeta = no.getItemMeta();
        noMeta.setDisplayName(Checkout.getString("CannotAfford"));
        no.setItemMeta(noMeta);

        ItemStack noItems = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 0);
        ItemMeta noItemsMeta = noItems.getItemMeta();
        noItemsMeta.setDisplayName(Checkout.getString("NothingInCart"));
        noItems.setItemMeta(noItemsMeta);

        inv.setItem(7, noItems);
        inv.setItem(8, noItems);

        inv.setItem(4, price);

        double total = 0.00;

        inv.setItem(0, back);

        inv.setItem(0, back);

        boolean can = false;

        HashMap<UUID, Shop> k = null;

        for (HashMap<UUID, Shop> key : cart.keySet()) {
            if (key.keySet().contains(p.getUniqueId())) {
                if (key.get(p.getUniqueId()).getName().equals(shop.getName())) {
                    can = true;
                    k = key;
                    break;
                }
            }
        }

        if (can) {
            List<HashMap<HashMap<ItemStack, Byte>, Integer>> items = cart.get(k);
            if (items != null) {
                for (HashMap<HashMap<ItemStack, Byte>, Integer> ie : items) {
                    for (HashMap<ItemStack, Byte> i : ie.keySet()) {
                        for (ItemStack it : i.keySet()) {

                            if (it != null && ie.get(i) != null) {
                                MaterialData data = new MaterialData(it.getType(), i.get(it));
                                it.setData(data);

                                total = total + (ie.get(i) * (shop.getPrice(it, false) / shop.getAmount(it, false)));

                                ItemMeta meta = it.getItemMeta();
                                List<String> lore;
                                if (shop.getLore(it, false) != null) {
                                    lore = shop.getLore(it, false);
                                } else {
                                    lore = new ArrayList<>();
                                }

                                ItemStack ite = it.clone();

                                if (lore != null) {

                                    if (ie.get(i) != null) {
                                        lore.add(MainGUI.getString("Amount")+ " §7" + ie.get(i));
                                    } else {
                                        lore.add(MainGUI.getString("Amount")+ " §71");
                                    }
                                    if (ie.get(i) != null) {
                                        lore.add(MainGUI.getString("Price")+ " §7" + (shop.getPrice(it, false) / shop.getAmount(it, false)) * ie.get(i));
                                    } else {
                                        lore.add(MainGUI.getString("Price")+ " §7" + (shop.getPrice(it, false) / shop.getAmount(it, false)) * 1);
                                    }
                                    lore.add(" ");
                                    lore.add(Checkout.getString("ClickToRemove"));
                                    meta.setLore(lore);
                                    ite.setItemMeta(meta);
                                }
                                inv.setItem(inv.firstEmpty(), ite);

                                priceMeta.setLore(Arrays.asList(Checkout.getString("TotalPrice") + " §7" + total,
                                        Checkout.getString("Balance") + " §7" + Core.getEconomy().getBalance(p),
                                        Checkout.getString("NewBalance") + " §7" + (Core.getEconomy().getBalance(p) - total)));
                                price.setItemMeta(priceMeta);
                                inv.setItem(4, price);

                                inv.setItem(7, buy);
                                inv.setItem(8, buy);
                            }
                        }
                    }
                }
            }
        }

        if ((Core.getEconomy().getBalance(p) - total) < 0) {
            inv.setItem(7, no);
            inv.setItem(8, no);
        }


        p.openInventory(inv);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onArrange(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getInventory().getName().contains("§7[Shop]")) {
            e.setCancelled(true);
            if (e.getInventory().getType() == InventoryType.CHEST) {
                if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {

                    String name = e.getInventory().getName();
                    name = name.substring(11);

                    Shop shop = ShopLimits.fromString(p, name);

                    if (e.getCurrentItem().getItemMeta() != null) {

                        if (e.getCurrentItem().getItemMeta().getLore() != null) {
                            if (e.getCurrentItem().getItemMeta().getLore().contains(MainGUI.getString("AddToCart")) && e.isShiftClick()) {
                                AmountChooser.openAmountChooser(1, e.getCurrentItem(), p, shop, e.getInventory());
                            }
                        }

                        if (e.getCurrentItem().getItemMeta() != null && e.getCurrentItem().getItemMeta().getLore() != null && e.getCurrentItem().getItemMeta().getLore().contains(MainGUI.getString("CheckoutLore")) && e.isLeftClick()) {
                            openCheckoutMenu(p, shop);

                        }

                        if (e.getCurrentItem().getItemMeta() != null && e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().contains(Checkout.getString("BuyItems"))) {
                            buyCheckoutItems(p, shop);

                        }

                        if (e.getCurrentItem().getItemMeta() != null && e.getCurrentItem().getItemMeta().getLore() != null && e.getCurrentItem().getItemMeta().getLore().contains(Checkout.getString("ClickToRemove")) && e.isLeftClick()) {
                            boolean can = false;

                            HashMap<UUID, Shop> k = null;

                            for (HashMap<UUID, Shop> key : cart.keySet()) {
                                if (key.keySet().contains(p.getUniqueId())) {
                                    if (key.get(p.getUniqueId()).getName().equals(shop.getName())) {
                                        can = true;
                                        k = key;
                                        break;
                                    }
                                }
                            }

                            if (can) {
                                List<HashMap<HashMap<ItemStack, Byte>, Integer>> items = cart.get(k);
                                if (items != null) {
                                    for (HashMap<HashMap<ItemStack, Byte>, Integer> ie : items) {
                                        for (HashMap<ItemStack, Byte> i : ie.keySet()) {
                                            for (ItemStack it : i.keySet()) {
                                                ItemMeta meta = it.getItemMeta();
                                                List<String> lore;
                                                if (shop.getLore(it, false) != null) {
                                                    lore = shop.getLore(it, false);
                                                } else {
                                                    lore = new ArrayList<>();
                                                }

                                                ItemStack ite = it.clone();

                                                if (lore != null) {
                                                    if (!lore.contains(Checkout.getString("ClickToRemove"))) {
                                                        if (ie.get(i) != null) {
                                                            lore.add(MainGUI.getString("Amount")+ " §7" + ie.get(i));
                                                        } else {
                                                            lore.add(MainGUI.getString("Amount")+ " §71");
                                                        }
                                                        if (ie.get(i) != null) {
                                                            lore.add(MainGUI.getString("Price")+ " §7" + (shop.getPrice(it, false) / shop.getAmount(it, false)) * ie.get(i));
                                                        } else {
                                                            lore.add(MainGUI.getString("Price")+ " §7" + (shop.getPrice(it, false) / shop.getAmount(it, false)) * 1);
                                                        }
                                                        lore.add(" ");
                                                        lore.add(Checkout.getString("ClickToRemove"));
                                                        meta.setLore(lore);
                                                        ite.setItemMeta(meta);
                                                    }
                                                }

                                                if (e.getCurrentItem().equals(ite) && e.getCurrentItem().getData().getData() == ite.getData().getData() || e.getCurrentItem().toString().equals(ite.toString()) && e.getCurrentItem().getData().getData() == ite.getData().getData()) {

                                                    cart.get(k).remove(ie);
                                                    openCheckoutMenu(p, shop);
                                                    return;
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
    }

    public static void buyCheckoutItems(Player p, Shop shop) {
        double total = 0.0;
        boolean can = false;

        HashMap<UUID, Shop> k = null;

        for (HashMap<UUID, Shop> key : cart.keySet()) {
            if (key.keySet().contains(p.getUniqueId())) {
                if (key.get(p.getUniqueId()).getName().equals(shop.getName())) {
                    can = true;
                    k = key;
                    break;
                }
            }
        }

        double t = 0;
        if (can) {
            List<HashMap<HashMap<ItemStack, Byte>, Integer>> items = cart.get(k);
            if (items != null) {
                for (HashMap<HashMap<ItemStack, Byte>, Integer> ie : items) {
                    for (HashMap<ItemStack, Byte> i : ie.keySet()) {
                        for (ItemStack it : i.keySet()) {
                            ItemMeta meta = it.getItemMeta();
                            List<String> lore;
                            if (shop.getLore(it, false) != null) {
                                lore = shop.getLore(it, false);
                            } else {
                                lore = new ArrayList<>();
                            }

                            int amt = 1;

                            ItemStack ite = it.clone();

                            if (lore != null) {
                                if (!lore.contains(Checkout.getString("ClickToRemove"))) {
                                    if (ie.get(i) != null) {
                                        lore.add(MainGUI.getString("Amount")+ " §7" + ie.get(i));
                                        amt = ie.get(i);
                                    } else {
                                        lore.add(MainGUI.getString("Amount")+ " §71");
                                    }
                                    if (ie.get(i) != null) {
                                        lore.add(MainGUI.getString("Price")+ " §7" + shop.getPrice(it, false) * ie.get(i));
                                        total = total + shop.getPrice(it, false) * ie.get(i);
                                    } else {
                                        lore.add(MainGUI.getString("Price")+ " §7" + shop.getPrice(it, false) * 1);
                                        total = total + shop.getPrice(it, false) * 1;
                                    }
                                    lore.add(" ");
                                    lore.add(Checkout.getString("ClickToRemove"));
                                    meta.setLore(lore);
                                    ite.setItemMeta(meta);
                                }
                            }

                            if (shop.getStock(ite, false) >= amt) {
                                if (Core.getEconomy().getBalance(p) >= shop.getPrice(it, false) * amt) {
                                    if (!shop.isServerShop()) {
                                        Core.getEconomy().withdrawPlayer(Bukkit.getOfflinePlayer(p.getUniqueId()), (shop.getPrice(it, false) / shop.getAmount(it, false)) * amt);
                                        Core.getEconomy().depositPlayer(Bukkit.getOfflinePlayer(shop.getOwner().getUniqueId()), (shop.getPrice(it, false) / shop.getAmount(it, false)) * amt);
                                        t = t + (shop.getPrice(it, false) / shop.getAmount(it, false)) * amt;
                                        if (shop.isNotify()) {
                                            if (shop.getOwner() != null && shop.getOwner().isOnline()) {
                                                shop.getOwner().getPlayer().sendMessage(Messages.getPrefix() + Messages.getNotifyBuyItem().replaceAll("<Player>", p.getDisplayName()));
                                                shop.getOwner().getPlayer().sendMessage(Messages.getPrefix() + Messages.getGainedAmountMessage().replaceAll("<Amount>", "" + (shop.getPrice(it, false) / shop.getAmount(it, false)) * amt));

                                                if (Core.isAboveEight() && Config.useTitles()) {

                                                    Core.getTitleManager().setTimes(shop.getOwner().getPlayer(), 20, 60, 20);
                                                    Core.getTitleManager().sendTitle(shop.getOwner().getPlayer(), Messages.getNotifyBuyItem().replaceAll("<Player>", p.getDisplayName()));
                                                    Core.getTitleManager().sendSubTitle(shop.getOwner().getPlayer(), Messages.getGainedAmountMessage().replaceAll("<Amount>", "" + (shop.getPrice(it, false) / shop.getAmount(it, false)) * amt));


                                                }
                                            }
                                        }
                                    } else {
                                        Core.getEconomy().withdrawPlayer(Bukkit.getOfflinePlayer(p.getUniqueId()), (shop.getPrice(it, false) / shop.getAmount(it, false)) * amt);
                                        t = t + (shop.getPrice(it, false) / shop.getAmount(it, false)) * amt;
                                    }

                                    for (int o = 0; o < amt; o++) {
                                        p.getInventory().addItem(it);
                                    }
                                    if (!shop.isInfinite(ite, false))
                                        shop.setStock(ite, shop.getStock(ite, false) - amt, false);

                                    shop.getHistory().addTransaction(p,new Date(),ite,t,amt,false,true);

                                } else {
                                    if (ite.getItemMeta() != null && ite.getItemMeta().getDisplayName() != null) {
                                        p.sendMessage(Messages.getPrefix() + "§cCould Not Afford §d" + ite.getItemMeta().getDisplayName());
                                    } else {
                                        p.sendMessage(Messages.getPrefix() + "§cCould Not Afford §d" + ite.getType().name().replaceAll("_", " "));
                                    }
                                    p.closeInventory();
                                    return;
                                }
                            } else {
                                if (shop.getStock(ite, false) > 0 || shop.getStock(ite, false) <= 0 && shop.isInfinite(ite, false)) {
                                    if (Core.getEconomy().getBalance(p) >= shop.getPrice(it, false) * shop.getStock(ite, false)) {
                                        if (!shop.isServerShop()) {
                                            Core.getEconomy().withdrawPlayer(Bukkit.getOfflinePlayer(p.getUniqueId()), (shop.getPrice(it, false) / shop.getAmount(it, false)) * amt);
                                            Core.getEconomy().depositPlayer(Bukkit.getOfflinePlayer(shop.getOwner().getUniqueId()), (shop.getPrice(it, false) / shop.getAmount(it, false)) * amt);
                                            t = t + (shop.getPrice(it, false) / shop.getAmount(it, false)) * amt;
                                            if (shop.isNotify()) {
                                                if (shop.getOwner() != null && shop.getOwner().isOnline()) {
                                                    shop.getOwner().getPlayer().sendMessage(Messages.getPrefix() + Messages.getNotifyBuyItem().replaceAll("<Player>", p.getDisplayName()));
                                                    shop.getOwner().getPlayer().sendMessage(Messages.getPrefix() + Messages.getGainedAmountMessage().replaceAll("<Amount>", "" + (shop.getPrice(it, false) / shop.getAmount(it, false)) * amt));

                                                    if (Core.isAboveEight() && Config.useTitles()) {

                                                        Core.getTitleManager().setTimes(shop.getOwner().getPlayer(), 20, 60, 20);
                                                        Core.getTitleManager().sendTitle(shop.getOwner().getPlayer(), Messages.getNotifyBuyItem().replaceAll("<Player>", p.getDisplayName()));
                                                        Core.getTitleManager().sendSubTitle(shop.getOwner().getPlayer(), Messages.getGainedAmountMessage().replaceAll("<Amount>", "" + (shop.getPrice(it, false) / shop.getAmount(it, false)) * amt));


                                                    }
                                                }
                                            }
                                        } else {
                                            Core.getEconomy().withdrawPlayer(Bukkit.getOfflinePlayer(p.getUniqueId()), (shop.getPrice(it, false) / shop.getAmount(it, false)) * amt);
                                            t = t + (shop.getPrice(it, false) / shop.getAmount(it, false)) * amt;
                                        }

                                        for (int o = 0; o < amt; o++) {
                                            p.getInventory().addItem(it);
                                        }

                                        if (!shop.isInfinite(ite, false)) {
                                            if (ite.getItemMeta() != null && ite.getItemMeta().getDisplayName() != null) {
                                                p.sendMessage(Messages.getPrefix() + "§cCan only buy §e" + shop.getStock(ite, false) + " §cof §d" + ite.getItemMeta().getDisplayName());
                                            } else {
                                                p.sendMessage(Messages.getPrefix() + "§cCan only buy §e" + shop.getStock(ite, false) + " §cof §d" + ite.getType().name().replaceAll("_", " "));
                                            }
                                        }

                                        if (!shop.isInfinite(ite, false))
                                            shop.setStock(ite, 0, false);

                                        if (shop.getHistory() == null){
                                            shop.loadTransactions();
                                        }
                                        shop.getHistory().addTransaction(p,new Date(),ite,t,amt,false,true);

                                        ShopBuyItemEvent ev = new ShopBuyItemEvent(ite,shop);

                                        Bukkit.getPluginManager().callEvent(ev);

                                    } else {
                                        if (ite.getItemMeta() != null && ite.getItemMeta().getDisplayName() != null) {
                                            p.sendMessage(Messages.getPrefix() + "§cCould Not Afford §d" + ite.getItemMeta().getDisplayName());
                                        } else {
                                            p.sendMessage(Messages.getPrefix() + "§cCould Not Afford §d" + ite.getType().name().replaceAll("_", " "));
                                        }
                                        p.closeInventory();
                                        return;
                                    }
                                } else {

                                    if (ite.getItemMeta() != null && ite.getItemMeta().getDisplayName() != null) {
                                        p.sendMessage(Messages.getPrefix() + "§cRan out of stock for §d" + ite.getItemMeta().getDisplayName());
                                    } else {
                                        p.sendMessage(Messages.getPrefix() + "§cRan out of stock for §d" + ite.getType().name().replaceAll("_", " "));
                                    }
                                    p.closeInventory();
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
        p.sendMessage(Messages.getPrefix() + Messages.getBuyItem());
        p.sendMessage(Messages.getPrefix() + Messages.getTakenAmountMessage().replaceAll("<Amount>", "" + t));


        if (Core.isAboveEight() && Config.useTitles()) {

            p.closeInventory();

            Core.getTitleManager().setTimes(p, 20, 60, 20);
            Core.getTitleManager().sendTitle(p, Messages.getBuyItem());
            Core.getTitleManager().sendSubTitle(p, Messages.getTakenAmountMessage().replaceAll("<Amount>", "" + t));

            p.closeInventory();

        }
        OpenShop.openShopItems(null, p, shop, 1);
        cart.remove(k);
    }
}
