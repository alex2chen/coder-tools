package com.github.common.bean;

import com.github.common.identity.RandomId;
import com.github.common.reflect.ClassUtil;
import com.github.common.service.EchoService;
import com.github.common.service.EchoServiceImpl;
import com.github.jvm.util.concurrent.ConcurrentArrayList;
import com.google.common.hash.Hashing;
import io.netty.buffer.ByteBuf;
import javassist.*;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @Author: alex.chen
 * @Description:
 * @Date: 2019/10/24
 */
public class Javassist_unit {
    private ServerInvokerFactory serverInvokerFactory = new ServerInvokerFactory();
    private Method echo = ClassUtil.getAccessibleMethod(EchoService.class, "echo", String.class);

    @Test
    public void go_genMethodClass() throws Exception {
        System.setProperty(MethodParamVO.CLASS_DEBUG_OUTPUT, Boolean.TRUE.toString());
        Method writeTo = ClassUtil.getAccessibleMethod(RandomId.class, "writeTo", ByteBuf.class);
        System.out.println(MethodParamVOClassFactory.createClass(writeTo));
        System.out.println(MethodParamVOClassFactory.createClass(echo));
    }

    @Test
    public void go_genSeviceClass() throws Exception {
        System.setProperty(MethodParamVO.CLASS_DEBUG_OUTPUT, Boolean.TRUE.toString());
        EchoService echoService = new EchoServiceImpl();
        serverInvokerFactory.register(EchoService.class, echoService);
        System.out.println(serverInvokerFactory.get(0).invoke(2, "top"));
        System.out.println(serverInvokerFactory.get(1).invoke("alex"));
        System.out.println(serverInvokerFactory.get(2).invoke());
    }

    public interface MethodParamVO {
        String NOT_SUPPORT_PARAMETER_NAME = "reflect method parameter name error, see https://www.concretepage.com/java/jdk-8/java-8-reflection-access-to-parameter-names-of-method-and-constructor-with-maven-gradle-and-eclipse-using-parameters-compiler-argument";
        String PARAMVO_PACKAGE = "com.github.common.generate.vo.";
        String CLASS_DEBUG_OUTPUT = "class.debugOutput";
        String CLASS_OUTPUT_DIR = "target";
    }

    public static class EmptyMethodParamVO implements MethodParamVO {
    }

    public static class MethodParamVOClassFactory {
        private static final ConcurrentMap<Method, Class<? extends MethodParamVO>> paramVOClassMap = new ConcurrentHashMap<>();

        public static Class<? extends MethodParamVO> createClass(Method method) throws CannotCompileException, NotFoundException, IOException {
            Objects.requireNonNull(method, "method must not be null");
            if (method.getParameterCount() == 0) {
                return EmptyMethodParamVO.class;
            }
            Class<? extends MethodParamVO> methodParamClass = paramVOClassMap.get(method);
            if (methodParamClass != null) {
                return methodParamClass;
            }
            synchronized (MethodParamVOClassFactory.class) {
                methodParamClass = paramVOClassMap.get(method);
                if (methodParamClass != null) {
                    return methodParamClass;
                }
                methodParamClass = doCreateClass(method);
                paramVOClassMap.put(method, methodParamClass);
            }
            return methodParamClass;
        }

