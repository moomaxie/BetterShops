package max.hubbard.bettershops.Menus.ShopMenus;

import max.hubbard.bettershops.Configurations.Language;
import max.hubbard.bettershops.Configurations.Permissions;
import max.hubbard.bettershops.Menus.MenuType;
import max.hubbard.bettershops.Menus.ShopMenu;
import max.hubbard.bettershops.Shops.Items.Actions.*;
import max.hubbard.bettershops.Shops.Items.ShopItem;
import max.hubbard.bettershops.Shops.Shop;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
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
public class OwnerBuying implements ShopMenu {

    Shop shop;
    Inventory inv;

    public OwnerBuying(Shop shop) {
        this.shop = shop;
        inv = Bukkit.createInventory(null, 54, Language.getString("MainGUI", "ShopHeader") + shop.getName());
    }

    @Override
    public MenuType getType() {
        return MenuType.OWNER_BUYING;
    }

    @Override
    public Shop getAttachedShop() {
        return shop;
    }

    @Override
    public void draw(final Player p, final int page, final Object... obj) {
        inv.clear();

        ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, shop.getFrameColor());
        ItemMeta m = glass.getItemMeta();
        m.setDisplayName(" ");
        glass.setItemMeta(m);

        for (int i = 0; i < 18; i++) {
            inv.setItem(i, glass);
        }

