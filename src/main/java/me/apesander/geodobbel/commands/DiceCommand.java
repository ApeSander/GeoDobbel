package me.apesander.geodobbel.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;

import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import me.apesander.geodobbel.data.ActiveGames;
import me.apesander.geodobbel.database.GeoDobbelDB;
import me.apesander.geodobbel.enums.*;

import me.apesander.geodobbel.models.*;
import me.apesander.geodobbel.utils.CodeGenerator;
import org.bukkit.entity.Player;

import java.util.StringJoiner;

@CommandAlias("dice|geodice|gd|dobbel|geodobbel")
@CommandPermission("geodobbel.commands")
public class DiceCommand extends BaseCommand {

    private GeoDobbelDB db;

    public DiceCommand(GeoDobbelDB db) {
        this.db = db;
    }

    @HelpCommand
    public void onHelp(Player player) {

    }

    @Subcommand("roll|gooi|rol|throw")
    public void onRoll(Player player) {

    }

    @Subcommand("user|gebruiker")
    public class UserCommand extends BaseCommand {
        @Subcommand("list|lijst")
        public void onList(Player player) { // DONE
            Game game = ActiveGames.getByUser(player);

            if (game == null) {
                player.sendMessage("§cJe moet in een spel zitten om dit command uit te voeren!");
                return;
            }

            StringJoiner joiner = new StringJoiner(", ");

            DiceAdmin[] admins = game.getAdmins();
            DicePlayer[] players = game.getPlayers();
            DiceSpectator[] spectators = game.getSpectators();

            for (DiceAdmin admin : admins) {
                joiner.add(admin.getPlayer().getDisplayName());
            }

            String adminStr = "§6" + joiner;
            joiner = new StringJoiner(", ");

            for (DicePlayer dplayer : players) {
                joiner.add(dplayer.getPlayer().getDisplayName());
            }

            String playerStr = "§6" + joiner;
            joiner = new StringJoiner(", ");

            for (DiceSpectator spectator : spectators) {
                joiner.add(spectator.getPlayer().getDisplayName());
            }

            String spectatorStr = "§6" + joiner;

            player.sendMessage("§3Beheerders:");
            player.sendMessage(adminStr);
            player.sendMessage("§3Spelers:");
            player.sendMessage(playerStr);
            player.sendMessage("§3Toeschouwers:");
            player.sendMessage(spectatorStr);
        }

        @Subcommand("score")
        public void onScore(Player player, OnlinePlayer target) {

        }

        @Subcommand("invite")
        public void onInvite(Player player, OnlinePlayer target) {

        }

        @Subcommand("kick")
        public void onKick(Player player, OnlinePlayer target) {

        }

        @Subcommand("promote")
        public void onPromote(Player player, OnlinePlayer target, Boolean on) {
            Game game = ActiveGames.getByUser(player);

            if (game == null) {
                player.sendMessage("§cJe moet in een spel zitten om dit command uit te voeren!");
                return;
            }

            if (!game.containsOperator(player)) {
                player.sendMessage("§cJe moet operator van het spel zijn om dit command uit te voeren!");
                return;
            }

            if (player == target.getPlayer()) {
                player.sendMessage("§cJe kan jezelf niet promoveren!");
            }

            game.promoteUser(target.getPlayer());
            target.getPlayer().sendMessage("§3Je hebt een hogere rang in het spel.");
            player.sendMessage("§3Je hebt §6" + target.getPlayer().getName() + "§3 gepromoveerd!");
        }

        @Subcommand("demote")
        public void onDemote(Player player, OnlinePlayer target, Boolean on) {
            Game game = ActiveGames.getByUser(player);

            if (game == null) {
                player.sendMessage("§cJe moet in een spel zitten om dit command uit te voeren!");
                return;
            }

            if (!game.containsOperator(player)) {
                player.sendMessage("§cJe moet operator van het spel zijn om dit command uit te voeren!");
                return;
            }

            if (player == target.getPlayer()) {
                player.sendMessage("§cJe kan jezelf niet degraderen!");
            }

            game.demoteUser(target.getPlayer());
            target.getPlayer().sendMessage("§3Je hebt een lagere rang in het spel.");
            player.sendMessage("§3Je hebt §6" + target.getPlayer().getName() + "§3 gedegradeerd!");
        }
    }

    @Subcommand("game|spel")
    public class GameCommand extends BaseCommand {
        @Subcommand("create|maak")
        public void onCreate(Player player) {
            Game game = ActiveGames.getByUser(player);

            if (game != null) {
                player.sendMessage("§cJe kan dit command niet uitvoeren, omdat je in een spel zit!");
                return;
            }

            short code = CodeGenerator.generate();

            ActiveGames.add(code, player);
            player.sendMessage("§3Je hebt een game aangemaakt met de code §6" + code + "§3!");
        }

