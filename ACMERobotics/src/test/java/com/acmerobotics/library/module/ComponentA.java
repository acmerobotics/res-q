package com.acmerobotics.library.module;

public class ComponentA {

    public ComponentB b;

    @Inject
    public ComponentA(ComponentB b, int i) {
        this.b = b;
    }

    public ComponentB getB() {
        return b;
    }

}
