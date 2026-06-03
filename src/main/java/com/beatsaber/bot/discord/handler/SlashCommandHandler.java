package com.beatsaber.bot.discord.handler;

import com.beatsaber.bot.platform.BeatLeaderClient;
import com.beatsaber.bot.platform.ScoreSaberClient;
import com.beatsaber.bot.role.RoleMapper;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SlashCommandHandler extends ListenerAdapter {

    private static final Logger log = LoggerFactory.getLogger(SlashCommandHandler.class);

    private final BeatLeaderClient beatLeaderClient;
    private final ScoreSaberClient scoreSaberClient;
    private final RoleMapper beatLeaderRoles;
    private final RoleMapper scoreSaberRoles;

    public SlashCommandHandler() {
        this.beatLeaderClient = new BeatLeaderClient();
        this.scoreSaberClient = new ScoreSaberClient();
        this.beatLeaderRoles = RoleMapper.fromResource("rolesBeatLeader.json");
        this.scoreSaberRoles = RoleMapper.fromResource("rolesScoreSaber.json");
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        switch (event.getName()) {
            case "link" -> handleLink(event);
            case "link2" -> handleLinkScoreSaber(event);
            case "unlink" -> handleUnlink(event);
        }
    }

    private void handleLink(SlashCommandInteractionEvent event) {
        event.deferReply(true).queue();
        Member member = event.getMember();
        if (member == null) {
            event.getHook().sendMessage("Команда доступна только на сервере.").queue();
            return;
        }

        try {
            double pp = beatLeaderClient.getUserPP(member.getId());
            assignRole(event.getHook(), member, beatLeaderRoles, (int) pp);
        } catch (Exception e) {
            log.error("BeatLeader lookup failed for Discord ID {}", member.getId(), e);
            event.getHook().sendMessage("Не удалось получить данные с BeatLeader. Убедись, что аккаунт привязан.").queue();
        }
    }

    private void handleLinkScoreSaber(SlashCommandInteractionEvent event) {
        event.deferReply(true).queue();
        Member member = event.getMember();
        if (member == null) {
            event.getHook().sendMessage("Команда доступна только на сервере.").queue();
            return;
        }

        String ssId = event.getOption("id").getAsString();

        try {
            double pp = scoreSaberClient.getUserPP(ssId);
            assignRole(event.getHook(), member, scoreSaberRoles, (int) Math.round(pp));
        } catch (Exception e) {
            log.error("ScoreSaber lookup failed for ID {}", ssId, e);
            event.getHook().sendMessage("Не удалось получить данные с ScoreSaber. Проверь правильность ID.").queue();
        }
    }

    private void handleUnlink(SlashCommandInteractionEvent event) {
        event.deferReply(true).queue();
        Member member = event.getMember();
        if (member == null) {
            event.getHook().sendMessage("Команда доступна только на сервере.").queue();
            return;
        }

        var allRoleIds = new HashSet<>(beatLeaderRoles.getAllRoleIds());
        allRoleIds.addAll(scoreSaberRoles.getAllRoleIds());

        List<Role> rolesToRemove = member.getRoles().stream()
            .filter(r -> allRoleIds.contains(r.getId()))
            .toList();

        if (rolesToRemove.isEmpty()) {
            event.getHook().sendMessage("У тебя нет привязанных ролей Beat Saber.").queue();
            return;
        }

        var guild = event.getGuild();
        var remaining = new AtomicInteger(rolesToRemove.size());

        for (Role role : rolesToRemove) {
            guild.removeRoleFromMember(member, role).queue(
                v -> {
                    if (remaining.decrementAndGet() == 0) {
                        event.getHook().sendMessage("Все роли Beat Saber сняты.").queue();
                    }
                },
                err -> log.error("Failed to remove role {} from {}", role.getName(), member.getId(), err)
            );
        }
    }

    private void assignRole(InteractionHook hook, Member member, RoleMapper mapper, int pp) {
        String roleId = mapper.findRole(pp);
        if (roleId == null) {
            hook.sendMessage("Роль для твоего уровня не найдена. PP: " + pp).queue();
            return;
        }

        var guild = member.getGuild();
        Role role = guild.getRoleById(roleId);
        if (role == null) {
            log.error("Role ID {} not found in guild {}", roleId, guild.getId());
            hook.sendMessage("Ошибка конфигурации: роль не найдена на сервере.").queue();
            return;
        }

        guild.addRoleToMember(member, role).queue(
            v -> hook.sendMessage("Роль **" + role.getName() + "** выдана! PP: " + pp).queue(),
            err -> {
                log.error("Failed to assign role {} to {}", role.getName(), member.getId(), err);
                hook.sendMessage("Не удалось выдать роль. Проверь права бота.").queue();
            }
        );
    }
}
