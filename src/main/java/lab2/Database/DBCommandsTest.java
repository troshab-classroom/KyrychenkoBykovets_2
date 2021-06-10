package lab2.Database;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DBCommandsTest {
@Test
    void testInsert(){
    DBCommands comm=new DBCommands();
    comm.Create("Test");
    int size=comm.Read().size();
    Product prod=comm.Insert(new Product("pineapple", 45, 86));
    assertEquals(size,comm.Read().size()-1 );
}
@Test
    void testDelete(){
        DBCommands comm=new DBCommands();
        comm.Create("Test2");
        comm.DeleteAll();
        Product prod=comm.Insert(new Product("pineapple", 45, 86));
    comm.Insert(new Product("meat", 123, 23));
    comm.Insert(new Product("milk", 23, 90));
    comm.Delete(prod);
        assertEquals(2,comm.Read().size() );
        comm.DeleteAll();
    }
    @Test
    void testCriteria(){
        DBCommands comm=new DBCommands();
        comm.Create("Test3");
        comm.DeleteAll();
       comm.Insert(new Product("pineapple", 45, 86));
        comm.Insert(new Product("meat", 123, 23));
        comm.Insert(new Product("meat", 245, 3));
        comm.Insert(new Product("milk", 23, 90));
        List<Product> res=comm.listByName("meat");
        assertEquals(2, res.size() );
        comm.DeleteAll();
    }
    @Test
    void testCriteriaPrice(){
        DBCommands comm=new DBCommands();
        comm.Create("Test3");
        comm.DeleteAll();
        comm.Insert(new Product("pineapple", 45, 86));
        comm.Insert(new Product("meat", 123, 23));
        comm.Insert(new Product("meat", 245, 3));
        comm.Insert(new Product("milk", 23, 90));
        comm.Insert(new Product("juice", 10, 23));
        List<Product> res=comm.listByPrice(">24");
        assertEquals(3, res.size() );
        comm.DeleteAll();
    }

}