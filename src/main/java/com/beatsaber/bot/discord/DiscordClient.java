package com.beatsaber.bot.discord;

import com.beatsaber.bot.config.BotConfig;
import com.beatsaber.bot.discord.handler.SlashCommandHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiscordClient {

    private static final Logger log = LoggerFactory.getLogger(DiscordClient.class);

    private final BotConfig config;

    public DiscordClient(BotConfig config) {
        this.config = config;
    }

    public void start() throws Exception {
        JDA jda = JDABuilder.createDefault(config.getToken())
            .enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT)
            .setStatus(OnlineStatus.DO_NOT_DISTURB)
            .setActivity(Activity.watching("статистику по Beat Saber"))
            .addEventListeners(new SlashCommandHandler())
            .build();

        jda.awaitReady();
        registerCommands(jda);
        log.info("Bot is ready");
    }

    private void registerCommands(JDA jda) {
        Guild guild = jda.getGuildById(config.getGuildId());
        if (guild == null) {
            log.warn("Guild {} not found — skipping command registration", config.getGuildId());
            return;
        }

        guild.updateCommands().addCommands(
            Commands.slash("link", "Привязать аккаунт BeatLeader по Discord ID"),
            Commands.slash("link2", "Привязать аккаунт ScoreSaber по ID")
                .addOption(OptionType.STRING, "id", "ID пользователя ScoreSaber", true),
            Commands.slash("unlink", "Убрать все роли Beat Saber")
        ).queue(
            cmds -> log.info("Registered {} slash commands in guild {}", cmds.size(), guild.getName()),
            err -> log.error("Failed to register slash commands", err)
        );
    }
}
