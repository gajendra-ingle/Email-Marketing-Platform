package com.mailchimp.marketing.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.mailchimp.marketing.config.MailchimpProperties;
import com.mailchimp.marketing.dto.request.CampaignContentRequest;
import com.mailchimp.marketing.dto.request.CampaignRequest;
import com.mailchimp.marketing.exception.MailchimpApiException;
import com.mailchimp.marketing.service.CampaignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CampaignServiceImpl implements CampaignService {

    private final RestTemplate mailchimpRestTemplate;
    private final MailchimpProperties properties;

    @Override
    public JsonNode getAllCampaigns(int count, int offset) {
        String url = properties.getBaseUrl() + "/campaigns?count=" + count + "&offset=" + offset;
        log.info("Fetching all campaigns, count={}, offset={}", count, offset);
        try {
            return mailchimpRestTemplate.getForObject(url, JsonNode.class);
        } catch (HttpClientErrorException e) {
            throw new MailchimpApiException("Failed to fetch campaigns", e.getStatusCode().value());
        }
    }

    @Override
    public JsonNode getCampaignById(String campaignId) {
        String url = properties.getBaseUrl() + "/campaigns/" + campaignId;
        try {
            return mailchimpRestTemplate.getForObject(url, JsonNode.class);
        } catch (HttpClientErrorException e) {
            throw new MailchimpApiException("Campaign not found: " + campaignId, e.getStatusCode().value());
        }
    }

    @Override
    public JsonNode createCampaign(CampaignRequest request) {
        String url = properties.getBaseUrl() + "/campaigns";
        log.info("Creating campaign of type: {}", request.getType());
        try {
            return mailchimpRestTemplate.postForObject(url, request, JsonNode.class);
        } catch (HttpClientErrorException e) {
            throw new MailchimpApiException("Failed to create campaign: " + e.getResponseBodyAsString(),
                    e.getStatusCode().value());
        }
    }

    @Override
    public JsonNode updateCampaign(String campaignId, CampaignRequest request) {
        String url = properties.getBaseUrl() + "/campaigns/" + campaignId;
        try {
            ResponseEntity<JsonNode> response = mailchimpRestTemplate.exchange(
                    url, HttpMethod.PATCH, new HttpEntity<>(request), JsonNode.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw new MailchimpApiException("Failed to update campaign: " + e.getResponseBodyAsString(),
                    e.getStatusCode().value());
        }
    }

    @Override
    public JsonNode setCampaignContent(String campaignId, CampaignContentRequest request) {
        String url = properties.getBaseUrl() + "/campaigns/" + campaignId + "/content";
        log.info("Setting content for campaign: {}", campaignId);
        try {
            ResponseEntity<JsonNode> response = mailchimpRestTemplate.exchange(
                    url, HttpMethod.PUT, new HttpEntity<>(request), JsonNode.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw new MailchimpApiException("Failed to set campaign content: " + e.getResponseBodyAsString(),
                    e.getStatusCode().value());
        }
    }

    @Override
    public JsonNode getCampaignContent(String campaignId) {
        String url = properties.getBaseUrl() + "/campaigns/" + campaignId + "/content";
        try {
            return mailchimpRestTemplate.getForObject(url, JsonNode.class);
        } catch (HttpClientErrorException e) {
            throw new MailchimpApiException("Failed to fetch campaign content", e.getStatusCode().value());
        }
    }

    @Override
    public JsonNode sendCampaign(String campaignId) {
        String url = properties.getBaseUrl() + "/campaigns/" + campaignId + "/actions/send";
        log.info("Sending campaign: {}", campaignId);
        try {
            ResponseEntity<JsonNode> response = mailchimpRestTemplate.exchange(
                    url, HttpMethod.POST, new HttpEntity<>(Map.of()), JsonNode.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw new MailchimpApiException("Failed to send campaign: " + e.getResponseBodyAsString(),
                    e.getStatusCode().value());
        }
    }

    @Override
    public JsonNode scheduleCampaign(String campaignId, String scheduleTime) {
        String url = properties.getBaseUrl() + "/campaigns/" + campaignId + "/actions/schedule";
        log.info("Scheduling campaign: {} at {}", campaignId, scheduleTime);
        Map<String, String> body = Map.of("schedule_time", scheduleTime);
        try {
            ResponseEntity<JsonNode> response = mailchimpRestTemplate.exchange(
                    url, HttpMethod.POST, new HttpEntity<>(body), JsonNode.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw new MailchimpApiException("Failed to schedule campaign: " + e.getResponseBodyAsString(),
                    e.getStatusCode().value());
        }
    }

    @Override
    public JsonNode unscheduleCampaign(String campaignId) {
        String url = properties.getBaseUrl() + "/campaigns/" + campaignId + "/actions/unschedule";
        try {
            ResponseEntity<JsonNode> response = mailchimpRestTemplate.exchange(
                    url, HttpMethod.POST, new HttpEntity<>(Map.of()), JsonNode.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw new MailchimpApiException("Failed to unschedule campaign", e.getStatusCode().value());
        }
    }

    @Override
    public JsonNode sendTestEmail(String campaignId, String[] testEmails) {
        String url = properties.getBaseUrl() + "/campaigns/" + campaignId + "/actions/test";
        Map<String, Object> body = Map.of(
                "test_emails", testEmails,
                "send_type", "html"
        );
        try {
            ResponseEntity<JsonNode> response = mailchimpRestTemplate.exchange(
                    url, HttpMethod.POST, new HttpEntity<>(body), JsonNode.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw new MailchimpApiException("Failed to send test email: " + e.getResponseBodyAsString(),
                    e.getStatusCode().value());
        }
    }

    @Override
    public void deleteCampaign(String campaignId) {
        String url = properties.getBaseUrl() + "/campaigns/" + campaignId;
        try {
            mailchimpRestTemplate.delete(url);
        } catch (HttpClientErrorException e) {
            throw new MailchimpApiException("Failed to delete campaign", e.getStatusCode().value());
        }
    }

    @Override
    public JsonNode getCampaignReport(String campaignId) {
        String url = properties.getBaseUrl() + "/reports/" + campaignId;
        log.info("Fetching report for campaign: {}", campaignId);
        try {
            return mailchimpRestTemplate.getForObject(url, JsonNode.class);
        } catch (HttpClientErrorException e) {
            throw new MailchimpApiException("Failed to fetch campaign report", e.getStatusCode().value());
        }
    }
}
