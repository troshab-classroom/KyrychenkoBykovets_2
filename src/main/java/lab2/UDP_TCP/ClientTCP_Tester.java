package lab2.UDP_TCP;

import lab2.Packet;

public class ClientTCP_Tester {
        public static void main(String[] args) {
            for (byte i = 0; i < 10; i++) {
                client(i);
            }

        }
        private static void client(byte clientId){
            new Thread(()-> {
                ClientTCP client2 = null;
                try {
                    client2 = new ClientTCP(clientId);
                    client2.send("Hello from client "+ clientId);

                   Packet packet= client2.receive();

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
            ).start();
        }}
