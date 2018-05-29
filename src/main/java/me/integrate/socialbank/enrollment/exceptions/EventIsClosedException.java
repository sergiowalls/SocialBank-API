package me.integrate.socialbank.enrollment.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "The event is already closed.")
public class EventIsClosedException extends RuntimeException {
}
