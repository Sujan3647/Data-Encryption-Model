package in.nic.login.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
public class RSAUtil {
  private static final String ALGORITHM = "AES";
  public static String encypt(String data , String secretKey) throws Exception{
    SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), ALGORITHM);
    Cipher cipher = Cipher.getInstance(ALGORITHM);
    cipher.init(Cipher.ENCRYPT_MODE, key);
    byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
    return Base64.getEncoder().encodeToString(encryptedBytes);
  }
  public static String decrypt(String data , String privateKey) throws Exception{
    SecretKeySpec key = new SecretKeySpec(privateKey.getBytes(StandardCharsets.UTF_8), ALGORITHM);
    Cipher cipher = Cipher.getInstance(ALGORITHM);
    cipher.init(Cipher.DECRYPT_MODE, key);
    byte[] decryptBytes = cipher.doFinal(Base64.getDecoder().decode(data));
    return new String(decryptBytes , StandardCharsets.UTF_8);
  }

}
