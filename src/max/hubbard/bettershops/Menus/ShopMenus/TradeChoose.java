package max.hubbard.bettershops.Menus.ShopMenus;

import com.google.common.collect.Lists;
import max.hubbard.bettershops.Configurations.Language;
import max.hubbard.bettershops.Menus.MenuType;
import max.hubbard.bettershops.Menus.ShopMenu;
import max.hubbard.bettershops.Shops.Items.Actions.ClickableItem;
import max.hubbard.bettershops.Shops.Items.Actions.LeftClickAction;
import max.hubbard.bettershops.Shops.Items.Actions.ShopItemStack;
import max.hubbard.bettershops.Shops.Shop;
import max.hubbard.bettershops.Utils.Trade;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

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
public class TradeChoose implements ShopMenu {

    Shop shop;
    Inventory inv;

    public TradeChoose(Shop shop) {
        this.shop = shop;
        inv = Bukkit.createInventory(null, 54, Language.getString("MainGUI", "ShopHeader") + shop.getName());

    }

    @Override
    public MenuType getType() {
        return MenuType.TRADE_CHOOSE;
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
        infoMeta.setLore(Arrays.asList(Language.getString("Trades", "ViewLore")));
        info.setItemMeta(infoMeta);


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

        int k = max.hubbard.bettershops.TradeManager.getTrades(shop).size();

        if (page != maxPage) {
            k = k - (j + (max.hubbard.bettershops.TradeManager.getTrades(shop).size() - 36));
        }

        List<Trade> list = max.hubbard.bettershops.TradeManager.getTrades(shop);

        List<Trade> l = Lists.reverse(list);

        if (list.size() > 0) {
            for (int i = j; i < k; i++) {
                final Trade trans = l.get(i);
                ItemStack it = trans.getIcon();
                ItemMeta sk = it.getItemMeta();

                sk.setDisplayName(Language.getString("Trades", "TradeId") + trans.getId());

                it.setItemMeta(sk);

                if (!trans.isTraded()) {
                    if (inv.firstEmpty() > 0)
                        inv.setItem(inv.firstEmpty(), it);
                }

                ClickableItem tradeClick = new ClickableItem(new ShopItemStack(it), inv, p);
                if (!trans.isTraded()) {
                    tradeClick.addLeftClickAction(new LeftClickAction() {
                        @Override
                        public void onAction(InventoryClickEvent e) {
                            if (!trans.isTraded()) {
                                shop.getMenu(MenuType.TRADE_CONFIRM).draw(p, page, trans);
                            } else {
                                draw(p, page);
                            }
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
