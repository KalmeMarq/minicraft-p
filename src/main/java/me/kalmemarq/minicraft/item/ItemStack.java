package me.kalmemarq.minicraft.item;

import me.kalmemarq.minicraft.client.Minicraft;

public class ItemStack {
    public static final ItemStack EMPTY = new ItemStack(null);

    private final Item item;
    private int amount;

    public ItemStack(Item item) {
        this(item, 1);
    }

    public ItemStack(Item item, int amount) {
        this.item = item;
        this.amount = amount;
    }

    public Item getItem() {
      return item == null ? Items.AIR : item;
    }

    public int getAmount() {
      return amount;
    }

    public void setAmount(int amount) {
      this.amount = amount;
    }

    public void incrementAmount(int amount) {
        this.amount += amount;

        if (this.amount < 0) {
            this.amount = 0;
        } else if (this.amount >= this.getMaxAmount()) {
            this.amount = this.getMaxAmount();

            Minicraft.LOGGER.warn("Tried to surpase the max amount for an item");
        }
    }

    public void decrementAmount(int amount) {
        this.incrementAmount(-amount);
    }

    public int getMaxAmount() {
        return this.item.getMaxAmount();
    }

    public boolean isStackable() {
        return this.getMaxAmount() > 1;
    }

    public boolean isEmpty() {
        if (this == EMPTY) return true;
        if (this.item == null || this.item == Items.AIR || this.amount <= 0) return true;
        return false;
    }

    public String toString() {
        return this.amount + " " + this.item;
    }
}
