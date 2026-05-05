package com.sparshsethi.bananatrade.marketdata.service;

import com.google.protobuf.InvalidProtocolBufferException;
import com.upstox.marketdatafeeder.rpc.proto.UpstoxMarketDataFeed;
import okhttp3.*;
import okio.ByteString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.concurrent.CountDownLatch;

import static java.lang.Thread.sleep;

@Service
public class UpstoxWebsocketClient implements MarketDataProvider {

    @Value("${UPSTOX_API_KEY}")
    String UPSTOX_API_KEY;

    private final OkHttpClient client = new OkHttpClient();

    @Async
    public void start() {
        connectLoop();
    }

    private void connectLoop() {

        while(true) {
            try {
                String wsUrl = getAuthorizedWsUrl(); // call Upstox authorize API

                CountDownLatch latch = new CountDownLatch(1);

                connect(wsUrl, latch);

                // wait until connection breaks
                latch.await();
            }
            catch(Exception e) {
                System.out.println("WS failed, retrying...");
                try {sleep(3000);
                } catch (InterruptedException ignored) {}
            }
        }
    }

    private void connect(String wsUrl, CountDownLatch latch) {
        Request request = new Request.Builder()
                .url(wsUrl)
                .build();

        System.out.println("Attempting to create connection");

        WebSocket ws = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
                latch.countDown();
                super.onClosed(webSocket, code, reason);
            }

            @Override
            public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
                System.out.println("Received Text : " + text);
                super.onMessage(webSocket, text);
            }

            @Override
            public void onMessage(@NotNull WebSocket webSocket, @NotNull ByteString bytes) {

                try {
                    UpstoxMarketDataFeed.FeedResponse response = UpstoxMarketDataFeed.FeedResponse.parseFrom(bytes.toByteArray());
                    System.out.println("Received : " + response);
                } catch (InvalidProtocolBufferException e) {
                    throw new RuntimeException(e);
                }
                super.onMessage(webSocket, bytes);
            }

            @Override
            public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
                System.out.println("Connected!");

                String sub = """
                    {
                      "guid": "1",
                      "method": "sub",
                      "data": {
                        "mode": "ltpc",
                        "instrumentKeys": ["NSE_EQ|RELIANCE"]
                      }
                    }
                """;

                webSocket.send(ByteString.encodeUtf8(sub));

            }

            @Override
            public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
                System.out.println("WS FAILURE: " + t.getMessage());

                if (response != null) {
                    System.out.println("Response: " + response);
                }
                latch.countDown();
                super.onFailure(webSocket, t, response);
            }

            @Override
            public void onClosing(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
                System.out.println("Closing: " + code + " / " + reason);
            }
        });
    }

    private String getAuthorizedWsUrl() {

        RestClient httpClient = RestClient.create();

        System.out.println("Attempting to authorize");

        String response = httpClient.get()
                .uri("https://api.upstox.com/v3/feed/market-data-feed/authorize")
                .header("Authorization", "Bearer " + UPSTOX_API_KEY)
                .retrieve()
                .body(String.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(response);

        String status = rootNode.get("status").asString();

        // handle failure
        if(!status.equals("success")) {
            return "";
        }

        JsonNode data = rootNode.get("data");
        String wsUrl = data.get("authorizedRedirectUri").asString();

        System.out.println("Authorization Success! Websocket URL received: " + wsUrl);
        return wsUrl;
    }

}
