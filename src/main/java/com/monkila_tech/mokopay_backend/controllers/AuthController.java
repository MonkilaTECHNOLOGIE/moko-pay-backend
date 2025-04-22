package com.monkila_tech.mokopay_backend.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.monkila_tech.mokopay_backend.models.ERole;
import com.monkila_tech.mokopay_backend.models.Role;
import com.monkila_tech.mokopay_backend.models.User;
import com.monkila_tech.mokopay_backend.payload.request.CheckUserRequest;
import com.monkila_tech.mokopay_backend.payload.request.LoginRequest;
import com.monkila_tech.mokopay_backend.payload.request.RegisterRequest;
import com.monkila_tech.mokopay_backend.payload.request.SendMailRequest;
import com.monkila_tech.mokopay_backend.payload.response.StatusResponse;
import com.monkila_tech.mokopay_backend.payload.response.UserInfoResponse;
import com.monkila_tech.mokopay_backend.repository.RoleRepository;
import com.monkila_tech.mokopay_backend.repository.UserRepository;
import com.monkila_tech.mokopay_backend.security.jwt.JwtUtils;
import com.monkila_tech.mokopay_backend.security.services.UserDetailsImpl;
import com.monkila_tech.mokopay_backend.services.OtpService;
import com.monkila_tech.mokopay_backend.services.UserService;
import com.monkila_tech.mokopay_backend.utils.ValueException;

import jakarta.validation.Valid;



