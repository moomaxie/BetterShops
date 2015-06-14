package max.hubbard.bettershops.Versions.v1_8_R1;

import net.minecraft.server.v1_8_R1.ChatComponentText;
import net.minecraft.server.v1_8_R1.EnumTitleAction;
import net.minecraft.server.v1_8_R1.PacketPlayOutTitle;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
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
public class TitleManager implements max.hubbard.bettershops.Versions.TitleManager{

    public void sendTitle(Player p, String message){

        PacketPlayOutTitle title = new PacketPlayOutTitle(EnumTitleAction.TITLE, new ChatComponentText(ChatColor.translateAlternateColorCodes("§".toCharArray()[0] , message)));

        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(title);
    }

    public void sendSubTitle(Player p, String message){

        PacketPlayOutTitle subtitle = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, new ChatComponentText(ChatColor.translateAlternateColorCodes("§".toCharArray()[0] , message)));


        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(subtitle);
    }

    public void setTimes(Player p, int fadein,int amt, int fadeout){

        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(new PacketPlayOutTitle(EnumTitleAction.RESET,null));
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(new PacketPlayOutTitle(fadein,amt,fadeout));

    }
}
