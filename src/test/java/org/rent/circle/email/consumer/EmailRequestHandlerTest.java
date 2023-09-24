package org.rent.circle.email.consumer;

import static io.restassured.RestAssured.given;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.lambda.runtime.events.SQSEvent.SQSMessage;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import java.util.List;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.rent.circle.email.resource.SesTestResource;

@QuarkusTest
@QuarkusTestResource(SesTestResource.class)
public class EmailRequestHandlerTest {

    @Test
    public void WhenGivenASQSEventWithInvalidMessage_ShouldThrowException() {

        // Arrange
        SQSMessage message = new SQSMessage();
        message.setMessageId("123");
        message.setBody("<p>Paragraph</p>");

        SQSEvent sqsEvent = new SQSEvent();
        sqsEvent.setRecords(List.of(message));

        // Act
        // Assert
        given()
            .contentType("application/json")
            .accept("application/json")
            .body(sqsEvent)
            .when()
            .post()
            .then()
            .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    public void WhenGivenASQSEvent_ShouldProcessMessages() {

        // Arrange
        SQSMessage message = new SQSMessage();
        message.setMessageId("123");
        message.setBody("{\"to\": \"to-testemail@email.com\", \"subject\": \"Test Email\", \"body\": \"Hello World\"}");

        SQSEvent sqsEvent = new SQSEvent();
        sqsEvent.setRecords(List.of(message));

        // Act
        // Assert
        given()
            .contentType("application/json")
            .accept("application/json")
            .body(sqsEvent)
            .when()
            .post()
            .then()
            .statusCode(HttpStatus.SC_NO_CONTENT);
    }
}
