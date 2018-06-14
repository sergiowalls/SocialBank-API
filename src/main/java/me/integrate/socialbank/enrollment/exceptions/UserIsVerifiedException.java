package me.integrate.socialbank.enrollment.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "User is verified. A verified user cannot join an event.")
public class UserIsVerifiedException extends RuntimeException {
}
