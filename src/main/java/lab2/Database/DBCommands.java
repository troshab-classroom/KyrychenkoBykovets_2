package lab2.Database;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBCommands {

    private Connection connStr;
    public void Create()  {
        try{
             Class.forName("org.sqlite.JDBC");
            connStr = DriverManager.getConnection("jdbc:sqlite:inmemory");
            PreparedStatement st = connStr.prepareStatement("CREATE TABLE IF NOT EXISTS 'product' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'name' text, 'price' double, 'amount' double );");
            st.executeUpdate();
            st = connStr.prepareStatement("CREATE TABLE IF NOT EXISTS 'user' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'login' text unique, 'password' text);");
            st.executeUpdate();
        }catch(ClassNotFoundException e){
            System.out.println("JDBC not found");
            e.printStackTrace();
            System.exit(0);
        }
        catch(SQLException e){
            System.out.println("SQL");
            e.printStackTrace();
        }
    }
    public Product Insert(Product product){
        try{
            PreparedStatement statement=connStr.prepareStatement("INSERT INTO product(name, price, amount) VALUES (?,?,?)");
            statement.setString(1, product.getName());
            statement.setDouble(2, product.getPrice());
            statement.setDouble(3, product.getAmount());
            statement.executeUpdate();
            ResultSet resSet=statement.getGeneratedKeys();
            product.setId(resSet.getInt("last_insert_rowid()"));
           // statement.close();

        }catch(SQLException e){
            System.out.println("SQL");
            e.printStackTrace();
        }
        return product;
    }
    public User InsertUser(User user){
        try{
            PreparedStatement statement=connStr.prepareStatement("INSERT INTO user(login,password) VALUES (?,?)");
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());

            statement.executeUpdate();
            ResultSet resSet=statement.getGeneratedKeys();
            user.setId(resSet.getInt("last_insert_rowid()"));
            // statement.close();

        }catch(SQLException e){
            System.out.println("SQL");
            e.printStackTrace();
        }
        return user;
    }
    public User getUserByLogin(String login) throws SQLException {

        try {
            PreparedStatement statement = connStr.prepareStatement("SELECT * FROM user WHERE login =  ?");
            statement.setString(1, login);
            ResultSet resSet = statement.executeQuery();

            while (resSet.next()) {
                return new User(resSet.getInt(1),resSet.getString(2), resSet.getString(3));


            }
            resSet.close();
        } catch (SQLException e){
            System.out.println("SQL");
            e.printStackTrace();
        }
       return null;

    }
    public List<Product> Read(){
        List<Product> productList = new ArrayList<>();
        Product prod;
        try{
            Statement st = connStr.createStatement();
            ResultSet res = st.executeQuery("SELECT * FROM product");

            while (res.next()) {
                prod = new Product( res.getString(2), res.getDouble(3), res.getDouble(4));
                prod.setId((res.getInt(1)));
                productList.add(prod);
            }
            res.close();
            st.close();
        }catch(SQLException e){
            System.out.println("Не вірний SQL запит на вибірку даних");
            e.printStackTrace();
        }
        return productList;
    }
    public void Delete(Product product){
        try {
            PreparedStatement statement = connStr.prepareStatement("DELETE FROM product WHERE id=?");
            statement.setInt(1, product.getId());
            statement.executeUpdate();
        }catch (SQLException e){
            System.out.println("SQL");
            e.printStackTrace();
            throw new RuntimeException("Trouble",e);
        }
    }
    public void DeleteAll(){
        try {
            Statement st=connStr.createStatement();
            st.executeUpdate("DELETE FROM product");
        }catch (SQLException e){
            System.out.println("SQL");
            e.printStackTrace();
            throw new RuntimeException("Trouble",e);
        }
    }
    public void updatePrice(String name_product,  double quantity){
        try{

            System.out.println();
            PreparedStatement statement = connStr.prepareStatement("UPDATE product SET price = ? WHERE name = ?");
            statement.setDouble(1, quantity );
            statement.setString(2, name_product );


            statement.executeUpdate();

            statement.close();


        }catch (SQLException e){
            System.out.println("Не вірний SQL запит на вставку");
            e.printStackTrace();
        }

    }
    public void updateAmount(String name_product,  double quantity){
        try{

            System.out.println();
            PreparedStatement statement = connStr.prepareStatement("UPDATE product SET amount = ? WHERE name = ?");


            statement.setDouble(1, quantity );
            statement.setString(2, name_product );

            statement.executeUpdate();

            statement.close();


        }catch (SQLException e){
            System.out.println("Не вірний SQL запит на вставку");
            e.printStackTrace();
        }

    }
    public List<Product> listByName(String prodName){
        List<Product> productList = new ArrayList<>();
        Product prod;
        try{
            PreparedStatement statement = connStr.prepareStatement("SELECT * FROM product WHERE name=?");
            statement.setString(1, prodName);
            ResultSet res = statement.executeQuery();

            while (res.next()) {
                prod = new Product( res.getString(2), res.getDouble(3), res.getDouble(4));
                prod.setId((res.getInt(1)));
                productList.add(prod);
            }
            res.close();
        }catch(SQLException e){
            System.out.println("Не вірний SQL запит");
            e.printStackTrace();
        }
        return productList;
    }
    public List<Product> listByPrice(String price){
        List<Product> productList = new ArrayList<>();
        Product prod;
        try{
            PreparedStatement statement = connStr.prepareStatement("SELECT * FROM product WHERE price "+ price);
            ResultSet res = statement.executeQuery();
            while (res.next()) {
                prod = new Product( res.getString(2), res.getDouble(3), res.getDouble(4));
                prod.setId((res.getInt(1)));
                productList.add(prod);
            }
            res.close();
        }catch(SQLException e){
            System.out.println("Не вірний SQL запит");
            e.printStackTrace();
        }
        return productList;
    }
    public static void main(String [] args) throws SQLException {
        DBCommands comm=new DBCommands();
        comm.Create();
        //comm.InsertUser(new User("login2","password"));
        System.out.println(comm.getUserByLogin("login2"));


    }


}
