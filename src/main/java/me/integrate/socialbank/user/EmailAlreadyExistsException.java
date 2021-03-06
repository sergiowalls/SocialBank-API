package me.integrate.socialbank.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Email already exists")
class EmailAlreadyExistsException extends RuntimeException {
}
