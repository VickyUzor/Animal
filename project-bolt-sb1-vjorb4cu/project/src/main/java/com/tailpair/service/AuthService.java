package com.tailpair.service;

import com.tailpair.dto.AuthRequest;
import com.tailpair.dto.AuthResponse;
import com.tailpair.dto.RegisterRequest;
import com.tailpair.dto.UserDto;
import com.tailpair.entity.User;
import com.tailpair.repository.UserRepository;
import com.tailpair.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private JwtTokenProvider tokenProvider;
    
    public AuthResponse login(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        
        String token = tokenProvider.generateToken(authentication);
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        UserDto userDto = new UserDto(user);
        
        return new AuthResponse(token, userDto);
    }
    
    public AuthResponse register(RegisterRequest request) {
        UserDto userDto = userService.createUser(request);
        
        // Automatically login after registration
        AuthRequest authRequest = new AuthRequest(request.getUsername(), request.getPassword());
        return login(authRequest);
    }
}