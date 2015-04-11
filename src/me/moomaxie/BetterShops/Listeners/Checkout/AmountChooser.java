package me.moomaxie.BetterShops.Listeners.Checkout;

import me.moomaxie.BetterShops.Configurations.AnvilGUI;
import me.moomaxie.BetterShops.Configurations.Config;
import me.moomaxie.BetterShops.Configurations.GUIMessages.Checkout;
import me.moomaxie.BetterShops.Configurations.GUIMessages.MainGUI;
import me.moomaxie.BetterShops.Configurations.Messages;
import me.moomaxie.BetterShops.Configurations.ShopLimits;
import me.moomaxie.BetterShops.Core;
import me.moomaxie.BetterShops.Listeners.BuyerOptions.OpenShop;
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

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2014. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class AmountChooser implements Listener {

    public static void openAmountChooser(int total, ItemStack item, Player p, Shop shop, Inventory inv) {

        boolean already = false;
        if (inv == null) {
            inv = Bukkit.createInventory(p, 54, MainGUI.getString("ShopHeader") + shop.getName());
            already = true;
        } else {
            inv.clear();
        }

        ItemStack ite = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7);
        ItemMeta m = ite.getItemMeta();
        m.setDisplayName(" ");
        ite.setItemMeta(m);
        for (int i = 0; i < 18; i++) {
            inv.setItem(i, ite);
        }

        ItemStack choose = new ItemStack(Material.PAPER);
        ItemMeta chooseMeta = choose.getItemMeta();
        chooseMeta.setDisplayName(Checkout.getString("SelectAmount"));
        choose.setItemMeta(chooseMeta);

        ItemStack totals = new ItemStack(Material.EMERALD);
        ItemMeta totalsMeta = totals.getItemMeta();
        totalsMeta.setDisplayName(Checkout.getString("AmountToBuy") + " §7" + total);
        totals.setItemMeta(totalsMeta);
        totals.setAmount(total);

        ItemStack confirm = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 4);
        ItemMeta confirmMeta = confirm.getItemMeta();
        confirmMeta.setDisplayName(Checkout.getString("Confirm"));
        confirm.setItemMeta(confirmMeta);

        ItemStack cancel = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 14);
        ItemMeta cancelMeta = cancel.getItemMeta();
        cancelMeta.setDisplayName(Checkout.getString("Cancel"));
        cancel.setItemMeta(cancelMeta);

        ItemStack addOne = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 5);
        ItemMeta addOneMeta = addOne.getItemMeta();
        addOneMeta.setDisplayName(Checkout.getString("AddOne"));
        addOne.setItemMeta(addOneMeta);

        ItemStack addSF = new ItemStack(Material.STAINED_GLASS_PANE, item.getMaxStackSize(), (byte) 5);
        ItemMeta addSFMeta = addSF.getItemMeta();
        addSFMeta.setDisplayName(Checkout.getString("AddStack"));
        addSF.setItemMeta(addSFMeta);

        ItemStack removeOne = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 14);
        ItemMeta removeOneMeta = removeOne.getItemMeta();
        removeOneMeta.setDisplayName(Checkout.getString("RemoveOne"));
        removeOne.setItemMeta(removeOneMeta);

        ItemStack removeSF = new ItemStack(Material.STAINED_GLASS_PANE, item.getMaxStackSize(), (byte) 14);
        ItemMeta removeSFMeta = removeSF.getItemMeta();
        removeSFMeta.setDisplayName(Checkout.getString("RemoveStack"));
        removeSF.setItemMeta(removeSFMeta);

        inv.setItem(0, cancel);
        inv.setItem(4, item);
        inv.setItem(22, totals);
        inv.setItem(37, addOne);
        inv.setItem(38, addSF);
        inv.setItem(42, removeOne);
        inv.setItem(43, removeSF);
