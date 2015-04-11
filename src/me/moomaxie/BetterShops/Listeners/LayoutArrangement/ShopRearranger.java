package me.moomaxie.BetterShops.Listeners.LayoutArrangement;

import me.moomaxie.BetterShops.Configurations.Config;
import me.moomaxie.BetterShops.Configurations.GUIMessages.MainGUI;
import me.moomaxie.BetterShops.Configurations.ShopLimits;
import me.moomaxie.BetterShops.Listeners.BuyerOptions.OpenShop;
import me.moomaxie.BetterShops.Listeners.ManagerOptions.OwnerPages;
import me.moomaxie.BetterShops.Listeners.OpenShopOptions;
import me.moomaxie.BetterShops.Listeners.OwnerSellingOptions.OpenSellingOptions;
import me.moomaxie.BetterShops.Listeners.SellerOptions.OpenSellShop;
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
public class ShopRearranger implements Listener {

    public HashMap<UUID, Map<Shop, ItemStack>> arrange = new HashMap<>();

    public static void openShopArranger(Inventory inv, Player p, Shop shop, int page, boolean sell) {
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

        ItemStack info = new ItemStack(Material.SIGN);
        ItemMeta infoMeta = info.getItemMeta();
        if (sell) {
            infoMeta.setDisplayName(MainGUI.getString("ArrangeSelling"));
        } else {
            infoMeta.setDisplayName(MainGUI.getString("ArrangeBuying"));
        }
        info.setItemMeta(infoMeta);

        ItemStack options = new ItemStack(Material.ENDER_CHEST);
        ItemMeta optionsMeta = options.getItemMeta();

        optionsMeta.setDisplayName(MainGUI.getString("Arrangement"));

        optionsMeta.setLore(Arrays.asList(MainGUI.getString("ArrangementLore")));
        options.setItemMeta(optionsMeta);

        ItemStack arrow = new ItemStack(Material.ARROW);
        ItemMeta arrowMeta = arrow.getItemMeta();
        arrowMeta.setDisplayName(MainGUI.getString("NextPage"));
        arrow.setItemMeta(arrowMeta);

        ItemStack barrow = new ItemStack(Material.ARROW);
        ItemMeta barrowMeta = barrow.getItemMeta();
        barrowMeta.setDisplayName(MainGUI.getString("PreviousPage"));
        barrow.setItemMeta(barrowMeta);

        ItemStack pg1 = new ItemStack(Material.INK_SACK, 1, (byte) 10);
        ItemMeta pg1Meta = pg1.getItemMeta();
        pg1Meta.setDisplayName(MainGUI.getString("Page") + " §7" + page);
        pg1.setItemMeta(pg1Meta);


        inv.setItem(3, info);
        inv.setItem(5, options);

        inv.setItem(13, pg1);

        if (page > 1) {
            inv.setItem(0, barrow);
        }

        inv.setItem(8, arrow);

        for (ShopItem it : shop.getShopItems(sell)) {
            if (it.getPage() == page) {
                ItemStack itemStack = it.getItem().clone();
                ItemMeta meta = itemStack.getItemMeta();

                List<String> lore = new ArrayList<>();
                if (it.getLore() != null) {
                    for (String s : it.getLore()){
                        lore.add(s);
                    }
                }
                if (!lore.contains(MainGUI.getString("Arrange"))) {
                    lore.add(" ");
                    lore.add(MainGUI.getString("Arrange"));
                }
                meta.setLore(lore);
                itemStack.setItemMeta(meta);
                inv.setItem(it.getSlot(), itemStack);
            }
        }


        if (!same)
            p.openInventory(inv);

    }


