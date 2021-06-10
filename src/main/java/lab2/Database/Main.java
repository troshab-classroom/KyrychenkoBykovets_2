package lab2.Database;

public class Main {
    public static void main(String [] args){
        DBCommands comm=new DBCommands();
        comm.Create();
        Product prod1=comm.Insert(new Product("apple", 12, 13));
        //System.out.println(comm.read());
        Product prod2=comm.Insert(new Product("cherry", 45, 56));
        // comm.Delete(prod1);

        // System.out.println(comm.read());
        System.out.println(comm.listByPrice(">14"));

        System.out.println(comm.Read());
    }
}
