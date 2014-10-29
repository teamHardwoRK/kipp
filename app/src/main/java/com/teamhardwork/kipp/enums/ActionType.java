package com.teamhardwork.kipp.enums;

import com.teamhardwork.kipp.R;

import java.util.ArrayList;
import java.util.List;

public enum ActionType {
    CALL("Call", Role.STUDENT, R.drawable.wh_phone_ic),
    TEXT("Text", Role.STUDENT, R.drawable.wh_sms_ic),
    EMAIL("Email", Role.STUDENT, R.drawable.wh_email_ic),
    PARENT_CALL("Call", Role.PARENT, R.drawable.wh_phone_ic),
    PARENT_TEXT("Text", Role.PARENT, R.drawable.wh_sms_ic),
    PARENT_EMAIL("Email", Role.PARENT, R.drawable.wh_email_ic);

    String displayName;
    Role role;
    int resource;

    ActionType(String displayName, Role role, int resource) {
        this.displayName = displayName;
        this.role = role;
        this.resource = resource;
    }

    public static List<ActionType> findForRole(Role role) {
        List<ActionType> typeList = new ArrayList<ActionType>();

        ActionType[] allTypes = values();

        for (int i = 0; i < allTypes.length; i++) {
            ActionType type = allTypes[i];

            if (type.role == role) {
                typeList.add(type);
            }
        }

        return typeList;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getResource() {
        return resource;
    }
}
