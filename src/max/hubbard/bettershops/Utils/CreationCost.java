package max.hubbard.bettershops.Utils;

import max.hubbard.bettershops.Configurations.Config;
import max.hubbard.bettershops.Configurations.Language;
import max.hubbard.bettershops.Configurations.Permissions;
import max.hubbard.bettershops.Core;
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
public class CreationCost {

    public static boolean useCost(Player p) {
        if ((boolean)Config.getObject("CostOnShops")) {
            double amt = (double)Config.getObject("CostForShops");
            if ((boolean)Config.getObject("Permissions")) {
                if (!Permissions.hasCostCreationPerm(p)) {
                    if (Core.getEconomy().getBalance(p) >= amt) {
                        Core.getEconomy().withdrawPlayer(p, amt);
                        p.sendMessage(Language.getString("Messages","Prefix") + Language.getString("Messages", "CreationCostAllow").replaceAll("<Amount>","" + amt));
                        return true;
                    } else {
                        p.sendMessage(Language.getString("Messages","Prefix") + Language.getString("Messages","CreationCostDeny").replaceAll("<Amount>","" + amt));
                        return false;
                    }
                } else {
                    return true;
                }
            } else {
                if (!p.isOp()) {
                    if (Core.getEconomy().getBalance(p) >= amt) {
                        Core.getEconomy().withdrawPlayer(p, amt);
                        p.sendMessage(Language.getString("Messages","Prefix") + Language.getString("Messages","CreationCostAllow").replaceAll("<Amount>", "" + amt));
                        return true;
                    } else {
                        p.sendMessage(Language.getString("Messages","Prefix") + Language.getString("Messages","CreationCostDeny").replaceAll("<Amount>", "" + amt));
                        return false;
                    }
                } else {
                    return true;
                }
            }
        } else {
            return true;
        }
    }
}
