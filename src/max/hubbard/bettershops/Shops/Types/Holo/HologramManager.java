package max.hubbard.bettershops.Shops.Types.Holo;

import max.hubbard.bettershops.Shops.Shop;
import max.hubbard.bettershops.Shops.Types.Holo.Icons.ShopIcon;

import java.util.ArrayList;
import java.util.HashMap;
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
public class HologramManager {
    private static List<ShopHologram> holos = new ArrayList<>();

    private static HashMap<Shop,ShopHologram> shopHolos = new HashMap<>();

    public static List<ShopHologram> getHolographicShops(){
        return holos;
    }

    public static HashMap<Shop,ShopHologram> getShopHolograms(){
        return shopHolos;
    }

    public static ShopHologram getShopHologram(Shop shop){
        return shopHolos.get(shop);
    }

    private static HashMap<Shop,ShopIcon> shopIcons = new HashMap<>();

    public static HashMap<Shop,ShopIcon> getShopIcons(){
        return shopIcons;
    }

    public static void addIcon(ShopIcon icon){
        shopIcons.put(icon.getItem().getShop(),icon);
    }

    public static void removeIcon(Shop shop){
        shopIcons.get(shop).getHologram().delete();
        shopIcons.remove(shop);

    }

    public static void addHolographicShop(ShopHologram holo){
        holos.add(holo);
        shopHolos.put(holo.getShop(),holo);
    }

    public static void removeHolographicShop(ShopHologram holo){
        holos.remove(holo);
        shopHolos.remove(holo.getShop());
    }
}
