package org.example.DiscordBotWorker;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import static org.example.BeatLeaderWorker.BeatLeaderWorker.getUserPP;
import static org.example.Logger.Logger.LOGGER;

public class Bot extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("link")) {
            if (event.getMember() == null) {
                event.reply("This command can only be used in a server.").setEphemeral(true).queue();
                return;
            }
            String userId = event.getMember().getId();
            try {
                getUserPP(userId);
                event.reply( "Ваша роль успешно синхронизирована с BeatLeader").setEphemeral(true).queue();
            } catch (Exception e) {
                LOGGER.warning("Error fetching user PP");
                event.reply("An error occurred while linking your profile.").setEphemeral(true).queue();
            }
        }
    }
}
