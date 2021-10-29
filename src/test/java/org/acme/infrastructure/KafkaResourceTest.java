package org.acme.infrastructure;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectSpy;
import io.smallrye.reactive.messaging.connectors.InMemoryConnector;
import io.smallrye.reactive.messaging.connectors.InMemorySink;
import io.smallrye.reactive.messaging.connectors.InMemorySource;
import org.acme.domain.commands.CommandRecord;
import org.acme.domain.commands.SomeKindOfCommand;
import org.acme.domain.commands.SomeOtherKindOfCommand;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.Any;
import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@QuarkusTest @QuarkusTestResource(InMemoryMessagingTestResource.class)
public class KafkaResourceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaResourceTest.class);

    @InjectSpy
    MyService myService;

    @Inject
    @Any
    InMemoryConnector sinkConnector;

    @Inject
    @Any
    InMemoryConnector sourceConnector;

    @Test
    public void test() {

        SomeKindOfCommand someKindOfCommand = new SomeKindOfCommand("Pass a JUnit test");
        InMemorySource<SomeKindOfCommand> sink = sinkConnector.source("commands-in");
        sink.send(someKindOfCommand);

        verify(myService, times(1)).handleCommand(any(SomeKindOfCommand.class));
        assertEquals(1, CommandRecord.count());

        InMemorySink<SomeOtherKindOfCommand> source = sourceConnector.sink("commands-out");
        assertEquals(1, source.received().size());
    }

}
