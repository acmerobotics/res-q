package com.acmerobotics.library.module.core;

import java.util.List;

public class Injector {

    private BaseModule module;

    public Injector(BaseModule baseModule) {
        module = baseModule;
        baseModule.configure();
    }

    public <T> T create(Class<T> c) {
        System.out.println("create: " + c.getSimpleName());
        return fulfil(new Dependency<T>(c, null));
    }

    public <T> T fulfil(Dependency<T> d) {
        System.out.println("fulfil:  " + d.getType().getSimpleName());
        List<Binding> bindings = module.matchAll(d);
        if (bindings.size() != 1) {
            System.out.println(bindings.size() + " bindings (should only be 1 for now)");
        }
        Binding<? extends T> binding = bindings.get(0);
        T output = binding.getProvider().provide(this, d);
        System.out.println("found: " + output.getClass().getSimpleName());
        return output;
    }

}
