package max.hubbard.bettershops.Utils;

import max.hubbard.bettershops.Core;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2015. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class Conversion {

    public static boolean checkConversion() {
        File file = new File(Core.getCore().getDataFolder(), "Shops/");

        if (!file.exists()) {
            file.mkdirs();
            return false;
        }

        for (File f : file.listFiles()) {
            if (f.isFile() && f.getName().contains(".yml")) {
                return true;
            }
        }
        return false;
    }

    public static void startConversion() {
        Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §aStarting Conversion of Shops (may take awhile)");
        File file = new File(Core.getCore().getDataFolder(), "Shops/");
        int total = 0;

        for (File f : file.listFiles()) {
            if (f.isFile() && f.getName().contains(".yml")) {
                total++;
            }
        }
        int i = 0;
        for (File f : file.listFiles()) {
            if (f.isFile() && f.getName().contains(".yml")) {
                File file1 = new File(f.getParentFile(), f.getName().substring(0, f.getName().length() - 4));
                if (!file1.exists())
                    file1.mkdirs();

                YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

                for (String s : config.getKeys(false)) {
                    String d = s;
                    s = s.replaceAll(Pattern.quote("/"), "");
                    File f1 = new File(file1, s + ".yml");

                    if (!f1.exists()) {
                        try {
                            f1.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    YamlConfiguration c = YamlConfiguration.loadConfiguration(f1);
                    for (String s1 : config.getConfigurationSection(d).getKeys(true)) {
                        c.set(s1, config.getConfigurationSection(d).get(s1));
                    }

                    try {
                        c.save(f1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    i++;
                    if (i == (total * .10)) {
                        Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §a10%");
                    }
                    if (i == (total * .5)) {
                        Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §a50%");
                    }
                    if (i == (total * .25)) {
                        Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §a25%");
                    }
                    if (i == (total * .75)) {
                        Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §a75%");
                    }
                    if (i == (total * .9)) {
                        Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §a90%");
                    }
                }
                f.delete();
            }
        }
        Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §aDone!");
    }
}
