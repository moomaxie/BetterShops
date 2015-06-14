package max.hubbard.bettershops.Versions.v1_7_R3;

import net.minecraft.server.v1_7_R3.PacketPlayOutUpdateSign;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.v1_7_R3.entity.CraftPlayer;
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
public class SignChanger {

    public static void doSignChange(Sign sign, org.bukkit.Material mat, Player p, String[] lines) {

        PacketPlayOutUpdateSign packet = new PacketPlayOutUpdateSign(sign.getX(),sign.getY(),sign.getZ(),lines);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
    }
}
