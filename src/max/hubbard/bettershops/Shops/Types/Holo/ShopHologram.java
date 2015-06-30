package max.hubbard.bettershops.Shops.Types.Holo;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.handler.TouchHandler;
import com.gmail.filoghost.holographicdisplays.api.line.ItemLine;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import max.hubbard.bettershops.Configurations.Config;
import max.hubbard.bettershops.Configurations.Language;
import max.hubbard.bettershops.Menus.MenuType;
import max.hubbard.bettershops.Shops.FileShop;
import max.hubbard.bettershops.Shops.Items.FileShopItem;
import max.hubbard.bettershops.Shops.Items.SQLShopItem;
import max.hubbard.bettershops.Shops.Items.ShopItem;
import max.hubbard.bettershops.Shops.Shop;
import max.hubbard.bettershops.Utils.AnvilManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2015. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class ShopHologram {
    Shop shop;
    Hologram holo;
    ItemLine itemLine;
    TextLine shopLine;
    TextLine nameLine;

    public ShopHologram(final Shop shop, Hologram hologram) {
        this.shop = shop;
        holo = hologram;

        if (shop.getShopItems().size() > 0) {
            final ShopItem item = shop.getShopItems().get(0);

            nameLine = holo.appendTextLine("§a§l" + shop.getName());
            holo.appendTextLine(" ");

            if (item.isSelling()) {
                shopLine = holo.appendTextLine(Language.getString("MainGUI", "Selling"));
            } else {
                shopLine = holo.appendTextLine(Language.getString("MainGUI", "Buying"));
            }
            holo.appendTextLine(" ");
            TextLine upLine = holo.appendTextLine("§e§l▲");
            this.itemLine = holo.appendItemLine(item.getItem());
            TextLine downLine = holo.appendTextLine("§e§l▼");
            final TextLine stockLine = holo.appendTextLine(Language.getString("MainGUI", "Stock") + "§7" + item.getStock());
            final TextLine amountLine = holo.appendTextLine(Language.getString("MainGUI", "Amount") + "§7" + item.getAmount());
            final TextLine priceLine = holo.appendTextLine(Language.getString("MainGUI", "Price") + "§7" + item.getPriceAsString());

            updateItemLines(itemLine, item.isSelling());

            //Buy/Sell items
            itemLine.setTouchHandler(new TouchHandler() {
                @Override
                public void onTouch(Player player) {
                    TextLine l = (TextLine) holo.getLine(2);
                    if (l.getText().equals(Language.getString("MainGUI", "Buying"))) {
                        if (shop.getOwner().getUniqueId().equals(player.getUniqueId()) && !shop.isServerShop() || shop.getOwner().getUniqueId().toString().equals(player.getUniqueId().toString()) && !shop.isServerShop()) {

                            if (shop instanceof FileShop) {
                                ShopItem ite = FileShopItem.fromItemStack(shop, itemLine.getItemStack(), false);
                                shop.getMenu(MenuType.ITEM_MANAGER_BUYING).draw(player, ite.getPage(), ite);
                            } else {
                                ShopItem ite = SQLShopItem.fromItemStack(shop, itemLine.getItemStack(), false);
                                shop.getMenu(MenuType.ITEM_MANAGER_BUYING).draw(player, ite.getPage(), ite);
                            }

                        } else {
                            if (shop instanceof FileShop) {
                                buyItem(FileShopItem.fromItemStack(shop, itemLine.getItemStack(), false), player, false);
                            } else {
                                buyItem(SQLShopItem.fromItemStack(shop, itemLine.getItemStack(), false), player, false);
                            }
                        }
                    } else {

                        if (shop.getOwner().getUniqueId().equals(player.getUniqueId()) && !shop.isServerShop() || shop.getOwner().getUniqueId().toString().equals(player.getUniqueId().toString()) && !shop.isServerShop()) {
                            if (shop instanceof FileShop) {
                                ShopItem ite = FileShopItem.fromItemStack(shop, itemLine.getItemStack(), true);
                                shop.getMenu(MenuType.ITEM_MANAGER_SELLING).draw(player, ite.getPage(), ite);
                            } else {
                                ShopItem ite = SQLShopItem.fromItemStack(shop, itemLine.getItemStack(), true);
                                shop.getMenu(MenuType.ITEM_MANAGER_SELLING).draw(player, ite.getPage(), ite);
                            }

                        } else {

                            if (shop instanceof FileShop) {
                                buyItem(FileShopItem.fromItemStack(shop, itemLine.getItemStack(), true), player, true);
                            } else {
                                buyItem(SQLShopItem.fromItemStack(shop, itemLine.getItemStack(), true), player, true);
                            }
                        }
                    }
                }
            });

            //Open Shop Options
            nameLine.setTouchHandler(new TouchHandler() {
                @Override
                public void onTouch(Player player) {
                    if (shop.getOwner().getUniqueId().equals(player.getUniqueId()) || shop.getOwner().getUniqueId().toString().equals(player.getUniqueId().toString())) {
                        shop.getMenu(MenuType.SHOP_SETTINGS).draw(player, 1);
                    }
                }
            });

            //Change Buy/Sell Shop
            shopLine.setTouchHandler(new TouchHandler() {
                @Override
                public void onTouch(Player player) {
                    if (shop.getOwner().getUniqueId().equals(player.getUniqueId()) || shop.getOwner().getUniqueId().toString().equals(player.getUniqueId().toString())) {
                        boolean sell;
                        TextLine l = (TextLine) holo.getLine(2);
                        sell = !l.getText().equals(Language.getString("MainGUI", "Buying"));
                        if (sell) {
                            if (shop.getShopItems(false).size() > 0) {
                                shopLine.setText(Language.getString("MainGUI", "Buying"));

                                ShopItem it = shop.getShopItems(false).get(0);
                                itemLine.setItemStack(it.getItem());

                                updateItemLines(itemLine, false);
                            }

                        } else {
                            if ((boolean) Config.getObject("SellingShops")) {
                                if (shop.getShopItems(true).size() > 0) {
                                    shopLine.setText(Language.getString("MainGUI", "Selling"));

                                    ShopItem it = shop.getShopItems(true).get(0);
                                    itemLine.setItemStack(it.getItem());

                                    updateItemLines(itemLine, true);
                                }
                            }
                        }
                    }
                }
            });

            //change Price
            priceLine.setTouchHandler(new TouchHandler() {
                @Override
                public void onTouch(Player player) {
                    if (shop.getOwner().getUniqueId().equals(player.getUniqueId()) || shop.getOwner().getUniqueId().toString().equals(player.getUniqueId().toString())) {
                        boolean sell;
                        TextLine l = (TextLine) holo.getLine(2);
                        sell = !l.getText().equals(Language.getString("MainGUI", "Buying"));
                        if (shop instanceof FileShop) {
                            changePrice(player, FileShopItem.fromItemStack(shop, itemLine.getItemStack(), sell));
                        } else {
                            changePrice(player, SQLShopItem.fromItemStack(shop, itemLine.getItemStack(), sell));
                        }
                    }
                }
            });

            //change Amount
            amountLine.setTouchHandler(new TouchHandler() {
                @Override
                public void onTouch(Player player) {
                    if (shop.getOwner().getUniqueId().equals(player.getUniqueId()) || shop.getOwner().getUniqueId().toString().equals(player.getUniqueId().toString())) {
                        boolean sell;
                        TextLine l = (TextLine) holo.getLine(2);
                        sell = !l.getText().equals(Language.getString("MainGUI", "Buying"));
                        if (shop instanceof FileShop) {
                            changeAmount(player, FileShopItem.fromItemStack(shop, itemLine.getItemStack(), sell));
                        } else {
                            changeAmount(player, SQLShopItem.fromItemStack(shop, itemLine.getItemStack(), sell));
                        }

                    }
                }
            });


            //Up/Down Movement

            upLine.setTouchHandler(new TouchHandler() {
                @Override
                public void onTouch(Player player) {
                    boolean sell;
                    TextLine l = (TextLine) holo.getLine(2);
                    sell = !l.getText().equals(Language.getString("MainGUI", "Buying"));

                    ShopItem item;

                    if (shop instanceof FileShop) {
                        item = FileShopItem.fromItemStack(shop, itemLine.getItemStack(), sell);
                    } else {
                        item = SQLShopItem.fromItemStack(shop, itemLine.getItemStack(), sell);
                    }
                    if (item != null) {
                        if (shop.getShopItems(sell).contains(item)) {

                            int index = shop.getShopItems(sell).indexOf(item);
                            index += 1;
                            if (shop.getShopItems(sell).size() != index) {
                                ShopItem it = shop.getShopItems(sell).get(index);
                                itemLine.setItemStack(it.getItem());

                                updateItemLines(itemLine, sell);
                            } else {
                                ShopItem it = shop.getShopItems(sell).get(0);
                                itemLine.setItemStack(it.getItem());

                                updateItemLines(itemLine, sell);
                            }
                        } else {
                            if (shop.getShopItems(sell).size() > 0) {
                                ShopItem it = shop.getShopItems(sell).get(0);
                                itemLine.setItemStack(it.getItem());

                                updateItemLines(itemLine, sell);
                            } else {
                                itemLine.setItemStack(new ItemStack(Material.IRON_FENCE));

                                stockLine.setText(Language.getString("MainGUI", "Stock") + "-");
                                amountLine.setText(Language.getString("MainGUI", "Amount") + "-");
                                priceLine.setText(Language.getString("MainGUI", "Price") + "-");
                            }
                        }
                    } else {
                        if (shop.getShopItems(sell).size() > 0) {
                            ShopItem it = shop.getShopItems(sell).get(0);
                            itemLine.setItemStack(it.getItem());

                            updateItemLines(itemLine, sell);
                        } else {
                            itemLine.setItemStack(new ItemStack(Material.IRON_FENCE));

                            stockLine.setText(Language.getString("MainGUI", "Stock") + "-");
                            amountLine.setText(Language.getString("MainGUI", "Amount") + "-");
                            priceLine.setText(Language.getString("MainGUI", "Price") + "-");
                        }
                    }
                }
            });

            downLine.setTouchHandler(new TouchHandler() {
                @Override
                public void onTouch(Player player) {
                    boolean sell;
                    TextLine l = (TextLine) holo.getLine(2);
                    sell = !l.getText().equals(Language.getString("MainGUI", "Buying"));

                    ShopItem item;

                    if (shop instanceof FileShop) {
                        item = FileShopItem.fromItemStack(shop, itemLine.getItemStack(), sell);
                    } else {
                        item = SQLShopItem.fromItemStack(shop, itemLine.getItemStack(), sell);
                    }

                    if (item != null) {
                        if (shop.getShopItems(sell).contains(item)) {

                            int index = shop.getShopItems(sell).indexOf(item);
                            index -= 1;
                            if (index >= 0) {
                                ShopItem it = shop.getShopItems(sell).get(index);
                                itemLine.setItemStack(it.getItem());

                                updateItemLines(itemLine, sell);
                            } else {
                                ShopItem it = shop.getShopItems(sell).get(shop.getShopItems(sell).size() - 1);
                                itemLine.setItemStack(it.getItem());

                                updateItemLines(itemLine, sell);
                            }
                        } else {
                            if (shop.getShopItems(sell).size() > 0) {
                                ShopItem it = shop.getShopItems(sell).get(0);
                                itemLine.setItemStack(it.getItem());

                                updateItemLines(itemLine, sell);
                            } else {
                                itemLine.setItemStack(new ItemStack(Material.IRON_FENCE));

                                stockLine.setText(Language.getString("MainGUI", "Stock") + "-");
                                amountLine.setText(Language.getString("MainGUI", "Amount") + "-");
                                priceLine.setText(Language.getString("MainGUI", "Price") + "-");
                            }
                        }
                    } else {
                        if (shop.getShopItems(sell).size() > 0) {
                            ShopItem it = shop.getShopItems(sell).get(0);
                            itemLine.setItemStack(it.getItem());

                            updateItemLines(itemLine, sell);
                        } else {
                            itemLine.setItemStack(new ItemStack(Material.IRON_FENCE));

                            stockLine.setText(Language.getString("MainGUI", "Stock") + "-");
                            amountLine.setText(Language.getString("MainGUI", "Amount") + "-");
                            priceLine.setText(Language.getString("MainGUI", "Price") + "-");
                        }
                    }
                }
            });
        } else {
            shop.setObject("Holo", false);
            HologramManager.removeHolographicShop(this);
        }
    }

    public Shop getShop() {
        return shop;
    }

    public TextLine getShopLine() {
        return shopLine;
    }

    public Hologram getHologram() {
        return holo;
    }

    private void buyItem(ShopItem item, Player p, boolean sell) {
        if (sell) {
            shop.getMenu(MenuType.MAIN_SELLING).draw(p, item.getPage());
        } else {
            shop.getMenu(MenuType.MAIN_BUYING).draw(p, item.getPage());
        }
    }

    private void changePrice(final Player p, final ShopItem shopItem) {

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
                    if (amt > 0) {
                        if (amt <= Config.getMaxPrice()) {
                            shopItem.setPrice(amt);
                            updateItemLines(itemLine, shopItem.isSelling());
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
            }
        });
    }


    private void changeAmount(final Player p, final ShopItem shopItem) {

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
                        shopItem.setObject("Amount", amt);
                        updateItemLines(itemLine, shopItem.isSelling());
                        p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "ChangeAmount"));
                    } else {
                        p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "HighAmount"));
                    }
                }
            }
        });
    }

    public void updateItemLines(ItemLine line, boolean sell) {
        ShopItem it;
        if (shop instanceof FileShop) {
            it = FileShopItem.fromItemStack(shop, line.getItemStack(), sell);
        } else {
            it = SQLShopItem.fromItemStack(shop, line.getItemStack(), sell);
        }

        if (!it.isInfinite()) {
            ((TextLine) holo.getLine(7)).setText(Language.getString("MainGUI", "Stock") + "§7" + it.getStock());
        } else {
            ((TextLine) holo.getLine(7)).setText(Language.getString("MainGUI", "Stock") + "§7-");
        }

        if (sell) {
            ((TextLine) holo.getLine(8)).setText(Language.getString("MainGUI", "AskingAmount") + "§7" + it.getAmount());
            if (!it.getLiveEco()) {
                ((TextLine) holo.getLine(9)).setText(Language.getString("MainGUI", "AskingPrice") + "§7" + it.getPriceAsString());
            } else {
                if (it.getAdjustedPrice() != it.getOrigPrice()) {
                    ((TextLine) holo.getLine(9)).setText(Language.getString("MainGUI", "AskingPrice") + "§c§m" + it.getOrigPrice() + "§a" + it.getAdjustedPriceAsString());
                } else {
                    ((TextLine) holo.getLine(9)).setText(Language.getString("MainGUI", "AskingPrice") + "§7" + it.getPriceAsString());
                }
            }
        } else {
            ((TextLine) holo.getLine(8)).setText(Language.getString("MainGUI", "Amount") + "§7" + it.getAmount());
            if (!it.getLiveEco()) {
                ((TextLine) holo.getLine(9)).setText(Language.getString("MainGUI", "Price") + "§7" + it.getPriceAsString());
            } else {
                if (it.getAdjustedPrice() != it.getOrigPrice()) {
                    ((TextLine) holo.getLine(9)).setText(Language.getString("MainGUI", "Price") + "§c§m" + it.getOrigPrice() + "§a" + it.getAdjustedPriceAsString());
                } else {
                    ((TextLine) holo.getLine(9)).setText(Language.getString("MainGUI", "Price") + "§7" + it.getPriceAsString());
                }
            }
        }
    }

    public ItemLine getItemLine() {
        return itemLine;
    }

    public TextLine getNameLine() {
        return nameLine;
    }
}
