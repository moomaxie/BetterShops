package me.moomaxie.BetterShops.Configurations;

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
public interface TitleManager {

    public void sendTitle(Player p, String message);

    public void sendSubTitle(Player p, String message);

    public void setTimes(Player p, int fadein,int amt, int fadeout);
}
