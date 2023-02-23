package me.apesander.geodobbel.models;

import me.apesander.geodobbel.enums.RollMode;

import java.util.ArrayList;

// This object holds all dice in a game
public class Dice {
    private ArrayList<Die> list = new ArrayList<>();
    public RollMode rollMode = RollMode.NONE;

    public void add(String namePattern, String facePattern) {

        remove(namePattern);

        namePattern = namePattern.replaceAll("\"", "");
        namePattern = namePattern.replaceAll("'", "");
        namePattern = namePattern.replaceAll(" ", "");

        String[] names = namePattern.split(",");

        for (String name : names) {
            int amount = 0;

            for (int i = 0; i < name.length(); i++) {
                if (name.charAt(i) == '*') amount++;
            }

            if (amount <= 1) list.add(new Die(name, facePattern));
            else {
                for (int i = 1; i <= amount; i++) {
                    String diceName = name.replaceAll("\\*", "")+i;
                    list.add(new Die(diceName, facePattern));
                }
            }
        }
    }

    public void remove(String namePattern) {
        Die[] dice = getMulti(namePattern);

        for (Die die : dice) {
            list.remove(die);
        }
    }

    private Die[] getMulti(String namePattern) {
        namePattern = namePattern.replaceAll("\"", "");
        namePattern = namePattern.replaceAll("'", "");
        namePattern = namePattern.replaceAll(" ", "");

        String[] names = namePattern.split(",");
        ArrayList<Die> diceResult = new ArrayList<>();

        for (String name : names) {
            int amount = 0;

            for (int i = 0; i < name.length(); i++) {
                if (name.charAt(i) == '*') amount++;
            }

            if (amount <= 1) diceResult.add(get(name));
            else {
                for (int i = 1; i <= amount; i++) {
                    diceResult.add(get(name.replaceAll("\\*", "")+i));
                }
            }
        }

        return diceResult.toArray(new Die[0]);
    }

    private Die get(String name) {
        for (Die die : list) {
            if (name.equals(die.name)) return die;
        }

        return null;
    }

    public Roll rollAll() {
        Face[] faces = new Face[list.size()];

        for (int i = 0; i < list.size(); i++) {
            faces[i] = list.get(i).roll();
        }

        return new Roll(rollMode, faces);
    }

    public Roll roll(String namePattern) {
        Die[] dice = getMulti(namePattern);
        Face[] faces = new Face[dice.length];

        for (int i = 0; i < dice.length; i++) {
            faces[i] = dice[i].roll();
        }

        return new Roll(rollMode, faces);
    }

    public String[] getNameList() {
        String[] names = new String[list.size()];

        for (int i = 0; i < list.size(); i++) {
            names[i] = list.get(i).name;
        }

        return names;
    }
}
