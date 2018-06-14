package me.integrate.socialbank.enrollment.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "This user does not have enough hours.")
public class UserDoesNotHaveEnoughHoursException extends RuntimeException {
}
