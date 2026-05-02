package com.mailchimp.marketing.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CampaignRequest {

    @NotBlank(message = "Campaign type is required")
    private String type;

    @NotNull(message = "Recipients are required")
    private Recipients recipients;

    private Settings settings;

    @Data
    public static class Recipients {
        @NotBlank(message = "List ID is required")
        @JsonProperty("list_id")
        private String listId;

        @JsonProperty("segment_opts")
        private SegmentOptions segmentOpts;
    }

    @Data
    public static class Settings {
        @NotBlank
        @JsonProperty("subject_line")
        private String subjectLine;

        @JsonProperty("preview_text")
        private String previewText;

        @NotBlank
        private String title;

        @NotBlank
        @JsonProperty("from_name")
        private String fromName;

        @NotBlank
        @JsonProperty("reply_to")
        private String replyTo;

        @JsonProperty("use_conversation")
        private boolean useConversation = false;

        @JsonProperty("to_name")
        private String toName;

        @JsonProperty("auto_footer")
        private boolean autoFooter = false;

        @JsonProperty("inline_css")
        private boolean inlineCss = false;

        @JsonProperty("authenticate")
        private boolean authenticate = true;
    }

    @Data
    public static class SegmentOptions {
        @JsonProperty("saved_segment_id")
        private Long savedSegmentId;

        private String match;
    }
}
