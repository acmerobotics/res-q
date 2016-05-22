package com.acmerobotics.library.module.core;

public class ClassResolver<T> implements Provider<T> {

    @Override
    public T provide(Injector injector, Dependency dependency) {
        ResolveTo resolveTo = (ResolveTo) dependency.getAnnotation(ResolveTo.class);
        return (T) injector.create(resolveTo.value());
    }

}
