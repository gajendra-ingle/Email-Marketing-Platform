package com.mailchimp.marketing.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CampaignContentRequest {

    private String html;

    @JsonProperty("plain_text")
    private String plainText;

    private Template template;

    @Data
    public static class Template {
        private Long id;
        private java.util.Map<String, String> sections;
    }
}
