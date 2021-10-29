package org.acme.infrastructure;

import io.quarkus.arc.Unremovable;
import org.acme.domain.commands.CommandRecord;
import org.acme.domain.commands.SomeKindOfCommand;
import org.acme.domain.commands.SomeOtherKindOfCommand;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped @Unremovable
public class MyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyService.class);

    @Inject
    @Channel("commands-out")
    Emitter<SomeOtherKindOfCommand> commandEmitter;

    @Transactional(Transactional.TxType.SUPPORTS)
    public void handleCommand(final SomeKindOfCommand someKindOfCommand) {

        LOGGER.debug("handleCommand: {}", someKindOfCommand);

        CommandRecord commandRecord = new CommandRecord(someKindOfCommand.toString());
        commandRecord.persist();

        commandEmitter.send(new SomeOtherKindOfCommand(someKindOfCommand.getWhatToDo()));
    }
}
