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

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class GetTaskByIdTest {
    @Rule
    public PactProviderRule mockProvdider = new PactProviderRule("Tasks", this);

    @Pact(consumer = "TasksFront")
    public RequestResponsePact createPact(PactDslWithProvider builder) {
        DslPart body = new PactDslJsonBody()
                .numberType("id", 1L)
                .stringType("task")
                .date("dueDate", "yyyy-MM-dd", new Date());

        return builder
                .given("There is a task with id = 1")
                    .uponReceiving("Retrieve Task #1")
//                    .path("/todo/1")
                    .matchPath("/todo/\\d+", "/todo/1")
                    .method("GET")
                .willRespondWith()
                    .status(200)
                    .body(body)
                .toPact();
    }

    @Test
    @PactVerification
    public void test() throws IOException {
        // Arranje
        TasksRespository consumer = new TasksRespository(mockProvdider.getUrl());
        // Act
        Todo task = consumer.getTodo(1L);
        // Assert
        assertThat(task.getId(), is(notNullValue()));
        assertThat(task.getId(), is(1L));
        assertThat(task.getTask(), is(notNullValue()));
        assertThat(task.getTask(), is(notNullValue()));
        assertThat(task.getDueDate(), is(LocalDate.now()));
    }
}
