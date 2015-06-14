package max.hubbard.bettershops.Versions.v1_8_R1;

import net.minecraft.server.v1_8_R1.*;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.v1_8_R1.block.CraftSign;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
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
        IChatBaseComponent[] components = CraftSign.sanitizeLines(lines);


        World w = MinecraftServer.getServer().getWorld();
        for (WorldServer s : MinecraftServer.getServer().worlds) {
            if (s.getWorld().getName().equals(sign.getWorld().getName())) {
                w = s;
            }
        }

        Location loc = sign.getLocation();

        PacketPlayOutUpdateSign packet = new PacketPlayOutUpdateSign(w, new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()), components);


        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);

    }
}
