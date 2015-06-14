package max.hubbard.bettershops.Shops.Types.NPC;

import max.hubbard.bettershops.Shops.Shop;

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
public class NPCManager {
    private static List<ShopsNPC> holos = new ArrayList<>();

    private static HashMap<Shop,ShopsNPC> shopHolos = new HashMap<>();

    public static List<ShopsNPC> getNPCShops(){
        return holos;
    }

    public static ShopsNPC getNPCShop(Shop shop){
        return shopHolos.get(shop);
    }

    public static void addNPCShop(ShopsNPC holo){
        holos.add(holo);
        shopHolos.put(holo.getShop(),holo);
    }

    public static void removeNPCShop(ShopsNPC holo){
        holos.remove(holo);
        shopHolos.remove(holo.getShop());
    }
}
