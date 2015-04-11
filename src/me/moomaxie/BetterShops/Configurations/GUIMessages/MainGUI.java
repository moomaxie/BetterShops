package me.moomaxie.BetterShops.Configurations.GUIMessages;

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
public class MainGUI {

    public static File file = new File(Bukkit.getPluginManager().getPlugin("BetterShops").getDataFolder(), "Language/MainGUI.yml");
    public static YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

    public static String getString(String name){
        if (config.isString(name)){
            return config.getString(name).replaceAll("&", "§");
        } else {
            return "";
        }
    }

    public static void setString(String name, String msg){
        config.set(name,msg);
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void changeMainGUIConfig() {
        boolean found3 = false;

        if (Core.getCore().getDataFolder() != null) {

            if (Core.getCore().getDataFolder().listFiles() == null) {
                Core.getCore().getDataFolder().mkdir();
            }
            for (File f : Core.getCore().getDataFolder().listFiles()) {
                if (f.getName().equals("Language")) {
                    for (File file1 : f.listFiles()) {
                        if (file1.getName().equals("MainGUI.yml")) {
                            found3 = true;
                        }
                    }
                }
            }
        }

        if (!found3) {

            File fil = Core.getCore().getFile();

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
                if (file.getName().equals("Language/MainGUI.yml")) {
                    java.io.File f = new java.io.File(Core.getCore().getDataFolder() + java.io.File.separator + file.getName());
                    try {
                        MainGUI.file.createNewFile();
                    } catch (IOException e) {

                    }
                    if (file.isDirectory()) { // if its a directory, create it
                        f.mkdir();
                        continue;
                    } else {
                        if (!f.exists()){
                            if (f.getParentFile().mkdirs()) {
                                try {
                                    f.createNewFile();
                                } catch (IOException e) {

                                }
                            }
                        }
                    }
                    try {
                        java.io.InputStream is = jar.getInputStream(file); // get the input stream
                        java.io.FileOutputStream fos = new java.io.FileOutputStream(f);
                        while (is.available() > 0) {  // write contents of 'is' to 'fos'
                            fos.write(is.read());
                        }
                        fos.close();
                        is.close();
                        Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §eCreated the Main GUI Language file.");

                    } catch (Exception ignored) {
                    }
                }
            }
        } else {

            if (config != null) {
                HashMap<String, Object> hash = new HashMap<>();

                for (String s : config.getKeys(false)) {
                    if (!s.contains("Version")) {
                        hash.put(s, config.get(s));
                    }
                }

                if (config.isString("Version")) {
                    String d = config.getString("Version");

                    if (!d.equals(Core.getCore().getDescription().getVersion())) {
                        if (file.delete()) {
                            File fil = Core.getCore().getFile();

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
                                if (file.getName().equals("Language/MainGUI.yml")) {
                                    java.io.File f = new java.io.File(Core.getCore().getDataFolder() + java.io.File.separator + file.getName());
                                    try {
                                        MainGUI.file.createNewFile();
                                    } catch (IOException e) {

                                    }
                                    if (file.isDirectory()) { // if its a directory, create it
                                        f.mkdir();
                                        continue;
                                    } else {
                                        if (!f.exists()){
                                            if (f.getParentFile().mkdirs()) {
                                                try {
                                                    f.createNewFile();
                                                } catch (IOException e) {

                                                }
                                            }
                                        }
                                    }
                                    try {
                                        java.io.InputStream is = jar.getInputStream(file); // get the input stream
                                        java.io.FileOutputStream fos = new java.io.FileOutputStream(f);
                                        while (is.available() > 0) {  // write contents of 'is' to 'fos'
                                            fos.write(is.read());
                                        }
                                        fos.close();
                                        is.close();
                                        Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §eChanged the MainGUI Language file.");

                                    } catch (Exception ignored) {
                                    }
                                }
                            }
                        }
                    }

                } else {

                    file.delete();

                    File fil = Core.getCore().getFile();

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
                        if (file.getName().equals("Language/MainGUI.yml")) {
                            java.io.File f = new java.io.File(Core.getCore().getDataFolder() + java.io.File.separator + file.getName());
                            try {
                                MainGUI.file.createNewFile();
                            } catch (IOException e) {

                            }
                            if (file.isDirectory()) { // if its a directory, create it
                                f.mkdir();
                                continue;
                            } else {
                                if (!f.exists()){
                                    if (f.getParentFile().mkdirs()) {
                                        try {
                                            f.createNewFile();
                                        } catch (IOException e) {

                                        }
                                    }
                                }
                            }
                            try {
                                java.io.InputStream is = jar.getInputStream(file); // get the input stream
                                java.io.FileOutputStream fos = new java.io.FileOutputStream(f);
                                while (is.available() > 0) {  // write contents of 'is' to 'fos'
                                    fos.write(is.read());
                                }
                                fos.close();
                                is.close();
                                Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §eChanged the MainGUI Language file.");

                            } catch (Exception ignored) {
                            }
                        }
                    }
                }
                file = new File(Bukkit.getPluginManager().getPlugin("BetterShops").getDataFolder(), "Language/MainGUI.yml");

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
        file = new File(Bukkit.getPluginManager().getPlugin("BetterShops").getDataFolder(), "Language/MainGUI.yml");

        config = YamlConfiguration.loadConfiguration(file);
    }
}
