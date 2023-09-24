package org.rent.circle.email.configuration;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithName;

@ConfigMapping(prefix = "email.client")
public interface EmailClientConfig {

    @WithName("from.address")
    String fromAddress();
}
