package me.integrate.socialbank.purchase;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Transaction id already exists")
public class TransactionAlreadyExistsException extends RuntimeException {
}
