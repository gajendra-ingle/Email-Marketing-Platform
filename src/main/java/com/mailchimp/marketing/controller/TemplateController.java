package com.mailchimp.marketing.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.mailchimp.marketing.dto.response.ApiResponse;
import com.mailchimp.marketing.service.TemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/templates")
@RequiredArgsConstructor
@Tag(name = "Templates", description = "Manage reusable Mailchimp email templates")
public class TemplateController {

    private final TemplateService templateService;

    @GetMapping
    @Operation(summary = "Get all saved templates")
    public ResponseEntity<ApiResponse<JsonNode>> getAllTemplates(
            @RequestParam(defaultValue = "10") int count,
            @RequestParam(defaultValue = "0") int offset) {
        return ResponseEntity.ok(ApiResponse.success(templateService.getAllTemplates(count, offset)));
    }

    @GetMapping("/{templateId}")
    @Operation(summary = "Get a template by ID")
    public ResponseEntity<ApiResponse<JsonNode>> getTemplate(@PathVariable Long templateId) {
        return ResponseEntity.ok(ApiResponse.success(templateService.getTemplateById(templateId)));
    }

    @GetMapping("/{templateId}/default-content")
    @Operation(summary = "Get template default content sections")
    public ResponseEntity<ApiResponse<JsonNode>> getDefaultContent(@PathVariable Long templateId) {
        return ResponseEntity.ok(ApiResponse.success(
                templateService.getTemplateDefaultContent(templateId)));
    }

    @PostMapping
    @Operation(summary = "Create a custom HTML template")
    public ResponseEntity<ApiResponse<JsonNode>> createTemplate(
            @RequestBody Map<String, String> body) {
        JsonNode created = templateService.createTemplate(body.get("name"), body.get("html"));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Template created successfully", created));
    }

    @PatchMapping("/{templateId}")
    @Operation(summary = "Update a template's name or HTML")
    public ResponseEntity<ApiResponse<JsonNode>> updateTemplate(
            @PathVariable Long templateId,
            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(ApiResponse.success("Template updated",
                templateService.updateTemplate(templateId, body.get("name"), body.get("html"))));
    }

    @DeleteMapping("/{templateId}")
    @Operation(summary = "Delete a template")
    public ResponseEntity<ApiResponse<Void>> deleteTemplate(@PathVariable Long templateId) {
        templateService.deleteTemplate(templateId);
        return ResponseEntity.ok(ApiResponse.success("Template deleted", null));
    }
}
