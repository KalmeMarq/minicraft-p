package me.kalmemarq.minicraft.item;

import me.kalmemarq.minicraft.world.Level;
import me.kalmemarq.minicraft.world.World;

public class Item {
    private final int maxAmount;

    public Item() {
        this(1);
    }

    public Item(int maxAmount) {
        this.maxAmount = maxAmount;
    }

    public void use(World world, Level level) {}

    public int getMaxAmount() {
      return maxAmount;
    }

    public void renderInMenu(int x, int y) {
    }
}
