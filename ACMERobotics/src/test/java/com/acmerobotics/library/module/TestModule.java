package com.acmerobotics.library.module;

import com.acmerobotics.library.module.core.BaseModule;
import com.acmerobotics.library.module.core.Inject;

public class TestModule extends BaseModule {

    @Override
    public void configure() {
        bind(int.class).toInstance(5);
        bind(String.class).toInstance("Hello, world!");
    }

}