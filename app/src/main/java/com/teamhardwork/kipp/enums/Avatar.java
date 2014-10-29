package com.teamhardwork.kipp.enums;

import com.teamhardwork.kipp.R;

public enum Avatar {
    NINJA_TURTLE(R.drawable.img_leo),
    MORTAL_KOMBAT(R.drawable.img_mortal_kombat);

    int resourceId;

    Avatar(int resourceId) {
        this.resourceId = resourceId;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }
}
