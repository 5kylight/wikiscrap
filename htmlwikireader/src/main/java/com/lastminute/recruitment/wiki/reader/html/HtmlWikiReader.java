package com.lastminute.recruitment.wiki.reader.html;

import com.lastminute.recruitment.client.HtmlWikiClient;
import com.lastminute.recruitment.domain.WikiPage;
import com.lastminute.recruitment.domain.WikiReader;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class HtmlWikiReader implements WikiReader {

    private final HtmlWikiClient htmlWikiClient;
    private final HtmlParser htmlParser;

    public HtmlWikiReader(HtmlWikiClient htmlWikiClient, HtmlParser htmlParser) {
        this.htmlWikiClient = htmlWikiClient;
        this.htmlParser = htmlParser;
    }

    @Override
    public WikiPage read(String link)  {
        String htmlString = htmlWikiClient.readHtml(link);
        Document parsedDocument = htmlParser.parse(htmlString);
        Optional<Element> body = Optional.of(parsedDocument.body());
        String title = body.map((it) -> it.getElementsByClass("title"))
                .map((Elements::text))
                .orElse(null);
        String content = body.map((it) -> it.getElementsByClass("content"))
                .map((Elements::text))
                .orElse(null);
        List<String> links = body.map((it) -> it.getElementsByClass("links"))
                .map((linksElement) -> linksElement.select("a[href]").stream().map((it) -> it.attr("href")).collect(Collectors.toList()))
                .orElse(Collections.emptyList());
        // TODO Question should we take link from arguments or from page, assert are those equal?
        return new WikiPage(title, content, link, links);
    }

}


