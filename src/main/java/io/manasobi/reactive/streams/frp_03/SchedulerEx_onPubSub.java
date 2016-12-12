package io.manasobi.reactive.streams.frp_03;

/**
 * Created by tw.jang on 2016-12-12.
 */

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Reactive Streams - Schedulers
 *
 *
 */
@Slf4j
public class SchedulerEx_onPubSub {

    public static void main(String[] args) {

        Publisher<Integer> pub = sub -> sub.onSubscribe(
            new Subscription() {
                @Override
                public void request(long n) {
                    log.debug("request :: ");
                    sub.onNext(1);
                    sub.onNext(2);
                    sub.onNext(3);
                    sub.onNext(4);
                    sub.onNext(5);
                    sub.onComplete();
                }

                @Override
                public void cancel() {

                }
            }
        );

        Publisher<Integer> subOnPub = sub -> {
            ExecutorService executorService = Executors.newSingleThreadExecutor(new CustomizableThreadFactory() {
                @Override
                public String getThreadNamePrefix() {
                    return "subOn-";
                }
            });
            executorService.execute(() -> pub.subscribe(sub));
            executorService.shutdown();
        };

        Publisher<Integer> pubOnPub = sub -> {

            subOnPub.subscribe(new Subscriber<Integer>() {

                //ExecutorService es = Executors.newCachedThreadPool();
                ExecutorService executorService = Executors.newSingleThreadExecutor(new CustomizableThreadFactory() {
                    @Override
                    public String getThreadNamePrefix() {
                        return "pubOn-";
                    }
                });

                @Override
                public void onSubscribe(Subscription s) {
                    sub.onSubscribe(s);
                }

                @Override
                public void onNext(Integer integer) {
                    executorService.execute(() -> sub.onNext(integer));
                }

                @Override
                public void onError(Throwable t) {
                    executorService.execute(() -> sub.onError(t));
                    executorService.shutdown();
                }

                @Override
                public void onComplete() {
                    executorService.execute(() -> sub.onComplete());
                    executorService.shutdown();
                }
            });
        };

        pubOnPub.subscribe(new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                log.debug("onSubscribe");
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(Integer integer) {
                log.debug("onNext :: {}", integer);
            }

            @Override
            public void onError(Throwable t) {
                log.debug("onError :: {}", t);

            }

            @Override
            public void onComplete() {
                log.debug("onComplete");
            }
        });

        log.debug("end...");
    }

}
