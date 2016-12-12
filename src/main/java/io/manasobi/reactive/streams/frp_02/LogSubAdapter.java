package io.manasobi.reactive.streams.frp_02;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 * Created by tw.jang on 2016-12-12.
 */
@Slf4j
public class LogSubAdapter implements Subscriber<Integer> {

    @Override
    public void onSubscribe(Subscription s) {
        log.info("Subscriber.onSubscribe");
        s.request(Long.MAX_VALUE);
    }

    @Override
    public void onNext(Integer i) {
        log.info("Subscriber.onNext :: {}", i);
    }

    @Override
    public void onError(Throwable t) {
        log.info("Subscriber.onError :: {}", t.getMessage());
    }

    @Override
    public void onComplete() {
        log.info("Subscriber.onComplete");
    }
}
