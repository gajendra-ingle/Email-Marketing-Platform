package com.mailchimp.marketing.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.mailchimp.marketing.config.MailchimpProperties;
import com.mailchimp.marketing.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Health", description = "Application and Mailchimp connectivity checks")
public class HealthController {

    private final RestTemplate mailchimpRestTemplate;
    private final MailchimpProperties properties;

    @GetMapping("/ping")
    @Operation(summary = "Ping Mailchimp API",
            description = "Verifies your API key and server prefix are correctly configured")
    public ResponseEntity<ApiResponse<JsonNode>> ping() {
        try {
            String url = properties.getBaseUrl() + "/ping";
            JsonNode response = mailchimpRestTemplate.getForObject(url, JsonNode.class);
            log.info("Mailchimp ping successful");
            return ResponseEntity.ok(ApiResponse.success("Mailchimp API connected successfully", response));
        } catch (Exception e) {
            log.error("Mailchimp ping failed: {}", e.getMessage());
            return ResponseEntity.status(503)
                    .body(ApiResponse.error("Mailchimp API unreachable: " + e.getMessage()));
        }
    }

    @GetMapping("/info")
    @Operation(summary = "Get Mailchimp account info")
    public ResponseEntity<ApiResponse<JsonNode>> getAccountInfo() {
        String url = properties.getBaseUrl() + "/";
        JsonNode info = mailchimpRestTemplate.getForObject(url, JsonNode.class);
        return ResponseEntity.ok(ApiResponse.success(info));
    }
}
