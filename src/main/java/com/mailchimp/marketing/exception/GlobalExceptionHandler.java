package com.mailchimp.marketing.exception;

import com.mailchimp.marketing.dto.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MailchimpApiException.class)
    public ResponseEntity<ApiResponse<Void>> handleMailchimpApiException(MailchimpApiException ex) {
        log.error("Mailchimp API error [{}]: {}", ex.getStatusCode(), ex.getMessage());
        HttpStatus status = HttpStatus.resolve(ex.getStatusCode());
        if (status == null) status = HttpStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(status)
                .body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ApiResponse<Void>> handleHttpClientError(HttpClientErrorException ex) {
        log.error("HTTP client error: {} - {}", ex.getStatusCode(), ex.getResponseBodyAsString());
        return ResponseEntity.status(ex.getStatusCode())
                .body(ApiResponse.error("Mailchimp API error: " + ex.getResponseBodyAsString()));
    }

    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceAccessException(ResourceAccessException ex) {
        log.error("Connection error to Mailchimp API: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(ApiResponse.error("Unable to reach Mailchimp API. Please check your server prefix and network."));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationErrors(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String field = ((FieldError) error).getField();
            errors.put(field, error.getDefaultMessage());
        });
        return ResponseEntity.badRequest()
                .body(ApiResponse.<Map<String, String>>builder()
                        .success(false)
                        .message("Validation failed")
                        .data(errors)
                        .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception ex) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("An unexpected error occurred: " + ex.getMessage()));
    }
}
