package me.apesander.geodobbel.models;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class DiceScoreboard {

    private short code;

    private DiceAdmin[] admins;
    private DiceSpectator[] spectators;
    private DicePlayer[] players;

    public DiceScoreboard(short code) {
        this.code = code;
    }

    public void show(DiceUsers users) {
        players = users.getPlayerList();
        admins = users.getAdminList();
        spectators = users.getSpectatorList();

        update();
    }

    public void update(){
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

        Objective objective = scoreboard.registerNewObjective("dicescoreboard", "dummy", "§6" + code);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        for (int i = 0; i < players.length; i++) {
            DicePlayer player = players[i];
            Score score = objective.getScore("§3" + player.getPlayer().getName() + ": §6" + player.getScore());
            score.setScore(players.length-i);
        }

        for (DicePlayer player : players) {
            player.getPlayer().setScoreboard(scoreboard);
        }

        for (DiceAdmin player : admins) {
            player.getPlayer().setScoreboard(scoreboard);
        }

        for (DiceSpectator player : spectators) {
            player.getPlayer().setScoreboard(scoreboard);
        }
    }
}
