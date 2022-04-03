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

        // Get the player being challenged
        User player2 = interaction.getOptionUserValueByIndex(0).orElse(null);
        if (player2 == null || interaction.getUser().getId() == player2.getId() || player2.isBot()) {
            interaction.createImmediateResponder()
                    .setContent("Please provide a valid user to play against.")
                    .setFlags(InteractionCallbackDataFlag.EPHEMERAL)
                    .respond();
            return;
        }

        Game game = new Game(interaction.getUser(), player2);

        // Check if either of the players are already playing a game of Connect4
        for (Long id : games.keySet()) {
            if (games.get(id).getPlayer1().getId() == game.getPlayer1().getId() || games.get(id).getPlayer2().getId() == game.getPlayer1().getId()) {
                interaction.createImmediateResponder()
                        .setContent("Please finish your previous game before starting a new game.")
                        .setFlags(InteractionCallbackDataFlag.EPHEMERAL)
                        .respond();
                return;
            }
            if (games.get(id).getPlayer1().getId() == game.getPlayer2().getId() || games.get(id).getPlayer2().getId() == game.getPlayer2().getId()) {
                interaction.createImmediateResponder()
                        .setContent("The user you challenged needs to finish their previous game before starting a new game.")
                        .setFlags(InteractionCallbackDataFlag.EPHEMERAL)
                        .respond();
                return;
            }
        }

        // Create game message
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Connect4");
        eb.setDescription(game.toString());
        eb.setColor(Color.YELLOW);
        eb.setFooter(game.getPlayer1().getDiscriminatedName() + "'s turn");

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

        // Inform the challenged player of the game
        game.getPlayer2().sendMessage(game.getPlayer1().getMentionTag() + " has challenged you to a game of Connect4! Go to <#" + interaction.getChannel().get().getIdAsString() + "> to play.");

        games.put(message.getId(), game);
    }
}
