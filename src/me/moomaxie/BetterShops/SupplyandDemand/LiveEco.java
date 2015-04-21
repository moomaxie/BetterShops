package me.moomaxie.BetterShops.SupplyandDemand;

import me.moomaxie.BetterShops.Configurations.AnvilGUI;
import me.moomaxie.BetterShops.Configurations.Config;
import me.moomaxie.BetterShops.Configurations.GUIMessages.ItemTexts;
import me.moomaxie.BetterShops.Configurations.GUIMessages.LiveEconomy;
import me.moomaxie.BetterShops.Configurations.GUIMessages.MainGUI;
import me.moomaxie.BetterShops.Configurations.GUIMessages.SearchEngine;
import me.moomaxie.BetterShops.Configurations.Messages;
import me.moomaxie.BetterShops.Configurations.ShopManager;
import me.moomaxie.BetterShops.Core;
import me.moomaxie.BetterShops.Listeners.Misc.ChatMessages;
import me.moomaxie.BetterShops.Shops.Shop;
import me.moomaxie.BetterShops.Shops.ShopItem;
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
import java.util.HashMap;
import java.util.Map;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2015. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class LiveEco implements Listener {

    public static void openLiveEcoInventory(Shop shop, Player p, ShopItem item, ItemStack ite) {
        Inventory inv = Bukkit.createInventory(p, 54, MainGUI.getString("ShopHeader") + shop.getName());

        ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 14);
        ItemMeta m = glass.getItemMeta();
        m.setDisplayName(" ");
        if (item.getLiveEco()) {
            glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 5);
        }
        glass.setItemMeta(m);
        for (int i = 0; i < 18; i++) {
            inv.setItem(i, glass);
        }

        ItemStack enable = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 14);
        ItemMeta enableMeta = enable.getItemMeta();
        if (item.getLiveEco()) {
            enableMeta.setDisplayName(LiveEconomy.getString("LiveEcoOn"));
            enable = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 5);
        } else {
            enableMeta.setDisplayName(LiveEconomy.getString("LiveEcoOff"));
        }
        enableMeta.setLore(Arrays.asList(LiveEconomy.getString("LiveEcoLore")));
        enable.setItemMeta(enableMeta);

        ItemStack doubleAmt = new ItemStack(Material.STAINED_CLAY, 1, (byte) 10);
        ItemMeta doubleAmtMeta = doubleAmt.getItemMeta();
        doubleAmtMeta.setDisplayName(LiveEconomy.getString("VariableAmount").replaceAll("<Value>", "" + item.getAmountToDouble()));
        doubleAmtMeta.setLore(Arrays.asList(LiveEconomy.getString("VariableLore"),
                LiveEconomy.getString("AffectPrice"),
                LiveEconomy.getString("LowNumber"),
                LiveEconomy.getString("HighNumber")));
        doubleAmt.setItemMeta(doubleAmtMeta);

        ItemStack percent = new ItemStack(Material.DIAMOND);
        ItemMeta percentMeta = percent.getItemMeta();
        percentMeta.setDisplayName(LiveEconomy.getString("PriceChange").replaceAll("<Value>", item.getPriceChangePercent() + "%"));
        percent.setItemMeta(percentMeta);

        ItemStack info = new ItemStack(Material.SIGN);
        ItemMeta infoMeta = info.getItemMeta();
        infoMeta.setDisplayName(LiveEconomy.getString("Information"));
        infoMeta.setLore(Arrays.asList(LiveEconomy.getString("Info1")));
        info.setItemMeta(infoMeta);

        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName(MainGUI.getString("BackArrow"));
        back.setItemMeta(backMeta);

        inv.setItem(0, back);

        {
            ItemStack infor = new ItemStack(Material.MAP);
            ItemMeta inforMeta = infor.getItemMeta();
            inforMeta.setDisplayName("§e§l1.");
            inforMeta.setLore(Arrays.asList(LiveEconomy.getString("Info2")));
            infor.setItemMeta(inforMeta);
            inv.setItem(33, infor);
        }

        {
            ItemStack infor = new ItemStack(Material.MAP);
            ItemMeta inforMeta = infor.getItemMeta();
            inforMeta.setDisplayName("§e§l2.");
            inforMeta.setLore(Arrays.asList(LiveEconomy.getString("Info3")));
            infor.setItemMeta(inforMeta);
            inv.setItem(34, infor);
        }

        {
            ItemStack infor = new ItemStack(Material.MAP);
            ItemMeta inforMeta = infor.getItemMeta();
            inforMeta.setDisplayName("§e§l3.");
            inforMeta.setLore(Arrays.asList(LiveEconomy.getString("Info4")));
            infor.setItemMeta(inforMeta);
            inv.setItem(35, infor);
        }

        {
            ItemStack infor = new ItemStack(Material.MAP);
            ItemMeta inforMeta = infor.getItemMeta();
            inforMeta.setDisplayName("§e§l4.");
            inforMeta.setLore(Arrays.asList(LiveEconomy.getString("Info5")));
            infor.setItemMeta(inforMeta);
            inv.setItem(42, infor);
        }

        {
            ItemStack infor = new ItemStack(Material.MAP);
            ItemMeta inforMeta = infor.getItemMeta();
            inforMeta.setDisplayName("§e§l5.");
            inforMeta.setLore(Arrays.asList(LiveEconomy.getString("Info6")));
            infor.setItemMeta(inforMeta);
            inv.setItem(43, infor);
        }

        {
            ItemStack infor = new ItemStack(Material.MAP);
            ItemMeta inforMeta = infor.getItemMeta();
            inforMeta.setDisplayName("§e§l6.");
            inforMeta.setLore(Arrays.asList(LiveEconomy.getString("Info7"), "§8Not yet implemented"));
            infor.setItemMeta(inforMeta);
            inv.setItem(44, infor);
        }

        inv.setItem(27, enable);
        inv.setItem(25, info);
        inv.setItem(36, doubleAmt);
        inv.setItem(45, percent);
        inv.setItem(4, ite);

        p.openInventory(inv);

    }

    @EventHandler
    public void onLiveEco(final InventoryClickEvent e) {
        final Player p = (Player) e.getWhoClicked();
        if (e.getInventory().getName().contains(MainGUI.getString("ShopHeader"))) {
            e.setCancelled(true);
            if (e.getInventory().getType() == InventoryType.CHEST) {
                if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {

                    String name = e.getInventory().getName();
                    name = name.substring(MainGUI.getString("ShopHeader").length());

                    final Shop shop = ShopManager.fromString(p, name);

                    if (shop != null) {
                        if (shop.getOwner().getUniqueId().equals(p.getUniqueId())) {
                            if (e.getCurrentItem().getItemMeta().getLore() != null && e.getCurrentItem().getItemMeta().getLore().contains(ItemTexts.getString("LiveEcoLore"))) {
                                openLiveEcoInventory(shop, p, ShopItem.fromItemStack(shop, e.getInventory().getItem(4), false), e.getInventory().getItem(4));
                            }
                            if (e.getCurrentItem().getItemMeta().getLore() != null && e.getCurrentItem().getItemMeta().getLore().contains(LiveEconomy.getString("LiveEcoLore"))) {
                                ShopItem item = ShopItem.fromItemStack(shop, e.getInventory().getItem(4), false);

                                item.setLiveEco(!item.getLiveEco());

                                if (item.getSister() == null) {
                                    ShopItem sis = shop.createShopItem(item.getItem(), shop.getNextSlotForPage(shop.getNextAvailablePage(!item.isSelling()), true), shop.getNextAvailablePage(!item.isSelling()), true);
                                    sis.setPrice(item.getPrice() / 2);
                                    sis.setLiveEco(item.getLiveEco());
                                    item.getSister().setAdjustedPrice(item.getPrice() / 2);
                                } else {
                                    item.getSister().setPrice(item.getPrice() / 2);
                                    item.getSister().setLiveEco(item.getLiveEco());
                                    item.getSister().setAdjustedPrice(item.getPrice() / 2);
                                }

                                openLiveEcoInventory(shop, p, item, e.getInventory().getItem(4));
                            }
                            if (e.getCurrentItem().getItemMeta().getLore() != null && e.getCurrentItem().getItemMeta().getLore().contains(LiveEconomy.getString("VariableLore"))) {
                                final ShopItem item = ShopItem.fromItemStack(shop, e.getInventory().getItem(4), false);

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
                                                                LiveEco.openLiveEcoInventory(shop, p, item, e.getInventory().getItem(4));
                                                                can = false;
                                                            }

                                                            if (can) {

                                                                item.setAmountToDouble(amt);
                                                            }
                                                            LiveEco.openLiveEcoInventory(shop, p, item, e.getInventory().getItem(4));
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
                                    meta.setDisplayName(SearchEngine.getString("NewAmount"));
                                    it.setItemMeta(meta);

                                    gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, it);

                                    gui.open();
                                } else {
                                    p.closeInventory();
                                    Map<Shop, ShopItem> map = new HashMap<>();
                                    map.put(shop, item);

                                    ChatMessages.setDoubleAmount.put(p, map);
                                    p.sendMessage(Messages.getString("Prefix") + Messages.getString("ChatMessage"));
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
