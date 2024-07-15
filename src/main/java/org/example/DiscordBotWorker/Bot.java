package org.example.DiscordBotWorker;

import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.Map;
import java.util.Objects;

import static org.example.BeatSaberStats.BeatLeaderWorker.getUserPP;
import static org.example.BeatSaberStats.ScoreSaberWorker.getUserPPScoreSaber;
import static org.example.Logger.Logger.LOGGER;

public class Bot extends ListenerAdapter {
    private Map<String, String> roleRanges;

    public Bot() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            roleRanges = mapper.readValue(new File(getClass().getClassLoader().getResource("roles.json").toURI()), Map.class);
        } catch (Exception e) {
            LOGGER.warning("Failed to load role ranges: " + e.getMessage());
        }
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("link")) {
            if (event.getMember() == null) {
                event.reply("Команда только для серверов.").setEphemeral(true).queue();
                return;
            }
            String userId = event.getMember().getId();
            try {
                String ppString = getUserPP(userId);
                if (ppString == null) {
                    LOGGER.warning("PP string is null for user ID: " + userId);
                    event.reply("Не удалось получить данные о пользователе.").setEphemeral(true).queue();
                    return;
                }
                int userScore = Integer.parseInt(ppString);
                String roleToAssign = findRoleForScore(userScore);
                if (roleToAssign != null) {
                    Objects.requireNonNull(event.getGuild()).addRoleToMember(UserSnowflake.fromId(userId), event.getGuild().getRolesByName(roleToAssign, true).get(0)).queue();
                    event.reply("Ваша роль успешно синхронизирована с BeatLeader и назначена роль: " + roleToAssign).setEphemeral(true).queue();
                } else {
                    event.reply("Для ваших поинтов пока нет роли.").setEphemeral(true).queue();
                }
            } catch (Exception e) {
                LOGGER.warning("Error fetching user PP for user ID: " + userId + " - " + e.getMessage());
                event.reply("Не удалось привязать профиль.").setEphemeral(true).queue();
            }
        }

        if ("link2".equals(event.getName())) {
            if (event.getMember() == null) {
                event.reply("Команда только для серверов.").setEphemeral(true).queue();
                return;
            }
            String userId = event.getOption("id") != null ? event.getOption("id").getAsString() : null;
            if (userId == null) {
                LOGGER.warning("User ID is null for command /link2");
                event.reply("ID пользователя не предоставлен.").setEphemeral(true).queue();
                return;
            }

            try {
                String ppString = getUserPPScoreSaber(userId);
                if (ppString == null) {
                    LOGGER.warning("PP string is null for user ID: " + userId);
                    event.reply("Не удалось получить данные о пользователе.").setEphemeral(true).queue();
                    return;
                }
                // Convert ppString to a decimal and then to an integer for role assignment
                int userScore = (int) Math.round(Double.parseDouble(ppString));
                String roleToAssign = findRoleForScore(userScore);
                if (roleToAssign != null) {
                    if (event.getGuild() == null || event.getGuild().getRolesByName(roleToAssign, true).isEmpty()) {
                        LOGGER.warning("Guild is null or role not found for: " + roleToAssign);
                        event.reply("Ошибка при назначении роли.").setEphemeral(true).queue();
                        return;
                    }
                    event.getGuild().addRoleToMember(UserSnowflake.fromId(event.getMember().getId()), event.getGuild().getRolesByName(roleToAssign, true).get(0)).queue();
                    event.reply("Ваша роль успешно синхронизирована с ScoreSaber и назначена роль: " + roleToAssign).setEphemeral(true).queue();
                } else {
                    event.reply("Для ваших поинтов пока нет роли.").setEphemeral(true).queue();
                }
            } catch (Exception e) {
                LOGGER.warning("Error fetching user PP for user ID: " + userId + " - " + e.getMessage());
                event.reply("Не удалось привязать профиль.").setEphemeral(true).queue();
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
