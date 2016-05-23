package com.acmerobotics.library.module.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

public class ClassProvider<T> implements Provider<T> {

    private Class<T> klass;

    public ClassProvider(Class<T> c) {
        klass = c;
    }

    @Override
    public T provide(Injector injector, Dependency dependency) {
        System.out.println("class provider:\t" + dependency.toString());
        Constructor<T> constructor = getConstructor();
        Class[] parameters = constructor.getParameterTypes();
        Annotation[][] annotations = constructor.getParameterAnnotations();
        Scope scope = dependency.getScope();
        int size = parameters.length;
        Object[] args = new Object[size];
        for (int i = 0; i < size; i++) {
            List<Annotation> fullAnnotations = Arrays.asList(annotations[i]);
            fullAnnotations.addAll(scope.getAnnotations());
            args[i] = injector.fulfil(new Dependency<T>(
                    parameters[i],
                    fullAnnotations,
                    scope.andAll(dependency.getAnnotations())
            ));
            System.out.println("class provider:\targ " + args[i].toString());
        }
        try {
            return constructor.newInstance(args);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Constructor getConstructor() {
        Constructor[] constructors = klass.getConstructors();
        for (Constructor constructor : constructors) {
            if (constructor.isAnnotationPresent(Inject.class)) {
                return constructor;
            }
        }
        return constructors[0];
    }

}
