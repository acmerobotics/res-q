package com.acmerobotics.library.module.core;

import com.acmerobotics.library.module.hardware.Hardware;

import java.lang.annotation.Annotation;

public class Dependency<T> {

    private Class<T> type;
    private Annotation[] annotations;

    public Dependency(Class<T> c, Annotation[] a) {
        type = c;
        annotations = a;
    }

    public Class<T> getType() {
        return type;
    }

    public Annotation[] getAnnotations() {
        return annotations == null ? new Annotation[0] : annotations;
    }

    public Annotation getAnnotation(Class c) {
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().equals(c)) {
                return annotation;
            }
        }
        return null;
    }

}
