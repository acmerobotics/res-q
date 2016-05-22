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

    public <V extends T> Binding<V> to(Class<V> dest) {
        return toProvider(new ClassProvider(dest));
    }

    public <V extends T> Binding<V> toInstance(V o) {
        return toProvider(new InstanceProvider(o));
    }

    public <V extends T> Binding<V> toProvider(Provider<V> provider) {
        binding = new Binding(filters, provider);
        return binding;
    }

    public Binding getBinding() {
        return binding;
    }

}
