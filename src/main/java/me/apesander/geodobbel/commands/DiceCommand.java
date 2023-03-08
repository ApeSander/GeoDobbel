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

import java.io.IOException;
import java.sql.SQLException;
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
    public void onRoll(Player player, @Default("2") Short amount, @Default("6") Short range) {
        Game game = ActiveGames.getByUser(player);

        if (game == null) {
            player.sendMessage("§cJe moet in een spel zitten om dit command uit te voeren!");
            return;
        }

        if (!game.containsPlayer(player)) {
            player.sendMessage("§cJe moet meedoen als speler om te dobbelen!");
            return;
        }

        if (game.state == GameState.START) {
            player.sendMessage("§cHet spel is nog niet begonnen!");
            return;
        }

        if (game.state == GameState.END) {
            player.sendMessage("§cHet spel is al afgelopen!");
            return;
        }

        if (!game.isTurnOf(player)) {
            player.sendMessage("§cHet is niet jouw beurt om te dobbelen!");
            return;
        }

        StringJoiner joiner = new StringJoiner(", ");

        if (game.settings.gameMode == GameMode.NORMAL) {
            Face[] result = game.rollAllDice(player).getFaces();
            for (Face face : result) {
                joiner.add(face.dieName + ": " + face.name);
            }
        } else if (game.settings.gameMode == GameMode.DICESELECT) {
            Face[] result = game.rollDice(player, "*").getFaces();
            for (Face face : result) {
                joiner.add(face.dieName + ": " + face.name);
            }
        } else if (game.settings.gameMode == GameMode.FREEROLL) {
            Face[] result = game.freeRoll(player, range, amount).getFaces();
            for (Face face : result) {
                joiner.add(face.dieName + ": " + face.name);
            }
        }

        game.showMessage("§3" + player.getName() + " heeft de dobbelstenen gegooid:");
        game.showMessage("§6" + joiner);

        if (game.settings.turnMode == TurnMode.OFF) game.showMessage("§3Een andere speler kan dobbelen!");
        else if (game.settings.turnMode == TurnMode.MANUAL) game.showMessage("§3De volgende beurt wordt bepaald door een beheerder!");
        else game.showMessage("§6" + game.getCurrentPlayer().getPlayer().getName() + " §3is aan de beurt!");
    }

    @Subcommand("roll|gooi|rol|throw")
    public void onRoll(Player player, @Default("*") String namePattern) {
        Game game = ActiveGames.getByUser(player);

        if (game == null) {
            player.sendMessage("§cJe moet in een spel zitten om dit command uit te voeren!");
            return;
        }

        if (!game.containsPlayer(player)) {
            player.sendMessage("§cJe moet meedoen als speler om te dobbelen!");
            return;
        }

        if (game.state == GameState.START) {
            player.sendMessage("§cHet spel is nog niet begonnen!");
            return;
        }

        if (game.state == GameState.END) {
            player.sendMessage("§cHet spel is al afgelopen!");
            return;
        }

        if (!game.isTurnOf(player)) {
            player.sendMessage("§cHet is niet jouw beurt om te dobbelen!");
            return;
        }

        StringJoiner joiner = new StringJoiner(", ");

        if (game.settings.gameMode == GameMode.NORMAL) {
            Face[] result = game.rollAllDice(player).getFaces();
            for (Face face : result) {
                joiner.add(face.dieName + ": " + face.name);
            }
        } else if (game.settings.gameMode == GameMode.DICESELECT) {
            Face[] result = game.rollDice(player, namePattern).getFaces();
            for (Face face : result) {
                joiner.add(face.dieName + ": " + face.name);
            }
        } else if (game.settings.gameMode == GameMode.FREEROLL) {
             player.sendMessage("§cDe spelmodus staat op freeroll. Geef een aantal en een bereik om te dobbelen!");
             return;
        }

        game.showMessage("§3" + player.getName() + " heeft de dobbelstenen gegooid:");
        game.showMessage("§6" + joiner);

        if (game.settings.turnMode == TurnMode.OFF) game.showMessage("§3Een andere speler kan dobbelen!");
        else if (game.settings.turnMode == TurnMode.MANUAL) game.showMessage("§3De volgende beurt wordt bepaald door een beheerder!");
        else game.showMessage("§6" + game.getCurrentPlayer().getPlayer().getName() + " §3is aan de beurt!");
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
            Game game = ActiveGames.getByUser(player);

            if (game == null) {
                player.sendMessage("§cJe moet in een spel zitten om dit command uit te voeren!");
                return;
            }

            if (!game.containsUser(target.getPlayer())) {
                player.sendMessage("§cDe opgegeven speler zit niet in het dobbelspel!");
                return;
            }

            if (target.getPlayer() == player) {
                player.sendMessage("§cJe kan jezelf niet kicken!");
                return;
            }

            float score = game.getPlayer(target.getPlayer()).getScore();

            player.sendMessage("§3De score van §6" + target.getPlayer().getName() + " §3is §6" + score + "§3!");
        }

        @Subcommand("kick")
        public void onKick(Player player, OnlinePlayer target) {
            Game game = ActiveGames.getByUser(player);

            if (game == null) {
                player.sendMessage("§cJe moet in een spel zitten om dit command uit te voeren!");
                return;
            }

            if (!game.containsAdmin(player)) {
                player.sendMessage("§cJe moet beheerder van het spel zijn om dit command uit te voeren!");
                return;
            }

            if (!game.containsUser(target.getPlayer())) {
                player.sendMessage("§cDe opgegeven speler zit niet in het dobbelspel!");
                return;
            }

            game.remUser(player);
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

            if (!game.containsAdmin(player)) {
                player.sendMessage("§cJe moet beheerder van het spel zijn om dit command uit te voeren!");
                return;
            }

            game.toggleAdminPlaying(player);
            if (game.containsPlayer(player)) player.sendMessage("§3Je doet nu mee met het spel!");
            else player.sendMessage("§3Je doet niet meer mee met het spel maar bent nog steeds beheerder.");
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
            Game game = ActiveGames.getByUser(player);

            if (game == null) {
                player.sendMessage("§cJe moet in een spel zitten om dit command uit te voeren!");
                return;
            }

            if (!game.containsOperator(player)) {
                player.sendMessage("§cJe moet operator van het spel zijn om dit command uit te voeren!");
                return;
            }

            if (game.state != GameState.START) {
                player.sendMessage("§cJe kan dit command niet uitvoeren want het spel is al begonnen!");
                return;
            }


            Preset preset;

            try {
                preset = db.getPreset(presetName);
            } catch (SQLException | IOException e) {
                player.sendMessage("§cEr ging iets fout met de database! Geef dit door aan een stafflid.");
                return;
            }

            if (preset == null) {
                player.sendMessage("§cEr bestaat geen preset met deze naam! Doe /preset list om een lijst met presets te bekijken!");
                return;
            }

            game.settings = preset.data.settings;
            game.setDice(preset.data.dice);
            player.sendMessage("§3Je hebt de §6" + preset.name + " §3preset ingeladen!");
        }

        @Subcommand("save")
        public void onSave(Player player, String presetName) {
            Game game = ActiveGames.getByUser(player);

            if (game == null) {
                player.sendMessage("§cJe moet in een spel zitten om dit command uit te voeren!");
                return;
            }

            if (!game.containsOperator(player)) {
                player.sendMessage("§cJe moet operator van het spel zijn om dit command uit te voeren!");
                return;
            }

            try {
                if (db.getPreset(presetName) != null) {
                    player.sendMessage("§cEr bestaat al een preset met deze naam! Probeer een andere naam.");
                    return;
                }

                db.savePreset(new Preset(presetName, new PresetData(game.getDice(), game.settings)));
                player.sendMessage("§3Je hebt een preset van deze game aangemaakt met de naam §6" + presetName + "§3!");
            } catch (SQLException | IOException e) {
                player.sendMessage("§cEr ging iets fout met de database! Geef dit door aan een stafflid.");
            }
        }

        @Subcommand("list|lijst")
        public void onList(Player player, @Default("1") Short page) {
            Preset[] presets = new Preset[0];

            try {
                presets = db.getPresets().toArray(new Preset[0]);
            } catch (SQLException e) {
                player.sendMessage("§cEr ging iets fout met de database! Geef dit door aan een stafflid.");
            }

            StringJoiner joiner = new StringJoiner(", ");

            for (int i = page*10-10; i < page*10; i++) {
                try {
                    joiner.add(presets[i].name);
                } catch (IndexOutOfBoundsException e) {
                    break;
                }
            }

            player.sendMessage("§3De volgende presets zijn beschikbaar:");
            player.sendMessage("§6" + joiner);
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
            
            if (game.state != GameState.START) {
                player.sendMessage("§cJe kan dit command niet uitvoeren want het spel is al begonnen!");
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

            if (game.state != GameState.START) {
                player.sendMessage("§cJe kan dit command niet uitvoeren want het spel is al begonnen!");
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

            if (game.state != GameState.START) {
                player.sendMessage("§cJe kan dit command niet uitvoeren want het spel is al begonnen!");
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

            if (game.state != GameState.START) {
                player.sendMessage("§cJe kan dit command niet uitvoeren want het spel is al begonnen!");
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

            if (game.state != GameState.START) {
                player.sendMessage("§cJe kan dit command niet uitvoeren want het spel is al begonnen!");
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
            Game game = ActiveGames.getByUser(player);

            if (game == null) {
                player.sendMessage("§cJe moet in een spel zitten om dit command uit te voeren!");
                return;
            }

            if (!game.containsAdmin(player)) {
                player.sendMessage("§cJe moet beheerder zijn om dit command uit te voeren!");
                return;
            }

            if (game.state != GameState.PLAY) {
                player.sendMessage("§cJe kan dit command niet uitvoeren, omdat het spel niet bezig is!");
                return;
            }

            game.nextTurn();
            game.showMessage("§3De beurt is overgeslagen! §6" + game.getCurrentPlayer().getPlayer().getName() + "§3 is nu aan de beurt!");
        }

        @Subcommand("back|terug")
        public void onBack(Player player) {
            Game game = ActiveGames.getByUser(player);

            if (game == null) {
                player.sendMessage("§cJe moet in een spel zitten om dit command uit te voeren!");
                return;
            }

            if (!game.containsAdmin(player)) {
                player.sendMessage("§cJe moet beheerder zijn om dit command uit te voeren!");
                return;
            }

            if (game.state != GameState.PLAY) {
                player.sendMessage("§cJe kan dit command niet uitvoeren, omdat het spel niet bezig is!");
                return;
            }

            game.pastTurn();
            game.showMessage("§3De beurt is teruggegaan! §6" + game.getCurrentPlayer().getPlayer().getName() + "§3 is weer aan de beurt!");
        }

        @Subcommand("to|aan")
        public void onTo(Player player, OnlinePlayer target) {
            Game game = ActiveGames.getByUser(player);

            if (game == null) {
                player.sendMessage("§cJe moet in een spel zitten om dit command uit te voeren!");
                return;
            }

            if (!game.containsAdmin(player)) {
                player.sendMessage("§cJe moet beheerder zijn om dit command uit te voeren!");
                return;
            }

            if (!game.containsUser(target.getPlayer())) {
                player.sendMessage("§cDe opgegeven speler zit niet in het dobbelspel!");
                return;
            }

            if (game.state != GameState.PLAY) {
                player.sendMessage("§cJe kan dit command niet uitvoeren, omdat het spel niet bezig is!");
                return;
            }

            game.setTurn(target.getPlayer());
            game.showMessage("§3De beurt is gegeven aan §6" + target.getPlayer().getName() + "§3!");
        }
    }
}