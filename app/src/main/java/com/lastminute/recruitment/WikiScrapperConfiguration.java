package com.lastminute.recruitment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lastminute.recruitment.client.HtmlWikiClient;
import com.lastminute.recruitment.client.JsonWikiClient;
import com.lastminute.recruitment.domain.WikiScrapper;
import com.lastminute.recruitment.domain.WikiPageRepository;
import com.lastminute.recruitment.domain.WikiReader;
import com.lastminute.recruitment.persistence.InMemoryWikiPageRepository;
import com.lastminute.recruitment.wiki.reader.html.HtmlParser;
import com.lastminute.recruitment.wiki.reader.html.HtmlWikiReader;
import com.lastminute.recruitment.wiki.reader.json.JsonWikiReader;
import org.jsoup.Jsoup;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class WikiScrapperConfiguration {

    @Bean
    @Profile("!json")
    public WikiReader htmlWikiReader(HtmlWikiClient htmlWikiClient, HtmlParser htmlParser) {
        return new HtmlWikiReader(htmlWikiClient, htmlParser);
    }

    @Bean
    @Profile("json")
    public WikiReader jsonWikiReader(JsonWikiClient jsonWikiClient, ObjectMapper objectMapper) {
        return new JsonWikiReader(jsonWikiClient, objectMapper);
    }

    @Bean
    public JsonWikiClient jsonWikiClient() {
        return new JsonWikiClient();
    }
    @Bean
    public HtmlWikiClient htmlWikiClient() {
        return new HtmlWikiClient();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public HtmlParser htmlParser() {
        return Jsoup::parse;
    }

    @Bean
    public WikiPageRepository wikiPageRepository() {
        return new InMemoryWikiPageRepository();
    }

    @Bean
    public WikiScrapper wikiScrapper(WikiPageRepository wikiPageRepository, WikiReader wikiReader) {
        return new WikiScrapper(wikiReader, wikiPageRepository);
    }
}
