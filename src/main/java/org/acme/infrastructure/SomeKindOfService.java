package org.acme.infrastructure;

import org.acme.domain.commands.CommandRecord;
import org.acme.domain.commands.SomeKindOfCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

@ApplicationScoped
public class SomeKindOfService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SomeKindOfService.class);

    @Transactional
    public void handleCommand(SomeKindOfCommand someKindOfCommand) {

        LOGGER.debug("handling: {}", someKindOfCommand);
        CommandRecord commandRecord = new CommandRecord();
        commandRecord.setPayload(someKindOfCommand.toString());
        commandRecord.persist();
    }
}
