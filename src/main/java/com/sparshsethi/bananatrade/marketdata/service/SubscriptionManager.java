package com.sparshsethi.bananatrade.marketdata.service;

import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Getter
public class SubscriptionManager {

    // instrument -> users
    private final Map<String, Set<String>> instrumentSubscribers = new ConcurrentHashMap<>();

    // user -> instruments
    private final Map<String, Set<String>> userSubscriptions = new ConcurrentHashMap<>();

    public void subscribe(Set<String> instruments, String userId) {
        for(String instrument : instruments) {

            Set<String> subscribers = instrumentSubscribers.computeIfAbsent(
                    instrument,
                    k -> ConcurrentHashMap.newKeySet()
            );
            subscribers.add(userId);

            Set<String> subscribedInstruments = userSubscriptions.computeIfAbsent(
                    userId,
                    i -> ConcurrentHashMap.newKeySet()
            );
            subscribedInstruments.add(instrument);
        }
    }

    public void unsubscribe(Set<String> instruments, String userId) {
        for(String instrument : instruments) {


            instrumentSubscribers.computeIfPresent(
                    instrument,
                    (key, subscribers) -> {
                        subscribers.remove(userId);

                        // remove map entry if empty
                        return subscribers.isEmpty()
                                ? null
                                : subscribers;
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
    }

}
