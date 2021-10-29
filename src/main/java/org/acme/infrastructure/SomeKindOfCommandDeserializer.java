package org.acme.infrastructure;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;
import org.acme.domain.commands.SomeKindOfCommand;

public class SomeKindOfCommandDeserializer extends ObjectMapperDeserializer<SomeKindOfCommand> {

    public SomeKindOfCommandDeserializer() {
        super(SomeKindOfCommand.class);
    }
}
