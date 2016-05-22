package com.acmerobotics.library.module.core;

import java.lang.reflect.Field;
import java.util.List;

public class Injector {

    private BaseModule module;

    public Injector(BaseModule baseModule) {
        module = baseModule;
        baseModule.configure();
    }

    public <T> void fill(T obj) {
        System.out.println("fill: " + obj.getClass().getSimpleName());
        Class c = obj.getClass();
        for (Field field : c.getDeclaredFields()) {
            Dependency dep = new Dependency(field.getType(), field.getAnnotations());
            if (!field.getName().startsWith("$")) {
                try {
                    field.set(obj, fulfil(dep));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public <T> T create(Class<T> c) {
        System.out.println("create: " + c.getSimpleName());
        return fulfil(new Dependency<T>(c, null));
    }

    public <T> T fulfil(Dependency<T> d) {
        System.out.println("fulfil:  " + d.getType().getSimpleName());
        List<Binding> bindings = module.matchAll(d);
        if (bindings.size() == 0) {
            System.out.println("made: JIT binding");
            Class<T> type = d.getType();
            bindings.add(module.bind(type).to(type));
        }
        Binding<? extends T> binding = bindings.get(0);
        T output = binding.getProvider().provide(this, d);
        System.out.println("found: " + output.getClass().getSimpleName());
        return output;
    }

}
