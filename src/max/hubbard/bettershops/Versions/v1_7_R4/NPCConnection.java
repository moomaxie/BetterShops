package max.hubbard.bettershops.Versions.v1_7_R4;

import max.hubbard.bettershops.Versions.v1_7_R4.Entities.PlayerNPC;
import net.minecraft.server.v1_7_R4.MinecraftServer;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PlayerConnection;

public class NPCConnection extends PlayerConnection {

    public NPCConnection(MinecraftServer server, PlayerNPC npc) {
        super(server, new NPCNetworkManager(), npc);
    }

    @Override
    public void sendPacket(Packet packet) {
        //Don't send packets to an npc
    }
}