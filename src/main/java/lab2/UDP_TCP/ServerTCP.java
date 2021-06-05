package lab2.UDP_TCP;

import lab2.ClientPacket;
import lab2.Packet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

public class ServerTCP implements Serializable {

    public static final Queue<ClientPacket> ANSWERS = new ConcurrentLinkedQueue<>();
    private final ConcurrentMap<Byte, ClientHandler> clients = new ConcurrentHashMap<>();
  //  private final ConcurrentMap<Byte, SocketAddress> clientsAddress = new ConcurrentHashMap<>();
   // ObjectInputStream ois;
    ServerSocket serverSocket;
    Socket socket;
    private static int port = 9876;

    public ServerTCP() throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("connected");
    }

    public void start() {

        new Thread(() -> {
            try {
                receiveReply();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "Server receiver ").start();

       /*new Thread(() -> {
            try {
                send();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "Server sender ").start();*/
    }

    public void send() throws IOException, ClassNotFoundException {


        while (true) {
            try {

                ClientPacket packet = ANSWERS.poll();
                if (packet != null) {
                    {

                        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                        //write object to Socket
                        oos.writeObject(packet.getPacket());
                     //   oos.close();
                        //

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void receiveReply() throws Exception {
        while (true) {
            System.out.println("Waiting for the client request");

            socket = serverSocket.accept();
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            //convert ObjectInputStream object to String
            byte[] messagebytes = (byte[]) ois.readObject();
            System.out.println("Message Received: " + messagebytes);
            Packet pack = new Packet(messagebytes);

            clients.computeIfAbsent(pack.bSrc, clientID -> new ClientHandler()).accept(pack);
            ClientPacket packet = ANSWERS.poll();
          //  ois.close();
            // ClientPacket packet = PACKETS_TO_SEND.poll();
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            if (packet != null) {
                {


                    //write object to Socket
                    oos.writeObject(packet.getPacket());
                  //
                    //

                }
               // ois.close();
              //  oos.close();
                socket.close();

                ;

                ;
                //clientsAddress.put(response.bSrc, dataPacket.getSocketAddress());
            }
        }
    }
}
