package com.beatsaber.bot.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class BotConfigTest {

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void throwsWhenTokenMissing(String token) {
        IllegalStateException ex = assertThrows(
            IllegalStateException.class,
            () -> BotConfig.of(token, "123456789")
        );
        assertTrue(ex.getMessage().contains("DISCORD_TOKEN"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void throwsWhenGuildIdMissing(String guildId) {
        IllegalStateException ex = assertThrows(
            IllegalStateException.class,
            () -> BotConfig.of("valid-token", guildId)
        );
        assertTrue(ex.getMessage().contains("GUILD_ID"));
    }

    @Test
    void throwsOnNonNumericGuildId() {
        assertThrows(NumberFormatException.class, () -> BotConfig.of("token", "not-a-number"));
    }

    @Test
    void loadsValidConfig() {
        BotConfig config = BotConfig.of("my-token", "987654321");
        assertEquals("my-token", config.getToken());
        assertEquals(987654321L, config.getGuildId());
    }

    @Test
    void trimWhitespaceFromValues() {
        BotConfig config = BotConfig.of("  my-token  ", "  123456789  ");
        assertEquals("my-token", config.getToken());
        assertEquals(123456789L, config.getGuildId());
    }
}
