package com.teamAlpha.bookHub.catalog.model;

import org.springframework.hateoas.RepresentationModel;

public class DeleteMessage extends RepresentationModel<DeleteMessage> {
private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
