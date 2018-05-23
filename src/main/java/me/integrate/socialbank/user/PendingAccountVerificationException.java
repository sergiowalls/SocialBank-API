package me.integrate.socialbank.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "This account has a pending verification")
class PendingAccountVerificationException extends RuntimeException {
}
