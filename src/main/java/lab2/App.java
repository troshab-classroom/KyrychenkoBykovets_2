package lab2;

public class App {
    public static void main(String[] args) throws Exception {
        Message mes = new Message(2, 3, "success");
        Packet pack = new Packet((byte) 1, mes);
        byte[] encoded = pack.encodePackage();//перетворюємо пакет в масив байтів та зашифровуємо повідомлення
        System.out.println(pack);

        Packet decode1 = new Packet(encoded);//розшифровуємо повідомлення, формуємо масив байтів в пакет
        System.out.println(decode1);
    }
}
