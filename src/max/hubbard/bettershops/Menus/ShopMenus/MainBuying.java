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
public class MainBuying implements ShopMenu {

    Shop shop;
    Inventory inv;

    public MainBuying(Shop shop) {
        this.shop = shop;
        inv = Bukkit.createInventory(null, 54, Language.getString("MainGUI", "ShopHeader") + shop.getName());
    }

    @Override
    public MenuType getType() {
        return MenuType.MAIN_BUYING;
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
        optionsMeta.setDisplayName("§a§l" + shop.getName());

        if (!shop.isServerShop()) {
            optionsMeta.setLore(Arrays.asList("§7" + shop.getObject("Description"), " ", Language.getString("MainGUI", "Owner") + " §a§l" + shop.getOwner().getName(),
                    Language.getString("MainGUI", "Keepers") + " §7" + shop.getKeepers().size()));
            options.setItemMeta(optionsMeta);
        } else {
            if (!shop.getOwner().getUniqueId().equals(p.getUniqueId()) || !shop.getOwner().getUniqueId().toString().equals(p.getUniqueId().toString())) {
                optionsMeta.setLore(Arrays.asList("§7" + shop.getObject("Description"), " ", Language.getString("MainGUI", "Owner") + " " + Language.getString("MainGUI", "Server"),
                        Language.getString("MainGUI", "Keepers") + " §7" + shop.getKeepers().size()));
                options.setItemMeta(optionsMeta);
            } else {
                optionsMeta.setLore(Arrays.asList("§7" + shop.getObject("Description"), " ", Language.getString("MainGUI", "Owner") + " " + Language.getString("MainGUI", "Server"),
                        Language.getString("MainGUI", "Keepers") + " §7" + shop.getKeepers().size(), " ", Language.getString("MainGUI", "OpenShopSettings"), " ", Language.getString("MainGUI", "TurnOffServerShop")));
                options.setItemMeta(optionsMeta);
            }
        }

        if (Permissions.hasEditPerm(p, shop) && !p.getUniqueId().toString().equals(shop.getOwner().getUniqueId().toString())) {
            List<String> lore = optionsMeta.getLore();
            lore.add(" ");
            lore.add(Language.getString("MainGUI", "EditLore"));
            optionsMeta.setLore(lore);
            options.setItemMeta(optionsMeta);
            ClickableItem optionsClick = new ClickableItem(new ShopItemStack(options), inv, p);
            optionsClick.addShiftClickAction(new ShiftClickAction() {
                @Override
                public void onAction(InventoryClickEvent e) {
                    shop.getMenu(MenuType.OWNER_BUYING).draw(p, page, obj);
                }
            });
        }

        if (p.getUniqueId().toString().equals(shop.getOwner().getUniqueId().toString())) {
            ClickableItem optionsClick = new ClickableItem(new ShopItemStack(options), inv, p);
            optionsClick.addLeftClickAction(new LeftClickAction() {
                @Override
                public void onAction(InventoryClickEvent e) {
                    shop.setObject("Server", false);
                    shop.getMenu(MenuType.OWNER_BUYING).draw(p, page, obj);
                }
            });
            optionsClick.addRightClickAction(new RightClickAction() {
                @Override
                public void onAction(InventoryClickEvent e) {
                    shop.getMenu(MenuType.SHOP_SETTINGS).draw(p, page, obj);
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
        infoMeta.setLore(Arrays.asList(Language.getString("MainGUI", "ToggleShop"), Language.getString("MainGUI", "SearchOptions")));
        info.setItemMeta(infoMeta);
        ClickableItem infoClick = new ClickableItem(new ShopItemStack(info), inv, p);
        infoClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                shop.getMenu(MenuType.SEARCH_ENGINE).draw(p, page, false);
            }
        });
        infoClick.addRightClickAction(new RightClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                shop.getMenu(MenuType.MAIN_SELLING).draw(p, page, obj);
            }
        });

