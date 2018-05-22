package me.integrate.socialbank.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Set;

@RestController
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public Set<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/users/{email}")
    public User getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public User saveUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @PutMapping("/users/{email}/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePassword(Principal principal, @PathVariable String email, @RequestBody String password) {
        if (!email.equals(principal.getName()))
            throw new UnauthorizedUserException();
        else
            userService.updatePassword(email, password);
    }

    @PutMapping("/users/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUser(Principal principal, @PathVariable String email, @RequestBody User user) {
        if (!email.equals(principal.getName()))
            throw new UnauthorizedUserException();
        else
            userService.updateUser(email, user);
    }

    @PostMapping("/users/{email}/report")
    @ResponseStatus(HttpStatus.CREATED)
    public void reportUser(Principal principal, @PathVariable String email) {
        userService.reportUser(principal.getName(), email);
    }
}
