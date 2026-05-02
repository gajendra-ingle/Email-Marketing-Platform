package com.mailchimp.marketing.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.mailchimp.marketing.dto.request.CampaignContentRequest;
import com.mailchimp.marketing.dto.request.CampaignRequest;

public interface CampaignService {

    JsonNode getAllCampaigns(int count, int offset);

    JsonNode getCampaignById(String campaignId);

    JsonNode createCampaign(CampaignRequest request);

    JsonNode updateCampaign(String campaignId, CampaignRequest request);

    JsonNode setCampaignContent(String campaignId, CampaignContentRequest request);

    JsonNode getCampaignContent(String campaignId);

    JsonNode sendCampaign(String campaignId);

    JsonNode scheduleCampaign(String campaignId, String scheduleTime);

    JsonNode unscheduleCampaign(String campaignId);

    JsonNode sendTestEmail(String campaignId, String[] testEmails);

    void deleteCampaign(String campaignId);

    JsonNode getCampaignReport(String campaignId);
}
