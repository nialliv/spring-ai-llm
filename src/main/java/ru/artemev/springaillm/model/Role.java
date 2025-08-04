package ru.artemev.springaillm.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Role {

    USER("user"),
    ASSISTANT("assistant"),
    SYSTEM("system");

    private final String role;

    public static Role getRole(String roleName) {
        return Role.valueOf(roleName.toUpperCase());
    }
}
