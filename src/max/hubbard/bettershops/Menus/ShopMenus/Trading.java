package max.hubbard.bettershops.Menus.ShopMenus;

import com.google.common.collect.Lists;
import max.hubbard.bettershops.Configurations.Language;
import max.hubbard.bettershops.Listeners.CreateShop;
import max.hubbard.bettershops.Menus.MenuType;
import max.hubbard.bettershops.Menus.ShopMenu;
import max.hubbard.bettershops.Shops.Items.Actions.ClickableItem;
import max.hubbard.bettershops.Shops.Items.Actions.LeftClickAction;
import max.hubbard.bettershops.Shops.Items.Actions.ShopItemStack;
import max.hubbard.bettershops.Shops.Shop;
import max.hubbard.bettershops.TradeManager;
import max.hubbard.bettershops.Utils.AnvilManager;
import max.hubbard.bettershops.Utils.Stocks;
import max.hubbard.bettershops.Utils.Trade;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2015. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class Trading implements ShopMenu {

    Shop shop;
    Inventory inv;

    public Trading(Shop shop) {
        this.shop = shop;
        inv = Bukkit.createInventory(null, 54, Language.getString("MainGUI", "ShopHeader") + shop.getName());

    }

    @Override
    public MenuType getType() {
        return MenuType.TRADING;
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

        ItemStack info = new ItemStack(Material.NAME_TAG);
        ItemMeta infoMeta = info.getItemMeta();
        infoMeta.setDisplayName(Language.getString("Trades", "Trades"));
        infoMeta.setLore(Arrays.asList(Language.getString("Trades", "TradesLore")));
        info.setItemMeta(infoMeta);

        ItemStack add = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 5);
        ItemMeta addMeta = add.getItemMeta();
        addMeta.setDisplayName(Language.getString("Trades", "AddTrade"));
        addMeta.setLore(Arrays.asList(Language.getString("Trades", "AddLore")));
        add.setItemMeta(addMeta);
        ClickableItem addClick = new ClickableItem(new ShopItemStack(add), inv, p);
        addClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {

                final AnvilManager man = new AnvilManager(p);
                Bukkit.getScheduler().runTaskAsynchronously(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                    @Override
                    public void run() {
                        String na = man.call();

                        if (CreateShop.isAlphaNumeric(na)) {
                            if (TradeManager.getTrade(na) == null) {
                                Trade tr = new Trade(na, shop, new ArrayList<ItemStack>(), 0, new ArrayList<ItemStack>(), 0, false);
                                TradeManager.ps.put(p.getUniqueId(), tr);
                                shop.getMenu(MenuType.TRADE_MANAGER).draw(p, page, tr, tr);
                            } else {
                                p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "NameTaken"));
                                draw(p, page, obj);
                            }
                        } else {
                            p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "ImproperName"));
                            draw(p, page, obj);
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
                shop.getMenu(MenuType.OWNER_BUYING).draw(p, page, obj);
            }
        });

        ItemStack arrow = new ItemStack(Material.ARROW);
        ItemMeta arrowMeta = arrow.getItemMeta();
        arrowMeta.setDisplayName(Language.getString("MainGUI", "NextPage"));
        arrow.setItemMeta(arrowMeta);
        ClickableItem arrowClick = new ClickableItem(new ShopItemStack(arrow), inv, p);
        arrowClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                draw(p, page + 1, obj);
            }
        });

        ItemStack barrow = new ItemStack(Material.ARROW);
        ItemMeta barrowMeta = barrow.getItemMeta();
        barrowMeta.setDisplayName(Language.getString("MainGUI", "PreviousPage"));
        barrow.setItemMeta(barrowMeta);
        ClickableItem barrowClick = new ClickableItem(new ShopItemStack(barrow), inv, p);
        barrowClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                draw(p, page - 1, obj);
            }
        });

        if (page > 1) {
            inv.setItem(1, barrow);
        }

        inv.setItem(0, back);

        inv.setItem(4, info);
        inv.setItem(7, add);

        int maxPage = (int) Math.ceil((double) (shop.getHistory().getAllTransactions().size()) / 36);

        if (maxPage == 0) {
            maxPage = 1;
        }


        if (page != maxPage) {
            inv.setItem(8, arrow);
        }

        int j = 0;

        if (page > 1) {
            j = 36 * (page - 1);
        }

        int k = 0;

        if (TradeManager.getTrades(shop) != null){
            k = TradeManager.getTrades(shop).size();
        }

        if (page != maxPage) {
            k = k - (j + (TradeManager.getTrades(shop).size() - 36));
        }

        List<Trade> list = TradeManager.getTrades(shop);

        List<Trade> l = Lists.reverse(list);

        if (list.size() > 0) {
            for (int i = j; i < k; i++) {
                final Trade trans = l.get(i);
                ItemStack it = trans.getIcon();
                ItemMeta sk = it.getItemMeta();

                sk.setDisplayName(Language.getString("Trades", "TradeId") + trans.getId());

                if (trans.isTraded()) {
                    sk.setLore(Arrays.asList(Language.getString("Trades", "Collect")));
                }

                it.setItemMeta(sk);

                if (inv.firstEmpty() > 0)
                    inv.setItem(inv.firstEmpty(), it);

                ClickableItem tradeClick = new ClickableItem(new ShopItemStack(it), inv, p);
                if (!trans.isTraded()) {
                    tradeClick.addLeftClickAction(new LeftClickAction() {
                        @Override
                        public void onAction(InventoryClickEvent e) {
                            shop.getMenu(MenuType.TRADE_MANAGER).draw(p, page, trans, trans);
                        }
                    });
                } else {
                    tradeClick.addLeftClickAction(new LeftClickAction() {
                        @Override
                        public void onAction(InventoryClickEvent e) {
                            for (ItemStack it : trans.getRecievingItems()) {
                                int amt = it.getAmount();
                                it.setAmount(1);
                                Stocks.addItemsToInventory(it, p, amt);
                            }
                            TradeManager.removeTrade(trans, shop);
                            draw(p, page);

                        }
                    });
                }
            }
        }

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
