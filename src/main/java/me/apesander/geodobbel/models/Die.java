package me.apesander.geodobbel.models;

import me.apesander.geodobbel.constants.Numbers;

import java.util.ArrayList;
import java.util.Random;

// This object is a die
public class Die {
    public Die(String name, String facePattern) throws NumberFormatException {
        this.name = name;
        setFaces(facePattern);
    }

    public String name;
    private Face[] faces;

    private void setFaces(String facePattern) throws NumberFormatException {
        facePattern = facePattern.replaceAll("\"", "");
        facePattern = facePattern.replaceAll("'", "");
        facePattern = facePattern.replaceAll(" ", "");
        String[] faces = facePattern.split(",");

        for (String face : faces) {
            String name = face.substring(face.indexOf('(') + 1, face.indexOf(')'));
            ArrayList<Short> values = new ArrayList<>();
            short amount = 0;

            for (int i = 0; i < face.length(); i++) {
                if (face.charAt(i) == '*') amount++;
            }

            if (amount == 0) amount = 1;

            face = face.replaceAll("\\(.*\\)", "");
            face = face.replaceAll("\\*", "");

            if (face.contains("-")) values = getValues(face);

            this.faces = new Face[values.size() * amount];

            for (int i = 0; i < amount; i++) {
                for (short value : values) {
                    this.faces[i] = new Face(value, name);
                }
            }
        }
    }

    private ArrayList<Short> getValues(String regex) throws NumberFormatException {
        String[] values = regex.split("-");

        short num1 = Short.parseShort(values[0]);
        short num2 = Short.parseShort(values[1]);
        short temp;

        if (num1 > num2) {
            temp = num1;
            num1 = num2;
            num2 = temp;
        }

        if (num1 < Numbers.MIN_FACE_VALUE ||
                num1 > Numbers.MAX_FACE_VALUE || num2 > Numbers.MAX_FACE_VALUE) throw new NumberFormatException();

        ArrayList<Short> result = new ArrayList<>();

        for (short i = num1; i <= num2; i++) {
            result.add(i);
        }

        return result;
    }

    public Face roll() {
        Random random = new Random();
        return faces[random.nextInt(faces.length)];
    }
}
