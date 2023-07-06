package com.lastminute.recruitment;

import com.lastminute.recruitment.domain.WikiPageRepository;
import com.lastminute.recruitment.rest.WikiScrapperResource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = { WikiScrapperResource.class, WikiScrapperConfiguration.class})
@ActiveProfiles("json")
@AutoConfigureMockMvc
class WikiScrapperApplicationJsonTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WikiPageRepository wikiPageRepository;

    @Test
    public void givenExistingLinkThenPageShouldBeSaved() throws Exception {
        // given
        String link = "http://wikiscrapper.test/site1";

        // when
        mockMvc.perform(post("/wiki/scrap").content(link))
                .andExpect(status().isOk());
        // then
        assertThat(wikiPageRepository.existsByLink(link)).isEqualTo(true);
    }
    @Test
    public void givenNotExistingLinkThenNotFoundShouldBeReturned() throws Exception {
        // given
        String link = "http://wikiscrapper.test/siteNOTFOUND";

        // when
        mockMvc.perform(post("/wiki/scrap").content(link))
                .andExpect(status().isNotFound());
        // then
        assertThat(wikiPageRepository.existsByLink(link)).isEqualTo(false);
    }

}