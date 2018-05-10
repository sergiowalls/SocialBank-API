package me.integrate.socialbank.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Attempting to do an illegal operation")
class UnauthorizedUserException extends RuntimeException
{
}
