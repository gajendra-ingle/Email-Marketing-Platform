package com.mailchimp.marketing.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateAudienceRequest {

    @NotBlank(message = "Audience name is required")
    private String name;

    @NotBlank(message = "Permission reminder is required")
    @JsonProperty("permission_reminder")
    private String permissionReminder;

    @JsonProperty("email_type_option")
    private boolean emailTypeOption = false;

    @JsonProperty("contact")
    private AudienceContact contact;

    @JsonProperty("campaign_defaults")
    private CampaignDefaults campaignDefaults;

    @Data
    public static class AudienceContact {
        @NotBlank private String company;
        @NotBlank private String address1;
        private String address2;
        @NotBlank private String city;
        @NotBlank private String state;
        @NotBlank private String zip;
        @NotBlank private String country;
        private String phone;
    }

    @Data
    public static class CampaignDefaults {
        @NotBlank
        @JsonProperty("from_name")
        private String fromName;

        @Email
        @NotBlank
        @JsonProperty("from_email")
        private String fromEmail;

        @NotBlank
        private String subject;

        @NotBlank
        private String language;
    }
}
