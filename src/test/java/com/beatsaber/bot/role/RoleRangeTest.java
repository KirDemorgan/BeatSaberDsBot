package com.beatsaber.bot.role;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class RoleRangeTest {

    private final RoleRange range = new RoleRange(100, 200, "role-id");

    @Test
    void matchesExactMin() {
        assertTrue(range.matches(100));
    }

    @Test
    void matchesExactMax() {
        assertTrue(range.matches(200));
    }

    @ParameterizedTest
    @ValueSource(ints = {101, 150, 199})
    void matchesWithinRange(int pp) {
        assertTrue(range.matches(pp));
    }

    @Test
    void doesNotMatchBelowMin() {
        assertFalse(range.matches(99));
    }

    @Test
    void doesNotMatchAboveMax() {
        assertFalse(range.matches(201));
    }

    @Test
    void exposesRoleId() {
        assertEquals("role-id", range.roleId());
    }
}
