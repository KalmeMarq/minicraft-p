package me.kalmemarq.minicraft.client.network;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import me.kalmemarq.minicraft.network.Packet;
import me.kalmemarq.minicraft.network.packet.InTimePacket;
import me.kalmemarq.minicraft.network.packet.PingPacket;

public class ClientNetworkHandler extends SimpleChannelInboundHandler<Packet> {
    private Channel channel;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.channel = ctx.channel();
    }
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet packet) throws Exception {
        if (packet instanceof PingPacket pingPacket) {
            System.out.println("Your ping is: " + (TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - pingPacket.getTime())) + "ms");
        } else if (packet instanceof InTimePacket inTimePacket) {
            System.out.println("Time-offset: " + (new Date(inTimePacket.getTime() - System.currentTimeMillis())));
        }
    }
}
