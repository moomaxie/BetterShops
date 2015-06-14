package max.hubbard.bettershops.Menus.ShopMenus;

import max.hubbard.bettershops.Configurations.Language;
import max.hubbard.bettershops.Core;
import max.hubbard.bettershops.Menus.MenuType;
import max.hubbard.bettershops.Menus.ShopMenu;
import max.hubbard.bettershops.Shops.Items.Actions.ClickableItem;
import max.hubbard.bettershops.Shops.Items.Actions.LeftClickAction;
import max.hubbard.bettershops.Shops.Items.Actions.ShopItemStack;
import max.hubbard.bettershops.Shops.Shop;
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
public class TradeConfirm implements ShopMenu {

    Shop shop;
    Inventory inv;

    public TradeConfirm(Shop shop) {
        this.shop = shop;
        inv = Bukkit.createInventory(null, 54, Language.getString("MainGUI", "ShopHeader") + shop.getName());

    }

    @Override
    public MenuType getType() {
        return MenuType.TRADE_CONFIRM;
    }

    @Override
    public Shop getAttachedShop() {
        return shop;
    }

    @Override
    public void draw(final Player p, final int page, final Object... obj) {
        inv.clear();

        final Trade trade = (Trade) obj[0];

        max.hubbard.bettershops.TradeManager.ps.put(p.getUniqueId(), trade);

        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, shop.getFrameColor());
        ItemMeta m = item.getItemMeta();
        m.setDisplayName(" ");
        item.setItemMeta(m);
        for (int i = 0; i < 9; i++) {
            inv.setItem(i, item);
        }
        inv.setItem(13, item);
        inv.setItem(22, item);
        inv.setItem(31, item);
        for (int i = 45; i < 54; i++) {
            inv.setItem(i, item);
        }
        inv.setItem(40, item);


        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName(Language.getString("MainGUI", "BackArrow"));
        back.setItemMeta(backMeta);
        ClickableItem backClick = new ClickableItem(new ShopItemStack(back), inv, p);
        backClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                shop.getMenu(MenuType.TRADE_CHOOSE).draw(p, page, obj);
            }
        });

        ItemStack yourMoney = new ItemStack(Material.EMERALD, trade.getTradeGold());
        ItemMeta yourMoneyMeta = yourMoney.getItemMeta();
        yourMoneyMeta.setDisplayName(Language.getString("Trades", "YourMoney").replaceAll("<Amount>", trade.getTradeGold() + ""));
        yourMoney.setItemMeta(yourMoneyMeta);

        ItemStack theirMoney = new ItemStack(Material.EMERALD, trade.getReceivingGold());
        ItemMeta theirMoneyMeta = theirMoney.getItemMeta();
        theirMoneyMeta.setDisplayName(Language.getString("Trades", "TheirMoney").replaceAll("<Amount>", trade.getReceivingGold() + ""));
        theirMoney.setItemMeta(theirMoneyMeta);

        ItemStack info = new ItemStack(Material.EMERALD_BLOCK);
        ItemMeta infoMeta = info.getItemMeta();
        infoMeta.setDisplayName(Language.getString("Trades", "Trades"));
        infoMeta.setLore(Arrays.asList(Language.getString("Trades", "TradeId") + trade.getId()));
        info.setItemMeta(infoMeta);

        ItemStack confirm = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 5);
        ItemMeta confirmMeta = confirm.getItemMeta();
        confirmMeta.setDisplayName(Language.getString("Trades", "Confirm"));
        confirm.setItemMeta(confirmMeta);
        ClickableItem conClick = new ClickableItem(new ShopItemStack(confirm), inv, p);
        conClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {

                boolean can = true;
                boolean c = true;

                for (ItemStack it : trade.getRecievingItems()) {
                    int amt = it.getAmount();
                    it.setAmount(1);
                    if (Stocks.getNumberInInventory(it, p) >= amt) {
                        c = true;
                    } else {
                        c = false;
                        break;
                    }
                    it.setAmount(amt);
                }

                if (Core.getEconomy().getBalance(p) < trade.getReceivingGold()) {
                    can = false;
                }
                if (!trade.isTraded()) {

                    if (can && c) {

                        if (Core.getEconomy().getBalance(shop.getOwner()) >= trade.getTradeGold()) {

                            for (ItemStack it : trade.getRecievingItems()) {
                                int amt = it.getAmount();
                                it.setAmount(1);
                                Stocks.removeItemsFromInventory(it, p, amt);
                            }

                            for (ItemStack it : trade.getTradeItems()) {
                                int amt = it.getAmount();
                                it.setAmount(1);
                                Stocks.addItemsToInventory(it, p, amt);
                                p.updateInventory();
                            }

                            Core.getEconomy().withdrawPlayer(shop.getOwner(), trade.getTradeGold());
                            Core.getEconomy().depositPlayer(p, trade.getTradeGold());

                            Core.getEconomy().depositPlayer(shop.getOwner(), trade.getReceivingGold());
                            Core.getEconomy().withdrawPlayer(p, trade.getReceivingGold());

                            max.hubbard.bettershops.TradeManager.setTraded(p,trade, true);
                            shop.getMenu(MenuType.TRADE_CHOOSE).draw(p, page);
                            p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "Trade"));
                        } else {
                            p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "OwnerMoney"));
                        }
                    } else {
                        if (can)
                            p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "NotEnough"));
                        else
                            p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "NoMoney"));
                    }
                } else {
                    p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "AlreadyTraded"));
                }
            }
        });


        int i = 9;
        for (final ItemStack it : trade.getTradeItems()) {

            inv.setItem(i, it);

            if (i == 12) {
                i = 18;
            } else if (i == 21) {
                i = 27;
            } else if (i == 30) {
                i = 36;
            } else {
                i++;
            }

        }

        i = 14;
        for (final ItemStack it : trade.getRecievingItems()) {

            inv.setItem(i, it);

            if (i == 12) {
                i = 23;
            } else if (i == 26) {
                i = 32;
            } else if (i == 35) {
                i = 41;
            } else {
                i++;
            }

        }

        inv.setItem(0, back);
        inv.setItem(2, yourMoney);
        inv.setItem(4, info);
        inv.setItem(6, theirMoney);
        inv.setItem(8, confirm);


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
