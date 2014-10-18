package com.teamhardwork.kipp.enums;

import java.util.ArrayList;
import java.util.List;

public enum ActionType {
    CALL("call", Role.STUDENT),
    TEXT("text", Role.STUDENT),
    NOTE("note", Role.STUDENT),
    EMAIL("email", Role.STUDENT),
    PARENT_CALL("call", Role.PARENT),
    PARENT_TEXT("textt", Role.PARENT),
    PARENT_EMAIL("email", Role.PARENT);

    String displayName;
    Role role;

    ActionType(String displayName, Role role) {
        this.displayName = displayName;
        this.role  = role;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static List<ActionType> findForRole(Role role) {
        List<ActionType> typeList = new ArrayList<ActionType>();

        ActionType[] allTypes = values();

        for(int i = 0; i < allTypes.length; i++) {
            ActionType type = allTypes[i];

            if(type.role == role) {
                typeList.add(type);
            }
        }

        return typeList;
    }
}
