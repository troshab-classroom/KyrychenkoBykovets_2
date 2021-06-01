package lab2;





import lombok.Data;

import java.nio.ByteBuffer;

@Data
public class Packet {

    public final static Byte BMagic =  0x13;
   // static int bPktId;
    Byte bSrc;
     Long bPktId = 0L;
   static long fPktId=0;
    Integer wLen;
    Short wCrc16;
    Message bMsq ;
    Short wCrc16_2;


    public Packet(Byte bSrc, Message bMsq ) {
        this.bSrc = bSrc;
        this.bMsq = bMsq;
        this.bPktId = fPktId++;

        wLen = bMsq.message.length();

    }
    public Packet(Byte bSrc, Message bMsq,  Long bPltId ) {
        this.bSrc = bSrc;
        this.bMsq = bMsq;
        this.bPktId = bPltId;

        wLen = bMsq.message.length();

    }
    public  byte[] encodePackage () throws Exception {

        byte[] firstPart =  ByteBuffer.allocate(14).put(BMagic)
                .put(bSrc).putLong(bPktId)
                .putInt(wLen).array();

        wCrc16 = (CRC16.crc16(firstPart));

        byte[] secondPart1 =  (ByteBuffer.allocate( bMsq.getMessageLength()).put(bMsq.messageToPacket()).array());
        byte[] secondPart =  (ByteBuffer.allocate( Encryptor.encrypt(bMsq.messageToPacket()).length).put( Encryptor.encrypt(bMsq.messageToPacket())).array()) ;
        wCrc16_2 = CRC16.crc16(secondPart1);
        return  ByteBuffer.allocate( firstPart.length + 4 + secondPart.length)
                .put(firstPart).putShort(wCrc16).put(secondPart).putShort(wCrc16_2).array();

    }

    public Packet(byte[] encodedPacket) throws Exception{

        ByteBuffer byteBuffer =  ByteBuffer.wrap(encodedPacket);
        Byte bMagic = byteBuffer.get();
        if(!bMagic.equals(BMagic)) throw new Exception ("bMagic is not correct ");

        bSrc = byteBuffer.get();
        bPktId = byteBuffer.getLong();
        wLen =  byteBuffer.getInt();
        wCrc16 = byteBuffer.getShort();

        byte[] head = byteBuffer.allocate(14).put(bMagic).put(bSrc).putLong(bPktId).putInt(wLen).array();
        if(CRC16.crc16(head)!= wCrc16) throw new IllegalArgumentException("crc16_1 is not correct");

        bMsq =  new Message();
        byte[] messageBytes = new byte[encodedPacket.length-18];
        byteBuffer.get(messageBytes);
        byte[] secondPart1 = messageBytes;
        byte[] secondPart= Decryptor.decrypt(messageBytes);
        ByteBuffer encodeMessage = byteBuffer.wrap(secondPart);
        bMsq.setcType(encodeMessage.getInt());
        bMsq.setbUserId(encodeMessage.getInt());
        ByteBuffer encodeMessage2 = byteBuffer.wrap(secondPart);

        byte[] messageBody = new byte[wLen];
        byte[] messageBody2 = new byte[wLen];
        try{
            encodeMessage.get(messageBody);
         //   encodeMessage2.get(messageBody2);
        } catch(Exception ex){
        }
     //   System.out.println(new String(messageBody2) +" encoded message");
        bMsq.setMessage(new String (messageBody));
        wCrc16_2 = byteBuffer.getShort();
        byte[] message = ByteBuffer.allocate(8+wLen).putInt(bMsq.cType).putInt(bMsq.bUserId).put(messageBody).array();

        if(CRC16.crc16(message)!= wCrc16_2)
            throw new IllegalArgumentException("crc16_2 is not correct");

    }

}