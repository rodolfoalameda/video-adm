package br.com.fc.video.adm.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException {
    private final HttpStatus status;
    private final String userMessage;
    private final String technicalMessage;

    public CustomException(HttpStatus status, String userMessage, String technicalMessage) {
        super(technicalMessage != null ? technicalMessage : userMessage);
        this.status = status;
        this.userMessage = userMessage;
        this.technicalMessage = technicalMessage;
    }

    public CustomException(HttpStatus status, String userMessage) {
        this(status, userMessage, null);
    }
}