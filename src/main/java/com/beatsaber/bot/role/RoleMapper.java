package com.beatsaber.bot.role;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.*;

public class RoleMapper {

    private final List<RoleRange> ranges;

    private RoleMapper(List<RoleRange> ranges) {
        this.ranges = Collections.unmodifiableList(ranges);
    }

    public static RoleMapper fromResource(String resourceName) {
        try (InputStream is = RoleMapper.class.getClassLoader().getResourceAsStream(resourceName)) {
            if (is == null) throw new IllegalStateException("Resource not found: " + resourceName);

            Map<String, String> raw = new ObjectMapper().readValue(is, new TypeReference<>() {});
            List<RoleRange> ranges = new ArrayList<>();
            for (Map.Entry<String, String> entry : raw.entrySet()) {
                String[] parts = entry.getKey().split("/");
                ranges.add(new RoleRange(
                    Integer.parseInt(parts[0]),
                    Integer.parseInt(parts[1]),
                    entry.getValue()
                ));
            }
            return new RoleMapper(ranges);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load role mapping from " + resourceName, e);
        }
    }

    public String findRole(int pp) {
        return ranges.stream()
            .filter(r -> r.matches(pp))
            .map(RoleRange::roleId)
            .findFirst()
            .orElse(null);
    }

    public Set<String> getAllRoleIds() {
        Set<String> ids = new HashSet<>();
        for (RoleRange r : ranges) ids.add(r.roleId());
        return ids;
    }
}
