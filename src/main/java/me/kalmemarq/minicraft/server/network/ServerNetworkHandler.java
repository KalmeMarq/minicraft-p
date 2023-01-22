package me.kalmemarq.minicraft.server.network;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import me.kalmemarq.minicraft.network.Packet;
import me.kalmemarq.minicraft.network.packet.ExitPacket;
import me.kalmemarq.minicraft.network.packet.InTimePacket;
import me.kalmemarq.minicraft.network.packet.OutTimePacket;
import me.kalmemarq.minicraft.network.packet.PingPacket;

public class ServerNetworkHandler extends SimpleChannelInboundHandler<Packet> {
    private Channel channel;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.channel = ctx.channel();
    }
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet packet) throws Exception {
        if (packet instanceof PingPacket) {
            this.channel.writeAndFlush(packet, this.channel.voidPromise());
        } else if (packet instanceof InTimePacket) {
            this.channel.writeAndFlush(new OutTimePacket(System.currentTimeMillis() + (short) (Math.random() * Short.MAX_VALUE)), this.channel.voidPromise());
        } else if (packet instanceof ExitPacket) {
            this.channel.close();
        }
    }
}
