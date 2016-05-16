package com.acmerobotics.library.module;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class Injector {

    private BaseModule module;

    public Injector(BaseModule baseModule) {
        module = baseModule;
    }

    public <T> T inject(Class<T> root) {
        return ((T) injectDependency(resolveClassAsDependency(root)));
    }

    public void fill(Object o) {
        Class c = o.getClass();
        Field[] fields = c.getFields();
        for (Field field : fields) {
            try {
                field.set(o, inject(field.getType()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private Object injectDependency(Dependency rootDep) {
        try {
            Constructor constructor = getConstructor(rootDep.getClassDependency());
            Class[] parameters = constructor.getParameterTypes();
            int size = parameters.length;
            if (size == 0) {
                return constructor.newInstance(null);
            } else {
                Object[] args = new Object[size];
                for (int i = 0; i < size; i++) {
                    Class param = parameters[i];
                    if (rootDep.hasKnownArg(i)) {
                        args[i] = rootDep.getKnownArg(i);
                    } else {
                        args[i] = injectDependency(resolveClassAsDependency(param));
                    }
                }
                return constructor.newInstance(args);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Dependency resolveClassAsDependency(Class c) {
        if (module.hasDependency(c)) {
            return module.getDependency(c);
        } else {
            return Dependency.type(c);
        }
    }

    private Constructor getConstructor(Class c) {
        Constructor[] constructors = c.getConstructors();
        for (Constructor constructor : constructors) {
            if (constructor.isAnnotationPresent(Inject.class)) {
                return constructor;
            }
        }
        return constructors[0];
    }

}
