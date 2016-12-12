package io.manasobi.reactive.streams.frp_02;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 * Created by tw.jang on 2016-12-12.
 */
public class SubscriptionAdapter implements Subscription {

    private Iterable<Integer> iter;

    private Subscriber subscriber;

    public SubscriptionAdapter(Iterable<Integer> iter, Subscriber<? super Integer> subscriber) {
        this.iter = iter;
        this.subscriber = subscriber;
    }

    @Override
    public void request(long n) {
        try {
            iter.forEach(s -> subscriber.onNext(s));
            subscriber.onComplete();
        } catch (Exception e) {
            e.printStackTrace();
            subscriber.onError(e);
        }
    }

    @Override
    public void cancel() {

    }
}
