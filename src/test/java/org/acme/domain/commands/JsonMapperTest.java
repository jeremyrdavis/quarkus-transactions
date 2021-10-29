package org.acme.domain.commands;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

public class JsonMapperTest {


    @Test
    public void testSomeKindOfCommand() throws JsonProcessingException {

        SomeKindOfCommand someKindOfCommand = new SomeKindOfCommand("Foo, Bar, Baz");
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writeValueAsString(someKindOfCommand));
    }
}
