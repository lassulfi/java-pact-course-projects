package br.ce.wcaquino.consumer.tasks.pact.barriga;

import au.com.dius.pact.consumer.ConsumerPactBuilder;
import au.com.dius.pact.consumer.ConsumerPactRunnerKt;
import au.com.dius.pact.consumer.PactVerificationResult;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.model.MockProviderConfig;
import au.com.dius.pact.core.model.RequestResponsePact;
import br.ce.wcaquino.consumer.barriga.service.BarrigaConsumer;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class UpdateAccountTest {
    private final String TOKEN = "JWT yourTokenHere";

    @Test
    public void test() {
        PactDslJsonBody requestBody = new PactDslJsonBody()
                .stringValue("nome", "Updated Account");

        RequestResponsePact pact = ConsumerPactBuilder
                .consumer("BasicConsumer")
                .hasPactWith("Barriga")
                .given("I have a valid token")
                .given("I have an accountId")
                .uponReceiving("Update and existing account")
//                    .path("/contas/1000")
                    .pathFromProviderState("/contas/${accountId}", "/contas/1000")
                    .method("PUT")
//                    .matchHeader("Authorization", "JWT .*", TOKEN)
                    .headerFromProviderState("Authorization", "${token}", TOKEN)
                    .body(requestBody)
                .willRespondWith()
                    .status(200)
                .toPact();

        MockProviderConfig config = MockProviderConfig.createDefault();
        PactVerificationResult result = ConsumerPactRunnerKt.runConsumerTest(pact, config, (mockServer, context) -> {
            BarrigaConsumer consumer = new BarrigaConsumer(mockServer.getUrl());
            consumer.updateAccount("1000", "Updated Account", TOKEN);
            return null;
        });

        if (result instanceof PactVerificationResult.Error)
            throw new RuntimeException(((PactVerificationResult.Error) result).getError());

        assertThat(result, is(instanceOf(PactVerificationResult.Ok.class)));

    }
}
