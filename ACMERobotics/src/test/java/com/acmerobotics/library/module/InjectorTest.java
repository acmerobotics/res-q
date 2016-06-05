package com.acmerobotics.library.module;

import com.acmerobotics.library.module.core.Injector;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class InjectorTest {

    @Test
    public void testInjectorInject() {
        TestModule module = new TestModule();
        Injector injector = new Injector(module);
        ComponentA a = injector.create(ComponentA.class);
        injector.create(ComponentC.class);
        assertEquals("should inject 'Hello, world!' into ComponentB's string argument", "Hello, world!", a.getB().getString());
    }

}
