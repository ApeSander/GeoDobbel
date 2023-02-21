package me.apesander.geodobbel.models;

// This object is a game preset
public class Preset {
    public Preset(String name, PresetData data) {
        this.name = name;
        this.data = data;
    }

    public String name;
    public PresetData data;
}
