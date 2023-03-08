package me.apesander.geodobbel.models;

import me.apesander.geodobbel.enums.GameState;
import me.apesander.geodobbel.enums.ScoreMode;
import me.apesander.geodobbel.enums.TurnMode;
import org.bukkit.entity.Player;

// This object is a dice game holding all the data like players, settings and dice
public class Game {
    public Game(short code, Player player) {
        this.code = code;
        users.addUser(player);
        users.promoteUser(player);
        users.promoteUser(player);
        users.setOperator(player);
    }

    public short code;
    public Settings settings = new Settings();
    private Dice dice = new Dice();
    private Turn turn = new Turn();
    private DiceUsers users = new DiceUsers();
    public GameState state = GameState.START;
    private DiceScoreboard scoreboard;

    public void promoteUser(Player player) {
        users.promoteUser(player);
    }

    public void demoteUser(Player player) {
        users.demoteUser(player);
    }

    public void addDice(String namePattern, String facePattern) throws NumberFormatException {
        dice.add(namePattern, facePattern);
    }

    public void toggleAdminPlaying(Player player) {
        users.setAdminPlaying(player, !users.containsPlayer(player));
    }

    public void remDice(String namePattern) {
        dice.remove(namePattern);
    }

    public String[] diceList() {
        return dice.getNameList();
    }

    public void addUser(Player player) {
        users.addUser(player);
    }

    public void remUser(Player player) {
        users.remUser(player);
    }

    public boolean isTurnOf(Player player) {
        if (settings.turnMode == TurnMode.OFF) return true;

        return users.indexOfPlayer(player) == turn.get();
    }

    public DicePlayer getCurrentPlayer() {
        if (settings.turnMode == TurnMode.OFF) return null;
        return users.getPlayer(turn.get());
    }

    public Roll rollAllDice(Player player) {
        Roll roll = users.getPlayer(player).rollAll(dice);
        scoreboard.update();
        return roll;
    }

    public Roll rollDice(Player player, String namePattern) {
        Roll roll;
        if (namePattern.equals("*")) roll = users.getPlayer(player).rollAll(dice);
        else roll = users.getPlayer(player).roll(dice, namePattern);
        scoreboard.update();
        return roll;
    }

    public Roll freeRoll(Player player, short range, short amount) {
        Roll roll = users.getPlayer(player).freeRoll(settings.rollMode, range, amount);
        scoreboard.update();
        return roll;
    }

    public void nextTurn() {
        turn.nextTurn();
    }

    public void pastTurn() {
        turn.pastTurn();
    }

    public void start() {
        turn.setTurnMode(settings.turnMode);
        turn.playerSize = (short) users.getPlayerList().length;
        dice.rollMode = settings.rollMode;
        for (DicePlayer player : getPlayers()) {
            player.setScoreMode(settings.scoreMode);
        }

        scoreboard = new DiceScoreboard(code, settings.order);

        scoreboard.show(users);

        state = GameState.PLAY;
    }

    public void setTurn(Player player) {
        turn.setTurn((short) users.indexOfPlayer(player));
    }

    public void stop() {
        state = GameState.END;
    }

    public DicePlayer[] getPlayers() {
        return users.getPlayerList();
    }

    public DiceSpectator[] getSpectators() {
        return users.getSpectatorList();
    }

    public DiceAdmin[] getAdmins() {
        return users.getAdminList();
    }

    public DicePlayer getPlayer(Player player) {
        return users.getPlayer(player);
    }

    public boolean containsPlayer(Player player) {
        return users.containsPlayer(player);
    }

    public boolean containsSpectator(Player player) {
        return users.containsSpectator(player);
    }

    public boolean containsAdmin(Player player) {
        return users.containsAdmin(player);
    }

    public boolean containsUser(Player player) {
        return users.containsUser(player);
    }

    public boolean containsOperator(Player player) {
        return users.containsOperator(player);
    }

    public void showMessage(String message) {
        users.showMessage(message);
    }

    public void showAdminMessage(String message) {
        users.showAdminMessage(message);
    }

    public Die[] getDice() {
        return dice.getList();
    }

    public void setDice(Die[] dice) {
        this.dice.set(dice);
    }
}
