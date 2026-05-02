package com.mailchimp.marketing.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.mailchimp.marketing.config.MailchimpProperties;
import com.mailchimp.marketing.dto.request.SubscriberRequest;
import com.mailchimp.marketing.dto.request.TagRequest;
import com.mailchimp.marketing.exception.MailchimpApiException;
import com.mailchimp.marketing.service.SubscriberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriberServiceImpl implements SubscriberService {

    private final RestTemplate mailchimpRestTemplate;
    private final MailchimpProperties properties;

    @Override
    public JsonNode getAllSubscribers(String listId, int count, int offset) {
        String url = properties.getBaseUrl() + "/lists/" + listId
                + "/members?count=" + count + "&offset=" + offset;
        log.info("Fetching subscribers for list: {}", listId);
        try {
            return mailchimpRestTemplate.getForObject(url, JsonNode.class);
        } catch (HttpClientErrorException e) {
            throw new MailchimpApiException("Failed to fetch subscribers", e.getStatusCode().value());
        }
    }

    @Override
    public JsonNode getSubscriber(String listId, String subscriberHash) {
        String url = properties.getBaseUrl() + "/lists/" + listId + "/members/" + subscriberHash;
        try {
            return mailchimpRestTemplate.getForObject(url, JsonNode.class);
        } catch (HttpClientErrorException e) {
            throw new MailchimpApiException("Subscriber not found", e.getStatusCode().value());
        }
    }

    @Override
    public JsonNode addOrUpdateSubscriber(String listId, String subscriberHash, SubscriberRequest request) {
        String url = properties.getBaseUrl() + "/lists/" + listId + "/members/" + subscriberHash;
        log.info("Upsert subscriber in list: {}", listId);
        try {
            ResponseEntity<JsonNode> response = mailchimpRestTemplate.exchange(
                    url, HttpMethod.PUT, new HttpEntity<>(request), JsonNode.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw new MailchimpApiException("Failed to upsert subscriber: " + e.getResponseBodyAsString(),
                    e.getStatusCode().value());
        }
    }

    @Override
    public JsonNode addSubscriber(String listId, SubscriberRequest request) {
        String url = properties.getBaseUrl() + "/lists/" + listId + "/members";
        log.info("Adding subscriber to list: {}", listId);
        try {
            return mailchimpRestTemplate.postForObject(url, request, JsonNode.class);
        } catch (HttpClientErrorException e) {
            throw new MailchimpApiException("Failed to add subscriber: " + e.getResponseBodyAsString(),
                    e.getStatusCode().value());
        }
    }

    @Override
    public JsonNode updateSubscriberStatus(String listId, String subscriberHash, String status) {
        String url = properties.getBaseUrl() + "/lists/" + listId + "/members/" + subscriberHash;
        String body = "{\"status\": \"" + status + "\"}";
        try {
            ResponseEntity<JsonNode> response = mailchimpRestTemplate.exchange(
                    url, HttpMethod.PATCH, new HttpEntity<>(body), JsonNode.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw new MailchimpApiException("Failed to update subscriber status", e.getStatusCode().value());
        }
    }

    @Override
    public void deleteSubscriber(String listId, String subscriberHash) {
        String url = properties.getBaseUrl() + "/lists/" + listId + "/members/" + subscriberHash;
        try {
            mailchimpRestTemplate.delete(url);
        } catch (HttpClientErrorException e) {
            throw new MailchimpApiException("Failed to delete subscriber", e.getStatusCode().value());
        }
    }

    @Override
    public JsonNode updateSubscriberTags(String listId, String subscriberHash, TagRequest tagRequest) {
        String url = properties.getBaseUrl() + "/lists/" + listId + "/members/" + subscriberHash + "/tags";
        try {
            ResponseEntity<JsonNode> response = mailchimpRestTemplate.exchange(
                    url, HttpMethod.POST, new HttpEntity<>(tagRequest), JsonNode.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw new MailchimpApiException("Failed to update subscriber tags", e.getStatusCode().value());
        }
    }

    @Override
    public JsonNode getSubscriberActivity(String listId, String subscriberHash) {
        String url = properties.getBaseUrl() + "/lists/" + listId + "/members/" + subscriberHash + "/activity";
        try {
            return mailchimpRestTemplate.getForObject(url, JsonNode.class);
        } catch (HttpClientErrorException e) {
            throw new MailchimpApiException("Failed to fetch subscriber activity", e.getStatusCode().value());
        }
    }

    @Override
    public String md5Hash(String email) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(email.toLowerCase().trim().getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new MailchimpApiException("MD5 algorithm not available");
        }
    }
}
