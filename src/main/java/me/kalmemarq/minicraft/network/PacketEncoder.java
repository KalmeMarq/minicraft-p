package me.kalmemarq.minicraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketEncoder extends MessageToByteEncoder<Packet> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf out) throws Exception {
        int id = Packet.PACKETS.indexOf(packet.getClass());

        if (id == -1) {
            throw new NullPointerException("Could not find id for packet " + packet.getClass().getSimpleName());
        }

        out.writeByte(id);
        packet.write(new PacketByteBuf(out));
    }
}
