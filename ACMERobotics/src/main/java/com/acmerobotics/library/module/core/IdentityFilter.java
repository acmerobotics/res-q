package com.acmerobotics.library.module.core;

public class IdentityFilter implements Filter {

    private Class identity;

    public IdentityFilter(Class c) {
        identity = c;
    }

    @Override
    public boolean apply(Dependency a) {
        return isSubClass(a.getType());
    }

    public boolean isSubClass(Class subClass) {
        if (subClass.equals(identity)) {
            return true;
        } else if (subClass.equals(Object.class)) {
            return false;
        }
        Class[] parents = getParents(subClass);
        if (parents.length == 0) {
            return false;
        } else {
            for (Class parent : parents) {
                if (parent.equals(identity) || isSubClass(parent)) {
                    return true;
                }
            }
        }
        return false;
    }

    private Class[] getParents(Class subClass) {
        Class superClass = subClass.getSuperclass();
        Class[] interfaces = subClass.getInterfaces();
        Class[] parents;
        if (superClass == null || superClass.equals(Object.class)) {
            parents = interfaces;
        } else {
            parents = new Class[interfaces.length + 1];
            parents[0] = superClass;
            for (int i = 0; i < interfaces.length; i++) {
                parents[i+1] = interfaces[i];
            }
        }
        return parents;
    }
}
