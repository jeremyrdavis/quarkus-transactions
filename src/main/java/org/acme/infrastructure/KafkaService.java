package org.acme.infrastructure;

import org.acme.domain.commands.SomeKindOfCommand;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
public class KafkaService {

    @Inject
    MyService myService;

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaService.class);

    @Incoming("commands-in") @Transactional
    public void handleSomeKindOfCommand(final SomeKindOfCommand someKindOfCommand) {

        LOGGER.debug("handleSomeKindOfCommand: {}", someKindOfCommand);
        myService.handleCommand(someKindOfCommand);
    }
}
