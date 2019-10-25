package com.github.common.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/4/15.
 */
public class ClassUtil {
    private static final String CGLIB_CLASS_SEPARATOR = "$$";
    private static Logger logger = LoggerFactory.getLogger(org.springside.modules.utils.reflect.ClassUtil.class);
    private static final Map<Class<?>, Class<?>> primitiveWrapperTypeMap = new IdentityHashMap(8);
    private static final String SETTER_PREFIX = "set";
    private static final String GETTER_PREFIX = "get";
    private static final String IS_PREFIX = "is";

    public ClassUtil() {
    }

    public static String getShortClassName(Class<?> cls) {
        return ClassUtils.getShortClassName(cls);
    }

    public static String getShortClassName(String className) {
        return ClassUtils.getShortClassName(className);
    }

    public static String getPackageName(Class<?> cls) {
        return ClassUtils.getPackageName(cls);
    }

    public static String getPackageName(String className) {
        return ClassUtils.getPackageName(className);
    }

    public static List<Class<?>> getAllSuperclasses(Class<?> cls) {
        return ClassUtils.getAllSuperclasses(cls);
    }

    public static List<Class<?>> getAllInterfaces(Class<?> cls) {
        return ClassUtils.getAllInterfaces(cls);
    }

    public static Set<Annotation> getAllAnnotations(Class<?> cls) {
        List<Class<?>> allTypes = getAllSuperclasses(cls);
        allTypes.addAll(getAllInterfaces(cls));
        allTypes.add(cls);
        Set<Annotation> anns = new HashSet();
        Iterator i$ = allTypes.iterator();

        while (i$.hasNext()) {
            Class<?> type = (Class) i$.next();
            anns.addAll(Arrays.asList(type.getDeclaredAnnotations()));
        }

        Set<Annotation> superAnnotations = new HashSet();
        Iterator $i = anns.iterator();

        while ($i.hasNext()) {
            Annotation ann = (Annotation) $i.next();
            getSupperAnnotations(ann.annotationType(), superAnnotations);
        }

        anns.addAll(superAnnotations);
        return anns;
    }

    private static <A extends Annotation> void getSupperAnnotations(Class<A> annotationType, Set<Annotation> visited) {
        Annotation[] anns = annotationType.getDeclaredAnnotations();
        Annotation[] arr$ = anns;
        int len$ = anns.length;

        for (int i$ = 0; i$ < len$; ++i$) {
            Annotation ann = arr$[i$];
            if (!ann.annotationType().getName().startsWith("java.lang") && visited.add(ann)) {
                getSupperAnnotations(ann.annotationType(), visited);
            }
        }

    }

    public static <T extends Annotation> Set<Field> getAnnotatedPublicFields(Class<? extends Object> clazz, Class<T> annotation) {
        if (Object.class.equals(clazz)) {
            return Collections.emptySet();
        } else {
            Set<Field> annotatedFields = new HashSet();
            Field[] fields = clazz.getFields();
            Field[] arr$ = fields;
            int len$ = fields.length;

            for (int i$ = 0; i$ < len$; ++i$) {
                Field field = arr$[i$];
                if (field.getAnnotation(annotation) != null) {
                    annotatedFields.add(field);
                }
            }

            return annotatedFields;
        }
    }

    public static <T extends Annotation> Set<Field> getAnnotatedFields(Class<? extends Object> clazz, Class<T> annotation) {
        if (Object.class.equals(clazz)) {
            return Collections.emptySet();
        } else {
            Set<Field> annotatedFields = new HashSet();
            Field[] fields = clazz.getDeclaredFields();
            Field[] arr$ = fields;
            int len$ = fields.length;

            for (int i$ = 0; i$ < len$; ++i$) {
                Field field = arr$[i$];
                if (field.getAnnotation(annotation) != null) {
                    annotatedFields.add(field);
                }
            }

            annotatedFields.addAll(getAnnotatedFields(clazz.getSuperclass(), annotation));
            return annotatedFields;
        }
    }

    public static <T extends Annotation> Set<Method> getAnnotatedPublicMethods(Class<?> clazz, Class<T> annotation) {
        if (Object.class.equals(clazz)) {
            return Collections.emptySet();
        } else {
            List<Class<?>> ifcs = ClassUtils.getAllInterfaces(clazz);
            Set<Method> annotatedMethods = new HashSet();
            Method[] methods = clazz.getMethods();
            Method[] arr$ = methods;
            int len$ = methods.length;

            for (int i$ = 0; i$ < len$; ++i$) {
                Method method = arr$[i$];
                if (method.getAnnotation(annotation) != null || searchOnInterfaces(method, annotation, ifcs)) {
                    annotatedMethods.add(method);
                }
            }

            return annotatedMethods;
        }
    }

    private static <T extends Annotation> boolean searchOnInterfaces(Method method, Class<T> annotationType, List<Class<?>> ifcs) {
        Iterator i$ = ifcs.iterator();

        while (i$.hasNext()) {
            Class iface = (Class) i$.next();

            try {
                Method equivalentMethod = iface.getMethod(method.getName(), method.getParameterTypes());
                if (equivalentMethod.getAnnotation(annotationType) != null) {
                    return true;
                }
            } catch (NoSuchMethodException var6) {
            }
        }

        return false;
    }

