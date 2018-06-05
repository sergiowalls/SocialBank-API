package me.integrate.socialbank.enrollment.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "This user is not enrolled in this event.")
public class UserIsNotEnrolledException extends RuntimeException {
}
