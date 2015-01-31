package me.moomaxie.BetterShops.NPC;

import java.util.ArrayList;
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

    public static void addNPC(ShopsNPC npc){
        npcs.add(npc);
    }

    public static void removeNPC(ShopsNPC npc){
        npcs.remove(npc);
    }

    public static List<ShopsNPC> getNPCs(){
        return npcs;
    }
}
