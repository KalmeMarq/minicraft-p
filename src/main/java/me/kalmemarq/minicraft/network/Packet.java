package me.kalmemarq.minicraft.network;

import java.util.Arrays;
import java.util.List;

import io.netty.buffer.ByteBuf;
import me.kalmemarq.minicraft.network.packet.ExitPacket;
import me.kalmemarq.minicraft.network.packet.InTimePacket;
import me.kalmemarq.minicraft.network.packet.OutTimePacket;
import me.kalmemarq.minicraft.network.packet.PingPacket;

public abstract class Packet {
    public static final List<Class<? extends Packet>> PACKETS = Arrays.asList(
        PingPacket.class,
        InTimePacket.class,
        OutTimePacket.class,
        ExitPacket.class
    );

    abstract public void read(ByteBuf byteBuf) throws Exception;
    abstract public void write(ByteBuf byteBuf) throws Exception;

    static {
    }
}
