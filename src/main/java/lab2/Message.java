package lab2;

import java.nio.ByteBuffer;
import lombok.Data;
import java.nio.ByteBuffer;

    @Data
    public class Message {

        Integer cType;
        Integer bUserId;
        String message;


        public Message(){
        }
        public Message(Integer cType, Integer bUserId, String message) {
            this.cType = cType;
            this.bUserId = bUserId;
            this.message = message;
        }

        public byte[] messageToPacket(){
            return ByteBuffer.allocate(this.getMessageLength())
                    .putInt(cType).
                            putInt(bUserId).put(message.getBytes()).array();
        }
        public int Length() throws Exception {
            return myCipher.encrypt(this.messageToPacket()).length;
        }
        public byte[] slpart(){
            return ByteBuffer.allocate(8).putInt(cType).putInt(bUserId).array();
        }
        public byte[] mespart(){
            return ByteBuffer.allocate(message.length()).put(message.getBytes()).array();
        }
        public int getMessageLength(){

            return message.length()+8;
        }

        public  void setcType(Integer cType){
            this.cType = cType;
        }
        public void setbUserId(Integer bUserId){
            this.bUserId = bUserId;
        }

        public void setMessage( String message){
            this.message = message;

        }
    }


