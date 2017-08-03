package com.lamfire.utils;


import java.lang.annotation.Annotation;
import java.util.Set;

public class AnnotationUtils {

    /**
     * 获取带有注释的类
     * @param pkgName
     * @param annotationClass
     * @param <A>
     * @return
     */
    public static <A extends Annotation> Set<Class<?>> getClassesWithAnnotation(String pkgName,Class<A> annotationClass) {
        Set<Class<?>> classesSet = Sets.newHashSet();

        Set<Class<?>> allClassesSet = null;
        try {
            allClassesSet = ClassLoaderUtils.getClasses(pkgName);
         }catch (Exception e){

        }

        if(allClassesSet != null) {
            for (Class<?> clzz : allClassesSet) {
                Annotation anno = clzz.getAnnotation(annotationClass);
                if (anno != null) {
                    classesSet.add(clzz);
                }
            }
        }

        return classesSet;
    }

    public static <A extends Annotation> boolean hasAnnotation(Class<?> clazz,Class<A> annotationClass){
        return clazz.getAnnotation(annotationClass) != null;
    }

}
