package me.kalmemarq.minicraft.network.packet;

import me.kalmemarq.minicraft.network.Packet;
import me.kalmemarq.minicraft.network.PacketByteBuf;
import me.kalmemarq.minicraft.network.PacketListener;
import me.kalmemarq.minicraft.util.Identifier;
import me.kalmemarq.minicraft.util.math.MathHelper;

public class PlaySoundPacket extends Packet {
    private Identifier soundEvent;
    private float volume;

    public PlaySoundPacket() {}

    public PlaySoundPacket(Identifier soundEvent, float volume) {
        this.soundEvent = soundEvent;
        this.volume = MathHelper.clamp(volume, 0.0f, 1.0f);
    }

    @Override
    public void read(PacketByteBuf buffer) throws Exception {
        this.soundEvent = buffer.readIdentifier();
        this.volume = MathHelper.clamp(buffer.readFloat(), 0.0f, 1.0f);
    }

    @Override
    public void write(PacketByteBuf buffer) throws Exception {
        buffer.writeIdentifier(this.soundEvent);
        buffer.writeFloat(this.volume);
    }

    @Override
    public void handle(PacketListener listener) {
        listener.onPlaySound(this);
    }

    public Identifier getSoundEvent() {
      return this.soundEvent;
    }
}
