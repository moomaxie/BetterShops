package max.hubbard.bettershops.Menus.ShopMenus;

import com.google.common.collect.Lists;
import max.hubbard.bettershops.Configurations.Language;
import max.hubbard.bettershops.Menus.MenuType;
import max.hubbard.bettershops.Menus.ShopMenu;
import max.hubbard.bettershops.Shops.FileShop;
import max.hubbard.bettershops.Shops.Items.Actions.ClickableItem;
import max.hubbard.bettershops.Shops.Items.Actions.LeftClickAction;
import max.hubbard.bettershops.Shops.Items.Actions.ShopItemStack;
import max.hubbard.bettershops.Shops.Shop;
import max.hubbard.bettershops.Utils.Transaction;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.LinkedList;
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
public class History implements ShopMenu {

    Shop shop;
    Inventory inv;

    public History(Shop shop) {
        this.shop = shop;
        inv = Bukkit.createInventory(null, 54, Language.getString("MainGUI" , "ShopHeader") + shop.getName());

    }

    @Override
    public MenuType getType() {
        return MenuType.HISTORY;
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

        ItemStack info = new ItemStack(Material.ENDER_CHEST);
        ItemMeta infoMeta = info.getItemMeta();
        infoMeta.setDisplayName(Language.getString("History" , "History"));
        infoMeta.setLore(Arrays.asList(Language.getString("MainGUI" , "Page") + " §7" + page));
        info.setItemMeta(infoMeta);

        ItemStack clear = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 14);
        ItemMeta clearMeta = clear.getItemMeta();
        clearMeta.setDisplayName(Language.getString("History" , "ClearHistory"));
        clearMeta.setLore(Arrays.asList(Language.getString("History" , "ClearHistoryLore")));
        clear.setItemMeta(clearMeta);
        ClickableItem clearClick = new ClickableItem(new ShopItemStack(clear), inv, p);
        clearClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                shop.getHistory().clearHistory();
                draw(p, page, obj);
            }
        });

        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName(Language.getString("MainGUI" , "BackArrow"));
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
        arrowMeta.setDisplayName(Language.getString("MainGUI" , "NextPage"));
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
        barrowMeta.setDisplayName(Language.getString("MainGUI" , "PreviousPage"));
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
        if (shop instanceof FileShop)
            inv.setItem(7, clear);

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

        int k = shop.getHistory().getAllTransactions().size();

        if (page != maxPage) {
            k = k - (j + (shop.getHistory().getAllTransactions().size() - 36));
        }

        LinkedList<Transaction> list = shop.getHistory().getAllTransactions();

        List<Transaction> l = Lists.reverse(list);

        if (list.size() > 0) {
            for (int i = j; i < k; i++) {
                Transaction trans = l.get(i);
                ItemStack it = new ItemStack(Material.EMERALD);
                ItemMeta sk = it.getItemMeta();
                String s = "Buying";
                if (trans.isSell()) {
                    it = new ItemStack(Material.EMERALD_BLOCK);
                    s = "Selling";
                }

                sk.setDisplayName("§a" + trans.getPlayer().getName());

                sk.setLore(Arrays.asList(Language.getString("History" , "Date") + " §8" + trans.getDate().toLocaleString(),
                        Language.getString("History" , "Item") + " §8" + trans.getItem(),
                        Language.getString("History" , "Price") + " §8" + trans.getPrice(),
                        Language.getString("History" , "Amount") + " §8" + trans.getAmount(),
                        Language.getString("History" , "Shop") + " §8" + s));

                it.setItemMeta(sk);

                if (inv.firstEmpty() > 0)
                    inv.setItem(inv.firstEmpty(), it);
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
