package br.ce.wcaquino.consumer.tasks.pact.barriga;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.PactTestExecutionContext;
import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit.ConsumerPactTest;
import au.com.dius.pact.core.model.RequestResponsePact;
import br.ce.wcaquino.consumer.barriga.service.BarrigaConsumer;
import io.pactfoundation.consumer.dsl.LambdaDsl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class GetAccountTest extends ConsumerPactTest {
    private final String TOKEN = "JWT your-token";

    @Override
    protected RequestResponsePact createPact(PactDslWithProvider builder) {
        DslPart body = LambdaDsl.newJsonArrayMinLike(1, (arr) -> {
            arr.object((obj) -> {
                obj.numberType("id");
                obj.stringType("nome");
            });
        }).build();

        return builder
                .given("There is at least one account")
                .uponReceiving("Retrieve the user's accounts")
                    .path("/contas")
                    .method("GET")
                    .headers("Authorization", TOKEN)
                .willRespondWith()
                    .status(200)
                    .body(body)
                .toPact();
    }

    @Override
    protected String providerName() {
        return "Barriga";
    }

    @Override
    protected String consumerName() {
        return "BasicConsumer";
    }

    @Override
    protected void runTest(MockServer mockServer, PactTestExecutionContext pactTestExecutionContext) throws IOException {
        BarrigaConsumer consumer = new BarrigaConsumer(mockServer.getUrl());
        List<Map> accounts = consumer.getAccounts(TOKEN);
        assertTrue(accounts.size() >= 1);
    }
}
