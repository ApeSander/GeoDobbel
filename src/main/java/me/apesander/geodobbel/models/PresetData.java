package me.apesander.geodobbel.models;

import java.util.ArrayList;

// This object holds all data a preset needs
public class PresetData {
    public PresetData(ArrayList<Die> dice, Settings settings) {
        this.dice = dice;
        this.settings = settings;
    }

    public ArrayList<Die> dice;
    public Settings settings;
}
