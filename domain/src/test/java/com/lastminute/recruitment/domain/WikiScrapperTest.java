package com.lastminute.recruitment.domain;

import com.lastminute.recruitment.domain.error.WikiPageNotFound;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


class WikiScrapperTest {
    private final WikiReader wikiReaderMock = Mockito.mock();
    private final WikiPageRepository wikiPageRepositoryMock = Mockito.mock();
    private final WikiScrapper wikiScrapper = new WikiScrapper(wikiReaderMock, wikiPageRepositoryMock);

    @AfterEach
    void tearDown() {
        Mockito.reset(wikiReaderMock, wikiPageRepositoryMock);
    }

    @Test
    void givenExistingPageWhenReadIsCalledThenShouldSavePageAndItsLinks()  {
        // given
        String parentLink = "http://wikiscrapper.test/parentSite";
        String childLink = "http://wikiscrapper.test/childSite";
        WikiPage parentPage = new WikiPage("parentTitle", "parentContent", parentLink, Collections.singletonList(childLink));
        WikiPage childPage  = new WikiPage("childTitle", "childContent", childLink, Collections.emptyList());
        when(wikiReaderMock.read(parentLink)).thenReturn(parentPage);
        when(wikiReaderMock.read(childLink)).thenReturn(childPage);
        when(wikiPageRepositoryMock.existsByLink(parentLink)).thenReturn(false);
        when(wikiPageRepositoryMock.existsByLink(childLink)).thenReturn(false);

        // when
        wikiScrapper.read(parentLink);

        // then
        verify(wikiReaderMock).read(parentLink);
        verify(wikiReaderMock).read(childLink);
        verify(wikiPageRepositoryMock).save(parentPage);
        verify(wikiPageRepositoryMock).save(childPage);
    }

    @Test
    void givenNotExistingPageWhenReadIsCalledThenShouldThrowException()  {
        // given
        String link = "http://wikiscrapper.test/site";
        when(wikiPageRepositoryMock.existsByLink(link)).thenReturn(false);
        when(wikiReaderMock.read(link)).thenThrow(new WikiPageNotFound());

        // when/then
        assertThrows(WikiPageNotFound.class, () -> wikiScrapper.read(link));
    }

    @Test
    void givenPageWithLinkToItselfWhenReadIsCalledThenShouldSavePageOnlyOnce()  {
        // given
        String parentLink = "http://wikiscrapper.test/parentSite";
        WikiPage parentPage = new WikiPage("parentTitle", "parentContent", parentLink, Collections.singletonList(parentLink));
        when(wikiReaderMock.read(parentLink)).thenReturn(parentPage);
        when(wikiPageRepositoryMock.existsByLink(parentLink)).thenReturn(false).thenReturn(true);
        // when
        wikiScrapper.read(parentLink);

        // then
        verify(wikiReaderMock, times(1)).read(parentLink);
        verify(wikiPageRepositoryMock, times(1)).save(parentPage);
    }

}