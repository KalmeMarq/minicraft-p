package me.kalmemarq.minicraft.server.network;

import me.kalmemarq.minicraft.network.MinicraftConnection;
import me.kalmemarq.minicraft.network.PacketListener;
import me.kalmemarq.minicraft.network.packet.C2SExitPacket;
import me.kalmemarq.minicraft.network.packet.C2STimePacket;
import me.kalmemarq.minicraft.network.packet.MessagePacket;
import me.kalmemarq.minicraft.network.packet.PingPacket;
import me.kalmemarq.minicraft.network.packet.S2CTimePacket;

public class NetworkServerHandler implements PacketListener {
    private final MinicraftConnection connection;

    public NetworkServerHandler(MinicraftConnection connection) {
        this.connection = connection;
    }
    
    @Override
    public void onPing(PingPacket packet) {
        this.connection.sendPacket(packet);
    }

    @Override
    public void onTime(C2STimePacket packet) {
        this.connection.sendPacket(new S2CTimePacket(System.currentTimeMillis() + (short) (Math.random() * Short.MAX_VALUE)));
    }

    @Override
    public void onExit(C2SExitPacket packet) {
        this.connection.disconnect();
    }

    @Override
    public void onMessage(MessagePacket packet) {
        System.out.println("[" + packet.getTime() + "] " + packet.getText());
        this.connection.sendPacket(packet);
    }
}
