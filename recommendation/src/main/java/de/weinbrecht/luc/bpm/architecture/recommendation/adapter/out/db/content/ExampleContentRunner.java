package de.weinbrecht.luc.bpm.architecture.recommendation.adapter.out.db.content;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Profile("!test")
public class ExampleContentRunner implements CommandLineRunner {

    private final ContentCRUDRepository contentCRUDRepository;

    @Override
    public void run(String... args) throws Exception {
        ContentEntity contentEntity = new ContentEntity();
        contentEntity.setId(1L);
        contentEntity.setContent("Here we do some cross-selling");
        contentCRUDRepository.save(contentEntity);
    }
}
