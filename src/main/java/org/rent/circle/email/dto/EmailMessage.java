package org.rent.circle.email.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@RegisterForReflection
public class EmailMessage {

    private String to;
    private String subject;
    private String body;
}
