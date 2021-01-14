package br.ce.wcaquino.taskfrontend.pact.consumer;

import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit.PactProviderRule;
import au.com.dius.pact.consumer.junit.PactVerification;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import br.ce.wcaquino.tasksfrontend.model.Todo;
import br.ce.wcaquino.tasksfrontend.repositories.TasksRespository;
import org.junit.Rule;
import org.junit.Test;

import java.time.LocalDate;

public class UpdateTaskTest {

    @Rule
    public PactProviderRule mockProvider = new PactProviderRule("Tasks", this);

    @Pact(consumer = "TasksFront")
    public RequestResponsePact createPact(PactDslWithProvider builder) {
        DslPart requestBody = new PactDslJsonBody()
                .numberType("id", 1)
                .stringType("task", "Task updated")
                .array("dueDate")
                    .numberType(LocalDate.now().getYear())
                    .numberType(LocalDate.now().getMonthValue())
                    .numberType(LocalDate.now().getDayOfMonth())
                .closeArray();

        return builder
                .given("There is a task with id = 1")
                .uponReceiving("Update a Task")
                    .path("/todo/1")
                    .method("PUT")
                    .matchHeader("Content-Type", "application/json.*", "application/json")
                    .body(requestBody)
                .willRespondWith()
                    .status(200)
                .toPact();
    }

    @Test
    @PactVerification
    public void shouldUpdateTask() {
        TasksRespository repo = new TasksRespository(mockProvider.getUrl());
        repo.update(new Todo(1L, "Task updated", LocalDate.now()));
    }


}
