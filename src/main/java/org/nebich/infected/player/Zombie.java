package org.nebich.infected.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class Zombie extends InfectedPlayer implements Listener{
    private final Player player;
    private boolean isSelectedAtStart;

    public Zombie(Player player) {
        super(player, true);
        this.player = player;
        this.isSelectedAtStart = false;
    }

    public Player getPlayer() {
        return player;
    }

    public void transform() {
        // Will be implemented with zombies features
    }

    public boolean isSelectedAtStart() {
        return isSelectedAtStart;
    }

    public void setSelectedAtStart(boolean selectedAtStart) {
        isSelectedAtStart = selectedAtStart;
    }
}
