

import au.com.dius.pact.consumer.ConsumerPactBuilder
import au.com.dius.pact.consumer.ConsumerPactRunnerKt
import au.com.dius.pact.consumer.PactVerificationResult
import au.com.dius.pact.consumer.model.MockProviderConfig
import com.example.consumerservice.service.ConsumerService
import org.springframework.web.reactive.function.client.WebClient
import reactor.test.StepVerifier
import spock.lang.Specification

class ConsumerPactTest extends Specification {

    def "get hello string"() {
        given:
        def pact = ConsumerPactBuilder.consumer("consumer-service")
            .hasPactWith("producer-service")
            .uponReceiving("sample request")
            .method("GET")
            .path("/v1/get")
            .willRespondWith()
            .status(200)
            .body("Hello from V1")
            .toPact()

        when:
        def result = ConsumerPactRunnerKt.runConsumerTest(
            pact, MockProviderConfig.createDefault()) {mockServer, context ->

            def webClient = WebClient.create(mockServer.getUrl())
            def consumerService = new ConsumerService(webClient)

            def resultMono = consumerService.go()

            StepVerifier.create(resultMono)
                .expectNext("Hello from V1")
                .verifyComplete()
        }

        then:
        result instanceof PactVerificationResult.Ok
    }

    def "get hello v2 string"() {
        given:
        def pact = ConsumerPactBuilder.consumer("consumer-service")
            .hasPactWith("producer-service")
            .uponReceiving("sample request 2")
            .method("GET")
            .path("/v2/get")
            .willRespondWith()
            .status(200)
            .body("Hello from V3")
            .toPact()

        when:
        def result = ConsumerPactRunnerKt.runConsumerTest(
            pact, MockProviderConfig.createDefault()) {mockServer, context ->

            def webClient = WebClient.create(mockServer.getUrl())
            def consumerService = new ConsumerService(webClient)

            def resultMono = consumerService.goV2()

            StepVerifier.create(resultMono)
                .expectNext("Hello from V3")
                .verifyComplete()
        }

        then:
        result instanceof PactVerificationResult.Ok
    }

}
