package com.acmerobotics.library.module.core;

import java.util.ArrayList;
import java.util.List;

public class BaseModule {

    protected List<BindingBuilder> mapBuilders;

    public BaseModule() {
        mapBuilders = new ArrayList<>();
    }

    public BindingBuilder bind(Class c) {
        return bindAll().customFilter(new IdentityFilter(c));
    }

    public BindingBuilder bindAll() {
        BindingBuilder builder = new BindingBuilder<>();
        mapBuilders.add(builder);
        return builder;
    }

    public List<Binding> matchAll(Dependency arg) {
        List<Binding> bindings = new ArrayList<>();
        for (BindingBuilder builder : mapBuilders) {
            Binding binding = builder.getBinding();
            if (binding.applyFilters(arg)) {
                bindings.add(binding);
            }
        }
        return bindings;
    }

    public void configure() {
        bindAll().withAnnotation(ResolveTo.class).toProvider(new ClassResolver());
    }

}
