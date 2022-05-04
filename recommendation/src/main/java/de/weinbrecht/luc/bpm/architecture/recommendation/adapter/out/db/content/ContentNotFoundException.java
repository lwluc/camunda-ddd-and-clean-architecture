package de.weinbrecht.luc.bpm.architecture.recommendation.adapter.out.db.content;

public class ContentNotFoundException extends RuntimeException {
    public ContentNotFoundException(String message) {
        super(message);
    }
}
