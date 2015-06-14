package max.hubbard.bettershops.Menus.ShopMenus;

import max.hubbard.bettershops.Configurations.Language;
import max.hubbard.bettershops.Menus.MenuType;
import max.hubbard.bettershops.Menus.ShopMenu;
import max.hubbard.bettershops.Shops.Items.Actions.ClickableItem;
import max.hubbard.bettershops.Shops.Items.Actions.LeftClickAction;
import max.hubbard.bettershops.Shops.Items.Actions.ShopItemStack;
import max.hubbard.bettershops.Shops.Items.ShopItem;
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
public class LiveEconomy implements ShopMenu {

    Shop shop;
    Inventory inv;

    public LiveEconomy(Shop shop) {
        this.shop = shop;
        inv = Bukkit.createInventory(null, 54, Language.getString("MainGUI", "ShopHeader") + shop.getName());

    }

    @Override
    public MenuType getType() {
        return MenuType.LIVE_ECONOMY;
    }

    @Override
    public Shop getAttachedShop() {
        return shop;
    }

    @Override
    public void draw(final Player p, final int page, final Object... obj) {
        inv.clear();

        final ShopItem item = (ShopItem) obj[0];

        ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 14);
        ItemMeta m = glass.getItemMeta();
        m.setDisplayName(" ");
        if (item.getLiveEco()) {
            glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 5);
        }
        glass.setItemMeta(m);
        for (int i = 0; i < 18; i++) {
            inv.setItem(i, glass);
        }

        ItemStack enable = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 14);
        ItemMeta enableMeta = enable.getItemMeta();
        if (item.getLiveEco()) {
            enableMeta.setDisplayName(Language.getString("LiveEconomy", "LiveEcoOn"));
            enable = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 5);
        } else {
            enableMeta.setDisplayName(Language.getString("LiveEconomy", "LiveEcoOff"));
        }
        enableMeta.setLore(Arrays.asList(Language.getString("LiveEconomy", "LiveEcoLore")));
        enable.setItemMeta(enableMeta);
        ClickableItem enableClick = new ClickableItem(new ShopItemStack(enable), inv, p);
        enableClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                item.setObject("LiveEconomy", !item.getLiveEco());
                if (item.getSister() == null)
                    shop.createShopItem(item.getItem(), shop.getNextSlotForPage(shop.getNextAvailablePage(!item.isSelling()), !item.isSelling()), shop.getNextAvailablePage(!item.isSelling()), !item.isSelling());
                item.getSister().setPrice(item.getAdjustedPrice() / 2);
                draw(p, page, obj);
            }
        });

        ItemStack doubleAmt = new ItemStack(Material.STAINED_CLAY, 1, (byte) 10);
        ItemMeta doubleAmtMeta = doubleAmt.getItemMeta();
        doubleAmtMeta.setDisplayName(Language.getString("LiveEconomy", "VariableAmount").replaceAll("<Value>", "" + item.getAmountToDouble()));
        doubleAmtMeta.setLore(Arrays.asList(Language.getString("LiveEconomy", "VariableLore"),
                Language.getString("LiveEconomy", "AffectPrice"),
                Language.getString("LiveEconomy", "LowNumber"),
                Language.getString("LiveEconomy", "HighNumber")));
        doubleAmt.setItemMeta(doubleAmtMeta);
        ClickableItem dbClick = new ClickableItem(new ShopItemStack(doubleAmt), inv, p);
        dbClick.addLeftClickAction(new LeftClickAction() {
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
                            if (amt > 0) {
                                item.setAmountToDouble(amt);
                            } else {
                                p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "Zero"));
                            }
                        }
                        draw(p, page, obj);
                    }
                });
            }
        });


        ItemStack percent = new ItemStack(Material.DIAMOND);
        ItemMeta percentMeta = percent.getItemMeta();
        percentMeta.setDisplayName(Language.getString("LiveEconomy", "PriceChange").replaceAll("<Value>", item.getPriceChangePercent() + "%"));
        percent.setItemMeta(percentMeta);

        ItemStack info = new ItemStack(Material.SIGN);
        ItemMeta infoMeta = info.getItemMeta();
        infoMeta.setDisplayName(Language.getString("LiveEconomy", "Information"));
        infoMeta.setLore(Arrays.asList(Language.getString("LiveEconomy", "Info1")));
        info.setItemMeta(infoMeta);

        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName(Language.getString("MainGUI", "BackArrow"));
        back.setItemMeta(backMeta);
        ClickableItem backClick = new ClickableItem(new ShopItemStack(back), inv, p);
        backClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                shop.getMenu(MenuType.ITEM_MANAGER_BUYING).draw(p, page, obj);
            }
        });

        inv.setItem(0, back);

        {
            ItemStack infor = new ItemStack(Material.MAP);
            ItemMeta inforMeta = infor.getItemMeta();
            inforMeta.setDisplayName("§e§l1.");
            inforMeta.setLore(Arrays.asList(Language.getString("LiveEconomy", "Info2")));
            infor.setItemMeta(inforMeta);
            inv.setItem(33, infor);
        }

        {
            ItemStack infor = new ItemStack(Material.MAP);
            ItemMeta inforMeta = infor.getItemMeta();
            inforMeta.setDisplayName("§e§l2.");
            inforMeta.setLore(Arrays.asList(Language.getString("LiveEconomy", "Info3")));
            infor.setItemMeta(inforMeta);
            inv.setItem(34, infor);
        }

        {
            ItemStack infor = new ItemStack(Material.MAP);
            ItemMeta inforMeta = infor.getItemMeta();
            inforMeta.setDisplayName("§e§l3.");
            inforMeta.setLore(Arrays.asList(Language.getString("LiveEconomy", "Info4")));
            infor.setItemMeta(inforMeta);
            inv.setItem(35, infor);
        }

        {
            ItemStack infor = new ItemStack(Material.MAP);
            ItemMeta inforMeta = infor.getItemMeta();
            inforMeta.setDisplayName("§e§l4.");
            inforMeta.setLore(Arrays.asList(Language.getString("LiveEconomy", "Info5")));
            infor.setItemMeta(inforMeta);
            inv.setItem(42, infor);
        }

        {
            ItemStack infor = new ItemStack(Material.MAP);
            ItemMeta inforMeta = infor.getItemMeta();
            inforMeta.setDisplayName("§e§l5.");
            inforMeta.setLore(Arrays.asList(Language.getString("LiveEconomy", "Info6")));
            infor.setItemMeta(inforMeta);
            inv.setItem(43, infor);
        }

        {
            ItemStack infor = new ItemStack(Material.MAP);
            ItemMeta inforMeta = infor.getItemMeta();
            inforMeta.setDisplayName("§e§l6.");
            inforMeta.setLore(Arrays.asList(Language.getString("LiveEconomy", "Info7")));
            infor.setItemMeta(inforMeta);
            inv.setItem(44, infor);
        }

        inv.setItem(27, enable);
        inv.setItem(25, info);
        inv.setItem(36, doubleAmt);
        inv.setItem(45, percent);
        inv.setItem(4, item.getItem());

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
