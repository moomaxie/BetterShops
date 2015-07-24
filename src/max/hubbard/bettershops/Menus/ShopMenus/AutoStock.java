package max.hubbard.bettershops.Menus.ShopMenus;

import max.hubbard.bettershops.Configurations.Config;
import max.hubbard.bettershops.Configurations.Language;
import max.hubbard.bettershops.Menus.MenuType;
import max.hubbard.bettershops.Menus.ShopMenu;
import max.hubbard.bettershops.Shops.Items.Actions.*;
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
public class AutoStock implements ShopMenu {

    Shop shop;
    Inventory inv;

    public AutoStock(Shop shop) {
        this.shop = shop;
        inv = Bukkit.createInventory(null, 54, Language.getString("MainGUI", "ShopHeader") + shop.getName());
    }

    @Override
    public MenuType getType() {
        return MenuType.AUTO_STOCK;
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

        final ShopItem shopItem = (ShopItem) obj[0];

        ItemStack nam = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 14);
        ItemMeta namMeta = nam.getItemMeta();
        if (shopItem.isAutoStock()) {
            nam = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 5);
            namMeta.setDisplayName(Language.getString("Timings", "AutoStockOn"));
        } else {
            namMeta.setDisplayName(Language.getString("Timings", "AutoStockOff"));
        }
        namMeta.setLore(Arrays.asList(Language.getString("Timings", "AutoStockToggle")));
        nam.setItemMeta(namMeta);
        ClickableItem notifyClick = new ClickableItem(new ShopItemStack(nam), inv, p);
        notifyClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                shopItem.setObject("Auto", !shopItem.isAutoStock());
                if (shopItem.isAutoStock()){
                    shopItem.getAutoStockTiming().startTime();
                } else {
                    shopItem.getAutoStockTiming().stop();
                }
                shopItem.setObject("AutoStock",shopItem.getAutoStockTiming().toString());
                draw(p, page, obj);
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
                if (shopItem.isSelling()) {
                    shop.getMenu(MenuType.ITEM_MANAGER_SELLING).draw(p, page, shopItem);
                } else {
                    shop.getMenu(MenuType.ITEM_MANAGER_BUYING).draw(p, page, shopItem);
                }
            }
        });


        ItemStack autoStock = new ItemStack(Material.EMERALD, (int) shopItem.getAutoStockTiming().getObject());
        ItemMeta autoStockMeta = autoStock.getItemMeta();
        autoStockMeta.setDisplayName(Language.getString("Timings", "RestockAmount") + (int) shopItem.getAutoStockTiming().getObject());
        autoStock.setItemMeta(autoStockMeta);
        ClickableItem autoStockClick = new ClickableItem(new ShopItemStack(autoStock), inv, p);
        autoStockClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                final AnvilManager man = new AnvilManager(p);
                Bukkit.getScheduler().runTaskAsynchronously(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                    @Override
                    public void run() {
                        String name = man.call();
                        int amt;
                        int limit = (int) Config.getObject("StockLimit");
                        if (limit != 0 && shopItem.getStock() >= limit) {
                            p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "HighStock"));
                            return;
                        }
                        try {
                            amt = Integer.parseInt(name);
                        } catch (Exception ex) {

                            p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "InvalidNumber"));
                            draw(p, page, obj);
                            return;

                        }


                        if (limit != 0 && amt + shopItem.getStock() > limit) {
                            shopItem.getAutoStockTiming().setObject(limit);
                        } else {
                            shopItem.getAutoStockTiming().setObject(amt);
                        }

                        draw(p, page, obj);
                    }
                });
            }
        });

        // Days
        ItemStack days = new ItemStack(Material.IRON_FENCE, shopItem.getAutoStockTiming().getDays());
        ItemMeta daysMeta = days.getItemMeta();
        daysMeta.setDisplayName(Language.getString("Timings", "Days") + shopItem.getAutoStockTiming().getDays());
        days.setItemMeta(daysMeta);

        ItemStack addDays = new ItemStack(Material.ARROW);
        ItemMeta addDaysMeta = addDays.getItemMeta();
        addDaysMeta.setDisplayName(Language.getString("Timings", "AddDays"));
        addDaysMeta.setLore(Arrays.asList(Language.getString("Timings", "AddOne"),
                Language.getString("Timings", "AddThirty"),
                Language.getString("Timings", "AddSixty")));
        addDays.setItemMeta(addDaysMeta);
        ClickableItem addDaysClick = new ClickableItem(new ShopItemStack(addDays), inv, p);
        addDaysClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                shopItem.getAutoStockTiming().setDays(shopItem.getAutoStockTiming().getDays() + 1);
                shopItem.setObject("AutoStock", shopItem.getAutoStockTiming().toString());
                draw(p, page, obj);
            }
        });
        addDaysClick.addRightClickAction(new RightClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                shopItem.getAutoStockTiming().setDays(shopItem.getAutoStockTiming().getDays() + 30);
                shopItem.setObject("AutoStock", shopItem.getAutoStockTiming().toString());
                draw(p, page, obj);
            }
        });
        addDaysClick.addShiftClickAction(new ShiftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                shopItem.getAutoStockTiming().setDays(shopItem.getAutoStockTiming().getDays() + 60);
                shopItem.setObject("AutoStock", shopItem.getAutoStockTiming().toString());
                draw(p, page, obj);
            }
        });

        ItemStack removeDays = new ItemStack(Material.ARROW);
        ItemMeta removeDaysMeta = removeDays.getItemMeta();
        removeDaysMeta.setDisplayName(Language.getString("Timings", "RemoveDays"));
        removeDaysMeta.setLore(Arrays.asList(Language.getString("Timings", "RemoveOne"),
                Language.getString("Timings", "RemoveThirty"),
                Language.getString("Timings", "RemoveSixty")));
        removeDays.setItemMeta(removeDaysMeta);
        ClickableItem removeDaysClick = new ClickableItem(new ShopItemStack(removeDays), inv, p);
        removeDaysClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                if (shopItem.getAutoStockTiming().getDays() - 1 >= 0) {
                    shopItem.getAutoStockTiming().setDays(shopItem.getAutoStockTiming().getDays() - 1);
                } else {
                    shopItem.getAutoStockTiming().setDays(0);
                }
                shopItem.setObject("AutoStock", shopItem.getAutoStockTiming().toString());
                draw(p, page, obj);
            }
        });
        removeDaysClick.addRightClickAction(new RightClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                if (shopItem.getAutoStockTiming().getDays() - 30 >= 0) {
                    shopItem.getAutoStockTiming().setDays(shopItem.getAutoStockTiming().getDays() - 30);
                } else {
                    shopItem.getAutoStockTiming().setDays(0);
                }
                shopItem.setObject("AutoStock", shopItem.getAutoStockTiming().toString());
                draw(p, page, obj);
            }
        });
        removeDaysClick.addShiftClickAction(new ShiftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                if (shopItem.getAutoStockTiming().getDays() - 60 >= 0) {
                    shopItem.getAutoStockTiming().setDays(shopItem.getAutoStockTiming().getDays() - 60);
                } else {
                    shopItem.getAutoStockTiming().setDays(0);
                }
                shopItem.setObject("AutoStock", shopItem.getAutoStockTiming().toString());
                draw(p, page, obj);
            }
        });


        // Hours

        ItemStack hours = new ItemStack(Material.IRON_FENCE, shopItem.getAutoStockTiming().getHours());
        ItemMeta hoursMeta = hours.getItemMeta();
        hoursMeta.setDisplayName(Language.getString("Timings", "Hours") + shopItem.getAutoStockTiming().getHours());
        hours.setItemMeta(hoursMeta);

        ItemStack addHours = new ItemStack(Material.ARROW);
        ItemMeta addHoursMeta = addHours.getItemMeta();
        addHoursMeta.setDisplayName(Language.getString("Timings", "AddHours"));
        addHoursMeta.setLore(Arrays.asList(Language.getString("Timings", "AddOne"),
                Language.getString("Timings", "AddSix"),
                Language.getString("Timings", "AddTwelve")));
        addHours.setItemMeta(addHoursMeta);
        ClickableItem addHoursClick = new ClickableItem(new ShopItemStack(addHours), inv, p);
        addHoursClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                if (shopItem.getAutoStockTiming().getHours() + 1 >= 24) {
                    int dif = (shopItem.getAutoStockTiming().getHours() + 1) - 24;
                    shopItem.getAutoStockTiming().setHours(dif);
                } else {
                    shopItem.getAutoStockTiming().setHours(shopItem.getAutoStockTiming().getHours() + 1);
                }
                shopItem.setObject("AutoStock", shopItem.getAutoStockTiming().toString());
                draw(p, page, obj);
            }
        });
        addHoursClick.addRightClickAction(new RightClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                if (shopItem.getAutoStockTiming().getHours() + 6 >= 24) {
                    int dif = (shopItem.getAutoStockTiming().getHours() + 6) - 24;
                    shopItem.getAutoStockTiming().setHours(dif);
                } else {
                    shopItem.getAutoStockTiming().setHours(shopItem.getAutoStockTiming().getHours() + 6);
                }
                shopItem.setObject("AutoStock", shopItem.getAutoStockTiming().toString());
                draw(p, page, obj);
            }
        });
        addHoursClick.addShiftClickAction(new ShiftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                if (shopItem.getAutoStockTiming().getHours() + 12 >= 24) {
                    int dif = (shopItem.getAutoStockTiming().getHours() + 12) - 24;
                    shopItem.getAutoStockTiming().setHours(dif);
                } else {
                    shopItem.getAutoStockTiming().setHours(shopItem.getAutoStockTiming().getHours() + 12);
                }
                shopItem.setObject("AutoStock", shopItem.getAutoStockTiming().toString());
                draw(p, page, obj);
            }
        });

        ItemStack removeHours = new ItemStack(Material.ARROW);
        ItemMeta removeHoursMeta = removeHours.getItemMeta();
        removeHoursMeta.setDisplayName(Language.getString("Timings", "RemoveHours"));
        removeHoursMeta.setLore(Arrays.asList(Language.getString("Timings", "RemoveOne"),
                Language.getString("Timings", "RemoveSix"),
                Language.getString("Timings", "RemoveTwelve")));
        removeHours.setItemMeta(removeHoursMeta);
        ClickableItem removeHoursClick = new ClickableItem(new ShopItemStack(removeHours), inv, p);
        removeHoursClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                if (shopItem.getAutoStockTiming().getHours() - 1 >= 0) {
                    shopItem.getAutoStockTiming().setHours(shopItem.getAutoStockTiming().getHours() - 1);
                } else {
                    shopItem.getAutoStockTiming().setHours(0);
                }
                shopItem.setObject("AutoStock", shopItem.getAutoStockTiming().toString());
                draw(p, page, obj);
            }
        });
        removeHoursClick.addRightClickAction(new RightClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                if (shopItem.getAutoStockTiming().getHours() - 6 >= 0) {
                    shopItem.getAutoStockTiming().setHours(shopItem.getAutoStockTiming().getHours() - 6);
                } else {
                    shopItem.getAutoStockTiming().setHours(0);
                }
                shopItem.setObject("AutoStock", shopItem.getAutoStockTiming().toString());
                draw(p, page, obj);
            }
        });
        removeHoursClick.addShiftClickAction(new ShiftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                if (shopItem.getAutoStockTiming().getHours() - 12 >= 0) {
                    shopItem.getAutoStockTiming().setHours(shopItem.getAutoStockTiming().getHours() - 12);
                } else {
                    shopItem.getAutoStockTiming().setHours(0);
                }
                shopItem.setObject("AutoStock", shopItem.getAutoStockTiming().toString());
                draw(p, page, obj);
            }
        });

        // Minutes

        ItemStack minutes = new ItemStack(Material.IRON_FENCE, shopItem.getAutoStockTiming().getMinutes());
        ItemMeta minutesMeta = minutes.getItemMeta();
        minutesMeta.setDisplayName(Language.getString("Timings", "Minutes") + shopItem.getAutoStockTiming().getMinutes());
        minutes.setItemMeta(minutesMeta);

        ItemStack addMinutes = new ItemStack(Material.ARROW);
        ItemMeta addMinutesMeta = addMinutes.getItemMeta();
        addMinutesMeta.setDisplayName(Language.getString("Timings", "AddMinutes"));
        addMinutesMeta.setLore(Arrays.asList(Language.getString("Timings", "AddOne"),
                Language.getString("Timings", "AddThirty"),
                Language.getString("Timings", "AddSixty")));
        addMinutes.setItemMeta(addMinutesMeta);
        ClickableItem addMinutesClick = new ClickableItem(new ShopItemStack(addMinutes), inv, p);
        addMinutesClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                if (shopItem.getAutoStockTiming().getMinutes() + 1 >= 60) {
                    int dif = (shopItem.getAutoStockTiming().getMinutes() + 1) - 60;
                    shopItem.getAutoStockTiming().setMinutes(dif);
                } else {
                    shopItem.getAutoStockTiming().setMinutes(shopItem.getAutoStockTiming().getMinutes() + 1);
                }
                shopItem.setObject("AutoStock", shopItem.getAutoStockTiming().toString());
                draw(p, page, obj);
            }
        });
        addMinutesClick.addRightClickAction(new RightClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                if (shopItem.getAutoStockTiming().getMinutes() + 30 >= 60) {
                    int dif = (shopItem.getAutoStockTiming().getMinutes() + 30) - 60;
                    shopItem.getAutoStockTiming().setMinutes(dif);
                } else {
                    shopItem.getAutoStockTiming().setMinutes(shopItem.getAutoStockTiming().getMinutes() + 30);
                }
                shopItem.setObject("AutoStock", shopItem.getAutoStockTiming().toString());
                draw(p, page, obj);
            }
        });
        addMinutesClick.addShiftClickAction(new ShiftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                if (shopItem.getAutoStockTiming().getMinutes() + 60 >= 60) {
                    int dif = (shopItem.getAutoStockTiming().getMinutes() + 60) - 60;
                    shopItem.getAutoStockTiming().setMinutes(dif);
                } else {
                    shopItem.getAutoStockTiming().setMinutes(shopItem.getAutoStockTiming().getMinutes() + 60);
                }
                shopItem.setObject("AutoStock", shopItem.getAutoStockTiming().toString());
                draw(p, page, obj);
            }
        });

        ItemStack removeMinutes = new ItemStack(Material.ARROW);
        ItemMeta removeMinutesMeta = removeMinutes.getItemMeta();
        removeMinutesMeta.setDisplayName(Language.getString("Timings", "RemoveMinutes"));
        removeMinutesMeta.setLore(Arrays.asList(Language.getString("Timings", "RemoveOne"),
                Language.getString("Timings", "RemoveThirty"),
                Language.getString("Timings", "RemoveSixty")));
        removeMinutes.setItemMeta(removeMinutesMeta);
        ClickableItem removeMinutesClick = new ClickableItem(new ShopItemStack(removeMinutes), inv, p);
        removeMinutesClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                if (shopItem.getAutoStockTiming().getMinutes() - 1 >= 0) {
                    shopItem.getAutoStockTiming().setMinutes(shopItem.getAutoStockTiming().getMinutes() - 1);
                } else {
                    shopItem.getAutoStockTiming().setMinutes(0);
                }
                shopItem.setObject("AutoStock", shopItem.getAutoStockTiming().toString());
                draw(p, page, obj);
            }
        });
        removeMinutesClick.addRightClickAction(new RightClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                if (shopItem.getAutoStockTiming().getMinutes() - 30 >= 0) {
                    shopItem.getAutoStockTiming().setMinutes(shopItem.getAutoStockTiming().getMinutes() - 30);
                } else {
                    shopItem.getAutoStockTiming().setMinutes(0);
                }
                shopItem.setObject("AutoStock", shopItem.getAutoStockTiming().toString());
                draw(p, page, obj);
            }
        });
        removeMinutesClick.addShiftClickAction(new ShiftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                if (shopItem.getAutoStockTiming().getMinutes() - 60 >= 0) {
                    shopItem.getAutoStockTiming().setMinutes(shopItem.getAutoStockTiming().getMinutes() - 60);
                } else {
                    shopItem.getAutoStockTiming().setMinutes(0);
                }
                shopItem.setObject("AutoStock", shopItem.getAutoStockTiming().toString());
                draw(p, page, obj);
            }
        });

        // Seconds

        ItemStack seconds = new ItemStack(Material.IRON_FENCE, shopItem.getAutoStockTiming().getSeconds());
        ItemMeta secondsMeta = seconds.getItemMeta();
        secondsMeta.setDisplayName(Language.getString("Timings", "Seconds") + shopItem.getAutoStockTiming().getSeconds());
        seconds.setItemMeta(secondsMeta);

        ItemStack addSeconds = new ItemStack(Material.ARROW);
        ItemMeta addSecondsMeta = addSeconds.getItemMeta();
        addSecondsMeta.setDisplayName(Language.getString("Timings", "AddSeconds"));
        addSecondsMeta.setLore(Arrays.asList(Language.getString("Timings", "AddOne"),
                Language.getString("Timings", "AddThirty"),
                Language.getString("Timings", "AddSixty")));
        addSeconds.setItemMeta(addSecondsMeta);
        ClickableItem addSecondsClick = new ClickableItem(new ShopItemStack(addSeconds), inv, p);
        addSecondsClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                if (shopItem.getAutoStockTiming().getSeconds() + 1 >= 60) {
                    int dif = (shopItem.getAutoStockTiming().getSeconds() + 1) - 60;
                    shopItem.getAutoStockTiming().setSeconds(dif);
                } else {
                    shopItem.getAutoStockTiming().setSeconds(shopItem.getAutoStockTiming().getSeconds() + 1);
                }
                shopItem.setObject("AutoStock", shopItem.getAutoStockTiming().toString());
                draw(p, page, obj);
            }
        });
        addSecondsClick.addRightClickAction(new RightClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                if (shopItem.getAutoStockTiming().getSeconds() + 30 >= 60) {
                    int dif = (shopItem.getAutoStockTiming().getSeconds() + 30) - 60;
                    shopItem.getAutoStockTiming().setSeconds(dif);
                } else {
                    shopItem.getAutoStockTiming().setSeconds(shopItem.getAutoStockTiming().getSeconds() + 30);
                }
                shopItem.setObject("AutoStock", shopItem.getAutoStockTiming().toString());
                draw(p, page, obj);
            }
        });
        addSecondsClick.addShiftClickAction(new ShiftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                if (shopItem.getAutoStockTiming().getSeconds() + 60 >= 60) {
                    int dif = (shopItem.getAutoStockTiming().getSeconds() + 60) - 60;
                    shopItem.getAutoStockTiming().setSeconds(dif);
                } else {
                    shopItem.getAutoStockTiming().setSeconds(shopItem.getAutoStockTiming().getSeconds() + 60);
                }
                shopItem.setObject("AutoStock", shopItem.getAutoStockTiming().toString());

                draw(p, page, obj);
            }
        });

        ItemStack removeSeconds = new ItemStack(Material.ARROW);
        ItemMeta removeSecondsMeta = removeSeconds.getItemMeta();
        removeSecondsMeta.setDisplayName(Language.getString("Timings", "RemoveSeconds"));
        removeSecondsMeta.setLore(Arrays.asList(Language.getString("Timings", "RemoveOne"),
                Language.getString("Timings", "RemoveThirty"),
                Language.getString("Timings", "RemoveSixty")));
        removeSeconds.setItemMeta(removeSecondsMeta);
        ClickableItem removeSecondsClick = new ClickableItem(new ShopItemStack(removeSeconds), inv, p);
        removeSecondsClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                if (shopItem.getAutoStockTiming().getSeconds() - 1 >= 0) {
                    shopItem.getAutoStockTiming().setSeconds(shopItem.getAutoStockTiming().getSeconds() - 1);
                } else {
                    shopItem.getAutoStockTiming().setSeconds(0);
                }
                shopItem.setObject("AutoStock", shopItem.getAutoStockTiming().toString());
                draw(p, page, obj);
            }
        });
        removeSecondsClick.addRightClickAction(new RightClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                if (shopItem.getAutoStockTiming().getSeconds() - 30 >= 0) {
                    shopItem.getAutoStockTiming().setSeconds(shopItem.getAutoStockTiming().getSeconds() - 30);
                } else {
                    shopItem.getAutoStockTiming().setSeconds(0);
                }
                shopItem.setObject("AutoStock", shopItem.getAutoStockTiming().toString());
                draw(p, page, obj);
            }
        });
        removeSecondsClick.addShiftClickAction(new ShiftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                if (shopItem.getAutoStockTiming().getSeconds() - 60 >= 0) {
                    shopItem.getAutoStockTiming().setSeconds(shopItem.getAutoStockTiming().getSeconds() - 60);
                } else {
                    shopItem.getAutoStockTiming().setSeconds(0);
                }
                shopItem.setObject("AutoStock", shopItem.getAutoStockTiming().toString());

                draw(p, page, obj);
            }
        });

        inv.setItem(0, back);
        inv.setItem(4, shopItem.getItem());
        inv.setItem(7, nam);
        inv.setItem(8, autoStock);

        inv.setItem(19, addDays);
        inv.setItem(21, addHours);
        inv.setItem(23, addMinutes);
        inv.setItem(25, addSeconds);

        inv.setItem(28, days);
        inv.setItem(30, hours);
        inv.setItem(32, minutes);
        inv.setItem(34, seconds);

        inv.setItem(37, removeDays);
        inv.setItem(39, removeHours);
        inv.setItem(41, removeMinutes);
        inv.setItem(43, removeSeconds);

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