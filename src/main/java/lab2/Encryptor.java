package lab2;

import lombok.SneakyThrows;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.concurrent.BlockingQueue;

public class Encryptor implements Runnable{

    public static  int l=0;
    private BlockingQueue<Packet> packetsAnswer;
    private BlockingQueue<byte[]> encryptedPackets;
    private final byte[] poisonPill;
    private final Packet poisonPill2;
    private final int poisonPillPerProducer;

    public Encryptor(BlockingQueue<Packet> packetsAnswer, BlockingQueue<byte[]> encryptedPackets, byte[] poisonPill, Packet poisonPill2, int poisonPillPerProducer){
        this.packetsAnswer = packetsAnswer;
        this.encryptedPackets=encryptedPackets;
        this.poisonPill=poisonPill;
        this.poisonPill2 = poisonPill2;
        this.poisonPillPerProducer = poisonPillPerProducer;
    }
    private static final String ALGO = "AES";
    private static final byte[] keyValue =
            new byte[]{'T', 'h', 'e', 'B', 'e', 's', 't',
                    'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y'};
    private  static Key key;

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
        return encVal;
    }

   public static String getALGO(){
        return ALGO;
   }
   public static Key getKey(){
        return key;
   }

   private static Key generateKey() throws Exception {
       Key key = new SecretKeySpec(keyValue, ALGO);
        return key;
   }

    @SneakyThrows
    @Override
    public void run() {
        Packet pack = packetsAnswer.take();
        while(!pack.equals(poisonPill2)) {
            try {
                encryptedPackets.put(pack.encodePackage());
                pack = packetsAnswer.take();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        encryptedPackets.put(poisonPill);
        Thread.currentThread().interrupt();
    }
}