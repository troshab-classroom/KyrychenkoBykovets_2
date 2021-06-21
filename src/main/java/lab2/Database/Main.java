package lab2.Database;

import java.sql.SQLException;

public class Main {
    public static void main(String [] args) throws SQLException {
        DBCommands comm=new DBCommands();

        comm.Create();
        Product prod1=comm.Insert(new Product("apple", 12, 13));
        //System.out.println(comm.read());
        Product prod2=new Product("cherry", 45, 56);
        // comm.Delete(prod1);
comm.updateProduct(prod1.getId(), prod2);
        // System.out.println(comm.read());
        //System.out.println(comm.listByPrice(">14"));
//comm.listByName("apple");
        System.out.println(comm.getProdById(prod1.getId()));
    }
}
