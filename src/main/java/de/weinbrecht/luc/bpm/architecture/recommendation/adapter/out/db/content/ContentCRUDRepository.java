package de.weinbrecht.luc.bpm.architecture.recommendation.adapter.out.db.content;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentCRUDRepository extends CrudRepository<ContentEntity, Long> {
}
