package me.kalmemarq.minicraft.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import me.kalmemarq.minicraft.Minicraft;
import me.kalmemarq.minicraft.client.gfx.Renderer;
import me.kalmemarq.minicraft.client.network.ClientNetworkHandler;
import me.kalmemarq.minicraft.network.PacketDecoder;
import me.kalmemarq.minicraft.network.PacketEncoder;
import me.kalmemarq.minicraft.network.packet.ExitPacket;
import me.kalmemarq.minicraft.network.packet.OutTimePacket;
import me.kalmemarq.minicraft.network.packet.PingPacket;
import me.kalmemarq.minicraft.util.OperatingSystem;
import me.kalmemarq.minicraft.util.Util;
import me.kalmemarq.optionparser.ArgOption;
import me.kalmemarq.optionparser.ArgOptionParser;

public class Main {
    public static void main(String[] args) {
        Util.Logging.setupCustomOutputs();

        ArgOptionParser optionParser = new ArgOptionParser();
        ArgOption<Boolean> debug = optionParser.accepts("debug", Boolean.class);
        ArgOption<Boolean> fullscreen = optionParser.accepts("fullscreen", Boolean.class);
        ArgOption<Boolean> maximized = optionParser.accepts("maximized", Boolean.class);
        ArgOption<File> gameDir = optionParser.accepts("gameDir", File.class).defaultsTo(new File(OperatingSystem.getOS().getAppData(), "playminicraft/mods/Minicraft_P"));
        ArgOption<Integer> width = optionParser.accepts("width", Integer.class).alias("w").defaultsTo(Renderer.WIDTH * 3);
        ArgOption<Integer> height = optionParser.accepts("height", Integer.class).alias("h").defaultsTo(Renderer.HEIGHT * 3);
        ArgOption<String> serverIp = optionParser.accepts("serverIp", String.class);
        ArgOption<Integer> serverPort = optionParser.accepts("serverPort", Integer.class).defaultsTo(25565);

        optionParser.parse(args);

        ClientRunArgs runArgs = ClientRunArgs.fromArgs(debug, maximized, fullscreen, gameDir, width, height, serverIp, serverPort);

        if (runArgs.serverIp() == null) {
            Minicraft mc;

            try {
                mc = new Minicraft(runArgs);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }

            try {
                mc.run();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mc.stop();
            }

            System.exit(0);
        } else {
            boolean EPOLL = Epoll.isAvailable();
            EventLoopGroup eventLoopGroup = EPOLL ? new EpollEventLoopGroup() : new NioEventLoopGroup();

            try {
                Channel channel = new Bootstrap()
                    .group(eventLoopGroup)
                    .channel(EPOLL ? EpollSocketChannel.class : NioSocketChannel.class)
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            channel.pipeline().addLast(new PacketDecoder()).addLast(new PacketEncoder()).addLast(new ClientNetworkHandler());
                        }
                    })
                    .connect(runArgs.serverIp(), runArgs.serverPort()).sync().channel();

                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

                String line;
                while ((line = reader.readLine()) != null) {
                    if (line == "") {
                        continue;
                    }
                 
                    if (line.startsWith("ping")) {
                        channel.writeAndFlush(new PingPacket(System.nanoTime()), channel.voidPromise());
                    } else if (line.startsWith("time")) {
                        channel.writeAndFlush(new OutTimePacket());
                    } else if (line.startsWith("exit")) {
                        channel.writeAndFlush(new ExitPacket(), channel.voidPromise());
                        // System.out.println("Waiting diconnect");
                        channel.closeFuture().syncUninterruptibly();
                        // System.out.println("Diconnected");
                        break;
                    }
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                eventLoopGroup.shutdownGracefully();
            }

            Util.shutdownWorkers();
            System.exit(0);
        }
    }
}
