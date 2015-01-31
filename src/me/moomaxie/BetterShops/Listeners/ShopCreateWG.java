package me.moomaxie.BetterShops.Listeners;

import BetterShops.Dev.API.Events.ShopCreateEvent;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import me.moomaxie.BetterShops.Configurations.AnvilGUI;
import me.moomaxie.BetterShops.Configurations.Config;
import me.moomaxie.BetterShops.Configurations.GUIMessages.MainGUI;
import me.moomaxie.BetterShops.Configurations.Messages;
import me.moomaxie.BetterShops.Configurations.Permissions.Permissions;
import me.moomaxie.BetterShops.Configurations.ShopLimits;
import me.moomaxie.BetterShops.Core;
import me.moomaxie.BetterShops.Listeners.CreationCost.CreationCost;
import me.moomaxie.BetterShops.Listeners.Misc.ChatMessages;
import me.moomaxie.BetterShops.Shops.AddShop;
import me.moomaxie.BetterShops.Shops.Shop;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2014. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class ShopCreateWG implements Listener {

    @EventHandler
    public void onSign(final SignChangeEvent e) {
        final Player p = e.getPlayer();

        Chest chest = null;
        if (e.getLine(0).equalsIgnoreCase(MainGUI.getString("ShopCreate")) || e.getLine(0).equalsIgnoreCase(ChatColor.BLACK + MainGUI.getString("ShopCreate"))) {

            boolean can = true;
            boolean wgCan = false;



            if (Config.usePerms() && !Permissions.hasCreatePerm(p)) {
                can = false;
            }

            if (Config.useLimit() && Config.usePerms() && !Permissions.hasLimitPerm(p) && ShopLimits.atLimit(p) || Config.useLimit() && !p.isOp() && !Config.usePerms() && ShopLimits.atLimit(p)) {
                can = false;
            }

            if (Core.useWorldGuard() && Config.useAllowFlag()){
                ApplicableRegionSet set = Core.getRegionSet(e.getBlock().getLocation());

                if (set.allows(com.sk89q.worldguard.protection.flags.DefaultFlag.ENABLE_SHOP)){
                    wgCan = true;
                }
            } else if (Core.useWorldGuard() && !Config.useAllowFlag()){
                wgCan = true;
                ApplicableRegionSet set = Core.getRegionSet(e.getBlock().getLocation());

                if (!set.allows(com.sk89q.worldguard.protection.flags.DefaultFlag.ENABLE_SHOP)){
                    wgCan = false;
                }
            } else {
                wgCan = true;
            }

            if (can) {

                if (wgCan) {

                    if (e.getBlock().getType() == Material.WALL_SIGN) {

                        org.bukkit.block.Sign sign = (org.bukkit.block.Sign) e.getBlock().getState();

                        Block face = e.getBlock().getRelative(((org.bukkit.material.Sign) (sign.getData())).getAttachedFace());


                        if (face.getType() == Material.CHEST) {
                            if (face.getState() instanceof Chest) {
                                chest = (Chest) face.getState();
                            }

                        } else {
                            p.sendMessage(Messages.getPrefix() + "§cMust be attached to a chest!");
                            e.setLine(0, " ");
                            e.setLine(1, " ");
                            e.setLine(2, " ");
                            e.setLine(3, " ");
                            e.setCancelled(true);
                            return;
                        }

                    } else {
                        return;
                    }
                } else {
                    p.sendMessage(Messages.getPrefix() + Messages.getWorldGuardDenyShop());
                    return;
                }


                final Chest finalChest = chest;

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

                                            boolean can = true;
                                            boolean Long = false;

                                            if (name.length() > 21) {
                                                Long = true;
                                            }

                                            if (new File(Core.getCore().getDataFolder(), "Shops").listFiles() != null) {

                                                for (File file : new File(Core.getCore().getDataFolder(), "Shops").listFiles()) {
                                                    if (file.getName().contains(".yml")) {
                                                        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

                                                        for (String s : config.getKeys(false)) {
                                                            if (s.equals(name)) {
                                                                can = false;
                                                            }
                                                        }
                                                    }
                                                }
                                            }

                                            if (can && !Long) {
                                                if (CreationCost.useCost(p)) {
                                                    new AddShop(e.getPlayer(), finalChest, name);
                                                    e.getPlayer().sendMessage(Messages.getPrefix() + Messages.getCreateShop());

                                                    e.setLine(0, "§b§k**************");
                                                    e.setLine(1, "§aShop");
                                                    e.setLine(2, "§cClosed");
                                                    e.setLine(3, "§b§k**************");

                                                    Sign s = (Sign) e.getBlock().getState();

                                                    s.setLine(0, "§b§k**************");
                                                    s.setLine(1, "§aShop");
                                                    s.setLine(2, "§cClosed");
                                                    s.setLine(3, "§b§k**************");

                                                    s.update();

                                                    if (Core.isAboveEight() && Config.useTitles()) {


                                                        Core.getTitleManager().setTimes(p, 20, 40, 20);
                                                        Core.getTitleManager().sendTitle(p, Messages.getCreateShop());


                                                    }

                                                    if (finalChest != null) {
                                                        ShopCreateEvent e = new ShopCreateEvent(ShopLimits.fromString(finalChest.getLocation()));

                                                        Bukkit.getPluginManager().callEvent(e);
                                                    }

//                                                    ShopLimits.loadShops();

                                                    if (Config.autoAddItems()) {

                                                        if (finalChest != null && finalChest.getBlockInventory() != null) {
                                                            Shop shop = ShopLimits.fromString(finalChest.getLocation());
                                                            int i = 18;
                                                            for (final ItemStack items : finalChest.getBlockInventory().getContents()) {
                                                                if (items != null && items.getType() != Material.AIR) {

                                                                    int am = items.getAmount();

                                                                    items.setAmount(1);

                                                                    if (!shop.getShopContents(false).containsKey(items)) {
                                                                        if (i < 53) {
                                                                            shop.addItem(items, i, false);
                                                                            i++;
                                                                        } else {
                                                                            if (i == 53) {
                                                                                shop.addItem(items, i, false);
                                                                                i = 72;
                                                                            } else {
                                                                                shop.addItem(items, i, false);
                                                                                i++;
                                                                            }
                                                                        }
                                                                    } else {
                                                                        shop.addItem(items, i, false);
                                                                    }

                                                                    items.setAmount(am);

                                                                    if (items.getAmount() > 1) {
                                                                        int amt = items.getAmount();

                                                                        shop.setStock(items, ((shop.getStock(items, false) + amt) - 1), false);

                                                                        Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                                                                            public void run() {
                                                                                for (int in = 0; in < finalChest.getBlockInventory().getSize(); in++) {
                                                                                    if (finalChest.getBlockInventory().getItem(in) != null) {

                                                                                        if (finalChest.getBlockInventory().getItem(in).equals(items)) {
                                                                                            finalChest.getBlockInventory().setItem(in, new ItemStack(Material.AIR));
                                                                                        }
                                                                                    }
                                                                                }

                                                                            }
                                                                        }, 5L);

                                                                    } else {
                                                                        Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                                                                            public void run() {
                                                                                for (int in = 0; in < finalChest.getBlockInventory().getSize(); in++) {
                                                                                    if (finalChest.getBlockInventory().getItem(in) != null) {

                                                                                        if (finalChest.getBlockInventory().getItem(in).equals(items)) {
                                                                                            finalChest.getBlockInventory().setItem(in, new ItemStack(Material.AIR));
                                                                                        }
                                                                                    }
                                                                                }

                                                                            }
                                                                        }, 5L);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            } else {
                                                if (Long) {
                                                    e.getPlayer().sendMessage(Messages.getPrefix() + "§cThat Shop Name Is Too long! §7(Max: 21 Characters)");
                                                    e.setLine(0, " ");
                                                    e.setLine(1, " ");
                                                    e.setLine(2, " ");
                                                    e.setLine(3, " ");
                                                    if (Core.isAboveEight() && Config.useTitles()) {

                                                        Core.getTitleManager().setTimes(p, 20, 40, 20);
                                                        Core.getTitleManager().sendTitle(p, "§cName Too Long");

                                                    }
                                                }

                                                if (!can) {
                                                    e.getPlayer().sendMessage(Messages.getPrefix() + "§cA shop with that name already exists!");
                                                    e.setLine(0, " ");
                                                    e.setLine(1, " ");
                                                    e.setLine(2, " ");
                                                    e.setLine(3, " ");
                                                    if (Core.isAboveEight() && Config.useTitles()) {


                                                        Core.getTitleManager().setTimes(p, 20, 40, 20);
                                                        Core.getTitleManager().sendTitle(p, "§cName Already Exists");

                                                    }
                                                }
                                            }


                                        } else {
                                            e.getPlayer().sendMessage(Messages.getPrefix() + "§4ERROR: §cMalfunction with Shop Name Creating, is the plugin updated?");
                                            e.setLine(0, " ");
                                            e.setLine(1, " ");
                                            e.setLine(2, " ");
                                            e.setLine(3, " ");
                                            if (Core.isAboveEight() && Config.useTitles()) {


                                                Core.getTitleManager().setTimes(p, 20, 40, 20);
                                                Core.getTitleManager().sendTitle(p, "§4Error");
                                                Core.getTitleManager().sendSubTitle(p, "§cMalfunction with Shop Name Creating, is the plugin updated?");


                                            }
                                            e.setCancelled(true);
                                        }
                                    } else {
                                        e.getPlayer().sendMessage(Messages.getPrefix() + "§4ERROR: §cMalfunction with Shop Name Creating, is the plugin updated?");
                                        e.setLine(0, " ");
                                        e.setLine(1, " ");
                                        e.setLine(2, " ");
                                        e.setLine(3, " ");
                                        if (Core.isAboveEight() && Config.useTitles()) {


                                            Core.getTitleManager().setTimes(p, 20, 40, 20);
                                            Core.getTitleManager().sendTitle(p, "§4Error");
                                            Core.getTitleManager().sendSubTitle(p, "§cMalfunction with Shop Name Creating, is the plugin updated?");


                                        }
                                        e.setCancelled(true);
                                    }
                                } else {
                                    e.getPlayer().sendMessage(Messages.getPrefix() + "§cShop creation cancelled");
                                    e.setLine(0, " ");
                                    e.setLine(1, " ");
                                    e.setLine(2, " ");
                                    e.setLine(3, " ");
                                    if (Core.isAboveEight() && Config.useTitles()) {

                                        Core.getTitleManager().setTimes(p, 20, 40, 20);
                                        Core.getTitleManager().sendTitle(p, "§cShop creation cancelled");

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
                    meta.setDisplayName("Enter Name");
                    it.setItemMeta(meta);

                    gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, it);

                    gui.open();
                } else {
                    ChatMessages.shopCreate.put(p, finalChest);
                    ChatMessages.shopCreate2.put(p, e.getBlock());
                    p.sendMessage(Messages.getPrefix() + Messages.getChatMessage());

                }
            } else {
                p.sendMessage(Messages.getPrefix() + Messages.getNoPermission());
                e.setLine(0, " ");
                e.setLine(1, " ");
                e.setLine(2, " ");
                e.setLine(3, " ");
                if (Core.isAboveEight() && Config.useTitles()) {


                    Core.getTitleManager().setTimes(p, 20, 40, 20);
                    Core.getTitleManager().sendTitle(p, Messages.getNoPermission());


                }
            }
        }
    }
}
