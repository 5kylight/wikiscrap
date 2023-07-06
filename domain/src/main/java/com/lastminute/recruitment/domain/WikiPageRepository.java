package com.lastminute.recruitment.domain;

public interface WikiPageRepository {

    void save(WikiPage wikiPage);

    boolean existsByLink(String link);
}
