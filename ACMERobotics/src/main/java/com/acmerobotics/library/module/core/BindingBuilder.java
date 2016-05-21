package com.acmerobotics.library.module.core;

import java.util.ArrayList;
import java.util.List;

public class BindingBuilder<T> {

    private List<Filter> filters;
    private Binding binding = null;

    public BindingBuilder() {
        filters = new ArrayList<>();
    }

    public BindingBuilder customFilter(Filter filter) {
        filters.add(filter);
        return this;
    }

    public BindingBuilder withAnnotation(Class c) {
        return customFilter(new AnnotationFilter(c));
    }

    public <V extends T> void to(Class<V> dest) {
        toProvider(new ClassProvider(dest));
    }

    public <V extends T> void toInstance(V o) {
        toProvider(new InstanceProvider(o));
    }

    public <V extends T> void toProvider(Provider<V> provider) {
        binding = new Binding(filters, provider);
    }

    public Binding getBinding() {
        return binding;
    }

}
