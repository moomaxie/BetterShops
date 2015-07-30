package max.hubbard.bettershops.Commands;

import max.hubbard.bettershops.Configurations.Blacklist;
import max.hubbard.bettershops.Configurations.Config;
import max.hubbard.bettershops.Configurations.ConfigMenu.ConfigMenu;
import max.hubbard.bettershops.Configurations.Language;
import max.hubbard.bettershops.Configurations.LanguageMenu.GUIMessagesInv;
import max.hubbard.bettershops.Configurations.Permissions;
import max.hubbard.bettershops.Core;
import max.hubbard.bettershops.Listeners.Opener;
import max.hubbard.bettershops.ShopManager;
import max.hubbard.bettershops.Shops.Shop;
import max.hubbard.bettershops.Shops.Types.Holo.DeleteHoloShop;
import max.hubbard.bettershops.Shops.Types.Holo.HologramManager;
import max.hubbard.bettershops.Shops.Types.NPC.NPCManager;
import max.hubbard.bettershops.Shops.Types.NPC.ShopsNPC;
import max.hubbard.bettershops.Shops.Types.Sign.SignShopManager;
import max.hubbard.bettershops.Updater;
import max.hubbard.bettershops.Utils.Conversion;
import max.hubbard.bettershops.Utils.ShopDeleter;
import max.hubbard.bettershops.Versions.SignChange;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
public class BSCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("bs")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (args.length == 1) {

