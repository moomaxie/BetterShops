package max.hubbard.bettershops.Menus.ShopMenus;

import max.hubbard.bettershops.Configurations.Language;
import max.hubbard.bettershops.Menus.MenuType;
import max.hubbard.bettershops.Menus.ShopMenu;
import max.hubbard.bettershops.Shops.Items.Actions.*;
import max.hubbard.bettershops.Shops.Shop;
import max.hubbard.bettershops.Utils.AnvilManager;
import max.hubbard.bettershops.Utils.MaterialSearch;
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
public class TradeManager implements ShopMenu {

    Shop shop;
    Inventory inv;

    public TradeManager(Shop shop) {
        this.shop = shop;
        inv = Bukkit.createInventory(null, 54, Language.getString("MainGUI", "ShopHeader") + shop.getName());

    }

    @Override
    public MenuType getType() {
        return MenuType.TRADE_MANAGER;
    }

    @Override
    public Shop getAttachedShop() {
        return shop;
    }

    @Override
    public void draw(final Player p, final int page, final Object... obj) {
        inv.clear();

        final Trade trade = (Trade) obj[0];
        final Trade orig = (Trade) obj[1];

        max.hubbard.bettershops.TradeManager.ps.put(p.getUniqueId(),trade);

        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, shop.getFrameColor());
        ItemMeta m = item.getItemMeta();
        m.setDisplayName(" ");
        item.setItemMeta(m);
        for (int i = 0; i < 9; i++) {
            inv.setItem(i, item);
        }
        inv.setItem(13, item);
        inv.setItem(31, item);
        inv.setItem(40, item);
        inv.setItem(48, item);
        inv.setItem(49, item);
        inv.setItem(50, item);

        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName(Language.getString("MainGUI", "BackArrow"));
        back.setItemMeta(backMeta);
        ClickableItem backClick = new ClickableItem(new ShopItemStack(back), inv, p);
        backClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                shop.getMenu(MenuType.TRADING).draw(p, page, obj);
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

