package com.mailchimp.marketing.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.mailchimp.marketing.dto.request.CreateAudienceRequest;

public interface AudienceService {

    JsonNode getAllAudiences(int count, int offset);

    JsonNode getAudienceById(String listId);

    JsonNode createAudience(CreateAudienceRequest request);

    JsonNode updateAudience(String listId, CreateAudienceRequest request);

    void deleteAudience(String listId);

    JsonNode getAudienceStats(String listId);
}
