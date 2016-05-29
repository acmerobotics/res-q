package com.acmerobotics.library.module.core;

import java.util.List;

public class Binding<T> {

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

    public Provider<T> getProvider() {
        return provider;
    }

}
