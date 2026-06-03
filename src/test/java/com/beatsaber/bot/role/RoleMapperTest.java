package com.beatsaber.bot.role;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RoleMapperTest {

    private RoleMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = RoleMapper.fromResource("roles-test.json");
    }

    @ParameterizedTest(name = "pp={0} → {1}")
    @CsvSource({
        "0,     role-beginner",
        "50,    role-beginner",
        "100,   role-beginner",
        "101,   role-intermediate",
        "150,   role-intermediate",
        "200,   role-intermediate",
        "201,   role-advanced",
        "300,   role-advanced",
        "301,   role-expert",
        "9999,  role-expert",
    })
    void findsCorrectRoleForPP(int pp, String expectedRoleId) {
        assertEquals(expectedRoleId, mapper.findRole(pp));
    }

    @Test
    void returnsNullWhenNoPPRangeMatches() {
        RoleMapper sparse = RoleMapper.fromResource("roles-test.json");
        // negative PP — outside any defined range
        assertNull(sparse.findRole(-1));
    }

    @Test
    void getAllRoleIdsReturnsAllFour() {
        Set<String> ids = mapper.getAllRoleIds();
        assertEquals(4, ids.size());
        assertTrue(ids.containsAll(Set.of(
            "role-beginner", "role-intermediate", "role-advanced", "role-expert"
        )));
    }

    @Test
    void throwsOnMissingResource() {
        assertThrows(RuntimeException.class, () -> RoleMapper.fromResource("nonexistent.json"));
    }
}
