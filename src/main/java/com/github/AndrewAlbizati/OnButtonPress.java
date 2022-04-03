package com.github.AndrewAlbizati;

import org.javacord.api.event.interaction.MessageComponentCreateEvent;
import org.javacord.api.interaction.MessageComponentInteraction;
import org.javacord.api.listener.interaction.MessageComponentCreateListener;

import java.awt.*;

public class OnButtonPress implements MessageComponentCreateListener {
    @Override
    public void onComponentCreate(MessageComponentCreateEvent messageComponentCreateEvent) {
        MessageComponentInteraction interaction = messageComponentCreateEvent.getMessageComponentInteraction();
        String customId = interaction.getCustomId();

        // Button click wasn't for a Connect4 game
        if (!Connect4CommandHandler.games.containsKey(interaction.getMessage().getId())) {
            return;
        }

        Game game = Connect4CommandHandler.games.get(interaction.getMessage().getId());

        // The other player clicks a button
        if (game.getCurrentTurn().getId() != interaction.getUser().getId()) {
            return;
        }

        // Player forfeits
        if (customId.equals("quit")) {
            interaction.getMessage().createUpdater()
                    .setEmbed(interaction.getMessage().getEmbeds().get(0).toBuilder()
                            .setDescription(game.toString())
                            .setColor(game.getCurrentTurn() == game.getPlayer1() ? Color.RED : Color.YELLOW)
                            .setFooter(game.getCurrentTurn() == game.getPlayer1() ? game.getPlayer1().getDiscriminatedName() : game.getPlayer2().getDiscriminatedName() + " has forfeited!")
                    ).removeAllComponents()
                    .applyChanges();
            interaction.acknowledge();
            Connect4CommandHandler.games.remove(interaction.getMessage().getId());
            return;
        }

        // Place the tile and switch turns
        game.placeTile(switch(customId) {
            case "one" -> 0;
            case "two" -> 1;
            case "three" -> 2;
            case "four" -> 3;
            case "five" -> 4;
            case "six" -> 5;
            case "seven" -> 6;
            default -> throw new IllegalStateException("Unexpected value: " + customId);
        });

        // Game board has no more available spaces
        if (game.isFilled()) {
            interaction.getMessage().createUpdater()
                    .removeAllComponents()
                    .setEmbed(interaction.getMessage().getEmbeds().get(0).toBuilder()
                            .setDescription(game.toString())
                            .setColor(Color.BLACK)
                            .setFooter(game.getPlayer1().getDiscriminatedName() + " and " + game.getPlayer2().getDiscriminatedName() + " tie!")
                    ).applyChanges();
            interaction.acknowledge();
            Connect4CommandHandler.games.remove(interaction.getMessage().getId());
            return;
        }

        // Player 1 or 2 has won
        if (game.getWinner() != null) {
            interaction.getMessage().createUpdater()
                    .removeAllComponents()
                    .setEmbed(interaction.getMessage().getEmbeds().get(0).toBuilder()
                            .setDescription(game.toString())
                            .setColor(game.getWinner() == game.getPlayer1() ? Color.YELLOW : Color.RED)
                            .setFooter((game.getWinner() == game.getPlayer1() ? game.getPlayer1().getDiscriminatedName() : game.getPlayer2().getDiscriminatedName()) + " wins!")
                    ).applyChanges();
            interaction.acknowledge();
            Connect4CommandHandler.games.remove(interaction.getMessage().getId());
            return;
        }

        // Update message and wait for next turn
        interaction.getMessage().createUpdater()
                .setEmbed(interaction.getMessage().getEmbeds().get(0).toBuilder()
                        .setDescription(game.toString())
                        .setColor(game.getCurrentTurn() == game.getPlayer1() ? Color.YELLOW : Color.RED)
                        .setFooter((game.getCurrentTurn() == game.getPlayer1() ? game.getPlayer1().getDiscriminatedName() : game.getPlayer2().getDiscriminatedName()) +  "'s turn")
                ).applyChanges();
        interaction.acknowledge();
    }
}
