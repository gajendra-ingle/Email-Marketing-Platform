package com.mailchimp.marketing.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.mailchimp.marketing.config.MailchimpProperties;
import com.mailchimp.marketing.exception.MailchimpApiException;
import com.mailchimp.marketing.service.TemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class TemplateServiceImpl implements TemplateService {

    private final RestTemplate mailchimpRestTemplate;
    private final MailchimpProperties properties;

    @Override
    public JsonNode getAllTemplates(int count, int offset) {
        String url = properties.getBaseUrl() + "/templates?count=" + count + "&offset=" + offset;
        log.info("Fetching all templates");
        try {
            return mailchimpRestTemplate.getForObject(url, JsonNode.class);
        } catch (HttpClientErrorException e) {
            throw new MailchimpApiException("Failed to fetch templates", e.getStatusCode().value());
        }
    }

    @Override
    public JsonNode getTemplateById(Long templateId) {
        String url = properties.getBaseUrl() + "/templates/" + templateId;
        try {
            return mailchimpRestTemplate.getForObject(url, JsonNode.class);
        } catch (HttpClientErrorException e) {
            throw new MailchimpApiException("Template not found: " + templateId, e.getStatusCode().value());
        }
    }

    @Override
    public JsonNode createTemplate(String name, String html) {
        String url = properties.getBaseUrl() + "/templates";
        Map<String, String> body = new HashMap<>();
        body.put("name", name);
        body.put("html", html);
        log.info("Creating template: {}", name);
        try {
            return mailchimpRestTemplate.postForObject(url, body, JsonNode.class);
        } catch (HttpClientErrorException e) {
            throw new MailchimpApiException("Failed to create template: " + e.getResponseBodyAsString(),
                    e.getStatusCode().value());
        }
    }

    @Override
    public JsonNode updateTemplate(Long templateId, String name, String html) {
        String url = properties.getBaseUrl() + "/templates/" + templateId;
        Map<String, String> body = new HashMap<>();
        if (name != null) body.put("name", name);
        if (html != null) body.put("html", html);
        try {
            ResponseEntity<JsonNode> response = mailchimpRestTemplate.exchange(
                    url, HttpMethod.PATCH, new HttpEntity<>(body), JsonNode.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw new MailchimpApiException("Failed to update template", e.getStatusCode().value());
        }
    }

    @Override
    public void deleteTemplate(Long templateId) {
        String url = properties.getBaseUrl() + "/templates/" + templateId;
        try {
            mailchimpRestTemplate.delete(url);
        } catch (HttpClientErrorException e) {
            throw new MailchimpApiException("Failed to delete template", e.getStatusCode().value());
        }
    }

    @Override
    public JsonNode getTemplateDefaultContent(Long templateId) {
        String url = properties.getBaseUrl() + "/templates/" + templateId + "/default-content";
        try {
            return mailchimpRestTemplate.getForObject(url, JsonNode.class);
        } catch (HttpClientErrorException e) {
            throw new MailchimpApiException("Failed to fetch template default content", e.getStatusCode().value());
        }
    }
}
