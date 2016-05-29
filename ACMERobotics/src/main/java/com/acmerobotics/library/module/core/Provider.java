package com.acmerobotics.library.module.core;

public interface Provider<T> {

    public T provide(Injector injector, Dependency dependency);

}
