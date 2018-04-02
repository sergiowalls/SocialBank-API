package me.integrate.socialbank.event;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.CONFLICT, reason="Email already exists")
public class EventAlreadyExistsException extends RuntimeException{
}
