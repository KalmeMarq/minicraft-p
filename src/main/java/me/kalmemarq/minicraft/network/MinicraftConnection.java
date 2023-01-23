package me.kalmemarq.minicraft.network;

import org.jetbrains.annotations.Nullable;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MinicraftConnection extends SimpleChannelInboundHandler<Packet> {
    private Channel channel;
    @Nullable
    private PacketListener listener;

    public void setListener(PacketListener listener) {
        this.listener = listener;
    }
    
    public void sendPacket(Packet packet) {
        if (!this.channel.isOpen()) return;
    
        this.channel.writeAndFlush(packet, this.channel.voidPromise());
    }

    public void disconnect() {
        if (!this.channel.isOpen()) return;
    
        this.channel.close().awaitUninterruptibly();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.channel = ctx.channel();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet packet) throws Exception {
        if (!this.channel.isOpen()) return;

        packet.handle(this.listener);
    }
}
