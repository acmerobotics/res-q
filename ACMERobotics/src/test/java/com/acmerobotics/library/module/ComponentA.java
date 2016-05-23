package com.acmerobotics.library.module;

import com.acmerobotics.library.module.core.Inject;
import com.acmerobotics.library.module.hardware.Hardware;

public class ComponentA {

    public ComponentB b;
    private int i;

    @Inject
    public ComponentA(ComponentB b, int i) {
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
