package me.integrate.socialbank.enrollment;

import me.integrate.socialbank.enrollment.exceptions.*;
import me.integrate.socialbank.event.Event;
import me.integrate.socialbank.event.EventService;
import me.integrate.socialbank.user.User;
import me.integrate.socialbank.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public Enrollment saveEnrollment(int id, String emailEnrolled) {
        Event event = eventService.getEventById(id);
        User userEnrolled = userService.getUserByEmail(emailEnrolled);
        Date eventIniDate = event.getIniDate();
        if (eventIniDate == null) { if (event.isClosed()) throw new EventIsClosedException(); }
        else if (eventIniDate.before(new Date())) throw new TooLateException();
        if (event.getCreatorEmail().equals(emailEnrolled)) throw new UserIsTheCreatorException();
        if (userEnrolled.getVerified()) throw new UserIsVerifiedException();
        if (userEnrolled.getBalance() < event.getIntervalTime())
            throw new UserDoesNotHaveEnoughHoursException();
        Enrollment enrollment = null;
        try {
            enrollment = enrollmentRepository.saveEnrollment(id, emailEnrolled);
            eventService.incrementNumberEnrolled(id);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return enrollment;
    }

    public List<String> getEnrollmentsOfEvent(int id) {
        return enrollmentRepository.getEnrollmentsOfEvent(id);
    }

    public List<Integer> getEnrollmentsOfUser(String email) {
        return enrollmentRepository.getEnrollmentsOfUser(email);
    }

    public List<Event> getEventsUserIsEnrolled(String email) {
        List<Integer> ids = enrollmentRepository.getEnrollmentsOfUser(email);
        List<Event> events = new ArrayList<>();
        for (Integer id : ids) events.add(eventService.getEventById(id));
        return events;
    }

    public Enrollment deleteEnrollment(int id, String email) {
        Event event = eventService.getEventById(id);
        if (event.isClosed()) throw new EventIsClosedException();
        if (event.beginsInLessThan24h()) throw new TooLateException();
        try {
            enrollmentRepository.deleteEnrollment(id, email);
            eventService.decrementNumberEnrolled(id);
        } catch (DataAccessException e) {
            throw new UserIsNotEnrolledException();
        }
        return new Enrollment(id, email); //necessary for tests
    }
}
