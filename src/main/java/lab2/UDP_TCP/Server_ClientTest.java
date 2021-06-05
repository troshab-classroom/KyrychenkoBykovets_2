package lab2.UDP_TCP;

import lab2.Packet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.SocketException;


class Server_ClientTest {
    @Test
    void checkWorkUDP() throws SocketException {
       StoreServerUDP server=new StoreServerUDP(1761);
        server.start();
        new Thread(()-> {
            StoreClientUDP client2 ;
            try {
                client2 = new StoreClientUDP((byte) 2);
                Packet packet=client2.sendReceive("Request from client "+ 2);
                Assertions.assertEquals(packet.bSrc, (byte)2);
                Assertions.assertEquals(packet.getBMsq().message, "Ok");
                Assertions.assertEquals(packet.bPktId, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ).start();

    }

    @Test
    void checkWorkTCP() throws IOException {
        ServerTCP server=new ServerTCP();
        server.start();
        for (byte i = 0; i < 10; i++) {


            byte finalI = i;
            new Thread(() -> {
                ClientTCP client2 ;
                try {
                    client2 = new ClientTCP((byte) finalI);
                    client2.send("Hello from client "+ finalI);

                    Packet packet = client2.receive();
                    Assertions.assertEquals(packet.bSrc, (byte) 2);
                    Assertions.assertEquals(packet.getBMsq().message, "Ok");
                    Assertions.assertEquals(packet.bPktId, 0);
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
            ).start();
        }
    }
}