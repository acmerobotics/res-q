package com.acmerobotics.library.inject.core;

public interface Provider<T> {

    public T provide(Injector injector, Dependency dependency);

}
