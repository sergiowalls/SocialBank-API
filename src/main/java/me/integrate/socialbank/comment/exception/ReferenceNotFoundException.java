package me.integrate.socialbank.comment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Event or replied comment does not exist.")
public class ReferenceNotFoundException extends RuntimeException {
}
