package com.acmerobotics.library.module.core;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class Injector {

    public BaseModule module;

    public Injector(BaseModule baseModule) {
        module = baseModule;
        baseModule.configure();
    }

    public <T> void fill(T obj) {
        System.out.println("injector fill:\t\t" + obj.getClass().getCanonicalName());
        Class c = obj.getClass();
        for (Field field : c.getDeclaredFields()) {
            Dependency dep = new Dependency(field.getType(), Arrays.asList(field.getAnnotations()));
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
        System.out.println("injector create:\t" + c.getCanonicalName());
        return fulfil(new Dependency<T>(c, null));
    }

    public <T> T fulfil(Dependency<T> d) {
        System.out.println("injector fulfil:\t" + d.getType().getCanonicalName());
        List<Binding> bindings = module.matchAll(d);
        if (bindings.size() == 0) {
            System.out.println("injector made:\t\tjit binding");
            Class<T> type = d.getType();
            bindings.add(module.bind(type).to(type));
        }
        Binding<? extends T> binding = bindings.get(0);
        Provider<? extends T> provider = binding.getProvider();
        System.out.println("injector provider:\tusing " + provider.getClass().getCanonicalName());
        T output = provider.provide(this, d);
        if (!(provider instanceof InstanceProvider)) {
            module.addBinding(new Binding(binding.getFilters(), new InstanceProvider(output)));
        }
        System.out.println("injector output:\t" + output);
        return output;
    }

}
