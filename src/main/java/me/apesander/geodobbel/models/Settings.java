package me.apesander.geodobbel.models;
import me.apesander.geodobbel.enums.*;

// This object holds the dice game settings
public class Settings {
    public TurnMode turnMode = TurnMode.NORMAL;
    public RollMode rollMode = RollMode.ADD;
    public ScoreMode scoreMode = ScoreMode.ADD; // Scores true
    public ScoreOrder order = ScoreOrder.DESCENDING; // Scores true
    public GameMode gameMode = GameMode.NORMAL;
}
