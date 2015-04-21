package me.moomaxie.BetterShops.Configurations.Permissions;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.EntityType;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2014. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class Permissions {

    //Permissions:
    // bettershops.shop.*
    // bettershops.shop.create
    // bettershops.shop.buy
    // bettershops.shop.sell
    // bettershops.shop.limit
    // bettershops.shop.infinite
    // bettershops.shop.owneruse
    // bettershops.shop.NPC
    // bettershops.shop.creationcost
    // bettershops.shop.break
    // bettershops.shop.hologram
    // bettershops.shop.liveeco
    // bettershops.shop.arrange
    // bettershops.shop.blacklist

    // bettershops.command.update
    // bettershops.command.config
    // bettershops.command.language
    // bettershops.command.open
    // bettershops.command.list
    // bettershops.command.blacklist

    // bettershops.npc.*
    // bettershops.npc.TYPE

    // bettershops.buy.*
    // bettershops.buy.<Material>

    // bettershops.sell.*
    // bettershops.sell.<Material>

    public static boolean hasCreatePerm(OfflinePlayer p){
        return p.isOp() || p.getPlayer().hasPermission("bettershops.shop.create") || p.getPlayer().hasPermission("bettershops.shop.*");
    }

    public static boolean hasBuyPerm(OfflinePlayer p){
        return p.isOp() || p.getPlayer().hasPermission("bettershops.shop.buy") || p.getPlayer().hasPermission("bettershops.shop.*");
    }

    public static boolean hasSellPerm(OfflinePlayer p){
        return p.isOp() || p.getPlayer().hasPermission("bettershops.shop.sell") || p.getPlayer().hasPermission("bettershops.shop.*");
    }

    public static boolean hasLimitPerm(OfflinePlayer p){
        return p.isOp() || p.getPlayer().hasPermission("bettershops.shop.limit") || p.getPlayer().hasPermission("bettershops.shop.*");
    }

    public static boolean hasInfinitePerm(OfflinePlayer p){
        return p.isOp() || p.getPlayer().hasPermission("bettershops.shop.infinite") || p.getPlayer().hasPermission("bettershops.shop.*");
    }

    public static boolean hasUsePerm(OfflinePlayer p){
        return p.isOp() || p.getPlayer().hasPermission("bettershops.shop.owneruse") || p.getPlayer().hasPermission("bettershops.shop.*");
    }

    public static boolean hasNPCPerm(OfflinePlayer p){
        return p.isOp() || p.getPlayer().hasPermission("bettershops.shop.NPC") || p.getPlayer().hasPermission("bettershops.shop.*");
    }

    public static boolean hasCostCreationPerm(OfflinePlayer p){
        return p.isOp() || p.getPlayer().hasPermission("bettershops.shop.creationcost") || p.getPlayer().hasPermission("bettershops.shop.*");
    }

    public static boolean hasUpdatePerm(OfflinePlayer p){
        return p.isOp() || p.getPlayer().hasPermission("bettershops.command.update") || p.getPlayer().hasPermission("bettershops.command.*");
    }

    public static boolean hasConfigGUIPerm(OfflinePlayer p){
        return p.isOp() || p.getPlayer().hasPermission("bettershops.command.config") || p.getPlayer().hasPermission("bettershops.command.*");
    }

    public static boolean hasLanguagePerm(OfflinePlayer p){
        return p.isOp() || p.getPlayer().hasPermission("bettershops.command.language") || p.getPlayer().hasPermission("bettershops.command.*");
    }

    public static boolean hasOpenCommandPerm(OfflinePlayer p){
        return p.isOp() || p.getPlayer().hasPermission("bettershops.command.open") || p.getPlayer().hasPermission("bettershops.command.*");
    }

    public static boolean hasListPerm(OfflinePlayer p){
        return p.isOp() || p.getPlayer().hasPermission("bettershops.command.list") || p.getPlayer().hasPermission("bettershops.command.*");
    }

    public static boolean hasRemoveCommandPerm(OfflinePlayer p){
        return p.isOp() || p.getPlayer().hasPermission("bettershops.command.remove") || p.getPlayer().hasPermission("bettershops.command.*");
    }

    public static boolean hasBreakPerm(OfflinePlayer p){
        return p.isOp() || p.getPlayer().hasPermission("bettershops.shop.break") || p.getPlayer().hasPermission("bettershops.shop.*");
    }

    public static boolean hasNPCTypePerm(EntityType type, OfflinePlayer p){
        return p.isOp() || p.getPlayer().hasPermission("bettershops.npc." + type.name().replaceAll("_","").toLowerCase()) || p.getPlayer().hasPermission("bettershops.npc.*") || p.getPlayer().hasPermission("bettershops.shop.*");
    }

    public static boolean hasLiveEcoPerm(OfflinePlayer p){
        return p.isOp() || p.getPlayer().hasPermission("bettershops.shop.liveeco") || p.getPlayer().hasPermission("bettershops.shop.*");
    }

    public static boolean hasHoloPerm(OfflinePlayer p){
        return p.isOp() || p.getPlayer().hasPermission("bettershops.shop.hologram") || p.getPlayer().hasPermission("bettershops.shop.*");
    }

    public static boolean hasArrangePerm(OfflinePlayer p){
        return p.isOp() || p.getPlayer().hasPermission("bettershops.shop.arrange") || p.getPlayer().hasPermission("bettershops.shop.*");
    }

    public static boolean hasBlacklistPerm(OfflinePlayer p){
        return p.isOp() || p.getPlayer().hasPermission("bettershops.shop.blacklist") || p.getPlayer().hasPermission("bettershops.shop.*");
    }

    public static boolean hasBlacklistCommandPerm(OfflinePlayer p){
        return p.isOp() || p.getPlayer().hasPermission("bettershops.command.blacklist") || p.getPlayer().hasPermission("bettershops.command.*");
    }

    public static boolean hasBuyItemPerm(OfflinePlayer p, Material m){
        return p.isOp() || p.getPlayer().hasPermission("bettershops.buy." + m.name()) || p.getPlayer().hasPermission("bettershops.buy.*");
    }

    public static boolean hasSellItemPerm(OfflinePlayer p, Material m){
        return p.isOp() || p.getPlayer().hasPermission("bettershops.sell." + m.name()) || p.getPlayer().hasPermission("bettershops.sell.*");
    }
}
