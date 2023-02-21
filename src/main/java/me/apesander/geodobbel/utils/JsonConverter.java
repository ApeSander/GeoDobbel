package me.apesander.geodobbel.utils;

import com.google.gson.Gson;
import me.apesander.geodobbel.models.PresetData;

public class JsonConverter {
    public static String serializePreset(PresetData preset) {
        Gson gson = new Gson();
        return gson.toJson(preset);
    }

    public static PresetData deserializePreset(String jsonPreset) {
        Gson gson = new Gson();
        return gson.fromJson(jsonPreset, PresetData.class);
    }
}
