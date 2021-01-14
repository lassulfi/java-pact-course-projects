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
import org.apache.tomcat.jni.Local;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class SaveTaskTest {
    @Rule
    public PactProviderRule mockProvdider = new PactProviderRule("Tasks", this);

    @Pact(consumer = "TasksFront")
    public RequestResponsePact createPact(PactDslWithProvider builder) {
        DslPart requestBody = new PactDslJsonBody()
                .nullValue("id")
                .stringType("task", "New Task")
//                .date("dueDate", "yyyy-MM-dd", new Date());
                .array("dueDate")
                    .numberType(LocalDate.now().getYear())
                    .numberType(LocalDate.now().getMonthValue())
                    .numberType(LocalDate.now().getDayOfMonth())
                .closeArray();

        DslPart responseBody = new PactDslJsonBody()
                .numberType("id")
                .stringType("task", "New Task")
                .date("dueDate", "yyyy-MM-dd", new Date());

        return builder
                .uponReceiving("Save a task")
                    .path("/todo")
                    .method("POST")
                    .matchHeader("Content-Type", "application/json.*", "application/json")
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
        TasksRespository consumer = new TasksRespository(mockProvdider.getUrl());
        // Act
        Todo task = consumer.save(new Todo(null, "New Task", LocalDate.now()));
        // Assert
        assertThat(task.getId(), is(notNullValue()));
        assertThat(task.getTask(), is(notNullValue()));
        assertThat(task.getTask(), is("New Task"));
        assertThat(task.getDueDate(), is(LocalDate.now()));
    }
}
