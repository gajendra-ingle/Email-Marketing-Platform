package com.mailchimp.marketing.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Map;

@Data
public class SubscriberRequest {

    @Email(message = "Valid email address is required")
    @NotBlank(message = "Email is required")
    @JsonProperty("email_address")
    private String emailAddress;

    /**
     * Status: subscribed | unsubscribed | pending
     */
    private String status = "subscribed";

    @JsonProperty("merge_fields")
    private Map<String, String> mergeFields;

    private Map<String, Boolean> interests;

    @JsonProperty("email_type")
    private String emailType = "html";

    private String language;

    @JsonProperty("vip")
    private boolean vip = false;

    @JsonProperty("tags")
    private String[] tags;
}
