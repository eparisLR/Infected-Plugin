package org.nebich.infected.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.nebich.infected.Infected;
import org.nebich.infected.game.GameStatus;
import org.nebich.infected.scoreboard.InfectedScoreboard;
import org.nebich.infected.survivors.Archer;
import org.nebich.infected.survivors.Doctor;
import org.nebich.infected.survivors.Ninja;
import org.nebich.infected.survivors.Role;
import org.nebich.infected.survivors.Roles;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

public class PlayerManager implements Listener {
    private final List<InfectedPlayer> playerList = new ArrayList<>();
    private final Infected plugin;

    public PlayerManager(Infected plugin) {
        this.plugin = plugin;
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void chooseFirstZombies() {
        int survivorsSize = this.playerList.size();
        int zombiesToChoose = survivorsSize <= 10 ? 1 : Math.max(Math.round((float) survivorsSize / 10), 5);
        for (int i=0; i < zombiesToChoose; i++) {
            Random random = new SecureRandom();
            int randomIndex = random.nextInt(survivorsSize);
            InfectedPlayer playerToTransform = this.playerList.get(randomIndex);
            playerToTransform.setIsZombie(true);
            playerToTransform.setIsSurvivor(false);
            playerToTransform.setSelectedAtStart(true);
            playerToTransform.getPlayer().sendMessage("Vous venez de vous transformer en zombie.");
        }
    }

    public void addPlayer(Player player) {
        InfectedScoreboard infectedScoreboard = this.plugin.getInfectedScoreboard();
        if (this.plugin.getGameManager().getGameStatus() == GameStatus.PLAYING) {
            this.playerList.add(new InfectedPlayer(this.plugin, player, true));
            infectedScoreboard.addZombieTeamEntry(player);
        } else {
            InfectedPlayer infectedPlayer = new InfectedPlayer(this.plugin,player);
            infectedPlayer.setRole(getRandomRole(player));
            this.playerList.add(infectedPlayer);
            infectedScoreboard.addSurvivorTeamEntry(player);
        }
        player.setScoreboard(infectedScoreboard.getScoreboard());
    }

    public void selectSurvivorClass(Player player, Role role) {
        Optional<InfectedPlayer> survivor = this.playerList.stream().filter(s -> s.getPlayer().getUniqueId() == player.getUniqueId()).findFirst();
        survivor.ifPresent(infectedPlayer -> infectedPlayer.setRole(role));
    }

    public void addZombie(Player player) {
        this.playerList.add(new InfectedPlayer(this.plugin, player, true));
        InfectedScoreboard infectedScoreboard = this.plugin.getInfectedScoreboard();
        infectedScoreboard.addZombieTeamEntry(player);
        player.setScoreboard(infectedScoreboard.getScoreboard());
    }

    public Optional<InfectedPlayer> getPlayer(UUID uuid) {
        return this.playerList.stream().filter(player -> player.getPlayer().getUniqueId() == uuid).findFirst();
    }

    public List<InfectedPlayer> getSurvivors() {
        return this.playerList.stream().filter(InfectedPlayer::isSurvivor).toList();
    }

    public List<InfectedPlayer> getZombies() {
        return this.playerList.stream().filter(InfectedPlayer::isZombie).toList();
    }

    protected Role getRandomRole(Player player) {
        SecureRandom secureRandom = new SecureRandom();
        Roles[] roles = Roles.values();
        int randomIndex = secureRandom.nextInt(roles.length-1);
        switch (roles[randomIndex]) {
            case ARCHER -> {
                return new Archer(this.plugin, player);
            }
            case DOCTOR -> {
                return new Doctor(this.plugin, player);
            }
            default -> {
                return new Ninja(this.plugin, player);
            }
        }
    }

    @EventHandler
    public void handlePlayerChangeWorld(PlayerChangedWorldEvent event) {
        if (event.getFrom().getName().startsWith("world_infected")) {
            final UUID playerChangingWorldUUID = event.getPlayer().getUniqueId();
            this.playerList.removeIf(player -> player.getPlayer().getUniqueId() == playerChangingWorldUUID);
        }
    }
}
