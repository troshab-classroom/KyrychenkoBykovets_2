package lab2;


import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

public class myCipher {

    private static final String ALGO = "AES";
    private static final byte[] keyValue =
            new byte[]{'T', 'h', 'e', 'B', 'e', 's', 't',
                    'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y'};
    static Key key;

    static {
        try {
            key = generateKey();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static byte[] encrypt(byte[] Data) throws Exception {

        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(Data);
        //String encryptedValue = new BASE64Encoder().encode(encVal);
        return encVal;
    }

    public static byte[] decrypt(byte[] encryptedData) throws Exception {

        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decValue = c.doFinal(encryptedData);
        return decValue;
    }

    private static Key generateKey() throws Exception {
        Key key = new SecretKeySpec(keyValue, ALGO);
        return key;
    }
}