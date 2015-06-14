package max.hubbard.bettershops.Menus.ShopMenus;

import max.hubbard.bettershops.Configurations.Language;
import max.hubbard.bettershops.Menus.MenuType;
import max.hubbard.bettershops.Menus.ShopMenu;
import max.hubbard.bettershops.Shops.Items.Actions.ClickableItem;
import max.hubbard.bettershops.Shops.Items.Actions.LeftClickAction;
import max.hubbard.bettershops.Shops.Items.Actions.ShopItemStack;
import max.hubbard.bettershops.Shops.Items.ShopItem;
import max.hubbard.bettershops.Shops.Shop;
import max.hubbard.bettershops.Shops.Types.Holo.ShopHologram;
import max.hubbard.bettershops.Utils.AnvilManager;
import max.hubbard.bettershops.Utils.Stocks;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2015. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class KeeperItemManager implements ShopMenu {

    Shop shop;
    Inventory inv;

    public KeeperItemManager(Shop shop) {
        this.shop = shop;
        inv = Bukkit.createInventory(null, 54, Language.getString("MainGUI", "ShopHeader") + shop.getName());

    }


    @Override
    public MenuType getType() {
        return MenuType.KEEPER_ITEM_MANAGER;
    }

    @Override
    public Shop getAttachedShop() {
        return shop;
    }

    @Override
    public void draw(final Player p, final int page, final Object... obj) {
        inv.clear();

        final ShopItem it = (ShopItem) obj[0];

        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, shop.getFrameColor());
        ItemMeta m = item.getItemMeta();
        m.setDisplayName(" ");
        item.setItemMeta(m);
        for (int i = 0; i < 18; i++) {
            inv.setItem(i, item);
        }

        ItemStack stock = new ItemStack(Material.STAINED_CLAY, 1, (byte) 4);
        ItemMeta stockMeta = stock.getItemMeta();
        stockMeta.setDisplayName(Language.getString("ItemTexts", "AddStockDisplayName"));
        stockMeta.setLore(Arrays.asList(Language.getString("ItemTexts", "AddStockLore")));
        stock.setItemMeta(stockMeta);
        ClickableItem addClick = new ClickableItem(new ShopItemStack(stock), inv, p);
        addClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                final AnvilManager man = new AnvilManager(p);
                Bukkit.getScheduler().runTaskAsynchronously(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                    @Override
                    public void run() {
                        String name = man.call();
                        int amt;
                        try {
                            amt = Integer.parseInt(name);
                        } catch (Exception ex) {
                            if (name.equalsIgnoreCase("all")) {
                                Stocks.addAll(it, shop, p);
                                if (shop.isHoloShop()) {
                                    ShopHologram h = shop.getHolographicShop();
                                    h.updateItemLines(h.getItemLine(), false);
                                }
                                draw(p, page, obj);
                                return;
                            } else {
                                p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "InvalidNumber"));
                                draw(p, page, obj);
                                return;
                            }
                        }


                        Stocks.addStock(it, amt, p, shop);
                        draw(p, page, obj);
                    }
                });
            }
        });


        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName(Language.getString("MainGUI", "BackArrow"));
        back.setItemMeta(backMeta);
        ClickableItem backClick = new ClickableItem(new ShopItemStack(back), inv, p);
        backClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                shop.getMenu(MenuType.MAIN_BUYING).draw(p, page, obj);
            }
        });

        inv.setItem(0, back);

        inv.setItem(4, it.getItem());

        inv.setItem(inv.firstEmpty(), stock);


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
