package lab2;

import javax.crypto.Cipher;
import java.util.concurrent.BlockingQueue;

public class Decryptor {
    private BlockingQueue<byte[] > packetsToDecrypt;
    public Decryptor(BlockingQueue<byte[] > packetsToDecrypt){
this.packetsToDecrypt = packetsToDecrypt;

    }
    public static byte[] decrypt(byte[] encryptedData) throws Exception {

        Cipher c = Cipher.getInstance(Encryptor.getALGO());
        c.init(Cipher.DECRYPT_MODE, Encryptor.getKey());
        byte[] decValue = c.doFinal(encryptedData);
        return decValue;
    }

}
