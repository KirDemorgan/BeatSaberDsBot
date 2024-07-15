package org.example.DiscordBotWorker;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.example.Logger.Bot;

import javax.security.auth.login.LoginException;
import java.util.List;

public class DiscordBotWorker {
    Bot bot = new Bot();

    private final String token = "MTI2MjQ3MjQ4NTM2NjkyNzUwMw.G5Armu.WIOriJ2k9cfW1VW7BZRTpKYiEuX-GWaJqwRVEU";

    public void startBot() {
        try {
            JDABuilder jdaBuilder = (JDABuilder) JDABuilder
                    .createDefault(token)
                    .enableIntents(
                            List.of(
                                    GatewayIntent.GUILD_MESSAGES,
                                    GatewayIntent.MESSAGE_CONTENT
                            )
                    )
                    .setStatus(OnlineStatus.DO_NOT_DISTURB)
                    .setActivity(Activity.watching("Смотрю статистику"))
                    .addEventListeners(new Bot());
            jdaBuilder.build();
        }   catch (Exception e) {
            System.err.println("Error starting the bot: " + e.getMessage());
        }
    }
}
