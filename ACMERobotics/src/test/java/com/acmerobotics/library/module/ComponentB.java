package com.acmerobotics.library.module;

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
