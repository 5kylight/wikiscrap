package com.lastminute.recruitment.wiki.reader.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lastminute.recruitment.client.JsonWikiClient;
import com.lastminute.recruitment.domain.WikiPage;
import com.lastminute.recruitment.domain.WikiReader;
import com.lastminute.recruitment.wiki.reader.json.model.JsonWikiPage;

public class JsonWikiReader implements WikiReader {

    private final JsonWikiClient jsonWikiClient;
    private final ObjectMapper objectMapper;

    public JsonWikiReader(JsonWikiClient jsonWikiClient, ObjectMapper objectMapper) {
        this.jsonWikiClient = jsonWikiClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public WikiPage read(String link)  {
        String json = jsonWikiClient.readJson(link);

        try {
            JsonWikiPage page = objectMapper.readValue(json, JsonWikiPage.class);
            return new WikiPage(page.title(), page.content(), link, page.links());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to parse data.", e);
        }
    }

}