//        inv.setItem(40, choose);

        inv.setItem(52, confirm);
        inv.setItem(53, confirm);

        if (already)
            p.openInventory(inv);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onSelect(final InventoryClickEvent e) {
        final Player p = (Player) e.getWhoClicked();
        if (e.getInventory().getName().contains(MainGUI.getString("ShopHeader"))) {
            e.setCancelled(true);
            if (e.getInventory().getType() == InventoryType.CHEST) {
                if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {

                    String name = e.getInventory().getName();
                    name = name.substring(11);

                    final Shop shop = ShopLimits.fromString(p, name);

                    if (e.getInventory().getItem(22) != null) {

                        if (e.getInventory().getItem(22).getItemMeta().getDisplayName() != null && e.getInventory().getItem(22).getItemMeta().getDisplayName().contains(Checkout.getString("AmountToBuy"))) {

                            final int total = Integer.parseInt(e.getInventory().getItem(22).getItemMeta().getDisplayName().substring(Checkout.getString("AmountToBuy").length() + 3));

                            if (e.getCurrentItem().getItemMeta().getDisplayName() != null) {

                                final ShopItem shopItem = ShopItem.fromItemStack(shop, e.getInventory().getItem(4), false);

                                if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Checkout.getString("Cancel"))) {
                                    if (!shop.isOpen()) {
                                        if (shop.getOwner() == p) {
                                            OpenShop.openShopItems(e.getInventory(), p, shop, 1);
                                        } else {
                                            p.sendMessage(Messages.getString("Prefix") + "§cShop is closed.");
                                        }
                                    } else {
                                        OpenShop.openShopItems(e.getInventory(), p, shop, 1);
                                    }
                                }

                                if (e.getCurrentItem().getItemMeta().getLore() != null) {
                                    if (e.getCurrentItem().getItemMeta().getLore().contains(MainGUI.getString("AddToCart")) && e.isShiftClick()) {
                                        AmountChooser.openAmountChooser(1, e.getCurrentItem(), p, shop, e.getInventory());
                                    }
                                } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Checkout.getString("Confirm"))) {

                                    CheckoutMenu.addToCart(p, shop, ShopItem.fromItemStack(shop, e.getInventory().getItem(4), false), total);

                                    p.sendMessage(Messages.getString("Prefix") + "§eAdded Item to §aCart");

                                    OpenShop.openShopItems(e.getInventory(), p, shop, 1);

                                } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Checkout.getString("AddOne"))) {
                                    if (shopItem.getStock() >= total + 1 || shopItem.isInfinite()) {
                                        openAmountChooser(total + 1, e.getInventory().getItem(4), p, shop, e.getInventory());
                                    }
                                } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Checkout.getString("AddStack"))) {
                                    if (shopItem.getStock() >= total + e.getInventory().getItem(4).getMaxStackSize() || shopItem.isInfinite()) {
                                        openAmountChooser(total + e.getInventory().getItem(4).getMaxStackSize(), e.getInventory().getItem(4), p, shop, e.getInventory());
                                    } else {
                                        openAmountChooser(shopItem.getStock(), e.getInventory().getItem(4), p, shop, e.getInventory());
                                    }
                                } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Checkout.getString("RemoveOne"))) {
                                    if (1 <= total - 1) {
                                        openAmountChooser(total - 1, e.getInventory().getItem(4), p, shop, e.getInventory());
                                    } else {
                                        openAmountChooser(1, e.getInventory().getItem(4), p, shop, e.getInventory());
                                    }
                                } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Checkout.getString("RemoveStack"))) {
                                    if (1 <= total - e.getInventory().getItem(4).getMaxStackSize()) {
                                        openAmountChooser(total - e.getInventory().getItem(4).getMaxStackSize(), e.getInventory().getItem(4), p, shop, e.getInventory());
                                    } else {
                                        openAmountChooser(1, e.getInventory().getItem(4), p, shop, e.getInventory());
                                    }
                                } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Checkout.getString("SelectAmount"))) {

                                    if (Core.getAnvilGUI() != null && Config.useAnvil()) {
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
                                                                            openAmountChooser(amt, e.getInventory().getItem(4), p, shop, null);

                                                                        } catch (Exception ex) {
                                                                            if (name.equalsIgnoreCase("all")) {
                                                                                openAmountChooser(shopItem.getStock(), e.getInventory().getItem(4), p, shop, null);

                                                                            } else {
                                                                                ((Player) e.getWhoClicked()).sendMessage(Messages.getString("Prefix") + Messages.getString("InvalidNumber"));
                                                                                openAmountChooser(total, e.getInventory().getItem(4), p, shop, null);

                                                                            }
                                                                        }


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
