package me.moomaxie.BetterShops.ShopTypes.NPC;

import me.moomaxie.BetterShops.Shops.Shop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2014. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class NPCs {

    private static List<ShopsNPC> npcs = new ArrayList<>();

    private static HashMap<Shop,ShopsNPC> shopNPCs = new HashMap<>();

    public static void addNPC(ShopsNPC npc){
        npcs.add(npc);
        shopNPCs.put(npc.getShop(),npc);
    }

    public static void removeNPC(ShopsNPC npc){
        npcs.remove(npc);
        shopNPCs.remove(npc.getShop());
    }

    public static List<ShopsNPC> getNPCs(){
        return npcs;
    }

    public static HashMap<Shop,ShopsNPC> getShopNPCs(){return shopNPCs;}
}
