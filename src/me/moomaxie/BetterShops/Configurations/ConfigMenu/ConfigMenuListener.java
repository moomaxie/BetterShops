package me.moomaxie.BetterShops.Configurations.ConfigMenu;

import me.moomaxie.BetterShops.Configurations.AnvilGUI;
import me.moomaxie.BetterShops.Configurations.Config;
import me.moomaxie.BetterShops.Configurations.Messages;
import me.moomaxie.BetterShops.Core;
import me.moomaxie.BetterShops.Metrics.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.text.DecimalFormat;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2014. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class ConfigMenuListener implements Listener{

    @EventHandler
    public void onConfig(final InventoryClickEvent e){
        if (e.getInventory().getName().equals("§7[BetterShops] §dConfig")){
            if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR){
                e.setCancelled(true);
                if (e.getCurrentItem().getItemMeta().getDisplayName().contains("§eAuto Add §7-")){
                    Config.setAutoAddItems(!Config.autoAddItems());
                    ConfigMenu.openConfigMenu(e.getInventory(),(Player) e.getWhoClicked());
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().contains("§ePermissions §7-")){
                    Config.setPermissions(!Config.usePerms());
                    ConfigMenu.openConfigMenu(e.getInventory(),(Player) e.getWhoClicked());
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().contains("§eAnvil §7-")){
                    Config.setAnvil(!Config.useAnvil());
                    ConfigMenu.openConfigMenu(e.getInventory(),(Player) e.getWhoClicked());
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().contains("§eUse Shop When Closed §7-")){
                    Config.setUseOnClose(!Config.useWhenClosed());
                    ConfigMenu.openConfigMenu(e.getInventory(),(Player) e.getWhoClicked());
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().contains("§eTitles §7-")){
                    Config.setTitles(!Config.useTitles());
                    ConfigMenu.openConfigMenu(e.getInventory(),(Player) e.getWhoClicked());
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().contains("§eDelete By Break §7-")){
                    Config.setDeleteByBreak(!Config.useDeleteByBreak());
                    ConfigMenu.openConfigMenu(e.getInventory(),(Player) e.getWhoClicked());
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().contains("§eMetrics §7-")){
                    Config.setMetrics(!Config.useMetrics());

                    if (!Config.useMetrics()) {
                        try {
                            Core.getMetrics().disable();
                        } catch (IOException e1) {
                            Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §4Error: §cCould not disable §eMetrics");
                        }
                    } else {
                        try {
                            Core.metrics = new Metrics(Core.getCore());
                            Core.getCore().setUpMetrics();
                            Core.getMetrics().start();
                        } catch (Exception ie){
                            Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §4Error: §cCould not enable §eMetrics");
                        }
                    }
                    ConfigMenu.openConfigMenu(e.getInventory(),(Player) e.getWhoClicked());
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().contains("§eCreation Limit §7-")){
                    Config.setCreationLimit(!Config.useLimit());
                    ConfigMenu.openConfigMenu(e.getInventory(),(Player) e.getWhoClicked());
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().contains("§eCreation Limit Amount:")){
                    setLimit((Player) e.getWhoClicked());
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().contains("§eCost On Shops §7-")){
                    Config.setCostOnShops(!Config.useCreationCost());
                    ConfigMenu.openConfigMenu(e.getInventory(),(Player) e.getWhoClicked());
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().contains("§eCost On Shops Amount:")){
                    setCost((Player) e.getWhoClicked());
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().contains("§eNPCs §7-")){
                    Config.setEnableNPC(!Config.useNPCs());
                    ConfigMenu.openConfigMenu(e.getInventory(),(Player) e.getWhoClicked());
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().contains("§eChoose NPCs")){
                    NPCChooser.openNPCConfigChooser((Player) e.getWhoClicked());
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().contains("§eAllow-Shop Flag §7-")){
                    Config.setEnableAllowShopsFlag(!Config.useAllowFlag());
                    ConfigMenu.openConfigMenu(e.getInventory(),(Player) e.getWhoClicked());
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().contains("§eNPC Override §7-")){
                    Config.setNPCOverride(!Config.useNPCOverride());
                    ConfigMenu.openConfigMenu(e.getInventory(),(Player) e.getWhoClicked());
                }

                if (e.getCurrentItem().getItemMeta().getDisplayName().contains("§eSelling Shop §7-")){
                    Config.setUseSellingShops(!Config.useSellingShop());
                    ConfigMenu.openConfigMenu(e.getInventory(),(Player) e.getWhoClicked());
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().contains("§eDefault Price:")){
                    setDefaultPrice((Player) e.getWhoClicked());
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().contains("§eMaximum Price:")){
                    setMaxPrice((Player) e.getWhoClicked());
                }
            }
        }
    }

    public void setLimit(final Player p){
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

                                if (!can) {
                                    Config.setLimit(amt);
                                    Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                                        public void run() {
                                            ConfigMenu.openConfigMenu(null,p);
                                        }
                                    }, 1L);
                                } else {
                                    p.sendMessage(Messages.getPrefix() + Messages.getInvalidNumber());
                                    Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                                        public void run() {
                                            ConfigMenu.openConfigMenu(null,p);
                                        }
                                    }, 1L);
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
        meta.setDisplayName("Type Limit");
        it.setItemMeta(meta);

        gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, it);

        gui.open();
    }

    public void setDefaultPrice(final Player p){
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
                                boolean can = true;
                                try {
                                    amt = Double.parseDouble(name);
                                    amt = Double.valueOf(new DecimalFormat("#.00").format(amt));
                                    can = false;


                                } catch (Exception ex) {
                                }

                                if (!can) {
                                    Config.setDefaultPrice(amt);
                                    Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                                        public void run() {
                                            ConfigMenu.openConfigMenu(null,p);
                                        }
                                    }, 1L);
                                } else {
                                    p.sendMessage(Messages.getPrefix() + Messages.getInvalidNumber());
                                    Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                                        public void run() {
                                            ConfigMenu.openConfigMenu(null,p);
                                        }
                                    }, 1L);
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
        meta.setDisplayName("Type Price");
        it.setItemMeta(meta);

        gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, it);

        gui.open();
    }

    public void setMaxPrice(final Player p){
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
                                boolean can = true;
                                try {
                                    amt = Double.parseDouble(name);
                                    amt = Double.valueOf(new DecimalFormat("#.00").format(amt));
                                    can = false;


                                } catch (Exception ex) {
                                }

                                if (!can) {
                                    Config.setMaxPrice(amt);
                                    Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                                        public void run() {
                                            ConfigMenu.openConfigMenu(null,p);
                                        }
                                    }, 1L);
                                } else {
                                    p.sendMessage(Messages.getPrefix() + Messages.getInvalidNumber());
                                    Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                                        public void run() {
                                            ConfigMenu.openConfigMenu(null,p);
                                        }
                                    }, 1L);
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
        meta.setDisplayName("Type Price");
        it.setItemMeta(meta);

        gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, it);

        gui.open();
    }

    public void setCost(final Player p){
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
                                boolean can = true;
                                try {
                                    amt = Double.parseDouble(name);
                                    can = false;

                                } catch (Exception ex) {
                                }

                                if (!can) {
                                    Config.setCostForShops(amt);
                                    Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                                        public void run() {
                                            ConfigMenu.openConfigMenu(null,p);
                                        }
                                    }, 1L);
                                } else {
                                    p.sendMessage(Messages.getPrefix() + Messages.getInvalidNumber());
                                    Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                                        public void run() {
                                            ConfigMenu.openConfigMenu(null,p);
                                        }
                                    }, 1L);
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
        meta.setDisplayName("Type Cost");
        it.setItemMeta(meta);

        gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, it);

        gui.open();
    }
}
