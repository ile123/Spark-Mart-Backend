package com.ilario.sparkmart.security;

import com.ilario.sparkmart.security.misc.AuthenticationRequest;
import com.ilario.sparkmart.security.misc.AuthenticationResponse;
import com.ilario.sparkmart.security.misc.RegisterRequest;
import com.ilario.sparkmart.security.misc.UserInformation;
import com.ilario.sparkmart.services.implementations.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:3000/")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserServiceImpl userService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request) {
        var existingUser = userService.checkIfEmailIsAlreadyUsed(request.email());
        if(existingUser) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/logout")
    public void logout() {
        SecurityContextHolder.clearContext();
    }

    @PostMapping("/email-exists")
    public ResponseEntity<Boolean> doesEmailExist(@RequestBody String request) {
        var jsonObject = new JSONObject(request);
        var emailExist = userService.checkIfEmailIsAlreadyUsed(jsonObject.get("email").toString());
        return ResponseEntity.ok(emailExist);
    }

    @GetMapping("/get-user-info")
    public ResponseEntity<UserInformation> getUserByToken(@RequestHeader (name="Authorization") String header) {
        var token = header.substring(7);
        String[] pieces = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String payload = new String(decoder.decode(pieces[1]));
        var jsonObject = new JSONObject(payload);
        var user = userService.getUserByEmail(jsonObject.get("sub").toString());
        var userInformation = new UserInformation(user.id(), user.firstName(), user.role());
        return ResponseEntity.ok(userInformation);
    }

    @GetMapping("/does-administrator-exist")
    public ResponseEntity<Boolean> doesAdministratorExist() {
        var response = userService.doesAdministratorExist();
        return ResponseEntity.ok(response);
    }
}
