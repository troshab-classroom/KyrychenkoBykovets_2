package lab2;

import lombok.SneakyThrows;

import javax.crypto.Cipher;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;

public class Decryptor implements Runnable{
    public static  int l=0;
    private final byte[] poisonPill;
    private int poisonPillPerProducer;
    private BlockingQueue<byte[]> packetsToDecrypt;
    private BlockingQueue<Packet> decryptedPackets;
    public Decryptor(byte[] poisonPill,int poisonPillPerProducer,BlockingQueue<byte[]> packetsToDecrypt, BlockingQueue<Packet> decryptedPackets){
        this.poisonPill = poisonPill;
        this.poisonPillPerProducer = poisonPillPerProducer;
        this.packetsToDecrypt = packetsToDecrypt;
this.decryptedPackets=decryptedPackets;

    }
    public static byte[] decrypt(byte[] encryptedData) throws Exception {

        Cipher c = Cipher.getInstance(Encryptor.getALGO());
        c.init(Cipher.DECRYPT_MODE, Encryptor.getKey());
        byte[] decValue = c.doFinal(encryptedData);
        return decValue;
    }

    @SneakyThrows
    @Override
    public void run() {
        byte[] arr =  packetsToDecrypt.take();
        while(!Arrays.equals(arr, poisonPill)) {
            try {
                decryptedPackets.put(new Packet(arr));
                arr =  packetsToDecrypt.take();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        decryptedPackets.put(new Packet(poisonPill));
        Thread.currentThread().interrupt();
        }
    }

