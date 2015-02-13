package org.dataart.qdump.service.test;

import org.dataart.qdump.service.security.utils.CryptoUtils;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by artemvlasov on 08/02/15.
 */
public class CryptoUtilsTest {
    private CryptoUtils cryptoUtils;
    private String data;

    @Before
    public void setup() {
        cryptoUtils = new CryptoUtils();
        data = new String("Hello");
    }

    @Test
    public void encryptSuccess() {
        String encyptedString = cryptoUtils.encrypt(data);
        assertNotNull(encyptedString);
        assertNotEquals(data, encyptedString);
    }
    @Test(expected = RuntimeException.class)
    public void encryptErrorDataNull() {
        String encyptedString = cryptoUtils.encrypt(null);
        assertNull(encyptedString);
    }
    @Test
    public void decryptSuccess() {
        String encryptedString = cryptoUtils.encrypt(data);
        String secryptedString = cryptoUtils.decrypt(encryptedString);
        assertNotNull(secryptedString);
        assertEquals(data, secryptedString);
    }
    @Test(expected = RuntimeException.class)
    public void decryptErrorDataNull() {
        String decryptedString = cryptoUtils.decrypt(null);
        assertNotEquals(data, decryptedString);
    }
}
