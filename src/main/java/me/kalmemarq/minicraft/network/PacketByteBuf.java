package me.kalmemarq.minicraft.network;

import java.nio.charset.StandardCharsets;
import java.time.Instant;

import io.netty.buffer.ByteBuf;

public class PacketByteBuf {
    private final ByteBuf buffer;

    public PacketByteBuf(ByteBuf buffer) {
        this.buffer = buffer;
    }

    public void writeByte(int value) {
        this.buffer.writeByte(value);
    }

    public void writeShort(int value) {
        this.buffer.writeShort(value);
    }

    public void writeInt(int value) {
        this.buffer.writeInt(value);
    }

    public void writeLong(long value) {
        this.buffer.writeLong(value);
    }

    public void writeInstant(Instant value) {
        this.writeLong(value.toEpochMilli());
    }

    public void writeString(String value) {
        byte[] data = value.getBytes(StandardCharsets.UTF_8);
        this.writeShort(data.length);
        this.buffer.writeBytes(data);
    }

    public byte readByte() {
        return this.buffer.readByte();
    }

    public short readShort() {
        return this.buffer.readShort();
    }

    public int readInt() {
        return this.buffer.readInt();
    }

    public long readLong() {
        return this.buffer.readLong();
    }
    
    public Instant readInstant() {
        return Instant.ofEpochMilli(this.readLong());
    }

    public String readString() {
        int len = this.readShort();
        String data = this.buffer.toString(this.buffer.readerIndex(), len, StandardCharsets.UTF_8);
        this.buffer.readerIndex(this.buffer.readerIndex() + len);
        return data;
    }
}
