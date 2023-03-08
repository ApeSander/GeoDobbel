package me.apesander.geodobbel.database;

import me.apesander.geodobbel.models.Preset;
import me.apesander.geodobbel.models.PresetData;
import me.apesander.geodobbel.utils.JsonConverter;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

// This object handles the GeoDobbel database
public class GeoDobbelDB {

    private Connection connection;

    String url;

    public GeoDobbelDB(FileConfiguration config) {
        String name = config.getString("dbsettings.database");
        String ip = config.getString("dbsettings.ip");
        String type = config.getString("dbsettings.type");
        String port = config.getString("dbsettings.port");
        String user = config.getString("dbsettings.user");
        String pass = config.getString("dbsettings.pass");

        url = "jdbc:" + type + "://" + user + ":" + pass + "@" + ip + ":" + port + "/" + name;

        System.out.println(url);
    }

    public Connection getConnection() throws SQLException{
        if (connection != null) return connection;

        connection = DriverManager.getConnection(url, "u6_7Mx9hVtaY7", "ZaHmBD%5EoqBN%3D1GfUoB%5Em0A%40V");

        return connection;
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
