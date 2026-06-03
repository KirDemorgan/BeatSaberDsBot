package com.beatsaber.bot.role;

public record RoleRange(int min, int max, String roleId) {

    public boolean matches(int pp) {
        return pp >= min && pp <= max;
    }
}
