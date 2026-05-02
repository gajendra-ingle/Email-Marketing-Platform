package com.mailchimp.marketing.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.mailchimp.marketing.dto.request.CreateAudienceRequest;
import com.mailchimp.marketing.dto.response.ApiResponse;
import com.mailchimp.marketing.service.AudienceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/audiences")
@RequiredArgsConstructor
@Tag(name = "Audiences", description = "Mailchimp Audience (List) management APIs")
public class AudienceController {

    private final AudienceService audienceService;

    @GetMapping
    @Operation(summary = "Get all audiences", description = "Retrieve all Mailchimp audiences with pagination")
    public ResponseEntity<ApiResponse<JsonNode>> getAllAudiences(
            @Parameter(description = "Number of records to return") @RequestParam(defaultValue = "10") int count,
            @Parameter(description = "Pagination offset") @RequestParam(defaultValue = "0") int offset) {
        return ResponseEntity.ok(ApiResponse.success(audienceService.getAllAudiences(count, offset)));
    }

    @GetMapping("/{listId}")
    @Operation(summary = "Get audience by ID")
    public ResponseEntity<ApiResponse<JsonNode>> getAudienceById(
            @PathVariable String listId) {
        return ResponseEntity.ok(ApiResponse.success(audienceService.getAudienceById(listId)));
    }

    @GetMapping("/{listId}/stats")
    @Operation(summary = "Get audience statistics")
    public ResponseEntity<ApiResponse<JsonNode>> getAudienceStats(
            @PathVariable String listId) {
        return ResponseEntity.ok(ApiResponse.success(audienceService.getAudienceStats(listId)));
    }

    @PostMapping
    @Operation(summary = "Create a new audience")
    public ResponseEntity<ApiResponse<JsonNode>> createAudience(
            @Valid @RequestBody CreateAudienceRequest request) {
        JsonNode created = audienceService.createAudience(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Audience created successfully", created));
    }

    @PatchMapping("/{listId}")
    @Operation(summary = "Update an existing audience")
    public ResponseEntity<ApiResponse<JsonNode>> updateAudience(
            @PathVariable String listId,
            @Valid @RequestBody CreateAudienceRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Audience updated successfully",
                audienceService.updateAudience(listId, request)));
    }

    @DeleteMapping("/{listId}")
    @Operation(summary = "Delete an audience")
    public ResponseEntity<ApiResponse<Void>> deleteAudience(@PathVariable String listId) {
        audienceService.deleteAudience(listId);
        return ResponseEntity.ok(ApiResponse.success("Audience deleted successfully", null));
    }
}
