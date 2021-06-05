package lab2;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

class AppTest {

    @org.junit.jupiter.api.Test
    void main() throws Exception {
        int BOUND = 10;
        int N_PRODUCERS = 4;
        int N_CONSUMERS = Runtime.getRuntime().availableProcessors();
        Packet stop=new Packet((byte)1, new Message(2,3,"End of thread"));
        byte[] poisonPill = stop.encodePackage();
        int poisonPillPerProducer = 1;
        int mod = N_CONSUMERS % N_PRODUCERS;

        BlockingQueue<byte[]> packetsToDecrypt = new LinkedBlockingQueue<>(BOUND);
        BlockingQueue<Packet> packets = new LinkedBlockingQueue<>(BOUND);
        BlockingQueue<Packet> packetsAnswer = new LinkedBlockingQueue<>(BOUND);
        BlockingQueue<byte[]> encryptedPackets = new LinkedBlockingQueue<>(BOUND);

        for (int i = 1; i < N_PRODUCERS; i++) {
            new Thread(new PacketGenerator(packetsToDecrypt, poisonPill, poisonPillPerProducer)).start();
        }
        for (int i = 1; i < N_PRODUCERS; i++) {
            new Thread(new Decryptor( poisonPill,poisonPillPerProducer, packetsToDecrypt, packets)).start();
        }
        for (int i = 1; i < N_PRODUCERS; i++) {
            new Thread(new RecieverClass(packetsAnswer, packets, stop)).start();
        }

        for (int i = 1; i < N_PRODUCERS; i++) {
            new Thread(new Encryptor(packetsAnswer, encryptedPackets, poisonPill, stop, poisonPillPerProducer)).start();
        }
        for (int i = 1; i < N_PRODUCERS; i++) {
            new Thread(new Sender(encryptedPackets, stop)).start();
        }
        //Assertions.assertTrue(Thread.interrupted());

      // Assertions.assertEquals(null,  packetsToDecrypt.poll());
        //Assertions.assertEquals(null,  packets.poll());
       // System.out.println(Thread.activeCount());
//System.out.println(Thread.activeCount());
        //System.out.println(Thread.activeCount());
       // Assertions.assertEquals(null,  packetsAnswer.poll());
        Assertions.assertEquals(null,  encryptedPackets.poll());

    }
}
