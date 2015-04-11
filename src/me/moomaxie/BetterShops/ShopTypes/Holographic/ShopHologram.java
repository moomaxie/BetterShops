package me.moomaxie.BetterShops.ShopTypes.Holographic;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.handler.TouchHandler;
import com.gmail.filoghost.holographicdisplays.api.line.ItemLine;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import me.moomaxie.BetterShops.Configurations.AnvilGUI;
import me.moomaxie.BetterShops.Configurations.Config;
import me.moomaxie.BetterShops.Configurations.GUIMessages.MainGUI;
import me.moomaxie.BetterShops.Configurations.Messages;
import me.moomaxie.BetterShops.Core;
import me.moomaxie.BetterShops.Listeners.BuyerOptions.BuyItem;
import me.moomaxie.BetterShops.Listeners.ManagerOptions.ItemManager;
import me.moomaxie.BetterShops.Listeners.ManagerOptions.ShopSettings;
import me.moomaxie.BetterShops.Listeners.Misc.ChatMessages;
import me.moomaxie.BetterShops.Listeners.OwnerSellingOptions.SellingItemManager;
import me.moomaxie.BetterShops.Listeners.SellerOptions.SellItem;
import me.moomaxie.BetterShops.Shops.Shop;
import me.moomaxie.BetterShops.Shops.ShopItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

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

    public ShopHologram(final Shop shop, Hologram hologram) {
        this.shop = shop;
        holo = hologram;

        if (shop.getShopItems().size() > 0) {
            final ShopItem item = shop.getShopItems().get(0);

            TextLine nameLine = holo.appendTextLine("§a§l" + shop.getName());
            holo.appendTextLine(" ");

            if (item.isSelling()) {
                shopLine = holo.appendTextLine(MainGUI.getString("Selling"));
            } else {
                shopLine = holo.appendTextLine(MainGUI.getString("Buying"));
            }
            holo.appendTextLine(" ");
            TextLine upLine = holo.appendTextLine("§e§l▲");
            this.itemLine = holo.appendItemLine(item.getItem());
            TextLine downLine = holo.appendTextLine("§e§l▼");
            final TextLine stockLine = holo.appendTextLine(MainGUI.getString("Stock") + "§7" + item.getStock());
            final TextLine amountLine = holo.appendTextLine(MainGUI.getString("Amount") + "§7" + item.getAmount());
            final TextLine priceLine = holo.appendTextLine(MainGUI.getString("Price") + "§7" + item.getPriceAsString());

            updateItemLines(itemLine, item.isSelling());

            //Buy/Sell items
            itemLine.setTouchHandler(new TouchHandler() {
                @Override
                public void onTouch(Player player) {
                    TextLine l = (TextLine) holo.getLine(2);
                    if (l.getText().equals(MainGUI.getString("Buying"))) {
                        if (shop.getOwner().getUniqueId().equals(player.getUniqueId()) && !shop.isServerShop() || shop.getOwner().getUniqueId().toString().equals(player.getUniqueId().toString()) && !shop.isServerShop()) {
                            ShopItem ite = ShopItem.fromItemStack(shop, itemLine.getItemStack(), false);
                            ItemManager.openItemManager(null, player, shop, ite, ite.getItem());

                        } else {

                            buyItem(ShopItem.fromItemStack(shop, itemLine.getItemStack(), false), player, false);
                        }
                    } else {

                        if (shop.getOwner().getUniqueId().equals(player.getUniqueId()) && !shop.isServerShop() || shop.getOwner().getUniqueId().toString().equals(player.getUniqueId().toString()) && !shop.isServerShop()) {
                            ShopItem ite = ShopItem.fromItemStack(shop, itemLine.getItemStack(), true);
                            SellingItemManager.openItemManager(null, player, shop, ite.getItem());

                        } else {

                            buyItem(ShopItem.fromItemStack(shop, itemLine.getItemStack(), true), player, true);
                        }
                    }
                }
            });

            //Open Shop Options
            nameLine.setTouchHandler(new TouchHandler() {
                @Override
                public void onTouch(Player player) {
                    if (shop.getOwner().getUniqueId().equals(player.getUniqueId()) || shop.getOwner().getUniqueId().toString().equals(player.getUniqueId().toString())) {
                        ShopSettings.openShopManager(null, player, shop);
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
                        sell = !l.getText().equals(MainGUI.getString("Buying"));

                        if (sell) {
                            if (shop.getShopItems(false).size() > 0) {
                                shopLine.setText(MainGUI.getString("Buying"));

                                ShopItem it = shop.getShopItems(false).get(0);
                                itemLine.setItemStack(it.getItem());

                                updateItemLines(itemLine, false);
                            }

                        } else {
                            if (Config.useSellingShop()) {
                                if (shop.getShopItems(true).size() > 0) {
                                    shopLine.setText(MainGUI.getString("Selling"));

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
                        sell = !l.getText().equals(MainGUI.getString("Buying"));
                        changePrice(player, ShopItem.fromItemStack(shop, itemLine.getItemStack(), sell));

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
                        sell = !l.getText().equals(MainGUI.getString("Buying"));
                        changeAmount(player, ShopItem.fromItemStack(shop, itemLine.getItemStack(), sell));

                    }
                }
            });


            //Up/Down Movement

            upLine.setTouchHandler(new TouchHandler() {
                @Override
                public void onTouch(Player player) {
                    boolean sell;
                    TextLine l = (TextLine) holo.getLine(2);
                    sell = !l.getText().equals(MainGUI.getString("Buying"));

                    ShopItem item = ShopItem.fromItemStack(shop, itemLine.getItemStack(), sell);
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
                                itemLine.setItemStack(new ItemStack(Material.AIR));

                                stockLine.setText(MainGUI.getString("Stock") + "-");
                                amountLine.setText(MainGUI.getString("Amount") + "-");
                                priceLine.setText(MainGUI.getString("Price") + "-");
                            }
                        }
                    } else {
                        if (shop.getShopItems(sell).size() > 0) {
                            ShopItem it = shop.getShopItems(sell).get(0);
                            itemLine.setItemStack(it.getItem());

                            updateItemLines(itemLine, sell);
                        } else {
                            itemLine.setItemStack(new ItemStack(Material.AIR));

                            stockLine.setText(MainGUI.getString("Stock") + "-");
                            amountLine.setText(MainGUI.getString("Amount") + "-");
                            priceLine.setText(MainGUI.getString("Price") + "-");
                        }
                    }
                }
            });

            downLine.setTouchHandler(new TouchHandler() {
                @Override
                public void onTouch(Player player) {
                    boolean sell;
                    TextLine l = (TextLine) holo.getLine(2);
                    sell = !l.getText().equals(MainGUI.getString("Buying"));

                    ShopItem item = ShopItem.fromItemStack(shop, itemLine.getItemStack(), sell);
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
                                itemLine.setItemStack(new ItemStack(Material.AIR));

                                stockLine.setText(MainGUI.getString("Stock") + "-");
                                amountLine.setText(MainGUI.getString("Amount") + "-");
                                priceLine.setText(MainGUI.getString("Price") + "-");
                            }
                        }
                    } else {
                        if (shop.getShopItems(sell).size() > 0) {
                            ShopItem it = shop.getShopItems(sell).get(0);
                            itemLine.setItemStack(it.getItem());

                            updateItemLines(itemLine, sell);
                        } else {
                            itemLine.setItemStack(new ItemStack(Material.AIR));

                            stockLine.setText(MainGUI.getString("Stock") + "-");
                            amountLine.setText(MainGUI.getString("Amount") + "-");
                            priceLine.setText(MainGUI.getString("Price") + "-");
                        }
                    }
                }
            });
        } else {
            shop.setHoloShop(false);
            HologramManager.removeHolographicShop(this);
        }
    }

    public Shop getShop() {
        return shop;
    }

    public TextLine getShopLine(){
        return shopLine;
    }

    public Hologram getHologram() {
        return holo;
    }

    private void buyItem(ShopItem item, Player p, boolean sell) {
        if (sell) {
            SellItem.openSellScreen(null, p, shop, item.getItem());
        } else {
            BuyItem.openBuyScreen(null, p, shop, item.getItem());
        }
    }

    private void changePrice(final Player p, final ShopItem shopItem) {
        if (Config.useAnvil()) {
            AnvilGUI gui = Core.getAnvilGUI();
            gui.doGUIThing(p, new AnvilGUI.AnvilClickEventHandler() {
                @Override
                public void onAnvilClick(AnvilGUI.AnvilClickEvent ev) {
                    if (ev.getSlot() == 2) {
                        ev.setWillClose(true);
                        ev.setWillDestroy(true);

                        if (ev.getCurrentItem().getType() == Material.PAPER) {
                            if (ev.getCurrentItem().hasItemMeta()) {
                                if (ev.getCurrentItem().getItemMeta().getDisplayName() != null) {
                                    String name = ev.getCurrentItem().getItemMeta().getDisplayName();

                                    boolean can;
                                    double amt = 0.0;
                                    try {
                                        amt = Double.parseDouble(name);

                                        can = true;
                                    } catch (Exception ex) {
                                        p.sendMessage(Messages.getString("Prefix") + Messages.getString("InvalidNumber"));
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
                                                p.sendMessage(Messages.getString("Prefix") + Messages.getString("ChangePrice"));
                                            } else {
                                                if (String.valueOf(Config.getMaxPrice()).contains("E")) {
                                                    p.sendMessage(Messages.getString("Prefix") + Messages.getString("HighPrice") + " §7(Max: " + Config.getMaxPriceAsString() + ")");
                                                } else {
                                                    p.sendMessage(Messages.getString("Prefix") + Messages.getString("HighPrice") + " §7(Max: " + Config.getMaxPrice() + ")");
                                                }
                                            }
                                        } else {
                                            p.sendMessage(Messages.getString("Prefix") + Messages.getString("Zero"));
                                        }
                                    }
                                    p.closeInventory();
                                }
                            }
                        }

                    } else {
                        ev.setWillClose(false);
                        ev.setWillDestroy(false);
                    }
                }
            });

            ItemStack it = new ItemStack(Material.PAPER);
            ItemMeta meta = it.getItemMeta();
            meta.setDisplayName("Type New Price");
            it.setItemMeta(meta);

            gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, it);

            gui.open();
        } else {
            p.closeInventory();
            Map<Shop, ShopItem> map = new HashMap<>();
            map.put(shop, shopItem);

            if (!shopItem.isSelling()) {
                ChatMessages.setBuyPrice.put(p, map);
            } else {
                ChatMessages.setSellPrice.put(p, map);
            }
            p.sendMessage(Messages.getString("Prefix") + Messages.getString("ChatMessage"));
        }
    }

    private void changeAmount(final Player p, final ShopItem shopItem) {
        if (Config.useAnvil()) {
            AnvilGUI gui = Core.getAnvilGUI();
            gui.doGUIThing(p, new AnvilGUI.AnvilClickEventHandler() {
                @Override
                public void onAnvilClick(AnvilGUI.AnvilClickEvent ev) {
                    if (ev.getSlot() == 2) {
                        ev.setWillClose(true);
                        ev.setWillDestroy(true);


                        if (ev.getCurrentItem().getType() == Material.PAPER) {
                            if (ev.getCurrentItem().hasItemMeta()) {
                                if (ev.getCurrentItem().getItemMeta().getDisplayName() != null) {
                                    String name = ev.getCurrentItem().getItemMeta().getDisplayName();

                                    boolean can;
                                    int amt = 0;
                                    try {
                                        amt = Integer.parseInt(name);
                                        can = true;
                                    } catch (Exception ex) {
                                        p.sendMessage(Messages.getString("Prefix") + Messages.getString("InvalidNumber"));
                                        can = false;
                                    }

                                    if (can) {

                                        if (amt > 0 && amt <= 2304) {
                                            shopItem.setAmount(amt);
                                            updateItemLines(itemLine, shopItem.isSelling());
                                            p.sendMessage(Messages.getString("Prefix") + Messages.getString("ChangeAmount"));
                                        } else {
                                            p.sendMessage(Messages.getString("Prefix") + Messages.getString("HighAmount"));
                                        }
                                    }
                                    p.closeInventory();
                                }
                            }
                        }

                    } else {
                        ev.setWillClose(false);
                        ev.setWillDestroy(false);
                    }
                }
            });

            ItemStack it = new ItemStack(Material.PAPER);
            ItemMeta meta = it.getItemMeta();
            meta.setDisplayName("Type New Amount");
            it.setItemMeta(meta);

            gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, it);

            gui.open();
        } else {
            p.closeInventory();
            Map<Shop, ShopItem> map = new HashMap<>();
            map.put(shop, shopItem);

            if (!shopItem.isSelling()) {
                ChatMessages.setBuyAmount.put(p, map);
            } else {
                ChatMessages.setSellAmount.put(p, map);
            }
            p.sendMessage(Messages.getString("Prefix") + Messages.getString("ChatMessage"));
        }
    }

    public void updateItemLines(ItemLine line, boolean sell) {
        ShopItem it = ShopItem.fromItemStack(shop, line.getItemStack(), sell);

        if (!it.isInfinite()) {
            ((TextLine) holo.getLine(7)).setText(MainGUI.getString("Stock") + "§7" + it.getStock());
        } else {
            ((TextLine) holo.getLine(7)).setText(MainGUI.getString("Stock") + "§7-");
        }

        if (sell) {
            ((TextLine) holo.getLine(8)).setText(MainGUI.getString("AskingAmount") + "§7" + it.getAmount());
            if (!it.getLiveEco()) {
                ((TextLine) holo.getLine(9)).setText(MainGUI.getString("AskingPrice") + "§7" + it.getPriceAsString());
            } else {
                if (it.getAdjustedPrice() != it.getOrigPrice()) {
                    ((TextLine) holo.getLine(9)).setText(MainGUI.getString("AskingPrice") + "§c§m" + it.getOrigPrice() + "§a" + it.getAdjustedPriceAsString());
                } else {
                    ((TextLine) holo.getLine(9)).setText(MainGUI.getString("AskingPrice") + "§7" + it.getPriceAsString());
                }
            }
        } else {
            ((TextLine) holo.getLine(8)).setText(MainGUI.getString("Amount") + "§7" + it.getAmount());
            if (!it.getLiveEco()) {
                ((TextLine) holo.getLine(9)).setText(MainGUI.getString("Price") + "§7" + it.getPriceAsString());
            } else {
                if (it.getAdjustedPrice() != it.getOrigPrice()) {
                    ((TextLine) holo.getLine(9)).setText(MainGUI.getString("Price") + "§c§m" + it.getOrigPrice() + "§a" + it.getAdjustedPriceAsString());
                } else {
                    ((TextLine) holo.getLine(9)).setText(MainGUI.getString("Price") + "§7" + it.getPriceAsString());
                }
            }
        }
    }

    public ItemLine getItemLine(){
        return itemLine;
    }
}
