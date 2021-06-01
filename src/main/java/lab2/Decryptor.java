package lab2;

import javax.crypto.Cipher;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;

public class Decryptor implements Runnable{
    private final byte[] poisonPill;
   /* private final byte[] poisonPillPerProducer;*/
    private BlockingQueue<byte[]> packetsToDecrypt;
    private BlockingQueue<Packet> decryptedPackets;
    public Decryptor(byte[] poisonPill/*, byte[] poisonPillPerProducer, */,BlockingQueue<byte[]> packetsToDecrypt, BlockingQueue<Packet> decryptedPackets){
        this.poisonPill = poisonPill;
       /* this.poisonPillPerProducer = poisonPillPerProducer;*/
        this.packetsToDecrypt = packetsToDecrypt;
this.decryptedPackets=decryptedPackets;

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
                byte[] arr =  packetsToDecrypt.take();
                if (Arrays.equals(arr, poisonPill)) return;
                decryptedPackets.put(new Packet(arr));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        }
    }

