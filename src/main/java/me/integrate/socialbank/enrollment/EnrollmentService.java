package me.integrate.socialbank.enrollment;

import me.integrate.socialbank.enrollment.exceptions.EventIsClosedException;
import me.integrate.socialbank.enrollment.exceptions.TooLateException;
import me.integrate.socialbank.enrollment.exceptions.UserIsNotEnrolledException;
import me.integrate.socialbank.enrollment.exceptions.UserIsTheCreatorException;
import me.integrate.socialbank.event.Event;
import me.integrate.socialbank.event.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class EnrollmentService {

    private EnrollmentRepository enrollmentRepository;
    private EventService eventService;

    @Autowired
    public EnrollmentService(EnrollmentRepository enrollmentRepository, EventService eventService) {
        this.enrollmentRepository = enrollmentRepository;
        this.eventService = eventService;
    }

    public Enrollment saveEnrollment(int id, String email) {
        Event event = eventService.getEventById(id);
        Date eventDate = event.getIniDate();
        if (eventDate != null) {
            if (eventDate.before(new Date())) throw new TooLateException();
        } else {
            if (event.isClosed()) throw new EventIsClosedException();
        }

        if (event.getCreatorEmail().equals(email)) throw new UserIsTheCreatorException();
        return enrollmentRepository.saveEnrollment(id, email);
    }

    public List<String> getEnrollmentsOfEvent(int id) {
        return enrollmentRepository.getEnrollmentsOfEvent(id);
    }

    public List<Integer> getEnrollmentsOfUser(String email) {
        return enrollmentRepository.getEnrollmentsOfUser(email);
    }

    public Enrollment deleteEnrollment(int id, String email) {
        try {
            enrollmentRepository.deleteEnrollment(id, email);
        } catch (DataAccessException e) {
            throw new UserIsNotEnrolledException();
        }
        return new Enrollment(id, email); //necessary for tests
    }
}
