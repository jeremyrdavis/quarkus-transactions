package org.acme.infrastructure;

import org.acme.domain.commands.SomeKindOfCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/hello")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RESTResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(RESTResource.class);

    @Inject
    MyService myService;

    @GET
    public SomeKindOfCommand hello() {
        LOGGER.debug("hello");
        return new SomeKindOfCommand("Hello RESTEasy");
    }

    @POST
    @Transactional
    public SomeKindOfCommand sayHello(final SomeKindOfCommand someKindOfCommand) {
        LOGGER.debug("sayHello: {}", someKindOfCommand);
        myService.handleCommand(someKindOfCommand);
        return new SomeKindOfCommand("Hello POST");
    }

}