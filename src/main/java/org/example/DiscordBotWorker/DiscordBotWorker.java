package org.example.DiscordBotWorker;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.example.Config.ConfigLoader;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DiscordBotWorker extends ListenerAdapter {
    ConfigLoader configLoader = new ConfigLoader();
    private JDA jda;

    public void startBot() {
        String token = configLoader.getToken();
        try {
            jda = JDABuilder.createDefault(token)
                    .enableIntents(List.of(
                            GatewayIntent.GUILD_MESSAGES,
                            GatewayIntent.MESSAGE_CONTENT))
                    .setStatus(OnlineStatus.DO_NOT_DISTURB)
                    .setActivity(Activity.watching("статистику по Beat Saber"))
                    .addEventListeners(this, new Bot())
                    .build();
        } catch (Exception e) {
            System.err.println("Error starting the bot: " + e.getMessage());
        }
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        super.onReady(event);

        String guildId = configLoader.getGuildId();
        Guild guild = jda.getGuildById(guildId);

        if (guild != null) {
            guild.upsertCommand("link", "Связать ваш аккаунт с BeatLeader").queue();
            guild.upsertCommand("link2", "Связать ваш аккаунт с ScoreSaber").addOption(OptionType.STRING, "id", "ID пользователя ScoreSaber", true).queue();
        } else {
            System.err.println("Guild not found with ID: " + guildId);
        }
    }
}
