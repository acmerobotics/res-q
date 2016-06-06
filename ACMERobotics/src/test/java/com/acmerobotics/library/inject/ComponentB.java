package com.acmerobotics.library.inject;

import com.acmerobotics.library.inject.core.Inject;

public class ComponentB {

    private String string;

    @Inject
    public ComponentB(String s) {
        string = s;
    }

    public String getString() {
        return string;
    }

}
