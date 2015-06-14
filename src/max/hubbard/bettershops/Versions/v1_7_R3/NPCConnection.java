package max.hubbard.bettershops.Versions.v1_7_R3;

import max.hubbard.bettershops.Versions.v1_7_R3.Entities.PlayerNPC;
import net.minecraft.server.v1_7_R3.MinecraftServer;
import net.minecraft.server.v1_7_R3.Packet;
import net.minecraft.server.v1_7_R3.PlayerConnection;

public class NPCConnection extends PlayerConnection {

    public NPCConnection(MinecraftServer server, PlayerNPC npc) {
        super(server, new NPCNetworkManager(), npc);
    }

    @Override
    public void sendPacket(Packet packet) {
        //Don't send packets to an npc
    }
}