package com.acmerobotics.library.module.core;

import com.acmerobotics.library.module.hardware.Hardware;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class Dependency<T> {

    private Class<T> type;
    private List<Annotation> annotations;
    private Scope scope;

    public Dependency(Class<T> c, List<Annotation> a) {
        this(c, a, Scope.root());
    }

    public Dependency(Class<T> c, List<Annotation> a, Scope s) {
        type = c;
        annotations = a;
        scope = s;
    }

    public Class<T> getType() {
        return type;
    }

    public List<Annotation> getAnnotations() {
        return annotations == null ? new ArrayList<Annotation>() : annotations;
    }

    public Annotation getAnnotation(Class c) {
        for (Annotation annotation : getAllAnnotations()) {
            if (annotation.annotationType().equals(c)) {
                return annotation;
            }
        }
        return null;
    }

    public Scope getScope() {
        return scope;
    }

    public List<Annotation> getAllAnnotations() {
        List<Annotation> copy = new ArrayList<Annotation>(getAnnotations());
        copy.addAll(getScope().getAnnotations());
        return copy;
    }

    public String toString() {
        return "dependency: " + getType().getCanonicalName();
    }

}
