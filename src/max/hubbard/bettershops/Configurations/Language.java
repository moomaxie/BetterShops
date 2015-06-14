package max.hubbard.bettershops.Configurations;

import max.hubbard.bettershops.Core;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2015. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class Language {

    public static HashMap<String, YamlConfiguration> configs = new HashMap<String, YamlConfiguration>();
    public static HashMap<String, File> files = new HashMap<String, File>();

    public static YamlConfiguration getConfig(String file) {
        return configs.get(file);
    }

    public static File getFile(String file) {
        return files.get(file);
    }

    public static String getString(String file, String s) {
        if (configs.containsKey(file)) {
            return configs.get(file).getString(s).replaceAll("&", "§").replaceAll("»", "»");
        } else {
            File f = new File(Bukkit.getPluginManager().getPlugin("BetterShops").getDataFolder(), "Language/" + file + ".yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(f);
            configs.put(file, config);
            files.put(file, f);
            return config.getString(s).replaceAll("&", "§").replaceAll("»", "»");
        }
    }

    public static void setString(String file, String s, String message) {
        if (configs.containsKey(file)) {
            configs.get(file).set(s, message);
            try {
                configs.get(file).save(files.get(file));
            } catch (IOException ignored) {

            }
        } else {
            File f = new File(Bukkit.getPluginManager().getPlugin("BetterShops").getDataFolder(), "Language/" + file + ".yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(f);
            configs.put(file, config);
            files.put(file, f);
            config.set(s, message);
            try {
                configs.get(file).save(files.get(file));
            } catch (IOException ignored) {

            }
        }
    }

    public static void moveMessagesFile() {
        File f = new File(Core.getCore().getDataFolder(), "Messages.yml");
        if (f.exists()) {
            File f2 = new File(Core.getCore().getDataFolder(), "Language/Messages.yml");
            try {
                Files.copy(f.toPath(), f2.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                f.delete();
            }
        }
    }

    public static void updateFiles() {

        File fil = Core.getCore().getFile();

        java.util.jar.JarFile jar = null;
        try {
            jar = new java.util.jar.JarFile(fil);
        } catch (IOException e) {
            try {
                jar = new java.util.jar.JarFile(new File(Core.getCore().getDataFolder().getParent(), "BetterShops.jar"));
            } catch (Exception e7) {
                Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §cCan't find the jar file, Rename the .Jar to 'BetterShops.jar'. Plugin Disabling!");
                Bukkit.getPluginManager().disablePlugin(Core.getCore());
                return;
            }
        }

        java.util.Enumeration enumEntries = jar.entries();
        while (enumEntries.hasMoreElements()) {
            java.util.jar.JarEntry file = (java.util.jar.JarEntry) enumEntries.nextElement();

            File f = new File(Core.getCore().getDataFolder(), file.getName());

            if (file.getName().contains(".yml") && !file.getName().equals("plugin.yml")) {
                if (f.exists()) {
                    YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

                    HashMap<String, Object> hash = new HashMap<>();

                    String d = null;

                    for (String s : config.getKeys(true)) {
                        if (!s.contains("Version"))
                            hash.put(s, config.get(s));
                        else
                            d = config.getString(s);
                    }

                    if (d != null) {

                        if (!d.equals(Core.getCore().getDescription().getVersion())) {
                            try {
                                YamlConfiguration c = YamlConfiguration.loadConfiguration(jar.getInputStream(file));

                                for (String s : c.getKeys(true)) {
                                    config.set(s, c.get(s));
                                }

                                String name = f.getName().substring(0, f.getName().length() - 4);
                                Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §eChanged the " + name + " Language file.");

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }

                    for (String s : hash.keySet()) {
                        config.set(s, hash.get(s));
                    }

                    config.set("Version", Core.getCore().getDescription().getVersion());

                    try {
                        config.save(f);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    configs.put(f.getName().substring(0, f.getName().length() - 4), config);
                    files.put(f.getName().substring(0, f.getName().length() - 4), f);

                } else {
                    try {
                        if (!f.getParentFile().exists()) {
                            f.getParentFile().mkdirs();
                        }

                        f.createNewFile();

                        java.io.InputStream is = jar.getInputStream(file); // get the input stream
                        java.io.FileOutputStream fos = new java.io.FileOutputStream(f);
                        while (is.available() > 0) {  // write contents of 'is' to 'fos'
                            fos.write(is.read());
                        }
                        fos.close();
                        is.close();

                        String name = f.getName().substring(0, f.getName().length() - 4);
                        Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §eCreated the " + name + " Language file.");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    configs.put(f.getName().substring(0, f.getName().length() - 4), YamlConfiguration.loadConfiguration(f));
                    files.put(f.getName().substring(0, f.getName().length() - 4), f);
                }
            }
        }
    }

}
