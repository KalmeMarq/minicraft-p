package me.kalmemarq.minicraft.client.network;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import me.kalmemarq.minicraft.network.MinicraftConnection;
import me.kalmemarq.minicraft.network.PacketListener;
import me.kalmemarq.minicraft.network.packet.MessagePacket;
import me.kalmemarq.minicraft.network.packet.PingPacket;
import me.kalmemarq.minicraft.network.packet.S2CTimePacket;

public class NetworkClientHandler implements PacketListener {
    private final MinicraftConnection connection;

    public NetworkClientHandler(MinicraftConnection connection) {
        this.connection = connection;
    }

    @Override
    public void onPing(PingPacket packet) {
        System.out.println("Your ping is: " + (TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - packet.getTime())) + "ms");
    }

    @Override
    public void onTime(S2CTimePacket packet) {
        System.out.println("Time-offset: " + (new Date(packet.getTime() - System.currentTimeMillis())));
    }

    @Override
    public void onMessage(MessagePacket packet) {
        System.out.println(packet.getText());
    }
}