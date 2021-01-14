package br.ce.wcaquino.taskfrontend.pact.consumer;

import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit.PactProviderRule;
import au.com.dius.pact.consumer.junit.PactVerification;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import br.ce.wcaquino.tasksfrontend.model.Todo;
import br.ce.wcaquino.tasksfrontend.repositories.TasksRespository;
import io.pactfoundation.consumer.dsl.LambdaDsl;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class GetAllTasksTest {
    @Rule
    public PactProviderRule mockProvdider = new PactProviderRule("Tasks", this);

    @Pact(consumer = "TasksFront")
    public RequestResponsePact createPact(PactDslWithProvider builder) {
        DslPart body = LambdaDsl.newJsonArrayMinLike(1, (arr) -> {
            arr.object((obj) -> {
                obj.numberType("id", 1L);
                obj.stringType("task", "Task Demo");
                obj.date("dueDate", "yyyy-MM-dd", new Date());
            });
        }).build();

        return builder
                .given("There is a task with id = 1")
                    .uponReceiving("Retrieve all Tasks")
                    .path("/todo")
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
        Todo[] todos = consumer.getTodos();
        // Assert
        assertThat(todos.length, is(1));
        assertThat(todos[0].getTask(), is("Task Demo"));
    }
}
