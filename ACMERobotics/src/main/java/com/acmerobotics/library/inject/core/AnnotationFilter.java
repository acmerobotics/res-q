package com.acmerobotics.library.inject.core;

import java.lang.annotation.Annotation;
import java.util.List;

public class AnnotationFilter implements Filter {

    private Class anno;

    public AnnotationFilter(Class c) {
        anno = c;
    }

    @Override
    public boolean apply(Dependency a) {
        List<Annotation> annotations = a.getAllAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().equals(anno)) {
                System.out.println("annotation filter:\tmatched " + anno.toString());
                return true;
            }
        }
        return false;
    }

}
