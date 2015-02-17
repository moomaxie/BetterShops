package me.moomaxie.BetterShops.Listeners.ManagerOptions;

import me.moomaxie.BetterShops.Configurations.AnvilGUI;
import me.moomaxie.BetterShops.Configurations.Config;
import me.moomaxie.BetterShops.Configurations.GUIMessages.MainGUI;
import me.moomaxie.BetterShops.Configurations.Messages;
import me.moomaxie.BetterShops.Configurations.ShopLimits;
import me.moomaxie.BetterShops.Core;
import me.moomaxie.BetterShops.Listeners.BuyerOptions.OpenShop;
import me.moomaxie.BetterShops.Listeners.Misc.ChatMessages;
import me.moomaxie.BetterShops.Listeners.OpenShopOptions;
import me.moomaxie.BetterShops.Shops.Shop;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;

/**
 * ***********************************************************************
 * Copyright me.moomaxie (c) 2014. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of me.moomaxie. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class ShopKeeperManager implements Listener {

    @EventHandler
    public void onSettings(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getInventory().getName().contains("§7[Shop]")) {
            e.setCancelled(true);

            if (e.getInventory().getType() == InventoryType.CHEST) {
                if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {

                    String name = e.getInventory().getName();
                    name = name.substring(11);

                    Shop shop = ShopLimits.fromString(p, name);

                    if (shop.getOwner().getUniqueId().equals(p.getUniqueId())) {
                        if (e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().equals(MainGUI.getString("ShopKeepersDisplayName"))) {
                            if (e.getAction() == InventoryAction.PICKUP_ALL) {
                                openKeeperManager(p, shop);
                            }
                        }
                    }
                }
            }
        }
    }

    public static void openKeeperManager(Player p, Shop shop) {
        Inventory inv = Bukkit.createInventory(p, 54, "§7[Shop] §a" + shop.getName());

        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7);
        ItemMeta m = item.getItemMeta();
        m.setDisplayName(" ");
        item.setItemMeta(m);
        for (int i = 0; i < 18; i++) {
            inv.setItem(i, item);
        }

        ItemStack nam = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 5);
        ItemMeta namMeta = nam.getItemMeta();
        namMeta.setDisplayName(me.moomaxie.BetterShops.Configurations.GUIMessages.ShopKeeperManager.getString("AddKeeperDisplayName"));
        namMeta.setLore(Arrays.asList(me.moomaxie.BetterShops.Configurations.GUIMessages.ShopKeeperManager.getString("AddKeeperLore")));
        nam.setItemMeta(namMeta);

        ItemStack desc = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 14);
        ItemMeta descMeta = desc.getItemMeta();
        descMeta.setDisplayName(me.moomaxie.BetterShops.Configurations.GUIMessages.ShopKeeperManager.getString("RemoveKeeperDisplayName"));
        descMeta.setLore(Arrays.asList(me.moomaxie.BetterShops.Configurations.GUIMessages.ShopKeeperManager.getString("RemoveKeeperLore")));
        desc.setItemMeta(descMeta);

        ItemStack info = new ItemStack(Material.ENDER_CHEST);
        ItemMeta infoMeta = info.getItemMeta();
        infoMeta.setDisplayName(me.moomaxie.BetterShops.Configurations.GUIMessages.ShopKeeperManager.getString("KeeperInformationDisplayName"));
        infoMeta.setLore(Arrays.asList(me.moomaxie.BetterShops.Configurations.GUIMessages.ShopKeeperManager.getString("KeeperManage"),me.moomaxie.BetterShops.Configurations.GUIMessages.ShopKeeperManager.getString("KeeperDeposit"),me.moomaxie.BetterShops.Configurations.GUIMessages.ShopKeeperManager.getString("KeeperWithdraw")));
        info.setItemMeta(infoMeta);

        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName(MainGUI.getString("BackArrow"));
        back.setItemMeta(backMeta);

        inv.setItem(0, back);

        inv.setItem(5, nam);
        inv.setItem(6, nam);
        inv.setItem(4, info);
        inv.setItem(3, desc);
        inv.setItem(2, desc);

        for (OfflinePlayer pl : shop.getManagers()) {
            ItemStack it = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
            SkullMeta sk = (SkullMeta) it.getItemMeta();

            sk.setDisplayName("§a" + pl.getName());
            sk.setLore(Arrays.asList(me.moomaxie.BetterShops.Configurations.GUIMessages.ShopKeeperManager.getString("ClickRemoveKeeper")));
            sk.setOwner(pl.getName());
            it.setItemMeta(sk);

            if (inv.firstEmpty() > 0)
                inv.setItem(inv.firstEmpty(), it);
        }

        p.openInventory(inv);
    }

    @EventHandler
    public void onSettingsClick(final InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getInventory().getName().contains("§7[Shop]")) {
            e.setCancelled(true);

            if (e.getInventory().getType() == InventoryType.CHEST) {
                if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
                    String name = e.getInventory().getName();
                    name = name.substring(11);

                    final Shop shop = ShopLimits.fromString(p, name);

                    if (e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().equals(MainGUI.getString("BackArrow"))) {
                        if (shop.getOwner().getUniqueId().equals(p.getUniqueId())) {
                            OpenShopOptions.openShopOwnerOptionsInventory(e.getInventory(),p, shop, 1);
                        } else {
                            OpenShop.openShopItems(e.getInventory(),p, shop, 1);
                        }
                    }

                    if (e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().equals(me.moomaxie.BetterShops.Configurations.GUIMessages.ShopKeeperManager.getString("AddKeeperDisplayName"))) {
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
                                                    OfflinePlayer player = Bukkit.getOfflinePlayer(name);
                                                    if (player.hasPlayedBefore()) {

                                                        if (!shop.getManagers().contains(player)){
                                                            shop.addManager(player);
                                                            ((Player) e.getWhoClicked()).sendMessage(Messages.getString("Prefix") + Messages.getString("AddedKeeper"));
                                                            openKeeperManager((Player) e.getWhoClicked(), shop);
                                                        } else {
                                                            ((Player) e.getWhoClicked()).sendMessage(Messages.getString("Prefix") + "§cAlready a shop keeper");
                                                            openKeeperManager((Player) e.getWhoClicked(), shop);
                                                        }
                                                    } else {
                                                        ((Player) e.getWhoClicked()).sendMessage(Messages.getString("Prefix") + Messages.getString("InvalidKeeper"));
                                                        openKeeperManager((Player) e.getWhoClicked(), shop);
                                                    }
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
                            meta.setDisplayName("Type Name");
                            it.setItemMeta(meta);

                            gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, it);

                            gui.open();
                        } else {
                            p.closeInventory();
                            ChatMessages.addKeeper.put(p, shop);
                            p.sendMessage(Messages.getString("Prefix") + Messages.getString("ChatMessage"));
                        }
                    } else if (e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().equals(me.moomaxie.BetterShops.Configurations.GUIMessages.ShopKeeperManager.getString("RemoveKeeperDisplayName"))) {
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

                                                    OfflinePlayer player = Bukkit.getOfflinePlayer(name);
                                                    if (shop.getManagers().contains(player) && player.hasPlayedBefore()) {

                                                        shop.removeManager(Bukkit.getOfflinePlayer(name));
                                                        ((Player) e.getWhoClicked()).sendMessage(Messages.getString("Prefix") + Messages.getString("RemovedKeeper"));
                                                        openKeeperManager((Player) e.getWhoClicked(), shop);
                                                    } else {
                                                        ((Player) e.getWhoClicked()).sendMessage(Messages.getString("Prefix") + Messages.getString("InvalidKeeper"));
                                                    }
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
                            meta.setDisplayName("Type Name");
                            it.setItemMeta(meta);

                            gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, it);

                            gui.open();
                        } else {
                            p.closeInventory();
                            ChatMessages.removeKeeper.put(p, shop);
                            p.sendMessage(Messages.getString("Prefix") + Messages.getString("ChatMessage"));
                        }
                    } else if (e.getCurrentItem().getItemMeta().getLore() != null && e.getCurrentItem().getItemMeta().getLore().contains(me.moomaxie.BetterShops.Configurations.GUIMessages.ShopKeeperManager.getString("ClickRemoveKeeper"))) {

                        if (e.getCurrentItem().getItemMeta().getDisplayName() != null){
                            String n = e.getCurrentItem().getItemMeta().getDisplayName().substring(2);
                            shop.removeManager(Bukkit.getOfflinePlayer(n));
                            ((Player) e.getWhoClicked()).sendMessage(Messages.getString("Prefix") + Messages.getString("RemovedKeeper"));
                            openKeeperManager(p,shop);
                        }
                    }
                }
            }
        }
    }
}
