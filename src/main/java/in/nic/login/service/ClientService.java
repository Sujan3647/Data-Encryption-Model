package in.nic.login.service;

import in.nic.login.dto.KeyPair;
import in.nic.login.dto.SignUpRequest;
import in.nic.login.config.DateConfig;
import in.nic.login.model.Client;
import in.nic.login.repository.ClientRepository;
import in.nic.login.util.RSAUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Optionals;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final Map<String, String> dataStore = new ConcurrentHashMap<>(); // Thread-safe data store
    public String publicKeyMain;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Optional<String> login(long mobileNo) {
        Optional<Client> optionalClient = clientRepository.findByMobileNo(mobileNo);
        return optionalClient.isPresent() ? Optional.of(generateOtp()) : Optional.empty();
    }

    private String generateOtp() {
        return String.format("%04d", new Random().nextInt(10000));
    }

    public Optional<Client> signup(String clientId, SignUpRequest signUpRequest) {
        if (!DateConfig.isValidDate(signUpRequest.getDob())) {
            throw new IllegalArgumentException("Invalid date format. Please use dd-MM-yyyy");
        }

        return clientRepository.findByClientId(clientId)
                .map(client -> {
                    client.setMobileNo(signUpRequest.getMobileNo());
                    client.setEmailId(signUpRequest.getEmailId());
                    client.setName(signUpRequest.getName());
                    client.setGender(signUpRequest.getGender());
                    client.setDob(signUpRequest.getDob());
                    client.setAddress(signUpRequest.getAddress());
                    return clientRepository.save(client);
                });
    }

    public KeyPair getKeys(int keySize) {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(keySize);
            java.security.KeyPair pair = keyGen.generateKeyPair();
            String privateKey = Base64.getEncoder().encodeToString(pair.getPrivate().getEncoded());
            String publicKey = KeyGenerators.string().generateKey();
            this.publicKeyMain = publicKey;
            return new KeyPair(privateKey, publicKey);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating RSA keys", e);
        }
    }

    public String pushData(String toBeEncrypted, String publicKeyString) {
        try {
            if (publicKeyMain == null || !publicKeyString.equals(publicKeyMain)) {
                return "Please provide me a valid key or generate new one";
            }
            String  encryptedData =  RSAUtil.encypt(toBeEncrypted.toString(), publicKeyString.toString());
            String transactionId = UUID.randomUUID().toString();
            dataStore.put(transactionId, encryptedData);
            return transactionId;
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting data", e);
        }
    }
    public Optional<String> getData(String transactionId) throws Exception {
        if (transactionId ==null) {
            return Optional.ofNullable("Please provide me valid public key");
        }
      return  Optional.ofNullable(RSAUtil.decrypt(dataStore.get(transactionId), publicKeyMain));
    }
}
