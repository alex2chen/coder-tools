##1.servlet中的filter
servlet中分别定义了一个 Filter和FilterChain的接口，核心代码如下：
```java    
    public final class ApplicationFilterChain implements FilterChain {
        private int pos = 0; //当前执行filter的offset
        private int n; //当前filter的数量
        private ApplicationFilterConfig[] filters;  //filter配置类，通过getFilter()方法获取Filter
        private Servlet servlet

        @Override
        public void doFilter(ServletRequest request, ServletResponse response) {
            if (pos < n) {
                ApplicationFilterConfig filterConfig = filters[pos++];
                Filter filter = filterConfig.getFilter();
                filter.doFilter(request, response, this);
            } else {
                // filter都处理完毕后，执行servlet
                servlet.service(request, response);
            }
        }

    }
```
##2.dubbo中的filter
Dubbo在创建Filter的时候是另外一个方法，通过把Filter封装成 Invoker的匿名类，通过链表这样的数据结构来完成责任链，
```java 
    private static <T> Invoker<T> buildInvokerChain(final Invoker<T> invoker, String key, String group) {
        Invoker<T> last = invoker;
        //只获取满足条件的Filter
        List<Filter> filters = ExtensionLoader.getExtensionLoader(Filter.class).getActivateExtension(invoker.getUrl(), key, group);
        if (filters.size() > 0) {
            for (int i = filters.size() - 1; i >= 0; i --) {
                final Filter filter = filters.get(i);
                final Invoker<T> next = last;
                last = new Invoker<T>() {
                    ...
                    public Result invoke(Invocation invocation) throws RpcException {
                        return filter.invoke(next, invocation);
                    }
                    ...
                };
            }
        }
        return last;
    }
```    
##3.mybatis中的plugin
Mybatis可以配置各种Plugin，无论是官方提供的还是自己定义的，Plugin和Filter类似，就在执行Sql语句的时候做一些操作。Mybatis的责任链则是通过动态代理的方式，使用Plugin代理实际的Executor类。（这里实际还使用了组合模式，因为Plugin可以嵌套代理）
```java 
    public class Plugin implements InvocationHandler{
        private Object target;
        private Interceptor interceptor;
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (满足代理条件) {
                return interceptor.intercept(new Invocation(target, method, args));
            }
            return method.invoke(target, args);
        }

        //对传入的对象进行代理，可能是实际的Executor类，也可能是Plugin代理类
        public static Object wrap(Object target, Interceptor interceptor) {

            Class<?> type = target.getClass();
            Class<?>[] interfaces = getAllInterfaces(type, signatureMap);
            if (interfaces.length > 0) {
                return Proxy.newProxyInstance(
                        type.getClassLoader(),
                        interfaces,
                        new Plugin(target, interceptor, signatureMap));
            }
            return target;
        }
    }  
```