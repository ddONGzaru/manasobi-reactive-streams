package io.manasobi.reactive.streams.frp_03;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by tw.jang on 2016-12-12.
 */
@Slf4j
public class IntervalEx {

    public static void main(String[] args) {

        Publisher<Integer> pub = sub -> {
            sub.onSubscribe(new Subscription() {

                int no = 0;
                boolean cancelled = false;

                @Override
                public void request(long n) {
                    ScheduledExecutorService es = Executors.newSingleThreadScheduledExecutor();
                    es.scheduleAtFixedRate(() -> {
                        if (cancelled) {
                            es.shutdown();
                            return;
                        }
                        sub.onNext(++no);
                    }, 0, 500, TimeUnit.MILLISECONDS);
                }

                @Override
                public void cancel() {
                    cancelled = true;
                }
            });
        };

        Publisher<Integer> takeOp = sub -> {
            pub.subscribe(new Subscriber<Integer>() {

                private int count = 1;
                Subscription subscription;

                @Override
                public void onSubscribe(Subscription s) {
                    sub.onSubscribe(s);
                    this.subscription = s;
                }

                @Override
                public void onNext(Integer integer) {

                    if (count++ >= 9) {
                        subscription.cancel();
                    }

                    sub.onNext(integer);
                }

                @Override
                public void onError(Throwable t) {
                    sub.onError(t);
                }

                @Override
                public void onComplete() {
                    sub.onComplete();
                }
            });
        };

        takeOp.subscribe(new Subscriber<Integer>() {
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

        log.debug("exit...");

    }
}