        @Subcommand("start|begin")
        public void onStart(Player player) {
            Game game = ActiveGames.getByUser(player);

            if (game == null) {
                player.sendMessage("§cJe moet in een spel zitten om dit command uit te voeren!");
                return;
            }

            if (!game.containsOperator(player)) {
                player.sendMessage("§cJe moet operator van het spel zijn om dit command uit te voeren!");
                return;
            }

            game.start();
            game.showMessage("§3Het spel is §6gestart§3!");
        }

        @Subcommand("stop|end")
        public void onStop(Player player) {
            Game game = ActiveGames.getByUser(player);

            if (game == null) {
                player.sendMessage("§cJe moet in een spel zitten om dit command uit te voeren!");
                return;
            }

            if (!game.containsOperator(player)) {
                player.sendMessage("§cJe moet operator van het spel zijn om dit command uit te voeren!");
                return;
            }

            game.stop();
            game.showMessage("§3Het spel is §6beëindigd§3!");
        }

        @Subcommand("join|betreed")
        public void onJoin(Player player, Short code) {
            Game game = ActiveGames.getByUser(player);

            if (game != null) {
                player.sendMessage("§cJe kan dit command niet uitvoeren, omdat je in een spel zit!");
                return;
            }

            Game gameToJoin = ActiveGames.getByCode(code);

            if (gameToJoin == null) {
                player.sendMessage("§cEr is geen game gevonden met de opgegeven code!");
                return;
            }

            game.showMessage("§6" + player.getName() + " §3heeft het spel betreden!");
            gameToJoin.addUser(player);
            player.sendMessage("§3Je hebt het spel betreden!");
        }

        @Subcommand("code|id")
        public void onCode(Player player) {
            Game game = ActiveGames.getByUser(player);

            if (game == null) {
                player.sendMessage("§cJe moet in een spel zitten om dit command uit te voeren!");
                return;
            }

            player.sendMessage("§3De code van het spel is §6" + game.code + "§3!");
        }

        @Subcommand("leave|verlaat")
        public void onLeave(Player player) {
            Game game = ActiveGames.getByUser(player);

            if (game == null) {
                player.sendMessage("§cJe moet in een spel zitten om dit command uit te voeren!");
                return;
            }

            game.remUser(player);
            game.showMessage("§6" + player.getName() + "§3 is weggegaan!");
            player.sendMessage("§3Je hebt het spel verlaten!");
        }

        @Subcommand("adminplay|speelbeheerder")
        public void onPlay(Player player) {
            Game game = ActiveGames.getByUser(player);

            if (game == null) {
                player.sendMessage("§cJe moet in een spel zitten om dit command uit te voeren!");
                return;
            }

            if (game.containsAdmin(player)) {
                player.sendMessage("§cJe moet beheerder van het spel zijn om dit command uit te voeren!");
                return;
            }

            game.toggleAdminPlaying(player);
            if (game.containsPlayer(player)) player.sendMessage("§3Je doet nu mee met het spel!");
            else player.sendMessage("§3Je doet niet meer mee met het spel maar bent nog steed beheerder.");
        }
    }

    @Subcommand("dice|dobbelstenen")
    public class SubDiceCommand extends BaseCommand {
        @Subcommand("add|toevoegen")
        public void onAdd(Player player, @Default("dobbelsteen**") String namePattern, @Default("1(een),2(twee),3(drie),4(vier),5(vijf),6(zes)") String facePattern) {
            Game game = ActiveGames.getByUser(player);

            if (game == null) {
                player.sendMessage("§cJe moet in een spel zitten om dit command uit te voeren!");
                return;
            }

            if (!game.containsAdmin(player)) {
                player.sendMessage("§cJe moet beheerder van het spel zijn om dit command uit te voeren!");
                return;
            }

            try {
                game.addDice(namePattern, facePattern);
                player.sendMessage("§3Je hebt de nieuwe dobbelstenen toegevoegd!");
            } catch (NumberFormatException e) {
                player.sendMessage("§cHet opgegeven patroon is niet geldig. /dice help PATROON");
            }
        }

        @Subcommand("remove|rem|verwijder")
        public void onRemove(Player player, String namePattern) {
            Game game = ActiveGames.getByUser(player);

            if (game == null) {
                player.sendMessage("§cJe moet in een spel zitten om dit command uit te voeren!");
                return;
            }

            if (!game.containsAdmin(player)) {
                player.sendMessage("§cJe moet beheerder van het spel zijn om dit command uit te voeren!");
                return;
            }

            game.remDice(namePattern);
            player.sendMessage("§3Je hebt de dobbelstenen uit het spel verwijderd.");
        }

