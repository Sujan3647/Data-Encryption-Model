package in.nic.login.service;

import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@Service
public class KeyService {

    public Map<String, String> generateKeys(int keySize) throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(keySize);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        Map<String, String> keys = new HashMap<>();
        keys.put("private_key", new String(keyPair.getPrivate().getEncoded()));
        keys.put("public_key", new String(keyPair.getPublic().getEncoded()));

        return keys;
    }
}