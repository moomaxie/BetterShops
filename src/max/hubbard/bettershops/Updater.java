package max.hubbard.bettershops;

import max.hubbard.bettershops.Configurations.Config;
import max.hubbard.bettershops.Configurations.Language;
import max.hubbard.bettershops.Listeners.CreateShop;
import max.hubbard.bettershops.Shops.FileShop;
import max.hubbard.bettershops.Shops.Items.FileShopItem;
import max.hubbard.bettershops.Shops.Items.SQLShopItem;
import max.hubbard.bettershops.Shops.Items.ShopItem;
import max.hubbard.bettershops.Shops.Shop;
import max.hubbard.bettershops.Shops.Types.Holo.CreateHologram;
import max.hubbard.bettershops.Shops.Types.Holo.DeleteHoloShop;
import max.hubbard.bettershops.Shops.Types.NPC.*;
import max.hubbard.bettershops.Utils.ShopDeleter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;

import java.io.File;
import java.util.ArrayList;

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


                            CreateShop.createShopExternally(new Location(w, x, y, z), s1, Bukkit.getOfflinePlayer(owner));
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
                            ShopDeleter.deleteShopExternally(shop);
                        }
                    }
                }

                if (s.equals("CONFIG")) {
                    Config.file = new File(Bukkit.getPluginManager().getPlugin("BetterShops").getDataFolder(), "config.yml");
                    Config.config = YamlConfiguration.loadConfiguration(Config.file);
                }

                if (s.equals("LANGUAGE")) {
                    for (String s1 : config.getConfigurationSection(s).getKeys(false)) {

                        File f = new File(Bukkit.getPluginManager().getPlugin("BetterShops").getDataFolder(), "Language/" + s1 + ".yml");
                        Language.files.put(s1, f);
                        YamlConfiguration c = YamlConfiguration.loadConfiguration(f);
                        Language.configs.put(s1, c);

                    }
                }

                if (s.equals("SHOP")) {
                    for (String s1 : config.getConfigurationSection(s).getKeys(false)) {
                        Shop shop = ShopManager.fromString(s1);
                        if (shop != null) {
                            for (String s2 : config.getConfigurationSection(s).getConfigurationSection(s1).getKeys(false)) {
                                if (s2.equals("DESCRIPTION")) {
                                    shop.setObject("Description", config.getConfigurationSection(s).getConfigurationSection(s1).getString(s2));
                                }
                                if (s2.equals("OPEN")) {
                                    shop.setOpen(config.getConfigurationSection(s).getConfigurationSection(s1).getBoolean(s2));
                                }
                                if (s2.equals("NOTIFY")) {
                                    shop.setObject("Notify", config.getConfigurationSection(s).getConfigurationSection(s1).getBoolean(s2));
                                }
                                if (s2.equals("FRAME")) {
                                    shop.setObject("Frame", config.getConfigurationSection(s).getConfigurationSection(s1).getInt(s2));
                                }
                                if (s2.equals("SERVER SHOP")) {
                                    shop.setObject("Server", config.getConfigurationSection(s).getConfigurationSection(s1).getBoolean(s2));
                                }
                                if (s2.equals("NPC SHOP")) {
                                    if (config.getConfigurationSection(s).getConfigurationSection(s1).getBoolean(s2)) {

                                        if (Core.useCitizens()){
                                            ShopsNPC npc = new CitizensShop(new EntityInfo(EntityType.VILLAGER,new ArrayList<String>(),false,false,false),shop);
                                            npc.spawn();
                                            NPCManager.addNPCShop(npc);
                                        } else {
                                            NPCManager.addNPCShop(new NPCShop(EntityType.VILLAGER, new ArrayList<String>(), shop,false,false,false));
                                        }


                                        shop.setObject("NPC", true);
                                        shop.setOpen(true);

                                    } else {
                                        if (shop.isNPCShop() || shop.getNPCShop() != null) {
                                            DeleteNPC.deleteNPC(shop.getNPCShop());
                                        }
                                    }
                                }
                                if (s2.equals("HOLO SHOP")) {
                                    if (config.getConfigurationSection(s).getConfigurationSection(s1).getBoolean(s2)) {
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
                                        ShopItem item;
                                        if (shop instanceof FileShop) {
                                            item = FileShopItem.loadShopItem(shop, Integer.parseInt(s3),false);
                                        } else {
                                            item = SQLShopItem.fromId(shop,Integer.parseInt(s3),false);
                                        }

                                        if (item != null) {

                                            for (String s4 : config.getConfigurationSection(s).getConfigurationSection(s1).getConfigurationSection(s2).getConfigurationSection(s3).getKeys(false)) {
                                                if (s4.equals("STOCK")) {
                                                    item.setObject("Stock", config.getConfigurationSection(s).getConfigurationSection(s1).getConfigurationSection(s2).getConfigurationSection(s3).getInt(s4));
                                                }
                                                if (s4.equals("AMOUNT")) {
                                                    item.setObject("Amount", config.getConfigurationSection(s).getConfigurationSection(s1).getConfigurationSection(s2).getConfigurationSection(s3).getInt(s4));
                                                }
                                                if (s4.equals("PRICE")) {
                                                    item.setPrice(config.getConfigurationSection(s).getConfigurationSection(s1).getConfigurationSection(s2).getConfigurationSection(s3).getDouble(s4));
                                                }
                                                if (s4.equals("LIVE ECO")) {
                                                    item.setObject("LiveEconomy", config.getConfigurationSection(s).getConfigurationSection(s1).getConfigurationSection(s2).getConfigurationSection(s3).getBoolean(s4));
                                                }
                                                if (s4.equals("INFINITE")) {
                                                    item.setObject("Infinite", config.getConfigurationSection(s).getConfigurationSection(s1).getConfigurationSection(s2).getConfigurationSection(s3).getBoolean(s4));
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
                                        ShopItem item;
                                        if (shop instanceof FileShop) {
                                            item = FileShopItem.loadShopItem(shop, Integer.parseInt(s3),false);
                                        } else {
                                            item = SQLShopItem.fromId(shop,Integer.parseInt(s3),false);
                                        }

                                        if (item != null) {

                                            for (String s4 : config.getConfigurationSection(s).getConfigurationSection(s1).getConfigurationSection(s2).getConfigurationSection(s3).getKeys(false)) {
                                                if (s4.equals("STOCK")) {
                                                    item.setObject("Stock",config.getConfigurationSection(s).getConfigurationSection(s1).getConfigurationSection(s2).getConfigurationSection(s3).getInt(s4));
                                                }
                                                if (s4.equals("AMOUNT")) {
                                                    item.setObject("Amount",config.getConfigurationSection(s).getConfigurationSection(s1).getConfigurationSection(s2).getConfigurationSection(s3).getInt(s4));
                                                }
                                                if (s4.equals("PRICE")) {
                                                    item.setPrice(config.getConfigurationSection(s).getConfigurationSection(s1).getConfigurationSection(s2).getConfigurationSection(s3).getDouble(s4));
                                                }
                                                if (s4.equals("LIVE ECO")) {
                                                    item.setObject("LiveEconomy",config.getConfigurationSection(s).getConfigurationSection(s1).getConfigurationSection(s2).getConfigurationSection(s3).getBoolean(s4));
                                                }
                                                if (s4.equals("INFINITE")) {
                                                    item.setObject("Infinite",config.getConfigurationSection(s).getConfigurationSection(s1).getConfigurationSection(s2).getConfigurationSection(s3).getBoolean(s4));
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
                                        shop.addKeeper(Bukkit.getOfflinePlayer(s3));
                                    }
                                }
                                if (s2.equals("REMOVE")) {
                                    for (String s3 : config.getConfigurationSection(s).getConfigurationSection(s1).getConfigurationSection(s2).getKeys(false)) {
                                        shop.removeKeeper(Bukkit.getOfflinePlayer(s3));
                                    }
                                }
                            }
                        }
                    }
                }
                if (s.equals("BLACKLIST")) {
                    for (String s1 : config.getConfigurationSection(s).getKeys(false)) {
                        Shop shop = ShopManager.fromString(s1);
                        if (shop != null) {
                            for (String s2 : config.getConfigurationSection(s).getConfigurationSection(s1).getKeys(false)) {
                                if (s2.equals("ADD")) {
                                    for (String s3 : config.getConfigurationSection(s).getConfigurationSection(s1).getConfigurationSection(s2).getKeys(false)) {
                                        shop.addBlacklist(Bukkit.getOfflinePlayer(s3));
                                    }
                                }
                                if (s2.equals("REMOVE")) {
                                    for (String s3 : config.getConfigurationSection(s).getConfigurationSection(s1).getConfigurationSection(s2).getKeys(false)) {
                                        shop.removeBlacklist(Bukkit.getOfflinePlayer(s3));
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
