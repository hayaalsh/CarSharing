package carsharing;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.h2.jdbc.JdbcSQLSyntaxErrorException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CarDaoImp implements CarDao {

    Statement stmt;

    public CarDaoImp(Statement stmt){
        this.stmt = stmt;
        String sql = "CREATE TABLE CAR (" +
                "ID INT PRIMARY KEY AUTO_INCREMENT, " +
                "NAME VARCHAR UNIQUE NOT NULL," +
                "COMPANY_ID INT NOT NULL," +
                "CONSTRAINT fk_company FOREIGN KEY (COMPANY_ID) " +
                "REFERENCES COMPANY(ID)" +
                ")";
        try {
            stmt.executeUpdate(sql);
            System.out.println("Created CAR table.");
        } catch(JdbcSQLSyntaxErrorException se){
            System.out.println("Table CAR exists.");
        } catch(SQLException se) {
            se.printStackTrace();
        }
    }

    @Override
    public List<Car> getAll(Company company) {
        List<Car> cars = new ArrayList<>();
        String sql = "SELECT * FROM CAR " +
                "WHERE COMPANY_ID = " + company.getId() +";";
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
            e.printStackTrace();
        }
        return cars;
    }

    @Override
    public List<Car> getAvalible(Company company) {
        List<Car> cars = new ArrayList<>();
        String sql = "SELECT ID, NAME FROM CAR " +
                "WHERE COMPANY_ID = " + company.getId() +
                "AND ID NOT IN (" +
                "SELECT RENTED_CAR_ID FROM CUSTOMER " +
                "WHERE RENTED_CAR_ID is NOT NULL" +
                ");";
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int lastId = rs.getInt("ID");
                String lastName = rs.getString("NAME");
                Car car = new Car(lastId, lastName, company.getId());
                cars.add(car);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }

    @Override
    public void addCar(String name, Company company) {
        String sql = "INSERT INTO CAR (NAME, COMPANY_ID) VALUES ('"
                + name + "', " + company.getId() + ")";
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
    public Car get(Customer owner) {
        Car car = null;
        String sql = "SELECT NAME, COMPANY_ID FROM CAR WHERE ID = " + owner.getCarId() + ";";
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String name = rs.getString("NAME");
                int companyId = rs.getInt("COMPANY_ID");
                car = new Car(owner.getCarId(), name, companyId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return car;
    }
}