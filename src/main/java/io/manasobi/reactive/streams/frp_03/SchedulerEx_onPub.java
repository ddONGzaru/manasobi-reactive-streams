package io.manasobi.reactive.streams.frp_03;

/**
 * Created by tw.jang on 2016-12-12.
 */

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Reactive Streams - Schedulers
 *
 *
 */
@Slf4j
public class SchedulerEx_onPub {

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

        Publisher<Integer> pubOnPub = sub -> {

            pub.subscribe(new Subscriber<Integer>() {

                //ExecutorService es = Executors.newCachedThreadPool();
                ExecutorService es = Executors.newSingleThreadExecutor();

                @Override
                public void onSubscribe(Subscription s) {
                    sub.onSubscribe(s);
                }

                @Override
                public void onNext(Integer integer) {
                    es.execute(() -> sub.onNext(integer));

                }

                @Override
                public void onError(Throwable t) {
                    es.execute(() -> sub.onError(t));
                }

                @Override
                public void onComplete() {
                    es.execute(() -> sub.onComplete());
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
