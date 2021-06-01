package lab2;

import java.util.Arrays;
import java.util.concurrent.BlockingQueue;

public class Sender implements  Runnable{

    private BlockingQueue<byte[]> encryptedPackets;
    /*private final int poisonPillPerProducer;*/
    static int r;
    private final byte[] poisonPill;
    public Sender(BlockingQueue<byte[]> encryptedPackets,/*, int poisonPillPerProducer, int poisonPill*/byte[] poisonPill){
        this.encryptedPackets = encryptedPackets;
        /*this.poisonPillPerProducer = poisonPillPerProducer;
        this.poisonPill = poisonPill;*/
        this.poisonPill = poisonPill;
    }
    public String CheckIfSended() throws Exception {
        Packet packet = new Packet(encryptedPackets.take());
        return packet.bMsq.message;
    }

    @Override
    public void run() {
//int r=t;
        while (true) {
            try {
               // if (Arrays.equals(encryptedPackets.take(), poisonPill)) { System.out.println("Interrupteed");Thread.currentThread().interrupt();}
                /*if (Arrays.equals(encryptedPackets.take(), poisonPill)) {Thread.currentThread().interrupt();}
                else*/
                System.out.println(CheckIfSended());
                 r++;
                //if (Arrays.equals(encryptedPackets.take(), poisonPill)) System.out.println("Eeeeeend");
                /*l++;
                System.out.println("Sended packet "+l);*/
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
