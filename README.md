# Connect4 Bot
This Discord bot allows users to play Connect4 against each other online. This bot was written in Java 17 using IntelliJ IDEA and is designed to be used on small Discord servers.

## Setup
1. When registering the Discord bot, enable the guild member intent.
2. Add a file titled `config.properties` into the main folder.
3. Add the following to the file (replace `{Discord bot token}` with the bot's token)
```properties
token={Discord bot token}
```
3. If on Windows, run the following into a command prompt:
   1. `gradlew build`
   2. `move build\libs\connect4bot-1.0.0.jar .`
   3. `java -jar connect4bot.jar`

4. If on macOS/Linux, run the following into a terminal: 
   1. `chmod +x gradlew` 
   2. `./gradlew build`
   3. `mv build/libs/connect4bot-1.0.0.jar .`
   4. `java -jar connect4bot-1.0.0.jar`

## How to play
To play Connect4, type `/connect4 <user>` in any channel that the bot is allowed to read and send messages. The user who sent the command will have the first turn. Click on the buttons 1-7 to place a tile in the numbered columns.

## Dependencies
- Javacord 3.4.0 (https://github.com/Javacord/Javacord)
- JSONSimple 1.1.1 (https://github.com/fangyidong/json-simple)
