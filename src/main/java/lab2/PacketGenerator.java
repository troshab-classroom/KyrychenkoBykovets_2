package lab2;

import lombok.SneakyThrows;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class PacketGenerator implements Runnable {
    public static  int l=0;
    private BlockingQueue<byte[] > packets;
    private final byte[] poisonPill;
    private final int poisonPillPerProducer;
    PacketGenerator(BlockingQueue<byte[]> packets, byte[] poisonPill, int poisonPillPerProducer){
        this.packets = packets;
        this.poisonPill = poisonPill;
        this.poisonPillPerProducer = poisonPillPerProducer;
    }
    public static byte[] generate() throws Exception {

        Random myRandom = new Random();
        int comandNumber = myRandom.nextInt(Message.commandTypes.values().length);
        String messageBody = Message.commandTypes.values()[myRandom.nextInt(6)].toString();
        Message message = new Message(comandNumber,2,messageBody);
        return new Packet((byte)3,message).encodePackage();
    }


    @SneakyThrows
    @Override
    public void run() {

        for (int i = 0; i < 4; i++) {
            try {
                packets.put(generate());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        for (int j = 0; j < poisonPillPerProducer; j++) {
                packets.put(poisonPill);
        }
        Thread.currentThread().interrupt();


    }
}
