package me.integrate.socialbank.enrollment.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "This user is the creator of the event. It cannot enroll it.")
public class UserIsTheCreatorException extends RuntimeException {
}
