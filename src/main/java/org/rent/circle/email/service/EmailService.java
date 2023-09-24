package org.rent.circle.email.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.rent.circle.email.configuration.EmailClientConfig;
import org.rent.circle.email.dto.EmailMessage;
import software.amazon.awssdk.services.ses.SesClient;

@ApplicationScoped
public class EmailService {

    @Inject
    EmailClientConfig emailClientConfig;

    @Inject
    SesClient emailClient;

    public String send(EmailMessage message) {

        return emailClient.sendEmail(req -> req
                .source(emailClientConfig.fromAddress())
                .destination(d -> d.toAddresses(message.getTo()))
                .message(msg -> msg
                    .subject(sub -> sub.data(message.getSubject()))
                    .body(b -> b.text(txt -> txt.data(message.getBody())))))
            .messageId();
    }
}