    public static Method getSetterMethod(Class<?> clazz, String propertyName, Class<?> parameterType) {
        String setterMethodName = "set" + StringUtils.capitalize(propertyName);
        return getAccessibleMethod(clazz, setterMethodName, parameterType);
    }

    public static Method getGetterMethod(Class<?> clazz, String propertyName) {
        String getterMethodName = "get" + StringUtils.capitalize(propertyName);
        Method method = getAccessibleMethod(clazz, getterMethodName);
        if (method == null) {
            getterMethodName = "is" + StringUtils.capitalize(propertyName);
            method = getAccessibleMethod(clazz, getterMethodName);
        }

        return method;
    }

    public static Field getAccessibleField(Class clazz, String fieldName) {
        Validate.notNull(clazz, "clazz can't be null", new Object[0]);
        Validate.notEmpty(fieldName, "fieldName can't be blank", new Object[0]);
        Class superClass = clazz;

        while (superClass != Object.class) {
            try {
                Field field = superClass.getDeclaredField(fieldName);
                makeAccessible(field);
                return field;
            } catch (NoSuchFieldException var4) {
                superClass = superClass.getSuperclass();
            }
        }

        return null;
    }

    public static Method getAccessibleMethod(Class<?> clazz, String methodName, Class... parameterTypes) {
        Validate.notNull(clazz, "class can't be null", new Object[0]);
        Validate.notEmpty(methodName, "methodName can't be blank", new Object[0]);
        Class[] theParameterTypes = ArrayUtils.nullToEmpty(parameterTypes);
        wrapClassses(theParameterTypes);
        Class searchType = clazz;

        while (searchType != Object.class) {
            try {
                Method method = searchType.getDeclaredMethod(methodName, theParameterTypes);
                makeAccessible(method);
                return method;
            } catch (NoSuchMethodException var6) {
                searchType = searchType.getSuperclass();
            }
        }

        return null;
    }

    public static Method getAccessibleMethodByName(Class clazz, String methodName) {
        Validate.notNull(clazz, "clazz can't be null", new Object[0]);
        Validate.notEmpty(methodName, "methodName can't be blank", new Object[0]);

        for (Class searchType = clazz; searchType != Object.class; searchType = searchType.getSuperclass()) {
            Method[] methods = searchType.getDeclaredMethods();
            Method[] arr$ = methods;
            int len$ = methods.length;

            for (int i$ = 0; i$ < len$; ++i$) {
                Method method = arr$[i$];
                if (method.getName().equals(methodName)) {
                    makeAccessible(method);
                    return method;
                }
            }
        }

        return null;
    }

    public static void makeAccessible(Method method) {
        if ((!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers())) && !method.isAccessible()) {
            method.setAccessible(true);
        }

    }

    public static void makeAccessible(Field field) {
        if ((!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers()) || Modifier.isFinal(field.getModifiers())) && !field.isAccessible()) {
            field.setAccessible(true);
        }

    }

    private static void wrapClassses(Class<?>[] source) {
        for (int i = 0; i < source.length; ++i) {
            Class<?> wrapClass = (Class) primitiveWrapperTypeMap.get(source[i]);
            if (wrapClass != null) {
                source[i] = wrapClass;
            }
        }

    }

    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;

        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable var3) {
        }

        if (cl == null) {
            cl = ClassUtils.class.getClassLoader();
            if (cl == null) {
                try {
                    cl = ClassLoader.getSystemClassLoader();
                } catch (Throwable var2) {
                }
            }
        }

        return cl;
    }

    public static Class<?> unwrapCglib(Object instance) {
        Validate.notNull(instance, "Instance must not be null", new Object[0]);
        Class<?> clazz = instance.getClass();
        if (clazz != null && clazz.getName().contains("$$")) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null && !Object.class.equals(superClass)) {
                return superClass;
            }
        }

        return clazz;
    }

    public static <T> Class<T> getClassGenricType(Class clazz) {
        return getClassGenricType(clazz, 0);
    }

    public static Class getClassGenricType(Class clazz, int index) {
        Type genType = clazz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            logger.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
            return Object.class;
        } else {
            Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
            if (index < params.length && index >= 0) {
                if (!(params[index] instanceof Class)) {
                    logger.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
                    return Object.class;
                } else {
                    return (Class) params[index];
                }
            } else {
                logger.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: " + params.length);
                return Object.class;
            }
        }
    }

    public static boolean isPresent(String className, ClassLoader classLoader) {
        try {
            classLoader.loadClass(className);
            return true;
        } catch (Throwable var3) {
            return false;
        }
    }

    public static boolean isSubClassOrInterfaceOf(Class subclass, Class superclass) {
        return superclass.isAssignableFrom(subclass);
    }

    static {
        primitiveWrapperTypeMap.put(Boolean.class, Boolean.TYPE);
        primitiveWrapperTypeMap.put(Byte.class, Byte.TYPE);
        primitiveWrapperTypeMap.put(Character.class, Character.TYPE);
        primitiveWrapperTypeMap.put(Double.class, Double.TYPE);
        primitiveWrapperTypeMap.put(Float.class, Float.TYPE);
        primitiveWrapperTypeMap.put(Integer.class, Integer.TYPE);
        primitiveWrapperTypeMap.put(Long.class, Long.TYPE);
        primitiveWrapperTypeMap.put(Short.class, Short.TYPE);
    }
}