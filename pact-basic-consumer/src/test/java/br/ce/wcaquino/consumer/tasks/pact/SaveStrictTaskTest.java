package br.ce.wcaquino.consumer.tasks.pact;

import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit.PactProviderRule;
import au.com.dius.pact.consumer.junit.PactVerification;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import br.ce.wcaquino.consumer.tasks.model.Task;
import br.ce.wcaquino.consumer.tasks.service.TasksConsumer;
import io.pactfoundation.consumer.dsl.LambdaDsl;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class SaveStrictTaskTest {

    @Rule
    public PactProviderRule mockProvdider = new PactProviderRule("Tasks", this);

    @Pact(consumer = "BasicConsumer")
    public RequestResponsePact createPact(PactDslWithProvider builder) {
        DslPart requestBody = new PactDslJsonBody()
                .stringType("task", "Task with string")
                .date("dueDate", "yyyy-MM-dd", new Date());

        DslPart responseBody = new PactDslJsonBody()
                .numberType("id")
                .stringType("task")
                .stringType("dueDate");

        DslPart lambdaBody = LambdaDsl.newJsonArrayMinLike(1, (arr) -> {
            arr.object((obj) -> {
                obj.numberType("id", 1L);
                obj.stringType("task", "Task Demo");
                obj.date("dueDate", "yyyy-MM-dd", new Date());
            });
        }).build();

        return builder
                .uponReceiving("Retrieve all tasks")
                    .path("/todo")
                    .method("GET")
                .willRespondWith()
                    .status(200)
                    .body(lambdaBody)
                .uponReceiving("Save task with string")
                    .path("/todo")
                    .method("POST")
                    .body(requestBody)
                .willRespondWith()
                    .status(201)
                    .body(responseBody)
                .toPact();
    }

    @Test
    @PactVerification
    public void test() throws IOException {
        // Arranje
        TasksConsumer consumer = new TasksConsumer(mockProvdider.getUrl());
        DateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");

        // Act
        Task task = consumer.saveStrictTask("Task with string", dFormat.format(new Date()));
        // Acert
        assertThat(task.getId(), is(notNullValue()));
        assertThat(task.getTask(), is(notNullValue()));
    }
}
