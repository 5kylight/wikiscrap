package com.lastminute.recruitment.domain;

import com.lastminute.recruitment.domain.error.WikiPageNotFound;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WikiScrapper {

    private final WikiReader wikiReader;
    private final WikiPageRepository repository;

    public WikiScrapper(WikiReader wikiReader, WikiPageRepository repository) {
        this.wikiReader = wikiReader;
        this.repository = repository;
    }


    public void read(String startingLink)  {
        WikiPage page = savePageUnsafe(startingLink);

        List<String> links = page.links();
        while (!links.isEmpty()) {
            String currentLink = links.stream().findFirst().get();

            Stream<String> pageLinks;
            if (!repository.existsByLink(currentLink)) {
                pageLinks = savePage(currentLink);
            } else {
                System.out.println("Page: %s already saved. Ignoring."); // TODO replace with logger
                pageLinks = Stream.empty();
            }

            Stream<String> withoutCurrent = links.stream().filter((l) -> !l.equals(currentLink));

            links = Stream.concat(withoutCurrent, pageLinks).collect(Collectors.toList());
        }
    }

    private Stream<String> savePage(String currentLink) {
        Stream<String> pageLinks;
        try {
            WikiPage wikiPage = savePageUnsafe(currentLink);
            pageLinks = wikiPage.links().stream();
        } catch (WikiPageNotFound wikiPageNotFound) {
            // TODO Question What should we do? Assuming that child links errors can be ignored
            System.err.println("Warning. One of child links not exists. Ignoring error and continue processing"); // TODO replace with logger
            pageLinks = Stream.empty();
        }
        return pageLinks;
    }

    private WikiPage savePageUnsafe(String startingLink)  {
        WikiPage wikiPage = wikiReader.read(startingLink);
        repository.save(wikiPage);
        return wikiPage;
    }

}