    @EventHandler
    public void onArrange(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getInventory().getName().contains(MainGUI.getString("ShopHeader"))) {
            e.setCancelled(true);
            if (e.getInventory().getType() == InventoryType.CHEST) {
                if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {

                    String name = e.getInventory().getName();
                    name = name.substring(11);

                    Shop shop = ShopLimits.fromString(p, name);

                    if (shop.getOwner().getUniqueId().equals(p.getUniqueId())) {

                        //Enter Arrangement Mode
                        if (e.getCurrentItem().getItemMeta().getLore() != null && e.getCurrentItem().getItemMeta().getLore().contains(MainGUI.getString("ArrangementMode")) && e.isShiftClick()) {
                            ItemStack it = e.getInventory().getItem(3);
                            if (it != null && it.getItemMeta().getDisplayName() != null) {
                                String n = it.getItemMeta().getDisplayName();

                                int page = OwnerPages.getPage(e.getInventory());

                                if (n.contains(MainGUI.getString("Buying"))) {
                                    openShopArranger(e.getInventory(), p, shop, page, false);
                                } else {
                                    openShopArranger(e.getInventory(), p, shop, page, true);
                                }
                            }
                            //Leave Arrangement Mode
                        } else {
                            if (e.getCurrentItem().getItemMeta().getLore() != null && e.getCurrentItem().getItemMeta().getLore().contains(MainGUI.getString("ArrangementLore"))) {
                                ItemStack it = e.getInventory().getItem(3);
                                if (it != null && it.getItemMeta().getDisplayName() != null) {
                                    String n = it.getItemMeta().getDisplayName();
                                    int page = OwnerPages.getPage(e.getInventory());
                                    if (n.contains(MainGUI.getString("ArrangeBuying"))) {
                                        if (shop.isServerShop()) {
                                            OpenShop.openShopItems(null, p, shop, page);
                                        } else {
                                            OpenShopOptions.openShopOwnerOptionsInventory(null, p, shop, page);
                                        }
                                    } else if (Config.useSellingShop()) {
                                        if (shop.isServerShop()) {
                                            OpenSellShop.openSellerShop(null, p, shop, page);
                                        } else {
                                            OpenSellingOptions.openShopSellingOptions(null, p, shop, page);
                                        }
                                    } else {
                                        if (shop.isServerShop()) {
                                            OpenShop.openShopItems(null, p, shop, page);
                                        } else {
                                            OpenShopOptions.openShopOwnerOptionsInventory(null, p, shop, page);
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

    @EventHandler
    public void onUseArrange(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getInventory().getName().contains(MainGUI.getString("ShopHeader"))) {
            e.setCancelled(true);
            if (e.getInventory().getType() == InventoryType.CHEST) {


                String name = e.getInventory().getName();
                name = name.substring(11);

                Shop shop = ShopLimits.fromString(p, name);

                if (!arrange.containsKey(p.getUniqueId())) {
                    if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
                        if (e.getCurrentItem().getItemMeta().getLore() != null && e.getCurrentItem().getItemMeta().getLore().contains(MainGUI.getString("Arrange"))) {

                            Map<Shop, ItemStack> map = new HashMap<>();

                            map.put(shop, e.getCurrentItem());

                            arrange.put(p.getUniqueId(), map);

                            List<String> lore = e.getCurrentItem().getItemMeta().getLore();

                            lore.remove(MainGUI.getString("Arrange"));
                            lore.add(MainGUI.getString("Selected"));

                            ItemMeta meta = e.getCurrentItem().getItemMeta();
                            meta.setLore(lore);
                            e.getCurrentItem().setItemMeta(meta);
                        }
                    }
                } else {
                    if (e.getCurrentItem() != null) {
                        if (e.getInventory().getItem(e.getSlot()) != null) {

                            if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
                                if (e.getCurrentItem().getItemMeta().getLore() != null && e.getCurrentItem().getItemMeta().getLore().contains(MainGUI.getString("Arrange"))) {

                                    Map<Shop, ItemStack> map = arrange.get(p.getUniqueId());

                                    ItemStack ite = null;

                                    for (Shop s : map.keySet()) {
                                        if (s.getName().equals(shop.getName())) {
                                            ite = map.get(s);
                                            break;
                                        }
                                    }

                                    if (ite != null) {

                                        int page = OwnerPages.getPage(e.getInventory());

                                        ItemStack it = e.getInventory().getItem(3);
                                        if (it != null && it.getItemMeta().getDisplayName() != null) {
                                            String n = it.getItemMeta().getDisplayName();
                                            if (n.contains(MainGUI.getString("ArrangeBuying"))) {

                                                ShopItem shopItem1 = ShopItem.fromItemStack(shop,ite,false);
                                                ShopItem shopItem2 = ShopItem.fromItemStack(shop,e.getCurrentItem(),false);

                                                shop.exchangeItems(shopItem1, shopItem2);
//
                                                openShopArranger(e.getInventory(), p, shop, page, false);
                                            } else {
                                                ShopItem shopItem1 = ShopItem.fromItemStack(shop, ite, true);
                                                ShopItem shopItem2 = ShopItem.fromItemStack(shop, e.getCurrentItem(), true);

                                                shop.exchangeItems(shopItem1, shopItem2);
//
                                                openShopArranger(e.getInventory(), p, shop, page, true);
                                            }
                                            arrange.remove(p.getUniqueId());
                                        }
                                    }
                                }
                            }
                        } else {
                            Map<Shop, ItemStack> map = arrange.get(p.getUniqueId());

                            ItemStack ite = null;

                            for (Shop s : map.keySet()) {
                                if (s.getName().equals(shop.getName())) {
                                    ite = map.get(s);
                                    break;
                                }
                            }

                            if (ite != null) {
                                ItemStack it = e.getInventory().getItem(3);
                                if (it != null && it.getItemMeta().getDisplayName() != null) {
                                    String n = it.getItemMeta().getDisplayName();
                                    int page = OwnerPages.getPage(e.getInventory());
                                    if (n.contains(MainGUI.getString("ArrangeBuying"))) {
                                        ShopItem shopItem = ShopItem.fromItemStack(shop,ite,false);
                                        shop.changePlaces(shopItem,e.getSlot(),page);
//
                                        openShopArranger(e.getInventory(), p, shop, page, false);
                                    } else {
                                        ShopItem shopItem = ShopItem.fromItemStack(shop,ite,true);
                                        shop.changePlaces(shopItem,e.getSlot(),page);

                                        openShopArranger(e.getInventory(), p, shop, page, true);
                                    }

                                    arrange.remove(p.getUniqueId());
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
