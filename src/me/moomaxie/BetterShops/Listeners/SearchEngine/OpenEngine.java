package me.moomaxie.BetterShops.Listeners.SearchEngine;

import me.moomaxie.BetterShops.Configurations.AnvilGUI;
import me.moomaxie.BetterShops.Configurations.GUIMessages.MainGUI;
import me.moomaxie.BetterShops.Configurations.GUIMessages.SearchEngine;
import me.moomaxie.BetterShops.Configurations.Messages;
import me.moomaxie.BetterShops.Configurations.ShopLimits;
import me.moomaxie.BetterShops.Core;
import me.moomaxie.BetterShops.Shops.Shop;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2014. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class OpenEngine implements Listener {

    @EventHandler
    public void onEngine(final InventoryClickEvent e) {
        final Player p = (Player) e.getWhoClicked();
        if (e.getInventory().getName().contains("§7[Shop]")) {
            e.setCancelled(true);

            if (e.getInventory().getType() == InventoryType.CHEST) {
                if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
                    String name = e.getInventory().getName();
                    name = name.substring(11);

                    final Shop shop = ShopLimits.fromString(p, name);

                    if (e.isLeftClick()) {

                        if (e.getCurrentItem().getItemMeta().getLore() != null && e.getCurrentItem().getItemMeta().getLore().contains("§e§lLeft Click §7to open §aSearch Options")) {
                            boolean sell = false;

                            if (e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().equals("§e§lSelling")) {
                                sell = true;
                            }

                            openSearchEngine(e.getInventory(),p, shop, sell);
                        }
                    }
                }
            }
        }
    }

    public void openSearchEngine(Inventory inv, Player p, Shop shop, boolean sel) {
        boolean same = true;
        if (inv == null) {
            same = false;
            inv = Bukkit.createInventory(p, 54, "§7[Shop]" + " §a" + shop.getName());
        } else {
            inv.clear();
        }

        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7);
        ItemMeta m = item.getItemMeta();
        m.setDisplayName(" ");
        item.setItemMeta(m);

        for (int i = 0; i < 18; i++) {
            inv.setItem(i, item);
        }

        ItemStack sell = new ItemStack(Material.SIGN);
        ItemMeta sellMeta = sell.getItemMeta();
        if (!sel) {
            sellMeta.setDisplayName(SearchEngine.getString("SearchBuyItems"));
        } else {
            sellMeta.setDisplayName(SearchEngine.getString("SearchSellItems"));
        }
        sell.setItemMeta(sellMeta);

        ItemStack nam = new ItemStack(Material.WOOL, 1, (byte) 5);
        ItemMeta namMeta = nam.getItemMeta();
        namMeta.setDisplayName(SearchEngine.getString("SearchMaterials"));
        namMeta.setLore(Arrays.asList(SearchEngine.getString("SearchMaterialsLore")));
        nam.setItemMeta(namMeta);

        ItemStack desc = new ItemStack(Material.WOOL, 1, (byte) 14);
        ItemMeta descMeta = desc.getItemMeta();
        descMeta.setDisplayName(SearchEngine.getString("SearchName"));
        descMeta.setLore(Arrays.asList(SearchEngine.getString("SearchNameLore")));
        desc.setItemMeta(descMeta);

        ItemStack stock = new ItemStack(Material.WOOL, 1, (byte) 4);
        ItemMeta stockMeta = stock.getItemMeta();
        stockMeta.setDisplayName(SearchEngine.getString("SearchId"));
        stockMeta.setLore(Arrays.asList(SearchEngine.getString("SearchIdLore")));
        stock.setItemMeta(stockMeta);

        ItemStack amount = new ItemStack(Material.WOOL, 1, (byte) 2);
        ItemMeta amountMeta = amount.getItemMeta();
        amountMeta.setDisplayName(SearchEngine.getString("SearchPrice"));
        amountMeta.setLore(Arrays.asList(SearchEngine.getString("SearchPriceLore")));
        amount.setItemMeta(amountMeta);

        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName(MainGUI.getString("BackArrow"));
        back.setItemMeta(backMeta);

        inv.setItem(0, back);

        inv.setItem(4, sell);

        inv.setItem(19, nam);
        inv.setItem(21, desc);
        inv.setItem(23, stock);
        inv.setItem(25, amount);

        if (!same)
            p.openInventory(inv);
    }


    public static void useMaterialSearch(final Player p, final Shop shop, final boolean sell) {
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
                                final String name = ev.getCurrentItem().getItemMeta().getDisplayName();

                                int amt = 0;
                                boolean can = true;
                                try {
                                    amt = Integer.parseInt(name);
                                    can = false;


                                } catch (Exception ex) {
                                }

                                if (can) {
                                    Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                                        public void run() {
                                            MaterialCheck.searchByMaterial(null,p, shop, name, sell);
                                        }
                                    }, 1L);

                                } else {
                                    p.sendMessage(Messages.getPrefix() + Messages.getImproperSearch());
                                }
                            }
                        }
                    }

                } else {
                    ev.setWillClose(true);
                    ev.setWillDestroy(true);
                }
            }
        });

        ItemStack it = new ItemStack(Material.PAPER);
        ItemMeta meta = it.getItemMeta();
        meta.setDisplayName("Better Search");
        it.setItemMeta(meta);

        gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, it);

        gui.open();
    }

    public static void useNameSearch(final Player p, final Shop shop, final boolean sell) {
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
                                final String name = ev.getCurrentItem().getItemMeta().getDisplayName();

                                int amt = 0;
                                boolean can = true;
                                try {
                                    amt = Integer.parseInt(name);
                                    can = false;

                                } catch (Exception ex) {
                                }

                                if (can) {
                                    Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                                        public void run() {
                                            DisplayNameCheck.searchByName(null,p, shop, name, sell);
                                        }
                                    }, 1L);

                                } else {
                                    p.sendMessage(Messages.getPrefix() + Messages.getImproperSearch());
                                }
                            }
                        }
                    }

                } else {
                    ev.setWillClose(true);
                    ev.setWillDestroy(true);
                }
            }
        });

        ItemStack it = new ItemStack(Material.PAPER);
        ItemMeta meta = it.getItemMeta();
        meta.setDisplayName("Better Search");
        it.setItemMeta(meta);

        gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, it);

        gui.open();
    }

    public static void useIdSearch(final Player p, final Shop shop, final boolean sell) {
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
                                final String name = ev.getCurrentItem().getItemMeta().getDisplayName();

                                int amt = 0;
                                boolean can = false;
                                try {
                                    amt = Integer.parseInt(name);
                                    can = true;

                                } catch (Exception ex) {
                                }

                                if (can) {
                                    final int carl = amt;
                                    Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                                        public void run() {
                                            IdCheck.searchById(null,p, shop, carl, sell);
                                        }
                                    }, 1L);

                                } else {
                                    p.sendMessage(Messages.getPrefix() + Messages.getImproperSearch());
                                }
                            }
                        }
                    }

                } else {
                    ev.setWillClose(true);
                    ev.setWillDestroy(true);
                }
            }
        });

        ItemStack it = new ItemStack(Material.PAPER);
        ItemMeta meta = it.getItemMeta();
        meta.setDisplayName("Better Search");
        it.setItemMeta(meta);

        gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, it);

        gui.open();
    }

    public static void usePriceSearch(final Player p, final Shop shop, final boolean sell) {
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
                                final String name = ev.getCurrentItem().getItemMeta().getDisplayName();

                                double amt = 0;
                                boolean can = false;
                                try {
                                    amt = Double.parseDouble(name);
                                    can = true;

                                } catch (Exception ex) {
                                }

                                if (can) {
                                    final double carl = amt;
                                    Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                                        public void run() {
                                            PriceCheck.searchByPrice(null,p, shop, carl, sell);
                                        }
                                    }, 1L);

                                } else {
                                    p.sendMessage(Messages.getPrefix() + Messages.getImproperSearch());
                                }
                            }
                        }
                    }

                } else {
                    ev.setWillClose(true);
                    ev.setWillDestroy(true);
                }
            }
        });

        ItemStack it = new ItemStack(Material.PAPER);
        ItemMeta meta = it.getItemMeta();
        meta.setDisplayName("Better Search");
        it.setItemMeta(meta);

        gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, it);

        gui.open();
    }
}
