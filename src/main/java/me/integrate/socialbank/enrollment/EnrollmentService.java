package me.integrate.socialbank.enrollment;

import me.integrate.socialbank.enrollment.exceptions.*;
import me.integrate.socialbank.event.Event;
import me.integrate.socialbank.event.EventService;
import me.integrate.socialbank.user.User;
import me.integrate.socialbank.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class EnrollmentService {

    private EnrollmentRepository enrollmentRepository;
    private EventService eventService;
    private UserService userService;

    @Autowired
    public EnrollmentService(EnrollmentRepository enrollmentRepository, EventService eventService, UserService
            userService) {
        this.enrollmentRepository = enrollmentRepository;
        this.eventService = eventService;
        this.userService = userService;
    }

    public Enrollment saveEnrollment(int id, String email) {
        Event event = eventService.getEventById(id);
        String creatorEmail = event.getCreatorEmail();
        User user = userService.getUserByEmail(creatorEmail);
        Date eventIniDate = event.getIniDate();
        if (eventIniDate == null) {
            if (event.isClosed()) throw new EventIsClosedException();

        } else {
            if (eventIniDate.before(new Date())) throw new TooLateException();
            if (event.beginsInLessThan24h()) throw new TooLateException();

        }
        if (creatorEmail.equals(email)) throw new UserIsTheCreatorException();
        if (user.getBalance() < event.getIntervalTime())
            throw new UserDoesNotHaveEnoughHours();
        return enrollmentRepository.saveEnrollment(id, email);
    }

    public List<String> getEnrollmentsOfEvent(int id) {
        return enrollmentRepository.getEnrollmentsOfEvent(id);
    }

    public List<Integer> getEnrollmentsOfUser(String email) {
        return enrollmentRepository.getEnrollmentsOfUser(email);
    }

    public int getNumberOfUsersEnrolledInEvent(int id) {
        return enrollmentRepository.getNumberOfUsersEnrolledInEvent(id);
    }

    public Enrollment deleteEnrollment(int id, String email) {
        Event event = eventService.getEventById(id);
        if (event.isClosed()) throw new EventIsClosedException();
        if (event.beginsInLessThan24h()) throw new TooLateException();
        try {
            enrollmentRepository.deleteEnrollment(id, email);
        } catch (DataAccessException e) {
            throw new UserIsNotEnrolledException();
        }
        return new Enrollment(id, email); //necessary for tests
    }
}
