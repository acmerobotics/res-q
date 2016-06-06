package com.acmerobotics.library.inject.core;

public class ClassResolver<T> implements Provider<T> {

    @Override
    public T provide(Injector injector, Dependency dependency) {
        ResolveTo resolveTo = (ResolveTo) dependency.getAnnotation(ResolveTo.class);
        return (new ClassProvider<T>(resolveTo.value())).provide(injector, dependency);
    }

}
