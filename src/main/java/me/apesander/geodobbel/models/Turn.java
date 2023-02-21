package me.apesander.geodobbel.models;

import me.apesander.geodobbel.enums.TurnMode;
import me.apesander.geodobbel.utils.RandomNum;

// This object handles turns in the dice game
public class Turn {
    public short playerSize = 0;
    private TurnMode turnMode;
    private boolean toRight = true;
    private short turn = 0;
    private short pastTurn = 0;

    public void setTurnMode(TurnMode turnMode) {
        this.turnMode = turnMode;
    }

    public int get() {
        return turn;
    }

    public void nextTurn() {
        pastTurn = turn;

        switch (turnMode) {
            case NORMAL:
                turn++;
                if (turn >= playerSize) turn = 0;
            case RANDOM:
                turn = RandomNum.genShort((short) 0, (short) (playerSize-1));
            case PINGPONG:
                if (toRight) turn++;
                else turn--;

                if (turn >= playerSize) {
                    turn = (short) (playerSize-2);
                    toRight = false;
                }
                if (turn < 0) {
                    turn = 1;
                    toRight = true;
                }
        }
    }

    public void pastTurn() {
        turn = pastTurn;
    }

    public void setTurn(short turn) {
        if (turn < 0) turn = 0;
        if (turn >= playerSize) turn = (short) (playerSize-1);
        this.turn = turn;
    }
}
