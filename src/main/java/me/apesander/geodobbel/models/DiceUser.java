package me.apesander.geodobbel.models;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

// This is the base object of a dice player, admin, or spectator
public class DiceUser {
    protected UUID userId;

    public DiceUser(UUID userId) {
        this.userId = userId;
    }

    public final Player getPlayer() {
        return Bukkit.getPlayer(userId);
    }
}
