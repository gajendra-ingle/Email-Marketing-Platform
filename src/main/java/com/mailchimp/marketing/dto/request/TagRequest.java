package com.mailchimp.marketing.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class TagRequest {

    @NotEmpty(message = "At least one tag is required")
    private List<Tag> tags;

    @Data
    public static class Tag {
        private String name;

        /**
         * Status: active | inactive
         */
        private String status = "active";
    }
}
