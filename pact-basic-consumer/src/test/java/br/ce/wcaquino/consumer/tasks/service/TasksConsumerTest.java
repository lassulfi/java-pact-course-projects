package br.ce.wcaquino.consumer.tasks.service;

import br.ce.wcaquino.consumer.tasks.model.Task;
import br.ce.wcaquino.consumer.utils.RequestHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TasksConsumerTest {

    private static final String INVALID_URL = "http://invalidUrl.com";

    @InjectMocks
    private TasksConsumer consumer = new TasksConsumer(INVALID_URL);

    @Mock
    private RequestHelper helper;

    @Test
    public void shouldGetAnExistingTask() throws IOException {
        // Arranje
        Map<String, String> expectedTask = new HashMap<>();
        expectedTask.put("id", "1");
        expectedTask.put("task", "Mocked Task!");
        expectedTask.put("dueDate", "2000-01-01");

        when(helper.get(INVALID_URL + "/todo/1")).thenReturn(expectedTask);

        // Act
        Task task = consumer.getTask(1L);

        // Assert
        assertNotNull(task);
        assertThat(task.getId(), is(1L));
    }
}
