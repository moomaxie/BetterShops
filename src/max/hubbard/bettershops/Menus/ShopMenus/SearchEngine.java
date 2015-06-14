package max.hubbard.bettershops.Menus.ShopMenus;

import max.hubbard.bettershops.Configurations.Language;
import max.hubbard.bettershops.Menus.MenuType;
import max.hubbard.bettershops.Menus.ShopMenu;
import max.hubbard.bettershops.Shops.Items.Actions.ClickableItem;
import max.hubbard.bettershops.Shops.Items.Actions.LeftClickAction;
import max.hubbard.bettershops.Shops.Items.Actions.ShopItemStack;
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
public class SearchEngine implements ShopMenu {

    Shop shop;
    Inventory inv;

    public SearchEngine(Shop shop) {
        this.shop = shop;
        inv = Bukkit.createInventory(null, 54, Language.getString("MainGUI", "ShopHeader") + shop.getName());

    }


    @Override
    public MenuType getType() {
        return MenuType.SEARCH_ENGINE;
    }

    @Override
    public Shop getAttachedShop() {
        return shop;
    }

    @Override
    public void draw(final Player p, final int page, final Object... obj) {
        inv.clear();

        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, shop.getFrameColor());
        ItemMeta m = item.getItemMeta();
        m.setDisplayName(" ");
        item.setItemMeta(m);

        for (int i = 0; i < 18; i++) {
            inv.setItem(i, item);
        }

        final boolean sel = (boolean) obj[0];

        ItemStack sell = new ItemStack(Material.SIGN);
        ItemMeta sellMeta = sell.getItemMeta();
        if (!sel) {
            sellMeta.setDisplayName(Language.getString("SearchEngine", "SearchBuyItems"));
        } else {
            sellMeta.setDisplayName(Language.getString("SearchEngine", "SearchSellItems"));
        }
        sell.setItemMeta(sellMeta);

        ItemStack nam = new ItemStack(Material.WOOL, 1, (byte) 5);
        ItemMeta namMeta = nam.getItemMeta();
        namMeta.setDisplayName(Language.getString("SearchEngine", "SearchMaterials"));
        namMeta.setLore(Arrays.asList(Language.getString("SearchEngine", "SearchMaterialsLore")));
        nam.setItemMeta(namMeta);
        ClickableItem namClick = new ClickableItem(new ShopItemStack(nam), inv, p);
        namClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {

                final AnvilManager man = new AnvilManager(p);
                Bukkit.getScheduler().runTaskAsynchronously(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                    @Override
                    public void run() {
                        String c = man.call();

                        if (sel) {
                            shop.getMenu(MenuType.MAIN_SELLING).draw(p, page, null, null, c, null);
                        } else {
                            shop.getMenu(MenuType.MAIN_BUYING).draw(p, page, null, null, c, null);
                        }
                    }
                });
            }
        });

        ItemStack desc = new ItemStack(Material.WOOL, 1, (byte) 14);
        ItemMeta descMeta = desc.getItemMeta();
        descMeta.setDisplayName(Language.getString("SearchEngine", "SearchName"));
        descMeta.setLore(Arrays.asList(Language.getString("SearchEngine", "SearchNameLore")));
        desc.setItemMeta(descMeta);
        ClickableItem nameClick = new ClickableItem(new ShopItemStack(desc), inv, p);
        nameClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {

                final AnvilManager man = new AnvilManager(p);
                Bukkit.getScheduler().runTaskAsynchronously(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                    @Override
                    public void run() {
                        String c = man.call();

                        if (sel) {
                            shop.getMenu(MenuType.MAIN_SELLING).draw(p, page, null, c, null, null);
                        } else {
                            shop.getMenu(MenuType.MAIN_BUYING).draw(p, page, null, c, null, null);
                        }
                    }
                });
            }
        });

        ItemStack stock = new ItemStack(Material.WOOL, 1, (byte) 4);
        ItemMeta stockMeta = stock.getItemMeta();
        stockMeta.setDisplayName(Language.getString("SearchEngine", "SearchId"));
        stockMeta.setLore(Arrays.asList(Language.getString("SearchEngine", "SearchIdLore")));
        stock.setItemMeta(stockMeta);
        ClickableItem idClick = new ClickableItem(new ShopItemStack(stock), inv, p);
        idClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {

                final AnvilManager man = new AnvilManager(p);
                Bukkit.getScheduler().runTaskAsynchronously(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                    @Override
                    public void run() {
                        String c = man.call();
                        int amt = 0;
                        try {
                            amt = Integer.parseInt(c);
                        } catch (Exception ignored) {

                        }
                        if (sel) {
                            shop.getMenu(MenuType.MAIN_SELLING).draw(p, page, null, null, null, amt);
                        } else {
                            shop.getMenu(MenuType.MAIN_BUYING).draw(p, page, null, null, null, amt);
                        }
                    }
                });
            }
        });

        ItemStack amount = new ItemStack(Material.WOOL, 1, (byte) 2);
        ItemMeta amountMeta = amount.getItemMeta();
        amountMeta.setDisplayName(Language.getString("SearchEngine", "SearchPrice"));
        amountMeta.setLore(Arrays.asList(Language.getString("SearchEngine", "SearchPriceLore")));
        amount.setItemMeta(amountMeta);
        ClickableItem priceClick = new ClickableItem(new ShopItemStack(amount), inv, p);
        priceClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {

                final AnvilManager man = new AnvilManager(p);
                Bukkit.getScheduler().runTaskAsynchronously(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                    @Override
                    public void run() {
                        String c = man.call();
                        double amt = 0;
                        try {
                            amt = Double.parseDouble(c);
                        } catch (Exception ignored) {

                        }
                        if (sel) {
                            shop.getMenu(MenuType.MAIN_SELLING).draw(p, page, amt, null, null, null);
                        } else {
                            shop.getMenu(MenuType.MAIN_BUYING).draw(p, page, amt, null, null, null);
                        }
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
                if (sel) {
                    shop.getMenu(MenuType.MAIN_SELLING).draw(p, page);
                } else {
                    shop.getMenu(MenuType.MAIN_BUYING).draw(p, page);
                }
            }
        });

        inv.setItem(0, back);

        inv.setItem(4, sell);

        inv.setItem(19, nam);
        inv.setItem(21, desc);
        inv.setItem(23, stock);
        inv.setItem(25, amount);

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
