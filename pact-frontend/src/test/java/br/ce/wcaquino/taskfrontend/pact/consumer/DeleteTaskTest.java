package br.ce.wcaquino.taskfrontend.pact.consumer;

import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit.PactProviderRule;
import au.com.dius.pact.consumer.junit.PactVerification;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import br.ce.wcaquino.tasksfrontend.repositories.TasksRespository;
import org.junit.Rule;
import org.junit.Test;

public class DeleteTaskTest {

    @Rule
    public PactProviderRule mockProvider = new PactProviderRule("Tasks", this);

    @Pact(consumer = "TasksFront")
    public RequestResponsePact createPact(PactDslWithProvider builder) {

        return builder
                .given("There is a task with id = 1")
                .uponReceiving("Delete a Task")
                    .path("/todo/1")
                    .method("DELETE")
                .willRespondWith()
                    .status(204)
                .toPact();
    }

    @Test
    @PactVerification
    public void shouldUpdateTask() {
        TasksRespository repo = new TasksRespository(mockProvider.getUrl());
        repo.delete(1L);
    }


}