        @Subcommand("list|lijst")
        public void onList(Player player) {
            Game game = ActiveGames.getByUser(player);

            if (game == null) {
                player.sendMessage("§cJe moet in een spel zitten om dit command uit te voeren!");
                return;
            }

            StringJoiner joiner = new StringJoiner(", ");

            for (String diceName : game.diceList()) {
                joiner.add(diceName);
            }

            player.sendMessage("§3De volgende dobbelstenen zitten in het spel:");
            player.sendMessage("§6" + joiner);
        }
    }

    @Subcommand("preset")
    @Description("Gebruik spel presets.")
    @CommandPermission("geodobbel.commands.preset")
    public class PresetCommand extends BaseCommand {
        @Subcommand("load|laad")
        public void onLoad(Player player, String presetName) {

        }

        @Subcommand("save")
        public void onSave(Player player, String presetName) {

        }

        @Subcommand("list|lijst")
        public void onList(Player player, @Default("1") Short page) {

        }
    }

    @Subcommand("settings|instellingen")
    public class SettingsCommand extends BaseCommand
    {
        @Subcommand("turnmode|turnmodus")
        public void onTurnMode(Player player, TurnMode turnMode) {
            Game game = ActiveGames.getByUser(player);

            if (game == null) {
                player.sendMessage("§cJe moet in een spel zitten om dit command uit te voeren!");
                return;
            }

            if (!game.containsAdmin(player)) {
                player.sendMessage("§cJe moet beheerder zijn om dit command uit te voeren!");
                return;
            }

            game.settings.turnMode = turnMode;
            player.sendMessage("§3Je hebt de beurtenmodus veranderd naar §6" + turnMode.toString() + " §3!");
        }

        @Subcommand("rollmode|rolmodus")
        public void onRollMode(Player player, RollMode rollMode) {
            Game game = ActiveGames.getByUser(player);

            if (game == null) {
                player.sendMessage("§cJe moet in een spel zitten om dit command uit te voeren!");
                return;
            }

            if (!game.containsAdmin(player)) {
                player.sendMessage("§cJe moet beheerder zijn om dit command uit te voeren!");
                return;
            }

            game.settings.rollMode = rollMode;
            player.sendMessage("§3Je hebt de rolmodus veranderd naar §6" + rollMode.toString() + " §3!");
        }

        @Subcommand("scoremode|scoremodus")
        public void onScoreMode(Player player, ScoreMode scoreMode) {
            Game game = ActiveGames.getByUser(player);

            if (game == null) {
                player.sendMessage("§cJe moet in een spel zitten om dit command uit te voeren!");
                return;
            }

            if (!game.containsAdmin(player)) {
                player.sendMessage("§cJe moet beheerder zijn om dit command uit te voeren!");
                return;
            }

            game.settings.scoreMode = scoreMode;
            player.sendMessage("§3Je hebt de scoremodus veranderd naar §6" + scoreMode.toString() + " §3!");
        }

        @Subcommand("order|volgorde")
        public void onOrder(Player player, ScoreOrder order) {
            Game game = ActiveGames.getByUser(player);

            if (game == null) {
                player.sendMessage("§cJe moet in een spel zitten om dit command uit te voeren!");
                return;
            }

            if (!game.containsAdmin(player)) {
                player.sendMessage("§cJe moet beheerder zijn om dit command uit te voeren!");
                return;
            }

            game.settings.order = order;
            player.sendMessage("§3Je hebt de volgorde veranderd naar §6" + order.toString() + " §3!");
        }

        @Subcommand("gamemode|spelmodus")
        public void onGameMode(Player player, GameMode gameMode) {
            Game game = ActiveGames.getByUser(player);

            if (game == null) {
                player.sendMessage("§cJe moet in een spel zitten om dit command uit te voeren!");
                return;
            }

            if (!game.containsAdmin(player)) {
                player.sendMessage("§cJe moet beheerder zijn om dit command uit te voeren!");
                return;
            }

            game.settings.gameMode = gameMode;
            player.sendMessage("§3Je hebt de spelmodus veranderd naar §6" + gameMode.toString() + " §3!");
        }
    }

    @Subcommand("turn")
    public class TurnCommand extends BaseCommand {
        @Subcommand("skip|volgende")
        public void onSkip(Player player) {

        }

        @Subcommand("back|terug")
        public void onBack(Player player) {

        }

        @Subcommand("to|aan")
        public void onTo(Player player, Player target) {

        }
    }
}
