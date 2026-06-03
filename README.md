# Beat Saber Discord Bot

A Discord bot that assigns roles to server members based on their performance points (PP) from [BeatLeader](https://beatleader.xyz) and [ScoreSaber](https://scoresaber.com). Players link their accounts via slash commands and automatically receive a rank role matching their PP range.

## Features

- `/link` — fetch PP from BeatLeader using Discord account (must be linked on BeatLeader's side)
- `/link2 <id>` — fetch PP from ScoreSaber by player ID
- `/unlink` — remove all Beat Saber rank roles from the member

## Prerequisites

- Java 21+
- Maven 3.8+
- A Discord bot token ([Discord Developer Portal](https://discord.com/developers/applications))
- Your Discord server (guild) ID

## Setup

### 1. Clone the repo

```bash
git clone https://github.com/KirDemorgan/BeatSaberDiscordBot.git
cd BeatSaberDiscordBot
```

### 2. Configure environment

Copy the example file and fill in your values:

```bash
cp .env.example .env
```

```
DISCORD_TOKEN=your_bot_token_here
GUILD_ID=your_guild_id_here
```

Alternatively, copy `src/main/resources/config.properties.example` to `config.properties` and fill it in — the bot checks environment variables first, then falls back to the properties file.

### 3. Configure roles

Edit `src/main/resources/rolesBeatLeader.json` and `rolesScoreSaber.json`. Replace the placeholder values with actual Discord role IDs from your server:

```json
{
  "0/100":        "123456789012345678",
  "101/200":      "234567890123456789",
  "201/300":      "345678901234567890",
  "301/99999999": "456789012345678901"
}
```

Keys are PP ranges in `min/max` format (inclusive). Values are Discord role IDs (right-click a role → Copy ID).

### 4. Build

```bash
mvn package -q
```

The fat JAR will be at `target/beatsaber-bot-1.0.0.jar`.

### 5. Run

```bash
java -jar target/beatsaber-bot-1.0.0.jar
```

Logs are written to `logs/bot.log` with daily rotation.

## Running with Docker

```bash
docker compose up -d
```

The `.env` file is picked up automatically. Logs are mounted to `./logs` on the host.

To rebuild after code changes:

```bash
mvn package -q && docker compose up -d --build
```

## Bot permissions

The bot needs the following permissions on your server:

- **Manage Roles** — to assign and remove roles
- **Use Application Commands** — to register slash commands

Make sure the bot's role is ranked **above** the roles it assigns in the server's role hierarchy.

## Project structure

```
src/main/java/com/beatsaber/bot/
├── BeatSaberBot.java           # entry point
├── config/
│   └── BotConfig.java          # env / properties loading
├── discord/
│   ├── DiscordClient.java       # JDA setup and command registration
│   └── handler/
│       └── SlashCommandHandler.java  # slash command logic
├── platform/
│   ├── BeatLeaderClient.java   # BeatLeader API
│   └── ScoreSaberClient.java   # ScoreSaber API
└── role/
    ├── RoleMapper.java          # loads JSON, finds matching role
    └── RoleRange.java           # PP range record
```

## Contributing

PRs are welcome. Fork the repo, create a branch, make your changes, open a pull request.

## Author

[KirDemorgan](https://github.com/KirDemorgan)
