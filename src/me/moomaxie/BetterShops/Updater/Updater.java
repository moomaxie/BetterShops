package me.moomaxie.BetterShops.Updater;

import me.moomaxie.BetterShops.Configurations.Config;
import me.moomaxie.BetterShops.Configurations.GUIMessages.*;
import me.moomaxie.BetterShops.Configurations.ShopManager;
import me.moomaxie.BetterShops.Core;
import me.moomaxie.BetterShops.Listeners.ShopCreate;
import me.moomaxie.BetterShops.Listeners.ShopDelete;
import me.moomaxie.BetterShops.ShopTypes.Holographic.CreateHologram;
import me.moomaxie.BetterShops.ShopTypes.Holographic.DeleteHoloShop;
import me.moomaxie.BetterShops.ShopTypes.NPC.DeleteNPC;
import me.moomaxie.BetterShops.ShopTypes.NPC.ShopsNPC;
import me.moomaxie.BetterShops.Shops.Shop;
import me.moomaxie.BetterShops.Shops.ShopItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;

import java.io.File;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2015. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class Updater {

    public static void startCheckForUpdate() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
            @Override
            public void run() {
                parseFile();
            }
        }, 0L, 3600L);
    }

    public static void parseFile() {
        File file = new File(Core.getCore().getDataFolder(), "Updater.yml");
        if (file.exists()) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

            for (String s : config.getKeys(false)) {
                if (s.equals("CREATE")) {
                    for (String s1 : config.getConfigurationSection(s).getKeys(false)) {

                        if (ShopManager.fromString(s1) == null) {

                            String owner = config.getConfigurationSection(s).getConfigurationSection(s1).getString("OWNER");

                            String c = config.getConfigurationSection(s).getConfigurationSection(s1).getString("LOCATION");

                            String[] locs = c.split(" ");

                            World w = Bukkit.getWorld(locs[0]);

                            double x = Double.parseDouble(locs[1]);
                            double y = Double.parseDouble(locs[2]);
                            double z = Double.parseDouble(locs[3]);


                            ShopCreate.createShopExternally(new Location(w, x, y, z), s1, Bukkit.getOfflinePlayer(owner));
                        }
                    }
                }

                if (s.equals("DELETE")) {
                    for (String s1 : config.getConfigurationSection(s).getKeys(false)) {
                        Shop shop = ShopManager.fromString(s1);
                        if (shop != null) {
                            if (shop.isNPCShop() || shop.getNPCShop() != null) {
                                DeleteNPC.deleteNPC(shop.getNPCShop());
                            }
                            if (shop.isHoloShop()) {
                                DeleteHoloShop.deleteHologramShop(shop.getHolographicShop());
                            }
                            ShopDelete.deleteShopExternally(shop);
                        }
                    }
                }

                if (s.equals("CONFIG")) {
                    Config.file = new File(Bukkit.getPluginManager().getPlugin("BetterShops").getDataFolder(), "config.yml");
                    Config.config = YamlConfiguration.loadConfiguration(Config.file);
                }

                if (s.equals("LANGUAGE")) {
                    for (String s1 : config.getConfigurationSection(s).getKeys(false)) {
                        s1 = s1 + ".yml";
                        if (s1.equals("BuyingAndSelling.yml")) {
                            BuyingAndSelling.file = new File(Bukkit.getPluginManager().getPlugin("BetterShops").getDataFolder(), "Language/BuyingAndSelling.yml");
                            BuyingAndSelling.config = YamlConfiguration.loadConfiguration(BuyingAndSelling.file);
                        }
                        if (s1.equals("Checkout.yml")) {
                            Checkout.file = new File(Bukkit.getPluginManager().getPlugin("BetterShops").getDataFolder(), "Language/Checkout.yml");
                            Checkout.config = YamlConfiguration.loadConfiguration(Checkout.file);
                        }
                        if (s1.equals("History.yml")) {
                            History.file = new File(Bukkit.getPluginManager().getPlugin("BetterShops").getDataFolder(), "Language/History.yml");
                            History.config = YamlConfiguration.loadConfiguration(History.file);
                        }
                        if (s1.equals("ItemTexts.yml")) {
                            ItemTexts.file = new File(Bukkit.getPluginManager().getPlugin("BetterShops").getDataFolder(), "Language/ItemTexts.yml");
                            ItemTexts.config = YamlConfiguration.loadConfiguration(ItemTexts.file);
                        }
                        if (s1.equals("LiveEconomy.yml")) {
                            LiveEconomy.file = new File(Bukkit.getPluginManager().getPlugin("BetterShops").getDataFolder(), "Language/LiveEconomy.yml");
                            LiveEconomy.config = YamlConfiguration.loadConfiguration(LiveEconomy.file);
                        }
                        if (s1.equals("MainGUI.yml")) {
                            MainGUI.file = new File(Bukkit.getPluginManager().getPlugin("BetterShops").getDataFolder(), "Language/MainGUI.yml");
                            MainGUI.config = YamlConfiguration.loadConfiguration(MainGUI.file);
                        }
                        if (s1.equals("NPCs.yml")) {
                            NPCs.file = new File(Bukkit.getPluginManager().getPlugin("BetterShops").getDataFolder(), "Language/NPCs.yml");
                            NPCs.config = YamlConfiguration.loadConfiguration(NPCs.file);
                        }
                        if (s1.equals("SearchEngine.yml")) {
                            SearchEngine.file = new File(Bukkit.getPluginManager().getPlugin("BetterShops").getDataFolder(), "Language/SearchEngine.yml");
                            SearchEngine.config = YamlConfiguration.loadConfiguration(SearchEngine.file);
                        }
                        if (s1.equals("ShopKeeperManager.yml")) {
                            ShopKeeperManager.file = new File(Bukkit.getPluginManager().getPlugin("BetterShops").getDataFolder(), "Language/ShopKeeperManager.yml");
                            ShopKeeperManager.config = YamlConfiguration.loadConfiguration(ShopKeeperManager.file);
                        }
                        if (s1.equals("ShopSettings.yml")) {
                            ShopSettings.file = new File(Bukkit.getPluginManager().getPlugin("BetterShops").getDataFolder(), "Language/ShopSettings.yml");
                            ShopSettings.config = YamlConfiguration.loadConfiguration(ShopSettings.file);
                        }
                    }
                }

                if (s.equals("SHOP")) {
                    for (String s1 : config.getConfigurationSection(s).getKeys(false)) {
                        Shop shop = ShopManager.fromString(s1);
                        if (shop != null) {
                            for (String s2 : config.getConfigurationSection(s).getConfigurationSection(s1).getKeys(false)) {
                                if (s2.equals("DESCRIPTION")) {
                                    shop.setDescription(config.getConfigurationSection(s).getConfigurationSection(s1).getString(s2));
                                }
                                if (s2.equals("OPEN")) {
                                    shop.setOpen(config.getConfigurationSection(s).getConfigurationSection(s1).getBoolean(s2));
                                    if (!shop.isNPCShop() && shop.getNPCShop() == null) {


                                        Chest chest = (Chest) shop.getLocation().getWorld().getBlockAt(shop.getLocation()).getState();

                                        Block block = chest.getBlock();

                                        Sign sign = null;
                                        if (block.getRelative(1, 0, 0).getType() == Material.WALL_SIGN) {
                                            sign = (Sign) block.getRelative(1, 0, 0).getState();
                                        } else if (block.getRelative(-1, 0, 0).getType() == Material.WALL_SIGN) {
                                            sign = (Sign) block.getRelative(-1, 0, 0).getState();
                                        } else if (block.getRelative(0, 0, 1).getType() == Material.WALL_SIGN) {
                                            sign = (Sign) block.getRelative(0, 0, 1).getState();
                                        } else if (block.getRelative(0, 0, -1).getType() == Material.WALL_SIGN) {
                                            sign = (Sign) block.getRelative(0, 0, -1).getState();
                                        }

                                        if (sign != null) {
                                            if (sign.getLine(0).contains(MainGUI.getString("SignLine1"))) {
                                                if (sign.getLine(3).contains(MainGUI.getString("SignLine4"))) {
                                                    if (sign.getLine(1).contains(MainGUI.getString("SignLine2"))) {
                                                        if (config.getConfigurationSection(s).getConfigurationSection(s1).getBoolean(s2)) {
                                                            sign.setLine(2, MainGUI.getString("SignLine3Open"));
                                                            sign.update();
                                                        } else {
                                                            sign.setLine(2, MainGUI.getString("SignLine3Closed"));
                                                            sign.update();
                                                        }

                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                if (s2.equals("NOTIFY")) {
                                    shop.setNotification(config.getConfigurationSection(s).getConfigurationSection(s1).getBoolean(s2));
                                }
                                if (s2.equals("SERVER SHOP")) {
                                    shop.setServerShop(config.getConfigurationSection(s).getConfigurationSection(s1).getBoolean(s2));
                                }
                                if (s2.equals("NPC SHOP")) {
                                    if (config.getConfigurationSection(s).getConfigurationSection(s1).getBoolean(s2)){
                                        me.moomaxie.BetterShops.ShopTypes.NPC.NPCs.addNPC(new ShopsNPC(EntityType.VILLAGER,shop));
                                        shop.setNPCShop(true);
                                        shop.setOpen(true);
                                        me.moomaxie.BetterShops.Listeners.ManagerOptions.ShopSettings.removeChest(shop);
                                    } else {
                                        if (shop.isNPCShop() || shop.getNPCShop() != null) {
                                            DeleteNPC.deleteNPC(shop.getNPCShop());
                                        }
                                    }
                                }
                                if (s2.equals("HOLO SHOP")) {
                                    if (config.getConfigurationSection(s).getConfigurationSection(s1).getBoolean(s2)){
                                        if (shop.getShopItems().size() > 0) {
                                            CreateHologram.createHolographicShop(shop);
                                        }
                                    } else {
                                        if (shop.isHoloShop()) {
                                            DeleteHoloShop.deleteHologramShop(shop.getHolographicShop());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (s.equals("SHOP ITEM")) {
                    for (String s1 : config.getConfigurationSection(s).getKeys(false)) {
                        Shop shop = ShopManager.fromString(s1);
                        if (shop != null) {
                            for (String s2 : config.getConfigurationSection(s).getConfigurationSection(s1).getKeys(false)) {
                                if (s2.equals("BUYING")) {
                                    for (String s3 : config.getConfigurationSection(s).getConfigurationSection(s1).getConfigurationSection(s2).getKeys(false)) {
                                        ShopItem item = ShopItem.fromId(shop, Integer.parseInt(s3), false);

                                        if (item != null) {

                                            for (String s4 : config.getConfigurationSection(s).getConfigurationSection(s1).getConfigurationSection(s2).getConfigurationSection(s3).getKeys(false)) {
                                                if (s4.equals("STOCK")) {
                                                    item.setStock(config.getConfigurationSection(s).getConfigurationSection(s1).getConfigurationSection(s2).getConfigurationSection(s3).getInt(s4));
                                                }
                                                if (s4.equals("AMOUNT")) {
                                                    item.setAmount(config.getConfigurationSection(s).getConfigurationSection(s1).getConfigurationSection(s2).getConfigurationSection(s3).getInt(s4));
                                                }
                                                if (s4.equals("PRICE")) {
                                                    item.setPrice(config.getConfigurationSection(s).getConfigurationSection(s1).getConfigurationSection(s2).getConfigurationSection(s3).getDouble(s4));
                                                }
                                                if (s4.equals("LIVE ECO")) {
                                                    item.setLiveEco(config.getConfigurationSection(s).getConfigurationSection(s1).getConfigurationSection(s2).getConfigurationSection(s3).getBoolean(s4));
                                                }
                                                if (s4.equals("INFINITE")) {
                                                    item.setInfinite(config.getConfigurationSection(s).getConfigurationSection(s1).getConfigurationSection(s2).getConfigurationSection(s3).getBoolean(s4));
                                                }
                                                if (s4.equals("SECRET AMOUNT")) {
                                                    item.setAmountToDouble(config.getConfigurationSection(s).getConfigurationSection(s1).getConfigurationSection(s2).getConfigurationSection(s3).getInt(s4));
                                                }
                                            }
                                        }
                                    }
                                }
                                if (s2.equals("SELLING")) {
                                    for (String s3 : config.getConfigurationSection(s).getConfigurationSection(s1).getConfigurationSection(s2).getKeys(false)) {
                                        ShopItem item = ShopItem.fromId(shop, Integer.parseInt(s3), true);

                                        if (item != null) {
                                            for (String s4 : config.getConfigurationSection(s).getConfigurationSection(s1).getConfigurationSection(s2).getConfigurationSection(s3).getKeys(false)) {
                                                if (s4.equals("STOCK")) {
                                                    item.setStock(config.getConfigurationSection(s).getConfigurationSection(s1).getConfigurationSection(s2).getConfigurationSection(s3).getInt(s4));
                                                }
                                                if (s4.equals("AMOUNT")) {
                                                    item.setAmount(config.getConfigurationSection(s).getConfigurationSection(s1).getConfigurationSection(s2).getConfigurationSection(s3).getInt(s4));
                                                }
                                                if (s4.equals("PRICE")) {
                                                    item.setPrice(config.getConfigurationSection(s).getConfigurationSection(s1).getConfigurationSection(s2).getConfigurationSection(s3).getDouble(s4));
                                                }
                                                if (s4.equals("LIVE ECO")) {
                                                    item.setLiveEco(config.getConfigurationSection(s).getConfigurationSection(s1).getConfigurationSection(s2).getConfigurationSection(s3).getBoolean(s4));
                                                }
                                                if (s4.equals("SECRET AMOUNT")) {
                                                    item.setAmountToDouble(config.getConfigurationSection(s).getConfigurationSection(s1).getConfigurationSection(s2).getConfigurationSection(s3).getInt(s4));
                                                }
                                                if (s4.equals("DATA")) {
                                                    item.setData((byte) config.getConfigurationSection(s).getConfigurationSection(s1).getConfigurationSection(s2).getConfigurationSection(s3).getInt(s4));
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (s.equals("MANAGERS")) {
                    for (String s1 : config.getConfigurationSection(s).getKeys(false)) {
                        Shop shop = ShopManager.fromString(s1);
                        if (shop != null) {
                            for (String s2 : config.getConfigurationSection(s).getConfigurationSection(s1).getKeys(false)) {
                                if (s2.equals("ADD")) {
                                    for (String s3 : config.getConfigurationSection(s).getConfigurationSection(s1).getConfigurationSection(s2).getKeys(false)) {
                                        shop.addManager(Bukkit.getOfflinePlayer(s3));
                                    }
                                }
                                if (s2.equals("REMOVE")) {
                                    for (String s3 : config.getConfigurationSection(s).getConfigurationSection(s1).getConfigurationSection(s2).getKeys(false)) {
                                        shop.removeManager(Bukkit.getOfflinePlayer(s3));
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §aUpdated the plugin from an Outside Source");

            file.delete();
        }
    }
}
