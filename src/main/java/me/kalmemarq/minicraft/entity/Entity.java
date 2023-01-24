package me.kalmemarq.minicraft.entity;

import me.kalmemarq.minicraft.util.CollisionBox;
import me.kalmemarq.minicraft.util.Direction;
import me.kalmemarq.minicraft.util.math.Vec2d;
import me.kalmemarq.minicraft.world.Level;
import me.kalmemarq.minicraft.world.World;

public abstract class Entity {
    protected World world;
    protected Level level;

    protected Vec2d pos = new Vec2d(0, 0);
    protected Vec2d prevPos = new Vec2d(0, 0);

    protected Direction direction = Direction.DOWN;
    protected Direction prevDirection = direction;

    protected boolean moving;
    protected boolean swimming;

    protected boolean removed;

    protected CollisionBox collisionBox;

    public Entity(World world, Level level) {
        this.world = world;
        this.level = level;
        this.collisionBox = new CollisionBox(16, 16);
    }

    public void onDirectionChange() {
    }

    public double getX() {
        return this.pos.getX();
    }

    public double getY() {
        return this.pos.getY();
    }

    public void tick() {
        this.moving = this.prevPos.getX() != this.pos.getX() || this.prevPos.getY() != this.pos.getY();

        double dx = this.pos.getX() - this.prevPos.getX();
        double dy = this.pos.getY() - this.prevPos.getY();

        if (dx != 0 || dy != 0) {
            this.direction = Direction.get(dx, dy);
        }

        if (this.direction != this.prevDirection) {
            this.onDirectionChange();
        }

        this.prevDirection = this.direction;
    }

    public void move(Vec2d movement) {
        this.prevPos.setX(this.pos.getX());
        this.prevPos.setY(this.pos.getY());
        this.pos.add(movement);

        if ((this.pos.getX() - this.collisionBox.getWidth() / 2 < 0) || (this.pos.getX() + this.collisionBox.getWidth() / 2) >= Level.toPixel(this.level.getWidth())) {
            this.pos.setX(this.prevPos.getX());
        }
        
        if (this.pos.getY() - this.collisionBox.getHeight() / 2 < 0 || (this.pos.getY() + this.collisionBox.getHeight() / 2) >= Level.toPixel(this.level.getHeight())) {
            this.pos.setY(this.prevPos.getY());
        }
    }

    public void render() {}

    public void moveToLevel(Level level) {
    }
}
