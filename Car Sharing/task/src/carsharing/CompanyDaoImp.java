package carsharing;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.h2.jdbc.JdbcSQLSyntaxErrorException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CompanyDaoImp implements CompanyDao {

    List<Company> companies;
    Statement stmt;

    public CompanyDaoImp(Statement stmt){
        this.stmt = stmt;
        try {
            String sql = "CREATE TABLE COMPANY (" +
                    "ID INT PRIMARY KEY AUTO_INCREMENT, " +
                    "NAME VARCHAR UNIQUE NOT NULL" +
                    ")";
            stmt.executeUpdate(sql);
            System.out.println("Created COMPANY table.");
        } catch(JdbcSQLSyntaxErrorException se){
            /*try {
                sql = "ALTER TABLE COMPANY ALTER COLUMN ID INT AUTO_INCREMENT";
                stmt.executeUpdate(sql);
                sql = "ALTER TABLE COMPANY ADD PRIMARY KEY (ID)";
                stmt.executeUpdate(sql);
                System.out.println("Altered ID column to be PRIMARY KEY and AUTO_INCREMENT.");
                sql = "ALTER TABLE COMPANY ADD UNIQUE (NAME)";
                stmt.executeUpdate(sql);
                sql = "ALTER TABLE COMPANY ALTER COLUMN NAME SET NOT NULL";
                stmt.executeUpdate(sql);
                System.out.println("Altered NAME column to be UNIQUE and NOT NULL.");
            } catch (SQLException se2){
            }*/
            // System.out.println("Table COMPANY exists.");
        } catch(SQLException se) {
            se.printStackTrace();
        }
    }

    @Override
    public Company get(int id){
        Company company = companies.get(id-1);
        return company;
    }

    @Override
    public void updateCompanies() {
        companies = new ArrayList<>();
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
    }

    @Override
    public int size() {
        return companies.size();
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

    public String toString() {
        String outputString ="";
        for (int i = 0; i < companies.size(); i++) {
            outputString = outputString + (i + 1) + ". " + companies.get(i) + "\n";
        }
        return outputString;
    }
}