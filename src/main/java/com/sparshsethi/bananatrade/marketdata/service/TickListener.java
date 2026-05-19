package com.sparshsethi.bananatrade.marketdata.service;


import com.sparshsethi.bananatrade.marketdata.dto.Tick;

public interface TickListener {

    void onTick(Tick tick);
}
