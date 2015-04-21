package me.moomaxie.BetterShops.Configurations;

import me.moomaxie.BetterShops.Core;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.jar.JarFile;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2014. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class Config {

    public static File file = new File(Bukkit.getPluginManager().getPlugin("BetterShops").getDataFolder(), "config.yml");
    public static FileConfiguration config = YamlConfiguration.loadConfiguration(file);

    public static boolean autoAddItems() {
        return config.isBoolean("Auto Add") && config.getBoolean("Auto Add");
    }

    public static boolean getAllowChest() {
        return config.isBoolean("UseChests") && config.getBoolean("UseChests");
    }

    public static void setAllowChests(boolean b) {
        config.set("UseChests", b);

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setAutoAddItems(boolean b) {
        config.set("Auto Add", b);

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean usePerms() {
        return config.isBoolean("Permissions") && config.getBoolean("Permissions");
    }

    public static void setPermissions(boolean b) {
        config.set("Permissions", b);

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean useTransactions() {
        return config.isBoolean("UseTransactions") && config.getBoolean("UseTransactions");
    }

    public static void setUseTransactions(boolean b) {
        config.set("UseTransactions", b);

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean useLiveEco() {
        return config.isBoolean("UseLiveEco") && config.getBoolean("UseLiveEco");
    }

    public static void setUseLiveEco(boolean b) {
        config.set("UseLiveEco", b);

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean useHoloShops() {
        return config.isBoolean("HoloShops") && config.getBoolean("HoloShops");
    }

    public static void setUseHoloShops(boolean b) {
        config.set("HoloShops", b);

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean useSellingShop() {
        return config.isBoolean("SellingShops") && config.getBoolean("SellingShops");
    }

    public static void setUseSellingShops(boolean b) {
        config.set("SellingShops", b);

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean useDeleteByBreak() {
        return config.isBoolean("DeleteByBreak") && config.getBoolean("DeleteByBreak");
    }

    public static void setDeleteByBreak(boolean b) {
        config.set("DeleteByBreak", b);

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean useLimit() {
        return config.isBoolean("Creation Limit") && config.getBoolean("Creation Limit");
    }

    public static void setCreationLimit(boolean b) {
        config.set("Creation Limit", b);

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean useTitles() {
        return config.isBoolean("Titles") && config.getBoolean("Titles");
    }

    public static void setTitles(boolean b) {
        config.set("Titles", b);

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean useCreationCost() {
        return config.isBoolean("CostOnShops") && config.getBoolean("CostOnShops");
    }

    public static void setCostOnShops(boolean b) {
        config.set("CostOnShops", b);

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean useNPCs() {
        return config.isBoolean("EnableNPC") && config.getBoolean("EnableNPC");
    }

    public static void setEnableNPC(boolean b) {
        config.set("EnableNPC", b);

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean useAnvil() {
        return config.isBoolean("Anvil") && config.getBoolean("Anvil");
    }

    public static void setAnvil(boolean b) {
        config.set("Anvil", b);

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean useMetrics() {
        return config.isBoolean("Metrics") && config.getBoolean("Metrics");
    }

    public static void setMetrics(boolean b) {
        config.set("Metrics", b);

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean useAllowFlag() {
        return config.isBoolean("EnableAllowShopsFlag") && config.getBoolean("EnableAllowShopsFlag");
    }

    public static void setEnableAllowShopsFlag(boolean b) {
        config.set("EnableAllowShopsFlag", b);

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean useNPCOverride() {
        return config.isBoolean("NPCOverride") && config.getBoolean("NPCOverride");
    }

    public static void setNPCOverride(boolean b) {
        config.set("NPCOverride", b);

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean useWhenClosed() {
        return config.isBoolean("UseOnClose") && config.getBoolean("UseOnClose");
    }

    public static void setUseOnClose(boolean b) {
        config.set("UseOnClose", b);

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getLimit() {
        if (config.isInt("Limit") || config.isDouble("Limit")) {
            return config.getInt("Limit");
        } else {
            return 5;
        }
    }

    public static void setLimit(float b) {
        config.set("Limit", b);

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        file = new File(Bukkit.getPluginManager().getPlugin("BetterShops").getDataFolder(), "config.yml");

        config = YamlConfiguration.loadConfiguration(file);
    }

    public static String getDefaultPriceAsString() {
        return (new BigDecimal(getDefaultPrice()).toPlainString());
    }

    public static String getMaxPriceAsString() {
        return (new BigDecimal(getMaxPrice()).toPlainString());
    }

    public static double getMaxPrice() {
        if (config.isDouble("MaxPrice")) {
            return config.getDouble("MaxPrice");
        } else {
            if (config.getString("MaxPrice").contains(",")) {

                return Double.parseDouble(new DecimalFormat("#.00").format(Double.parseDouble(config.getString("MaxPrice").replaceFirst(",", ".").replaceAll(",", ""))));

            } else {
                return 1000000000.00;
            }
        }

    }

    public static void setMaxPrice(double b) {
        config.set("MaxPrice", b);

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        file = new File(Bukkit.getPluginManager().getPlugin("BetterShops").getDataFolder(), "config.yml");

        config = YamlConfiguration.loadConfiguration(file);
    }

    public static double getCreationCost() {
        if (config.isDouble("CostForShops") || config.isInt("CostForShops")) {
            return config.getDouble("CostForShops");
        } else {
            return 19.99;
        }
    }

    public static double getDefaultPrice() {
        if (config.isDouble("DefaultPrice")) {
            return config.getDouble("DefaultPrice");
        } else {
            if (config.getString("DefaultPrice").contains(",")) {
                return Double.parseDouble(new DecimalFormat("#.00").format(Double.parseDouble(config.getString("DefaultPrice").replaceFirst(",", ".").replaceAll(",", ""))));
            } else {
                return 5.00;
            }
        }
    }

    public static void setDefaultPrice(double b) {
        config.set("DefaultPrice", b);


        try {
            config.save(file);

        } catch (IOException e) {
            e.printStackTrace();
        }

        file = new File(Bukkit.getPluginManager().getPlugin("BetterShops").getDataFolder(), "config.yml");

        config = YamlConfiguration.loadConfiguration(file);


    }


    public static boolean useNPC(EntityType type) {
        return config.getConfigurationSection("NPC").isBoolean(WordsCapitalizer.capitalizeEveryWord(type.name().replaceAll("_", " ")))
                && config.getConfigurationSection("NPC").getBoolean(WordsCapitalizer.capitalizeEveryWord(type.name().replaceAll("_", " ")));
    }

    public static void setUseNPC(EntityType type, boolean b) {
        config.getConfigurationSection("NPC").set(WordsCapitalizer.capitalizeEveryWord(type.name().replaceAll("_", " ")), b);

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setCostForShops(double b) {
        config.set("CostForShops", b);

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        file = new File(Bukkit.getPluginManager().getPlugin("BetterShops").getDataFolder(), "config.yml");

        config = YamlConfiguration.loadConfiguration(file);

    }

    public static List<EntityType> getAllNPCs() {

        List<EntityType> ent = new ArrayList<>();

        for (String s : config.getConfigurationSection("NPC").getKeys(false)) {
            if (isInEnum(s.toUpperCase().replaceAll(" ", "_"),EntityType.class)) {
                ent.add(EntityType.valueOf(s.toUpperCase().replaceAll(" ", "_")));
            }
        }

        return ent;
    }

    public static List<EntityType> getNPCs() {

        List<EntityType> ent = new ArrayList<>();



        for (String s : config.getConfigurationSection("NPC").getKeys(false)) {
            if (config.getConfigurationSection("NPC").getBoolean(s)) {
                if (isInEnum(s.toUpperCase().replaceAll(" ", "_"),EntityType.class)) {
                    ent.add(EntityType.valueOf(s.toUpperCase().replaceAll(" ", "_")));
                }
            }
        }

        return ent;
    }

    public static <E extends Enum<E>> boolean isInEnum(String value, Class<E> enumClass) {
        for (E e : enumClass.getEnumConstants()) {
            if(e.name().equals(value)) { return true; }
        }
        return false;
    }

    public static void changeConfig() {
        boolean found3 = false;

        if (Core.getCore().getDataFolder() != null) {

            if (Core.getCore().getDataFolder().listFiles() == null) {
                Core.getCore().getDataFolder().mkdir();
            }
            for (File f : Core.getCore().getDataFolder().listFiles()) {
                if (f.getName().equals("config.yml")) {
                    found3 = true;
                }
            }
        }

        if (!found3) {

            File fil = Core.getCore().getFile();

            JarFile jar = null;
            try {
                jar = new java.util.jar.JarFile(fil);
            } catch (IOException e) {
                Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §cCan't find the jar file, Rename the .Jar to 'BetterShops.jar'. Plugin Disabling!");
                Bukkit.getPluginManager().disablePlugin(Core.getCore());
            }
            java.util.Enumeration enumEntries = jar.entries();
            while (enumEntries.hasMoreElements()) {
                java.util.jar.JarEntry file = (java.util.jar.JarEntry) enumEntries.nextElement();
                if (file.getName().equals("config.yml")) {
                    java.io.File f = new java.io.File(Core.getCore().getDataFolder() + java.io.File.separator + file.getName());
                    if (file.isDirectory()) { // if its a directory, create it
                        f.mkdir();
                        continue;
                    }
                    try {
                        java.io.InputStream is = jar.getInputStream(file); // get the input stream
                        java.io.FileOutputStream fos = new java.io.FileOutputStream(f);
                        while (is.available() > 0) {  // write contents of 'is' to 'fos'
                            fos.write(is.read());
                        }
                        fos.close();
                        is.close();
                        Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §eCreated the config file.");

                    } catch (Exception e) {

                    }
                }
            }
        } else {

            if (config != null) {
                HashMap<String, Object> hash = new HashMap<>();

                for (String s : config.getKeys(true)) {
                    if (!s.contains("Version") && !s.contains("NPC"))
                        hash.put(s, config.get(s));
                }

                if (config.isString("Version")) {
                    String d = config.getString("Version");

                    if (!d.equals(Core.getCore().getDescription().getVersion())) {
                        if (file.delete()) {
                            File fil = Core.getCore().getFile();

                            JarFile jar = null;
                            try {
                                jar = new java.util.jar.JarFile(fil);
                            } catch (IOException e) {
                                Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §cImproper Jar Name, Rename the .Jar to 'BetterShops.jar'. Plugin Disabling!");
                                Bukkit.getPluginManager().disablePlugin(Core.getCore());
                            }
                            java.util.Enumeration enumEntries = jar.entries();
                            while (enumEntries.hasMoreElements()) {
                                java.util.jar.JarEntry file = (java.util.jar.JarEntry) enumEntries.nextElement();
                                if (file.getName().equals("config.yml")) {
                                    java.io.File f = new java.io.File(Core.getCore().getDataFolder() + java.io.File.separator + file.getName());
                                    if (file.isDirectory()) { // if its a directory, create it
                                        f.mkdir();
                                        continue;
                                    }
                                    try {
                                        java.io.InputStream is = jar.getInputStream(file); // get the input stream
                                        java.io.FileOutputStream fos = new java.io.FileOutputStream(f);
                                        while (is.available() > 0) {  // write contents of 'is' to 'fos'
                                            fos.write(is.read());
                                        }

                                        fos.close();
                                        is.close();

                                        Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §eChanged the Config file.");

                                    } catch (Exception e) {

                                    }
                                }
                            }
                        }
                    }

                } else {

                    file.delete();

                    File fil = Core.getCore().getFile();

                    JarFile jar = null;
                    try {
                        jar = new java.util.jar.JarFile(fil);
                    } catch (IOException e) {
                        Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §cImproper Jar Name, Rename the .Jar to 'BetterShops.jar'. Plugin Disabling!");
                        Bukkit.getPluginManager().disablePlugin(Core.getCore());
                    }
                    java.util.Enumeration enumEntries = jar.entries();
                    while (enumEntries.hasMoreElements()) {
                        java.util.jar.JarEntry file = (java.util.jar.JarEntry) enumEntries.nextElement();
                        if (file.getName().equals("config.yml")) {
                            java.io.File f = new java.io.File(Core.getCore().getDataFolder() + java.io.File.separator + file.getName());
                            if (file.isDirectory()) { // if its a directory, create it
                                f.mkdir();
                                continue;
                            }
                            try {
                                java.io.InputStream is = jar.getInputStream(file); // get the input stream
                                java.io.FileOutputStream fos = new java.io.FileOutputStream(f);
                                while (is.available() > 0) {  // write contents of 'is' to 'fos'
                                    fos.write(is.read());
                                }
                                fos.close();
                                is.close();
                                Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §eChanged the Config file.");

                            } catch (Exception e) {

                            }
                        }
                    }


                }
                file = new File(Bukkit.getPluginManager().getPlugin("BetterShops").getDataFolder(), "config.yml");

                config = YamlConfiguration.loadConfiguration(file);
                for (String s : hash.keySet()) {
                    config.set(s, hash.get(s));
                    try {
                        config.save(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        file = new File(Bukkit.getPluginManager().getPlugin("BetterShops").getDataFolder(), "config.yml");

        config = YamlConfiguration.loadConfiguration(file);

        config.options().copyHeader(true);
        config.options().copyDefaults(true);


    }
}