                    if (args[0].equalsIgnoreCase("migrate") && p.isOp()) {
                        p.sendMessage(Language.getString("Messages", "Prefix") + "§eStarting Shop Migration...");
                        Conversion.startConversion();

                        try {
                            ShopManager.loadFile();

                            Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §aDone!");
                            p.sendMessage(Language.getString("Messages", "Prefix") + "§aDone");
                        } catch (Exception e) {
                            e.printStackTrace();
                            p.sendMessage(Language.getString("Messages", "Prefix") + "§cAn error occurred, please inform a server administrator.");
                        }

                    } else if (args[0].equalsIgnoreCase("info")) {

                        p.sendMessage(Language.getString("Messages", "Prefix") + "You are running version: §e" + Bukkit.getPluginManager().getPlugin("BetterShops").getDescription().getVersion());

                        p.sendMessage(Language.getString("Messages", "Prefix") + "Total Shops: §e" + ShopManager.getShops().size());
                        p.sendMessage(Language.getString("Messages", "Prefix") + "Total NPC Shops: §e" + NPCManager.getNPCShops().size());
                        p.sendMessage(Language.getString("Messages", "Prefix") + "Total Holographic Shops: §e" + HologramManager.getHolographicShops().size());
                        p.sendMessage(Language.getString("Messages", "Prefix") + "Total Sign Shops: §e" + SignShopManager.getSigns().size());
                    } else if (args[0].equalsIgnoreCase("update")) {
                        if (Permissions.hasUpdatePerm(p)) {
                            Updater.parseFile();
                            SignChange.updateSigns(p);
                            p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "Updated"));
                        } else {
                            p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "NoPermission"));
                        }
                    } else if (args[0].equalsIgnoreCase("config")) {
                        if (Permissions.hasConfigGUIPerm(p)) {
                            ConfigMenu.openConfigMenu(null, p, 1);
                        } else {
                            p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "NoPermission"));
                        }
                    } else if (args[0].equalsIgnoreCase("language")) {
                        if (Permissions.hasLanguagePerm(p)) {
                            GUIMessagesInv.openGUIMessagesInv(p);
                        } else {
                            p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "NoPermission"));
                        }

                    } else if (args[0].equalsIgnoreCase("blacklist")) {
                        if (Permissions.hasBlacklistCommandPerm(p)) {
                            Blacklist.openBlacklistInventory(null, p, 1);
                        } else {
                            p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "NoPermission"));
                        }

                    } else if (args[0].equalsIgnoreCase("list")) {
                        if ((boolean) Config.getObject("Permissions") && Permissions.hasListPerm(p) || !(boolean) Config.getObject("Permissions")) {
                            List<Shop> shops = ShopManager.getShops();
                            p.sendMessage("§d<-Listing Shops (§c" + shops.size() + "§d)->");
                            for (int i = 1; i < shops.size() + 1; i++) {
                                if (shops.get(i - 1).isNPCShop() || shops.get(i - 1).getNPCShop() != null) {
                                    p.sendMessage("§c" + i + ". §a" + shops.get(i - 1).getName() + " §e(NPC)");
                                } else if (shops.get(i - 1).isHoloShop()) {
                                    p.sendMessage("§c" + i + ". §a" + shops.get(i - 1).getName() + " §e(Holo)");
                                } else {
                                    p.sendMessage("§c" + i + ". §a" + shops.get(i - 1).getName());
                                }
                            }
                            p.sendMessage("§d<-Listing Shops (§c" + shops.size() + "§d)->");
                        } else {
                            p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "NoPermission"));
                        }
                    } else {
                        p.sendMessage("§d<-Better Shops Help->");
                        p.sendMessage("    §a/bs info");
                        p.sendMessage("    §a/bs update");
                        p.sendMessage("    §a/bs config");
                        p.sendMessage("    §a/bs language");
                        p.sendMessage("    §a/bs blacklist");
                        p.sendMessage("    §a/bs open <Shop>");
                        p.sendMessage("    §a/bs remove <Shop>");
                        p.sendMessage("    §a/bs list <Player>");
                        p.sendMessage("    §a/bs migrate");
                        p.sendMessage("§d<-Better Shops Help->");
                    }
                } else if (args.length >= 2) {
                    if (args[0].equalsIgnoreCase("open")) {
                        if ((boolean) Config.getObject("Permissions") && Permissions.hasOpenCommandPerm(p) || !(boolean) Config.getObject("Permissions")) {


                            String name = args[1];

                            for (int i = 2; i < args.length; i++) {
                                name = name + " " + args[i];
                            }

                            Shop shop = ShopManager.fromString(p, name);


                            if (shop != null) {
                                if ((boolean) Config.getObject("Permissions") && !Permissions.hasOpenCommandWorldPerm(p, shop.getLocation().getWorld())) {
                                    p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "NoPermission"));
                                    return true;
                                }
                                if (!shop.getBlacklist().contains(p)) {
                                    if (shop.getOwner() != null) {
                                        Opener.open(p, shop);
                                    } else {
                                        p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "FakeShop"));
                                    }
                                } else {
                                    p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "NotAllowed"));
                                }
                            } else {
                                if (ShopManager.loadingTotal == ShopManager.getShops().size()) {
                                    p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "InvalidShop"));
                                } else {
                                    p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "Loading"));
                                }
                            }
                        } else {
                            p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "NoPermission"));
                        }
                    } else if (args[0].equalsIgnoreCase("list")) {
                        if ((boolean) Config.getObject("Permissions") && Permissions.hasListPerm(p) || !(boolean) Config.getObject("Permissions")) {
                            String name = args[1];

                            for (int i = 2; i < args.length; i++) {
                                name = name + " " + args[i];
                            }

                            OfflinePlayer pl = Bukkit.getOfflinePlayer(name);

                            if (pl != null) {
                                List<Shop> shops = ShopManager.getShopsForPlayer(pl);
                                if (shops != null && shops.size() > 0) {
                                    p.sendMessage("§d<-Listing §e" + pl.getName() + "'s §dShops (§c" + shops.size() + "§d)->");
                                    for (int i = 1; i < shops.size() + 1; i++) {
                                        if (shops.get(i - 1).isNPCShop() || shops.get(i - 1).getNPCShop() != null) {
                                            p.sendMessage("§c" + i + ". §a" + shops.get(i - 1).getName() + " §e(NPC)");
                                        } else if (shops.get(i - 1).isHoloShop()) {
                                            p.sendMessage("§c" + i + ". §a" + shops.get(i - 1).getName() + " §e(Holo)");
                                        } else {
                                            p.sendMessage("§c" + i + ". §a" + shops.get(i - 1).getName());
                                        }
                                    }
                                    p.sendMessage("§d<-Listing §e" + pl.getName() + "'s §dShops (§c" + shops.size() + "§d)->");
                                } else {
                                    p.sendMessage("§d<-Listing §e" + pl.getName() + "'s §dShops (§c0§d)->");
                                    p.sendMessage("§cNo Shops");
                                    p.sendMessage("§d<-Listing §e" + pl.getName() + "'s §dShops (§c0§d)->");
                                }
                            } else {
                                p.sendMessage("§d<-Listing §e" + name + "'s §dShops (§c0§d)->");
                                p.sendMessage("§cNo Shops");
                                p.sendMessage("§d<-Listing §e" + name + "'s §dShops (§c0§d)->");
                            }
                        } else {
                            p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "NoPermission"));
                        }
                    } else if (args[0].equalsIgnoreCase("remove")) {
                        if ((boolean) Config.getObject("Permissions") && !Permissions.hasRemoveCommandPerm(p)) {
                            p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "NoPermission"));
                            return true;
                        }

                        if (args.length > 1) {
                            String name = args[1];

                            for (int i = 2; i < args.length; i++) {
                                name = name + " " + args[i];
                            }

                            Shop shop = ShopManager.fromString(p, name);

                            if (shop != null) {

                                if (shop.getOwner() != null) {
                                    if (shop.getOwner().getUniqueId().equals(p.getUniqueId()) || p.isOp() || (boolean) Config.getObject("Permissions") && Permissions.hasBreakPerm(p)) {


                                        if (!shop.isNPCShop() && !shop.isHoloShop() || shop.getNPCShop() == null && !shop.isHoloShop()) {

                                            Location loc = shop.getLocation();

                                            Block b = loc.getBlock();

                                            if (b.getState() instanceof Chest) {

                                                for (Chunk c : loc.getWorld().getLoadedChunks()) {
                                                    for (BlockState bs : c.getTileEntities()) {
                                                        if (bs instanceof Sign) {
                                                            Sign sign = (Sign) bs;

                                                            Block face = sign.getBlock().getRelative(((org.bukkit.material.Sign) (sign.getData())).getAttachedFace());


                                                            if (face.getType() == Material.CHEST || face.getType() == Material.TRAPPED_CHEST) {
                                                                if (face.getState() instanceof Chest) {
                                                                    Chest ch = (Chest) face.getState();

                                                                    if (ch.getLocation().equals(loc)) {
                                                                        sign.getBlock().setType(Material.AIR);
                                                                        sign.getWorld().dropItem(loc, new ItemStack(Material.SIGN));
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }

                                                p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "DeleteShop"));

                                                ShopDeleter.deleteShopExternally(shop);

                                                if (Core.isAboveEight() && (boolean) Config.getObject("Titles") && Core.getTitleManager() != null) {


                                                    Core.getTitleManager().setTimes(p, 20, 40, 20);
                                                    Core.getTitleManager().sendTitle(p, Language.getString("Messages", "DeleteShop"));


                                                }
                                            } else {
                                                p.sendMessage(Language.getString("Messages", "Prefix") + "§4ERROR: §cShop is non-existant, please tell a server operator of this problem");
                                            }

                                        } else if (shop.isNPCShop() || shop.getNPCShop() != null) {

                                            boolean can = false;

                                            for (LivingEntity e : shop.getLocation().getWorld().getLivingEntities()) {

                                                if (e.getCustomName() != null && e.getCustomName().equals("§a§l" + shop.getName())) {

                                                    for (ShopsNPC npc : NPCManager.getNPCShops()) {
                                                        if (npc.getShop().getName().equals(shop.getName())) {
                                                            NPCManager.removeNPCShop(npc);
                                                            e.remove();

                                                            p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "DeleteShop"));

                                                            ShopDeleter.deleteShopExternally(shop);

                                                            can = true;

                                                            if (Core.isAboveEight() && (boolean) Config.getObject("Titles") && Core.getTitleManager() != null) {

                                                                Core.getTitleManager().setTimes(p, 20, 40, 20);
                                                                Core.getTitleManager().sendTitle(p, Language.getString("Messages", "DeleteShop"));


                                                            }
                                                            break;
                                                        }

                                                    }
                                                }
                                            }

                                            if (!can) {

                                                p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "DeleteShop"));

                                                ShopDeleter.deleteShopExternally(shop);

                                                if (Core.isAboveEight() && (boolean) Config.getObject("Titles") && Core.getTitleManager() != null) {

                                                    Core.getTitleManager().setTimes(p, 20, 40, 20);
                                                    Core.getTitleManager().sendTitle(p, Language.getString("Messages", "DeleteShop"));


                                                }

                                            }
                                        } else {
                                            DeleteHoloShop.deleteHologramShop(shop.getHolographicShop());

                                            Location loc = shop.getLocation();

                                            Block b = loc.getBlock();

                                            if (b.getState() instanceof Chest) {

                                                for (Chunk c : loc.getWorld().getLoadedChunks()) {
                                                    for (BlockState bs : c.getTileEntities()) {
                                                        if (bs instanceof Sign) {
                                                            Sign sign = (Sign) bs;

                                                            Block face = sign.getBlock().getRelative(((org.bukkit.material.Sign) (sign.getData())).getAttachedFace());


                                                            if (face.getType() == Material.CHEST || face.getType() == Material.TRAPPED_CHEST) {
                                                                if (face.getState() instanceof Chest) {
                                                                    Chest ch = (Chest) face.getState();

                                                                    if (ch.getLocation().equals(loc)) {
                                                                        sign.getBlock().setType(Material.AIR);
                                                                        sign.getWorld().dropItem(loc, new ItemStack(Material.SIGN));
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }

                                                p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "DeleteShop"));

                                                ShopDeleter.deleteShopExternally(shop);

                                                if (Core.isAboveEight() && (boolean) Config.getObject("Titles") && Core.getTitleManager() != null) {


                                                    Core.getTitleManager().setTimes(p, 20, 40, 20);
                                                    Core.getTitleManager().sendTitle(p, Language.getString("Messages", "DeleteShop"));


                                                }
                                            } else {
                                                p.sendMessage(Language.getString("Messages", "Prefix") + "§4ERROR: §cShop is non-existant, please tell a server operator of this problem");
                                            }
                                        }
                                    } else {
                                        p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "DenyDeleteShop"));
                                    }
                                } else {
                                    p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "FakeShop"));
                                }
                            } else {
                                p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "FakeShop"));
                            }

                        } else {
                            p.sendMessage(Language.getString("Messages", "Prefix") + "§cUsage: §d/sremove <ShopName>");
                        }

                    } else {
                        p.sendMessage("§d<-Better Shops Help->");
                        p.sendMessage("    §a/bs info");
                        p.sendMessage("    §a/bs update");
                        p.sendMessage("    §a/bs config");
                        p.sendMessage("    §a/bs language");
                        p.sendMessage("    §a/bs blacklist");
                        p.sendMessage("    §a/bs open <Shop>");
                        p.sendMessage("    §a/bs remove <Shop>");
                        p.sendMessage("    §a/bs list <Player>");
                        p.sendMessage("    §a/bs migrate");
                        p.sendMessage("§d<-Better Shops Help->");
                    }

                } else {
                    p.sendMessage("§d<-Better Shops Help->");
                    p.sendMessage("    §a/bs info");
                    p.sendMessage("    §a/bs update");
                    p.sendMessage("    §a/bs config");
                    p.sendMessage("    §a/bs language");
                    p.sendMessage("    §a/bs blacklist");
                    p.sendMessage("    §a/bs open <Shop>");
                    p.sendMessage("    §a/bs remove <Shop>");
                    p.sendMessage("    §a/bs list <Player>");
                    p.sendMessage("    §a/bs migrate");
                    p.sendMessage("§d<-Better Shops Help->");
                }
            }
            return true;
        }
        return false;
    }
}
