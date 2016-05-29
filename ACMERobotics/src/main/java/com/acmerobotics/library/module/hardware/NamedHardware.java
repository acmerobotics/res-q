package com.acmerobotics.library.module.hardware;

import com.acmerobotics.library.module.core.Scoped;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Scoped
@Retention(RetentionPolicy.RUNTIME)
public @interface NamedHardware {

    public String value();

}
