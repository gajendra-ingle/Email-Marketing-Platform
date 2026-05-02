package com.mailchimp.marketing.service;

import com.fasterxml.jackson.databind.JsonNode;

public interface TemplateService {

    JsonNode getAllTemplates(int count, int offset);

    JsonNode getTemplateById(Long templateId);

    JsonNode createTemplate(String name, String html);

    JsonNode updateTemplate(Long templateId, String name, String html);

    void deleteTemplate(Long templateId);

    JsonNode getTemplateDefaultContent(Long templateId);
}
