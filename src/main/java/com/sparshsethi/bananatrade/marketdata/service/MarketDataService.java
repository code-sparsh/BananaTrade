package com.sparshsethi.bananatrade.marketdata.service;

import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class MarketDataService {

    private final MarketDataProvider marketDataProvider = new UpstoxWebsocketClient();
    private final SubscriptionManager subscriptionManager = new SubscriptionManager();

    public void subscribe(Set<String> instruments, String userId) {

        Set<String> newInstruments = subscriptionManager.subscribe(instruments, userId);
        marketDataProvider.subscribeInstruments(newInstruments);
    }

    public void unsubscribe(Set<String> instruments, String userId) {
        Set<String> inactiveInstruments = subscriptionManager.unsubscribe(instruments, userId);
        marketDataProvider.unsubscribeInstruments(inactiveInstruments);
    }
}
