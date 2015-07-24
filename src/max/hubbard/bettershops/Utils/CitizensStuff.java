package max.hubbard.bettershops.Utils;

import max.hubbard.bettershops.Core;
import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.entity.LivingEntity;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2015. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class CitizensStuff {

    public static void deleteCitizensNPC(LivingEntity entity){
        if (Core.useCitizens()) {
            if (CitizensAPI.getNPCRegistry().isNPC(entity)) {
                net.citizensnpcs.api.npc.NPC npc = CitizensAPI.getNPCRegistry().getNPC(entity);
                npc.destroy();
                CitizensAPI.getNPCRegistry().deregister(npc);
            }
        }
    }
}
