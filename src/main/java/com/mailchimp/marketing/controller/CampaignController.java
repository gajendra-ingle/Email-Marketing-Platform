package com.mailchimp.marketing.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.mailchimp.marketing.dto.request.CampaignContentRequest;
import com.mailchimp.marketing.dto.request.CampaignRequest;
import com.mailchimp.marketing.dto.response.ApiResponse;
import com.mailchimp.marketing.service.CampaignService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/campaigns")
@RequiredArgsConstructor
@Tag(name = "Campaigns", description = "Create, manage, and send Mailchimp email campaigns")
public class CampaignController {

    private final CampaignService campaignService;

    @GetMapping
    @Operation(summary = "Get all campaigns")
    public ResponseEntity<ApiResponse<JsonNode>> getAllCampaigns(
            @RequestParam(defaultValue = "10") int count,
            @RequestParam(defaultValue = "0") int offset) {
        return ResponseEntity.ok(ApiResponse.success(campaignService.getAllCampaigns(count, offset)));
    }

    @GetMapping("/{campaignId}")
    @Operation(summary = "Get campaign by ID")
    public ResponseEntity<ApiResponse<JsonNode>> getCampaign(@PathVariable String campaignId) {
        return ResponseEntity.ok(ApiResponse.success(campaignService.getCampaignById(campaignId)));
    }

    @PostMapping
    @Operation(summary = "Create a new campaign")
    public ResponseEntity<ApiResponse<JsonNode>> createCampaign(
            @Valid @RequestBody CampaignRequest request) {
        JsonNode created = campaignService.createCampaign(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Campaign created successfully", created));
    }

    @PatchMapping("/{campaignId}")
    @Operation(summary = "Update an existing campaign")
    public ResponseEntity<ApiResponse<JsonNode>> updateCampaign(
            @PathVariable String campaignId,
            @Valid @RequestBody CampaignRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Campaign updated",
                campaignService.updateCampaign(campaignId, request)));
    }

    @GetMapping("/{campaignId}/content")
    @Operation(summary = "Get campaign HTML/text content")
    public ResponseEntity<ApiResponse<JsonNode>> getCampaignContent(@PathVariable String campaignId) {
        return ResponseEntity.ok(ApiResponse.success(campaignService.getCampaignContent(campaignId)));
    }

    @PutMapping("/{campaignId}/content")
    @Operation(summary = "Set campaign content (HTML or template)")
    public ResponseEntity<ApiResponse<JsonNode>> setCampaignContent(
            @PathVariable String campaignId,
            @RequestBody CampaignContentRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Content updated",
                campaignService.setCampaignContent(campaignId, request)));
    }

    @PostMapping("/{campaignId}/actions/send")
    @Operation(summary = "Send campaign immediately")
    public ResponseEntity<ApiResponse<JsonNode>> sendCampaign(@PathVariable String campaignId) {
        return ResponseEntity.ok(ApiResponse.success("Campaign sent successfully",
                campaignService.sendCampaign(campaignId)));
    }

    @PostMapping("/{campaignId}/actions/schedule")
    @Operation(summary = "Schedule campaign for later delivery",
            description = "scheduleTime format: ISO 8601 UTC, e.g. 2025-12-01T10:00:00+00:00")
    public ResponseEntity<ApiResponse<JsonNode>> scheduleCampaign(
            @PathVariable String campaignId,
            @RequestParam String scheduleTime) {
        return ResponseEntity.ok(ApiResponse.success("Campaign scheduled",
                campaignService.scheduleCampaign(campaignId, scheduleTime)));
    }

    @PostMapping("/{campaignId}/actions/unschedule")
    @Operation(summary = "Unschedule a scheduled campaign")
    public ResponseEntity<ApiResponse<JsonNode>> unscheduleCampaign(@PathVariable String campaignId) {
        return ResponseEntity.ok(ApiResponse.success("Campaign unscheduled",
                campaignService.unscheduleCampaign(campaignId)));
    }

    @PostMapping("/{campaignId}/actions/test")
    @Operation(summary = "Send a test email for a campaign")
    public ResponseEntity<ApiResponse<JsonNode>> sendTestEmail(
            @PathVariable String campaignId,
            @RequestParam String[] testEmails) {
        return ResponseEntity.ok(ApiResponse.success("Test email sent",
                campaignService.sendTestEmail(campaignId, testEmails)));
    }

    @GetMapping("/{campaignId}/report")
    @Operation(summary = "Get campaign performance report")
    public ResponseEntity<ApiResponse<JsonNode>> getCampaignReport(@PathVariable String campaignId) {
        return ResponseEntity.ok(ApiResponse.success(campaignService.getCampaignReport(campaignId)));
    }

    @DeleteMapping("/{campaignId}")
    @Operation(summary = "Delete a campaign")
    public ResponseEntity<ApiResponse<Void>> deleteCampaign(@PathVariable String campaignId) {
        campaignService.deleteCampaign(campaignId);
        return ResponseEntity.ok(ApiResponse.success("Campaign deleted", null));
    }
}
