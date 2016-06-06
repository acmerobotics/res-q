package com.acmerobotics.library.inject.core;

import java.util.ArrayList;
import java.util.List;

public class BaseModule {

    protected List<Binding.Builder> builders;

    public BaseModule() {
        builders = new ArrayList<>();
    }

    public Binding.Builder bind(Class c) {
        return bindAll().customFilter(new IdentityFilter(c));
    }

    public Binding.Builder addBinding(int index, Binding binding) {
        Binding.Builder builder = new Binding.Builder(binding);
        builders.add(index, builder);
        return builder;
    }

    public Binding.Builder addBinding(Binding binding) {
        return addBinding(0, binding);
    }

    public void removeBinding(Binding binding) {
        for (Binding.Builder builder : builders) {
            if (builder.getBinding().equals(binding)) {
                builders.remove(builder);
                return;
            }
        }
    }

    public Binding.Builder bindAll() {
        Binding.Builder builder = new Binding.Builder<>();
        builders.add(builder);
        return builder;
    }

    public List<Binding> matchAll(Dependency arg) {
        List<Binding> bindings = new ArrayList<>();
        for (Binding.Builder builder : this.builders) {
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
