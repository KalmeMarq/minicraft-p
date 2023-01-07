package me.kalmemarq.minicraft.gui;

import java.util.Arrays;

import me.kalmemarq.minicraft.world.GameMode;
import me.kalmemarq.minicraft.world.World;
import me.kalmemarq.minicraft.world.WorldProperties;
import me.kalmemarq.minicraft.world.WorldSize;

public class PlayMenu extends Menu {
    public PlayMenu(Menu parentMenu) {
        super();
        this.parentMenu = parentMenu;
    }

    private SelectEntry createWorldEntry;

    @Override
    protected void init() {
        entryGap = 6;
        menuAlign = MenuAlign.TOP_LEFT;

        InputEntry nameEntry = new InputEntry("minicraft.menu.world_name");
        addEntry(nameEntry);

        nameEntry.setListener(vl -> {
            if (createWorldEntry != null) {
                createWorldEntry.setEnabled(vl.trim().length() > 0);
            }
        });

        addEntry(new CycleEntry<>("minicraft.menu.gamemode", Arrays.asList(GameMode.values()), GameMode.SURVIVAL, v -> v.getName(), v -> {}));

        addEntry(new CycleEntry<>("minicraft.menu.world_size", Arrays.asList(WorldSize.values()), WorldSize.SMALL, v -> v.getName(), v -> {}));

        createWorldEntry = new SelectEntry("minicraft.menu.create_world", () -> {
            this.mc.world = new World(new WorldProperties(0, 0));

            this.mc.setMenu(null);
        });
        createWorldEntry.setEnabled(false);
        addEntry(createWorldEntry);

        addEntry(new SelectEntry("minicraft.menu.go_back", () -> {
            this.mc.setMenu(this.parentMenu);
        }), 8, 0);
    }
}
