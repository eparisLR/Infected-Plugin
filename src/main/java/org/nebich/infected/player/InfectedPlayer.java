package org.nebich.infected.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.nebich.infected.Infected;
import org.nebich.infected.survivors.Role;

import java.util.Optional;

public class InfectedPlayer implements Listener {
    private final Player player;
    private Role role;
    private boolean isZombie;
    private boolean isSurvivor;
    private boolean isSelectedAtStart = false;
    private final Infected plugin;

    protected InfectedPlayer(Infected plugin, Player player) {
        this.player = player;
        this.isSurvivor = true;
        this.isZombie = false;
        this.plugin = plugin;
    }

    protected InfectedPlayer(Infected plugin, Player player, boolean isZombie) {
        this.player = player;
        this.isSurvivor = !isZombie;
        this.isZombie = isZombie;
        this.plugin = plugin;
    }

    public Player getPlayer() {
        return this.player;
    }

    public boolean isZombie() {
        return this.isZombie;
    }

    public void setIsZombie(boolean bool) {
        this.isZombie = bool;
        this.isSurvivor = !bool;
    }

    public boolean isSurvivor() {
        return this.isSurvivor;
    }

    public void setIsSurvivor(boolean bool) {
        this.isSurvivor = bool;
        this.isZombie = !bool;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void transform() {
        this.setIsZombie(true);
    }

    public boolean isSelectedAtStart() {
        return this.isSelectedAtStart;
    }

    public void setSelectedAtStart(boolean selectedAtStart) {
        this.isSelectedAtStart = selectedAtStart;
    }

    @EventHandler
    public void handlePlayerDeath(PlayerDeathEvent event) {
        final Player deadPlayer = event.getEntity();
        Optional<InfectedPlayer> infectedPlayer = this.plugin.getPlayerManager().getPlayer(deadPlayer.getUniqueId());
        infectedPlayer.ifPresent(InfectedPlayer::transform);
    }
}
