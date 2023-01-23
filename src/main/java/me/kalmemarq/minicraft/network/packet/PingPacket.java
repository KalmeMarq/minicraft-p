package me.kalmemarq.minicraft.network.packet;

import me.kalmemarq.minicraft.network.Packet;
import me.kalmemarq.minicraft.network.PacketByteBuf;
import me.kalmemarq.minicraft.network.PacketListener;

public class PingPacket extends Packet {
    private long time;

    public PingPacket() {}

    public PingPacket(long time) {
        this.time = time;
    }

    @Override
    public void read(PacketByteBuf buffer) throws Exception {
        this.time = buffer.readLong();
    }

    @Override
    public void write(PacketByteBuf buffer) throws Exception {
        buffer.writeLong(this.time);
    }

    @Override
    public void handle(PacketListener listener) {
        listener.onPing(this);
    }

    public long getTime() {
        return time;
    }
}
