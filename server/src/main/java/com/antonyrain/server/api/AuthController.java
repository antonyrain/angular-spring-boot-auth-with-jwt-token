package com.antonyrain.server.api;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
// import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Controller;

import com.antonyrain.server.model.EnumRole;
import com.antonyrain.server.repository.RoleRepository;
import com.antonyrain.server.repository.UserRepository;
import com.antonyrain.server.service.UserDetailsImpl;
import com.antonyrain.server.model.Role;
import com.antonyrain.server.model.User;
import com.antonyrain.server.payload.SignupRequest;

@Controller
@RequestMapping("/api")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
	JwtEncoder jwtEncoder;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @GetMapping("login")
    public String login() {
        return "login";
    }

    record LoginRequest(String username, String password) {};
    record LoginResponse(String message, String access_token) {};
    record MessageResponse(String message) {};

	@PostMapping("login")
	public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.username, 
                    loginRequest.password
                )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            Instant now = Instant.now();
            long expiry = 36000L;
            
            String scope = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(" "));
            JwtClaimsSet claims = JwtClaimsSet.builder()
                    .issuer("self")
                    .issuedAt(now)
                    .expiresAt(now.plusSeconds(expiry))
                    .subject(userDetails.getUsername())
                    .claim("scope", scope)
                    .build();

            String access_token = this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

            System.out.println(access_token);

            return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, access_token)
                .body(userDetails);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    @PostMapping("signup")
    public ResponseEntity<?> signUser(@RequestBody SignupRequest signupRequest) {
        var existsUser = userRepository.existsByUsername(signupRequest.getUsername());
        var existsEmail = userRepository.existsByEmail(signupRequest.getEmail());
        if (existsUser.isEmpty()) {
        return ResponseEntity
            .badRequest()
            .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (existsEmail.isEmpty()) {
        return ResponseEntity
            .badRequest()
            .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(
            signupRequest.getUsername(),
            passwordEncoder.encode(signupRequest.getPassword()),
            signupRequest.getEmail(),
            signupRequest.isEnabled()
        );

        String strRoles = signupRequest.getRole();
        List <Role> roles = new ArrayList<>();

        if (strRoles.equals("user")) {
            Role userRole = roleRepository.findByName(EnumRole.ROLE_USER).get();
            roles.add(userRole);
        } else if (strRoles.equals("moderator")) { 
            Role moderatorRole  = roleRepository.findByName(EnumRole.ROLE_MODERATOR).get();
            roles.add(moderatorRole);
        } else if (strRoles.equals("admin")) { 
            Role adminRole = roleRepository.findByName(EnumRole.ROLE_ADMIN).get();
            roles.add(adminRole);
        }
        
        user.setRole(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    };
}
