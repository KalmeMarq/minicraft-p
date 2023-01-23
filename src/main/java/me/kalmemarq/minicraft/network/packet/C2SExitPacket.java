package me.kalmemarq.minicraft.network.packet;

import me.kalmemarq.minicraft.network.Packet;
import me.kalmemarq.minicraft.network.PacketByteBuf;
import me.kalmemarq.minicraft.network.PacketListener;

public class C2SExitPacket extends Packet {
    @Override
    public void read(PacketByteBuf buffer) throws Exception {
    }

    @Override
    public void write(PacketByteBuf buffer) throws Exception {
    }

    @Override
    public void handle(PacketListener listener) {
        listener.onExit(this);
    }
}
