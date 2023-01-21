package me.kalmemarq.minicraft.item;

import me.kalmemarq.minicraft.util.Identifier;
import me.kalmemarq.minicraft.util.Registry;

public class Items {
    public static Registry<Identifier, Item> ITEM_REGISTRY = new Registry<>();
    
    public static Item register(String id, Item item) {
        return register(new Identifier(id), item);
    }

    public static Item register(Identifier id, Item item) {
        ITEM_REGISTRY.add(id, item);
        return item;
    }

    public static final Item AIR = Items.register("air", new Item());
}