        private static Class<? extends MethodParamVO> doCreateClass(Method method)
                throws CannotCompileException, NotFoundException, IOException {
            Class<?>[] parameterTypes = method.getParameterTypes();
            Parameter[] parameters = method.getParameters();
            if (!parameters[0].isNamePresent()) {
                throw new RuntimeException(MethodParamVO.NOT_SUPPORT_PARAMETER_NAME);
            }
            String paramClassName = MethodParamVO.PARAMVO_PACKAGE + SourceCodeUtils.getClassSuffix(method, GenerateClassType.VO, null);
            try {
                Class<?> clazz = MethodParamVOClassFactory.class.getClassLoader().loadClass(paramClassName);
                if (clazz != null) {
                    return (Class<? extends MethodParamVO>) clazz;
                }
            } catch (ClassNotFoundException e) {
            }
            // 创建类
            ClassPool pool = ClassPool.getDefault();
            CtClass paramVOClass = pool.makeClass(paramClassName);
            CtClass[] interfaces = {pool.getCtClass(MethodParamVO.class.getName())};
            paramVOClass.setInterfaces(interfaces);
            for (int i = 0; i < parameterTypes.length; i++) {
                Parameter parameter = parameters[i];
                String paramName = parameter.getName();
                Class<?> paramType = parameterTypes[i];
                String capitalize = Character.toUpperCase(paramName.charAt(0)) + paramName.substring(1);
                String getter = "get" + capitalize;
                String setter = "set" + capitalize;
                CtField ctField = new CtField(pool.get(paramType.getName()), paramName, paramVOClass);
                ctField.setModifiers(Modifier.PRIVATE);
                paramVOClass.addField(ctField);
                paramVOClass.addMethod(CtNewMethod.getter("$param" + i, ctField));
                paramVOClass.addMethod(CtNewMethod.getter(getter, ctField));
                paramVOClass.addMethod(CtNewMethod.setter(setter, ctField));
            }
            // 添加无参的构造函数
            CtConstructor constructor0 = new CtConstructor(null, paramVOClass);
            constructor0.setModifiers(Modifier.PUBLIC);
            constructor0.setBody("{}");
            paramVOClass.addConstructor(constructor0);
            // 添加有参的构造函数
            CtClass[] paramCtClassArray = new CtClass[method.getParameterCount()];
            for (int i = 0; i < method.getParameterCount(); i++) {
                Class<?> paramType = parameterTypes[i];
                CtClass paramCtClass = pool.get(paramType.getName());
                paramCtClassArray[i] = paramCtClass;
            }
            StringBuilder bodyBuilder = new StringBuilder();
            bodyBuilder.append("{\r\n");
            for (int i = 0; i < method.getParameterCount(); i++) {
                String paramName = parameters[i].getName();
                bodyBuilder.append("$0.");
                bodyBuilder.append(paramName);
                bodyBuilder.append(" = $");
                bodyBuilder.append(i + 1);
                bodyBuilder.append(";\r\n");
            }
            bodyBuilder.append("}");
            CtConstructor constructor1 = new CtConstructor(paramCtClassArray, paramVOClass);
            constructor1.setBody(bodyBuilder.toString());
            paramVOClass.addConstructor(constructor1);
            if (System.getProperty(MethodParamVO.CLASS_DEBUG_OUTPUT) != null) {
                paramVOClass.writeFile(MethodParamVO.CLASS_OUTPUT_DIR);
            }
            return (Class<? extends MethodParamVO>) paramVOClass.toClass();
        }
    }

    public static interface Invoker<T> {
        String INVOKER_PACKAGE_FORMAT = "com.github.common.generate.invoke.%s";

        T invoke(Object... params);

        T invoke(MethodParamVO methodParamVO);

        default T invoke() {
            return invoke(new Object[]{});
        }

        default T invoke(Object param0) {
            return invoke(new Object[]{param0});
        }

        default T invoke(Object param0, Object param1) {
            return invoke(new Object[]{param0, param1});
        }

        default T invoke(Object param0, Object param1, Object param2) {
            return invoke(new Object[]{param0, param1, param2});
        }

        default T invoke(Object param0, Object param1, Object param2, Object param3) {
            return invoke(new Object[]{param0, param1, param2, param3});
        }

        default T invoke(Object param0, Object param1, Object param2, Object param3, Object param4) {
            return invoke(new Object[]{param0, param1, param2, param3, param4});
        }

        default T invoke(Object param0, Object param1, Object param2, Object param3, Object param4, Object param5) {
            return invoke(new Object[]{param0, param1, param2, param3, param4, param5});
        }
    }

    public static class SingleClassLoader extends ClassLoader {
        private final Class<?> clazz;

        public SingleClassLoader(ClassLoader parent, byte[] bytes) {
            super(parent);
            this.clazz = defineClass(null, bytes, 0, bytes.length, null);
        }

        public Class<?> getClazz() {
            return clazz;
        }

        @Override
        public Class<?> loadClass(String name) throws ClassNotFoundException {
            if (clazz != null && clazz.getName().equals(name)) {
                return clazz;
            }
            return getParent().loadClass(name);
        }

        public static <T> Class<T> loadClass(byte[] bytes) {
            ClassLoader parent = SingleClassLoader.class.getClassLoader();
            return loadClass(parent, bytes);
        }

        private static <T> Class<T> loadClass(ClassLoader parent, byte[] bytes) {
            return (Class<T>) new SingleClassLoader(parent, bytes).getClazz();
        }
    }

    public static class JavassistInvoker<T> implements Invoker<T> {
        private final int serviceId;
        private final Object service;
        private final Class<?> clazz;
        private final Method method;
        private final Class<?>[] parameterTypes;
        private final String[] parameterNames;
        private final int parameterCount;
        final Class<? extends MethodParamVO> methodParamClass;
        private final Invoker<T> realInvoker;

