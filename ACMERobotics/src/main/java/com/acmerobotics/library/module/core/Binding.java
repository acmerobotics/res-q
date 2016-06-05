package com.acmerobotics.library.module.core;

import java.util.ArrayList;
import java.util.List;

public class Binding<T> {

    public static class Builder<T> {

        private List<Filter> filters;
        private Binding binding = null;

        public Builder() {
            filters = new ArrayList<>();
        }

        public Builder(Binding binding) {
            filters = binding.filters;
            this.binding = binding;
        }

        public Builder customFilter(Filter filter) {
            filters.add(filter);
            return this;
        }

        public Builder withAnnotation(Class c) {
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

    private List<Filter> filters;
    private Provider<T> provider;

    public Binding(List<Filter> filterList, Provider<T> prov) {
        filters = filterList;
        provider = prov;
    }

    public boolean applyFilters(Dependency a) {
        for (Filter filter : filters) {
            if (!filter.apply(a)) {
                return false;
            }
        }
        return true;
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public Provider<T> getProvider() {
        return provider;
    }

}
