package me.moomaxie.BetterShops.Listeners.CreationCost;

import me.moomaxie.BetterShops.Configurations.Config;
import me.moomaxie.BetterShops.Configurations.Messages;
import me.moomaxie.BetterShops.Configurations.Permissions.Permissions;
import me.moomaxie.BetterShops.Core;
import org.bukkit.entity.Player;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2014. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class CreationCost {

    public static boolean useCost(Player p) {
        if (Config.useCreationCost()) {
            double amt = Config.getCreationCost();
            if (Config.usePerms()) {
                if (!Permissions.hasCostCreationPerm(p)) {
                    if (Core.getEconomy().getBalance(p) >= amt) {
                        Core.getEconomy().withdrawPlayer(p, amt);
                        p.sendMessage(Messages.getPrefix() + Messages.getCreationCostAllow().replaceAll("<Amount>","" + amt));
                        return true;
                    } else {
                        p.sendMessage(Messages.getPrefix() + Messages.getCreationCostDeny().replaceAll("<Amount>","" + amt));
                        return false;
                    }
                } else {
                    return true;
                }
            } else {
                if (!p.isOp()) {
                    if (Core.getEconomy().getBalance(p) >= amt) {
                        Core.getEconomy().withdrawPlayer(p, amt);
                        p.sendMessage(Messages.getPrefix() + Messages.getCreationCostAllow().replaceAll("<Amount>", "" + amt));
                        return true;
                    } else {
                        p.sendMessage(Messages.getPrefix() + Messages.getCreationCostDeny().replaceAll("<Amount>", "" + amt));
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
