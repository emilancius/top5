package com.emilancius.top5.artist;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class ExceptionResponse {

    private final String message;

    public ExceptionResponse(final String message) {
        this.message = message;
    }
}
