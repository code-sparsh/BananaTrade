package com.sparshsethi.bananatrade.marketdata.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Tick {
    private String instrument_key;
    private double ltp;
    private long ltt;
    private long ltq;
    private double cp;
    private String feedType;

    @Override
    public String toString() {
        return String.format("Instrument = %s, ltp = %f, ltt = %d, ltq = %d, cp = %f [%s]", getInstrument_key(), getLtp(), getLtt(), getLtq(), getCp(), getFeedType());
    }
}
