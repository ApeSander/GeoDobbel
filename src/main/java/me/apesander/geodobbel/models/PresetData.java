package me.apesander.geodobbel.models;

// This object holds all data a preset needs
public class PresetData {
    public PresetData(Die[] dice, Settings settings) {
        this.dice = dice;
        this.settings = settings;
    }

    public Die[] dice;
    public Settings settings;
}
