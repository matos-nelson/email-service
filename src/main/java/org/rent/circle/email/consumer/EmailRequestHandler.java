package org.rent.circle.email.consumer;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.lambda.runtime.events.SQSEvent.SQSMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.rent.circle.email.dto.EmailMessage;
import org.rent.circle.email.service.EmailService;

@Slf4j
public class EmailRequestHandler implements RequestHandler<SQSEvent, Void> {

    @Inject
    EmailService emailService;

    @Inject
    ObjectMapper objectMapper;

    @Override
    public Void handleRequest(SQSEvent event, Context context) {

        for (SQSMessage message : event.getRecords()) {

            log.info("Processing message: {}", message.getMessageId());
            EmailMessage emailMessage;

            try {
                emailMessage = objectMapper.readValue(message.getBody(), EmailMessage.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            String messageId = emailService.send(emailMessage);
            log.info("Processed message successfully: {}", messageId);
        }

        return null;
    }
}
