

import au.com.dius.pact.consumer.ConsumerPactBuilder
import au.com.dius.pact.consumer.ConsumerPactRunnerKt
import au.com.dius.pact.consumer.PactVerificationResult
import au.com.dius.pact.consumer.dsl.PactDslJsonBody
import au.com.dius.pact.consumer.model.MockProviderConfig
import com.example.consumerservice.records.Animal
import com.example.consumerservice.records.Person
import com.example.consumerservice.records.Pet
import com.example.consumerservice.service.ConsumerService
import com.example.consumerservice.utils.PetType
import org.springframework.web.reactive.function.client.WebClient
import reactor.test.StepVerifier
import spock.lang.Specification

class ConsumerPactTest extends Specification {

    def "say hello - v1"() {
        given:
        def pact = ConsumerPactBuilder.consumer("consumer-service")
            .hasPactWith("producer-service")
            .uponReceiving("Hello String") // needs to be unique or it will be overwritten in pact file
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

    def "get person - v2"() {
        given:
        def bodyJson = new PactDslJsonBody()
            .stringType("name", "Andrew Barbanera")
            .numberType("age", 36)
            .object("pet")
                .stringType("name", "Minou")
                .numberType("legs", 4)
                .stringType("type", PetType.CAT.toString())
                .numberType("eyes", 3)

        def pact = ConsumerPactBuilder.consumer("consumer-service")
            .hasPactWith("producer-service")
            .uponReceiving("Person with Pet")
            .method("GET")
            .path("/v2/get")
            .willRespondWith()
            .status(200)
            .body(bodyJson)
            .toPact()

        when:
        def result = ConsumerPactRunnerKt.runConsumerTest(
            pact, MockProviderConfig.createDefault()) {mockServer, context ->

            def webClient = WebClient.create(mockServer.getUrl())
            def consumerService = new ConsumerService(webClient)
            def resultMono = consumerService.goV2()

            StepVerifier.create(resultMono)
                .expectNext(new Person("Andrew Barbanera", 36, new Pet("Minou", 4, PetType.CAT, 3)))
                .verifyComplete()
        }
        then:
        result instanceof PactVerificationResult.Ok
    }

    def "get animal - v3"() {
        given:
        def animal = new PactDslJsonBody()
            .booleanType("hasHair", true)
            .stringType("name", "Rex")
            .numberType("price", 1.34)

        def pact = ConsumerPactBuilder.consumer("consumer-service")
            .hasPactWith("producer-service")
            .uponReceiving("Animal")
            .method("GET")
            .path("/v3/get")
            .willRespondWith()
            .status(200)
            .body(animal)
            .toPact()
        when:
        def result = ConsumerPactRunnerKt.runConsumerTest(
            pact, MockProviderConfig.createDefault()) {mockServer, context ->

            def webClient = WebClient.create(mockServer.getUrl())
            def consumerService = new ConsumerService(webClient)
            def resultMono = consumerService.goV3()

            StepVerifier.create(resultMono)
                .expectNext(new Animal(true, "Rex", BigDecimal.valueOf(1.34)))
                .verifyComplete()
        }
        then:
        result instanceof PactVerificationResult.Ok
    }
}