        ItemStack add10U = new ItemStack(Material.GOLD_NUGGET);
        ItemMeta add10UMeta = add10U.getItemMeta();
        add10UMeta.setDisplayName(Language.getString("Trades", "AddAmount").replaceAll("<Amount>", 10 + ""));
        add10UMeta.setLore(Arrays.asList(Language.getString("Trades", "AddLore"),
                Language.getString("Trades", "RemoveLore"),
                Language.getString("Trades", "ClearLore")));
        add10U.setItemMeta(add10UMeta);
        ClickableItem Uadd10 = new ClickableItem(new ShopItemStack(add10U), inv, p);
        Uadd10.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                draw(p, page, new Trade(trade.getId(),
                        trade.getShop(), trade.getTradeItems(), trade.getTradeGold() + 10,
                        trade.getRecievingItems(), trade.getReceivingGold(),false), orig);
            }
        });
        Uadd10.addRightClickAction(new RightClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                int amt = trade.getTradeGold() - 10;
                if (amt < 0) {
                    amt = 0;
                }

                draw(p, page, new Trade(trade.getId(),
                        trade.getShop(), trade.getTradeItems(), amt,
                        trade.getRecievingItems(), trade.getReceivingGold(),false), orig);
            }
        });
        Uadd10.addShiftClickAction(new ShiftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                draw(p, page, new Trade(trade.getId(),
                        trade.getShop(), trade.getTradeItems(), 0,
                        trade.getRecievingItems(), trade.getReceivingGold(),false), orig);
            }
        });

        ItemStack add100UU = new ItemStack(Material.GOLD_INGOT);
        ItemMeta add100UUMeta = add100UU.getItemMeta();
        add100UUMeta.setDisplayName(Language.getString("Trades", "AddAmount").replaceAll("<Amount>", 100 + ""));
        add100UUMeta.setLore(Arrays.asList(Language.getString("Trades", "AddLore"),
                Language.getString("Trades", "RemoveLore"),
                Language.getString("Trades", "ClearLore")));
        add100UU.setItemMeta(add100UUMeta);
        ClickableItem Uadd100U = new ClickableItem(new ShopItemStack(add100UU), inv, p);
        Uadd100U.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                draw(p, page, new Trade(trade.getId(),
                        trade.getShop(), trade.getTradeItems(), trade.getTradeGold() + 100,
                        trade.getRecievingItems(), trade.getReceivingGold(),false), orig);
            }
        });
        Uadd100U.addRightClickAction(new RightClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                int amt = trade.getTradeGold() - 100;
                if (amt < 0) {
                    amt = 0;
                }

                draw(p, page, new Trade(trade.getId(),
                        trade.getShop(), trade.getTradeItems(), amt,
                        trade.getRecievingItems(), trade.getReceivingGold(),false), orig);
            }
        });
        Uadd100U.addShiftClickAction(new ShiftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                draw(p, page, new Trade(trade.getId(),
                        trade.getShop(), trade.getTradeItems(), 0,
                        trade.getRecievingItems(), trade.getReceivingGold(),false), orig);
            }
        });

        ItemStack add1000UU = new ItemStack(Material.GOLD_BLOCK);
        ItemMeta add1000UUMeta = add1000UU.getItemMeta();
        add1000UUMeta.setDisplayName(Language.getString("Trades", "AddAmount").replaceAll("<Amount>", 1000 + ""));
        add1000UUMeta.setLore(Arrays.asList(Language.getString("Trades", "AddLore"),
                Language.getString("Trades", "RemoveLore"),
                Language.getString("Trades", "ClearLore")));
        add1000UU.setItemMeta(add1000UUMeta);
        ClickableItem Uadd1000U = new ClickableItem(new ShopItemStack(add1000UU), inv, p);
        Uadd1000U.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                draw(p, page, new Trade(trade.getId(),
                        trade.getShop(), trade.getTradeItems(), trade.getTradeGold() + 1000,
                        trade.getRecievingItems(), trade.getReceivingGold(),false), orig);
            }
        });
        Uadd1000U.addRightClickAction(new RightClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                int amt = trade.getTradeGold() - 1000;
                if (amt < 0) {
                    amt = 0;
                }

                draw(p, page, new Trade(trade.getId(),
                        trade.getShop(), trade.getTradeItems(), amt,
                        trade.getRecievingItems(), trade.getReceivingGold(),false), orig);
            }
        });
        Uadd1000U.addShiftClickAction(new ShiftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                draw(p, page, new Trade(trade.getId(),
                        trade.getShop(), trade.getTradeItems(), 0,
                        trade.getRecievingItems(), trade.getReceivingGold(),false), orig);
            }
        });

        ItemStack add10T = new ItemStack(Material.GOLD_NUGGET);
        ItemMeta add10TMeta = add10T.getItemMeta();
        add10TMeta.setDisplayName(Language.getString("Trades", "RecAmount").replaceAll("<Amount>", 10 + ""));
        add10TMeta.setLore(Arrays.asList(Language.getString("Trades", "AddLore"),
                Language.getString("Trades", "RemoveLore"),
                Language.getString("Trades", "ClearLore")));
        add10T.setItemMeta(add10TMeta);
        ClickableItem Uadd10T = new ClickableItem(new ShopItemStack(add10T), inv, p);
        Uadd10T.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                draw(p, page, new Trade(trade.getId(),
                        trade.getShop(), trade.getTradeItems(), trade.getTradeGold(),
                        trade.getRecievingItems(), trade.getReceivingGold() + 10,false), orig);
            }
        });
        Uadd10T.addRightClickAction(new RightClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                int amt = trade.getReceivingGold() - 10;
                if (amt < 0) {
                    amt = 0;
                }

                draw(p, page, new Trade(trade.getId(),
                        trade.getShop(), trade.getTradeItems(), trade.getTradeGold(),
                        trade.getRecievingItems(), amt,false), orig);
            }
        });
        Uadd10T.addShiftClickAction(new ShiftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                draw(p, page, new Trade(trade.getId(),
                        trade.getShop(), trade.getTradeItems(), trade.getTradeGold(),
                        trade.getRecievingItems(), 0,false), orig);
            }
        });

        ItemStack add100TU = new ItemStack(Material.GOLD_INGOT);
        ItemMeta add100TUMeta = add100TU.getItemMeta();
        add100TUMeta.setDisplayName(Language.getString("Trades", "RecAmount").replaceAll("<Amount>", 100 + ""));
        add100TUMeta.setLore(Arrays.asList(Language.getString("Trades", "AddLore"),
                Language.getString("Trades", "RemoveLore"),
                Language.getString("Trades", "ClearLore")));
        add100TU.setItemMeta(add100TUMeta);
        ClickableItem Uadd100T = new ClickableItem(new ShopItemStack(add100TU), inv, p);
        Uadd100T.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                draw(p, page, new Trade(trade.getId(),
                        trade.getShop(), trade.getTradeItems(), trade.getTradeGold(),
                        trade.getRecievingItems(), trade.getReceivingGold() + 100,false), orig);
            }
        });
        Uadd100T.addRightClickAction(new RightClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                int amt = trade.getReceivingGold() - 100;
                if (amt < 0) {
                    amt = 0;
                }

                draw(p, page, new Trade(trade.getId(),
                        trade.getShop(), trade.getTradeItems(), trade.getTradeGold(),
                        trade.getRecievingItems(), amt,false), orig);
            }
        });
        Uadd100T.addShiftClickAction(new ShiftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                draw(p, page, new Trade(trade.getId(),
                        trade.getShop(), trade.getTradeItems(), trade.getTradeGold(),
                        trade.getRecievingItems(), 0,false), orig);
            }
        });

        ItemStack add1000TU = new ItemStack(Material.GOLD_BLOCK);
        ItemMeta add1000TUMeta = add1000TU.getItemMeta();
        add1000TUMeta.setDisplayName(Language.getString("Trades", "RecAmount").replaceAll("<Amount>", 1000 + ""));
        add1000TUMeta.setLore(Arrays.asList(Language.getString("Trades", "AddLore"),
                Language.getString("Trades", "RemoveLore"),
                Language.getString("Trades", "ClearLore")));
        add1000TU.setItemMeta(add1000TUMeta);
        ClickableItem Uadd1000T = new ClickableItem(new ShopItemStack(add1000TU), inv, p);
        Uadd1000T.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                draw(p, page, new Trade(trade.getId(),
                        trade.getShop(), trade.getTradeItems(), trade.getTradeGold(),
                        trade.getRecievingItems(), trade.getReceivingGold() + 1000,false), orig);
            }
        });
        Uadd1000T.addRightClickAction(new RightClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                int amt = trade.getReceivingGold() - 1000;
                if (amt < 0) {
                    amt = 0;
                }

                draw(p, page, new Trade(trade.getId(),
                        trade.getShop(), trade.getTradeItems(), trade.getTradeGold(),
                        trade.getRecievingItems(), amt,false), orig);
            }
        });
        Uadd1000T.addShiftClickAction(new ShiftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                draw(p, page, new Trade(trade.getId(),
                        trade.getShop(), trade.getTradeItems(), trade.getTradeGold(),
                        trade.getRecievingItems(), 0,false), orig);
            }
        });

        ItemStack addItem = new ItemStack(Material.NAME_TAG);
        ItemMeta addItemMeta = addItem.getItemMeta();
        addItemMeta.setDisplayName(Language.getString("Trades", "AddRecItem"));
        addItem.setItemMeta(addItemMeta);
        ClickableItem addClick = new ClickableItem(new ShopItemStack(addItem), inv, p);
        addClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                final AnvilManager man = new AnvilManager(p);
                Bukkit.getScheduler().runTaskAsynchronously(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                    @Override
                    public void run() {
                        String na = man.call();
                        List<Material> mat = MaterialSearch.closestMaterial(na);

                        List<ItemStack> items = trade.getRecievingItems();

                        ItemStack t = null;

                        for (ItemStack it : items) {
                            if (it.getType() == mat.get(0)) {
                                t = it.clone();
                                t.setAmount(t.getAmount() + 1);
                                items.remove(it);
                                break;
                            }
                        }

                        if (t == null) {
                            items.add(new ItemStack(mat.get(0)));
                        } else {
                            items.add(t);
                        }

                        draw(p, page, new Trade(trade.getId(),
                                trade.getShop(), trade.getTradeItems(), trade.getTradeGold(),
                                items, trade.getReceivingGold(),false), orig);

                    }
                });
            }
        });

        ItemStack info = new ItemStack(Material.EMERALD_BLOCK);
        ItemMeta infoMeta = info.getItemMeta();
        infoMeta.setDisplayName(Language.getString("Trades", "Trades"));
        infoMeta.setLore(Arrays.asList(Language.getString("Trades", "TradeId") + orig.getId()));
        info.setItemMeta(infoMeta);

        ItemStack confirm = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 5);
        ItemMeta confirmMeta = confirm.getItemMeta();
        confirmMeta.setDisplayName(Language.getString("Trades", "Confirm"));
        confirm.setItemMeta(confirmMeta);
        ClickableItem conClick = new ClickableItem(new ShopItemStack(confirm), inv, p);
        conClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                max.hubbard.bettershops.TradeManager.removeTrade(orig, shop);
                max.hubbard.bettershops.TradeManager.addTrade(p,trade, shop);
                shop.getMenu(MenuType.TRADING).draw(p, page);
                shop.setObject("Removal", "");
            }
        });

        ItemStack delete = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 14);
        ItemMeta deleteMeta = delete.getItemMeta();
        deleteMeta.setDisplayName(Language.getString("Trades", "Delete"));
        delete.setItemMeta(deleteMeta);
        ClickableItem delClick = new ClickableItem(new ShopItemStack(delete), inv, p);
        delClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                max.hubbard.bettershops.TradeManager.deleteTrade(trade, shop);
                shop.getMenu(MenuType.TRADING).draw(p, page);
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

            ClickableItem clickableItem = new ClickableItem(new ShopItemStack(it), inv, p);
            clickableItem.addLeftClickAction(new LeftClickAction() {
                @Override
                public void onAction(InventoryClickEvent e) {
                    List<ItemStack> items = trade.getTradeItems();
                    items.remove(it);
                    draw(p, page, new Trade(trade.getId(),
                            trade.getShop(), items, trade.getTradeGold(),
                            trade.getRecievingItems(), trade.getReceivingGold(),false), orig);
                }
            });
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

            ClickableItem clickableItem = new ClickableItem(new ShopItemStack(it), inv, p);
            clickableItem.addLeftClickAction(new LeftClickAction() {
                @Override
                public void onAction(InventoryClickEvent e) {
                    List<ItemStack> items = trade.getRecievingItems();
                    items.remove(it);
                    draw(p, page, new Trade(trade.getId(),
                            trade.getShop(), trade.getTradeItems(), trade.getTradeGold(),
                            items, trade.getReceivingGold(),false), orig);
                }
            });
        }

        inv.setItem(0, back);
        inv.setItem(2, yourMoney);
        inv.setItem(4, info);
        inv.setItem(6, theirMoney);
        inv.setItem(8, confirm);
        inv.setItem(22, addItem);
        inv.setItem(45, add10U);
        inv.setItem(46, add100UU);
        inv.setItem(47, add1000UU);

        inv.setItem(53, add10T);
        inv.setItem(52, add100TU);
        inv.setItem(51, add1000TU);

        if (max.hubbard.bettershops.TradeManager.getTrade(trade.getId()) != null) {
            inv.setItem(49, delete);
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
