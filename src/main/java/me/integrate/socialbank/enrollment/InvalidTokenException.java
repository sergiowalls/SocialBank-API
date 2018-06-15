package me.integrate.socialbank.enrollment;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Exchange token doesn't exist")
public class InvalidTokenException extends RuntimeException {
}
