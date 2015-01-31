package me.moomaxie.BetterShops.Listeners.OwnerSellingOptions;

import me.moomaxie.BetterShops.Configurations.AnvilGUI;
import me.moomaxie.BetterShops.Configurations.Config;
import me.moomaxie.BetterShops.Configurations.Messages;
import me.moomaxie.BetterShops.Configurations.ShopLimits;
import me.moomaxie.BetterShops.Core;
import me.moomaxie.BetterShops.Listeners.Misc.ChatMessages;
import me.moomaxie.BetterShops.Listeners.SellerOptions.OpenSellShop;
import me.moomaxie.BetterShops.Shops.Shop;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2014. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class AddSellingItem implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAddItem(final InventoryClickEvent e) {

        if (e.getInventory().getType() == InventoryType.CHEST) {
            if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
                ItemStack ite = e.getCurrentItem();


               final Player p = (Player) e.getWhoClicked();

                if (e.isLeftClick()) {

                    if (e.getCurrentItem().getItemMeta() != null && e.getCurrentItem().getItemMeta().getLore() != null && e.getCurrentItem().getItemMeta().getLore().contains("§eStock:") && e.getCurrentItem().getItemMeta().getLore().contains("§eAmount:") && e.getCurrentItem().getItemMeta().getLore().contains("§ePrice:") && e.getCurrentItem().getItemMeta().getLore().contains("§e§lLeft Click §7to")) return;


                    if (p.getInventory().contains(ite)) {
                        if (p.getOpenInventory() != null && p.getOpenInventory().getTopInventory().getName().contains("§7[Shop]")) {

                            String name = p.getOpenInventory().getTopInventory().getName();
                            name = name.substring(11);

                            final Shop shop = ShopLimits.fromString(p, name);

                            int slot = e.getSlot();

                            if (shop.getOwner().getUniqueId().equals(p.getUniqueId())) {


                                if (e.getInventory().getItem(3).getItemMeta().getDisplayName() != null && e.getInventory().getItem(3).getItemMeta().getDisplayName().equals("§e§lSelling")) {

                                    ItemStack item = ite.clone();

                                    if (!shop.alreadyBeingSold(ite, true)) {
                                        if (shop.getHighestSlot(true) < 161) {

                                            if (shop.getHighestSlot(true) == 53) {
                                                shop.addItem(item, 72, true);
                                            } else if (shop.getHighestSlot(true) == 107) {
                                                shop.addItem(item, 126, true);
                                            } else if (e.getInventory().getItem(12).getData().getData() == (byte) 10) {
                                                if (e.getInventory().firstEmpty() >= 18) {
                                                    shop.addItem(item, p.getOpenInventory().getTopInventory(), true);
                                                } else {
                                                    shop.addItem(item, shop.getHighestSlot(true) + 1, true);
                                                }
                                            } else {
                                                shop.addItem(item, shop.getHighestSlot(true) + 1, true);
                                            }

                                            p.playSound(p.getLocation(), Sound.NOTE_PLING, 400, 400);

                                            int am = ite.getAmount();


                                                    if (shop.isServerShop()){
                                                        OpenSellShop.openSellerShop(e.getInventory(),p,shop,1);
                                                    } else {
                                                        OpenSellingOptions.openShopSellingOptions(e.getInventory(),p,shop,1);
                                                    }



                                            if (am > 0) {
                                                ite.setAmount(am);
                                            } else {
                                                p.getInventory().setItem(slot, new ItemStack(Material.AIR));
                                            }

                                            p.sendMessage(Messages.getPrefix() + Messages.getAddItem());


                                        } else {
                                            p.sendMessage(Messages.getPrefix() + Messages.getShopFull());
                                        }
                                    } else {
                                        p.sendMessage(Messages.getPrefix() + Messages.getAlreadyAsk());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onAdd(final InventoryClickEvent e) {
        final Player p = (Player) e.getWhoClicked();
        if (e.getInventory().getName().contains("§7[Shop]")) {
            e.setCancelled(true);

            if (e.getInventory().getType() == InventoryType.CHEST) {
                if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
                    String name = e.getInventory().getName();
                    name = name.substring(11);

                    final Shop shop = ShopLimits.fromString(p, name);


                    if (e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().equals("§e§lAdd Item")) {

                        if (Config.useAnvil()) {

                            AnvilGUI gui = Core.getAnvilGUI();
                            gui.doGUIThing(p, new AnvilGUI.AnvilClickEventHandler() {
                                @Override
                                public void onAnvilClick(AnvilGUI.AnvilClickEvent ev) {
                                    if (ev.getSlot() == 2) {
                                        ev.setWillClose(true);
                                        ev.setWillDestroy(true);

                                        boolean can = false;


                                        if (ev.getCurrentItem().getType() == Material.PAPER) {
                                            if (ev.getCurrentItem().hasItemMeta()) {
                                                if (ev.getCurrentItem().getItemMeta().getDisplayName() != null) {
                                                    String name = ev.getCurrentItem().getItemMeta().getDisplayName();

                                                    int amt;
                                                    try {
                                                        amt = Integer.parseInt(name);

                                                        ItemStack item = new ItemStack(amt);

                                                        if (item != null) {
                                                            if (!shop.alreadyBeingSold(item, true)) {

                                                                if (!shop.alreadyBeingSold(item, true)) {
                                                                    if (shop.getHighestSlot(true) < 161) {

                                                                        if (shop.getHighestSlot(true) == 53) {
                                                                            shop.addItem(item, 72, true);
                                                                        } else if (shop.getHighestSlot(true) == 107) {
                                                                            shop.addItem(item, 126, true);
                                                                        } else if (e.getInventory().getItem(12).getData().getData() == (byte) 10) {
                                                                            if (e.getInventory().firstEmpty() >= 18) {
                                                                                if (e.getInventory().firstEmpty() == 18) {
                                                                                    shop.addItem(item, 18, true);
                                                                                } else {
                                                                                    shop.addItem(item, shop.getHighestSlot(true) + 1, true);
                                                                                }
                                                                            } else {
                                                                                shop.addItem(item, shop.getHighestSlot(true) + 1, true);
                                                                            }
                                                                        } else {
                                                                            shop.addItem(item, shop.getHighestSlot(true) + 1, true);
                                                                        }

                                                                        p.playSound(p.getLocation(), Sound.NOTE_PLING, 400, 400);

                                                                        p.closeInventory();

                                                                        p.sendMessage(Messages.getPrefix() + Messages.getAddItem());

                                                                        can = true;

                                                                    } else {
                                                                        p.sendMessage(Messages.getPrefix() + Messages.getShopFull());
                                                                    }
                                                                } else {
                                                                    p.sendMessage(Messages.getPrefix() + Messages.getAlreadyAsk());
                                                                }
                                                            } else {
                                                                can = true;
                                                            }
                                                        }


                                                    } catch (Exception ex) {

                                                        if (Material.getMaterial(name.toUpperCase()) != null) {
                                                            ItemStack item = new ItemStack(Material.valueOf(name.toUpperCase()));

                                                            if (!shop.alreadyBeingSold(item, true)) {

                                                                if (!shop.alreadyBeingSold(item, true)) {
                                                                    if (shop.getHighestSlot(true) < 161) {

                                                                        if (shop.getHighestSlot(true) == 53) {
                                                                            shop.addItem(item, 72, true);
                                                                        } else if (shop.getHighestSlot(true) == 107) {
                                                                            shop.addItem(item, 126, true);
                                                                        } else if (e.getInventory().getItem(12).getData().getData() == (byte) 10) {
                                                                            if (e.getInventory().firstEmpty() >= 18) {
                                                                                if (e.getInventory().firstEmpty() == 18) {
                                                                                    shop.addItem(item, 18, true);
                                                                                } else {
                                                                                    shop.addItem(item, shop.getHighestSlot(true) + 1, true);
                                                                                }
                                                                            } else {
                                                                                shop.addItem(item, shop.getHighestSlot(true) + 1, true);
                                                                            }
                                                                        } else {
                                                                            shop.addItem(item, shop.getHighestSlot(true) + 1, true);
                                                                        }

                                                                        p.playSound(p.getLocation(), Sound.NOTE_PLING, 400, 400);

                                                                        p.closeInventory();

                                                                        p.sendMessage(Messages.getPrefix() + Messages.getAddItem());

                                                                        can = true;

                                                                    } else {
                                                                        p.sendMessage(Messages.getPrefix() + Messages.getShopFull());
                                                                    }
                                                                } else {
                                                                    p.sendMessage(Messages.getPrefix() + Messages.getAlreadyAsk());
                                                                }
                                                            } else {
                                                                can = true;
                                                            }
                                                        } else {
                                                            for (Material m : Material.values()) {
                                                                if (m.name().contains(name.toUpperCase().replaceAll(" ", "_"))) {
                                                                    ItemStack item = new ItemStack(m);

                                                                    if (!shop.alreadyBeingSold(item, true)) {

                                                                        if (!shop.alreadyBeingSold(item, true)) {
                                                                            if (shop.getHighestSlot(true) < 161) {

                                                                                if (shop.getHighestSlot(true) == 53) {
                                                                                    shop.addItem(item, 72, true);
                                                                                } else if (shop.getHighestSlot(true) == 107) {
                                                                                    shop.addItem(item, 126, true);
                                                                                } else if (e.getInventory().getItem(12).getData().getData() == (byte) 10) {
                                                                                    if (e.getInventory().firstEmpty() >= 18) {
                                                                                        if (e.getInventory().firstEmpty() == 18) {
                                                                                            shop.addItem(item, 18, true);
                                                                                        } else {
                                                                                            shop.addItem(item, shop.getHighestSlot(true) + 1, true);
                                                                                        }
                                                                                    } else {
                                                                                        shop.addItem(item, shop.getHighestSlot(true) + 1, true);
                                                                                    }
                                                                                } else {
                                                                                    shop.addItem(item, shop.getHighestSlot(true) + 1, true);
                                                                                }

                                                                                p.playSound(p.getLocation(), Sound.NOTE_PLING, 400, 400);

                                                                                p.closeInventory();

                                                                                p.sendMessage(Messages.getPrefix() + Messages.getAddItem());

                                                                                can = true;
                                                                                break;
                                                                            } else {
                                                                                p.sendMessage(Messages.getPrefix() + Messages.getShopFull());
                                                                            }
                                                                        } else {
                                                                            p.sendMessage(Messages.getPrefix() + Messages.getAlreadyAsk());
                                                                        }
                                                                    } else {
                                                                        can = true;
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                        if (!can) {
                                            p.sendMessage(Messages.getPrefix() + Messages.getInvalidItem());
                                        }

                                    } else {
                                        ev.setWillClose(true);
                                        ev.setWillDestroy(true);
                                    }
                                }
                            });

                            ItemStack it = new ItemStack(Material.PAPER);
                            ItemMeta meta = it.getItemMeta();
                            meta.setDisplayName("Type Item Name");
                            it.setItemMeta(meta);

                            gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, it);

                            gui.open();
                        } else {
                            p.closeInventory();

                            Map<Shop, Inventory> map = new HashMap<>();
                            map.put(shop, e.getInventory());

                            ChatMessages.addSellItem.put(p, map);
                            p.sendMessage(Messages.getPrefix() + Messages.getChatMessage());
                        }
                    }
                }
            }
        }
    }
}
