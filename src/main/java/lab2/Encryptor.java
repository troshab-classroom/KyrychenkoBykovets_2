package lab2;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.concurrent.BlockingQueue;

public class Encryptor implements Runnable{

    private BlockingQueue<Packet> packetsAnswer;
    private BlockingQueue<byte[]> encryptedPackets;
    /*private final int poisonPill;
    private final int poisonPillPerProducer;*/

    public Encryptor(BlockingQueue<Packet> packetsAnswer, BlockingQueue<byte[]> encryptedPackets/*, int poisonPill, int poisonPillPerProducer*/){
        this.packetsAnswer = packetsAnswer;
        this.encryptedPackets=encryptedPackets;
        /*this.poisonPill = poisonPill;
        this.poisonPillPerProducer = poisonPillPerProducer;*/
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
        //String encryptedValue = new BASE64Encoder().encode(encVal);
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

    @Override
    public void run() {
        while(true) {
            try {
               Packet pack =  packetsAnswer.take();
                encryptedPackets.put(pack.encodePackage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}