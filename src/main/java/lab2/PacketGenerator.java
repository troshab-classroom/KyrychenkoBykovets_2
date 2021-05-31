package lab2;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class PacketGenerator implements Runnable {
    private BlockingQueue<byte[] > packets;
    PacketGenerator(BlockingQueue<byte[] > packets){
        this.packets = packets;
    }
    public static byte[] generate() throws Exception {

        Random myRandom = new Random();
        int comandNumber = myRandom.nextInt(Message.commandTypes.values().length);
        String messageBody = Message.commandTypes.values()[myRandom.nextInt(6)].toString();
        Message message = new Message(comandNumber,2,messageBody);
        return new Packet((byte)3,message).encodePackage();
    }


    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            try {
                packets.put(generate());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
