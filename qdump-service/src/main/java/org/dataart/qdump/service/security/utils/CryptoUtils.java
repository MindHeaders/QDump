package org.dataart.qdump.service.security.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

/**
 * Created by artemvlasov on 08/02/15.
 */
public class CryptoUtils {
    private final String ALGORITHM = "AES";
    private final String KEY = "qdmumpprject2015";

    public String encrypt(String data) {
        if(data == null) {
            throw new RuntimeException("Encrypted data could not be null");
        }
        Key key = new SecretKeySpec(KEY.getBytes(), ALGORITHM);
        Cipher cipher = null;
        String encodedData = null;
        try {
            cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedData = cipher.doFinal(data.getBytes());
            encodedData = Base64.encodeBase64URLSafeString(encryptedData);
        } catch (NoSuchAlgorithmException
                | NoSuchPaddingException
                | InvalidKeyException
                | IllegalBlockSizeException
                | BadPaddingException e) {
            e.printStackTrace();
        }
        return encodedData;
    }
    public String decrypt(String encryptedString) {
        if(encryptedString == null) {
            throw new RuntimeException("Decrypted data could not be null");
        }
        Key key = new SecretKeySpec(KEY.getBytes(), ALGORITHM);
        Cipher cipher = null;
        String decrypted = null;
        try {
            cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            decrypted = new String(cipher.doFinal(Base64.decodeBase64(encryptedString)));
        } catch (NoSuchAlgorithmException
                | NoSuchPaddingException
                | InvalidKeyException
                | IllegalBlockSizeException
                | BadPaddingException e) {
            e.printStackTrace();
        }
        return decrypted;
    }
}
