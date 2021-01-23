package br.ce.wcaquino.consumer.tasks.service;

import br.ce.wcaquino.consumer.barriga.service.BarrigaConsumer;
import br.ce.wcaquino.consumer.tasks.model.Task;

import java.io.IOException;

public class DoubleDependency {

    private String barrigaUrl;
    private String tasksUrl;

    public DoubleDependency(String barrigaUrl, String tasksUrl) {
        this.barrigaUrl = barrigaUrl;
        this.tasksUrl = tasksUrl;
    }

    public String getToken(String user, String password) throws IOException {
        BarrigaConsumer barrigaConsumer = new BarrigaConsumer(barrigaUrl);
        TasksConsumer tasksConsumer = new TasksConsumer(tasksUrl);

        String token = barrigaConsumer.login(user, password);
        Task saveTask = tasksConsumer.saveTask("Expire token: " + token, "2050-01-01");

        return saveTask.getTask();
    }

    public static void main(String[] args) throws IOException {
        DoubleDependency dd = new DoubleDependency("https://barrigarest.wcaquino.me",
                "http://localhost:8000");
        String token = dd.getToken("your-email", "your-password");
        System.out.println(token);
    }
}
