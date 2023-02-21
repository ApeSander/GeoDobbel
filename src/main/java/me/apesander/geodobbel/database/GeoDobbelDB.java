package me.apesander.geodobbel.database;

import me.apesander.geodobbel.models.Preset;
import me.apesander.geodobbel.models.PresetData;
import me.apesander.geodobbel.utils.JsonConverter;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

// This object handles the GeoDobbel database
public class GeoDobbelDB {

    private Connection connection;

    public Connection getConnection() throws SQLException{
        if (connection != null) return connection;

        String url = "jdbc:mysql://45.140.142.27:3306/s881_testdb_ApeSander";
        String user = "u881_OL72Lnl0Le";
        String pass = "o8Skum.UnQNgnrr+TkC@zsx3";

        this.connection = DriverManager.getConnection(url, user, pass);

        return this.connection;
    }

    public void initDatabase() throws SQLException {
        Statement statement = getConnection().createStatement();

        String sql = "CREATE TABLE IF NOT EXISTS game_presets(id SMALLINT NOT NULL AUTO_INCREMENT, name VARCHAR(25), json_data LONGTEXT, PRIMARY KEY (id))";
        statement.execute(sql);
        statement.close();

        System.out.println("De game_presets table is aangemaakt in de database!");
    }

    public ArrayList<Preset> getPresets() throws SQLException {
        Statement statement = getConnection().createStatement();
        String sql = "SELECT * FROM game_presets";
        ResultSet results = statement.executeQuery(sql);

        ArrayList<Preset> presets = new ArrayList<>();

        while (results.next()) {
            short presetId = results.getShort("id");
            String presetName = results.getString("name");
            PresetData presetData = JsonConverter.deserializePreset(results.getString("json_data"));

            Preset preset = new Preset(presetName, presetData);

            presets.add(preset);
        }

        return presets;
    }

    public Preset getPreset(String name) throws SQLException, IOException {
        Statement statement = getConnection().createStatement();
        String sql = "SELECT * FROM game_presets WHERE name = '" + name + "'";
        ResultSet results = statement.executeQuery(sql);

        if (results.next()) {
            short presetId = results.getShort("id");
            String presetName = results.getString("name");
            PresetData presetData = JsonConverter.deserializePreset(results.getString("json_data"));

            Preset preset = new Preset(presetName, presetData);

            return preset;
        }

        return null;
    }

    public void savePreset(Preset preset) throws SQLException {
        Statement statement = getConnection().createStatement();
        String sql = "INSERT INTO game_presets(name, json_data) VALUES ('" + preset.name + "', '" + JsonConverter.serializePreset(preset.data) + "')";
        statement.execute(sql);
        statement.close();
    }
}
