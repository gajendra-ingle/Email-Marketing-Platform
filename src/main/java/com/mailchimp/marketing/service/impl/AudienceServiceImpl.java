package com.mailchimp.marketing.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.mailchimp.marketing.config.MailchimpProperties;
import com.mailchimp.marketing.dto.request.CreateAudienceRequest;
import com.mailchimp.marketing.exception.MailchimpApiException;
import com.mailchimp.marketing.service.AudienceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class AudienceServiceImpl implements AudienceService {

    private final RestTemplate mailchimpRestTemplate;
    private final MailchimpProperties properties;

    @Override
    public JsonNode getAllAudiences(int count, int offset) {
        String url = properties.getBaseUrl() + "/lists?count=" + count + "&offset=" + offset;
        log.info("Fetching all audiences: {}", url);
        try {
            return mailchimpRestTemplate.getForObject(url, JsonNode.class);
        } catch (HttpClientErrorException e) {
            throw new MailchimpApiException("Failed to fetch audiences: " + e.getResponseBodyAsString(),
                    e.getStatusCode().value());
        }
    }

    @Override
    public JsonNode getAudienceById(String listId) {
        String url = properties.getBaseUrl() + "/lists/" + listId;
        log.info("Fetching audience: {}", listId);
        try {
            return mailchimpRestTemplate.getForObject(url, JsonNode.class);
        } catch (HttpClientErrorException e) {
            throw new MailchimpApiException("Audience not found: " + listId, e.getStatusCode().value());
        }
    }

    @Override
    public JsonNode createAudience(CreateAudienceRequest request) {
        String url = properties.getBaseUrl() + "/lists";
        log.info("Creating audience: {}", request.getName());
        try {
            return mailchimpRestTemplate.postForObject(url, request, JsonNode.class);
        } catch (HttpClientErrorException e) {
            throw new MailchimpApiException("Failed to create audience: " + e.getResponseBodyAsString(),
                    e.getStatusCode().value());
        }
    }

    @Override
    public JsonNode updateAudience(String listId, CreateAudienceRequest request) {
        String url = properties.getBaseUrl() + "/lists/" + listId;
        log.info("Updating audience: {}", listId);
        try {
            ResponseEntity<JsonNode> response = mailchimpRestTemplate.exchange(
                    url, HttpMethod.PATCH, new HttpEntity<>(request), JsonNode.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw new MailchimpApiException("Failed to update audience: " + e.getResponseBodyAsString(),
                    e.getStatusCode().value());
        }
    }

    @Override
    public void deleteAudience(String listId) {
        String url = properties.getBaseUrl() + "/lists/" + listId;
        log.info("Deleting audience: {}", listId);
        try {
            mailchimpRestTemplate.delete(url);
        } catch (HttpClientErrorException e) {
            throw new MailchimpApiException("Failed to delete audience: " + listId, e.getStatusCode().value());
        }
    }

    @Override
    public JsonNode getAudienceStats(String listId) {
        String url = properties.getBaseUrl() + "/lists/" + listId + "?fields=stats";
        try {
            return mailchimpRestTemplate.getForObject(url, JsonNode.class);
        } catch (HttpClientErrorException e) {
            throw new MailchimpApiException("Failed to fetch audience stats", e.getStatusCode().value());
        }
    }
}
