package me.kalmemarq.minicraft.network;

import java.nio.charset.StandardCharsets;
import java.time.Instant;

import io.netty.buffer.ByteBuf;
import me.kalmemarq.minicraft.util.Identifier;

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

    public void writeFloat(float value) {
        this.buffer.writeFloat(value);
    }

    public void writeVarInt(int value) {
        while (true) {
            if ((value & 0xFFFFFF80) == 0) {
                this.writeByte(value);
                return;
            }
        
            this.writeByte(value & 0x7F | 0x80);
            value >>>= 7;
        }
    }

    public void writeVarLog(int value) {
        // TODO
    }

    public void writeInstant(Instant value) {
        this.writeLong(value.toEpochMilli());
    }

    public void writeString(String value) {
        byte[] data = value.getBytes(StandardCharsets.UTF_8);
        this.writeVarInt(data.length);
        this.buffer.writeBytes(data);
    }

    public void writeIdentifier(Identifier value) {
        this.writeString(value.toString());
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

    public float readFloat() {
        return this.buffer.readFloat();
    }

    public int readVarInt() {
        int result = 0;
        int shift = 0;
        int b;
        do {
          if (shift >= 32) {
            throw new IndexOutOfBoundsException("varint too long");
          }
          b = this.buffer.readByte();
          result |= (b & 0x7F) << shift;
          shift += 7;
        } while ((b & 0x80) != 0);
        return result;
    }

    public long readVarLong() {
        // TODO
        return 0L;
    }
    
    public Instant readInstant() {
        return Instant.ofEpochMilli(this.readLong());
    }

    public String readString() {
        int len = this.readVarInt();
        String data = this.buffer.toString(this.buffer.readerIndex(), len, StandardCharsets.UTF_8);
        this.buffer.readerIndex(this.buffer.readerIndex() + len);
        return data;
    }

    public Identifier readIdentifier() {
        return new Identifier(this.readString());
    }
}
