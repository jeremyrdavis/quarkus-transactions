package org.acme.infrastructure;

import io.smallrye.common.annotation.Blocking;
import org.acme.domain.commands.SomeKindOfCommand;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
public class KafkaResource {

    @Inject
    MyService myService;

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaResource.class);

    @Incoming("commands-in") @Transactional @Blocking
    public void handleSomeKindOfCommand(final SomeKindOfCommand someKindOfCommand) {

        LOGGER.debug("handleSomeKindOfCommand: {}", someKindOfCommand);
        myService.handleCommand(someKindOfCommand);
    }
}
