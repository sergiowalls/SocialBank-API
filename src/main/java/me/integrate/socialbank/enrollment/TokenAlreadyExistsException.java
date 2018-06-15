package me.integrate.socialbank.enrollment;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Exchange token already exists")
class TokenAlreadyExistsException extends RuntimeException {
}
