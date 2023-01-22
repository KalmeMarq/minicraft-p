package me.kalmemarq.minicraft.network;

import java.util.Arrays;
import java.util.List;

import me.kalmemarq.minicraft.network.packet.C2SExitPacket;
import me.kalmemarq.minicraft.network.packet.MessagePacket;
import me.kalmemarq.minicraft.network.packet.C2STimePacket;
import me.kalmemarq.minicraft.network.packet.PingPacket;
import me.kalmemarq.minicraft.network.packet.S2CTimePacket;

public abstract class Packet {
    public static final List<Class<? extends Packet>> PACKETS = Arrays.asList(
        PingPacket.class,
        C2STimePacket.class,
        S2CTimePacket.class,
        C2SExitPacket.class,
        MessagePacket.class
    );

    abstract public void read(PacketByteBuf buffer) throws Exception;
    abstract public void write(PacketByteBuf buffer) throws Exception;

    static {
    }
}
