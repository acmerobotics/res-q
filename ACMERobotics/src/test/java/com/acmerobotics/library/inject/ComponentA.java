package com.acmerobotics.library.inject;

import com.acmerobotics.library.inject.core.Inject;

public class ComponentA {

    public ComponentB b;
    private int i;

    @Inject
    public ComponentA(ComponentB b, int i) {
        System.out.println("a: " + b.toString());
        this.b = b;
        this.i = i;
    }

    public ComponentB getB() {
        return b;
    }

    public String toString() {
        return "a[b[" + b.getString() + "]," + i + "]";
    }

}
