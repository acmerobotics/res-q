package com.acmerobotics.library.inject.hardware;

import com.acmerobotics.library.inject.core.Scoped;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Scoped
@Retention(RetentionPolicy.RUNTIME)
public @interface Device {

    public String value();

}
