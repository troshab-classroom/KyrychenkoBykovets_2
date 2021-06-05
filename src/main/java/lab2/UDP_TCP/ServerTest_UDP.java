package lab2.UDP_TCP;


import lab2.UDP_TCP.StoreServerUDP;

public class ServerTest_UDP {
    public static void main(String[] args) throws Exception {
        StoreServerUDP server=new StoreServerUDP(3000);
        server.start();
    }
}
