package me.integrate.socialbank.event;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Too late buddy :(")
class TooLateException extends RuntimeException {
}