package lab2;

import java.util.concurrent.BlockingQueue;

public class Sender implements  Runnable{
    private BlockingQueue<byte[]> encryptedPackets;
    /*private final int poisonPillPerProducer;
    private final int poisonPill;*/
    public Sender(BlockingQueue<byte[]> encryptedPackets/*, int poisonPillPerProducer, int poisonPill*/){
        this.encryptedPackets = encryptedPackets;
        /*this.poisonPillPerProducer = poisonPillPerProducer;
        this.poisonPill = poisonPill;*/
    }
    public String CheckIfSended() throws Exception {
        Packet packet = new Packet(encryptedPackets.take());
        return packet.bMsq.message;
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println(CheckIfSended());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