        ItemStack cart = new ItemStack(Material.CHEST);
        ItemMeta cartMeta = cart.getItemMeta();
        cartMeta.setDisplayName(Language.getString("MainGUI", "CheckoutDisplayName"));
        cartMeta.setLore(Arrays.asList(Language.getString("MainGUI", "CheckoutLore")));
        cart.setItemMeta(cartMeta);
        ClickableItem cartClick = new ClickableItem(new ShopItemStack(cart), inv, p);
        cartClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                shop.getMenu(MenuType.CART).draw(p, page, obj);
            }
        });

        ItemStack trade = new ItemStack(Material.DIAMOND);
        ItemMeta tradeMeta = trade.getItemMeta();
        tradeMeta.setDisplayName(Language.getString("Trades", "Trades"));
        tradeMeta.setLore(Arrays.asList(Language.getString("Trades", "ViewTrades")));
        trade.setItemMeta(tradeMeta);
        ClickableItem tradeClick = new ClickableItem(new ShopItemStack(trade), inv, p);
        tradeClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                shop.getMenu(MenuType.TRADE_CHOOSE).draw(p, page, obj);
            }
        });

        inv.setItem(1, trade);
        inv.setItem(3, info);
        inv.setItem(4, cart);
        inv.setItem(5, options);

        inv.setItem(13, pg1);

        if (page > 1) {
            inv.setItem(0, barrow);
        }
        inv.setItem(8, arrow);

        if (obj.length == 0) {

            for (final ShopItem it : shop.getShopItems()) {
                if (!it.isSelling()) {
                    if (it.getPage() == page) {
                        final ItemStack itemStack = it.getItem().clone();
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
                        lore.add(Language.getString("MainGUI", "LeftClickToBuy"));
                        lore.add(Language.getString("MainGUI", "AddToCart"));

                        if (shop.getKeepers().contains(p)) {
                            lore.add(Language.getString("MainGUI", "ShopKeeperManage"));

                        }
                        meta.setLore(lore);
                        itemStack.setItemMeta(meta);
                        inv.setItem(it.getSlot(), itemStack);

                        ClickableItem itemClick = new ClickableItem(new ShopItemStack(itemStack), inv, p);
                        itemClick.addLeftClickAction(new LeftClickAction() {
                            @Override
                            public void onAction(InventoryClickEvent e) {
                                shop.getMenu(MenuType.BUY_ITEM).draw(p, page, itemStack, it);
                            }
                        });
                        itemClick.addShiftClickAction(new ShiftClickAction() {
                            @Override
                            public void onAction(InventoryClickEvent e) {
                                shop.getMenu(MenuType.AMOUNT_CHOOSER).draw(p, page, it, 0);
                            }
                        });

                        if (shop.getKeepers().contains(p)) {
                            itemClick.addRightClickAction(new RightClickAction() {
                                @Override
                                public void onAction(InventoryClickEvent e) {
                                    shop.getMenu(MenuType.KEEPER_ITEM_MANAGER).draw(p, page, it);
                                }
                            });
                        }
                    }
                }
            }
        } else {
            for (final ShopItem it : shop.getShopItems()) {
                if (inv.firstEmpty() != -1) {
                    if (!it.isSelling()) {

                        if (obj[0] != null) {
                            if (it.getPrice() != (int) obj[0]) {
                                continue;
                            }
                        }
                        if (obj[1] != null) {
                            if (it.getDisplayName() == null || !it.getDisplayName().equals(obj[1])) {
                                continue;
                            }
                        }
                        if (obj[2] != null) {
                            if (!it.getItem().getType().name().contains(obj[2].toString().toUpperCase().replaceAll(" ", "_"))) {
                                continue;
                            }
                        }
                        if (obj[3] != null) {
                            if (it.getItem().getTypeId() != (int) obj[3]) {
                                continue;
                            }
                        }

                        final ItemStack itemStack = it.getItem().clone();
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
                        lore.add(Language.getString("MainGUI", "LeftClickToBuy"));
                        lore.add(Language.getString("MainGUI", "AddToCart"));

                        if (shop.getKeepers().contains(p)) {
                            lore.add(Language.getString("MainGUI", "ShopKeeperManage"));

                        }
                        meta.setLore(lore);
                        itemStack.setItemMeta(meta);
                        inv.setItem(inv.firstEmpty(), itemStack);

                        ClickableItem itemClick = new ClickableItem(new ShopItemStack(itemStack), inv, p);
                        itemClick.addLeftClickAction(new LeftClickAction() {
                            @Override
                            public void onAction(InventoryClickEvent e) {
                                shop.getMenu(MenuType.BUY_ITEM).draw(p, page, itemStack, it);
                            }
                        });
                        itemClick.addShiftClickAction(new ShiftClickAction() {
                            @Override
                            public void onAction(InventoryClickEvent e) {
                                shop.getMenu(MenuType.AMOUNT_CHOOSER).draw(p, page, it, 0);
                            }
                        });

                        if (shop.getKeepers().contains(p)) {
                            itemClick.addRightClickAction(new RightClickAction() {
                                @Override
                                public void onAction(InventoryClickEvent e) {
                                    shop.getMenu(MenuType.KEEPER_ITEM_MANAGER).draw(p, page, it);
                                }
                            });
                        }
                    }
                } else {
                    break;
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
