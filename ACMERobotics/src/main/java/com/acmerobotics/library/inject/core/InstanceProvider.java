package com.acmerobotics.library.inject.core;

public class InstanceProvider<T> implements Provider<T> {

    private T instance;

    public InstanceProvider(T object) {
        instance = object;
    }

    @Override
    public T provide(Injector injector, Dependency dependency) {
        System.out.println("instance provider:\t" + instance);
        return instance;
    }
}
