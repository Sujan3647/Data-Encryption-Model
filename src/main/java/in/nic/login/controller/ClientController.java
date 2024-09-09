package in.nic.login.controller;

import in.nic.login.dto.KeyPair;
import in.nic.login.dto.PushDataRequest;
import in.nic.login.dto.SignUpRequest;
import in.nic.login.model.Client;
import in.nic.login.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
public class ClientController {
    @Autowired
    private ClientService clientService;

    @PostMapping("/signup")
    public ResponseEntity<Client> signup(@RequestParam String clientId, @RequestBody SignUpRequest signUpRequest) {
        return clientService.signup(clientId, signUpRequest)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam long mobileNo) {
        Optional<String> otp = clientService.login(mobileNo);
        return otp.map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().body("Invalid mobile number"));
    }

    @PostMapping("/getkeys")
    public ResponseEntity<KeyPair> getKeys( @RequestParam int keySize) {
        if (keySize !=1024) {
            return ResponseEntity.badRequest().build();
        }
        KeyPair keyPair = clientService.getKeys(keySize);
        return ResponseEntity.ok(keyPair);
    }

    @PostMapping("/pushdata")
    public ResponseEntity<String> pushData( @RequestBody PushDataRequest pushDataRequest) {
        String transactionId = clientService.pushData(pushDataRequest.getToBeEncrypted(), pushDataRequest.getPublicKey());
        return ResponseEntity.ok(transactionId);
    }

    @GetMapping("/getdata")
    public ResponseEntity<String> getData( @RequestParam Map<String , String> valuesMap) throws Exception{
        Optional<String> encryptedData = clientService.getData(valuesMap.get("transactionId"));
        return encryptedData.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
