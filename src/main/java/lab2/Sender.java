package lab2;

import lombok.SneakyThrows;

import java.util.concurrent.BlockingQueue;

public class Sender implements  Runnable{

    private BlockingQueue<byte[]> encryptedPackets;

    private Packet poisonPill;
    public Sender(BlockingQueue<byte[]> encryptedPackets,Packet poisonPill){
        this.encryptedPackets = encryptedPackets;
        this.poisonPill = poisonPill;
    }
    public String CheckIfSended(Packet packet) {

        return packet.bMsq.message;
    }

    @SneakyThrows
    @Override
    public void run() {
        Packet packet = new Packet(encryptedPackets.take());
        while (!packet.equals(poisonPill)) {
            try {
                System.out.println(CheckIfSended(packet));
                packet = new Packet(encryptedPackets.take());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    Thread.currentThread().interrupt();
    }
}
