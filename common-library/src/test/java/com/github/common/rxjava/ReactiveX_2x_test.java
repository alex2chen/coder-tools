package com.github.common.rxjava;

import com.alibaba.fastjson.JSON;
import com.beust.jcommander.internal.Maps;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import io.reactivex.*;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.*;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/8/13.
 */
public class ReactiveX_2x_test {


    @Test
    public void create() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            // 1. 创建被观察者 & 生产事件
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                System.out.println("subscribe传递");
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onComplete();
            }
        }).subscribe(new Observer<Integer>() {
            // 2. 通过通过订阅（subscribe）连接观察者和被观察者
            // 3. 创建观察者 & 定义响应事件的行为
            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("开始采用subscribe连接");
            }

            // 默认最先调用复写的 onSubscribe（）
            @Override
            public void onNext(Integer value) {
                System.out.println("对Next事件" + value + "作出响应");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("对Error事件作出响应");
            }

            @Override
            public void onComplete() {
                System.out.println("对Complete事件作出响应");
            }
        });

        Flowable<Integer> flowable = Flowable.fromIterable(() -> getList().iterator());
        flowable.subscribe(x -> System.out.println(x));
    }

    private List<Integer> getList() {
        return Lists.newArrayList(1, 23);
    }

    @Test
    public void signgleOperator() {
        Single.create(new SingleOnSubscribe() {
            @Override
            public void subscribe(@NonNull SingleEmitter singleEmitter) throws Exception {
                singleEmitter.onSuccess("hello");
                //singleEmitter.onError(new Exception("测试异常"));
            }
        }).subscribe(new SingleObserver() {
            @Override
            public void onSubscribe(@NonNull Disposable disposable) {
                System.out.println("onSubscribe");
            }

            @Override
            public void onSuccess(@NonNull Object o) {
                System.out.println("onSuccess");
            }

            @Override
            public void onError(@NonNull Throwable throwable) {
                System.out.println("onError");

            }
        });
    }

    @Test
    public void completable() {
        Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter completableEmitter) throws Exception {
                //completableEmitter.onComplete();
                completableEmitter.onError(new Exception("测试异常"));
            }
        }).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(@NonNull Disposable disposable) {
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }

            @Override
            public void onError(@NonNull Throwable throwable) {
                System.out.println("onError");
            }
        });
    }

    @Test
    public void maybe() {
        Maybe.create(new MaybeOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull MaybeEmitter<String> maybeEmitter) throws Exception {
                //maybeEmitter.onSuccess("suc");
                maybeEmitter.onError(new Exception("测试异常"));
                //maybeEmitter.onComplete();
            }
        }).subscribe(new MaybeObserver<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable disposable) {
            }

            @Override
            public void onSuccess(@NonNull String s) {
                System.out.println("onSuccess");
            }

            @Override
            public void onError(@NonNull Throwable throwable) {
                System.out.println("onError");
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        });
    }

    @Test
    public void zip() {
        //专用于合并事件(取决于发送器发送事件最少)
        Observable.zip(Observable.just("a", "b", "c"), Observable.fromArray(1, 2, 3, 4, 5, 7), new BiFunction<String, Integer, String>() {
            @Override
            public String apply(@NonNull String s, @NonNull Integer integer) throws Exception {
                return s + integer;
            }
        }).subscribe(x -> System.out.println(x));
    }

    @Test
    public void scheduler() throws InterruptedException {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> subscriber) throws Exception {
                System.out.println("create -  当前线程信息：" + Thread.currentThread().getName());
                subscriber.onNext(20);
                subscriber.onNext(3);
                subscriber.onNext(3);
                subscriber.onComplete();
            }
        }).subscribeOn(Schedulers.computation()).observeOn(Schedulers.io())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable disposable) {
                        System.out.println("onSubscribe - 当前线程信息：" + Thread.currentThread().getName());
                    }

                    @Override
                    public void onNext(@NonNull Integer integer) {
                        System.out.println("onNext - 当前线程信息：" + Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(@NonNull Throwable throwable) {
                        System.out.println("onError - 当前线程信息：" + Thread.currentThread().getName());
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete - 当前线程信息：" + Thread.currentThread().getName());
                    }
                });
        Thread.sleep(5000);
    }


    @Test
    public void netRequest() throws InterruptedException {
        Observable.create(new ObservableOnSubscribe<Response>() {
                              @Override
                              public void subscribe(@NonNull ObservableEmitter<Response> e) throws Exception {
                                  System.out.println("create 线程:" + Thread.currentThread().getName());
                                  Request request = new Request.Builder().url("http://api.avatardata.cn/MobilePlace/LookUp?key=ec47b85086be4dc8b5d941f5abd37a4e&mobileNumber=13021671512").build();
                                  Call call = new OkHttpClient().newCall(request);
                                  Response response = call.execute();
                                  e.onNext(response);
                              }
                          }
        ).map(new Function<Response, MobileAddress>() {
                  @Override
                  public MobileAddress apply(@NonNull Response response) throws Exception {
                      System.out.print("map 线程:" + Thread.currentThread().getName());
                      if (response.isSuccessful()) {
                          ResponseBody body = response.body();
                          if (body != null) {
                              System.out.println(",转换前:" + response.body());
                              return JSON.parseObject(body.string(), MobileAddress.class);
                          }
                      }
                      return null;
                  }
              }
        ).observeOn(Schedulers.computation()
        ).doOnNext(new Consumer<MobileAddress>() {
                       @Override
                       public void accept(@NonNull MobileAddress s) throws Exception {
                           System.out.print("doOnNext 线程:" + Thread.currentThread().getName());
                           System.out.println(", 保存成功：" + s.toString());
                       }
                   }
        ).subscribeOn(Schedulers.io()
        ).observeOn(Schedulers.computation()
        ).subscribe(new Consumer<MobileAddress>() {
            @Override
            public void accept(@NonNull MobileAddress data) throws Exception {
                System.out.print("subscribe 线程:" + Thread.currentThread().getName());
                System.out.println(",成功:" + data.toString());
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                System.out.print("subscribe 线程:" + Thread.currentThread().getName());
                System.out.println(",失败：" + throwable.getMessage());
            }
        });
        Thread.sleep(3000);
    }

    @Test
    public void loadCacheFromNet() {
        Map<String, MobileAddress> cacheManager = Maps.newHashMap();
        String key = "13021671512";

        Observable<MobileAddress> cache = Observable.create(new ObservableOnSubscribe<MobileAddress>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<MobileAddress> observableEmitter) throws Exception {
                System.out.println("cache>create");
                MobileAddress mobileAddress = cacheManager.get(key);
                if (mobileAddress != null) {
                    observableEmitter.onNext(mobileAddress);
                } else {
                    observableEmitter.onComplete();
                }
            }
        });
        Observable<MobileAddress> remoteData = Observable.create(new ObservableOnSubscribe<Response>() {
                                                                     @Override
                                                                     public void subscribe(@NonNull ObservableEmitter<Response> e) throws Exception {
                                                                         System.out.println("remoteData>create");
                                                                         Request request = new Request.Builder().url("http://api.avatardata.cn/MobilePlace/LookUp?key=ec47b85086be4dc8b5d941f5abd37a4e&mobileNumber=" + key).build();
                                                                         Call call = new OkHttpClient().newCall(request);
                                                                         Response response = call.execute();
                                                                         e.onNext(response);
                                                                     }
                                                                 }
        ).map(new Function<Response, MobileAddress>() {
                  @Override
                  public MobileAddress apply(@NonNull Response response) throws Exception {
                      System.out.println("remoteData>map");
                      if (response.isSuccessful()) {
                          ResponseBody body = response.body();
                          if (body != null) {
                              MobileAddress address = JSON.parseObject(body.string(), MobileAddress.class);
                              cacheManager.put(key, address);
                              return address;
                          }
                      }
                      return null;
                  }
              }
        );
        for (int i = 0; i < 5; i++) {
            Observable.concat(cache, remoteData).observeOn(Schedulers.io()
            ).subscribe(new Consumer<MobileAddress>() {
                @Override
                public void accept(MobileAddress mobileAddress) throws Exception {
                    System.out.println("suc:" + mobileAddress);
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    System.out.println("error:" + throwable);
                }
            });
        }
    }

    @Test
    public void flatMap() throws InterruptedException {
        AtomicReference<Throwable> exception = new AtomicReference<>();
        //把一个发射器 Observable 通过某种方法转换为多个 Observables
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> observableEmitter) throws Exception {
                //此位置很重要，如果已经onNext，再抛出异常，好像就不能捕捉异常
                if ((System.currentTimeMillis() + 1) % 2 == 0) throw new RuntimeException("provider error!");
                observableEmitter.onNext(8);
                observableEmitter.onNext(10);
                observableEmitter.onNext(3);
            }
        })
                .doOnComplete(() -> System.out.println("doOnComplete"))
                .doOnError(ex -> {//它只能捕捉provider error
                    exception.set(ex);
                    System.out.println("onError," + ex);
                })
                .subscribe(x -> {
                            System.out.println(Thread.currentThread() + ",suc:" + x);
                            if (System.currentTimeMillis() % 2 == 0) throw new RuntimeException("customerror!");
                        }, error -> {
                            exception.set(error);
                            System.out.println("consumerError," + error);
                        }, () -> {
                            System.out.println("onComplete");
                        }
                );
        System.out.println("ok!");
        //Thread.sleep(Integer.MAX_VALUE);
    }

    /**
     * 多线程
     */
    @Test
    public void flatMap2() throws InterruptedException {
        Flowable.range(1, 10)
                .flatMap(it -> Flowable.just(it)
                        .subscribeOn(Schedulers.computation())//控制在哪些线程上生成sub-stream
                        .map(i -> {
                            System.out.println(Thread.currentThread() + ",  item:" + i);
                            return i;
                        }))
                .subscribe(it -> {
                    System.out.println(Thread.currentThread().getName() + ",consumer=" + it);
                });
        Thread.sleep(Integer.MAX_VALUE);
    }

    /**
     * 多线程
     * parallel操作符是在RxJava 2.0.5引入的，相比flatMap其易用性、可读性都有较大的优势。
     */
    @Test
    public void paralle() throws InterruptedException {
        Executor executor = new ThreadPoolExecutor(4, 4, 500,
                TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(800), new ReactiveX_1x_test.NamedThreadFactory("aggrege"), new ThreadPoolExecutor.AbortPolicy());
        AtomicBoolean isComplate = new AtomicBoolean(false);
        AtomicReference<Throwable> exception = new AtomicReference<>();
        //ParallelFlowable
        try {
            Flowable.range(1, 10)
                    .map(x -> {
                        //if (System.currentTimeMillis() % 2 == 0) throw new RuntimeException("providerExcetpion" + x);
                        return x;
                    })
                    .parallel()
//                    .runOn(Schedulers.io())
                    .runOn(Schedulers.from(executor))
                    .map(new Function<Integer, Object>() {
                        @Override
                        public Object apply(@NonNull Integer integer) throws Exception {
                            System.out.println(Thread.currentThread().getName() + ",map=" + integer);
                            return integer.toString();
                        }
                    })
                    .sequential()
                    .doOnComplete(() -> {
                        isComplate.set(true);
                        System.out.println("doOnComplete");
                    })
                    .subscribe(it -> {//注意：不能执行耗时的操作，它一般执行计算类任务
                        //if (System.currentTimeMillis() % 2 == 0) throw new RuntimeException("error" + it);
                        System.out.println(Thread.currentThread().getName() + ",value=" + it);
                        Thread.sleep(3000);
                    }, error -> {//它可以捕捉，provider和consumer异常
                        isComplate.set(true);
                        exception.set(error);
                        System.out.println("onError," + error);
                    }, () -> {
                        isComplate.set(true);
                        System.out.println("onComplete");
                    });
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
        while (true) {
            if (isComplate.get()) break;
        }
        System.out.println("ok!" + exception.get());
    }

    @Test
    public void paralle2() throws InterruptedException {
        Executor executor = new ThreadPoolExecutor(4, 4, 500,
                TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(800), new ReactiveX_1x_test.NamedThreadFactory("aggrege"), new ThreadPoolExecutor.AbortPolicy());
        Stopwatch stopwatch = Stopwatch.createStarted();
        try {
            Flowable.range(1, 10)
                    .map(x -> {
                        //if (System.currentTimeMillis() % 2 == 0) throw new RuntimeException("providerExcetpion" + x);
                        return x;
                    })
                    .parallel()
//                    .runOn(Schedulers.io())
                    .runOn(Schedulers.from(executor))
                    .map(new Function<Integer, Object>() {
                        @Override
                        public Object apply(@NonNull Integer integer) throws Exception {
                            System.out.println(Thread.currentThread().getName() + ",map=" + integer);
                            Thread.sleep(3000);
                            if (System.currentTimeMillis() % 2 == 0) throw new RuntimeException("error");
                            return integer.toString();
                        }
                    })
                    .sequential()
                    .doOnError(e -> {
                        System.out.println(Thread.currentThread().getName() + ":" + e);
                    })
//                    .subscribe(x -> {
//
//                    }, error -> {
//                        System.out.println("error");
//                    }, () -> {
//                        System.out.println("isComplate");
//                    });
                    .blockingLast();

//                    .blockingForEach(x ->
//                            System.out.println(Thread.currentThread().getName() + ",value=" + x));

//                    .collect(() -> new String(), (collect, it) -> {
//                        System.out.println(Thread.currentThread().getName() + ",value=" + it);
//                    }).blockingGet();
        } catch (Exception ex) {
            System.out.println("error:");
            ex.printStackTrace();
            return;
        }
        System.out.println("ok!" + stopwatch);
    }

    public class MobileAddress {

        /**
         * error_code : 10017
         * reason : 当前用户已被禁用，如需恢复使用请与我们联系
         */

        private int error_code;
        private String reason;

        public int getError_code() {
            return error_code;
        }

        public void setError_code(int error_code) {
            this.error_code = error_code;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        @Override
        public String toString() {
            return "MobileAddress{" +
                    "error_code=" + error_code +
                    ", reason='" + reason + '\'' +
                    '}';
        }
    }

}
