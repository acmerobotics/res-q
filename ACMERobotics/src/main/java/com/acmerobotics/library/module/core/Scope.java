package com.acmerobotics.library.module.core;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Scope {

    private List<Annotation> annotations;

    private Scope(List<Annotation> annotationArgs) {
        annotations = annotationArgs;
    }

    public static Scope root() {
        return new Scope(new ArrayList<Annotation>());
    }

    public Scope andAll(Collection<Annotation> newAnnotations) {
        List<Annotation> scopeAnnotations = new ArrayList<Annotation>(annotations);
        for (Annotation a : newAnnotations) {
            Class<?> c = a.annotationType();
            if (c.isAnnotationPresent(Scoped.class)) {
                scopeAnnotations.add(a);
            }
        }
        return new Scope(scopeAnnotations);
    }

    public List<Annotation> getAnnotations() {
        return annotations;
    }

    @Override
    public String toString() {
        if (annotations.size() == 0)
            return "no annotations";
        String s = "(";
        for (int i = 0; i < (annotations.size() - 1); i++) {
            s += annotations.get(i).toString() + ", ";
        }
        s += annotations.get(annotations.size() - 1).toString() + ")";
        return s;
    }

}
