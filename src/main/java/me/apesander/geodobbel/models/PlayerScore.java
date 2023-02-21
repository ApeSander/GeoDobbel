package me.apesander.geodobbel.models;

import me.apesander.geodobbel.constants.Numbers;
import me.apesander.geodobbel.enums.ScoreMode;

import java.util.ArrayList;

// This object holds all rolls of a player and calculates the score
public class PlayerScore {

    public ScoreMode scoreMode = ScoreMode.NONE;
    private ArrayList<Roll> rolls = new ArrayList<>();

    public void addRoll(Roll roll) {
        rolls.add(roll);
    }

    public float getScore() {
        switch (scoreMode) {
            case HIGHEST:
                return highest();
            case LOWEST:
                return lowest();
            case ADD:
                return add();
            case AVERAGE:
                return average();
            case NONE:
                return Numbers.MIN_FACE_VALUE - 1;
        }

        return Numbers.MIN_FACE_VALUE - 1;
    }

    private float highest() {
        float x = 0;

        for (Roll roll : rolls) {
            if (roll.getCalculatedResult() > x) x = roll.getCalculatedResult();
        }

        return x;
    }

    private float lowest() {
        float x = Short.MAX_VALUE;

        for (Roll roll : rolls) {
            if (roll.getCalculatedResult() < x) x = roll.getCalculatedResult();
        }

        return x;
    }

    private float add() {
        float x = 0;

        for (Roll roll : rolls) {
            x += roll.getCalculatedResult();
        }

        return x;
    }

    private float average() {
        float x = 0;

        for (Roll roll : rolls) {
            x += roll.getCalculatedResult();
        }

        x /= rolls.size();

        return x;
    }
}
