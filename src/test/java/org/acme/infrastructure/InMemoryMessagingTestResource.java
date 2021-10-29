package org.acme.infrastructure;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import io.smallrye.reactive.messaging.connectors.InMemoryConnector;

import java.util.HashMap;
import java.util.Map;

public class InMemoryMessagingTestResource implements QuarkusTestResourceLifecycleManager {

    @Override
    public Map<String, String> start() {
        Map<String, String> env = new HashMap<>();
        Map<String, String> inventoryIn = InMemoryConnector.switchIncomingChannelsToInMemory("commands-in");
        Map<String, String> inventoryOut = InMemoryConnector.switchOutgoingChannelsToInMemory("commands-out");
        env.putAll(inventoryIn);
        env.putAll(inventoryOut);
        return env;
    }

    @Override
    public void stop() {
        InMemoryConnector.clear();
    }}
