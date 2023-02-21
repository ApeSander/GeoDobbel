package me.apesander.geodobbel.models;

import me.apesander.geodobbel.enums.ScoreMode;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;

// This object holds dice users in a game
public class DiceUsers {

    private ArrayList<DiceAdmin> admins = new ArrayList<>();
    private ArrayList<DicePlayer> players = new ArrayList<>();
    private ArrayList<DiceSpectator> spectators = new ArrayList<>();

    public void shufflePlayers() {
        Collections.shuffle(players);
    }

    public void setOperator(Player player) {
        getAdmin(player).operator = true;
    }

    public void addUser(Player player) {
        spectators.add(new DiceSpectator(player.getUniqueId()));
    }

    public void setPlayerScoreMode(ScoreMode scoreMode) {
        for (DicePlayer user : players) {
            user.setScoreMode(scoreMode);
        }
    }
    public void remUser(Player player) {
        spectators.remove(getSpectator(player));
        players.remove(getPlayer(player));
        admins.remove(getAdmin(player));
    }

    public void promoteUser(Player player) {
        if (containsSpectator(player)) {
            spectators.remove(getSpectator(player));
            players.add(new DicePlayer(player.getUniqueId()));
        } else if (containsPlayer(player)) {
            admins.add(new DiceAdmin(player.getUniqueId(), false));
        }
    }

    public void demoteUser(Player player) {
        if (getAdmin(player) != null) {
            admins.remove(getAdmin(player));
        }

        if (getPlayer(player) != null) {
            admins.remove(getAdmin(player));
            players.remove(getPlayer(player));
        }
    }

    public void setAdminPlaying(Player player, boolean playing) {
        if (playing);
    }

    public void showMessage(String message) {
        for (DicePlayer user : players) {
            if (getAdmin(user.getPlayer()) != null) user.getPlayer().sendMessage(message);
        }
        for (DiceAdmin user : admins) {
            user.getPlayer().sendMessage(message);
        }
        for (DiceSpectator spectator : spectators) {
            spectator.getPlayer().sendMessage(message);
        }
    }

    public void showAdminMessage(String message) {
        for (DiceAdmin user : admins) {
            user.getPlayer().sendMessage(message);
        }
    }

    public int indexOfPlayer(Player player) {
        return players.indexOf(getPlayer(player));
    }

    public DiceAdmin getAdmin(Player player) {
        for (DiceAdmin user : admins) {
            if (Bukkit.getPlayer(user.userId) == player) return user;
        }

        return null;
    }

    public DicePlayer getPlayer(Player player) {
        for (DicePlayer user : players) {
            if (Bukkit.getPlayer(user.userId) == player) return user;
        }

        return null;
    }

    public DiceSpectator getSpectator(Player player) {
        for (DiceSpectator user : spectators) {
            if (Bukkit.getPlayer(user.userId) == player) return user;
        }

        return null;
    }

    public DicePlayer[] getPlayerList() {
        return players.toArray(new DicePlayer[0]);
    }

    public DiceSpectator[] getSpectatorList() {
        return spectators.toArray(new DiceSpectator[0]);
    }

    public DiceAdmin[] getAdminList() {
        return admins.toArray(new DiceAdmin[0]);
    }

    public boolean containsUser(Player player) {
        for (DicePlayer user : players) {
            if (Bukkit.getPlayer(user.userId) == player) return true;
        }
        for (DiceSpectator user : spectators) {
            if (Bukkit.getPlayer(user.userId) == player) return true;
        }
        for (DiceAdmin user : admins) {
            if (Bukkit.getPlayer(user.userId) == player) return true;
        }

        return false;
    }

    public boolean containsPlayer(Player player) {
        for (DicePlayer user : players) {
            if (Bukkit.getPlayer(user.userId) == player) return true;
        }
        return false;
    }

    public boolean containsSpectator(Player player) {
        for (DiceSpectator user : spectators) {
            if (Bukkit.getPlayer(user.userId) == player) return true;
        }
        return false;
    }

    public boolean containsAdmin(Player player) {
        for (DiceAdmin user : admins) {
            if (Bukkit.getPlayer(user.userId) == player) return true;
        }
        return false;
    }

    public boolean containsOperator(Player player) {
        for (DiceAdmin user : admins) {
            if (user.operator && Bukkit.getPlayer(user.userId) == player) return true;
        }

        return false;
    }
}
