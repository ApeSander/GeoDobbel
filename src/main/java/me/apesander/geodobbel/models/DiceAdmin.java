package me.apesander.geodobbel.models;

import java.util.UUID;

// This object is a dice admin
public class DiceAdmin extends DiceUser {
    public boolean operator;

    public DiceAdmin(UUID userId, boolean operator) {
        super(userId);
        this.operator = operator;
    }
}
