package max.hubbard.bettershops.Versions.v1_8_R1;

import max.hubbard.bettershops.Versions.v1_8_R1.Entities.PlayerNPC;
import net.minecraft.server.v1_8_R1.MinecraftServer;
import net.minecraft.server.v1_8_R1.Packet;
import net.minecraft.server.v1_8_R1.PlayerConnection;

public class NPCConnection extends PlayerConnection {

    public NPCConnection(MinecraftServer server, PlayerNPC npc) {
        super(server, new NPCNetworkManager(), npc);
    }

    @Override
    public void sendPacket(Packet packet) {
        //Don't send packets to an npc
    }
}