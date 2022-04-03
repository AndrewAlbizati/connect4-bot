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

        if (!Connect4CommandHandler.games.containsKey(interaction.getMessage().getId())) {
            return;
        }
        Game game = Connect4CommandHandler.games.get(interaction.getMessage().getId());

        if (game.getCurrentTurn().getId() != interaction.getUser().getId()) {
            return;
        }

        if (customId.equals("quit")) {
            interaction.getMessage().createUpdater()
                    .setEmbed(interaction.getMessage().getEmbeds().get(0).toBuilder()
                            .setDescription(game.toString())
                            .setColor(game.getCurrentTurn() == game.getPlayer1() ? Color.RED : Color.YELLOW)
                            .setFooter(game.getCurrentTurn() == game.getPlayer1() ? game.getPlayer1().getDiscriminatedName() : game.getPlayer2().getDiscriminatedName() + " has forfeited!")
                    ).removeAllComponents()
                    .applyChanges();
            interaction.acknowledge();
            return;
        }

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

        if (game.isFilled()) {
            interaction.getMessage().createUpdater()
                    .removeAllComponents()
                    .setEmbed(interaction.getMessage().getEmbeds().get(0).toBuilder()
                            .setDescription(game.toString())
                            .setColor(Color.BLACK)
                            .setFooter(game.getPlayer1().getDiscriminatedName() + " and " + game.getPlayer2().getDiscriminatedName() + " tie!")
                    ).applyChanges();
            interaction.acknowledge();
            return;
        }

        if (game.getWinner() != null) {
            interaction.getMessage().createUpdater()
                    .removeAllComponents()
                    .setEmbed(interaction.getMessage().getEmbeds().get(0).toBuilder()
                            .setDescription(game.toString())
                            .setColor(game.getWinner() == game.getPlayer1() ? Color.YELLOW : Color.RED)
                            .setFooter(game.getWinner() == game.getPlayer1() ? game.getPlayer1().getDiscriminatedName() : game.getPlayer2().getDiscriminatedName() + " wins!")
                    ).applyChanges();
            interaction.acknowledge();
            return;
        }

        interaction.getMessage().createUpdater()
                .setEmbed(interaction.getMessage().getEmbeds().get(0).toBuilder()
                        .setDescription(game.toString())
                        .setColor(game.getCurrentTurn() == game.getPlayer1() ? Color.YELLOW : Color.RED)
                        .setFooter(game.getPlayer1().getDiscriminatedName() + " vs " + game.getPlayer2().getDiscriminatedName())
                ).applyChanges();
        interaction.acknowledge();
    }
}
