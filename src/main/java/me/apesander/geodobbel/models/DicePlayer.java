package me.apesander.geodobbel.models;

import me.apesander.geodobbel.enums.ScoreMode;

import java.util.UUID;

// This object is a dice player
public class DicePlayer extends DiceUser {
    private PlayerScore score = new PlayerScore();

    public DicePlayer(UUID userId) {
        super(userId);
    }

    public void setScoreMode(ScoreMode scoreMode) {
        score.scoreMode = scoreMode;
    }

    public String[] roll(Dice dice) {
        Roll roll = dice.rollAll();
        return roll.getResultNames();
    }
}
