package me.apesander.geodobbel.models;

import me.apesander.geodobbel.enums.GameState;
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

    public void rollDice(Player player) {
        String[] faceNames = users.getPlayer(player).roll(dice);
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

        state = GameState.PLAY;
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
}
