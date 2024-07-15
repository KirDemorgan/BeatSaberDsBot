# BeatSaber Discord Bot

## Overview
This project is a Discord bot designed to integrate with the BeatSaber game, allowing users to fetch and display their BeatSaber statistics directly in Discord. The bot uses the ScoreSaber API to retrieve player data, including scores, ranks, and performance points (pp), and then assigns Discord roles based on the player's rank in the game.

## Features
- **Player Statistics**: Fetch and display individual player statistics from ScoreSaber.
- **Role Assignment**: Automatically assign roles in Discord based on the player's rank in BeatSaber.
- **Leaderboard**: Display global and country-specific leaderboards within Discord.

## Requirements
- Java 11 or higher
- Maven
- Discord Bot Token
- Access to ScoreSaber API

## Installation
1. Clone the repository:
   ```
   git clone https://github.com/KirDemorgan/BeatSaberDiscordBot.git
   ```
2. Navigate to the project directory:
   ```
   cd BeatSaberDiscordBot
   ```
3. Install dependencies using Maven:
   ```
   mvn install
   ```

## Usage
To start the bot, run:
```
java -jar target/BeatSaberDiscordBot-1.0-SNAPSHOT.jar
```
Make sure to set your Discord bot token and ScoreSaber API key in the `application.properties` file.

## Contributing
Contributions are welcome! Please follow the standard fork -> clone -> branch -> commit -> pull request workflow.



## Contact
KirDemorgan - [@KirDemorgan](https://github.com/KirDemorgan)

Project Link: [https://github.com/KirDemorgan/BeatSaberDiscordBot](https://github.com/KirDemorgan/BeatSaberDiscordBot)

