package lab2.UDP_TCP;


import lab2.Packet;

public class ClientTest_UDP {
    public static void main(String[] args) {
        for (byte i = 0; i < 5; i++) {
            byte finalI = i;
            new Thread(()-> {
                StoreClientUDP client2 ;
                try {
                    client2 = new StoreClientUDP(finalI);
                    Packet packet=client2.sendReceive("Request from client "+ finalI);
                    System.out.println("Response to client "+ finalI +"  - "+packet.getBMsq().message);
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
            ).start();
        }


    }

}
