package me.apesander.geodobbel.models;

import me.apesander.geodobbel.enums.RollMode;
import me.apesander.geodobbel.enums.ScoreMode;
import me.apesander.geodobbel.utils.RandomNum;

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

    public Roll rollAll(Dice dice) {
        Roll roll = dice.rollAll();
        score.addRoll(roll);
        return roll;
    }

    public Roll roll(Dice dice, String namePattern){
        Roll roll = dice.roll(namePattern);
        score.addRoll(roll);
        return roll;
    }

    public Roll freeRoll(RollMode mode, short range, short amount) {
        Face[] faces = new Face[amount];

        for (int i = 0; i < amount; i++) {
            Face face = new Face(RandomNum.genShort((short) 1,range), null, "" + i+1);
            faces[0] = face;
        }

        Roll roll = new Roll(mode, faces);
        score.addRoll(roll);
        return roll;
    }

    public float getScore() {
        return score.getScore();
    }
}
