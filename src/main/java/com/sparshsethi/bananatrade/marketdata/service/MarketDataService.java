package com.sparshsethi.bananatrade.marketdata.service;

import com.sparshsethi.bananatrade.marketdata.dto.Tick;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class MarketDataService{

    private final MarketDataProvider marketDataProvider;
    private final SubscriptionManager subscriptionManager;

    public MarketDataService(MarketDataProvider marketDataProvider, SubscriptionManager subscriptionManager) {
        this.marketDataProvider = marketDataProvider;
        this.subscriptionManager = subscriptionManager;
    }

    public void subscribe(Set<String> instruments, String userId) {

        Set<String> newInstruments = subscriptionManager.subscribe(instruments, userId);
        marketDataProvider.subscribeInstruments(newInstruments);
    }

    public void unsubscribe(Set<String> instruments, String userId) {
        Set<String> inactiveInstruments = subscriptionManager.unsubscribe(instruments, userId);
        marketDataProvider.unsubscribeInstruments(inactiveInstruments);
    }
}
