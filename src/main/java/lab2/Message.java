package lab2;

import java.nio.ByteBuffer;
import lombok.Data;

@Data
    public class Message {

        Integer cType;
        Integer bUserId;
        public String message;


        public Message(){
        }
        public Message(Integer cType, Integer bUserId, String message) {
            this.cType = cType;
            this.bUserId = bUserId;
            this.message = message;
        }

        enum commandTypes{
            GET_AMOUNT_OF_PRODUCT,
            WRITE_OFF_PRODUCT,
            ADD_PRODUCT,
            ADD_GROUP_OF_PRODUCTS,
            ADD_PRODUCT_TO_GROUP,
            SET_PRICE
        }
        public byte[] messageToPacket(){
            return ByteBuffer.allocate(this.getMessageLength())
                    .putInt(cType).
                            putInt(bUserId).put(message.getBytes()).array();
        }
        public int Length() throws Exception {
            return Encryptor.encrypt(this.messageToPacket()).length;
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


