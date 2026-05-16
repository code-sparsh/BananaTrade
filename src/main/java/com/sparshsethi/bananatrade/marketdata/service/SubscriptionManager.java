package com.sparshsethi.bananatrade.marketdata.service;

import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Getter
public class SubscriptionManager {

    private final Lock lock = new ReentrantLock();

    // instrument -> users
    private final Map<String, Set<String>> instrumentSubscribers = new ConcurrentHashMap<>();

    // user -> instruments
    private final Map<String, Set<String>> userSubscriptions = new ConcurrentHashMap<>();

    // returns set of instruments that are new
    public Set<String> subscribe(Set<String> instruments, String userId) {

        lock.lock();

        try {
            Set<String> newInstruments = ConcurrentHashMap.newKeySet();

            for(String instrument : instruments) {

                Set<String> subscribers = instrumentSubscribers.computeIfAbsent(
                        instrument,
                        k -> {
                            newInstruments.add(instrument);
                            return ConcurrentHashMap.newKeySet();
                        }
                );
                subscribers.add(userId);

                Set<String> subscribedInstruments = userSubscriptions.computeIfAbsent(
                        userId,
                        i -> ConcurrentHashMap.newKeySet()
                );
                subscribedInstruments.add(instrument);
            }
            return newInstruments;

        }
        finally {
            lock.unlock();
        }
    }

    // returns set of inactive instruments
    public Set<String> unsubscribe(Set<String> instruments, String userId) {
        lock.lock();

        try {
            Set<String> inactiveInstruments = ConcurrentHashMap.newKeySet();

            for(String instrument : instruments) {

                instrumentSubscribers.computeIfPresent(
                        instrument,
                        (key, subscribers) -> {
                            subscribers.remove(userId);

                            // remove map entry if empty
                            if(subscribers.isEmpty()) {
                                inactiveInstruments.add(instrument);
                                return null;
                            }
                            return subscribers;
                        }
                );

                userSubscriptions.computeIfPresent(
                        userId,
                        (key, subscribedInstruments) -> {
                            subscribedInstruments.remove(instrument);

                            // remove map entry if empty
                            return subscribedInstruments.isEmpty()
                                    ? null
                                    : subscribedInstruments;
                        }
                );
            }

            return inactiveInstruments;
        }

        finally {
            lock.unlock();
        }

    }

}
