package com.acmerobotics.library.inject;

public class ComponentC {

    public ComponentC(ComponentB b) {
        System.out.println("c: " + b.toString());
    }

}
