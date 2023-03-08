package me.apesander.geodobbel.models;

// This object is a face of a die
public class Face {
    public Face(short num, String name, String dieName) {
        this.num = num;
        this.name = name;
        this.dieName = dieName;
    }

    public String dieName;
    public short num;
    public String name;
}
