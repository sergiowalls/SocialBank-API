package me.integrate.socialbank.event;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.sql.SQLException;

@ResponseStatus(value= HttpStatus.CONFLICT, reason="Incorrect date")
public class EventWithIncorrectDateException extends DataIntegrityViolationException {
    public EventWithIncorrectDateException(String msg) {
        super(msg);
    }
}