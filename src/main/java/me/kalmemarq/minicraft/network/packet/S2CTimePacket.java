package me.kalmemarq.minicraft.network.packet;

import me.kalmemarq.minicraft.network.Packet;
import me.kalmemarq.minicraft.network.PacketByteBuf;

public class S2CTimePacket extends Packet {
    private long time;

    public S2CTimePacket() {}

    public S2CTimePacket(long time) {
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

    public long getTime() {
      return time;
    }
}
