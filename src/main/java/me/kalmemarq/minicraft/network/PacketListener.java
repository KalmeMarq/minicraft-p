package me.kalmemarq.minicraft.network;

import me.kalmemarq.minicraft.network.packet.C2SExitPacket;
import me.kalmemarq.minicraft.network.packet.C2STimePacket;
import me.kalmemarq.minicraft.network.packet.MessagePacket;
import me.kalmemarq.minicraft.network.packet.PingPacket;
import me.kalmemarq.minicraft.network.packet.PlaySoundPacket;
import me.kalmemarq.minicraft.network.packet.S2CTimePacket;

public interface PacketListener {
    default void onMessage(MessagePacket packet) {}
    default void onPlaySound(PlaySoundPacket packet) {}
    default void onTime(S2CTimePacket packet) {}
    default void onTime(C2STimePacket packet) {}
    default void onPing(PingPacket packet) {}
    default void onExit(C2SExitPacket packet) {}
}