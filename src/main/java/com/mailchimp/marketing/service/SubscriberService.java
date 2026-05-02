package com.mailchimp.marketing.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.mailchimp.marketing.dto.request.SubscriberRequest;
import com.mailchimp.marketing.dto.request.TagRequest;

public interface SubscriberService {

    JsonNode getAllSubscribers(String listId, int count, int offset);

    JsonNode getSubscriber(String listId, String subscriberHash);

    JsonNode addOrUpdateSubscriber(String listId, String subscriberHash, SubscriberRequest request);

    JsonNode addSubscriber(String listId, SubscriberRequest request);

    JsonNode updateSubscriberStatus(String listId, String subscriberHash, String status);

    void deleteSubscriber(String listId, String subscriberHash);

    JsonNode updateSubscriberTags(String listId, String subscriberHash, TagRequest tagRequest);

    JsonNode getSubscriberActivity(String listId, String subscriberHash);

    String md5Hash(String email);
}
