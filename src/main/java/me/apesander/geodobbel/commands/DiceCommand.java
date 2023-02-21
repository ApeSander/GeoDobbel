package me.apesander.geodobbel.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;

import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import me.apesander.geodobbel.data.ActiveGames;
import me.apesander.geodobbel.database.GeoDobbelDB;
import me.apesander.geodobbel.enums.ScoreOrder;
import me.apesander.geodobbel.enums.RollMode;
import me.apesander.geodobbel.enums.ScoreMode;

import me.apesander.geodobbel.enums.TurnMode;
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
    @Description("Krijg een overzicht met alle mogelijke commands.")
    @CommandPermission("geodobbel.commands.help")
    public void onHelp(Player player) {

    }

    @Subcommand("roll|gooi|rol|throw")
    @Description("Gooi de dobbelstenen!")
    @CommandPermission("geodobbel.commands.roll")
    public void onRoll(Player player) {

    }

    @Subcommand("user|gebruiker")
    @Description("Krijg speler info.")
    @CommandPermission("geodobbel.commands.user")
    public class UserCommand extends BaseCommand {
        @Subcommand("list|lijst")
        @Description("Krijg een lijst van alle spelers in de game.")
        @CommandPermission("geodobbel.commands.player.list")
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
        @Description("Bekijk de score van een specifieke speler.")
        @CommandPermission("geodobbel.commands.player.score")
        @Syntax("<speler>")
        public void onScore(Player player, OnlinePlayer target) {

        }

        @Subcommand("invite")
        @Description("Nodig een speler uit.")
        @CommandPermission("geodobbel.commands.player.invite")
        @Syntax("<speler>")
        public void onInvite(Player player, OnlinePlayer target) {

        }

        @Subcommand("kick")
        @Description("Stuur een speler uit het spel.")
        @CommandPermission("geodobbel.commands.player.kick")
        @Syntax("<speler>")
        public void onKick(Player player, OnlinePlayer target) {

        }

        @Subcommand("promote")
        @Description("Geef een gebruiker hogere rank binnen het spel.")
        @CommandPermission("geodobbel.commands.player.promote")
        @Syntax("<speler>")
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
        @Description("Geef een gebruiker lagere rang binnen het spel.")
        @CommandPermission("geodobbel.commands.player.demote")
        @Syntax("<speler>")
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
    @Description("Verander de status van het spel.")
    @CommandPermission("geodobbel.commands.game")
    public class GameCommand extends BaseCommand {
        @Subcommand("create|maak")
        @Description("Maak een dobbelspel aan.")
        @CommandPermission("geodobbel.commands.game.create")
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
        @Description("Begin het spel.")
        @CommandPermission("geodobbel.commands.game.start")
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
        @Description("Beëindig het spel.")
        @CommandPermission("geodobbel.commands.game.stop")
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
        @Description("Betreed een spel.")
        @CommandPermission("geodobbel.commands.game.join")
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
        @Description("Krijg de code van het spel waar je in zit.")
        @CommandPermission("geodobbel.commands.game.code")
        public void onCode(Player player) {
            Game game = ActiveGames.getByUser(player);

            if (game == null) {
                player.sendMessage("§cJe moet in een spel zitten om dit command uit te voeren!");
                return;
            }

            player.sendMessage("§3De code van het spel is §6" + game.code + "§3!");
        }

        @Subcommand("leave|verlaat")
        @Description("Verlaat het spel waar je in zit.")
        @CommandPermission("geodobbel.commands.game.leave")
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
    }

    @Subcommand("dice|dobbelstenen")
    @Description("Verander dobbelstenen in het spel.")
    @CommandPermission("geodobbel.commands.dice")
    public class SubDiceCommand extends BaseCommand {
        @Subcommand("add")
        @Description("Doe nieuwe dobbelstenen in het spel.")
        @CommandPermission("geodobbel.commands.dice.add")
        @Syntax("<names regex> [faces regex]")
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
        @Description("Verwijder dobbelstenen uit het spel.")
        @CommandPermission("geodobbel.commands.dice.remove")
        @Syntax("<names regex>")
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
    }

    @Subcommand("preset")
    @Description("Gebruik spel presets.")
    @CommandPermission("geodobbel.commands.preset")
    public class PresetCommand extends BaseCommand {
        @Subcommand("load|laad")
        @Description("Laad een preset in je spel.")
        @CommandPermission("geodobbel.commands.preset.load")
        @Syntax("<naam van preset>")
        public void onLoad(Player player, String presetName) {

        }

        @Subcommand("save")
        @Description("Sla een preset op in de database.")
        @CommandPermission("geodobbel.commands.preset.save")
        @Syntax("<naam van preset>")
        public void onSave(Player player, String presetName) {

        }

        @Subcommand("list|lijst")
        @Description("Bekijk alle presets van de database.")
        @CommandPermission("geodobbel.commands.preset.save")
        @Syntax("<naam van preset>")
        public void onList(Player player, @Default("1") Short page) {

        }
    }

    @Subcommand("settings|instellingen")
    @Description("Verander de instellingen van het spel.")
    @CommandPermission("geodobbel.commands.settings")
    public class SettingsCommand extends BaseCommand
    {
        @Subcommand("turnmode|turnmodus")
        @Description("Zet beurten aan of uit.")
        @CommandPermission("geodobbel.commands.settings.turns")
        public void onTurnMode(Player player, TurnMode turnMode) {
            Game game = ActiveGames.getByUser(player);

            if (game == null) {
                player.sendMessage("§cJe moet in een spel zitten om dit command uit te voeren!");
                return;
            }

            game.settings.turnMode = turnMode;
            player.sendMessage("§3Je hebt de turnmodus veranderd naar §6" + turnMode.toString() + " §3!");
        }

        @Subcommand("scores")
        @Description("Zet scores aan of uit.")
        @CommandPermission("geodobbel.commands.settings.scores")
        @Syntax("<true|false>")
        public void onScores(Player player, Sett) {

        }

        @Subcommand("rollmode|rolmodus")
        @Description("Verander het roltype.")
        @CommandPermission("geodobbel.commands.settings.rolltype")
        @Syntax("<normal|add|average|highest|lowest>")
        public void onRollMode(Player player, RollMode rollType) {

        }

        @Subcommand("scoremode|scoremodus")
        @Description("Verander het roltype.")
        @CommandPermission("geodobbel.commands.settings.rolltype")
        @Syntax("<add|average|highest|lowest>")
        public void onScoreMode(Player player, ScoreMode scoreType) {

        }

        @Subcommand("order|volgorde")
        @Description("Verander de scorevolgorde.")
        @CommandPermission("geodobbel.commands.settings.rolltype")
        @Syntax("<ascending|descending|none>")
        public void onOrder(Player player, ScoreOrder order) {

        }
    }

    @Subcommand("turn")
    @Description("Geef de beurt aan een andere speler.")
    @CommandPermission("geodobbel.commands.turn")
    public class TurnCommand extends BaseCommand {
        @Subcommand("skip|volgende")
        @Description("Geef de volgende speler de beurt")
        @CommandPermission("geodobbel.commands.turn.skip")
        public void onSkip(Player player) {

        }

        @Subcommand("back|terug")
        @Description("Geef de vorige speler opnieuw de beurt")
        @CommandPermission("geodobbel.commands.turn.back")
        public void onBack(Player player) {

        }

        @Subcommand("to|aan")
        @Description("Geef een specifieke speler de beurt")
        @CommandPermission("geodobbel.commands.turn.to")
        public void onTo(Player player, Player target) {

        }
    }
}
