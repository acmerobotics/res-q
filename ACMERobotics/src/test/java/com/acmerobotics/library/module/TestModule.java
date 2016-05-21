package com.acmerobotics.library.module;

import com.acmerobotics.library.module.core.BaseModule;
import com.acmerobotics.library.module.core.Inject;

public class TestModule extends BaseModule {

    @Override
    public void configure() {
        bind(ComponentA.class).to(ComponentA.class);
        bind(ComponentB.class).to(ComponentB.class);
        bind(int.class).withAnnotation(Inject.class).toInstance(5);
        bind(String.class).toInstance("Hello, world!");
    }

}