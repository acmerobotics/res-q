package com.acmerobotics.library.inject;

import com.acmerobotics.library.inject.core.BaseModule;

public class TestModule extends BaseModule {

    @Override
    public void configure() {
        bind(int.class).toInstance(5);
        bind(String.class).toInstance("Hello, world!");
    }

}