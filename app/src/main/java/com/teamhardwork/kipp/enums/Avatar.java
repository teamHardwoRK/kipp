package com.teamhardwork.kipp.enums;

import com.teamhardwork.kipp.R;

public enum Avatar {
    // Animal characters
    CHESHIRE(R.drawable.avatar_cheshire),
    GARFIELD(R.drawable.avatar_garfield),
    GOOFY(R.drawable.avatar_goofy),
    HOBBES(R.drawable.avatar_hobbes),
    KERMIT(R.drawable.avatar_kermit),
    LEONARDO(R.drawable.avatar_leonardo),
    MARLIN(R.drawable.avatar_marlin),
    MOJO(R.drawable.avatar_mojo),
    NED(R.drawable.avatar_ned),
    PIKACHU(R.drawable.avatar_pikachu),
    REN(R.drawable.avatar_ren),
    SCOOBY(R.drawable.avatar_scooby),
    STIMPY(R.drawable.avatar_stimpy),
    TAZMANIAN(R.drawable.avatar_tazmanian),
    TIMON(R.drawable.avatar_timon),
    WINNIE(R.drawable.avatar_winnie),

    // Movie stars
    AMELIE(R.drawable.img_amelie),
    AVATAR(R.drawable.img_avatar),
    BLEACH(R.drawable.img_bleach),
    EDWARD(R.drawable.img_edward),
    EMMA(R.drawable.img_emma),
    GOLEM(R.drawable.img_golem),
    JOKER(R.drawable.img_joker),
    MOJO_JOJO(R.drawable.img_mojojojo),
    MORTAL_KOMBAT(R.drawable.img_mortal_kombat),
    NINJA_TURTLE(R.drawable.img_leo),
    PALE(R.drawable.img_pale),
    SCARLETT(R.drawable.img_scarlett),
    STORMTROOPER(R.drawable.img_stormtrooper),
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
