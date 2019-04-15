package com.github.common.reflect;

import org.apache.commons.lang3.ClassUtils;
import org.springside.modules.utils.reflect.ClassUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/4/15.
 */
public class AnnotationUtil {
    /**
     * 递归Class所有的Annotation，一个最彻底的实现.
     *
     * @param cls
     * @return
     */
    public static Set<Annotation> getAllAnnotations(final Class<?> cls) {
        List<Class<?>> allTypes = ClassUtil.getAllSuperclasses(cls);
        allTypes.addAll(ClassUtil.getAllInterfaces(cls));
        allTypes.add(cls);

        Set<Annotation> anns = new HashSet<Annotation>();
        for (Class<?> type : allTypes) {
            anns.addAll(Arrays.asList(type.getDeclaredAnnotations()));
        }

        Set<Annotation> superAnnotations = new HashSet<Annotation>();
        for (Annotation ann : anns) {
            getSuperAnnotations(ann.annotationType(), superAnnotations);
        }

        anns.addAll(superAnnotations);

        return anns;
    }

    private static <A extends Annotation> void getSuperAnnotations(Class<A> annotationType, Set<Annotation> visited) {
        Annotation[] anns = annotationType.getDeclaredAnnotations();

        for (Annotation ann : anns) {
            if (!ann.annotationType().getName().startsWith("java.lang") && visited.add(ann)) {
                getSuperAnnotations(ann.annotationType(), visited);
            }
        }
    }

    /**
     * 找出所有标注了该annotation的公共属性，循环遍历父类.
     * <p>
     * 暂未支持Spring风格Annotation继承Annotation
     * <p>
     * copy from org.unitils.util.AnnotationUtils
     */
    public static <T extends Annotation> Set<Field> getAnnotatedPublicFields(Class<? extends Object> clazz,
                                                                             Class<T> annotation) {

        if (Object.class.equals(clazz)) {
            return Collections.emptySet();
        }

        Set<Field> annotatedFields = new HashSet<Field>();
        Field[] fields = clazz.getFields();

        for (Field field : fields) {
            if (field.getAnnotation(annotation) != null) {
                annotatedFields.add(field);
            }
        }

        return annotatedFields;
    }

    /**
     * 找出所有标注了该annotation的属性，循环遍历父类，包含private属性.
     * <p>
     * 暂未支持Spring风格Annotation继承Annotation
     * <p>
     * copy from org.unitils.util.AnnotationUtils
     */
    public static <T extends Annotation> Set<Field> getAnnotatedFields(Class<? extends Object> clazz,
                                                                       Class<T> annotation) {
        if (Object.class.equals(clazz)) {
            return Collections.emptySet();
        }
        Set<Field> annotatedFields = new HashSet<Field>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.getAnnotation(annotation) != null) {
                annotatedFields.add(field);
            }
        }
        annotatedFields.addAll(getAnnotatedFields(clazz.getSuperclass(), annotation));
        return annotatedFields;
    }

    /**
     * 找出所有标注了该annotation的公共方法(含父类的公共函数)，循环其接口.
     * <p>
     * 暂未支持Spring风格Annotation继承Annotation
     * <p>
     * 另，如果子类重载父类的公共函数，父类函数上的annotation不会继承，只有接口上的annotation会被继承.
     */
    public static <T extends Annotation> Set<Method> getAnnotatedPublicMethods(Class<?> clazz, Class<T> annotation) {
        // 已递归到Objebt.class, 停止递归
        if (Object.class.equals(clazz)) {
            return Collections.emptySet();
        }

        List<Class<?>> ifcs = ClassUtils.getAllInterfaces(clazz);
        Set<Method> annotatedMethods = new HashSet<Method>();

        // 遍历当前类的所有公共方法
        Method[] methods = clazz.getMethods();

        for (Method method : methods) {
            // 如果当前方法有标注，或定义了该方法的所有接口有标注
            if (method.getAnnotation(annotation) != null || searchOnInterfaces(method, annotation, ifcs)) {
                annotatedMethods.add(method);
            }
        }
        return annotatedMethods;
    }

    private static <T extends Annotation> boolean searchOnInterfaces(Method method, Class<T> annotationType,
                                                                     List<Class<?>> ifcs) {
        for (Class<?> iface : ifcs) {
            try {
                Method equivalentMethod = iface.getMethod(method.getName(), method.getParameterTypes());
                if (equivalentMethod.getAnnotation(annotationType) != null) {
                    return true;
                }
            } catch (NoSuchMethodException ex) { // NOSONAR
                // Skip this interface - it doesn't have the method...
            }
        }
        return false;
    }
}
