package com.sparshsethi.bananatrade;

import com.sparshsethi.bananatrade.marketdata.service.UpstoxWebsocketClient;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class StartupRunner {

    private UpstoxWebsocketClient upstoxWebsocketClient;
    public StartupRunner(UpstoxWebsocketClient upstoxWebsocketClient) {
        this.upstoxWebsocketClient = upstoxWebsocketClient;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void start() {
        upstoxWebsocketClient.start();
    }
}
