package carsharing;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.h2.jdbc.JdbcSQLSyntaxErrorException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CustomerDaoImp { // implements CompanyDao {

    List<Customer> customers;
    Statement stmt;

    public CustomerDaoImp(Statement stmt){
        this.stmt = stmt;
        try {
            String sql = "CREATE TABLE CUSTOMER (" +
                    "ID INT PRIMARY KEY AUTO_INCREMENT, " +
                    "NAME VARCHAR UNIQUE NOT NULL," +
                    "RENTED_CAR_ID INT," +
                    "CONSTRAINT fk_car FOREIGN KEY (RENTED_CAR_ID) " +
                    "REFERENCES CAR(ID)" +
                    ")";
            stmt.executeUpdate(sql);
            System.out.println("Created CUSTOMER table.");
        } catch(JdbcSQLSyntaxErrorException se){
            // System.out.println("Table CUSTOMER exists.");
        } catch(SQLException se) {
            se.printStackTrace();
        }
    }

    public Customer get(int id){
        Customer customer = customers.get(id-1);
        return customer;
    }

    //@Override
    public void updateCustomer() {
        customers = new ArrayList<>();
        String sql = "SELECT * FROM CUSTOMER";
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int lastId = rs.getInt("ID");
                String lastName = rs.getString("NAME");
                int carId = rs.getInt("RENTED_CAR_ID");
                Customer customer = new Customer(lastId, lastName, carId);
                customers.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void rentCar(Customer customer, Car car){
        String sql = "UPDATE CUSTOMER " +
                "SET RENTED_CAR_ID = " + car.getId() +
                "WHERE ID = " + customer.getId() + ";";
        try {
            stmt.executeUpdate(sql);
            System.out.println("You rented '" + car + "'");
        } catch(SQLException se) {
            //se.printStackTrace();
        }
    }

    public void returnCar(Customer customer) {
        String sql = "UPDATE CUSTOMER " +
                "SET RENTED_CAR_ID = NULL " +
                "WHERE ID = " + customer.getId() + ";";
        try {
            stmt.executeUpdate(sql);
            System.out.println("You've returned a rented car!");
        } catch(SQLException se) {
            //se.printStackTrace();
        }
    }

    //@Override
    public int size() {
        return customers.size();
    }

    //@Override
    public void addCustomer(String name) {
        String sql = "INSERT INTO CUSTOMER (NAME) VALUES ('" + name + "')";
        try {
            stmt.executeUpdate(sql);
            System.out.println("The customer was added!");
        } catch (JdbcSQLIntegrityConstraintViolationException e1) {
            System.out.println("The company already exists.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println();
    }

    public String toString() {
        String outputString ="";
        for (int i=0; i<customers.size(); i++) {
            outputString = outputString + (i+1) + ". " + customers.get(i) + "\n";
        }
        return outputString;
    }
}