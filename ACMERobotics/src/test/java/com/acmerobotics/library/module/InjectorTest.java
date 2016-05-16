package com.acmerobotics.library.module;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class InjectorTest {

    @Test
    public void testInjectorInject() {
        TestModule module = new TestModule();
        Injector injector = new Injector(module);
        ComponentA comp = injector.inject(ComponentA.class);
        String expected = (String) module.getDependency(ComponentB.class).getKnownArg(0);
        assertEquals("should inject 'Hello, world!' into ComponentB's string argument", expected, comp.getB().getString());
    }

    @Test
    public void testInjectorFill() {
        TestModule module = new TestModule();
        Injector injector = new Injector(module);
        ComponentC componentC = new ComponentC();
        injector.fill(componentC);
        String expected = (String) module.getDependency(ComponentB.class).getKnownArg(0);
        assertEquals("should properly inject ComponentA into ComponentC", expected, componentC.componentA.getB().getString());
    }

}
