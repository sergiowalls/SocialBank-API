package me.integrate.socialbank.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "You have already reported this user")
class ReportAlreadyExistsException extends RuntimeException {
}
