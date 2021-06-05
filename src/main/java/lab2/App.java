package lab2;

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

    }
}
