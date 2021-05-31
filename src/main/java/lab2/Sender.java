package lab2;

import java.util.concurrent.BlockingQueue;

public class Sender implements  Runnable{
    private BlockingQueue<byte[]> encryptedPackets;
    public Sender(BlockingQueue<byte[]> encryptedPackets){
        this.encryptedPackets = encryptedPackets;
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
