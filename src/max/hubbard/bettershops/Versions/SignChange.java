package max.hubbard.bettershops.Versions;

import max.hubbard.bettershops.Configurations.Language;
import max.hubbard.bettershops.Shops.Types.Sign.SignShopManager;
import max.hubbard.bettershops.Utils.Stocks;
import max.hubbard.bettershops.Utils.WordsCapitalizer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2015. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class SignChange {

    public static void updateSigns(Player p) {
        for (Sign s : SignShopManager.getSigns().keySet()) {

            String[] lines;

            if (!SignShopManager.isSell(s)) {
                if (SignShopManager.isAdmin(s)) {
                    lines = new String[]{
                            "§a" + Language.getString("MainGUI", "SignShopLine1Buy"),
                            Language.getString("MainGUI", "SignShopLine2").replaceAll("<Item>", WordsCapitalizer.capitalizeEveryWord(SignShopManager.getSigns().get(s).name().replaceAll("_", " ")).replaceAll(" ", "")),
                            "§a$" + Language.getString("MainGUI", "SignShopLine3").replaceAll("<Price>", "" + SignShopManager.getPrices().get(s)),
                            Language.getString("MainGUI", "SignShopLine4").replaceAll("<Amount>", "" + SignShopManager.getAmounts().get(s))
                    };
                } else {
                    Block face = s.getBlock().getRelative(((org.bukkit.material.Sign) (s.getData())).getAttachedFace());
                    Chest chest = (Chest) face.getState();
                    if (Stocks.getNumberInInventory(SignShopManager.getItem(s), chest) >= SignShopManager.getAmounts().get(s)) {
                        lines = new String[]{
                                "§a" + Language.getString("MainGUI", "SignShopLine1Buy"),
                                Language.getString("MainGUI", "SignShopLine2").replaceAll("<Item>", WordsCapitalizer.capitalizeEveryWord(SignShopManager.getSigns().get(s).name().replaceAll("_", " ")).replaceAll(" ", "")),
                                "§a$" + Language.getString("MainGUI", "SignShopLine3").replaceAll("<Price>", "" + SignShopManager.getPrices().get(s)),
                                Language.getString("MainGUI", "SignShopLine4").replaceAll("<Amount>", "" + SignShopManager.getAmounts().get(s))
                        };
                    } else {
                        lines = new String[]{
                                "§c" + Language.getString("MainGUI", "SignShopLine1Buy"),
                                Language.getString("MainGUI", "SignShopLine2").replaceAll("<Item>", WordsCapitalizer.capitalizeEveryWord(SignShopManager.getSigns().get(s).name().replaceAll("_", " ")).replaceAll(" ", "")),
                                "§a$" + Language.getString("MainGUI", "SignShopLine3").replaceAll("<Price>", "" + SignShopManager.getPrices().get(s)),
                                Language.getString("MainGUI", "SignShopLine4").replaceAll("<Amount>", "" + SignShopManager.getAmounts().get(s))
                        };
                    }
                }
            } else {
                if (SignShopManager.isAdmin(s)) {
                    lines = new String[]{
                            "§a" + Language.getString("MainGUI", "SignShopLine1Sell"),
                            Language.getString("MainGUI", "SignShopLine2").replaceAll("<Item>", WordsCapitalizer.capitalizeEveryWord(SignShopManager.getSigns().get(s).name().replaceAll("_", " ")).replaceAll(" ", "")),
                            "§a$" + Language.getString("MainGUI", "SignShopLine3").replaceAll("<Price>", "" + SignShopManager.getPrices().get(s)),
                            Language.getString("MainGUI", "SignShopLine4").replaceAll("<Amount>", "" + SignShopManager.getAmounts().get(s))
                    };
                } else {
                    Block face = s.getBlock().getRelative(((org.bukkit.material.Sign) (s.getData())).getAttachedFace());
                    Chest chest = (Chest) face.getState();
                    if (Stocks.canAdd(SignShopManager.getItem(s), chest.getInventory(), SignShopManager.getAmounts().get(s))) {
                        lines = new String[]{
                                "§a" + Language.getString("MainGUI", "SignShopLine1Sell"),
                                Language.getString("MainGUI", "SignShopLine2").replaceAll("<Item>", WordsCapitalizer.capitalizeEveryWord(SignShopManager.getSigns().get(s).name().replaceAll("_", " ")).replaceAll(" ", "")),
                                "§a$" + Language.getString("MainGUI", "SignShopLine3").replaceAll("<Price>", "" + SignShopManager.getPrices().get(s)),
                                Language.getString("MainGUI", "SignShopLine4").replaceAll("<Amount>", "" + SignShopManager.getAmounts().get(s))
                        };
                    } else {
                        lines = new String[]{
                                "§c" + Language.getString("MainGUI", "SignShopLine1Sell"),
                                Language.getString("MainGUI", "SignShopLine2").replaceAll("<Item>", WordsCapitalizer.capitalizeEveryWord(SignShopManager.getSigns().get(s).name().replaceAll("_", " ")).replaceAll(" ", "")),
                                "§a$" + Language.getString("MainGUI", "SignShopLine3").replaceAll("<Price>", "" + SignShopManager.getPrices().get(s)),
                                Language.getString("MainGUI", "SignShopLine4").replaceAll("<Amount>", "" + SignShopManager.getAmounts().get(s))
                        };
                    }
                }
            }

            SignChange.doSignChange(s, SignShopManager.getSigns().get(s), p, lines);
        }
    }

    public static void updateSign(Sign s, Player p) {

        String[] lines;

        if (!SignShopManager.isSell(s)) {
            if (SignShopManager.isAdmin(s)) {
                lines = new String[]{
                        "§a" + Language.getString("MainGUI", "SignShopLine1Buy"),
                        Language.getString("MainGUI", "SignShopLine2").replaceAll("<Item>", WordsCapitalizer.capitalizeEveryWord(SignShopManager.getSigns().get(s).name().replaceAll("_", " ")).replaceAll(" ", "")),
                        "§a$" + Language.getString("MainGUI", "SignShopLine3").replaceAll("<Price>", "" + SignShopManager.getPrices().get(s)),
                        Language.getString("MainGUI", "SignShopLine4").replaceAll("<Amount>", "" + SignShopManager.getAmounts().get(s))
                };
            } else {
                Block face = s.getBlock().getRelative(((org.bukkit.material.Sign) (s.getData())).getAttachedFace());
                Chest chest = (Chest) face.getState();
                if (Stocks.getNumberInInventory(SignShopManager.getItem(s), chest) >= SignShopManager.getAmounts().get(s)) {
                    lines = new String[]{
                            "§a" + Language.getString("MainGUI", "SignShopLine1Buy"),
                            Language.getString("MainGUI", "SignShopLine2").replaceAll("<Item>", WordsCapitalizer.capitalizeEveryWord(SignShopManager.getSigns().get(s).name().replaceAll("_", " ")).replaceAll(" ", "")),
                            "§a$" + Language.getString("MainGUI", "SignShopLine3").replaceAll("<Price>", "" + SignShopManager.getPrices().get(s)),
                            Language.getString("MainGUI", "SignShopLine4").replaceAll("<Amount>", "" + SignShopManager.getAmounts().get(s))
                    };
                } else {
                    lines = new String[]{
                            "§c" + Language.getString("MainGUI", "SignShopLine1Buy"),
                            Language.getString("MainGUI", "SignShopLine2").replaceAll("<Item>", WordsCapitalizer.capitalizeEveryWord(SignShopManager.getSigns().get(s).name().replaceAll("_", " ")).replaceAll(" ", "")),
                            "§a$" + Language.getString("MainGUI", "SignShopLine3").replaceAll("<Price>", "" + SignShopManager.getPrices().get(s)),
                            Language.getString("MainGUI", "SignShopLine4").replaceAll("<Amount>", "" + SignShopManager.getAmounts().get(s))
                    };
                }
            }
        } else {
            if (SignShopManager.isAdmin(s)) {
                lines = new String[]{
                        "§a" + Language.getString("MainGUI", "SignShopLine1Sell"),
                        Language.getString("MainGUI", "SignShopLine2").replaceAll("<Item>", WordsCapitalizer.capitalizeEveryWord(SignShopManager.getSigns().get(s).name().replaceAll("_", " ")).replaceAll(" ", "")),
                        "§a$" + Language.getString("MainGUI", "SignShopLine3").replaceAll("<Price>", "" + SignShopManager.getPrices().get(s)),
                        Language.getString("MainGUI", "SignShopLine4").replaceAll("<Amount>", "" + SignShopManager.getAmounts().get(s))
                };
            } else {
                Block face = s.getBlock().getRelative(((org.bukkit.material.Sign) (s.getData())).getAttachedFace());
                Chest chest = (Chest) face.getState();
                if (Stocks.canAdd(SignShopManager.getItem(s), chest.getInventory(), SignShopManager.getAmounts().get(s))) {
                    lines = new String[]{
                            "§a" + Language.getString("MainGUI", "SignShopLine1Sell"),
                            Language.getString("MainGUI", "SignShopLine2").replaceAll("<Item>", WordsCapitalizer.capitalizeEveryWord(SignShopManager.getSigns().get(s).name().replaceAll("_", " ")).replaceAll(" ", "")),
                            "§a$" + Language.getString("MainGUI", "SignShopLine3").replaceAll("<Price>", "" + SignShopManager.getPrices().get(s)),
                            Language.getString("MainGUI", "SignShopLine4").replaceAll("<Amount>", "" + SignShopManager.getAmounts().get(s))
                    };
                } else {
                    lines = new String[]{
                            "§c" + Language.getString("MainGUI", "SignShopLine1Sell"),
                            Language.getString("MainGUI", "SignShopLine2").replaceAll("<Item>", WordsCapitalizer.capitalizeEveryWord(SignShopManager.getSigns().get(s).name().replaceAll("_", " ")).replaceAll(" ", "")),
                            "§a$" + Language.getString("MainGUI", "SignShopLine3").replaceAll("<Price>", "" + SignShopManager.getPrices().get(s)),
                            Language.getString("MainGUI", "SignShopLine4").replaceAll("<Amount>", "" + SignShopManager.getAmounts().get(s))
                    };
                }
            }
        }

        SignChange.doSignChange(s, SignShopManager.getSigns().get(s), p, lines);

    }

    public static void doSignChange(Sign sign, Material m, Player p, String[] lines) {
        String packageName = Bukkit.getServer().getClass().getPackage().getName();

        // Get full package string of CraftServer.
        // org.bukkit.craftbukkit.version
        String version = packageName.substring(packageName.lastIndexOf('.') + 1);

        try {
            Class c = Class.forName("max.hubbard.bettershops.Versions." + version + ".SignChanger");
            c.getMethod("doSignChange", Sign.class, Material.class, Player.class, String[].class).invoke(null, sign, m, p, lines);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}