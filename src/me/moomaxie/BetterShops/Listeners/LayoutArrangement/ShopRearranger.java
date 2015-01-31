package me.moomaxie.BetterShops.Listeners.LayoutArrangement;

import me.moomaxie.BetterShops.Configurations.GUIMessages.MainGUI;
import me.moomaxie.BetterShops.Configurations.ShopLimits;
import me.moomaxie.BetterShops.Listeners.BuyerOptions.OpenShop;
import me.moomaxie.BetterShops.Listeners.OpenShopOptions;
import me.moomaxie.BetterShops.Shops.Shop;
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
 * Copyright moomaxie (c) 2014. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of moomaxie. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class ShopRearranger implements Listener {

    public HashMap<UUID, Map<Shop, ItemStack>> arrange = new HashMap<>();

    public void openShopArranger(Inventory inv, Player p, Shop shop, int page, boolean sell) {
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

        ItemStack info = new ItemStack(Material.SIGN);
        ItemMeta infoMeta = info.getItemMeta();
        if (sell) {
            infoMeta.setDisplayName(MainGUI.getString("Selling"));
        } else {
            infoMeta.setDisplayName(MainGUI.getString("Buying"));
        }
        info.setItemMeta(infoMeta);

        ItemStack options = new ItemStack(Material.ENDER_CHEST);
        ItemMeta optionsMeta = options.getItemMeta();

        optionsMeta.setDisplayName(MainGUI.getString("Arrangement"));

        optionsMeta.setLore(Arrays.asList(MainGUI.getString("ArrangementLore")));
        options.setItemMeta(optionsMeta);


        ItemStack pg1 = new ItemStack(Material.INK_SACK, 1, (byte) 10);
        ItemMeta pg1Meta = pg1.getItemMeta();
        pg1Meta.setDisplayName(MainGUI.getString("Page") + " 1");
        pg1.setItemMeta(pg1Meta);

        ItemStack pg2 = new ItemStack(Material.INK_SACK, 1, (byte) 8);
        ItemMeta pg2Meta = pg2.getItemMeta();
        pg2Meta.setDisplayName(MainGUI.getString("Page") + " 2");
        pg2.setItemMeta(pg2Meta);

        ItemStack pg3 = new ItemStack(Material.INK_SACK, 1, (byte) 8);
        ItemMeta pg3Meta = pg3.getItemMeta();
        pg3Meta.setDisplayName(MainGUI.getString("Page") + " 3");
        pg3.setItemMeta(pg3Meta);

        if (page == 2) {
            pg1 = new ItemStack(Material.INK_SACK, 1, (byte) 8);
            pg1Meta = pg1.getItemMeta();
            pg1Meta.setDisplayName(MainGUI.getString("Page") + " 1");
            pg1.setItemMeta(pg1Meta);

            pg2 = new ItemStack(Material.INK_SACK, 1, (byte) 10);
            pg2Meta = pg2.getItemMeta();
            pg2Meta.setDisplayName(MainGUI.getString("Page") + " 2");
            pg2.setItemMeta(pg2Meta);

            pg3 = new ItemStack(Material.INK_SACK, 1, (byte) 8);
            pg3Meta = pg3.getItemMeta();
            pg3Meta.setDisplayName(MainGUI.getString("Page") + " 3");
            pg3.setItemMeta(pg3Meta);

        }

        if (page == 3) {
            pg1 = new ItemStack(Material.INK_SACK, 1, (byte) 8);
            pg1Meta = pg1.getItemMeta();
            pg1Meta.setDisplayName(MainGUI.getString("Page") + " 1");
            pg1.setItemMeta(pg1Meta);

            pg2 = new ItemStack(Material.INK_SACK, 1, (byte) 8);
            pg2Meta = pg2.getItemMeta();
            pg2Meta.setDisplayName(MainGUI.getString("Page") + " 2");
            pg2.setItemMeta(pg2Meta);

            pg3 = new ItemStack(Material.INK_SACK, 1, (byte) 10);
            pg3Meta = pg3.getItemMeta();
            pg3Meta.setDisplayName(MainGUI.getString("Page") + " 3");
            pg3.setItemMeta(pg3Meta);

        }

        inv.setItem(3, info);
        inv.setItem(5, options);

//        inv.setItem(12, pg1);
//        inv.setItem(13, pg2);
//        inv.setItem(14, pg3);

        for (ItemStack it : shop.getShopContents(sell).keySet()) {
            int slot = shop.getShopContents(sell).get(it);

            if (page == 1) {
                if (slot >= 18 && slot < 54) {
                    it.setAmount(1);
                    ItemMeta meta = it.getItemMeta();
                    List<String> lore;
                    if (meta.getLore() != null) {
                        lore = meta.getLore();
                    } else {
                        lore = new ArrayList<String>();
                    }
                    if (!lore.contains(MainGUI.getString("Arrange"))) {
                        lore.add(" ");
                        lore.add(MainGUI.getString("Arrange"));
                        meta.setLore(lore);
                        it.setItemMeta(meta);
                    }
                    inv.setItem(slot, it);
                }
            } else if (page == 2) {
                if (slot >= 72 && slot < 108) {
                    it.setAmount(1);
                    ItemMeta meta = it.getItemMeta();
                    List<String> lore;
                    if (meta.getLore() != null) {
                        lore = meta.getLore();
                    } else {
                        lore = new ArrayList<String>();
                    }
                    if (!lore.contains(MainGUI.getString("Arrange"))) {
                        lore.add(" ");
                        lore.add(MainGUI.getString("Arrange"));
                        meta.setLore(lore);
                        it.setItemMeta(meta);
                    }
                    slot = slot - 54;

                    inv.setItem(slot, it);
                }
            } else if (page == 3) {
                if (slot >= 126 && slot < 162) {
                    it.setAmount(1);
                    ItemMeta meta = it.getItemMeta();
                    List<String> lore;
                    if (meta.getLore() != null) {
                        lore = meta.getLore();
                    } else {
                        lore = new ArrayList<String>();
                    }

                    if (!lore.contains(MainGUI.getString("Arrange"))) {

                        lore.add(" ");
                        lore.add(MainGUI.getString("Arrange"));

                        meta.setLore(lore);
                        it.setItemMeta(meta);
                    }

                    slot = slot - 108;

                    inv.setItem(slot, it);
                }
            }

            if (!same)
                p.openInventory(inv);
        }
    }


    @EventHandler
    public void onArrange(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getInventory().getName().contains("§7[Shop]")) {
            e.setCancelled(true);
            if (e.getInventory().getType() == InventoryType.CHEST) {
                if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {

                    String name = e.getInventory().getName();
                    name = name.substring(11);

                    Shop shop = ShopLimits.fromString(p, name);

                    if (shop.getOwner().getUniqueId().equals(p.getUniqueId())) {
                        if (e.getCurrentItem().getItemMeta().getLore() != null && e.getCurrentItem().getItemMeta().getLore().contains("§e§lShift Click §7to start §aArrangement Mode") && e.isShiftClick()) {
                            ItemStack it = e.getInventory().getItem(3);
                            if (it != null && it.getItemMeta().getDisplayName() != null) {
                                String n = it.getItemMeta().getDisplayName();

                                if (n.contains(MainGUI.getString("Buying"))) {
                                    if (e.getInventory().getItem(12) != null && e.getInventory().getItem(12).getData().getData() == (byte) 10) {
                                        openShopArranger(e.getInventory(), p, shop, 1, false);
                                    }
                                    if (e.getInventory().getItem(13) != null && e.getInventory().getItem(13).getData().getData() == (byte) 10) {
                                        openShopArranger(e.getInventory(), p, shop, 2, false);
                                    }
                                    if (e.getInventory().getItem(14) != null && e.getInventory().getItem(14).getData().getData() == (byte) 10) {
                                        openShopArranger(e.getInventory(), p, shop, 3, false);
                                    }
                                } else {
                                    if (e.getInventory().getItem(12) != null && e.getInventory().getItem(12).getData().getData() == (byte) 10) {
                                        openShopArranger(e.getInventory(), p, shop, 1, true);
                                    }
                                    if (e.getInventory().getItem(13) != null && e.getInventory().getItem(13).getData().getData() == (byte) 10) {
                                        openShopArranger(e.getInventory(), p, shop, 2, true);
                                    }
                                    if (e.getInventory().getItem(14) != null && e.getInventory().getItem(14).getData().getData() == (byte) 10) {
                                        openShopArranger(e.getInventory(), p, shop, 3, true);
                                    }
                                }
                            }
                        } else {
                            if (e.getCurrentItem().getItemMeta().getLore() != null && e.getCurrentItem().getItemMeta().getLore().contains("§e§lClick §7to leave §aArrangement Mode")) {
                                if (shop.isServerShop()){
                                    OpenShop.openShopItems(null,p,shop,1);
                                } else {
                                    OpenShopOptions.openShopOwnerOptionsInventory(null,p,shop,1);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    //TODO: slot numbers for different pages

    @EventHandler
    public void onUseArrange(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getInventory().getName().contains("§7[Shop]")) {
            e.setCancelled(true);
            if (e.getInventory().getType() == InventoryType.CHEST) {


                String name = e.getInventory().getName();
                name = name.substring(11);

                Shop shop = ShopLimits.fromString(p, name);

                if (!arrange.containsKey(p.getUniqueId())) {
                    if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
                        if (e.getCurrentItem().getItemMeta().getLore() != null && e.getCurrentItem().getItemMeta().getLore().contains("§e§lLeft Click §7to arrange")) {

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

                                        ItemStack it = e.getInventory().getItem(3);
                                        if (it != null && it.getItemMeta().getDisplayName() != null) {
                                            String n = it.getItemMeta().getDisplayName();
                                            if (n.contains(MainGUI.getString("Buying"))) {
                                                int slot = e.getSlot();
                                                int old = shop.getSlotForItem(ite, false);

                                                shop.exchangeItems(ite, old, e.getCurrentItem(), slot, false);
//                                                if (e.getInventory().getItem(12) != null && e.getInventory().getItem(12).getData().getData() == (byte) 10) {
//                                                    openShopArranger(e.getInventory(), p, shop, 1, false);
//                                                }
//                                                if (e.getInventory().getItem(13) != null && e.getInventory().getItem(13).getData().getData() == (byte) 10) {
//                                                    openShopArranger(e.getInventory(), p, shop, 2, false);
//                                                }
//                                                if (e.getInventory().getItem(14) != null && e.getInventory().getItem(14).getData().getData() == (byte) 10) {
//                                                    openShopArranger(e.getInventory(), p, shop, 3, false);
//                                                }
                                                openShopArranger(e.getInventory(), p, shop, 1, false);
                                            } else {
                                                int slot = e.getSlot();
                                                int old = shop.getSlotForItem(ite, true);
                                                shop.exchangeItems(ite, old, e.getCurrentItem(), slot, true);
//                                                if (e.getInventory().getItem(12) != null && e.getInventory().getItem(12).getData().getData() == (byte) 10) {
//                                                    openShopArranger(e.getInventory(), p, shop, 1, true);
//                                                }
//                                                if (e.getInventory().getItem(13) != null && e.getInventory().getItem(13).getData().getData() == (byte) 10) {
//                                                    openShopArranger(e.getInventory(), p, shop, 2, true);
//                                                }
//                                                if (e.getInventory().getItem(14) != null && e.getInventory().getItem(14).getData().getData() == (byte) 10) {
//                                                    openShopArranger(e.getInventory(), p, shop, 3, true);
//                                                }
                                                openShopArranger(e.getInventory(), p, shop, 1, true);
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

                                    if (n.contains(MainGUI.getString("Buying"))) {
                                        shop.changePlaces(ite, e.getSlot(), false);
//                                        if (e.getInventory().getItem(12) != null && e.getInventory().getItem(12).getData().getData() == (byte) 10) {
//                                            openShopArranger(e.getInventory(), p, shop, 1, false);
//                                        }
//                                        if (e.getInventory().getItem(13) != null && e.getInventory().getItem(13).getData().getData() == (byte) 10) {
//                                            openShopArranger(e.getInventory(), p, shop, 2, false);
//                                        }
//                                        if (e.getInventory().getItem(14) != null && e.getInventory().getItem(14).getData().getData() == (byte) 10) {
//                                            openShopArranger(e.getInventory(), p, shop, 3, false);
//                                        }
                                        openShopArranger(e.getInventory(), p, shop, 1, false);
                                    } else {
                                        shop.changePlaces(ite, e.getSlot(), true);
//                                        if (e.getInventory().getItem(12) != null && e.getInventory().getItem(12).getData().getData() == (byte) 10) {
//                                            openShopArranger(e.getInventory(), p, shop, 1, true);
//                                        }
//                                        if (e.getInventory().getItem(13) != null && e.getInventory().getItem(13).getData().getData() == (byte) 10) {
//                                            openShopArranger(e.getInventory(), p, shop, 2, true);
//                                        }
//                                        if (e.getInventory().getItem(14) != null && e.getInventory().getItem(14).getData().getData() == (byte) 10) {
//                                            openShopArranger(e.getInventory(), p, shop, 3, true);
//                                        }
                                        openShopArranger(e.getInventory(), p, shop, 1, true);
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
