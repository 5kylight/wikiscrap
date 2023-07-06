package com.lastminute.recruitment.wiki.reader.json.model;

import java.util.List;

public record JsonWikiPage(
        String title,
        String content,
        String selfLink,
        List<String> links) {
}
