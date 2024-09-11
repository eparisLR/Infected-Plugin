package org.nebich.infected.tasks.game;

import org.bukkit.scheduler.BukkitRunnable;
import org.nebich.infected.game.GameManager;
import org.nebich.infected.player.PlayerManager;

public class GameEndTask extends BukkitRunnable {
    private final PlayerManager playerManager;
    private final GameManager gameManager;

    public GameEndTask(PlayerManager playerManager, GameManager gameManager) {
        this.playerManager = playerManager;
        this.gameManager = gameManager;
    }

    @Override
    public void run() {
        if (this.playerManager.getPlayers() != null && this.playerManager.getPlayers().isEmpty()) {
            this.gameManager.end();
            cancel();
        }
    }
}