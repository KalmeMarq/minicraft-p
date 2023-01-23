package me.kalmemarq.minicraft.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import me.kalmemarq.minicraft.network.MinicraftConnection;
import me.kalmemarq.minicraft.network.PacketDecoder;
import me.kalmemarq.minicraft.network.PacketEncoder;
import me.kalmemarq.minicraft.server.network.NetworkServerHandler;
import me.kalmemarq.optionparser.ArgOption;
import me.kalmemarq.optionparser.ArgOptionParser;

public class Main {
    private static final boolean EPOLL = Epoll.isAvailable();

    public static void main(String[] args) throws InterruptedException {
        ArgOptionParser optionParser = new ArgOptionParser();
        ArgOption<Integer> serverPort = optionParser.accepts("serverPort", Integer.class).defaultsTo(25565);
        optionParser.parse(args);
        
        ServerRunArgs runArgs = ServerRunArgs.fromArgs(serverPort);

        EventLoopGroup eventLoopGroup = EPOLL ? new EpollEventLoopGroup() : new NioEventLoopGroup();

        try {
            MinicraftConnection connection = new MinicraftConnection();
            NetworkServerHandler handler = new NetworkServerHandler(connection);
            connection.setListener(handler);

            new ServerBootstrap()
                .group(eventLoopGroup)
                .channel(EPOLL ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<Channel>() {
                    protected void initChannel(Channel channel) throws Exception {
                        channel.pipeline().addLast(new PacketDecoder()).addLast(new PacketEncoder()).addLast(connection);
                    }
                })
                .bind(runArgs.port()).sync().channel().closeFuture().syncUninterruptibly();
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }
}
