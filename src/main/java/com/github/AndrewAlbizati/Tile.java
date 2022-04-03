package com.github.AndrewAlbizati;

public enum Tile {
    RED(":red_circle:"),
    YELLOW(":yellow_circle:"),
    EMPTY(":black_circle:");

    public final String emoji;

    Tile(String emoji) {
        this.emoji = emoji;
    }
}
