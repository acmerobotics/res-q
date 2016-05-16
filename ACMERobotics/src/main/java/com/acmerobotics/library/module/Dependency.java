package com.acmerobotics.library.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dependency {

    private Map<Integer, Object> knownArgs;
    private final Class classDependency;

    public Dependency(Class c) {
        classDependency = c;
        knownArgs = new HashMap<>();
    }

    public static Dependency type(Class c) {
        return new Dependency(c);
    }

    public Dependency arg(int index, Object obj) {
        knownArgs.put(index, obj);
        return this;
    }

    public Class getClassDependency() {
        return classDependency;
    }

    public Map<Integer, Object> getKnownArgs() {
        return knownArgs;
    }

    public Object getKnownArg(int i) {
        return knownArgs.get(i);
    }

    public boolean hasKnownArg(int i) {
        return knownArgs.containsKey(i);
    }

}
