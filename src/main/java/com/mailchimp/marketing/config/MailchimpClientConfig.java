package com.mailchimp.marketing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Configuration
public class MailchimpClientConfig {

    private final MailchimpProperties properties;

    public MailchimpClientConfig(MailchimpProperties properties) {
        this.properties = properties;
    }
    
    @Bean
    public RestTemplate mailchimpRestTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(10000);

        RestTemplate restTemplate = new RestTemplate(factory);

        restTemplate.getInterceptors().add((request, body, execution) -> {
            String credentials = "apikey:" + properties.getKey();
            String encoded = Base64.getEncoder()
                    .encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
            request.getHeaders().set("Authorization", "Basic " + encoded);
            request.getHeaders().set("Content-Type", "application/json");
            return execution.execute(request, body);
        });

        return restTemplate;
    }
}
