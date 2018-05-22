package me.integrate.socialbank.user;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class UserTestUtils {

    public static User createUser(String email) {
        return createUser(email, "123");
    }

    static User createUser(String email, String password) {
        User user = new User();
        user.setEmail(email);
        try {
            user.setBirthdate(new SimpleDateFormat("yyyy-MM-dd").parse("2017-03-03"));
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        user.setDescription("asd");
        user.setGender(User.Gender.FEMALE);
        user.setName("Manolo");
        user.setSurname("Del Campo");
        user.setPassword(password);
        user.setImage("123");
        user.setBalance(10000f);
        return user;
    }
}
