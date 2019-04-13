## 引用
[十大特性](http://www.importnew.com/11908.html)

### 1.接口的默认与静态方法
默认方法的实现是非常高效的，通过字节码指令为方法调用提供了支持
java.util.Collection接口中去：stream(),parallelStream(),forEach(),removeIf()……
```java
private interface Defaulable {
    // may not implement (override) them.
    default String notRequired() {
        return "Default implementation";
    }
}
private interface DefaulableFactory {
    // Interfaces now allow static methods
    static Defaulable create( Supplier< Defaulable > supplier ) {
        return supplier.get();
    }
}
```
### 2.Lambda表达式与Functional接口
#### 2.1 Lambda表达式
    也称为闭包,允许把函数作为一个方法的参数,语法：->
    lambda引用类的成员变量与局部变量（如果这些变量不是final的话,它们会被隐含的转为final,这样效率更高）;
#### 2.2 函数式接口
    为了友好地支持lambda,增加函数式接口的概念(只有一个方法的普通接口),java8中增加@FunctionalInterface标记
    函数式接口最典型有java.lang.Runnable与java.util.concurrent.Callable
#### 2.3 方法引用
    为了直接能引用已有Java类/对象的方法或构造器而设计(紧凑简洁，减少冗余代码),语法有：::或::new
    构造器引用：它的语法是Class::new
    静态方法引用：它的语法是Class::static_method
    特定类的任意对象的方法引用，它的语法是Class::method
    特定对象的方法引用，它的语法是instance::method
    注意：该语法适用于方法的参数是函数式接口
### 3.Stream
    Stream API（java.util.stream）把真正的函数式编程风格引入到Java中. Map类型不支持stream
        Predicate接口： 返回boolean类型
        Function 接口： 有一个参数并且返回一个结果
        Supplier 接口： 返回一个任意范型的值
        Consumer 接口： 执行在单个参数上的操作

        Stream():   分为中间操作或者最终操作两种，java.util.Collection的子类，List或者Set， Map不支持。Stream的操作可以串行执行或者并行执行
        parallelStream()：  Streams并行运算

        Filter()：      属于中间操作，返回Stream
        Sort()：        属于中间操作，返回Stream
        Map()：         属于中间操作，将元素根据指定的Function接口来依次将元素转成另外的对象

        forEach()：     属于最终操作
        Match()：       属于最终操作，返回boolean
        Count()：       属于最终操作，返回long
        Reduce()：      属于最终操作，规约,将多个元素规约为一个元素，返回Optional
        allMatch：       属于最终操作，中全部元素符合传入的 predicate，返回 true
        anyMatch：       属于最终操作，中只要有一个元素符合传入的 predicate，返回 true
        noneMatch：      属于最终操作，中没有一个元素符合传入的 predicate，返回 true
### 4.Optional
    简单的容器(其值可能是null或不是)、防止NullPointerException异常的辅助类型、Java 8中推荐此做法
### 5.重复注解@Repeatable
### 6.Date/Time API
    对日期与时间的操作一直是Java程序员最痛苦的地方之,和开源的Joda-Time库差不多
    Clock 时钟、Timezones 时区、LocalTime 本地时间、LocalDate 本地日期、LocalDateTime 本地日期时间
### 7.JavaScript引擎Nashorn
    允许在JVM上开发运行某些JavaScript应用,同时提供命令行工具: jjs
```java
ScriptEngineManager manager = new ScriptEngineManager();
ScriptEngine engine = manager.getEngineByName( "JavaScript" );
System.out.println( engine.getClass().getName() );//jdk.nashorn.api.scripting.NashornScriptEngine
System.out.println( "Result:" + engine.eval( "function f() { return 1; }; f() + 1;" ) );//Result: 2
```
### 8.Base64
    在Java8中，Base64编码已经成为Java类库的标准,同时还提供了对URL、MIME友好的编码器与解码器
### 9.并行（parallel）数组
    Arrays.parallelSetAll
    Arrays.parallelSort
### 9.并发（Concurrency）
    java.util.concurrent.locks.StampedLock类提供一直基于容量的锁
    java.util.concurrent.atomic包DoubleAccumulator,LongAccumulator
### 10.类依赖分析器jdeps
    jdeps是一个很有用的命令行工具。它可以显示Java类的包级别或类级别的依赖。它接受一个.class文件，一个目录，或者一个jar文件作为输入。jdeps默认把结果输出到系统输出（控制台）上
### 11.Java虚拟机的新特性
    PermGen空间被移除了,取而代之的是Metaspace