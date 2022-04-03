package com.github.AndrewAlbizati;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.activity.ActivityType;
import org.javacord.api.entity.intent.Intent;
import org.javacord.api.entity.user.UserStatus;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;

import java.io.*;
import java.util.List;
import java.util.Properties;

public class Bot {
    public static void main(String[] args) {
        // Check if config.properties are present, creates new files if absent
        try {
            File config = new File("config.properties");
            if (config.createNewFile()) {
                FileWriter writer = new FileWriter("config.properties");
                writer.write("token=");
                writer.close();
                System.out.println("config.properties has been created");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Get token from config.properties
        String token;
        try {
            Properties prop = new Properties();
            FileInputStream ip = new FileInputStream("config.properties");
            prop.load(ip);
            ip.close();

            token = prop.getProperty("token");

            if (token.length() == 0)
                throw new NullPointerException("Please add a Discord bot token into config.properties");
        } catch (NullPointerException | IOException e) {
            e.printStackTrace();
            System.out.println("Token not found! " + e.getMessage());
            return;
        }

        // Start Discord bot
        DiscordApi api = new DiscordApiBuilder().setToken(token).setIntents(Intent.GUILD_MEMBERS).login().join();
        System.out.println("Logged in as " + api.getYourself().getDiscriminatedName());

        // Set bot status
        api.updateStatus(UserStatus.ONLINE);
        api.updateActivity(ActivityType.PLAYING, "Type /connect4 to start a game.");

        // Create slash command (may take a few minutes to update on Discord)
        SlashCommand.with("connect4", "Plays a game of Connect4 with another player",
                List.of(
                        SlashCommandOption.create(SlashCommandOptionType.USER, "USER", "The user you want to play with", true)
                )).createGlobal(api).join();

        // Create slash command listener for connect4
        api.addSlashCommandCreateListener(new Connect4CommandHandler());
        api.addMessageComponentCreateListener(new OnButtonPress());
    }
}
