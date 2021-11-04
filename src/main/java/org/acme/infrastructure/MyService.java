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
import java.util.concurrent.CompletableFuture;

@ApplicationScoped
public class MyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyService.class);

    @Inject
    @Channel("commands-out")
    Emitter<SomeOtherKindOfCommand> commandEmitter;

    @Transactional
    public void handleCommand(final SomeKindOfCommand someKindOfCommand) {

        LOGGER.debug("handleCommandFuture: {}", someKindOfCommand);

        CommandRecord commandRecord = new CommandRecord(someKindOfCommand.getWhatToDo());
        LOGGER.debug("created commandRecord: {}", commandRecord);
        commandRecord.persist();
        LOGGER.debug("persisted: {}", commandRecord);
        commandEmitter.send(new SomeOtherKindOfCommand(someKindOfCommand.getWhatToDo()));
    }

}
