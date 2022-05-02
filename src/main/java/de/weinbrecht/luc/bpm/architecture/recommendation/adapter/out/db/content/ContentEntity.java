package de.weinbrecht.luc.bpm.architecture.recommendation.adapter.out.db.content;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.AUTO;

@Data
@Entity
public class ContentEntity {

    @Id
    @GeneratedValue(strategy= AUTO)
    private Long id;
    private String content;
}
