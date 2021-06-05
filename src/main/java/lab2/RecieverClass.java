package lab2;

import lombok.SneakyThrows;

import java.util.concurrent.BlockingQueue;

public class RecieverClass implements Reciever, Runnable {

private Packet poisonPill;
    private BlockingQueue<Packet> packetsAnswer;
    private BlockingQueue<Packet> packets;
    public RecieverClass(BlockingQueue<Packet> packetsAnswer, BlockingQueue<Packet> packets, Packet poisonPill){
        this.poisonPill = poisonPill;
        this.packets = packets;
        this.packetsAnswer = packetsAnswer;
    }

    //dd
    @Override
    public void recieveMessage(Packet pack) throws InterruptedException {

       Message message = new Message(pack.bMsq.cType, pack.bMsq.bUserId, "OK");
       Packet packAnswer = new Packet(pack.bSrc,message, pack.bPktId);
       packetsAnswer.put(packAnswer);
    }

    @SneakyThrows
    @Override
    public void run() {
        Packet  pack = packets.take();
        while(!pack.equals(poisonPill)) {
            try {
                recieveMessage(pack);
                pack = packets.take();
            } catch (InterruptedException e) {
               Thread.currentThread().interrupt();
            }
        }
        packetsAnswer.put(poisonPill);
        Thread.currentThread().interrupt();
    }
}
