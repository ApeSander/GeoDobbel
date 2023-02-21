package me.apesander.geodobbel.data;

import me.apesander.geodobbel.models.Game;

import org.bukkit.entity.Player;

import java.util.ArrayList;

// This class stores all active games
public class ActiveGames {
    public static ArrayList<Game> map = new ArrayList<>();

    public static void add(short code, Player player) {
        map.add(new Game(code, player));
    }

    public static void remove(short code) {
        for (int i = 0; i < map.size(); i++) {
            if (map.get(i).code == code) map.remove(i);
            return;
        }
    }

    public static Game getByPlayer(Player player) {
        for (Game game : map) {
            if (game.containsPlayer(player)) return game;
        }

        return null;
    }

    public static Game getByAdmin(Player player) {
        for (Game game : map) {
            if (game.containsAdmin(player)) return game;
        }
        return null;
    }

    public static Game getByUser(Player player) {
        for (Game game : map) {
            if (game.containsUser(player)) return game;
        }

        return null;
    }

    public static Game getByCode(short code) {
        for (Game game : map) {
            if (game.code == code) return game;
        }

        return null;
    }
}
