package me.integrate.socialbank.event;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Event not found")
class EventNotFoundException extends RuntimeException {
}