        public JavassistInvoker(int serviceId, Object service, Class<?> clazz, Method method) {
            this.serviceId = serviceId;
            this.service = service;
            this.clazz = clazz;
            this.method = method;
            this.parameterTypes = method.getParameterTypes();
            this.parameterCount = parameterTypes.length;
            parameterNames = new String[parameterCount];
            Parameter[] parameters = method.getParameters();
            for (int i = 0; i < parameterCount; i++) {
                Parameter parameter = parameters[i];
                if (!parameter.isNamePresent()) {
                    throw new RuntimeException(MethodParamVO.NOT_SUPPORT_PARAMETER_NAME);
                }
                parameterNames[i] = parameter.getName();
            }
            try {
                methodParamClass = MethodParamVOClassFactory.createClass(method);
                this.realInvoker = generateRealInvoker();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public T invoke(Object... params) {
            if (params == null) {
                if (parameterCount != 0) {
                    throw new RuntimeException(method.getName() + " params count error, params is null");
                }
            } else if (parameterCount != params.length) {
                throw new RuntimeException(method.getName() + " params count error, " + Arrays.toString(params));
            }
            return realInvoker.invoke(params);
        }

        @Override
        public T invoke(MethodParamVO methodParamVO) {
            if (methodParamVO == null || methodParamVO instanceof EmptyMethodParamVO) {
                return realInvoker.invoke();
            }
            if (!methodParamClass.isInstance(methodParamVO)) {
                throw new IllegalArgumentException("methodParam not instanceof " + methodParamClass.getName());
            }
            return realInvoker.invoke(methodParamVO);
        }

        private Invoker<T> generateRealInvoker() {
            String invokerClassName = String.format(INVOKER_PACKAGE_FORMAT, SourceCodeUtils.getClassSuffix(this.method, GenerateClassType.Proxy, serviceId));
            ClassPool pool = ClassPool.getDefault();
            try {
                CtClass invokerCtClass = pool.makeClass(invokerClassName);
                invokerCtClass.setInterfaces(new CtClass[]{pool.getCtClass(Invoker.class.getName())});
                // 添加私有成员service
                CtField serviceField = new CtField(pool.get(service.getClass().getName()), "service", invokerCtClass);
                serviceField.setModifiers(Modifier.PRIVATE | Modifier.FINAL);
                invokerCtClass.addField(serviceField);
                // 添加有参的构造函数
                CtConstructor constructor = new CtConstructor(new CtClass[]{pool.get(service.getClass().getName())},
                        invokerCtClass);
                constructor.setBody("{$0.service = $1;}");
                invokerCtClass.addConstructor(constructor);
                {// 添加MethodParam方法
                    StringBuilder methodBuilder = new StringBuilder();
                    StringBuilder resultBuilder = new StringBuilder();
                    methodBuilder.append("public Object invoke(com.github.common.bean.Javassist_unit.MethodParamVO methodParam) {\r\n");
                    if (parameterTypes.length > 0) {
                        // 强制类型转换
                        methodBuilder.append(methodParamClass.getName());
                        methodBuilder.append("  params = (");
                        methodBuilder.append(methodParamClass.getName());
                        methodBuilder.append(")methodParam;");
                    }
                    methodBuilder.append("  return ");
                    resultBuilder.append("service.");
                    resultBuilder.append(method.getName());
                    resultBuilder.append("(");
                    for (int i = 0; i < parameterTypes.length; i++) {
                        resultBuilder.append("params.$param");
                        resultBuilder.append(i);
                        resultBuilder.append("()");
                        if (i != parameterTypes.length - 1) {
                            resultBuilder.append(", ");
                        }
                    }
                    resultBuilder.append(")");
                    String resultStr = SourceCodeUtils.box(method.getReturnType(), resultBuilder.toString());
                    methodBuilder.append(resultStr);
                    methodBuilder.append(";\r\n}");
                    invokerCtClass.addMethod(make(methodBuilder.toString(), invokerCtClass));
                }
                {// 添加通用方法
                    StringBuilder methodBuilder = new StringBuilder();
                    StringBuilder resultBuilder = new StringBuilder();
                    methodBuilder.append("public Object invoke(Object[] params) {\r\n");
                    methodBuilder.append("  return ");
                    resultBuilder.append("service.");
                    resultBuilder.append(method.getName());
                    resultBuilder.append("(");
                    for (int i = 0; i < parameterTypes.length; i++) {
                        Class<?> paramType = parameterTypes[i];
                        resultBuilder.append("((");
                        resultBuilder.append(SourceCodeUtils.forceCast(paramType));
                        resultBuilder.append(")params[");
                        resultBuilder.append(i);
                        resultBuilder.append("])");
                        resultBuilder.append(SourceCodeUtils.unbox(paramType));
                        if (i != parameterTypes.length - 1) {
                            resultBuilder.append(", ");
                        }
                    }
                    resultBuilder.append(")");
                    String resultStr = SourceCodeUtils.box(method.getReturnType(), resultBuilder.toString());
                    methodBuilder.append(resultStr);
                    methodBuilder.append(";\r\n}");
                    invokerCtClass.addMethod(make(methodBuilder.toString(), invokerCtClass));
                }
                if (parameterCount <= 6) {// just for benchmark
                    StringBuilder methodBuilder = new StringBuilder();
                    StringBuilder resultBuilder = new StringBuilder();
                    methodBuilder.append("public Object invoke(");
                    String params = IntStream.range(0, parameterCount).mapToObj(i -> "Object param" + i).collect(Collectors.joining(","));
                    methodBuilder.append(params);
                    methodBuilder.append(") {\r\n");
                    methodBuilder.append("  return ");
                    resultBuilder.append("service.");
                    resultBuilder.append(method.getName());
                    resultBuilder.append("(");
                    for (int i = 0; i < parameterCount; i++) {
                        Class<?> paramType = parameterTypes[i];
                        resultBuilder.append("((");
                        resultBuilder.append(SourceCodeUtils.forceCast(paramType));
                        resultBuilder.append(")param");
                        resultBuilder.append(i);
                        resultBuilder.append(")");
                        resultBuilder.append(SourceCodeUtils.unbox(paramType));
                        if (i != parameterCount - 1) {
                            resultBuilder.append(",");
                        }
                    }
                    resultBuilder.append(")");
                    String resultStr = SourceCodeUtils.box(method.getReturnType(), resultBuilder.toString());
                    methodBuilder.append(resultStr);
                    methodBuilder.append(";\r\n}");
                    invokerCtClass.addMethod(make(methodBuilder.toString(), invokerCtClass));
                }
                if (System.getProperty(MethodParamVO.CLASS_DEBUG_OUTPUT) != null) {
                    invokerCtClass.writeFile(MethodParamVO.CLASS_OUTPUT_DIR);
                }
                byte[] bytes = invokerCtClass.toBytecode();
                Class<?> invokerClass = SingleClassLoader.loadClass(service.getClass().getClassLoader(), bytes);
                Invoker<T> invoker = (Invoker<T>) invokerClass.getConstructor(service.getClass()).newInstance(service);
                return invoker;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private CtMethod make(String methodDes, CtClass declaring) {
            try {
                return CtNewMethod.make(methodDes, declaring);
            } catch (Exception ex) {
                System.out.println("make method fail,methodDes=" + methodDes + ",declaring=" + declaring);
                throw new RuntimeException(ex);
            }
        }

        @Override
        public int hashCode() {
            return serviceId;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            JavassistInvoker<?> other = (JavassistInvoker<?>) obj;
            if (serviceId != other.serviceId) {
                return false;
            }
            return true;
        }
    }

    public static class ServerInvokerFactory {
        private final ConcurrentArrayList<JavassistInvoker<?>> invokerMap = new ConcurrentArrayList<>();

        public <T> Invoker<T> get(int serviceId) {
            return (Invoker<T>) invokerMap.get(serviceId);
        }

        public synchronized void register(Class<?> clazz, Object service) {
            List<JavassistInvoker<?>> invokers = genrateInvokers(clazz, service);
            if (invokers != null && !invokers.isEmpty()) {
                invokerMap.addAll(invokers);
            }
        }

        private List<JavassistInvoker<?>> genrateInvokers(Class<?> clazz, Object service) {
            Objects.requireNonNull(clazz, "clazz is required.");
            Objects.requireNonNull(service, "service is required.");
            if (!clazz.isInterface()) {
                throw new RuntimeException("the clazz must be interface");
            }
            if (!Modifier.isPublic(clazz.getModifiers())) {
                throw new RuntimeException("the clazz must be public");
            }
            if (!clazz.isInstance(service)) {
                throw new RuntimeException("clazz is the interface, service is the implement");
            }
            Method[] allMethods = clazz.getDeclaredMethods();
            Stream<Method> methodStream = Stream
                    .of(allMethods)
                    .filter(m -> Modifier.isPublic(m.getModifiers()))//
                    .filter(m -> !Modifier.isStatic(m.getModifiers()))//
                    .peek(m -> {
//                        if (!CompletableFuture.class.equals(m.getReturnType())) {
//                            throw new RuntimeException("method return-type must be CompletableFuture, " + getServiceMethodName(m));
//                        }
                    })
                    .filter(m -> m.getAnnotation(Deprecated.class) == null ? true : false);
            AtomicInteger serviceIdCounter = new AtomicInteger(invokerMap.size());
            return methodStream.map(m -> {
                int serviceId = serviceIdCounter.getAndIncrement();
                return new JavassistInvoker<>(serviceId, service, m.getDeclaringClass(), m);
            }).collect(Collectors.toList());
        }

        private String getServiceMethodName(Method method) {
            Class<?>[] parameterTypes = method.getParameterTypes();
            String params = Stream.of(parameterTypes).map(clazz -> clazz.getName()).collect(Collectors.joining(",", "(", ")"));
            return method.getDeclaringClass().getName() + "#" + method.getName() + "#" + params;
        }
    }

    public static class SourceCodeUtils {

        public static String forceCast(Class<?> clazz) {
            if (int.class.equals(clazz)) {
                return Integer.class.getName();
            }

            if (long.class.equals(clazz)) {
                return Long.class.getName();
            }

            if (boolean.class.equals(clazz)) {
                return Boolean.class.getName();
            }

            if (double.class.equals(clazz)) {
                return Double.class.getName();
            }

            if (float.class.equals(clazz)) {
                return Float.class.getName();
            }

            if (short.class.equals(clazz)) {
                return Short.class.getName();
            }

            if (byte.class.equals(clazz)) {
                return Byte.class.getName();
            }

            if (char.class.equals(clazz)) {
                return Character.class.getName();
            }

            return clazz.getName();
        }

        public static String box(Class<?> clazz, String value) {
            if (int.class.equals(clazz)) {
                return Integer.class.getName() + ".valueOf(" + value + ")";
            }

            if (long.class.equals(clazz)) {
                return Long.class.getName() + ".valueOf(" + value + ")";
            }

            if (boolean.class.equals(clazz)) {
                return Boolean.class.getName() + ".valueOf(" + value + ")";
            }

            if (double.class.equals(clazz)) {
                return Double.class.getName() + ".valueOf(" + value + ")";
            }

            if (float.class.equals(clazz)) {
                return Float.class.getName() + ".valueOf(" + value + ")";
            }

            if (short.class.equals(clazz)) {
                return Short.class.getName() + ".valueOf(" + value + ")";
            }

            if (byte.class.equals(clazz)) {
                return Byte.class.getName() + ".valueOf(" + value + ")";
            }

            if (char.class.equals(clazz)) {
                return Character.class.getName() + ".valueOf(" + value + ")";
            }

            return value;
        }

        public static String unbox(Class<?> clazz) {
            if (int.class.equals(clazz)) {
                return ".intValue()";
            }

            if (long.class.equals(clazz)) {
                return ".longValue()";
            }

            if (boolean.class.equals(clazz)) {
                return ".booleanValue()";
            }

            if (double.class.equals(clazz)) {
                return ".doubleValue()";
            }

            if (float.class.equals(clazz)) {
                return ".floatValue()";
            }

            if (short.class.equals(clazz)) {
                return ".shortValue()";
            }

            if (byte.class.equals(clazz)) {
                return ".byteValue()";
            }

            if (char.class.equals(clazz)) {
                return ".charValue()";
            }

            return "";
        }

        /**
         * 坑：最新版本的javassist，文件名不能使用$,否则生成时Proxy会需要导入的VO中$替换为.
         * 如：EchoService$VO$echo$2b697b0dd，变成了EchoService.VO.echo.2b697b0dd，
         *
         * @param method
         * @param type
         * @param serviceId
         * @return
         */
        public static String getClassSuffix(Method method, GenerateClassType type, Integer serviceId) {
            Class<?>[] parameterTypes = method.getParameterTypes();
            String paramTypes = Stream.of(parameterTypes).map(clazz -> clazz.getName()).collect(Collectors.joining(",", "(", ")"));
            String hash = Hashing.murmur3_32().hashString(paramTypes, Charset.defaultCharset()).toString();
            return String.format("%s_%s%s_%s_%s%s", method.getDeclaringClass().getSimpleName(), type.toString(), serviceId == null ? "" : serviceId.toString(), method.getName(), parameterTypes.length, hash);
        }
    }

    public static enum GenerateClassType {
        VO,
        Proxy
    }
}
