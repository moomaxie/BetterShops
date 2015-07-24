package max.hubbard.bettershops.Menus.ShopMenus;

import max.hubbard.bettershops.Configurations.Config;
import max.hubbard.bettershops.Configurations.Language;
import max.hubbard.bettershops.Events.AmountChangeEvent;
import max.hubbard.bettershops.Events.PriceChangeEvent;
import max.hubbard.bettershops.Events.StockChangeEvent;
import max.hubbard.bettershops.Menus.MenuType;
import max.hubbard.bettershops.Menus.ShopMenu;
import max.hubbard.bettershops.Shops.Items.Actions.ClickableItem;
import max.hubbard.bettershops.Shops.Items.Actions.LeftClickAction;
import max.hubbard.bettershops.Shops.Items.Actions.ShopItemStack;
import max.hubbard.bettershops.Shops.Items.ShopItem;
import max.hubbard.bettershops.Shops.Shop;
import max.hubbard.bettershops.Shops.Types.Holo.DeleteHoloShop;
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

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2015. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class ItemManagerSelling implements ShopMenu {

    Shop shop;
    Inventory inv;

    public ItemManagerSelling(Shop shop) {
        this.shop = shop;
        inv = Bukkit.createInventory(null, 54, Language.getString("MainGUI", "ShopHeader") + shop.getName());

    }

    @Override
    public MenuType getType() {
        return MenuType.ITEM_MANAGER_SELLING;
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

        final ShopItem shopItem = (ShopItem) obj[0];

        ItemStack nam = new ItemStack(Material.STAINED_CLAY, 1, (byte) 4);
        ItemMeta namMeta = nam.getItemMeta();
        namMeta.setDisplayName(Language.getString("ItemTexts", "SellPriceDisplayName"));
        namMeta.setLore(Arrays.asList(Language.getString("ItemTexts", "PriceLore")));
        nam.setItemMeta(namMeta);
        ClickableItem namClick = new ClickableItem(new ShopItemStack(nam), inv, p);
        namClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                final AnvilManager man = new AnvilManager(p);
                Bukkit.getScheduler().runTaskAsynchronously(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                    @Override
                    public void run() {
                        String name = man.call();
                        boolean can;
                        double amt = 0.0;
                        try {
                            amt = Double.parseDouble(name);

                            can = true;
                        } catch (Exception ex) {
                            p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "InvalidNumber"));
                            can = false;
                        }

                        BigDecimal bd = new BigDecimal(amt);
                        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
                        amt = bd.doubleValue();

                        if (can) {
                            if (amt >= 0) {
                                if (amt <= Config.getMaxPrice()) {
                                    PriceChangeEvent e = new PriceChangeEvent(shopItem,shopItem.getPrice(),amt);
                                    Bukkit.getPluginManager().callEvent(e);
                                    shopItem.setPrice(amt);
                                    if (shop.isHoloShop()) {
                                        ShopHologram h = shop.getHolographicShop();
                                        h.updateItemLines(h.getItemLine(), false);
                                    }
                                    p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "ChangePrice"));
                                } else {
                                    if (String.valueOf(Config.getMaxPrice()).contains("E")) {
                                        p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "HighPrice") + " §7(Max: " + Config.getMaxPriceAsString() + ")");
                                    } else {
                                        p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "HighPrice") + " §7(Max: " + Config.getMaxPrice() + ")");
                                    }
                                }
                            } else {
                                p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "Zero"));
                            }
                        }

                        draw(p, page, obj);
                    }
                });
            }
        });

        ItemStack desc = new ItemStack(Material.STAINED_CLAY, 1, (byte) 15);
        ItemMeta descMeta = desc.getItemMeta();
        descMeta.setDisplayName(Language.getString("ItemTexts", "SellRemoveItemDisplayName"));
        descMeta.setLore(Arrays.asList(Language.getString("ItemTexts", "RemoveItemLore")));
        desc.setItemMeta(descMeta);
        ClickableItem descClick = new ClickableItem(new ShopItemStack(desc), inv, p);
        descClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                if (shopItem != null) {
                    Stocks.removeAllOfDeletedItem(shopItem, shop, p, true);

                    boolean cal = false;

                    if (shop.isHoloShop()) {
                        ShopHologram h = shop.getHolographicShop();
                        if (h.getItemLine().getItemStack().equals(shopItem.getItem())) {
                            cal = true;
                        }
                    }

                    shop.deleteShopItem(shopItem);

                    if (cal) {
                        ShopHologram h = shop.getHolographicShop();
                        if (shop.getShopItems(false).size() > 0) {
                            h.getItemLine().setItemStack(shop.getShopItems(false).get(0).getItem());
                        } else {
                            if (shop.getShopItems(true).size() > 0) {
                                h.getItemLine().setItemStack(shop.getShopItems(true).get(0).getItem());
                                h.getShopLine().setText(Language.getString("MainGUI", "Selling"));
                            } else {
                                DeleteHoloShop.deleteHologramShop(h);
                                shop.setObject("Holo", false);
                            }
                        }
                    }

                    if (!shopItem.isSelling()) {
                        shop.getMenu(MenuType.OWNER_BUYING).draw(p, page);
                    } else {
                        shop.getMenu(MenuType.OWNER_SELLING).draw(p, page);
                    }

                    if (shop.getShopItems().size() == 0 && max.hubbard.bettershops.TradeManager.getTrades(shop).size() == 0){
                        shop.setObject("Removal",new Date().getTime());
                    }

                    p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "RemoveItem"));

                } else {
                    p.closeInventory();
                    p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "NonExistingItem"));
                }

            }
        });

        ItemStack stock = new ItemStack(Material.STAINED_CLAY, 1, (byte) 4);
        ItemMeta stockMeta = stock.getItemMeta();
        stockMeta.setDisplayName(Language.getString("ItemTexts", "CollectStockDisplayName"));
        stockMeta.setLore(Arrays.asList(Language.getString("ItemTexts", "CollectStockLore")));
        stock.setItemMeta(stockMeta);
        ClickableItem remClick = new ClickableItem(new ShopItemStack(stock), inv, p);
        remClick.addLeftClickAction(new LeftClickAction() {
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
                                StockChangeEvent e = new StockChangeEvent(shopItem,shopItem.getStock(),0);
                                Bukkit.getPluginManager().callEvent(e);
                                Stocks.collectAll(shopItem, shop, p);
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


                        Stocks.collectStock(shopItem, amt, p, shop);
                        draw(p, page, obj);
                    }
                });
            }
        });

        ItemStack amount = new ItemStack(Material.STAINED_CLAY, 1, (byte) 2);
        ItemMeta amountMeta = amount.getItemMeta();
        amountMeta.setDisplayName(Language.getString("ItemTexts", "ChangeAskingAmount"));
        amountMeta.setLore(Arrays.asList(Language.getString("ItemTexts", "ChangeAskingAmountLore")));
        amount.setItemMeta(amountMeta);
        ClickableItem amtClick = new ClickableItem(new ShopItemStack(amount), inv, p);
        amtClick.addLeftClickAction(new LeftClickAction() {
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

                            if (amt > 0 && amt <= 2304) {
                                AmountChangeEvent e = new AmountChangeEvent(shopItem,shopItem.getAmount(),amt);
                                Bukkit.getPluginManager().callEvent(e);
                                shopItem.setObject("Amount", amt);

                                if (shopItem.getLiveEco()) {
                                    shopItem.getSister().setObject("Amount", amt);
                                }

                                if (shop.isHoloShop()) {
                                    ShopHologram h = shop.getHolographicShop();
                                    h.updateItemLines(h.getItemLine(), false);
                                }

                                p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "ChangeAmount"));
                                draw(p, page, obj);
                            } else {
                                draw(p, page, obj);
                                p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "HighAmount"));
                            }
                        }
                    }
                });
            }
        });

        ItemStack limit = new ItemStack(Material.NAME_TAG);
        ItemMeta limitMeta = limit.getItemMeta();
        limitMeta.setDisplayName(Language.getString("ItemTexts", "SellLimit").replaceAll("<Amount>", "" + shopItem.getLimit()));
        limitMeta.setLore(Arrays.asList(Language.getString("ItemTexts", "SellLimitLore")));
        limit.setItemMeta(limitMeta);
        ClickableItem limitClick = new ClickableItem(new ShopItemStack(limit), inv, p);
        limitClick.addLeftClickAction(new LeftClickAction() {
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
                            shopItem.setObject("Limit", amt);
                        } catch (Exception ex) {
                            p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "InvalidNumber"));
                        }
                        draw(p, page, obj);
                    }
                });
            }
        });

        ItemStack data = new ItemStack(Material.STAINED_CLAY, 1, (byte) 9);
        ItemMeta dataMeta = data.getItemMeta();
        dataMeta.setDisplayName(Language.getString("ItemTexts", "ItemDataDisplayName"));
        dataMeta.setLore(Arrays.asList(Language.getString("ItemTexts", "ItemDataWarning"),
                " ",
                Language.getString("ItemTexts", "ItemDataLore"),
                Language.getString("ItemTexts", "ItemDataExample")));
        data.setItemMeta(dataMeta);
        ClickableItem dataClick = new ClickableItem(new ShopItemStack(data), inv, p);
        dataClick.addLeftClickAction(new LeftClickAction() {
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
                            shopItem.setData((byte) amt);
                        } catch (Exception ex) {
                            p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "InvalidNumber"));
                        }
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
                shop.getMenu(MenuType.OWNER_SELLING).draw(p, page);
            }
        });

        ItemStack autoStock = new ItemStack(Material.DIAMOND);
        ItemMeta autoStockMeta = autoStock.getItemMeta();
        autoStockMeta.setDisplayName(Language.getString("Timings", "AutoStock"));
        autoStockMeta.setLore(Arrays.asList(Language.getString("Timings", "AutoStockLore")));
        autoStock.setItemMeta(autoStockMeta);
        ClickableItem autoStockClick = new ClickableItem(new ShopItemStack(autoStock), inv, p);
        autoStockClick.addLeftClickAction(new LeftClickAction() {
                                              @Override
                                              public void onAction(InventoryClickEvent e) {
                                                  shop.getMenu(MenuType.AUTO_STOCK).draw(p, page, shopItem);
                                              }
                                          }

        );

        ItemStack transCool = new ItemStack(Material.WATCH);
        ItemMeta transCoolMeta = transCool.getItemMeta();
        transCoolMeta.setDisplayName(Language.getString("Timings", "Transactions"));
        transCoolMeta.setLore(Arrays.asList(Language.getString("Timings", "TransactionsLore")));
        transCool.setItemMeta(transCoolMeta);
        ClickableItem transCoolClick = new ClickableItem(new ShopItemStack(transCool), inv, p);
        transCoolClick.addLeftClickAction(new LeftClickAction() {
                                              @Override
                                              public void onAction(InventoryClickEvent e) {
                                                  shop.getMenu(MenuType.COOLDOWNS).draw(p, page, shopItem);
                                              }
                                          }

        );

        inv.setItem(0, back);

        inv.setItem(4, shopItem.getItem());
        inv.setItem(8, limit);

        inv.setItem(48, transCool);

        inv.setItem(inv.firstEmpty(), nam);
        inv.setItem(inv.firstEmpty(), desc);
        inv.setItem(inv.firstEmpty(), stock);
        inv.setItem(inv.firstEmpty(), amount);
        inv.setItem(inv.firstEmpty(), data);

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
