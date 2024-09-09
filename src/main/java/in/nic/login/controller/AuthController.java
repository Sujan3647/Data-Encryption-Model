package in.nic.login.controller;

import in.nic.login.dto.AuthRequest;
import in.nic.login.dto.AuthResponse;
import in.nic.login.dto.RegisterRequest;
import in.nic.login.repository.ClientRepository;
import in.nic.login.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;
    private final ClientRepository clientRepository;

    @PostMapping("/init")
    public ResponseEntity<AuthResponse> init (@RequestBody AuthRequest request) {
        if (clientRepository.findByClientId(request.getClientId()).isPresent()) {
            try {
                AuthResponse response = service.authenticate(request);
                return new ResponseEntity<>(response, HttpStatus.OK);

            } catch (Exception e) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        } else {
            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setClientId(request.getClientId());
            registerRequest.setClientSecret(request.getClientSecret());

            try {
                AuthResponse response = service.register(registerRequest);
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            } catch (Exception e) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        }
    }
}

