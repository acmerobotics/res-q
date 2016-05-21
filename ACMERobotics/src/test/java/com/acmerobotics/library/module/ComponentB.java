package com.acmerobotics.library.module;

import com.acmerobotics.library.module.core.Inject;

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
