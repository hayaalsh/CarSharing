package carsharing;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.h2.jdbc.JdbcSQLSyntaxErrorException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CarDaoImp implements CarDao {

    List<Car> cars;
    Statement stmt;
    Company company;

    public CarDaoImp(Statement stmt){
        this.stmt = stmt;
        try {
            String sql = "CREATE TABLE CAR (" +
                    "ID INT PRIMARY KEY AUTO_INCREMENT, " +
                    "NAME VARCHAR UNIQUE NOT NULL," +
                    "COMPANY_ID INT NOT NULL," +
                    "CONSTRAINT fk_company FOREIGN KEY (COMPANY_ID) " +
                    "REFERENCES COMPANY(ID)" +
                    ")";
            stmt.executeUpdate(sql);
            System.out.println("Created CAR table.");
        } catch(JdbcSQLSyntaxErrorException se){
            // se.printStackTrace();
            // System.out.println("Table CAR exists.");
        } catch(SQLException se) {
            se.printStackTrace();
        }
    }

    @Override
    public void updateCars(Company company) {
        this.company = company;
        cars = new ArrayList<>();
        String sql = "SELECT ID, NAME FROM CAR WHERE COMPANY_ID = " +
                company.getId() + ";";
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int lastId = rs.getInt("ID");
                String lastName = rs.getString("NAME");
                Car car = new Car(lastId, lastName, company.getId());
                cars.add(car);
            }
        } catch (SQLException e) {
            // e.printStackTrace();
        }
    }

    @Override
    public void updateCars() {
        cars = new ArrayList<>();
        String sql = "SELECT * FROM CAR;";
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int lastId = rs.getInt("ID");
                String lastName = rs.getString("NAME");
                int companyId = rs.getInt("COMPANY_ID");
                Car car = new Car(lastId, lastName, companyId);
                cars.add(car);
            }
        } catch (SQLException e) {
            // e.printStackTrace();
        }
    }

    // @Override
    public void updateCarsNonrented(Company company) {
        this.company = company;
        cars = new ArrayList<>();
        String sql = "SELECT ID, NAME FROM CAR WHERE " +
                "COMPANY_ID = " + company.getId() +
                "AND " +
                "ID NOT IN (" +
                "SELECT RENTED_CAR_ID FROM CUSTOMER " +
                "WHERE RENTED_CAR_ID is NOT NULL" +
                ")" +
                ";";
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int lastId = rs.getInt("ID");
                String lastName = rs.getString("NAME");
                Car car = new Car(lastId, lastName, this.company.getId());
                cars.add(car);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addCar(String name) {
        String sql = "INSERT INTO CAR (NAME, COMPANY_ID) VALUES ('"
                + name + "', "
                + company.getId()
                + ")";
        try {
            stmt.executeUpdate(sql);
            System.out.println("The car was added!");
        } catch (JdbcSQLIntegrityConstraintViolationException e1) {
            System.out.println("The car already exists.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println();
    }

    @Override
    public int size(){
        return cars.size();
    }

    public String toString() {
        String outputString;
        outputString = "";
        for (int i=0; i<cars.size(); i++) {
            outputString = outputString + (i+1) + ". " + cars.get(i) + "\n";
        }
        return outputString;
    }

    @Override
    public Car get(int id) {
        return cars.get(id-1);
    }
}