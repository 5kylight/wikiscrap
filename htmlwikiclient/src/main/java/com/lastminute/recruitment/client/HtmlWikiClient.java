package com.lastminute.recruitment.client;

import com.lastminute.recruitment.domain.error.WikiPageNotFound;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class HtmlWikiClient {

    public String readHtml(String link)  {
        String name = link.replace("\"", "")
                .replace("http://wikiscrapper.test/", "/wikiscrapper/") + ".html";

        try {
            InputStream resourceAsStream = getClass().getResourceAsStream(name);
            if(resourceAsStream == null)
                throw new WikiPageNotFound();
            try (InputStreamReader streamReader = new InputStreamReader(resourceAsStream, StandardCharsets.UTF_8);
                 BufferedReader reader = new BufferedReader(streamReader)) {
                return reader.lines().collect(Collectors.joining("\n"));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
