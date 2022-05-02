package de.weinbrecht.luc.bpm.architecture.recommendation.usecase.out;

import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.Recommendation;

public interface SendNotification {
    void send(Recommendation recommendation);
}
