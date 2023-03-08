package me.apesander.geodobbel;

import co.aikar.commands.BukkitCommandManager;

import me.apesander.geodobbel.commands.DiceCommand;

import me.apesander.geodobbel.database.GeoDobbelDB;
import me.apesander.geodobbel.events.DiceEvent;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public final class GeoDobbel extends JavaPlugin {

    /* TODO:
        - help command
        - debuggen
    */
    BukkitCommandManager manager;
    GeoDobbelDB db;

    FileConfiguration config;

    @Override
    public void onEnable() {
        registerConfig();
        registerDatabase();
        registerEvents();
        registerCommands();
    }

    private void registerConfig() {
        saveDefaultConfig();
        config = getConfig();
    }

    private void registerDatabase() {
        db = new GeoDobbelDB(config);

        try {
            db.initDatabase();
            System.out.println("Het opzetten van de database is succesvol verlopen!");
        } catch (SQLException e) {
            System.err.println("Er was een error met het opzetten van de GeoDobbel database!");
            e.printStackTrace();
        }
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new DiceEvent(), this);
    }

    private void registerCommands() {
        manager = new BukkitCommandManager(this);
        manager.registerCommand(new DiceCommand(db));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
