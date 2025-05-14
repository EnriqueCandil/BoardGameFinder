package com.BoardGameFinder.BoardGameFinder.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.BoardGameFinder.BoardGameFinder.Model.User;
import com.BoardGameFinder.BoardGameFinder.Repository.UserRepository;
import com.BoardGameFinder.BoardGameFinder.Security.JwtUtil;

@Service
public class AuthService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    public String authenticateUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Invalid Email"));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Incorrect password");
        }

        return jwtUtil.generateToken(user);
    }

    public String getEmailFromToken(String token) {
        String email = jwtUtil.extractEmail(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Invalid token"));
        if (!jwtUtil.validateToken(token, user)) {
            throw new BadCredentialsException("Invalid token");
        }
        return email;
    }
}
