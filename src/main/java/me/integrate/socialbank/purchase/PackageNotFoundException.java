package me.integrate.socialbank.purchase;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Package not found")
public class PackageNotFoundException extends RuntimeException {
}
