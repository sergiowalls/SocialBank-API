package me.integrate.socialbank;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Environment;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class SocialBankApplication {

    public static void main(String[] args) {
        SpringApplication.run(SocialBankApplication.class, args);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public BraintreeGateway gateway() {
        //TODO: Move this to a config file
        return new BraintreeGateway(
                Environment.SANDBOX,
                "y3bwvxzq3zwfhvw9",
                "rxfy5zgr5gj3fk76",
                "d4965a0dfce81d2d40403d401990e434");
    }
}
