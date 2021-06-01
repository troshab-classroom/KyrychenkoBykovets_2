package lab2;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class App {
    public static void main(String[] args) throws Exception {
       /* Message mes = new Message(2, 3, "success");
        Packet pack = new Packet((byte) 1, mes);
        byte[] encoded = pack.encodePackage();//перетворюємо пакет в масив байтів та зашифровуємо повідомлення
        System.out.println(pack);

        Packet decode1 = new Packet(encoded);//розшифровуємо повідомлення, формуємо масив байтів в пакет
        System.out.println(decode1);*/
        /*Packet poisonPill = new Packet((byte) 0, new Message(6, 0, "EXIT"));
        byte[] poisonPillb = "EXIT".getBytes(StandardCharsets.UTF_8);
        System.out.println(Arrays.toString(poisonPillb));*/
        int BOUND = 10;
        int N_PRODUCERS = 4;
        int N_CONSUMERS = Runtime.getRuntime().availableProcessors();
        byte[] poisonPill = new byte[]{0,1};
        int poisonPillPerProducer = N_CONSUMERS / N_PRODUCERS;
        int mod = N_CONSUMERS % N_PRODUCERS;

        BlockingQueue<byte[]> packetsToDecrypt = new LinkedBlockingQueue<>(BOUND);
        BlockingQueue<Packet> packets = new LinkedBlockingQueue<>(BOUND);
        BlockingQueue<Packet> packetsAnswwer = new LinkedBlockingQueue<>(BOUND);
        BlockingQueue<byte[]> ebcryptedPackets = new LinkedBlockingQueue<>(BOUND);

        for (int i = 1; i < N_PRODUCERS; i++) {
            new Thread(new PacketGenerator(packetsToDecrypt, poisonPill, poisonPillPerProducer)).start();
        }
        for (int i = 1; i < N_PRODUCERS; i++) {
            new Thread(new Decryptor( poisonPill, packetsToDecrypt, packets)).start();
        }

        /*for (int j = 0; j < N_CONSUMERS; j++) {
            new Thread(new NumbersConsumer(queue, poisonPill)).start();
        }*/

    }
}