        ItemStack options = new ItemStack(Material.ENDER_CHEST);
        ItemMeta optionsMeta = options.getItemMeta();
        if (shop.isOpen()) {
            optionsMeta.setDisplayName(Language.getString("MainGUI", "ShopInfoDisplayNameOpen"));
        } else {
            optionsMeta.setDisplayName(Language.getString("MainGUI", "ShopInfoDisplayNameClosed"));
        }
        if (Permissions.hasArrangePerm(p)) {
            optionsMeta.setLore(Arrays.asList("§a" + shop.getObject("Name"), "§7" + shop.getObject("Description"), " ", Language.getString("MainGUI", "BuyingShop"), " ", Language.getString("MainGUI", "Owner") + " §a" + shop.getOwner().getName(),
                    Language.getString("MainGUI", "Keepers") + " §7" + shop.getKeepers().size(), " ", Language.getString("MainGUI", "OpenShopSettings"), Language.getString("MainGUI", "ToggleOpenAndClosed"), Language.getString("MainGUI", "ManageItems"),
                    Language.getString("MainGUI", "AddItemToShop"), " ", Language.getString("MainGUI", "ArrangementMode")));
            options.setItemMeta(optionsMeta);
        } else {
            optionsMeta.setLore(Arrays.asList("§a" + shop.getObject("Name"), "§7" + shop.getObject("Description"), " ", Language.getString("MainGUI", "BuyingShop"), " ", Language.getString("MainGUI", "Owner") + " §a" + shop.getOwner().getName(),
                    Language.getString("MainGUI", "Keepers") + " §7" + shop.getKeepers().size(), " ", Language.getString("MainGUI", "OpenShopSettings"), Language.getString("MainGUI", "ToggleOpenAndClosed"), Language.getString("MainGUI", "ManageItems"),
                    Language.getString("MainGUI", "AddItemToShop")));
            options.setItemMeta(optionsMeta);
        }
        ClickableItem optionsClick = new ClickableItem(new ShopItemStack(options), inv, p);
        optionsClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                shop.setOpen(!shop.isOpen());
                draw(p, page, obj);
            }
        });
        optionsClick.addRightClickAction(new RightClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                shop.getMenu(MenuType.SHOP_SETTINGS).draw(p, page, obj);
            }
        });
        if (Permissions.hasArrangePerm(p)) {
            optionsClick.addShiftClickAction(new ShiftClickAction() {
                @Override
                public void onAction(InventoryClickEvent e) {
                    shop.getMenu(MenuType.REARRANGE).draw(p, page, false);
                }
            });
        }

        ItemStack pg1 = new ItemStack(Material.INK_SACK, 1, (byte) 10);
        ItemMeta pg1Meta = pg1.getItemMeta();
        pg1Meta.setDisplayName(Language.getString("MainGUI", "Page") + " §7" + page);
        pg1.setItemMeta(pg1Meta);

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

        ItemStack info = new ItemStack(Material.SIGN);
        ItemMeta infoMeta = info.getItemMeta();
        infoMeta.setDisplayName(Language.getString("MainGUI", "Buying"));
        infoMeta.setLore(Arrays.asList(Language.getString("MainGUI", "ToggleShop")));
        info.setItemMeta(infoMeta);
        ClickableItem infoClick = new ClickableItem(new ShopItemStack(info), inv, p);
        infoClick.addRightClickAction(new RightClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                shop.getMenu(MenuType.OWNER_SELLING).draw(p, page, obj);
            }
        });

        ItemStack history = new ItemStack(Material.EMERALD);
        ItemMeta historyMeta = history.getItemMeta();
        historyMeta.setDisplayName(Language.getString("History", "History"));
        historyMeta.setLore(Arrays.asList(Language.getString("History", "OpenHistory")));
        history.setItemMeta(historyMeta);
        ClickableItem historyClick = new ClickableItem(new ShopItemStack(history), inv, p);
        historyClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                shop.getMenu(MenuType.HISTORY).draw(p, page, obj);
            }
        });

        ItemStack trade = new ItemStack(Material.DIAMOND);
        ItemMeta tradeMeta = trade.getItemMeta();
        tradeMeta.setDisplayName(Language.getString("Trades", "Trades"));
        tradeMeta.setLore(Arrays.asList(Language.getString("Trades", "OpenTrades")));
        trade.setItemMeta(tradeMeta);
        ClickableItem tradeClick = new ClickableItem(new ShopItemStack(trade), inv, p);
        tradeClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                shop.getMenu(MenuType.TRADING).draw(p, page, obj);
            }
        });

        ItemStack keepers = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
        SkullMeta keepersMeta = (SkullMeta) keepers.getItemMeta();
        keepersMeta.setDisplayName(Language.getString("MainGUI", "ShopKeepersDisplayName"));
        keepersMeta.setLore(Arrays.asList(Language.getString("MainGUI", "ShopKeeperLore")));
        keepersMeta.setOwner(shop.getOwner().getName());
        keepers.setItemMeta(keepersMeta);
        ClickableItem keepersClick = new ClickableItem(new ShopItemStack(keepers), inv, p);
        keepersClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                shop.getMenu(MenuType.KEEPER_MANAGER).draw(p, page, obj);
            }
        });

        inv.setItem(1, trade);
        inv.setItem(3, info);
        inv.setItem(4, options);
        inv.setItem(5, keepers);
        inv.setItem(7, history);

        inv.setItem(13, pg1);
        inv.setItem(8, arrow);

        if (page > 1) {
            inv.setItem(0, barrow);
        }

        for (final ShopItem it : shop.getShopItems()) {
            if (!it.isSelling()) {
                if (it.getPage() == page) {
                    ItemStack itemStack = it.getItem().clone();
                    List<String> lore = new ArrayList<>();
                    ItemMeta meta = itemStack.getItemMeta();
                    if (it.getLore() != null) {
                        for (String s : it.getLore()) {
                            lore.add(s);
                        }
                    }
                    if (it.isInfinite()) {
                        lore.add(Language.getString("MainGUI", "Stock") + " §7-");
                    } else {
                        lore.add(Language.getString("MainGUI", "Stock") + " §7" + it.getStock());
                    }
                    lore.add(Language.getString("MainGUI", "Amount") + " §7" + it.getAmount());
                    if (!it.getLiveEco()) {
                        if (it.getPrice() > 0) {
                            lore.add(Language.getString("MainGUI", "Price") + " §7" + it.getPriceAsString());
                        } else {
                            lore.add(Language.getString("MainGUI", "Price") + " §7" + Language.getString("MainGUI", "Free"));
                        }
                    } else {
                        if (it.getAdjustedPrice() != it.getOrigPrice()) {
                            lore.add(Language.getString("MainGUI", "Price") + " §c§m" + it.getOrigPrice() + "§a " + it.getAdjustedPriceAsString());
                        } else {
                            if (it.getPrice() > 0) {
                                lore.add(Language.getString("MainGUI", "Price") + " §7" + it.getPriceAsString());
                            } else {
                                lore.add(Language.getString("MainGUI", "Price") + " §7" + Language.getString("MainGUI", "Free"));
                            }
                        }
                    }
                    lore.add(" ");
                    lore.add(Language.getString("MainGUI", "ManageItem"));

                    meta.setLore(lore);
                    itemStack.setItemMeta(meta);
                    inv.setItem(it.getSlot(), itemStack);

                    ClickableItem itemClick = new ClickableItem(new ShopItemStack(itemStack), inv, p);
                    itemClick.addLeftClickAction(new LeftClickAction() {
                        @Override
                        public void onAction(InventoryClickEvent e) {
                            shop.getMenu(MenuType.ITEM_MANAGER_BUYING).draw(p, page, it);
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
