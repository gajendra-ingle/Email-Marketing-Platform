package com.mailchimp.marketing.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.mailchimp.marketing.dto.request.SubscriberRequest;
import com.mailchimp.marketing.dto.request.TagRequest;
import com.mailchimp.marketing.dto.response.ApiResponse;
import com.mailchimp.marketing.service.SubscriberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/audiences/{listId}/subscribers")
@RequiredArgsConstructor
@Tag(name = "Subscribers", description = "Manage subscribers within a Mailchimp audience")
public class SubscriberController {

    private final SubscriberService subscriberService;

    @GetMapping
    @Operation(summary = "Get all subscribers in an audience")
    public ResponseEntity<ApiResponse<JsonNode>> getAllSubscribers(
            @PathVariable String listId,
            @RequestParam(defaultValue = "10") int count,
            @RequestParam(defaultValue = "0") int offset) {
        return ResponseEntity.ok(ApiResponse.success(subscriberService.getAllSubscribers(listId, count, offset)));
    }

    @GetMapping("/{email}")
    @Operation(summary = "Get a subscriber by email address",
            description = "Email is automatically MD5-hashed as required by Mailchimp API")
    public ResponseEntity<ApiResponse<JsonNode>> getSubscriber(
            @PathVariable String listId,
            @Parameter(description = "Subscriber email address") @PathVariable String email) {
        String hash = subscriberService.md5Hash(email);
        return ResponseEntity.ok(ApiResponse.success(subscriberService.getSubscriber(listId, hash)));
    }

    @PostMapping
    @Operation(summary = "Add a new subscriber to an audience")
    public ResponseEntity<ApiResponse<JsonNode>> addSubscriber(
            @PathVariable String listId,
            @Valid @RequestBody SubscriberRequest request) {
        JsonNode result = subscriberService.addSubscriber(listId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Subscriber added successfully", result));
    }

    @PutMapping("/{email}")
    @Operation(summary = "Add or update (upsert) a subscriber")
    public ResponseEntity<ApiResponse<JsonNode>> upsertSubscriber(
            @PathVariable String listId,
            @PathVariable String email,
            @Valid @RequestBody SubscriberRequest request) {
        String hash = subscriberService.md5Hash(email);
        return ResponseEntity.ok(ApiResponse.success("Subscriber upserted successfully",
                subscriberService.addOrUpdateSubscriber(listId, hash, request)));
    }

    @PatchMapping("/{email}/status")
    @Operation(summary = "Update subscriber status",
            description = "Valid statuses: subscribed, unsubscribed, cleaned, pending")
    public ResponseEntity<ApiResponse<JsonNode>> updateStatus(
            @PathVariable String listId,
            @PathVariable String email,
            @RequestParam String status) {
        String hash = subscriberService.md5Hash(email);
        return ResponseEntity.ok(ApiResponse.success("Status updated",
                subscriberService.updateSubscriberStatus(listId, hash, status)));
    }

    @PostMapping("/{email}/tags")
    @Operation(summary = "Add or remove tags for a subscriber")
    public ResponseEntity<ApiResponse<JsonNode>> updateTags(
            @PathVariable String listId,
            @PathVariable String email,
            @Valid @RequestBody TagRequest tagRequest) {
        String hash = subscriberService.md5Hash(email);
        return ResponseEntity.ok(ApiResponse.success("Tags updated",
                subscriberService.updateSubscriberTags(listId, hash, tagRequest)));
    }

    @GetMapping("/{email}/activity")
    @Operation(summary = "Get subscriber activity history")
    public ResponseEntity<ApiResponse<JsonNode>> getActivity(
            @PathVariable String listId,
            @PathVariable String email) {
        String hash = subscriberService.md5Hash(email);
        return ResponseEntity.ok(ApiResponse.success(
                subscriberService.getSubscriberActivity(listId, hash)));
    }

    @DeleteMapping("/{email}")
    @Operation(summary = "Delete a subscriber from an audience")
    public ResponseEntity<ApiResponse<Void>> deleteSubscriber(
            @PathVariable String listId,
            @PathVariable String email) {
        String hash = subscriberService.md5Hash(email);
        subscriberService.deleteSubscriber(listId, hash);
        return ResponseEntity.ok(ApiResponse.success("Subscriber deleted", null));
    }
}