@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    OtpService otpService;

    @Autowired
    JwtUtils jwtUtils;

    SendMailRequest sendMailRequest;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        @SuppressWarnings("rawtypes")
        StatusResponse statusResponse = new StatusResponse();

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                        loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        if (userDetails.getUsername() == null) {
            statusResponse.setStatus(400);
            statusResponse.setMessage(loginRequest.getUsername() + " N'existe pas");
            return ResponseEntity.badRequest().body(statusResponse);
        }

        if (userDetails.getStatus() == false) {
            String message = "Ce compte est desactive";
            message.replace("e", "é");

            statusResponse.setStatus(400);
            statusResponse.setMessage(message);
            return ResponseEntity.badRequest().body(statusResponse);
        }

        // int otp = otpService.generateOTP(loginRequest.getUsername());
        // otpService.sendOtp(loginRequest.getUsername(), Integer.toString(otp));

        statusResponse.setStatus(200);
        statusResponse.setMessage("Authentification reussie");
        statusResponse.setData(new UserInfoResponse(userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                userDetails.getStatus(),
                roles, jwtCookie.getValue(), userDetails.getProfil(), userDetails.getPhone()));

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(statusResponse);
    }

    @SuppressWarnings({ "rawtypes", "unused" })
    @PostMapping("/checkUser")
    public ResponseEntity<?> checkUser(CheckUserRequest checkUserRequest) {

        StatusResponse statusResponse = new StatusResponse();
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        if (userRepository.existsByUsername(checkUserRequest.getUsername())) {
            String message = "est déjà utilise";
            message.replace("e", "é");

            statusResponse.setStatus(400);
            statusResponse.setMessage(checkUserRequest.getUsername() + message);
            return ResponseEntity.badRequest().body(statusResponse);
        }

        if (userRepository.findByPhone(checkUserRequest.getPhone()) != null) {
            String message = "est déjà utilise";
            message.replace("e", "é");

            statusResponse.setStatus(400);
            statusResponse.setMessage(checkUserRequest.getPhone() + message);
            return ResponseEntity.badRequest().body(statusResponse);
        }

        statusResponse.setStatus(200);
        statusResponse.setMessage("Cet utilisateur n'existe pas");

        return ResponseEntity.ok().body(statusResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(RegisterRequest signUpRequest) {

        @SuppressWarnings("rawtypes")
        StatusResponse statusResponse = new StatusResponse();

        if (userRepository.existsByUsername(signUpRequest.getName())) {
            String message = "est déjà utilise";
            message.replace("e", "é");

            statusResponse.setStatus(400);
            statusResponse.setMessage(signUpRequest.getName() + message);
            return ResponseEntity.badRequest().body(statusResponse);
        }

        // if (userRepository.findByPhone(signUpRequest.getPhone())) {
        // String message = "est déjà utilisé";
        // message.replace("é", "e");

        // statusResponse.setStatus(400);
        // statusResponse.setMessage(signUpRequest.getPhone() + message);
        // return ResponseEntity.badRequest().body(statusResponse);
        // }

        User user = new User(signUpRequest.getName(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()), signUpRequest.getStatus(), signUpRequest.getProfil(),
                signUpRequest.getPhone());


        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Le rôle n'est pas trouvé"));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Le rôle n'est pas trouvé"));
                        roles.add(adminRole);

                        break;
                    case "buyer":
                        Role clientRole = roleRepository.findByName(ERole.ROLE_BUYER)
                                .orElseThrow(() -> new RuntimeException("Le rôle n'est pas trouvé"));
                        roles.add(clientRole);

                        break;
                    case "seller":
                        Role bailleurRole = roleRepository.findByName(ERole.ROLE_SELLER)
                                .orElseThrow(() -> new RuntimeException("Le rôle n'est pas trouvé"));
                        roles.add(bailleurRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Le rôle n'est pas trouvé"));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        String message = "Enregistrement effectue";
        message.replace("e", "é");

        statusResponse.setStatus(200);
        statusResponse.setMessage(message);

        return ResponseEntity.ok(statusResponse);
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        @SuppressWarnings("rawtypes")
        StatusResponse statusResponse = new StatusResponse();

        String message = "Vous avez été déconnecte !";
        message.replace("e", "é");

        statusResponse.setStatus(200);
        statusResponse.setMessage("Vous avez été déconnecté !");
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(statusResponse);
    }

    @SuppressWarnings("rawtypes")
    @PutMapping("/user")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SELLER') or hasRole('USER') or hasRole('BUYER')")
    public ResponseEntity update(User user) {

        StatusResponse statusResponse = new StatusResponse();
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        try {
            User userUpdated = this.userService.updateUser(user);
            if (userUpdated != null) {
                String message = "Modification effectuée";
                message.replace("é", "e");

                statusResponse.setStatus(200);
                statusResponse.setMessage("Modification effectuée");
                statusResponse.setData(userUpdated);
                return ResponseEntity.status(HttpStatus.OK).body(statusResponse);
            } else {
                statusResponse.setStatus(400);
                statusResponse.setMessage("Echec de modification");
            }

        } catch (ValueException e) {
            statusResponse.setStatus(400);
            statusResponse.setMessage(e.getMessage());
        } catch (Exception e) {
            statusResponse.setStatus(400);
            statusResponse.setMessage("Erreur interne");
        }
        return ResponseEntity.status(httpStatus).body(statusResponse);
    }

    @SuppressWarnings("rawtypes")
    @PutMapping("/user/{userId}/{password}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SELLER') or hasRole('USER') or hasRole('BUYER')")
    public ResponseEntity updatePassword(@PathVariable("userId") Long id, @PathVariable("password") String password) {

        StatusResponse statusResponse = new StatusResponse();
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        try {
            User userUpdated = this.userService.updatePassword(id, password);
            if (userUpdated != null) {
                String message = "Modification effectuée";
                message.replace("é", "e");

                statusResponse.setStatus(200);
                statusResponse.setMessage("Modification effectuée");
                statusResponse.setData(userUpdated);
                return ResponseEntity.status(HttpStatus.OK).body(statusResponse);
            } else {
                statusResponse.setStatus(400);
                statusResponse.setMessage("Echec de modification");
            }

        } catch (ValueException e) {
            statusResponse.setStatus(400);
            statusResponse.setMessage(e.getMessage());
        } catch (Exception e) {
            statusResponse.setStatus(400);
            statusResponse.setMessage("Erreur interne");
        }
        return ResponseEntity.status(httpStatus).body(statusResponse);
    }

    @SuppressWarnings("rawtypes")
    @DeleteMapping("/user/{userId}")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity delete(@PathVariable("userId") Long userId) {

        StatusResponse statusResponse = new StatusResponse();
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        try {
            boolean isDeleted = this.userService.deleteUserById(userId);
            if (isDeleted == true) {
                String message = "Suppression effectuée";
                message.replace("é", "e");

                statusResponse.setStatus(200);
                statusResponse.setMessage(message);
                return ResponseEntity.status(200).body(statusResponse);
            } else {
                statusResponse.setStatus(400);
                statusResponse.setMessage("Echec de suppression");
            }

        } catch (ValueException e) {
            statusResponse.setStatus(400);
            statusResponse.setMessage(e.getMessage());
        } catch (Exception e) {
            statusResponse.setStatus(400);
            statusResponse.setMessage("Erreur interne");
        }
        return ResponseEntity.status(httpStatus).body(statusResponse);
    }

    @SuppressWarnings("rawtypes")
    @GetMapping("/user")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity getAllUsers() {

        StatusResponse statusResponse = new StatusResponse();
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        try {
            List<User> user = this.userService.fetchUserList();
            statusResponse.setStatus(200);
            statusResponse.setData(user);
            return ResponseEntity.status(HttpStatus.OK).body(statusResponse);
        } catch (ValueException e) {
            statusResponse.setStatus(400);
            statusResponse.setMessage(e.getMessage());
        } catch (Exception e) {
            statusResponse.setStatus(400);
            statusResponse.setMessage("Erreur interne");
        }
        return ResponseEntity.status(httpStatus).body(statusResponse);
    }

    @SuppressWarnings("rawtypes")
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SELLER') or hasRole('USER') or hasRole('BUYER')")
    public ResponseEntity getUserById(@PathVariable("userId") Long userId) {

        StatusResponse statusResponse = new StatusResponse();
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        try {
            User user = this.userService.getUserById(userId);
            statusResponse.setStatus(200);
            statusResponse.setData(user);
            return ResponseEntity.status(HttpStatus.OK).body(statusResponse);
        } catch (ValueException e) {
            statusResponse.setStatus(400);
            statusResponse.setMessage(e.getMessage());
        } catch (Exception e) {
            statusResponse.setStatus(400);
            statusResponse.setMessage("Erreur interne");
        }
        return ResponseEntity.status(httpStatus).body(statusResponse);
    }
}
