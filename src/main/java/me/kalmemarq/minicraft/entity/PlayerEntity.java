package me.kalmemarq.minicraft.entity;

import me.kalmemarq.minicraft.client.gfx.Renderer;
import me.kalmemarq.minicraft.client.util.Keybinding;
import me.kalmemarq.minicraft.util.Direction;
import me.kalmemarq.minicraft.util.math.Vec2d;
import me.kalmemarq.minicraft.world.Level;
import me.kalmemarq.minicraft.world.World;

public class PlayerEntity extends LivingEntity {
    public PlayerEntity(World world, Level level) {
        super(world, level);
        this.pos = new Vec2d(100, 100);
        this.prevPos = new Vec2d(100, 100);
    }

    public void updateCamera() {
        Renderer.camera.centerOn(
            ((int)(Math.round(this.pos.getX()))) + (this.collisionBox.getWidth() / 2), ((int)Math.round(this.pos.getY())) + (this.collisionBox.getHeight() / 2),
            0, 0,
            Level.toPixel(this.level.getWidth()), Level.toPixel(this.level.getHeight())
        );
    }

    private static double oneSqrtTwo = 1 / Math.sqrt(2); 

    @Override
    public void tick() {
        super.tick();

        double dx = 0;
        double dy = 0;

        if (Keybinding.MOVE_UP.down()) {
            --dy;
        }

        if (Keybinding.MOVE_DOWN.down()) {
            ++dy;
        }

        if (Keybinding.MOVE_LEFT.down()) {
            --dx;
        }

        if (Keybinding.MOVE_RIGHT.down()) {
            ++dx;
        }

        if (dx < 0 && dy < 0) {
            dx = -oneSqrtTwo;
            dy = -oneSqrtTwo;
        } else if (dx > 0 && dy > 0) {
            dx = oneSqrtTwo;
            dy = oneSqrtTwo;
        } else if (dx < 0 && dy > 0) {
            dx = -oneSqrtTwo;
            dy = oneSqrtTwo;
        } else if (dx > 0 && dy < 0) {
            dx = oneSqrtTwo;
            dy = -oneSqrtTwo;
        }

        this.move(new Vec2d(dx, dy));

        if (dx != 0 || dy != 0) {
            this.direction = Direction.get(dx, dy);
        }
    }

    @Override
    public void render() {
        Renderer.render("tiles.png", ((int)Math.round(this.pos.getX())) - this.collisionBox.getWidth() / 2, ((int)Math.round(this.pos.getY())) - this.collisionBox.getHeight() / 2, 0, 0, 16, 16);
    }

    @Override
    public int getMaxHealth() {
        return 10;
    }

    @Override
    public int getMaxStamina() {
        return 10;
    }
}
