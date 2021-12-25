package carsharing;

import java.sql.*;
import java.util.List;
import java.util.Scanner;

public class Main {

    static final String USER = "";
    static final String PASS = "";
    static Connection connection = null;
    static Statement stmt = null;
    static String databaseFileName = "~/IdeaProjects/Car Sharing/Car Sharing/task/src/carsharing/db/carsharing";

    public static void main(String[] args) {

        if (args.length>0 && args[0].equals('-' + "databaseFileName"))
            databaseFileName = "~/IdeaProjects/Car Sharing/Car Sharing/task/src/carsharing/db/"+args[1];

        openConnection();

        CustomerDaoImp CUSTOMER = new CustomerDaoImp(stmt);
        CompanyDaoImp COMPANY = new CompanyDaoImp(stmt);
        CarDaoImp CAR = new CarDaoImp(stmt);

        Scanner scan = new Scanner(System.in);

        int LogIn;

        do {
            System.out.println("1. Log in as a manager");
            System.out.println("2. Log in as a customer");
            System.out.println("3. Create a customer");
            System.out.println("0. Exit");
            System.out.print("> ");
            LogIn = scan.nextInt();

            if (LogIn == 1){ // 1. Log in as a manager
                int managerCompanyOptions;

                do {
                    System.out.println("1. Company list");
                    System.out.println("2. Create a company");
                    System.out.println("0. Back");
                    System.out.print("> ");
                    managerCompanyOptions = scan.nextInt();

                    if (managerCompanyOptions == 1) {
                        List<Company> companies = COMPANY.getAll();
                        if (companies.size()==0) {
                            System.out.println("The company list is empty!");
                        } else {
                            System.out.println("Choose the company:");
                            for (int i = 0; i < companies.size(); i++) {
                                System.out.println((i+1) + ". " + companies.get(i));
                            }
                            System.out.println("0. Back");
                            System.out.print("> ");
                            int selectedCompany = scan.nextInt();

                            if (selectedCompany>0 & selectedCompany<=companies.size()) {
                                Company company = companies.get(selectedCompany-1);
                                System.out.println("'" + company.getName() + "' company");
                                int managerCarOptions;
                                do {
                                    System.out.println("1. Car list");
                                    System.out.println("2. Create a car");
                                    System.out.println("0. Back");
                                    System.out.print("> ");
                                    managerCarOptions = scan.nextInt();

                                    if (managerCarOptions == 1) { // 1. Car list
                                        List<Car> cars = CAR.getAll(company);
                                        if (cars.size() == 0) {
                                            System.out.println("The car list is empty!");
                                        } else {
                                            System.out.println("Car list:");
                                            for (int i = 0; i < cars.size(); i++) {
                                                System.out.println((i + 1) + ". " + cars.get(i));
                                            }
                                        }
                                    } else if (managerCarOptions == 2) { // 2. Create a car
                                        scan.nextLine();
                                        System.out.println("Enter the car name:");
                                        System.out.print("> ");
                                        String name = scan.nextLine();
                                        CAR.addCar(name, company);
                                    }
                                } while (managerCarOptions != 0); // 0. Back
                            }
                        }
                    } else if (managerCompanyOptions == 2) {
                        scan.nextLine();
                        System.out.println("Enter the company name:");
                        System.out.print("> ");
                        String name = scan.nextLine();
                        COMPANY.addCompany(name);
                    }
                } while (managerCompanyOptions != 0);

            } else if (LogIn == 2) { // 2. Log in as a customer
                List<Customer> customers = CUSTOMER.getAll();
                if (customers.size()==0) {
                    System.out.println("The customer list is empty!");
                } else {
                    System.out.println("Customer list:");
                    for (int i=0; i<customers.size(); i++) {
                        System.out.println((i+1) + ". " + customers.get(i));
                    }
                    System.out.println("0. Back");
                    System.out.print("> ");
                    int customerId = scan.nextInt();

                    if (customerId>0 & customerId<=customers.size()) {
                        int customerOptions;
                        do {
                            customers = CUSTOMER.getAll();
                            Customer customer = customers.get(customerId - 1);

                            System.out.println("1. Rent a car");
                            System.out.println("2. Return a rented car");
                            System.out.println("3. My rented car");
                            System.out.println("0. Back");
                            System.out.print("> ");
                            customerOptions = scan.nextInt();

                            if (customerOptions == 1) { // 1. Rent a car
                                if (customer.hasCar()) {
                                    System.out.println("You've already rented a car!");
                                } else {
                                    System.out.println("Choose a company:");
                                    List<Company> companies = COMPANY.getAll();
                                    if (companies.size() == 0) {
                                        System.out.println("The company list is empty!");
                                    } else {
                                        for (int i = 0; i < companies.size(); i++) {
                                            System.out.println((i + 1) + ". " + companies.get(i));
                                        }
                                    }
                                    System.out.println("0. Back");
                                    System.out.print("> ");
                                    int selectedCompany = scan.nextInt();

                                    if (selectedCompany > 0 & selectedCompany <= companies.size()) {
                                        Company company = companies.get(selectedCompany - 1);
                                        List<Car> avalibleCars = CAR.getAvalible(company);
                                        if (avalibleCars.size() == 0) {
                                            System.out.println("The car list is empty!");
                                        } else {
                                            System.out.println("Choose a car:");
                                            for (int i = 0; i < avalibleCars.size(); i++) {
                                                System.out.println((i + 1) + ". " + avalibleCars.get(i));
                                            }
                                            System.out.println("0. Back");
                                            System.out.print("> ");
                                            int selectedCar = scan.nextInt();
                                            if (selectedCar > 0 & selectedCar <= avalibleCars.size()) {
                                                Car car = avalibleCars.get(selectedCar - 1);
                                                CUSTOMER.rentCar(customer, car);
                                            }

                                        }
                                    }
                                }
                            } else if (customerOptions == 2) { // 2. Return a rented car
                                if (customer.hasCar()) {
                                    CUSTOMER.returnCar(customer);
                                } else {
                                    System.out.println("You didn't rent a car!");
                                }
                            } else if (customerOptions == 3) { // 3. My rented car
                                if (customer.hasCar()) {
                                    Car car = CAR.get(customer);
                                    Company company = COMPANY.get(car);
                                    System.out.println("Your rented car:");
                                    System.out.println(car);
                                    System.out.println("Company:");
                                    System.out.println(company);
                                } else {
                                    System.out.println("You didn't rent a car!");
                                }
                            }
                        } while (customerOptions != 0); // 0. Back
                    }
                }
            } else if (LogIn == 3) { // 3. Create a customer
                scan.nextLine();
                System.out.println("Enter the customer name:");
                System.out.print("> ");
                String name = scan.nextLine();
                CUSTOMER.addCustomer(name);
            }
        } while (LogIn != 0); // 0. Exit

        closeConnection();
    }

    static void openConnection(){
        try {
            connection = DriverManager.getConnection("jdbc:h2:" + databaseFileName, USER, PASS);
            connection.setAutoCommit(true);
            System.out.println("Connected to database.");
            stmt = connection.createStatement();
        } catch(SQLException se) {
            se.printStackTrace();
        }
    }

    static void closeConnection(){
        try{
            if(stmt!=null)
                stmt.close();
        } catch(SQLException se2) {
            se2.printStackTrace();
        }

        try {
            if(connection!=null)
                connection.close();
        } catch(SQLException se){
            se.printStackTrace();
        }
    }

}