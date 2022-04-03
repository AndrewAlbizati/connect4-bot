package com.github.AndrewAlbizati;

import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.Button;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.callback.InteractionCallbackDataFlag;
import org.javacord.api.listener.interaction.SlashCommandCreateListener;

import java.awt.*;
import java.util.HashMap;

public class Connect4CommandHandler implements SlashCommandCreateListener {
    public static final HashMap<Long, Game> games = new HashMap<>();

    @Override
    public void onSlashCommandCreate(SlashCommandCreateEvent event) {
        SlashCommandInteraction interaction = event.getSlashCommandInteraction();

        // Ignore other slash commands
        if (!interaction.getCommandName().equalsIgnoreCase("connect4")) {
            return;
        }

        User player2 = interaction.getOptionUserValueByIndex(0).orElse(null);
        if (player2 == null || interaction.getUser().getId() == player2.getId() || player2.isBot()) {
            interaction.createImmediateResponder()
                    .setContent("Please provide a valid user to play against.")
                    .setFlags(InteractionCallbackDataFlag.EPHEMERAL)
                    .respond();
            return;
        }

        Game game = new Game(interaction.getUser(), player2);

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Connect4");
        eb.setDescription(game.toString());
        eb.setColor(Color.YELLOW);
        eb.setFooter(game.getPlayer1().getDiscriminatedName() + " vs " + game.getPlayer2().getDiscriminatedName());

        Message message = interaction.createImmediateResponder()
                .addEmbed(eb)
                .addComponents(
                        ActionRow.of(
                                Button.primary("one", "1"),
                                Button.primary("two", "2"),
                                Button.primary("three", "3"),
                                Button.primary("four", "4")
                        ),
                        ActionRow.of(
                                Button.primary("five", "5"),
                                Button.primary("six", "6"),
                                Button.primary("seven", "7"),
                                Button.danger("quit", "Quit")
                        )
                )
                .respond().join().update().join();

        games.put(message.getId(), game);
    }
}
