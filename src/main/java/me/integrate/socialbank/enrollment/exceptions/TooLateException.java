package me.integrate.socialbank.enrollment.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Too late buddy :(")
public class TooLateException extends RuntimeException {
}