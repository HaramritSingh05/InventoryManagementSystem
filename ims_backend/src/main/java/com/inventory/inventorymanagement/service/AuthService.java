package com.electrowaveselectronics.inventorymanagement.service;

import com.electrowaveselectronics.inventorymanagement.entity.Auth;
import com.electrowaveselectronics.inventorymanagement.repository.AuthRepository;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private AuthRepository authRepository;

    public void createAuthInfo(String username, String  cookie) {
        Auth existingAuth = authRepository.findByUsername(username);
        if (existingAuth != null) {
            // Update the existing authentication info if needed
            existingAuth.setCookie(cookie);
            authRepository.save(existingAuth);
        } else {
            // Create a new authentication info
            Auth auth = new Auth();
            auth.setUsername(username);
            auth.setCookie(cookie);
            authRepository.save(auth);
        }
    }

    public String findUsernameByToken(String token) throws Exception{
        return authRepository.findByToken(token);

    }

}
