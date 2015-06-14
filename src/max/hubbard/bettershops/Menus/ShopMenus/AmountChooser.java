package max.hubbard.bettershops.Menus.ShopMenus;

import max.hubbard.bettershops.Configurations.Language;
import max.hubbard.bettershops.Menus.MenuType;
import max.hubbard.bettershops.Menus.ShopMenu;
import max.hubbard.bettershops.Shops.Items.Actions.ClickableItem;
import max.hubbard.bettershops.Shops.Items.Actions.LeftClickAction;
import max.hubbard.bettershops.Shops.Items.Actions.ShopItemStack;
import max.hubbard.bettershops.Shops.Items.ShopItem;
import max.hubbard.bettershops.Shops.Shop;
import max.hubbard.bettershops.Utils.AnvilManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2015. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class AmountChooser implements ShopMenu {

    Shop shop;
    Inventory inv;

    public AmountChooser(Shop shop) {
        this.shop = shop;
        inv = Bukkit.createInventory(null, 54, Language.getString("MainGUI", "ShopHeader") + shop.getName());

    }

    @Override
    public MenuType getType() {
        return MenuType.AMOUNT_CHOOSER;
    }

    @Override
    public Shop getAttachedShop() {
        return shop;
    }

    @Override
    public void draw(final Player p, final int page, final Object... obj) {
        inv.clear();

        ItemStack ite = new ItemStack(Material.STAINED_GLASS_PANE, 1, shop.getFrameColor());
        ItemMeta m = ite.getItemMeta();
        m.setDisplayName(" ");
        ite.setItemMeta(m);
        for (int i = 0; i < 18; i++) {
            inv.setItem(i, ite);
        }

        final ShopItem it = (ShopItem) obj[0];
        final ItemStack item = it.getItem();
        final int total = (int) obj[1];

        ItemStack choose = new ItemStack(Material.PAPER);
        ItemMeta chooseMeta = choose.getItemMeta();
        chooseMeta.setDisplayName(Language.getString("Checkout", "SelectAmount"));
        choose.setItemMeta(chooseMeta);
        ClickableItem chooseClick = new ClickableItem(new ShopItemStack(choose), inv, p);
        chooseClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {

                final AnvilManager man = new AnvilManager(p);
                Bukkit.getScheduler().runTaskAsynchronously(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                    @Override
                    public void run() {
                        String name = man.call();

                        boolean can;
                        int amt = 0;
                        try {
                            amt = Integer.parseInt(name);
                            can = true;
                        } catch (Exception ex) {
                            p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "InvalidNumber"));
                            can = false;
                        }

                        if (can) {
                            try {
                                if (it.isInfinite() || it.getStock() >= amt) {
                                    draw(p, page, it, amt);
                                } else {
                                    draw(p, page, it, it.getStock());
                                }
                            } catch (Exception e1){
                                e1.printStackTrace();
                                draw(p,page,obj);
                            }
                        }
                    }
                });
            }
        });

        ItemStack totals = new ItemStack(Material.EMERALD);
        ItemMeta totalsMeta = totals.getItemMeta();
        totalsMeta.setDisplayName(Language.getString("Checkout", "AmountToBuy") + " §7" + total);
        totals.setItemMeta(totalsMeta);
        totals.setAmount(total);

        ItemStack confirm = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 4);
        ItemMeta confirmMeta = confirm.getItemMeta();
        confirmMeta.setDisplayName(Language.getString("Checkout", "Confirm"));
        confirm.setItemMeta(confirmMeta);
        ClickableItem conClick = new ClickableItem(new ShopItemStack(confirm), inv, p);
        conClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                Cart.addToCart(p, shop, it, total);
                shop.getMenu(MenuType.MAIN_BUYING).draw(p, page);
            }
        });

        ItemStack cancel = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 14);
        ItemMeta cancelMeta = cancel.getItemMeta();
        cancelMeta.setDisplayName(Language.getString("Checkout", "Cancel"));
        cancel.setItemMeta(cancelMeta);
        ClickableItem backClick = new ClickableItem(new ShopItemStack(cancel), inv, p);
        backClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                shop.getMenu(MenuType.MAIN_BUYING).draw(p, page);
            }
        });
        ItemStack addOne = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 5);
        ItemMeta addOneMeta = addOne.getItemMeta();
        addOneMeta.setDisplayName(Language.getString("Checkout", "AddOne"));
        addOne.setItemMeta(addOneMeta);
        ClickableItem addClick = new ClickableItem(new ShopItemStack(addOne), inv, p);
        addClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                if (it.isInfinite() || it.getStock() >= total + 1) {
                    draw(p, page, it, total + 1);
                } else {
                    draw(p, page, it, it.getStock());
                }
            }
        });

        ItemStack addSF = new ItemStack(Material.STAINED_GLASS_PANE, item.getMaxStackSize(), (byte) 5);
        ItemMeta addSFMeta = addSF.getItemMeta();
        addSFMeta.setDisplayName(Language.getString("Checkout", "AddStack"));
        addSF.setItemMeta(addSFMeta);
        ClickableItem addSFClick = new ClickableItem(new ShopItemStack(addSF), inv, p);
        addSFClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                if (it.isInfinite() || it.getStock() >= total + item.getMaxStackSize()) {
                    draw(p, page, it, total + item.getMaxStackSize());
                } else {
                    draw(p, page, it, it.getStock());
                }
            }
        });

        ItemStack removeOne = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 14);
        ItemMeta removeOneMeta = removeOne.getItemMeta();
        removeOneMeta.setDisplayName(Language.getString("Checkout", "RemoveOne"));
        removeOne.setItemMeta(removeOneMeta);
        ClickableItem remClick = new ClickableItem(new ShopItemStack(removeOne), inv, p);
        remClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                if (total - 1 >= 0) {
                    draw(p, page, it, total - 1);
                } else {
                    draw(p, page, it, 0);
                }
            }
        });

        ItemStack removeSF = new ItemStack(Material.STAINED_GLASS_PANE, item.getMaxStackSize(), (byte) 14);
        ItemMeta removeSFMeta = removeSF.getItemMeta();
        removeSFMeta.setDisplayName(Language.getString("Checkout", "RemoveStack"));
        removeSF.setItemMeta(removeSFMeta);
        ClickableItem remSFClick = new ClickableItem(new ShopItemStack(removeSF), inv, p);
        remSFClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                if (total - item.getMaxStackSize() >= 0) {
                    draw(p, page, it, total - item.getMaxStackSize());
                } else {
                    draw(p, page, it, 0);
                }
            }
        });
        inv.setItem(0, cancel);
        inv.setItem(4, item);
        inv.setItem(22, totals);
        inv.setItem(37, addOne);
        inv.setItem(38, addSF);
        inv.setItem(42, removeOne);
        inv.setItem(43, removeSF);
        inv.setItem(40, choose);

        inv.setItem(52, confirm);
        inv.setItem(53, confirm);

       new BukkitRunnable() {

            @Override
            public void run() {
                p.openInventory(inv);
            }

        }.runTask(Bukkit.getPluginManager().getPlugin("BetterShops"));
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }
}
