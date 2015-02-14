package me.moomaxie.BetterShops.Configurations;

import me.moomaxie.BetterShops.Core;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2014. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class Messages {

    private static File file = new File(Bukkit.getPluginManager().getPlugin("BetterShops").getDataFolder(), "Messages.yml");
    public static YamlConfiguration config;

    public static String getString(String name) {
        if (config.isString(name)) {
            return config.getString(name).replaceAll("&", "§");
        } else {
            return "";
        }
    }

    public static void setString(String name, String msg) {
        config.set(name, msg);
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getPrefix() {

        try {
            YamlConfiguration.loadConfiguration(file);
            config = YamlConfiguration.loadConfiguration(file);
        } catch (Exception e) {
        }

        if (config != null) {

            if (config.isString("Prefix")) {
                String name = config.getString("Prefix");
                name = name.replaceAll("&", "§");
                name = name.replaceAll("»", "»");
                name = name.replaceAll("Â", "");
                return name;
            } else {

                config.set("Prefix", "&b&lBetterShops &7&l» &d");
                try {
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "§b§lBetterShops §7§l» §d";
            }

        }
        return "§b§lBetterShops §7§l» §d";
    }

    public static String getChatMessage() {

        try {
            YamlConfiguration.loadConfiguration(file);
            config = YamlConfiguration.loadConfiguration(file);
        } catch (Exception e) {
        }

        if (config != null) {

            if (config.isString("ChatMessage")) {
                String name = config.getString("ChatMessage");
                name = name.replaceAll("&", "§");
                name = name.replaceAll("»", "»");
                name = name.replaceAll("Â", "");
                return name;
            } else {

                config.set("ChatMessage", "&aType a Name/Amount in your chat. Type &cCancel &ato cancel.");
                try {
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "§aType a Name/Amount in your chat. Type §cCancel §ato cancel.";
            }

        }
        return "§aType a Name/Amount in your chat. Type §cCancel §ato cancel.";
    }

    public static String getOpenShopMessage() {

        try {
            config = YamlConfiguration.loadConfiguration(file);
        } catch (Exception e) {
        }

        if (config != null) {

            if (config.isString("OpenShop")) {
                String name = config.getString("OpenShop");
                name = name.replaceAll("&", "§");
                return name;
            } else {
                config.set("OpenShop", "&eOpening Shop..");
                try {
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "§eOpening Shop..";
            }
        }
        return "§eOpening Shop..";
    }

    public static String getCreateShop() {

        try {
            config = YamlConfiguration.loadConfiguration(file);
        } catch (Exception e) {
        }

        if (config != null) {

            if (config.isString("CreateShop")) {
                String name = config.getString("CreateShop");
                name = name.replaceAll("&", "§");
                return name;
            } else {
                config.set("CreateShop", "&aShop Created!");
                try {
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "§aShop Created!";
            }
        }
        return "§aShop Created!";
    }

    public static String getRemoveShop() {

        try {
            config = YamlConfiguration.loadConfiguration(file);
        } catch (Exception e) {
        }

        if (config != null) {

            if (config.isString("DeleteShop")) {
                String name = config.getString("DeleteShop");
                name = name.replaceAll("&", "§");
                return name;
            } else {
                config.set("DeleteShop", "&aShop Removed!");
                try {
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "§aShop Removed!";
            }
        }
        return "§aShop Removed!";
    }

    public static String getDenyRemoveShop() {

        try {
            config = YamlConfiguration.loadConfiguration(file);
        } catch (Exception e) {
        }

        if (config != null) {

            if (config.isString("DenyDeleteShop")) {
                String name = config.getString("DenyDeleteShop");
                name = name.replaceAll("&", "§");
                return name;
            } else {
                config.set("DenyDeleteShop", "&cCannot Remove Shop!");
                try {
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "§cCannot Remove Shop!";
            }
        }
        return "§cCannot Remove Shop!";
    }

    public static String getAddItem() {

        try {
            config = YamlConfiguration.loadConfiguration(file);
        } catch (Exception e) {
        }

        if (config != null) {

            if (config.isString("AddItem")) {
                String name = config.getString("AddItem");
                name = name.replaceAll("&", "§");
                return name;
            } else {
                config.set("AddItem", "&aAdding Item To Shop");
                try {
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "§aAdding Item To Shop";
            }
        }
        return "§aAdding Item To Shop";
    }

    public static String getRemoveItem() {

        try {
            config = YamlConfiguration.loadConfiguration(file);
        } catch (Exception e) {
        }

        if (config != null) {

            if (config.isString("RemoveItem")) {
                String name = config.getString("RemoveItem");
                name = name.replaceAll("&", "§");
                return name;
            } else {
                config.set("RemoveItem", "&aRemoving Item From Shop");
                try {
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "§aRemoving Item From Shop";
            }
        }
        return "§aRemoving Item From Shop";
    }

    public static String getBuyItem() {

        try {
            config = YamlConfiguration.loadConfiguration(file);
        } catch (Exception e) {
        }

        if (config != null) {

            if (config.isString("BuyItem")) {
                String name = config.getString("BuyItem");
                name = name.replaceAll("&", "§");
                return name;
            } else {
                config.set("BuyItem", "&aSuccessfully Bought Item");
                try {
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "§aSuccessfully Bought Item";
            }
        }
        return "§aSuccessfully Bought Item";
    }

    public static String getSellItem() {

        try {
            config = YamlConfiguration.loadConfiguration(file);
        } catch (Exception e) {
        }

        if (config != null) {

            if (config.isString("SellItem")) {
                String name = config.getString("SellItem");
                name = name.replaceAll("&", "§");
                return name;
            } else {
                config.set("SellItem", "&aSuccessfully Sold Item");
                try {
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "§aSuccessfully Sold Item";
            }
        }
        return "§aSuccessfully Sold Item";
    }

    public static String getNotifySellItem() {

        try {
            config = YamlConfiguration.loadConfiguration(file);
        } catch (Exception e) {
        }

        if (config != null) {

            if (config.isString("NotifySell")) {
                String name = config.getString("NotifySell");
                name = name.replaceAll("&", "§");
                return name;
            } else {
                config.set("NotifySell", "&e<Player> &dSold an Item to you!");
                try {
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "§e<Player> §dSold an Item to you!";
            }
        }
        return "§e<Player> §dSold an Item to you!";
    }

    public static String getNotifyBuyItem() {

        try {
            config = YamlConfiguration.loadConfiguration(file);
        } catch (Exception e) {
        }

        if (config != null) {

            if (config.isString("NotifyBuy")) {
                String name = config.getString("NotifyBuy");
                name = name.replaceAll("&", "§");
                return name;
            } else {
                config.set("NotifyBuy", "&e<Player> &dBought an Item from you!");
                try {
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "§e<Player> §dBought an Item from you!";
            }
        }
        return "§e<Player> §dBought an Item from you!";
    }

    public static String getShopFull() {

        try {
            config = YamlConfiguration.loadConfiguration(file);
        } catch (Exception e) {
        }

        if (config != null) {

            if (config.isString("ShopFull")) {
                String name = config.getString("ShopFull");
                name = name.replaceAll("&", "§");
                return name;
            } else {
                config.set("ShopFull", "The Shop is &cFull");
                try {
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "The Shop is §cFull";
            }
        }
        return "The Shop is §cFull";
    }

    public static String getAlreadyAsk() {

        try {
            config = YamlConfiguration.loadConfiguration(file);
        } catch (Exception e) {
        }

        if (config != null) {

            if (config.isString("AlreadyHave")) {
                String name = config.getString("AlreadyHave");
                name = name.replaceAll("&", "§");
                return name;
            } else {
                config.set("AlreadyHave", "You are already asking for this item!");
                try {
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "You are already asking for this item!";
            }
        }
        return "You are already asking for this item!";
    }

    public static String getInvalidItem() {

        try {
            config = YamlConfiguration.loadConfiguration(file);
        } catch (Exception e) {
        }

        if (config != null) {

            if (config.isString("InvalidItem")) {
                String name = config.getString("InvalidItem");
                name = name.replaceAll("&", "§");
                return name;
            } else {
                config.set("InvalidItem", "Cannot find an item that matches");
                try {
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "Cannot find an item that matches";
            }
        }
        return "Cannot find an item that matches";
    }

    public static String getInvalidNumber() {

        try {
            config = YamlConfiguration.loadConfiguration(file);
        } catch (Exception e) {
        }

        if (config != null) {

            if (config.isString("InvalidNumber")) {
                String name = config.getString("InvalidNumber");
                name = name.replaceAll("&", "§");
                return name;
            } else {
                config.set("InvalidNumber", "Not a number");
                try {
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "Not a number";
            }
        }
        return "Not a number";
    }

    public static String getImproperSearch() {

        try {
            config = YamlConfiguration.loadConfiguration(file);
        } catch (Exception e) {
        }

        if (config != null) {

            if (config.isString("ImproperSearch")) {
                String name = config.getString("ImproperSearch");
                name = name.replaceAll("&", "§");
                return name;
            } else {
                config.set("ImproperSearch", "You are not searching for that item");
                try {
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "You are not searching for that item";
            }
        }
        return "You are not searching for that item";
    }

    public static String getChangeStock() {

        try {
            config = YamlConfiguration.loadConfiguration(file);
        } catch (Exception e) {
        }

        if (config != null) {

            if (config.isString("ChangeStock")) {
                String name = config.getString("ChangeStock");
                name = name.replaceAll("&", "§");
                return name;
            } else {
                config.set("ChangeStock", "Changed Stock");
                try {
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "Changed Stock";
            }
        }
        return "Changed Stock";
    }

    public static String getChangeAmount() {

        try {
            config = YamlConfiguration.loadConfiguration(file);
        } catch (Exception e) {
        }

        if (config != null) {

            if (config.isString("ChangeAmount")) {
                String name = config.getString("ChangeAmount");
                name = name.replaceAll("&", "§");
                return name;
            } else {
                config.set("ChangeAmount", "Changed Amount");
                try {
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "Changed Amount";
            }
        }
        return "Changed Amount";
    }

    public static String getChangePrice() {

        try {
            config = YamlConfiguration.loadConfiguration(file);
        } catch (Exception e) {
        }

        if (config != null) {

            if (config.isString("ChangePrice")) {
                String name = config.getString("ChangePrice");
                name = name.replaceAll("&", "§");
                return name;
            } else {
                config.set("ChangePrice", "Changed Price");
                try {
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "Changed Price";
            }
        }
        return "Changed Price";
    }

    public static String getChangeData() {

        try {
            config = YamlConfiguration.loadConfiguration(file);
        } catch (Exception e) {
        }

        if (config != null) {

            if (config.isString("ChangeData")) {
                String name = config.getString("ChangeData");
                name = name.replaceAll("&", "§");
                return name;
            } else {
                config.set("ChangeData", "Changed Data");
                try {
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "Changed Data";
            }
        }
        return "Changed Data";
    }

    public static String getLowStock() {

        try {
            config = YamlConfiguration.loadConfiguration(file);
        } catch (Exception e) {
        }

        if (config != null) {

            if (config.isString("LowStock")) {
                String name = config.getString("LowStock");
                name = name.replaceAll("&", "§");
                return name;
            } else {
                config.set("LowStock", "Stock is not high enough");
                try {
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "Stock is not high enough";
            }

        }
        return "Stock is not high enough";
    }

    public static String getCreationCostDeny() {

        try {
            config = YamlConfiguration.loadConfiguration(file);
        } catch (Exception e) {
        }

        if (config != null) {

            if (config.isString("CreationCostDeny")) {
                String name = config.getString("CreationCostDeny");
                name = name.replaceAll("&", "§");
                return name;
            } else {
                config.set("CreationCostDeny", "&cNot enough money to create a shop. Required: &e$<Amount>&c.");
                try {
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "§cNot enough money to create a shop. Required: §e$<Amount>§c.";
            }
        }
        return "§cNot enough money to create a shop. Required: §e$<Amount>§c.";
    }

    public static String getCreationCostAllow() {

        try {
            config = YamlConfiguration.loadConfiguration(file);
        } catch (Exception e) {
        }

        if (config != null) {

            if (config.isString("CreationCostAllow")) {
                String name = config.getString("CreationCostAllow");
                name = name.replaceAll("&", "§");
                return name;
            } else {
                config.set("CreationCostAllow", "&aRemoved &e$<Amount> &ato create the shop.");
                try {
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "§aRemoved §e$<Amount> §ato create the shop.";
            }
        }
        return "§aRemoved §e$<Amount> §ato create the shop.";
    }

    public static String getWorldGuardDenyShop() {

        try {
            config = YamlConfiguration.loadConfiguration(file);
        } catch (Exception e) {
        }

        if (config != null) {

            if (config.isString("WorldGuardDenyShop")) {
                String name = config.getString("WorldGuardDenyShop");
                name = name.replaceAll("&", "§");
                return name;
            } else {
                config.set("WorldGuardDenyShop", "&cCannot create a shop in this protected area.");
                try {
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "§cCannot create a shop in this protected area.";
            }
        }
        return "§cCannot create a shop in this protected area.";
    }

    public static String getWorldGuardDenyNPC() {

        try {
            config = YamlConfiguration.loadConfiguration(file);
        } catch (Exception e) {
        }

        if (config != null) {

            if (config.isString("WorldGuardDenyNPC")) {
                String name = config.getString("WorldGuardDenyNPC");
                name = name.replaceAll("&", "§");
                return name;
            } else {
                config.set("WorldGuardDenyNPC", "&cCannot create an NPC shop in this protected area.");
                try {
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "§cCannot create an NPC shop in this protected area.";
            }
        }
        return "§cCannot create an NPC shop in this protected area.";
    }

    public static String getInfiniteStock() {

        try {
            config = YamlConfiguration.loadConfiguration(file);
        } catch (Exception e) {
        }

        if (config != null) {

            if (config.isString("InfiniteStock")) {
                String name = config.getString("InfiniteStock");
                name = name.replaceAll("&", "§");
                return name;
            } else {
                config.set("InfiniteStock", "Turned <Value> &dInfinite Stock");
                try {
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "Turned <Value> &dInfinite Stock";
            }
        }
        return "Turned <Value> &dInfinite Stock";
    }

    public static String getNotEnoughItems() {

        try {
            config = YamlConfiguration.loadConfiguration(file);
        } catch (Exception e) {
        }

        if (config != null) {

            if (config.isString("NotEnoughItems")) {
                String name = config.getString("NotEnoughItems");
                name = name.replaceAll("&", "§");
                return name;
            } else {
                config.set("NotEnoughItems", "Not enough items for that stock");
                try {
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "Not enough items for that stock";
            }
        }
        return "Not enough items for that stock";
    }

    public static String getDenyKeeper() {

        try {
            config = YamlConfiguration.loadConfiguration(file);
        } catch (Exception e) {
        }

        if (config != null) {

            if (config.isString("DenyKeeper")) {
                String name = config.getString("DenyKeeper");
                name = name.replaceAll("&", "§");
                return name;
            } else {
                config.set("DenyKeeper", "Shop Keepers Cannot Withdraw Items!");
                try {
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "Shop Keepers Cannot Withdraw Items!";
            }
        }
        return "Shop Keepers Cannot Withdraw Items!";
    }

    public static String getNoPermission() {

        try {
            config = YamlConfiguration.loadConfiguration(file);
        } catch (Exception e) {
        }

        if (config != null) {

            if (config.isString("NoPermission")) {
                String name = config.getString("NoPermission");
                name = name.replaceAll("&", "§");
                return name;
            } else {
                config.set("NoPermission", "Not Enough Permissions");
                try {
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "Not Enough Permissions";
            }
        }
        return "Not Enough Permissions";
    }

    public static String getGainedAmountMessage() {

        try {
            config = YamlConfiguration.loadConfiguration(file);
        } catch (Exception e) {
        }

        if (config != null) {

            if (config.isString("ReceivedAmount")) {
                String name = config.getString("ReceivedAmount");
                name = name.replaceAll("&", "§");
                return name;
            } else {
                config.set("ReceivedAmount", "&dYou have gained &e<Amount> &dDollars");
                try {
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "§dYou have gained §e<Amount> §dDollars";
            }
        }
        return "§dYou have gained §e<Amount> §dDollars";
    }

    public static String getTakenAmountMessage() {

        try {
            config = YamlConfiguration.loadConfiguration(file);
        } catch (Exception e) {
        }

        if (config != null) {

            if (config.isString("TakenAmount")) {
                String name = config.getString("TakenAmount");
                name = name.replaceAll("&", "§");
                return name;
            } else {
                config.set("TakenAmount", "&e<Amount> &dDollars has been taken from your account");
                try {
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "§e<Amount> §dDollars has been taken from your account";
            }
        }
        return "§e<Amount> §dDollars has been taken from your account";
    }

    public static String getServerShop() {

        try {
            config = YamlConfiguration.loadConfiguration(file);
        } catch (Exception e) {
        }

        if (config != null) {

            if (config.isString("ServerShop")) {
                String name = config.getString("ServerShop");
                name = name.replaceAll("&", "§");
                return name;
            } else {
                config.set("ServerShop", "Turned <Value> &dServer Shop Mode");
                try {
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "Turned <Value> §dServer Shop Mode";
            }
        }
        return "Turned <Value> §dServer Shop Mode";
    }

    public static String getNPCShop() {

        try {
            config = YamlConfiguration.loadConfiguration(file);
        } catch (Exception e) {
        }

        if (config != null) {

            if (config.isString("NPCShop")) {
                String name = config.getString("NPCShop");
                name = name.replaceAll("&", "§");
                return name;
            } else {
                config.set("NPCShop", "Turned <Value> &dNPC Shop Mode");
                try {
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "Turned <Value> §dNPC Shop Mode";
            }
        }
        return "Turned <Value> §dNPC Shop Mode";
    }

    public static void changeMessages() {
        boolean found3 = false;

        if (Core.getCore().getDataFolder() != null) {

            if (Core.getCore().getDataFolder().listFiles() == null) {
                Core.getCore().getDataFolder().mkdir();
            }
            for (File f : Core.getCore().getDataFolder().listFiles()) {
                if (f.getName().equals("Messages.yml")) {
                    found3 = true;
                }
            }
        }

        if (!found3) {

            File fil = new File(Core.getCore().getDataFolder().getParent(), "BetterShops.jar");

            java.util.jar.JarFile jar = null;
            try {
                jar = new java.util.jar.JarFile(fil);
            } catch (IOException e) {
                Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §cImproper Jar Name, Rename the .Jar to 'BetterShops.jar'. Plugin Disabling!");
                Bukkit.getPluginManager().disablePlugin(Core.getCore());
            }
            java.util.Enumeration enumEntries = jar.entries();
            while (enumEntries.hasMoreElements()) {
                java.util.jar.JarEntry file = (java.util.jar.JarEntry) enumEntries.nextElement();
                if (file.getName().equals("Messages.yml")) {
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
                        Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §eCreated the messages file.");

                    } catch (Exception e) {

                    }
                }
            }
        } else {


            config = YamlConfiguration.loadConfiguration(file);

            HashMap<String, Object> hash = new HashMap<>();

            for (String s : config.getKeys(true)) {
                if (!s.contains("Version"))
                    hash.put(s, config.get(s));
            }

            if (config.isString("Version")) {
                String d = config.getString("Version");

                if (!d.equals(Core.getCore().getDescription().getVersion())) {
                    file.delete();
                    File fil = new File(Core.getCore().getDataFolder().getParent(), "BetterShops.jar");

                    java.util.jar.JarFile jar = null;
                    try {
                        jar = new java.util.jar.JarFile(fil);
                    } catch (IOException e) {
                        Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §cImproper Jar Name, Rename the .Jar to 'BetterShops.jar'. Plugin Disabling!");
                        Bukkit.getPluginManager().disablePlugin(Core.getCore());
                    }
                    java.util.Enumeration enumEntries = jar.entries();
                    while (enumEntries.hasMoreElements()) {
                        java.util.jar.JarEntry file = (java.util.jar.JarEntry) enumEntries.nextElement();
                        if (file.getName().equals("Messages.yml")) {
                            File f = new File(Core.getCore().getDataFolder() + File.separator + file.getName());
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
                                Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §eSwitched the messages file to it's new version");

                            } catch (Exception e) {

                            }
                        }
                    }
                }

            } else {

                file.delete();

                File fil = new File(Core.getCore().getDataFolder().getParent(), "BetterShops.jar");

                java.util.jar.JarFile jar = null;
                try {
                    jar = new java.util.jar.JarFile(fil);
                } catch (IOException e) {
                    Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §cImproper Jar Name, Rename the .Jar to 'BetterShops.jar'. Plugin Disabling!");
                    Bukkit.getPluginManager().disablePlugin(Core.getCore());
                }
                java.util.Enumeration enumEntries = jar.entries();
                while (enumEntries.hasMoreElements()) {
                    java.util.jar.JarEntry file = (java.util.jar.JarEntry) enumEntries.nextElement();
                    if (file.getName().equals("Messages.yml")) {
                        File f = new File(Core.getCore().getDataFolder() + File.separator + file.getName());
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
                            Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §eSwitched the messages file to it's new version");

                        } catch (Exception e) {

                        }
                    }
                }



            }
            file = new File(Bukkit.getPluginManager().getPlugin("BetterShops").getDataFolder(), "Messages.yml");


            config = YamlConfiguration.loadConfiguration(file);

            for (String s : hash.keySet()) {
                if (config.isString(s)) {
                    config.set(s, hash.get(s));
                }
            }
            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        file = new File(Bukkit.getPluginManager().getPlugin("BetterShops").getDataFolder(), "Messages.yml");

        config = YamlConfiguration.loadConfiguration(file);
    }
}
