package BetterShops.Versions.v1_7_R4;

import net.minecraft.server.v1_7_R4.ChatComponentText;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.spigotmc.ProtocolInjector;

/**
 * ***********************************************************************
 * Copyright moomaxie (c) 2014. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of moomaxie. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class TitleManager implements me.moomaxie.BetterShops.Configurations.TitleManager{

    public void sendTitle(Player p, String message){
        ProtocolInjector.PacketTitle title = new ProtocolInjector.PacketTitle(ProtocolInjector.PacketTitle.Action.TITLE, new ChatComponentText(ChatColor.translateAlternateColorCodes('§', message)));

        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(title);
    }

    public void sendSubTitle(Player p, String message){
        ProtocolInjector.PacketTitle subtitle = new ProtocolInjector.PacketTitle(ProtocolInjector.PacketTitle.Action.SUBTITLE, new ChatComponentText(ChatColor.translateAlternateColorCodes('§', message)));

        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(subtitle);
    }

    public void setTimes(Player p, int fadein,int amt, int fadeout){

        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(new ProtocolInjector.PacketTitle(ProtocolInjector.PacketTitle.Action.RESET));
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(new ProtocolInjector.PacketTitle(ProtocolInjector.PacketTitle.Action.TIMES, fadein, amt, fadeout));

    }
}
