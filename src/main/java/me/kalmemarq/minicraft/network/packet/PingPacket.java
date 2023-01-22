package me.kalmemarq.minicraft.network.packet;

import io.netty.buffer.ByteBuf;
import me.kalmemarq.minicraft.network.Packet;

public class PingPacket extends Packet {
    private long time;

    public PingPacket() {}

    public PingPacket(long time) {
        this.time = time;
    }

    @Override
    public void read(ByteBuf byteBuf) throws Exception {
        this.time = byteBuf.readLong();
    }

    @Override
    public void write(ByteBuf byteBuf) throws Exception {
        byteBuf.writeLong(this.time);
    }

    public long getTime() {
        return time;
    }
}
