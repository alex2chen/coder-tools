# 引用
[一览表](http://www.oschina.net/news/20119/new-features-of-java-7)
## 1.switch
    也可以接受string类型
## 2.catch
    异常catch可以一次处理完而不像以前一层层的surround
```java
catch (ExceptionOne | ExceptionTwo | ExceptionThree e) {
         }
```
## 3.自动资源管理Closeable
    Java中某些资源是需要手动关闭的如InputStream/Writes/Sockets/Sql classes等,这些资源作用于try代码块，并自动关闭(实现Closeable自动释放资源)
```java
    try(FileOutputStream fos = new FileOutputStream("movies.txt");DataOutputStream dos = new DataOutputStream(fos)){
       }catch (IOException ex){
            ex.printStackTrace();
     }
```
## 4.泛型类简化
    泛型类实例化也不用繁琐的将泛型声明再写一遍,运算符从引用的声明中推断类型
    Map<String, List<Trade>> trades = new TreeMap <> ();
    Map<String, List<String>> anagrams = new HashMap<>();
## 5.数值可以使用下划线分隔
    int million  =  1_000_000
## 6.声明二进制字面值
    int value2=0b10111001;
    int binary = 0b1001_1001;
    byte aByte = (byte)0b001;
    short aShort = (short)0b010;
## 7.文件读写功能增强及改变事件通知
    java.nio.file下Path、Paths、Files、WatchService、FileSystem
    FileSystems.getDefault().newWatchService
## 8.多核并行计算的支持加强 fork join
    ForkJoinPool pool = new ForkJoinPool(numberOfProcessors);
## 9.动态特性的加入
    java.lang.invoke 包的引入, MethodHandle/CallSite 还有一些其他类供使用

