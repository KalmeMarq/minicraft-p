package me.kalmemarq.minicraft.network;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class PacketDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {
        byte id = (byte)byteBuf.readUnsignedByte();
        Class<? extends Packet> packetClass = Packet.PACKETS.get(id);

        if (packetClass == null) {
            throw new NullPointerException("Packet of id " + id + " is invalid. Could not found packet id.");
        }

        Packet packet = packetClass.getDeclaredConstructor().newInstance();
        packet.read(new PacketByteBuf(byteBuf));
        out.add(packet);
    }
}
