package com.lastminute.recruitment.wiki.reader.html;

import org.jsoup.nodes.Document;

public interface HtmlParser {
    Document parse(String html);
}
