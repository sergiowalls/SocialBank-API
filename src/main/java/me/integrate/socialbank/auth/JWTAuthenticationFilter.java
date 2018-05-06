package me.integrate.socialbank.auth;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private static final String CONTENT_TYPE = "application/json";
    private AuthenticationManager authenticationManager;

    JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
            Map<String, String> user = new ObjectMapper().readValue(req.getInputStream(), new TypeReference<Map<String, String>>() {
            });

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.get("email"),
                            user.get("password"),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException {

        String token = Jwts.builder()
                .setSubject(((org.springframework.security.core.userdetails.User) auth.getPrincipal()).getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + Constants.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, Constants.SECRET)
                .compact();
        res.addHeader(Constants.HEADER_STRING, Constants.TOKEN_PREFIX + token);

        Map<String, String> user = new HashMap<>();
        User principal = (User) auth.getPrincipal();
        user.put("email", principal.getUsername());
        user.put("name", principal.getName());

        res.getWriter().write(new ObjectMapper().writeValueAsString(user));

        res.setContentType(CONTENT_TYPE);
    }
}
