package com.github.common.rxjava;

import com.google.common.base.Stopwatch;
import org.junit.Test;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/6/13.
 */
public class ReactiveX_1x_test {
    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), 10, 300,
            TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(500), new NamedThreadFactory("aggrege"), new ThreadPoolExecutor.AbortPolicy());

    @Test
    public void just() {
        // 方法1：just(T...)：直接将传入的参数依次发送出来
        Observable.just("a", "b").subscribe(x -> System.out.println(x));
    }

    @Test
    public void from() {
        // 方法2：from(T[])
        String[] words = {"A", "B", "C"};
        Observable.from(words).subscribe(x -> System.out.println(x));
    }

    @Test
    public void create() throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + ",当前线程");
        Observable.OnSubscribe<String> onSubscribe = new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                System.out.println(Thread.currentThread().getName() + ",create");
                subscriber.onNext("On");
                subscriber.onNext("Off");
                subscriber.onCompleted();
            }
        };
        //创建被观察者（也就是开关）：
        Observable switcherObservable = Observable.create(onSubscribe);
        //创建观察者（也就是台灯）
        Subscriber lightSubscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                //被观察者的onCompleted()事件会走到这里;
                System.out.println(Thread.currentThread().getName() + " 结束观察...\n");
            }

            @Override
            public void onError(Throwable e) {
                //出现错误会调用这个方法
            }

            @Override
            public void onNext(String s) {
                //处理传过来的onNext事件
                System.out.println(Thread.currentThread().getName() + ",onNext:" + s);
            }
        };
        switcherObservable.subscribeOn(Schedulers.io()).subscribe(lightSubscriber);
        lightSubscriber.unsubscribe();
        System.out.println("end.............");

        Stopwatch stopwatch = Stopwatch.createStarted();
        AtomicInteger integer = new AtomicInteger(8);
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                System.out.println(Thread.currentThread().getName() + ",create ");
                subscriber.onNext("dd");
                subscriber.onNext("ee");
                subscriber.onNext("1");
                subscriber.onNext("w");
                subscriber.onNext("e1");
                subscriber.onNext("35");
                subscriber.onNext("5");
                subscriber.onNext("8");
            }
        })
                .doOnNext(x -> System.out.println(Thread.currentThread().getName() + ",before," + x))
                .observeOn(Schedulers.from(threadPoolExecutor))//你的observer如何收到事件，也就是在那个线程中回调observer的 onNext/onError/onCompleted 函数，它只影响后面一个函数
                .doOnNext(x -> System.out.println(Thread.currentThread().getName() + ",after," + x))
                //.subscribeOn(Schedulers.io())//它决定create中的代码在那个 Scheduler 中执行。即使你没有调用 create 函数，但是内部也有一个 create 实现
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println(Thread.currentThread().getName() + ",rx--subscribe ");
                        integer.decrementAndGet();
                    }
                });
        System.out.println(integer.get());
        while (integer.get() > 0) {
        }
        System.out.println("ok!" + stopwatch);
        //Thread.sleep(5000);
    }

    //lift方法很重要，很多类似map() flatMap()等都是通过它实现的
    @Test
    public void lift() {
        Observable.just(1).lift(new Observable.Operator<String, Integer>() {
            @Override
            public Subscriber<? super Integer> call(final Subscriber<? super String> subscriber) {
                // 将事件序列中的 Integer 对象转换为 String 对象
                return new Subscriber<Integer>() {
                    @Override
                    public void onNext(Integer integer) {
                        subscriber.onNext("x" + integer);
                    }

                    @Override
                    public void onCompleted() {
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        subscriber.onError(e);
                    }
                };
            }
        }).subscribe(x -> System.out.println(x));
    }

    //使用 ObservableX.this时，它在内部成员变量中访问时，访问的不是当前作用域的地址
    @Test
    public void thisObj() {
        ObservableX observableX = new ObservableX(str -> System.out.println("hello"));
        observableX.display();
        ObservableX lift = observableX.lift();
        lift.display();
        lift.action.call("world");
    }

    @Test
    public void timerRun() throws InterruptedException {
        //2秒后输出日志“hello world”，然后结束。
//        Observable.timer(2, TimeUnit.SECONDS)
        Observable.interval(2, TimeUnit.SECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("error");
                    }

                    @Override
                    public void onNext(Long number) {
                        System.out.println("hello");
                    }
                });
        Thread.sleep(5000);
    }

    @Test
    public void concatFirst() {
        String memoryCache = "x";
        Observable<String> memory = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                if (memoryCache != null) {
                    subscriber.onNext(memoryCache);
                } else {
                    subscriber.onCompleted();
                }
            }
        });
        String diskData = "1";
        Observable<String> disk = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                if ("1".equals(diskData)) {
                    subscriber.onNext(diskData);
                } else {
                    subscriber.onCompleted();
                }
            }
        });
        Observable<String> network = Observable.just("network");
        //依次检查memory、disk、network
        Observable.concat(memory, disk, network).first().subscribe(s -> System.out.println("--------------subscribe: " + s));

    }

    @Test
    public void throttleFirstAndDebounce() {
        Observable<Integer> tObservable = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                try {
                    subscriber.onNext(1);
                    Thread.sleep(400);
                    subscriber.onNext(2);
                    Thread.sleep(505);
                    subscriber.onNext(3);
                    Thread.sleep(100);
                    subscriber.onNext(4);
                    Thread.sleep(450);
                    subscriber.onNext(5);
                    Thread.sleep(510);
                    subscriber.onCompleted();
                } catch (InterruptedException e) {
                    subscriber.onError(e);
                }
            }
        });
        //输出2,5
        tObservable.debounce(500, TimeUnit.MILLISECONDS)// 设置时间为0.5秒
                .subscribe(x -> System.out.println(x));
        System.out.println("throttleFirst:");
        //输出1,3,5
        tObservable.throttleFirst(500, TimeUnit.MILLISECONDS)// 设置时间为0.5秒
                .subscribe(x -> System.out.println(x));
    }

    @Test
    public void simplestAty() {
        //有Action0、Action1...到Action9
        Action1<String> onNextAction = new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println(s);
            }
        };
        Action1<Throwable> onErrorAction = new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                //错误处理
            }
        };
        Action0 onCompleteAction = new Action0() {
            @Override
            public void call() {
                System.out.println("completed");
            }
        };
        Observable observable = Observable.just("hello");
        observable.subscribe(onNextAction, onErrorAction, onCompleteAction);
    }

    @Test
    public void schedulePeriodically() {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                Schedulers.io().createWorker()  //指定在io线程执行
                        .schedulePeriodically(new Action0() {
                            @Override
                            public void call() {
                                subscriber.onNext("doNetworkCallAndGetStringResult");
                            }
                        }, 2000, 1000, TimeUnit.MILLISECONDS);//初始延迟，polling延迟
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println("polling..." + s);
            }
        });
    }

    @Test
    public void merge() {
        Observable.merge(getDataFromFile(), getDataFromNet())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("done loading all data");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("error");
                    }

                    @Override
                    public void onNext(String s) {
                        System.out.println("merge:" + s);
                    }
                });
    }

    private Observable<String> getDataFromFile() {
        String[] strs = {"filedata1", "filedata2", "filedata3", "filedata4"};
        //Thread.sleep(1000);
        Observable<String> temp = Observable.from(strs);
        return temp;
    }

    private Observable<String> getDataFromNet() {
        String[] strs = {"netdata1", "netdata2", "netdata3", "netdata4"};
        Observable<String> temp = Observable.from(strs);
        return temp;
    }

    public class ObservableX {
        Action1<String> action;

        public ObservableX(Action1 action) {
            this.action = action;
        }

        public void display() {
            System.out.println(ObservableX.this.action);
            System.out.println(action);
        }

        public ObservableX lift() {
            return new ObservableX(new Action1<String>() {
                @Override
                public void call(String s) {
                    System.out.println(ObservableX.this.action);
                }
            });
        }
    }

    public static class NamedThreadFactory implements ThreadFactory {
        private final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        public NamedThreadFactory(String prefixName) {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            namePrefix = prefixName + "-pool-" + poolNumber.getAndIncrement() + "-thread-";
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0);
            if (t.isDaemon()) {
                t.setDaemon(false);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    }

}