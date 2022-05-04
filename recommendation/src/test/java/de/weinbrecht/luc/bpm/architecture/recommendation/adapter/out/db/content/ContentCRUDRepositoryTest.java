package de.weinbrecht.luc.bpm.architecture.recommendation.adapter.out.db.content;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(ExampleContentRunner.class)
class ContentCRUDRepositoryTest {

    @Autowired
    private ContentCRUDRepository contentCRUDRepository;

    @Test
    void should_find_content() {
        assertThat(contentCRUDRepository.findById(1L)).isNotNull();
    }
}