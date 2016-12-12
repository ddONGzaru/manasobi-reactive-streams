package io.manasobi.reactive.streams.frp_02;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 * Created by tw.jang on 2016-12-12.
 */
public class SubscriberAdapter implements Subscriber<Integer> {

    private Subscriber sub;

    public SubscriberAdapter(Subscriber<? super Integer> sub) {
        this.sub = sub;
    }

    @Override
    public void onSubscribe(Subscription s) {
        sub.onSubscribe(s);
    }

    @Override
    public void onNext(Integer i) {
        sub.onNext(i);
    }

    @Override
    public void onError(Throwable t) {
        sub.onError(t);
    }

    @Override
    public void onComplete() {
        sub.onComplete();
    }
}
