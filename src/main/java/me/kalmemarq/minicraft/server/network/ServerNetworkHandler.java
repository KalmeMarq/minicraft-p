package me.kalmemarq.minicraft.server.network;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import me.kalmemarq.minicraft.network.Packet;
import me.kalmemarq.minicraft.network.packet.C2SExitPacket;
import me.kalmemarq.minicraft.network.packet.C2STimePacket;
import me.kalmemarq.minicraft.network.packet.MessagePacket;
import me.kalmemarq.minicraft.network.packet.PingPacket;
import me.kalmemarq.minicraft.network.packet.S2CTimePacket;

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
        } else if (packet instanceof C2STimePacket) {
            this.channel.writeAndFlush(new S2CTimePacket(System.currentTimeMillis() + (short) (Math.random() * Short.MAX_VALUE)), this.channel.voidPromise());
        } else if (packet instanceof C2SExitPacket) {
            this.disconnect();
        } else if (packet instanceof MessagePacket messagePacket) {
            System.out.println("[" + messagePacket.getTime() + "] " + messagePacket.getText());
            this.channel.writeAndFlush(messagePacket, this.channel.voidPromise());
        }
    }

    public void disconnect() {
        if (this.channel.isOpen()) {
            this.channel.close().awaitUninterruptibly();
        }
    }
}
