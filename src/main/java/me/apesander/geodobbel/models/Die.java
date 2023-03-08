package me.apesander.geodobbel.models;

import me.apesander.geodobbel.constants.Numbers;
import me.apesander.geodobbel.utils.RandomNum;

import java.util.ArrayList;

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

        ArrayList<Face> tempfaces = new ArrayList<>();

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

            System.out.println(face);

            if (face.contains("-")) values = getValues(face);
            else values.add(Short.parseShort(face));

            for (int i = 0; i < amount; i++) {
                for (short value : values) {
                    tempfaces.add(new Face(value, name, this.name));
                }
            }
        }

        this.faces = tempfaces.toArray(new Face[0]);
    }

    private ArrayList<Short> getValues(String facePattern) throws NumberFormatException {
        String[] values = facePattern.split("-");

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
        Face face = faces[RandomNum.genShort((short) 0, (short) (faces.length-1))];
        System.out.println(faces.length);
        System.out.println(face.name);
        return face;
    }
}
