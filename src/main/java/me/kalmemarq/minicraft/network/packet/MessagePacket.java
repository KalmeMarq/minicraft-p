package me.kalmemarq.minicraft.network.packet;

import java.time.Instant;

import me.kalmemarq.minicraft.network.Packet;
import me.kalmemarq.minicraft.network.PacketByteBuf;

public class MessagePacket extends Packet {
    private String text;
    private Instant time;
    
    public MessagePacket() {}
    
    public MessagePacket(String text, Instant time) {
        this.text = text;
        this.time = time;
    }

    @Override
    public void read(PacketByteBuf buffer) throws Exception {
        this.text = buffer.readString();
        this.time = buffer.readInstant();
    }

    @Override
    public void write(PacketByteBuf buffer) throws Exception {
        buffer.writeString(this.text);
        buffer.writeInstant(this.time);
    }

    public String getText() {
      return this.text;
    }

    public Instant getTime() {
      return this.time;
    }
}
