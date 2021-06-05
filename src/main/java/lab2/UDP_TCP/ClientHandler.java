package lab2.UDP_TCP;


import lab2.ClientPacket;
import lab2.Message;
import lab2.Packet;


import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ClientHandler extends Thread {

    private final Queue<Packet> queue;
    public ClientHandler(){
        queue=new ConcurrentLinkedQueue<>();
        start();

    }
    public void accept(Packet packet){
        queue.add(packet);
    }
    @Override
    public void run(){
        while(true){
            try{
        Packet packet=queue.poll();
        if(packet!=null){
            System.out.println("received: "+ packet.getBMsq().message);
        StoreServerUDP.ANSWERS.add(new ClientPacket(new Packet((byte)1, new Message(5,6,"Ok"), packet.bPktId),packet.getBSrc()));
        }
            } catch (Exception e)
            {

            }
        }

    }

}
