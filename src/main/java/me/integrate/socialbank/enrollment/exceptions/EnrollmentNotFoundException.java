package me.integrate.socialbank.enrollment.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Enrollment not found")
public class EnrollmentNotFoundException extends RuntimeException{
}
