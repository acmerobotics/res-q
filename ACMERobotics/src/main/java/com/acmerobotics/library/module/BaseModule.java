package com.acmerobotics.library.module;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseModule {

    private Map<Class, Dependency> dependencyMap;

    public BaseModule() {
        dependencyMap = new HashMap<>();

        configure();
    }

    protected void dependency(Class c, Dependency d) {
        dependencyMap.put(c, d);
    }

    protected void dependency(Dependency d) {
        dependency(d.getClassDependency(), d);
    }

    public boolean hasDependency(Class c) {
        return dependencyMap.containsKey(c);
    }

    public Dependency getDependency(Class c) {
        return dependencyMap.get(c);
    }

    protected abstract void configure();

}
