package me.apesander.geodobbel.models;

import me.apesander.geodobbel.constants.Numbers;
import me.apesander.geodobbel.enums.RollMode;

// This object is the roll a player did
public class Roll {
    public Roll(RollMode rollMode, Face[] result) {
        this.rollMode = rollMode;
        this.result = result;
    }

    public RollMode rollMode;
    private Face[] result;

    public String[] getResultNames() {
        String[] names = new String[result.length];

        for (int i = 0; i < result.length; i++) {
            if (result[i].name == null) names[i] = "" + result[i].num;
            else names[i] = result[i].name;
        }

        return names;
    }

    public float getCalculatedResult() {
        switch (rollMode) {
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

        for (Face face : result) {
            if (face.num > x) x = face.num;
        }

        return x;
    }

    private float lowest() {
        float x = Short.MAX_VALUE;

        for (Face face : result) {
            if (face.num < x) x = face.num;
        }

        return x;
    }

    private float add() {
        float x = 0;

        for (Face face : result) {
            x += face.num;
        }

        return x;
    }

    private float average() {
        float x = 0;

        for (Face face : result) {
            x += face.num;
        }

        x /= result.length;

        return x;
    }
}
