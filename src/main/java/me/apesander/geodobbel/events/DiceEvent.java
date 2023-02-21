package me.apesander.geodobbel.events;

import me.apesander.geodobbel.data.ActiveGames;
import me.apesander.geodobbel.models.Game;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

// This class handles all events that influence the dice plugin
public class DiceEvent implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        for (Game game : ActiveGames.map) {

        }
    }
}
