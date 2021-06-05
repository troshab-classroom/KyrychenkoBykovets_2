package lab2.UDP_TCP;

import lab2.Message;
import lab2.Packet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.Socket;
public class ClientTCP {

    InetAddress host = InetAddress.getLocalHost();
    Socket socket = new Socket(host.getHostName(), 9876);;
    ObjectOutputStream oos = null;
    ObjectInputStream ois = null;
    byte clientID;

    public ClientTCP(byte clientID) throws IOException {
        this.clientID = clientID;
    }

    public void send(String message) throws Exception {
        Packet response = new Packet(clientID, new Message(1, 2, message), 120L);
        byte[] responseBytes = response.encodePackage();
      //  socket =
        //write to socket using ObjectOutputStream
         oos = new ObjectOutputStream(socket.getOutputStream());
        System.out.println("Sending request to Socket Server");

        oos.writeObject(responseBytes);
    }


    public Packet receive() throws Exception {

         //   if(!socket.isClosed())
            ois = new ObjectInputStream(socket.getInputStream());
            byte[] messagebytes = (byte[]) ois.readObject();
              System.out.println("Message: " + messagebytes);


            return new Packet(messagebytes);

        }

    }

