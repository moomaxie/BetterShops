package max.hubbard.bettershops.Versions.v1_8_R3.Entities;

import com.mojang.authlib.GameProfile;
import max.hubbard.bettershops.Versions.v1_8_R3.NPCConnection;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.Collection;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2015. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class PlayerNPC extends EntityPlayer {

    public PlayerNPC(MinecraftServer minecraftserver, WorldServer worldserver, GameProfile gameprofile, PlayerInteractManager playerinteractmanager) {
        super(minecraftserver, worldserver, gameprofile, playerinteractmanager);
        playerConnection = new NPCConnection(minecraftserver, this);
        sendPacketsTo(Bukkit.getOnlinePlayers(), new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, this));
    }

    @Override
    public void move(double d0, double d1, double d2) {
        return;
    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        return false;
    }

    @Override
    public void g(double x, double y, double z) {

    }

    public static Player spawn(GameProfile pro, Location location) {
        MinecraftServer server = MinecraftServer.getServer();
        WorldServer world = server.getWorldServer(0);
        for (WorldServer ws : server.worlds) {
            if (ws.getWorld().getName().equals(location.getWorld().getName())) {
                world = ws;
                break;
            }
        }
        final PlayerNPC customEntity = new PlayerNPC(
                server, world, pro, new PlayerInteractManager(world));
        customEntity.setLocation(location.getX(), location.getY(),
                location.getZ(), location.getYaw(), location.getPitch());
        ((CraftLivingEntity) customEntity.getBukkitEntity())
                .setRemoveWhenFarAway(false);
        world.addEntity(customEntity, CreatureSpawnEvent.SpawnReason.CUSTOM);

        return (CraftPlayer) customEntity.getBukkitEntity();
    }

    public void sendPacketsTo(Collection<? extends Player> recipients, Packet... packets) {
        EntityPlayer[] nmsRecipients = getHandles(recipients);
        for (EntityPlayer recipient : nmsRecipients) {
            if (recipient == null) continue;
            for (Packet packet : packets) {
                if (packet == null) continue;
                recipient.playerConnection.sendPacket(packet);
            }
        }
    }

    public static EntityPlayer[] getHandles(Collection<? extends Player> bukkitPlayers) {
        EntityPlayer[] handles = new EntityPlayer[bukkitPlayers.size()];
        for (int i = 0; i < bukkitPlayers.size(); i++) {
            handles[i] = getHandle((Player) bukkitPlayers.toArray()[i]);
        }
        return handles;
    }

    public static EntityPlayer getHandle(Player bukkitPlayer) {
        if (!(bukkitPlayer instanceof CraftPlayer)) return null;
        return ((CraftPlayer) bukkitPlayer).getHandle();
    }
}
