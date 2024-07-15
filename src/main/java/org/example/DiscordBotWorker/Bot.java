package org.example.DiscordBotWorker;

import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.Map;
import java.util.Objects;

import static org.example.BeatLeaderWorker.BeatLeaderWorker.getUserPP;
import static org.example.Logger.Logger.LOGGER;

public class Bot extends ListenerAdapter {
    private Map<String, String> roleRanges;

    public Bot() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            roleRanges = mapper.readValue(new File(getClass().getClassLoader().getResource("roles.json").toURI()), Map.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("link")) {
            if (event.getMember() == null) {
                event.reply("This command can only be used in a server.").setEphemeral(true).queue();
                return;
            }
            String userId = event.getMember().getId();
            try {
                int userScore = Integer.parseInt(getUserPP(userId));
                String roleToAssign = findRoleForScore(userScore);
                if (roleToAssign != null) {
                    Objects.requireNonNull(event.getGuild()).addRoleToMember(UserSnowflake.fromId(userId), event.getGuild().getRolesByName(roleToAssign, true).get(0)).queue();
                    event.reply("Ваша роль успешно синхронизирована с BeatLeader и назначена роль: " + roleToAssign).setEphemeral(true).queue();
                } else {
                    event.reply("No role found for your score.").setEphemeral(true).queue();
                }
            } catch (Exception e) {
                LOGGER.warning("Error fetching user PP");
                event.reply("An error occurred while linking your profile.").setEphemeral(true).queue();
            }
        }
    }

    private String findRoleForScore(int score) {
        for (String range : roleRanges.keySet()) {
            String[] parts = range.split("/");
            int min = Integer.parseInt(parts[0]);
            int max = Integer.parseInt(parts[1]);
            if (score >= min && score <= max) {
                return roleRanges.get(range);
            }
        }
        return null;
    }
}