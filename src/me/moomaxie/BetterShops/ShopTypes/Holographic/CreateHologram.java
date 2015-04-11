package me.moomaxie.BetterShops.ShopTypes.Holographic;

import com.gmail.filoghost.holographicdisplays.disk.HologramDatabase;
import com.gmail.filoghost.holographicdisplays.object.NamedHologram;
import com.gmail.filoghost.holographicdisplays.object.NamedHologramManager;
import me.moomaxie.BetterShops.Configurations.GUIMessages.MainGUI;
import me.moomaxie.BetterShops.Shops.Shop;
import org.bukkit.Material;
import org.bukkit.block.Sign;

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

    public static void createHolographicShop(Shop shop){
        removeShopChest(shop);

        NamedHologram hologram = new NamedHologram(shop.getLocation().clone().add(.5,3.5,.5),"BS" + shop.getName().replaceAll(" ", "_"));

        ShopHologram shopHologram = new ShopHologram(shop, hologram);
        HologramManager.addHolographicShop(shopHologram);

        NamedHologramManager.addHologram(hologram);
        HologramDatabase.saveHologram((NamedHologram)shopHologram.getHologram());
        HologramDatabase.trySaveToDisk();

        shop.setHoloShop(true);

    }

    public static void removeShopChest(Shop shop){
        if (shop.getLocation().getBlock().getRelative(1, 0, 0).getType() == Material.WALL_SIGN) {
            Sign s = (Sign) shop.getLocation().getBlock().getRelative(1, 0, 0).getState();

            if (s.getLine(0).contains(MainGUI.getString("SignLine1"))) {
                if (s.getLine(3).contains(MainGUI.getString("SignLine4"))) {
                    if (s.getLine(1).contains(MainGUI.getString("SignLine2"))) {
                        s.getBlock().setType(Material.AIR);
                    }
                }
            }
        }

        if (shop.getLocation().getBlock().getRelative(-1, 0, 0).getType() == Material.WALL_SIGN) {
            Sign s = (Sign) shop.getLocation().getBlock().getRelative(-1, 0, 0).getState();

            if (s.getLine(0).contains(MainGUI.getString("SignLine1"))) {
                if (s.getLine(3).contains(MainGUI.getString("SignLine4"))) {
                    if (s.getLine(1).contains(MainGUI.getString("SignLine2"))) {
                        s.getBlock().setType(Material.AIR);
                    }
                }
            }
        }

        if (shop.getLocation().getBlock().getRelative(0, 0, 1).getType() == Material.WALL_SIGN) {
            Sign s = (Sign) shop.getLocation().getBlock().getRelative(0, 0, 1).getState();

            if (s.getLine(0).contains(MainGUI.getString("SignLine1"))) {
                if (s.getLine(3).contains(MainGUI.getString("SignLine4"))) {
                    if (s.getLine(1).contains(MainGUI.getString("SignLine2"))) {
                        s.getBlock().setType(Material.AIR);
                    }
                }
            }
        }

        if (shop.getLocation().getBlock().getRelative(0, 0, -1).getType() == Material.WALL_SIGN) {
            Sign s = (Sign) shop.getLocation().getBlock().getRelative(0, 0, -1).getState();

            if (s.getLine(0).contains(MainGUI.getString("SignLine1"))) {
                if (s.getLine(3).contains(MainGUI.getString("SignLine4"))) {
                    if (s.getLine(1).contains(MainGUI.getString("SignLine2"))) {
                        s.getBlock().setType(Material.AIR);
                    }
                }
            }
        }

        shop.getLocation().getBlock().setType(Material.AIR);
    }
}
