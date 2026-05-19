package com.sparshsethi.bananatrade.marketdata.service;

import com.sparshsethi.bananatrade.marketdata.dto.Tick;
import org.springframework.stereotype.Service;

@Service
public class MarketDataTickListener implements TickListener{

    @Override
    public void onTick(Tick tick) {
        System.out.println("(Received Tick): " + tick);
    }
}
