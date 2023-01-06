package me.kalmemarq.minicraft.entity;

import me.kalmemarq.minicraft.util.CollisionBox;
import me.kalmemarq.minicraft.util.Direction;
import me.kalmemarq.minicraft.util.Vec2i;
import me.kalmemarq.minicraft.world.Level;
import me.kalmemarq.minicraft.world.World;

public abstract class Entity {
    private World world;
    private Level level;

    private Vec2i pos;
    private Vec2i prevPos;

    private Direction direction = Direction.DOWN;
    private Direction prevDirection = direction;

    private boolean moving;
    private boolean swimming;

    private boolean removed;

    protected CollisionBox collisionBox;

    public Entity(World world, Level level) {
        this.world = world;
        this.level = level;
        this.collisionBox = new CollisionBox(6, 6);
    }

    public void onDirectionChange() {
    }

    public void tick() {
        this.moving = this.prevPos.getX() != this.pos.getX() || this.prevPos.getY() != this.pos.getY();

        int dx = this.pos.getX() - this.prevPos.getX();
        int dy = this.pos.getY() - this.prevPos.getY();

        if (dx != 0 || dy != 0) {
            this.direction = Direction.get(dx, dy);
        }

        if (this.direction != this.prevDirection) {
            this.onDirectionChange();
        }

        this.prevDirection = this.direction;
    }

    public void move(Vec2i movement) {}

    public void render() {}

    public void moveToLevel(Level level) {
    }
}
