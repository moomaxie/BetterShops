package max.hubbard.bettershops.Configurations;

import max.hubbard.bettershops.Utils.WordsCapitalizer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
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
public class Config {
    public static File file = new File(Bukkit.getPluginManager().getPlugin("BetterShops").getDataFolder(), "config.yml");
    public static YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

    public static Object getObject(String s) {
        return config.get(s);
    }

    public static void setObject(String path, Object obj) {
        config.set(path, obj);
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public static List<EntityType> getAllNPCs() {

        List<EntityType> ent = new ArrayList<>();

        for (String s : config.getConfigurationSection("NPC").getKeys(false)) {
            if (isInEnum(s.toUpperCase().replaceAll(" ", "_"), EntityType.class)) {
                ent.add(EntityType.valueOf(s.toUpperCase().replaceAll(" ", "_")));
            }
        }

        return ent;
    }

    public static List<EntityType> getNPCs() {

        List<EntityType> ent = new ArrayList<>();

        for (String s : config.getConfigurationSection("NPC").getKeys(false)) {
            if (config.getConfigurationSection("NPC").getBoolean(s)) {
                if (isInEnum(s.toUpperCase().replaceAll(" ", "_"), EntityType.class)) {
                    ent.add(EntityType.valueOf(s.toUpperCase().replaceAll(" ", "_")));
                }
            }
        }

        return ent;
    }

    public static <E extends Enum<E>> boolean isInEnum(String value, Class<E> enumClass) {
        for (E e : enumClass.getEnumConstants()) {
            if (e.name().equals(value)) {
                return true;
            }
        }
        return false;
    }
}
