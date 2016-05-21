package com.acmerobotics.library.module.core;

import java.lang.annotation.Annotation;

public class AnnotationFilter implements Filter {

    private Class anno;

    public AnnotationFilter(Class c) {
        anno = c;
    }

    @Override
    public boolean apply(Dependency a) {
        Annotation[] annotations = a.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().equals(anno)) {
                return true;
            }
        }
        return false;
    }

}
