package org.rent.circle.email.resource;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.testcontainers.DockerClientFactory;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.localstack.LocalStackContainer.EnabledService;
import org.testcontainers.containers.localstack.LocalStackContainer.Service;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;

public class SesTestResource implements QuarkusTestResourceLifecycleManager {

    private static final DockerImageName LOCALSTACK_IMAGE_NAME = DockerImageName.parse("localstack/localstack");
    private LocalStackContainer container;

    @Override
    public Map<String, String> start() {

        DockerClientFactory.instance().client();

        try {
            container = new LocalStackContainer(LOCALSTACK_IMAGE_NAME).withServices(Service.SES);
            container.start();

            URI endpointOverride = container.getEndpointOverride(EnabledService.named(Service.SES.getName()));

            StaticCredentialsProvider staticCredentials = StaticCredentialsProvider
                .create(AwsBasicCredentials.create("test-key", "test-secret"));

            String fromEmail = "from-testemail@email.com";
            try (SesClient client = SesClient.builder()
                .endpointOverride(endpointOverride)
                .credentialsProvider(staticCredentials)
                .httpClientBuilder(UrlConnectionHttpClient.builder())
                .region(Region.US_EAST_1).build()) {

                client.verifyEmailIdentity(req -> req.emailAddress(fromEmail));
            }

            Map<String, String> properties = new HashMap<>();
            properties.put("quarkus.ses.endpoint-override", endpointOverride.toString());
            properties.put("email.client.from.address", fromEmail);

            return properties;
        } catch (Exception e) {
            throw new RuntimeException("Could not start localstack server", e);
        }
    }

    @Override
    public void stop() {
        if (container != null) {
            container.close();
        }
    }
}
