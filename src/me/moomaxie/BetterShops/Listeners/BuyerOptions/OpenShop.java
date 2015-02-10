package me.moomaxie.BetterShops.Listeners.BuyerOptions;

import me.moomaxie.BetterShops.Configurations.GUIMessages.MainGUI;
import me.moomaxie.BetterShops.Shops.Shop;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ***********************************************************************
 * Copyright me.moomaxie (c) 2014. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of me.moomaxie. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class OpenShop implements Listener {

//    @EventHandler
//    public void onShop(PlayerInteractEvent e) {
//        Player p = e.getPlayer();
//
//        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
//            if (e.getClickedBlock().getState() instanceof Sign) {
//                Sign sign = (Sign) e.getClickedBlock().getState();
//                if (sign.getLine(0).contains("§b§k**********")) {
//                    if (sign.getLine(3).contains("§b§k**********")) {
//                        if (sign.getLine(1).contains("§aShop")) {
//                            if (sign.getLine(2).contains("§aOpen")) {
//
//                                Block face = e.getClickedBlock().getRelative(((org.bukkit.material.Sign) (sign.getData())).getAttachedFace());
//
//
//                                if (face.getType() == Material.CHEST) {
//                                    if (face.getState() instanceof Chest) {
//                                        Chest chest = (Chest) face.getState();
//
//                                        Shop shop = ShopLimits.fromLocation(chest.getLocation());
//
//                                        if (shop != null) {
//
//                                            if (shop.getOwner() != null) {
//                                                if (!shop.getOwner().getUniqueId().equals(p.getUniqueId()) || shop.isServerShop()) {
//                                                    if (shop.getShopContents(false).size() >= shop.getShopContents(true).size()) {
//                                                        openShopItems(null, p, shop, 1);
//                                                    } else {
//                                                        OpenSellShop.openSellerShop(null, p, shop, 1);
//                                                    }
//                                                    p.sendMessage(Messages.getPrefix() + Messages.getOpenShopMessage());
//                                                } else if (shop.getOwner().getUniqueId().equals(p.getUniqueId())) {
//                                                    if (shop.getShopContents(false).size() >= shop.getShopContents(true).size()) {
//                                                        OpenShopOptions.openShopOwnerOptionsInventory(null, p, shop, 1);
//                                                    } else {
//                                                        OpenSellingOptions.openShopSellingOptions(null, p, shop, 1);
//                                                    }
//                                                }
//                                            } else {
//                                                p.sendMessage(Messages.getPrefix() + "§cThis Shop has no Owner!");
//                                            }
//                                        }
//                                    }
//                                }
//                            } else if (sign.getLine(2).contains("§cClosed")) {
//                                Block face = e.getClickedBlock().getRelative(((org.bukkit.material.Sign) (sign.getData())).getAttachedFace());
//
//
//                                if (face.getType() == Material.CHEST) {
//                                    if (face.getState() instanceof Chest) {
//                                        Chest chest = (Chest) face.getState();
//
//                                        Shop shop = ShopLimits.fromLocation(chest.getLocation());
//
//                                        if (shop.getOwner() != p) {
//                                            p.sendMessage(Messages.getPrefix() + "Shop Is §cClosed");
//                                        } else {
//                                            if (!shop.isServerShop()) {
//                                                if (shop.getShopContents(false).size() >= shop.getShopContents(true).size()) {
//                                                    OpenShopOptions.openShopOwnerOptionsInventory(null, p, shop, 1);
//                                                } else {
//                                                    OpenSellingOptions.openShopSellingOptions(null, p, shop, 1);
//                                                }
//                                            } else {
//                                                if (shop.getShopContents(false).size() >= shop.getShopContents(true).size()) {
//                                                    openShopItems(null, p, shop, 1);
//                                                } else {
//                                                    OpenSellShop.openSellerShop(null, p, shop, 1);
//                                                }
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }

    public static void openShopItems(Inventory inv, Player p, Shop shop, int page) {

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

        ItemStack options = new ItemStack(Material.ENDER_CHEST);
        ItemMeta optionsMeta = options.getItemMeta();

        optionsMeta.setDisplayName("§a§l" + shop.getName());

        if (!shop.isServerShop()) {
            optionsMeta.setLore(Arrays.asList("§7" + shop.getDescription(), " ", MainGUI.getString("Owner")+ " §a§l" + shop.getOwner().getName(),
                    MainGUI.getString("Keepers") + " §7" + shop.getManagers().size()));
            options.setItemMeta(optionsMeta);
        } else {
            if (!shop.getOwner().getUniqueId().equals(p.getUniqueId()) || !shop.getOwner().getUniqueId().toString().equals(p.getUniqueId().toString())) {
                optionsMeta.setLore(Arrays.asList("§7" + shop.getDescription(), " ", MainGUI.getString("Owner") + " " + MainGUI.getString("Server"),
                        MainGUI.getString("Keepers") + " §7" + shop.getManagers().size()));
                options.setItemMeta(optionsMeta);
            } else {
                optionsMeta.setLore(Arrays.asList("§7" + shop.getDescription(), " ", MainGUI.getString("BuyingShop"), " ", MainGUI.getString("Owner") + " " + MainGUI.getString("Server"),
                        MainGUI.getString("Keepers") + " §7" + shop.getManagers().size(), " ", MainGUI.getString("OpenShopSettings"), " ", MainGUI.getString("TurnOffServerShop")));
                options.setItemMeta(optionsMeta);
            }
        }

        ItemStack info = new ItemStack(Material.SIGN);
        ItemMeta infoMeta = info.getItemMeta();
        infoMeta.setDisplayName(MainGUI.getString("Buying"));
        infoMeta.setLore(Arrays.asList(MainGUI.getString("ToggleShop"), MainGUI.getString("SearchOptions")));
        info.setItemMeta(infoMeta);

        ItemStack cart = new ItemStack(Material.CHEST);
        ItemMeta cartMeta = cart.getItemMeta();
        cartMeta.setDisplayName(MainGUI.getString("CheckoutDisplayName"));
        cartMeta.setLore(Arrays.asList(MainGUI.getString("CheckoutLore")));
        cart.setItemMeta(cartMeta);

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

        if (page == 1) {
            inv.setItem(8, arrow);
        }

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

            inv.setItem(0, barrow);
            inv.setItem(8, arrow);
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

            inv.setItem(0, barrow);
        }

        inv.setItem(3, info);
        inv.setItem(4, cart);
        inv.setItem(5, options);

        inv.setItem(12, pg1);
        inv.setItem(13, pg2);
        inv.setItem(14, pg3);

        for (ItemStack it : shop.getShopContents(false).keySet()) {
            int slot = shop.getShopContents(false).get(it);

            if (page == 1) {
                if (slot >= 18 && slot < 54) {
                    it.setAmount(1);
                    ItemMeta meta = it.getItemMeta();
                    List<String> lore;
                    if (shop.getLore(it) != null) {
                        lore = shop.getLore(it);
                    } else {
                        lore = new ArrayList<String>();
                    }

                    if (shop.isInfinite(it, false)) {
                        lore.add(MainGUI.getString("Stock") + " §7-");
                    } else {
                        lore.add(MainGUI.getString("Stock") + " §7" + shop.getStock(it, false));
                    }
                    lore.add(MainGUI.getString("Amount") + " §7" + shop.getAmount(it, false));
                    lore.add(MainGUI.getString("Price") + " §7" + shop.getPriceAsString(it, false));
                    lore.add(MainGUI.getString("LeftClickToBuy"));
                    lore.add(MainGUI.getString("AddToCart"));

                    if (shop.getManagers().contains(p)) {
                        lore.add(MainGUI.getString("ShopKeeperManage"));
                    }
                    meta.setLore(lore);
                    it.setItemMeta(meta);

                    inv.setItem(slot, it);
                }
            } else if (page == 2) {
                if (slot >= 72 && slot < 108) {
                    it.setAmount(1);
                    ItemMeta meta = it.getItemMeta();
                    List<String> lore;
                    if (shop.getLore(it) != null) {
                        lore = shop.getLore(it);
                    } else {
                        lore = new ArrayList<String>();
                    }

                    if (shop.isInfinite(it, false)) {
                        lore.add(MainGUI.getString("Stock") + " §7-");
                    } else {
                        lore.add(MainGUI.getString("Stock") + " §7" + shop.getStock(it, false));
                    }
                    lore.add(MainGUI.getString("Amount") + " §7" + shop.getAmount(it, false));
                    lore.add(MainGUI.getString("Price") + " §7" + shop.getPriceAsString(it, false));
                    lore.add(MainGUI.getString("LeftClickToBuy"));
                    lore.add(MainGUI.getString("AddToCart"));

                    if (shop.getManagers().contains(p)) {
                        lore.add(MainGUI.getString("ShopKeeperManage"));
                    }
                    meta.setLore(lore);
                    it.setItemMeta(meta);

                    slot = slot - 54;

                    inv.setItem(slot, it);
                }
            } else if (page == 3) {
                if (slot >= 126 && slot < 162) {
                    it.setAmount(1);
                    ItemMeta meta = it.getItemMeta();
                    List<String> lore;
                    if (shop.getLore(it) != null) {
                        lore = shop.getLore(it);
                    } else {
                        lore = new ArrayList<String>();
                    }

                    if (shop.isInfinite(it, false)) {
                        lore.add(MainGUI.getString("Stock") + " §7-");
                    } else {
                        lore.add(MainGUI.getString("Stock") + " §7" + shop.getStock(it, false));
                    }
                    lore.add(MainGUI.getString("Amount") + " §7" + shop.getAmount(it, false));
                    lore.add(MainGUI.getString("Price") + " §7" + shop.getPriceAsString(it, false));
                    lore.add(MainGUI.getString("LeftClickToBuy"));
                    lore.add(MainGUI.getString("AddToCart"));

                    if (shop.getManagers().contains(p)) {
                        lore.add(MainGUI.getString("ShopKeeperManage"));
                    }
                    meta.setLore(lore);
                    it.setItemMeta(meta);

                    slot = slot - 108;

                    inv.setItem(slot, it);
                }
            }
        }

        if (!same)
            p.openInventory(inv);
    }
}
