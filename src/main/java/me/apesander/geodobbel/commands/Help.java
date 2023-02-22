package me.apesander.geodobbel.commands;

import me.apesander.geodobbel.enums.HelpCategory;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Help {
    public String[] getHelp(Player player, HelpCategory helpCategory) {
        ArrayList<String> messages = new ArrayList<>();

        switch (helpCategory) {
            case ALL:
                messages.add("§3Je kan de volgende commands uitvoeren om meer hulp te krijgen: " );
                messages.add("§3Doe §6/dice help DICE §3om hulp te krijgen met dobbelstenen toevoegen." );
                messages.add("§3Doe §6/dice help COMMANDS §3om een overzicht van alle commands te krijgen." );
                messages.add("§3Doe §6/dice help GAME_SETTINGS §3om te kijken welke spelinstellingen beschikbaar zijn." );
                messages.add("§3Doe §6/dice help GETTING_STARTED §3om hulp te krijgen als dit je eerste keer is dat je de plugin gebruikt." );
                break;
            case DICE:
                break;
            case COMMANDS:
                break;
            case GAME_SETTINGS:
                break;
            case GETTING_STARTED:
                break;
        }

        return messages.toArray(new String[0]);
    }
}
