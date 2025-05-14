package com.BoardGameFinder.BoardGameFinder.Service;

import com.BoardGameFinder.BoardGameFinder.Model.User;
import com.BoardGameFinder.BoardGameFinder.Model.UserDetailsImpl;
import com.BoardGameFinder.BoardGameFinder.Repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        
        return new UserDetailsImpl(user);
    }
}
