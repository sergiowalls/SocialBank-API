package me.integrate.socialbank.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class RecoveryController {
    private RecoveryService recoveryService;

    @Autowired
    public RecoveryController(RecoveryService recoveryService) {
        this.recoveryService = recoveryService;
    }


    @PostMapping("/recover")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void requestEmail(@RequestBody String email) {
        recoveryService.requestEmail(email);
    }

    @PutMapping("/recover/{token}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void requestPasswordChange(@PathVariable String token, @RequestBody String newPassword) {
        recoveryService.requestPasswordChange(token, newPassword);
    }
}
