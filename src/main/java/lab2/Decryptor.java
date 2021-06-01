package lab2;

import javax.crypto.Cipher;
import java.util.concurrent.BlockingQueue;

public class Decryptor implements Runnable{
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

    @Override
    public void run() {
        while(true) {
            try {
                Decryptor.decrypt(packetsToDecrypt.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
