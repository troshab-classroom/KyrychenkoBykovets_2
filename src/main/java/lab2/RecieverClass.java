package lab2;

import java.util.concurrent.BlockingQueue;

public class RecieverClass implements Reciever, Runnable {
    /*private final int poisonPill;*/
    private BlockingQueue<Packet> packetsAnswer;
    private BlockingQueue<Packet> packets;
    public RecieverClass(/*int poisonPill,*/ BlockingQueue<Packet> packetsAnswer, BlockingQueue<Packet> packets){
        /*this.poisonPill = poisonPill;*/
        this.packets = packets;
        this.packetsAnswer = packetsAnswer;
    }
    //dd
    @Override
    public void recieveMessage() throws InterruptedException {
        Packet  pack = packets.take();
       Message message = new Message(pack.bMsq.cType, pack.bMsq.bUserId, "OK");
       Packet packAnswer = new Packet(pack.bSrc,message, pack.bPktId);
       packetsAnswer.put(packAnswer);

    }

    @Override
    public void run() {
        while(true) {
            try {
                recieveMessage();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
