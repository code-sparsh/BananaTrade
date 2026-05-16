package com.sparshsethi.bananatrade.marketdata.service;

import java.util.Set;

public interface MarketDataProvider {

    public void subscribeInstruments(Set<String> instruments);
    public void unsubscribeInstruments(Set<String> instruments);
}
