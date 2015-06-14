package max.hubbard.bettershops.Menus.ShopMenus;

import max.hubbard.bettershops.Configurations.Config;
import max.hubbard.bettershops.Configurations.Language;
import max.hubbard.bettershops.Configurations.Permissions;
import max.hubbard.bettershops.Core;
import max.hubbard.bettershops.Events.ShopBuyItemEvent;
import max.hubbard.bettershops.Menus.MenuType;
import max.hubbard.bettershops.Menus.ShopMenu;
import max.hubbard.bettershops.Shops.FileShop;
import max.hubbard.bettershops.Shops.Items.Actions.ClickableItem;
import max.hubbard.bettershops.Shops.Items.Actions.LeftClickAction;
import max.hubbard.bettershops.Shops.Items.Actions.ShopItemStack;
import max.hubbard.bettershops.Shops.Items.FileShopItem;
import max.hubbard.bettershops.Shops.Items.SQLShopItem;
import max.hubbard.bettershops.Shops.Items.ShopItem;
import max.hubbard.bettershops.Shops.Shop;
import max.hubbard.bettershops.Shops.Types.Holo.ShopHologram;
import max.hubbard.bettershops.Utils.Stocks;
import max.hubbard.bettershops.Utils.WordsCapitalizer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.scheduler.BukkitRunnable;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2015. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class Cart implements ShopMenu {

    private static HashMap<HashMap<UUID, Shop>, List<HashMap<HashMap<ShopItem, Byte>, Integer>>> cart = new HashMap<>();

    Shop shop;
    Inventory inv;

    public Cart(Shop shop) {
        this.shop = shop;
        inv = Bukkit.createInventory(null, 54, Language.getString("MainGUI", "ShopHeader") + shop.getName());

    }


    @Override
    public MenuType getType() {
        return MenuType.CART;
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
        for (int i = 0; i < 9; i++) {
            inv.setItem(i, item);
        }

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

        ItemStack price = new ItemStack(Material.EMERALD);
        ItemMeta priceMeta = price.getItemMeta();
        priceMeta.setDisplayName(Language.getString("Checkout", "Prices"));
        priceMeta.setLore(Arrays.asList(Language.getString("Checkout", "NothingInCart")));
        price.setItemMeta(priceMeta);

        ItemStack buy = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 5);
        ItemMeta buyMeta = buy.getItemMeta();
        buyMeta.setDisplayName(Language.getString("Checkout", "BuyItems"));
        buy.setItemMeta(buyMeta);
        ClickableItem buyClick = new ClickableItem(new ShopItemStack(buy), inv, p);
        buyClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                buyItems(p);
            }
        });

        ItemStack no = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 0);
        ItemMeta noMeta = no.getItemMeta();
        noMeta.setDisplayName(Language.getString("Checkout", "CannotAfford"));
        no.setItemMeta(noMeta);

        ItemStack noItems = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 0);
        ItemMeta noItemsMeta = noItems.getItemMeta();
        noItemsMeta.setDisplayName(Language.getString("Checkout", "NothingInCart"));
        noItems.setItemMeta(noItemsMeta);

        inv.setItem(7, noItems);
        inv.setItem(8, noItems);

        inv.setItem(4, price);

        double total = 0.00;

        inv.setItem(0, back);

        inv.setItem(0, back);

        boolean can = false;

        HashMap<UUID, Shop> k = null;

        for (HashMap<UUID, Shop> key : cart.keySet()) {
            if (key.keySet().contains(p.getUniqueId())) {
                if (key.get(p.getUniqueId()).getName().equals(shop.getName())) {
                    can = true;
                    k = key;
                    break;
                }
            }
        }

        if (can) {
            List<HashMap<HashMap<ShopItem, Byte>, Integer>> items = cart.get(k);
            if (items != null) {
                for (HashMap<HashMap<ShopItem, Byte>, Integer> ie : items) {
                    for (HashMap<ShopItem, Byte> i : ie.keySet()) {
                        for (final ShopItem it : i.keySet()) {

                            if (it != null && ie.get(i) != null) {
                                MaterialData data = new MaterialData(it.getItem().getType(), i.get(it));
                                it.getItem().setData(data);

                                total = total + (ie.get(i) * (it.getPrice() / it.getAmount()));

                                ItemMeta meta = it.getItem().getItemMeta();
                                List<String> lore = new ArrayList<>();
                                if (it.getLore() != null) {
                                    for (String s : it.getLore()) {
                                        lore.add(s);
                                    }
                                }

                                final ItemStack ite = it.getItem().clone();

                                if (lore != null) {

                                    if (ie.get(i) != null) {
                                        lore.add(Language.getString("MainGUI", "Amount") + " §7" + ie.get(i));
                                    } else {
                                        lore.add(Language.getString("MainGUI", "Amount") + " §71");
                                    }
                                    if (ie.get(i) != null) {
                                        BigDecimal bd = new BigDecimal((it.getPrice() / it.getAmount()) * ie.get(i));

                                        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
                                        lore.add(Language.getString("MainGUI", "Price") + " §7" + bd.doubleValue());
                                    } else {
                                        BigDecimal bd = new BigDecimal((it.getPrice() / it.getAmount()) * 1);
                                        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
                                        lore.add(Language.getString("MainGUI", "Price") + " §7" + bd.doubleValue());

                                    }
                                    lore.add(" ");
                                    lore.add(Language.getString("Checkout", "ClickToRemove"));
                                    meta.setLore(lore);
                                    ite.setItemMeta(meta);
                                }
                                inv.setItem(inv.firstEmpty(), ite);
                                final ClickableItem iteClick = new ClickableItem(new ShopItemStack(ite),inv,p);
                                iteClick.addLeftClickAction(new LeftClickAction() {
                                    @Override
                                    public void onAction(InventoryClickEvent e) {
                                        removeItem(it,p,ite);
                                    }
                                });

                                BigDecimal bd = new BigDecimal(total);
                                bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);

                                BigDecimal bd2 = new BigDecimal((Core.getEconomy().getBalance(p) - Double.parseDouble(new DecimalFormat("#.00").format(total).replaceFirst(",", ".").replaceAll(",", ""))));
                                bd2 = bd2.setScale(2, BigDecimal.ROUND_HALF_UP);

                                priceMeta.setLore(Arrays.asList(Language.getString("Checkout", "TotalPrice") + " §7" + bd.doubleValue(),
                                        Language.getString("Checkout", "Balance") + " §7" + Core.getEconomy().getBalance(p),
                                        Language.getString("Checkout", "NewBalance") + " §7" + bd2.doubleValue()));
                                price.setItemMeta(priceMeta);
                                inv.setItem(4, price);

                                inv.setItem(7, buy);
                                inv.setItem(8, buy);
                            }
                        }
                    }
                }
            }
        }

        if ((Core.getEconomy().getBalance(p) - total) < 0) {
            inv.setItem(7, no);
            inv.setItem(8, no);
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

    public void removeItem(ShopItem shopItem, Player p, ItemStack clicked) {
        boolean can = false;

        HashMap<UUID, Shop> k = null;

        for (HashMap<UUID, Shop> key : cart.keySet()) {
            if (key.keySet().contains(p.getUniqueId())) {
                if (key.get(p.getUniqueId()).getName().equals(shop.getName())) {
                    can = true;
                    k = key;
                    break;
                }
            }
        }

        if (can) {
            List<HashMap<HashMap<ShopItem, Byte>, Integer>> items = cart.get(k);
            if (items != null) {
                for (HashMap<HashMap<ShopItem, Byte>, Integer> ie : items) {
                    for (HashMap<ShopItem, Byte> i : ie.keySet()) {
                        for (ShopItem it : i.keySet()) {

                            if (it != null && ie.get(i) != null) {
                                MaterialData data = new MaterialData(it.getItem().getType(), i.get(it));
                                it.getItem().setData(data);

                                ItemMeta meta = it.getItem().getItemMeta();
                                List<String> lore = new ArrayList<>();
                                if (it.getLore() != null) {
                                    for (String s : it.getLore()) {
                                        lore.add(s);
                                    }
                                }

                                ItemStack ite = it.getItem().clone();

                                if (lore != null) {

                                    if (ie.get(i) != null) {
                                        lore.add(Language.getString("MainGUI", "Amount") + " §7" + ie.get(i));
                                    } else {
                                        lore.add(Language.getString("MainGUI", "Amount") + " §71");
                                    }
                                    if (ie.get(i) != null) {
                                        BigDecimal bd = new BigDecimal((it.getPrice() / it.getAmount()) * ie.get(i));

                                        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
                                        lore.add(Language.getString("MainGUI", "Price") + " §7" + bd.doubleValue());
                                    } else {
                                        BigDecimal bd = new BigDecimal((it.getPrice() / it.getAmount()) * 1);
                                        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
                                        lore.add(Language.getString("MainGUI", "Price") + " §7" + bd.doubleValue());

                                    }
                                    lore.add(" ");
                                    lore.add(Language.getString("Checkout", "ClickToRemove"));
                                    meta.setLore(lore);
                                    ite.setItemMeta(meta);

                                }

                                if (clicked.equals(ite) && clicked.getData().getData() == ite.getData().getData() || clicked.toString().equals(ite.toString()) && clicked.getData().getData() == ite.getData().getData()) {

                                    cart.get(k).remove(ie);
                                    draw(p, shopItem.getPage());
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static void addToCart(Player p, Shop shop, ShopItem item, int amt) {
        HashMap<UUID, Shop> k = null;
        boolean can = false;

        for (HashMap<UUID, Shop> key : cart.keySet()) {
            if (key.keySet().contains(p.getUniqueId())) {
                if (key.get(p.getUniqueId()).getName().equals(shop.getName())) {
                    can = true;
                    k = key;
                    break;
                }
            }
        }

        if (can) {

            HashMap<HashMap<ShopItem, Byte>, Integer> value = new HashMap<>();
            HashMap<ShopItem, Byte> by = new HashMap<>();
            by.put(item, item.getData());
            value.put(by, amt);

            List<HashMap<HashMap<ShopItem, Byte>, Integer>> v = cart.get(k);

            v.add(value);

            cart.put(k, v);


        } else {
            HashMap<UUID, Shop> key = new HashMap<>();
            key.put(p.getUniqueId(), shop);

            HashMap<HashMap<ShopItem, Byte>, Integer> value = new HashMap<>();
            HashMap<ShopItem, Byte> by = new HashMap<>();
            by.put(item, item.getData());
            value.put(by, amt);

            List<HashMap<HashMap<ShopItem, Byte>, Integer>> v = new ArrayList<>();
            v.add(value);

            cart.put(key, v);
        }
    }

    public void buyItems(Player p) {
        if (!shop.isOpen() && !(boolean)Config.getObject("UseOnClose")) {
            p.closeInventory();
            p.sendMessage(Language.getString("Messages","Prefix") + Language.getString("Messages","ShopClosed"));
            return;
        }

        for (int i = 9; i < inv.getContents().length; i++) {
            ItemStack item = inv.getItem(i);
            ShopItem shopItem;
            if (shop instanceof FileShop){
                shopItem = FileShopItem.fromItemStack(shop, item, false);
            } else {
                shopItem = SQLShopItem.fromItemStack(shop, item, false);
            }

            if (shopItem != null && item != null && item.getItemMeta().getLore() != null) {
                List<String> lore = item.getItemMeta().getLore();

                double pr = (double)Config.getObject("DefaultPrice");
                int amt = 1;
                for (String s : lore) {
                    if (s.contains(Language.getString("MainGUI","Price"))) {
                        pr = Double.parseDouble(s.substring(Language.getString("MainGUI","Price").length() + 3));
                    }
                    if (s.contains(Language.getString("MainGUI","Amount"))) {
                        amt = Integer.parseInt(s.substring(Language.getString("MainGUI","Amount").length() + 3));
                    }
                }

                BigDecimal bd = new BigDecimal((pr / amt) * shopItem.getAmount());

                bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);

                if (bd.doubleValue() != shopItem.getPrice()) {
                    p.closeInventory();
                    if (item.getItemMeta().getDisplayName() != null) {
                        p.sendMessage(Language.getString("Messages","Prefix") + Language.getString("Messages","PriceChange").replaceAll("<Item>", item.getItemMeta().getDisplayName()));
                    } else {
                        p.sendMessage(Language.getString("Messages","Prefix") + Language.getString("Messages","PriceChange").replaceAll("<Item>", WordsCapitalizer.capitalizeEveryWord(item.getType().name().replaceAll("_", " "))));
                    }

                    return;
                }
            }
        }

        buyCheckoutItems(p);
    }

    public void buyCheckoutItems(Player p) {
        double total = 0.0;
        boolean can = false;

        HashMap<UUID, Shop> k = null;

        for (HashMap<UUID, Shop> key : cart.keySet()) {
            if (key.keySet().contains(p.getUniqueId())) {
                if (key.get(p.getUniqueId()).getName().equals(shop.getName())) {
                    can = true;
                    k = key;
                    break;
                }
            }
        }

        double t = 0;
        if (can) {
            List<HashMap<HashMap<ShopItem, Byte>, Integer>> items = cart.get(k);
            if (items != null) {
                for (HashMap<HashMap<ShopItem, Byte>, Integer> ie : items) {
                    for (HashMap<ShopItem, Byte> i : ie.keySet()) {
                        for (ShopItem it : i.keySet()) {
                            ItemMeta meta = it.getItem().getItemMeta();
                            List<String> lore = new ArrayList<>();
                            if (it.getLore() != null) {
                                for (String s : it.getLore()) {
                                    lore.add(s);
                                }
                            }

                            int amt = 1;

                            ItemStack ite = it.getItem().clone();

                            if (lore != null) {
                                if (!lore.contains(Language.getString("Checkout","ClickToRemove"))) {
                                    if (ie.get(i) != null) {
                                        lore.add(Language.getString("MainGUI","Amount") + " §7" + ie.get(i));
                                        amt = ie.get(i);
                                    } else {
                                        lore.add(Language.getString("MainGUI","Amount") + " §71");
                                    }
                                    if (ie.get(i) != null) {
                                        BigDecimal bd = new BigDecimal((it.getPrice() / it.getAmount()) * ie.get(i));
                                        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
                                        lore.add(Language.getString("MainGUI","Price") + " §7" + bd.doubleValue());
                                        total = total + bd.doubleValue();
                                    } else {
                                        BigDecimal bd = new BigDecimal((it.getPrice() / it.getAmount()) * 1);
                                        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
                                        lore.add(Language.getString("MainGUI","Price") + " §7" + bd.doubleValue());
                                        total = total + bd.doubleValue();
                                    }
                                    lore.add(" ");
                                    lore.add(Language.getString("Checkout","ClickToRemove"));
                                    meta.setLore(lore);
                                    ite.setItemMeta(meta);
                                }
                            }

                            boolean c = true;

                            if ((boolean)Config.getObject("Permissions")) {
                                if (!Permissions.hasBuyItemPerm(p, it.getItem().getType())) {
                                    c = false;
                                }
                            }

                            if (c) {

                                if (it.getStock() >= amt) {
                                    if (Core.getEconomy().getBalance(p) >= it.getPrice() * amt) {
                                        double value;
                                        if (!shop.isServerShop()) {


                                            BigDecimal bd = new BigDecimal((it.getPrice() / it.getAmount()) * amt);
                                            bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
                                            Core.getEconomy().withdrawPlayer(Bukkit.getOfflinePlayer(p.getUniqueId()), bd.doubleValue());
                                            Core.getEconomy().depositPlayer(Bukkit.getOfflinePlayer(shop.getOwner().getUniqueId()), bd.doubleValue());
                                            t = t + bd.doubleValue();
                                            value = bd.doubleValue();


                                            if (shop.isNotify()) {
                                                if (shop.getOwner() != null && shop.getOwner().isOnline()) {
                                                    shop.getOwner().getPlayer().sendMessage(Language.getString("Messages","Prefix") + Language.getString("Messages","NotifyBuy").replaceAll("<Player>", p.getDisplayName()));
                                                    shop.getOwner().getPlayer().sendMessage(Language.getString("Messages","Prefix") + Language.getString("Messages","ReceivedAmount").replaceAll("<Amount>", "" + bd.doubleValue()));

                                                    if (Core.isAboveEight() && (boolean) Config.getObject("Titles") && Core.getTitleManager() != null) {

                                                        Core.getTitleManager().setTimes(shop.getOwner().getPlayer(), 20, 60, 20);
                                                        Core.getTitleManager().sendTitle(shop.getOwner().getPlayer(), Language.getString("Messages","NotifyBuy").replaceAll("<Player>", p.getDisplayName()));
                                                        Core.getTitleManager().sendSubTitle(shop.getOwner().getPlayer(), Language.getString("Messages","ReceivedAmount").replaceAll("<Amount>", "" + bd.doubleValue()));


                                                    }
                                                }
                                            }
                                        } else {
                                            BigDecimal bd = new BigDecimal((it.getPrice() / it.getAmount()) * amt);
                                            bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
                                            Core.getEconomy().withdrawPlayer(Bukkit.getOfflinePlayer(p.getUniqueId()), bd.doubleValue());
                                            t = t + bd.doubleValue();
                                            value = bd.doubleValue();
                                        }

                                        Stocks.addItemsToInventory(it, p, amt);

//                                    for (int o = 0; o < amt; o++) {
//                                        p.getInventory().addItem(it.getItem());
//                                    }
                                        if (!it.isInfinite())
                                            it.setObject("Stock", it.getStock() - amt);

                                        shop.getHistory().addTransaction(p, new Date(), it, value, amt, false, true);

                                        ShopBuyItemEvent ev = new ShopBuyItemEvent(it, shop, p);

                                        Bukkit.getPluginManager().callEvent(ev);

                                        double a = amt / it.getAmount();

                                        if (it.getLiveEco()) {
                                            it.setAmountTo(it.getAmountTo() + a);
                                        }

                                    } else {
                                        if (ite.getItemMeta() != null && ite.getItemMeta().getDisplayName() != null) {
                                            if (!Language.getString("Messages","CannotAfford").contains("<Item>")) {
                                                p.sendMessage(Language.getString("Messages","Prefix") + Language.getString("Messages","CannotAfford") + ite.getItemMeta().getDisplayName());
                                            } else {
                                                p.sendMessage(Language.getString("Messages","Prefix") + Language.getString("Messages","CannotAfford").replaceAll("<Item>", ite.getItemMeta().getDisplayName()));
                                            }
                                        } else {
                                            if (!Language.getString("Messages","CannotAfford").contains("<Item>")) {
                                                p.sendMessage(Language.getString("Messages","Prefix") + Language.getString("Messages","CannotAfford") + WordsCapitalizer.capitalizeEveryWord(ite.getType().name().replaceAll("_", " ")));
                                            } else {
                                                p.sendMessage(Language.getString("Messages","Prefix") + Language.getString("Messages","CannotAfford").replaceAll("<Item>", WordsCapitalizer.capitalizeEveryWord(ite.getType().name().replaceAll("_", " "))));
                                            }
                                        }
                                        p.closeInventory();
                                        return;
                                    }
                                } else {
                                    if (it.getStock() > 0 || it.getStock() <= 0 && it.isInfinite()) {
                                        if (Core.getEconomy().getBalance(p) >= it.getPrice() * it.getStock()) {
                                            double value;
                                            if (!shop.isServerShop()) {
                                                BigDecimal bd = new BigDecimal((it.getPrice() / it.getAmount()) * amt);
                                                bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
                                                Core.getEconomy().withdrawPlayer(Bukkit.getOfflinePlayer(p.getUniqueId()), bd.doubleValue());
                                                Core.getEconomy().depositPlayer(Bukkit.getOfflinePlayer(shop.getOwner().getUniqueId()), bd.doubleValue());
                                                t = t + bd.doubleValue();
                                                value = bd.doubleValue();
                                                if (shop.isNotify()) {
                                                    if (shop.getOwner() != null && shop.getOwner().isOnline()) {
                                                        shop.getOwner().getPlayer().sendMessage(Language.getString("Messages","Prefix") + Language.getString("Messages","NotifyBuy").replaceAll("<Player>", p.getDisplayName()));
                                                        shop.getOwner().getPlayer().sendMessage(Language.getString("Messages","Prefix") + Language.getString("Messages","ReceivedAmount").replaceAll("<Amount>", "" + bd.doubleValue()));

                                                        if (Core.isAboveEight() && (boolean) Config.getObject("Titles") && Core.getTitleManager() != null) {

                                                            Core.getTitleManager().setTimes(shop.getOwner().getPlayer(), 20, 60, 20);
                                                            Core.getTitleManager().sendTitle(shop.getOwner().getPlayer(), Language.getString("Messages","NotifyBuy").replaceAll("<Player>", p.getDisplayName()));
                                                            Core.getTitleManager().sendSubTitle(shop.getOwner().getPlayer(), Language.getString("Messages","ReceivedAmount").replaceAll("<Amount>", "" + bd.doubleValue()));


                                                        }
                                                    }
                                                }
                                            } else {
                                                BigDecimal bd = new BigDecimal((it.getPrice() / it.getAmount()) * amt);
                                                bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
                                                Core.getEconomy().withdrawPlayer(Bukkit.getOfflinePlayer(p.getUniqueId()), bd.doubleValue());
                                                t = t + bd.doubleValue();
                                                value = bd.doubleValue();
                                            }

                                            Stocks.addItemsToInventory(it, p, amt);

//                                        for (int o = 0; o < amt; o++) {
//                                            p.getInventory().addItem(it.getItem());
//                                        }

                                            if (!it.isInfinite()) {
                                                if (it.getDisplayName() != null) {
                                                    p.sendMessage(Language.getString("Messages","Prefix") + Language.getString("Messages","CanOnlyBuy") + it.getStock() + " §cof §d" + it.getDisplayName());
                                                } else {
                                                    p.sendMessage(Language.getString("Messages","Prefix") + Language.getString("Messages","CanOnlyBuy") + it.getStock() + " §cof §d" + it.getItem().getType().name().replaceAll("_", " "));
                                                }
                                            }

                                            if (!it.isInfinite())
                                                it.setObject("Stock",0);

                                            if (shop.getHistory() == null) {
                                                shop.loadTransactions();
                                            }
                                            shop.getHistory().addTransaction(p, new Date(), it, value, amt, false, true);
//
                                            ShopBuyItemEvent ev = new ShopBuyItemEvent(it, shop, p);

                                            Bukkit.getPluginManager().callEvent(ev);

                                            if (it.getLiveEco()) {
                                                double a = amt / it.getAmount();
                                                it.setAmountTo(it.getAmountTo() + a);
                                            }

                                        } else {
                                            if (ite.getItemMeta() != null && ite.getItemMeta().getDisplayName() != null) {
                                                if (!Language.getString("Messages","CannotAfford").contains("<Item>")) {
                                                    p.sendMessage(Language.getString("Messages","Prefix") + Language.getString("Messages","CannotAfford") + ite.getItemMeta().getDisplayName());
                                                } else {
                                                    p.sendMessage(Language.getString("Messages","Prefix") + Language.getString("Messages","CannotAfford").replaceAll("<Item>", ite.getItemMeta().getDisplayName()));
                                                }
                                            } else {
                                                if (!Language.getString("Messages","CannotAfford").contains("<Item>")) {
                                                    p.sendMessage(Language.getString("Messages","Prefix") + Language.getString("Messages","CannotAfford") + WordsCapitalizer.capitalizeEveryWord(ite.getType().name().replaceAll("_", " ")));
                                                } else {
                                                    p.sendMessage(Language.getString("Messages","Prefix") + Language.getString("Messages","CannotAfford").replaceAll("<Item>", WordsCapitalizer.capitalizeEveryWord(ite.getType().name().replaceAll("_", " "))));
                                                }
                                            }
                                            p.closeInventory();
                                            return;
                                        }
                                    } else {

                                        if (ite.getItemMeta() != null && ite.getItemMeta().getDisplayName() != null) {
                                            if (!Language.getString("Messages","OutOfStock").contains("<Item>")) {
                                                p.sendMessage(Language.getString("Messages","Prefix") + Language.getString("Messages","OutOfStock") + ite.getItemMeta().getDisplayName());
                                            } else {
                                                p.sendMessage(Language.getString("Messages","Prefix") + Language.getString("Messages","OutOfStock").replaceAll("<Item>", ite.getItemMeta().getDisplayName()));
                                            }
                                        } else {
                                            if (!Language.getString("Messages","OutOfStock").contains("<Item>")) {
                                                p.sendMessage(Language.getString("Messages","Prefix") + Language.getString("Messages","OutOfStock") + WordsCapitalizer.capitalizeEveryWord(ite.getType().name().replaceAll("_", " ")));
                                            } else {
                                                p.sendMessage(Language.getString("Messages","Prefix") + Language.getString("Messages","OutOfStock").replaceAll("<Item>", WordsCapitalizer.capitalizeEveryWord(ite.getType().name().replaceAll("_", " "))));
                                            }
                                        }
                                        p.closeInventory();
                                        return;
                                    }
                                }
                            } else {
                                p.sendMessage(Language.getString("Messages","Prefix") + Language.getString("Messages", "NoPermissionItem").replaceAll("<Item>", it.getItem().getType().name()));
                            }
                        }
                    }
                }
            }
        }
        p.sendMessage(Language.getString("Messages","Prefix") + Language.getString("Messages","BuyItem"));
        p.sendMessage(Language.getString("Messages","Prefix") + Language.getString("Messages","TakenAmount").replaceAll("<Amount>", "" + t));


        if (Core.isAboveEight() && (boolean) Config.getObject("Titles") && Core.getTitleManager() != null) {

            p.closeInventory();

            Core.getTitleManager().setTimes(p, 20, 60, 20);
            Core.getTitleManager().sendTitle(p, Language.getString("Messages","BuyItem"));
            Core.getTitleManager().sendSubTitle(p, Language.getString("Messages","TakenAmount").replaceAll("<Amount>", "" + t));

            p.closeInventory();

        } else {
            shop.getMenu(MenuType.MAIN_BUYING).draw(p,1);
        }
        cart.remove(k);

        if (shop.isHoloShop()) {
            ShopHologram holo = shop.getHolographicShop();
            holo.updateItemLines(holo.getItemLine(), false);
        }
    }
}
