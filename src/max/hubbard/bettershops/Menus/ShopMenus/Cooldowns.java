package max.hubbard.bettershops.Menus.ShopMenus;

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
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2015. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class Cooldowns implements ShopMenu {
    Shop shop;
    Inventory inv;


    // READ THISSSSS!!@!$!#@#%$#@@#$%
    // Keep track of with Transactions
    // No need for a new file

    public Cooldowns(Shop shop) {
        this.shop = shop;
        inv = Bukkit.createInventory(null, 54, Language.getString("MainGUI", "ShopHeader") + shop.getName());
    }

    @Override
    public MenuType getType() {
        return MenuType.COOLDOWNS;
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
        if (shopItem.isTransCooldown()) {
            nam = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 5);
            namMeta.setDisplayName(Language.getString("Timings", "TransCoolOn"));
        } else {
            namMeta.setDisplayName(Language.getString("Timings", "TransCoolOff"));
        }
        namMeta.setLore(Arrays.asList(Language.getString("Timings", "TransCoolToggle")));
        nam.setItemMeta(namMeta);
        ClickableItem notifyClick = new ClickableItem(new ShopItemStack(nam), inv, p);
        notifyClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                shopItem.setObject("Trans", !shopItem.isTransCooldown());
                if (shopItem.isTransCooldown()) {
                    shopItem.getTransCooldownTiming().startTime();
                } else {
                    shopItem.getTransCooldownTiming().stop();
                }
                shopItem.setObject("TransCool", shopItem.getTransCooldownTiming().toString());
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


        ItemStack autoStock = new ItemStack(Material.EMERALD, (int) shopItem.getTransCooldownTiming().getObject());
        ItemMeta autoStockMeta = autoStock.getItemMeta();
        autoStockMeta.setDisplayName(Language.getString("Timings", "TransactionAmount") + (int) shopItem.getTransCooldownTiming().getObject());
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

                        try {
                            amt = Integer.parseInt(name);
                        } catch (Exception ex) {

                            p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "InvalidNumber"));
                            draw(p, page, obj);
                            return;

                        }

                        shopItem.getTransCooldownTiming().setObject(amt);

                        draw(p, page, obj);
                    }
                });
            }
        });

        // Days
        ItemStack days = new ItemStack(Material.IRON_FENCE, shopItem.getTransCooldownTiming().getDays());
        ItemMeta daysMeta = days.getItemMeta();
        daysMeta.setDisplayName(Language.getString("Timings", "Days") + shopItem.getTransCooldownTiming().getDays());
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
                shopItem.getTransCooldownTiming().setDays(shopItem.getTransCooldownTiming().getDays() + 1);
                shopItem.setObject("AutoStock", shopItem.getTransCooldownTiming().toString());
                draw(p, page, obj);
            }
        });
        addDaysClick.addRightClickAction(new RightClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                shopItem.getTransCooldownTiming().setDays(shopItem.getTransCooldownTiming().getDays() + 30);
                shopItem.setObject("AutoStock", shopItem.getTransCooldownTiming().toString());
                draw(p, page, obj);
            }
        });
        addDaysClick.addShiftClickAction(new ShiftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                shopItem.getTransCooldownTiming().setDays(shopItem.getTransCooldownTiming().getDays() + 60);
                shopItem.setObject("AutoStock", shopItem.getTransCooldownTiming().toString());
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
                if (shopItem.getTransCooldownTiming().getDays() - 1 >= 0) {
                    shopItem.getTransCooldownTiming().setDays(shopItem.getTransCooldownTiming().getDays() - 1);
                } else {
                    shopItem.getTransCooldownTiming().setDays(0);
                }
                shopItem.setObject("AutoStock", shopItem.getTransCooldownTiming().toString());
                draw(p, page, obj);
            }
        });
        removeDaysClick.addRightClickAction(new RightClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                if (shopItem.getTransCooldownTiming().getDays() - 30 >= 0) {
                    shopItem.getTransCooldownTiming().setDays(shopItem.getTransCooldownTiming().getDays() - 30);
                } else {
                    shopItem.getTransCooldownTiming().setDays(0);
                }
                shopItem.setObject("AutoStock", shopItem.getTransCooldownTiming().toString());
                draw(p, page, obj);
            }
        });
        removeDaysClick.addShiftClickAction(new ShiftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                if (shopItem.getTransCooldownTiming().getDays() - 60 >= 0) {
                    shopItem.getTransCooldownTiming().setDays(shopItem.getTransCooldownTiming().getDays() - 60);
                } else {
                    shopItem.getTransCooldownTiming().setDays(0);
                }
                shopItem.setObject("AutoStock", shopItem.getTransCooldownTiming().toString());
                draw(p, page, obj);
            }
        });


        // Hours

        ItemStack hours = new ItemStack(Material.IRON_FENCE, shopItem.getTransCooldownTiming().getHours());
        ItemMeta hoursMeta = hours.getItemMeta();
        hoursMeta.setDisplayName(Language.getString("Timings", "Hours") + shopItem.getTransCooldownTiming().getHours());
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
                if (shopItem.getTransCooldownTiming().getHours() + 1 >= 24) {
                    int dif = (shopItem.getTransCooldownTiming().getHours() + 1) - 24;
                    shopItem.getTransCooldownTiming().setHours(dif);
                } else {
                    shopItem.getTransCooldownTiming().setHours(shopItem.getTransCooldownTiming().getHours() + 1);
                }
                shopItem.setObject("AutoStock", shopItem.getTransCooldownTiming().toString());
                draw(p, page, obj);
            }
        });
        addHoursClick.addRightClickAction(new RightClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                if (shopItem.getTransCooldownTiming().getHours() + 6 >= 24) {
                    int dif = (shopItem.getTransCooldownTiming().getHours() + 6) - 24;
                    shopItem.getTransCooldownTiming().setHours(dif);
                } else {
                    shopItem.getTransCooldownTiming().setHours(shopItem.getTransCooldownTiming().getHours() + 6);
                }
                shopItem.setObject("AutoStock", shopItem.getTransCooldownTiming().toString());
                draw(p, page, obj);
            }
        });
        addHoursClick.addShiftClickAction(new ShiftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                if (shopItem.getTransCooldownTiming().getHours() + 12 >= 24) {
                    int dif = (shopItem.getTransCooldownTiming().getHours() + 12) - 24;
                    shopItem.getTransCooldownTiming().setHours(dif);
                } else {
                    shopItem.getTransCooldownTiming().setHours(shopItem.getTransCooldownTiming().getHours() + 12);
                }
                shopItem.setObject("AutoStock", shopItem.getTransCooldownTiming().toString());
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
                if (shopItem.getTransCooldownTiming().getHours() - 1 >= 0) {
                    shopItem.getTransCooldownTiming().setHours(shopItem.getTransCooldownTiming().getHours() - 1);
                } else {
                    shopItem.getTransCooldownTiming().setHours(0);
                }
                shopItem.setObject("AutoStock", shopItem.getTransCooldownTiming().toString());
                draw(p, page, obj);
            }
        });
        removeHoursClick.addRightClickAction(new RightClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                if (shopItem.getTransCooldownTiming().getHours() - 6 >= 0) {
                    shopItem.getTransCooldownTiming().setHours(shopItem.getTransCooldownTiming().getHours() - 6);
                } else {
                    shopItem.getTransCooldownTiming().setHours(0);
                }
                shopItem.setObject("AutoStock", shopItem.getTransCooldownTiming().toString());
                draw(p, page, obj);
            }
        });
        removeHoursClick.addShiftClickAction(new ShiftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                if (shopItem.getTransCooldownTiming().getHours() - 12 >= 0) {
                    shopItem.getTransCooldownTiming().setHours(shopItem.getTransCooldownTiming().getHours() - 12);
                } else {
                    shopItem.getTransCooldownTiming().setHours(0);
                }
                shopItem.setObject("AutoStock", shopItem.getTransCooldownTiming().toString());
                draw(p, page, obj);
            }
        });

        // Minutes

        ItemStack minutes = new ItemStack(Material.IRON_FENCE, shopItem.getTransCooldownTiming().getMinutes());
        ItemMeta minutesMeta = minutes.getItemMeta();
        minutesMeta.setDisplayName(Language.getString("Timings", "Minutes") + shopItem.getTransCooldownTiming().getMinutes());
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
                if (shopItem.getTransCooldownTiming().getMinutes() + 1 >= 60) {
                    int dif = (shopItem.getTransCooldownTiming().getMinutes() + 1) - 60;
                    shopItem.getTransCooldownTiming().setMinutes(dif);
                } else {
                    shopItem.getTransCooldownTiming().setMinutes(shopItem.getTransCooldownTiming().getMinutes() + 1);
                }
                shopItem.setObject("AutoStock", shopItem.getTransCooldownTiming().toString());
                draw(p, page, obj);
            }
        });
        addMinutesClick.addRightClickAction(new RightClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                if (shopItem.getTransCooldownTiming().getMinutes() + 30 >= 60) {
                    int dif = (shopItem.getTransCooldownTiming().getMinutes() + 30) - 60;
                    shopItem.getTransCooldownTiming().setMinutes(dif);
                } else {
                    shopItem.getTransCooldownTiming().setMinutes(shopItem.getTransCooldownTiming().getMinutes() + 30);
                }
                shopItem.setObject("AutoStock", shopItem.getTransCooldownTiming().toString());
                draw(p, page, obj);
            }
        });
        addMinutesClick.addShiftClickAction(new ShiftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                if (shopItem.getTransCooldownTiming().getMinutes() + 60 >= 60) {
                    int dif = (shopItem.getTransCooldownTiming().getMinutes() + 60) - 60;
                    shopItem.getTransCooldownTiming().setMinutes(dif);
                } else {
                    shopItem.getTransCooldownTiming().setMinutes(shopItem.getTransCooldownTiming().getMinutes() + 60);
                }
                shopItem.setObject("AutoStock", shopItem.getTransCooldownTiming().toString());
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
                if (shopItem.getTransCooldownTiming().getMinutes() - 1 >= 0) {
                    shopItem.getTransCooldownTiming().setMinutes(shopItem.getTransCooldownTiming().getMinutes() - 1);
                } else {
                    shopItem.getTransCooldownTiming().setMinutes(0);
                }
                shopItem.setObject("AutoStock", shopItem.getTransCooldownTiming().toString());
                draw(p, page, obj);
            }
        });
        removeMinutesClick.addRightClickAction(new RightClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                if (shopItem.getTransCooldownTiming().getMinutes() - 30 >= 0) {
                    shopItem.getTransCooldownTiming().setMinutes(shopItem.getTransCooldownTiming().getMinutes() - 30);
                } else {
                    shopItem.getTransCooldownTiming().setMinutes(0);
                }
                shopItem.setObject("AutoStock", shopItem.getTransCooldownTiming().toString());
                draw(p, page, obj);
            }
        });
        removeMinutesClick.addShiftClickAction(new ShiftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                if (shopItem.getTransCooldownTiming().getMinutes() - 60 >= 0) {
                    shopItem.getTransCooldownTiming().setMinutes(shopItem.getTransCooldownTiming().getMinutes() - 60);
                } else {
                    shopItem.getTransCooldownTiming().setMinutes(0);
                }
                shopItem.setObject("AutoStock", shopItem.getTransCooldownTiming().toString());
                draw(p, page, obj);
            }
        });

        // Seconds

        ItemStack seconds = new ItemStack(Material.IRON_FENCE, shopItem.getTransCooldownTiming().getSeconds());
        ItemMeta secondsMeta = seconds.getItemMeta();
        secondsMeta.setDisplayName(Language.getString("Timings", "Seconds") + shopItem.getTransCooldownTiming().getSeconds());
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
                if (shopItem.getTransCooldownTiming().getSeconds() + 1 >= 60) {
                    int dif = (shopItem.getTransCooldownTiming().getSeconds() + 1) - 60;
                    shopItem.getTransCooldownTiming().setSeconds(dif);
                } else {
                    shopItem.getTransCooldownTiming().setSeconds(shopItem.getTransCooldownTiming().getSeconds() + 1);
                }
                shopItem.setObject("AutoStock", shopItem.getTransCooldownTiming().toString());
                draw(p, page, obj);
            }
        });
        addSecondsClick.addRightClickAction(new RightClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                if (shopItem.getTransCooldownTiming().getSeconds() + 30 >= 60) {
                    int dif = (shopItem.getTransCooldownTiming().getSeconds() + 30) - 60;
                    shopItem.getTransCooldownTiming().setSeconds(dif);
                } else {
                    shopItem.getTransCooldownTiming().setSeconds(shopItem.getTransCooldownTiming().getSeconds() + 30);
                }
                shopItem.setObject("AutoStock", shopItem.getTransCooldownTiming().toString());
                draw(p, page, obj);
            }
        });
        addSecondsClick.addShiftClickAction(new ShiftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                if (shopItem.getTransCooldownTiming().getSeconds() + 60 >= 60) {
                    int dif = (shopItem.getTransCooldownTiming().getSeconds() + 60) - 60;
                    shopItem.getTransCooldownTiming().setSeconds(dif);
                } else {
                    shopItem.getTransCooldownTiming().setSeconds(shopItem.getTransCooldownTiming().getSeconds() + 60);
                }
                shopItem.setObject("AutoStock", shopItem.getTransCooldownTiming().toString());
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
                if (shopItem.getTransCooldownTiming().getSeconds() - 1 >= 0) {
                    shopItem.getTransCooldownTiming().setSeconds(shopItem.getTransCooldownTiming().getSeconds() - 1);
                } else {
                    shopItem.getTransCooldownTiming().setSeconds(0);
                }
                shopItem.setObject("AutoStock", shopItem.getTransCooldownTiming().toString());
                draw(p, page, obj);
            }
        });
        removeSecondsClick.addRightClickAction(new RightClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                if (shopItem.getTransCooldownTiming().getSeconds() - 30 >= 0) {
                    shopItem.getTransCooldownTiming().setSeconds(shopItem.getTransCooldownTiming().getSeconds() - 30);
                } else {
                    shopItem.getTransCooldownTiming().setSeconds(0);
                }
                shopItem.setObject("AutoStock", shopItem.getTransCooldownTiming().toString());
                draw(p, page, obj);
            }
        });
        removeSecondsClick.addShiftClickAction(new ShiftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                if (shopItem.getTransCooldownTiming().getSeconds() - 60 >= 0) {
                    shopItem.getTransCooldownTiming().setSeconds(shopItem.getTransCooldownTiming().getSeconds() - 60);
                } else {
                    shopItem.getTransCooldownTiming().setSeconds(0);
                }
                shopItem.setObject("AutoStock", shopItem.getTransCooldownTiming().toString());
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

    public static boolean canTransaction(Player p, ShopItem item, int am) {
        if (item.getObject("Cooldowns") != null) {
            String[] split = ((String) item.getObject("Cooldowns")).split(Pattern.quote("|"));
            for (String s : split) {
                String[] sp = s.split(Pattern.quote(","));

                if (p.getUniqueId().toString().equals(sp[0])) {
                    int amt = Integer.parseInt(sp[1]);
                    return amt + am <= item.getTransCooldownTiming().getObject();
                }
            }
        }

        return true;
    }

    public static double getAmount(Player p, ShopItem item) {
        if (item.getObject("Cooldowns") != null) {
            String[] split = ((String) item.getObject("Cooldowns")).split(Pattern.quote("|"));
            for (String s : split) {
                String[] sp = s.split(Pattern.quote(","));

                if (p.getUniqueId().toString().equals(sp[0])) {
                    if (item.getTransCooldownTiming().getObject() - Integer.parseInt(sp[1]) < 0) return 0;

                    return item.getTransCooldownTiming().getObject() - Integer.parseInt(sp[1]);
                }
            }
        }
        return item.getTransCooldownTiming().getObject();
    }

    public static void addAmount(Player p, ShopItem item, int amt) {
        HashMap<String, Integer> a = new HashMap<>();
        String g = "";
        if (item.getObject("Cooldowns") != null) {
            String[] split = ((String) item.getObject("Cooldowns")).split(Pattern.quote("|"));
            for (String s : split) {
                String[] sp = s.split(Pattern.quote(","));

                if (sp.length > 1) {
                    a.put(sp[0], Integer.parseInt(sp[1]));
                }
            }

            if (a.size() > 0) {

                for (String s : a.keySet()) {
                    if (s.equals(p.getUniqueId().toString())) {
                        g = g + "|" + s + "," + (a.get(s) + amt);
                    } else {
                        g = g + "|" + s + "," + a.get(s);
                    }

                }
            } else {
                g = g + "|" + p.getUniqueId().toString() + "," + amt;
            }

        } else {
            g = p.getUniqueId().toString() + "," + amt;
        }

        item.setObject("Cooldowns", g);
    }
}
