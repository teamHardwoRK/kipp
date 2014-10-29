package com.teamhardwork.kipp.enums;

import com.teamhardwork.kipp.R;

public enum Avatar {
    AMELIE(R.drawable.img_amelie),
    AVATAR(R.drawable.img_avatar),
    BLEACH(R.drawable.img_bleach),
    EDWARD(R.drawable.img_edward),
    EMMA(R.drawable.img_emma),
    GOLEM(R.drawable.img_golem),
    JOKER(R.drawable.img_joker),
    MORTAL_KOMBAT(R.drawable.img_mortal_kombat),
    NINJA_TURTLE(R.drawable.img_leo),
    PALE(R.drawable.img_pale),
    SCARLETT(R.drawable.img_scarlett),
    STORMTROOER(R.drawable.img_stormtrooper),
    TERMINATOR(R.drawable.img_terminator),
    WOLVERINE(R.drawable.img_wolverine);

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
