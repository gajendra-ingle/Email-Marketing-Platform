package com.mailchimp.marketing.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "mailchimp.api")
public class MailchimpProperties {

    private String key;
    private String serverPrefix;
    private String baseUrl;
}
