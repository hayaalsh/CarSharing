package carsharing;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.h2.jdbc.JdbcSQLSyntaxErrorException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CompanyDaoImp implements CompanyDao {

    Statement stmt;

    public CompanyDaoImp(Statement stmt){
        this.stmt = stmt;
        String sql = "CREATE TABLE COMPANY (" +
                "ID INT PRIMARY KEY AUTO_INCREMENT, " +
                "NAME VARCHAR UNIQUE NOT NULL" +
                ")";
        try {
            stmt.executeUpdate(sql);
            System.out.println("Created COMPANY table.");
        } catch(JdbcSQLSyntaxErrorException se){
            System.out.println("Table COMPANY exists.");
        } catch(SQLException se) {
            se.printStackTrace();
        }
    }

    @Override
    public Company get(Car car) {
        Company company = null;
        String sql = "SELECT * FROM COMPANY WHERE ID = " + car.getCompanyId() + ";";
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String name = rs.getString("NAME");
                company = new Company(car.getCompanyId(), name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return company;
    }

    @Override
    public List<Company> getAll() {
        List<Company> companies = new ArrayList<>();
        String sql = "SELECT * FROM COMPANY";
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int lastId = rs.getInt("ID");
                String lastName = rs.getString("NAME");
                Company company = new Company(lastId, lastName);
                companies.add(company);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return companies;
    }

    @Override
    public void addCompany(String name) {
        String sql = "INSERT INTO COMPANY (NAME) VALUES ('" + name + "')";
        try {
            stmt.executeUpdate(sql);
            System.out.println("The company was created!");
        } catch (JdbcSQLIntegrityConstraintViolationException e1) {
            System.out.println("The company already exists.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println();
    }
}