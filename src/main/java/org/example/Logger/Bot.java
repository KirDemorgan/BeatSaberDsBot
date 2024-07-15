package org.example.Logger;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import static org.example.BeatLeaderWorker.BeatLeaderWorker.getUserPP;

public class Bot extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }
        String userId = event.getAuthor().getId();
        try {
            getUserPP(userId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
