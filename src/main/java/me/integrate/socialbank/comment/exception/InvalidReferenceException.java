package me.integrate.socialbank.comment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Replied comment not from same event.")
public class InvalidReferenceException extends RuntimeException {
}
