package com.acmerobotics.library.module;

public class TestModule extends BaseModule {

    @Override
    public void configure() {
        dependency(Dependency.type(ComponentA.class).arg(1, 5));
        dependency(Dependency.type(ComponentB.class).arg(0, "Hello, world!"));
    }

}