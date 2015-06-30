package max.hubbard.bettershops.Shops.Types.Holo;

import com.gmail.filoghost.holographicdisplays.object.NamedHologram;
import max.hubbard.bettershops.Configurations.Language;
import max.hubbard.bettershops.Shops.Shop;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2015. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class CreateHologram {

    public static void createHolographicShop(final Shop shop) {

        new BukkitRunnable() {

            @Override
            public void run() {
                NamedHologram hologram = new NamedHologram(shop.getLocation().clone().add(.5, 3.5, .5), "BS" + shop.getName().replaceAll(" ", "_"));
                ShopHologram shopHologram = new ShopHologram(shop, hologram);
                HologramManager.addHolographicShop(shopHologram);

                shop.setObject("Holo", true);
                removeShopChest(shop);
            }
        }.runTask(Bukkit.getPluginManager().getPlugin("BetterShops"));

    }

    public static void removeShopChest(final Shop shop) {
        new BukkitRunnable() {

            @Override
            public void run() {
                if (shop.getLocation().getBlock().getRelative(1, 0, 0).getType() == Material.WALL_SIGN) {
                    Sign s = (Sign) shop.getLocation().getBlock().getRelative(1, 0, 0).getState();

                    if (s.getLine(0).contains(Language.getString("MainGUI", "SignLine1"))) {
                        if (s.getLine(3).contains(Language.getString("MainGUI", "SignLine4"))) {
                            if (s.getLine(1).contains(Language.getString("MainGUI", "SignLine2"))) {
                                s.getBlock().setType(Material.AIR);
                            }
                        }
                    }
                }

                if (shop.getLocation().getBlock().getRelative(-1, 0, 0).getType() == Material.WALL_SIGN) {
                    Sign s = (Sign) shop.getLocation().getBlock().getRelative(-1, 0, 0).getState();

                    if (s.getLine(0).contains(Language.getString("MainGUI", "SignLine1"))) {
                        if (s.getLine(3).contains(Language.getString("MainGUI", "SignLine4"))) {
                            if (s.getLine(1).contains(Language.getString("MainGUI", "SignLine2"))) {
                                s.getBlock().setType(Material.AIR);
                            }
                        }
                    }
                }

                if (shop.getLocation().getBlock().getRelative(0, 0, 1).getType() == Material.WALL_SIGN) {
                    Sign s = (Sign) shop.getLocation().getBlock().getRelative(0, 0, 1).getState();

                    if (s.getLine(0).contains(Language.getString("MainGUI", "SignLine1"))) {
                        if (s.getLine(3).contains(Language.getString("MainGUI", "SignLine4"))) {
                            if (s.getLine(1).contains(Language.getString("MainGUI", "SignLine2"))) {
                                s.getBlock().setType(Material.AIR);
                            }
                        }
                    }
                }

                if (shop.getLocation().getBlock().getRelative(0, 0, -1).getType() == Material.WALL_SIGN) {
                    Sign s = (Sign) shop.getLocation().getBlock().getRelative(0, 0, -1).getState();

                    if (s.getLine(0).contains(Language.getString("MainGUI", "SignLine1"))) {
                        if (s.getLine(3).contains(Language.getString("MainGUI", "SignLine4"))) {
                            if (s.getLine(1).contains(Language.getString("MainGUI", "SignLine2"))) {
                                s.getBlock().setType(Material.AIR);
                            }
                        }
                    }
                }

                shop.getLocation().getBlock().setType(Material.AIR);

            }

        }.runTask(Bukkit.getPluginManager().getPlugin("BetterShops"));
    }
}
