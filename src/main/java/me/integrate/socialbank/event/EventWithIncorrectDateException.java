package me.integrate.socialbank.event;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Incorrect date")
class EventWithIncorrectDateException extends RuntimeException {
}